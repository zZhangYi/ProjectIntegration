package com.zhy.utils;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * 反射工具
 */
public class ReflectionUtil {

    /**
     * map转DTO
     *
     * @param source
     * @param target
     * @return void
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void objectToMap(Object source, Map target) {
        try {
            target.putAll(ClassInvokeUtil.initCommonDTO(source));
        } catch (Exception e) {
            throw new RuntimeException("fail to  mapToObject!");
        }
    }

    /**
     * dto转Map
     *
     * @param source
     * @param target
     * @return void
     */
    @SuppressWarnings("rawtypes")
    public static void mapToObject(Map source, Object target) {
        try {
            ClassInvokeUtil.objectToMap(source, target);
        } catch (Exception e) {
            throw new RuntimeException("fail to  objectToMap!");
        }
    }

    /**
     * 把source中的内容Copy到指定target
     *
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, null);
    }

    /**
     * 把src中的内容Copy到指定target，但是忽略指定的属性
     *
     * @param source           源对象
     * @param target           目标对象
     * @param ignoreProperties 指定忽略的属性
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void copyProperties(Object source, Object target, String[] ignoreProperties) {
        if (null == source || null == target) {
            return;
        }
        Set excludes = new HashSet();
        if (null != ignoreProperties) {
            excludes.addAll(Arrays.asList(ignoreProperties));
        }
        copyProperties(source, target, excludes, false);
    }

    /**
     * 方法功能描述
     *
     * @param source     源对象
     * @param target     目标对象
     * @param properties 参数
     * @param included   参数
     */

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void copyProperties(Object source, Object target, Set properties,
                                       boolean included) {
        try {
            BeanInfo sourceInfo = Introspector.getBeanInfo(source.getClass());
            Map targetDescrs = new HashMap();
            // init
            {
                BeanInfo targetInfo = Introspector.getBeanInfo(target.getClass());
                PropertyDescriptor[] targetPds = targetInfo.getPropertyDescriptors();
                for (int i = 0; i < targetPds.length; i++) {
                    targetDescrs.put(targetPds[i].getName(), targetPds[i]);
                }
            }
            PropertyDescriptor[] pds = sourceInfo.getPropertyDescriptors();
            Object[] params = new Object[1];

            // collect some stat info for debug later...

            for (int i = 0; i < pds.length; i++) {
                String property = pds[i].getName();
                if (included) {
                    if (!properties.contains(property)) {
                        continue;
                    }
                } else {
                    if (properties.contains(property)) {
                        continue;
                    }
                }
                PropertyDescriptor targetPD = (PropertyDescriptor) targetDescrs.get(property);
                if (null != targetPD) {
                    Method readMethod = pds[i].getReadMethod();
                    Method writeMethod = targetPD.getWriteMethod();
                    if (null == readMethod || null == writeMethod) {
                        // warning: no read or write
                    } else {
                        try {
                            Type[] ps = writeMethod.getGenericParameterTypes();
                            if (ps == null || ps.length != 1) {
                                continue;
                            }
                            Object propSrc = readMethod.invoke(source);
                            if (ps[0] instanceof ParameterizedType) {
                                ParameterizedType writeType = (ParameterizedType) (ps[0]);
                                if (!readMethod.getGenericReturnType().equals(writeType)) {
                                    throw new RuntimeException("Property:" + property
                                            + " have different generic type in source and target!");
                                }
                                params[0] = convertIfNeeded((Class) (writeType.getRawType()),
                                        propSrc, pds[i].getName());
                            } else {
                                params[0] = convertIfNeeded((Class) (ps[0]), propSrc,
                                        pds[i].getName());
                            }
                            writeMethod.invoke(target, params);
                        } catch (IllegalArgumentException e) {
                            // warning: failed read or write
                        } catch (IllegalAccessException e) {
                            // warning: failed read or write
                        } catch (InvocationTargetException e) {
                            // warning: failed read or write
                        }
                    }
                } else {
                    // warning: no target property
                }
            }
        } catch (IntrospectionException e) {
            handleEx(e);
        }
    }

    /**
     * 设置指定对象的属性
     *
     * @param target   对象
     * @param property 属性名
     * @param value    新的属性值
     * @throws RuntimeException
     */
    @SuppressWarnings("rawtypes")
    public static void setProperty(Object target, String property, Object value) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < pds.length; i++) {
                if (pds[i].getName().equals(property)) {
                    Method method = pds[i].getWriteMethod();
                    if (null == method) {
                        throw new IllegalArgumentException("No WriteMethod of property: "
                                + property);
                    }
                    Class[] ps = method.getParameterTypes();
                    if (ps == null || ps.length != 1) {
                        continue;
                    }
                    Object[] params = new Object[1];
                    // method.invoke(source, params);
                    params[0] = convertIfNeeded(ps[0], value, pds[i].getName());
                    method.invoke(target, params);

                    return;
                }
            }
            throw new IllegalArgumentException("No Such property: " + property);
        } catch (IntrospectionException e) {
            handleEx(e);
        } catch (IllegalAccessException e) {
            handleEx(e);
        } catch (InvocationTargetException e) {
            handleEx(e);
        }

        throw new RuntimeException("No Such property: " + property);
    }

    /**
     * 方法功能描述
     *
     * @param type     参数
     * @param value    参数
     * @param property 参数
     * @return Object 返回
     */
    @SuppressWarnings("rawtypes")
    private static Object convertIfNeeded(Class type, Object value, String property) {
        if (null != value) {
            if (value instanceof Number) {
                return convertNumber(type, (Number) value);
            }
            return value;
        }
        // 处理原始类型
        if (Integer.class == type || Integer.TYPE == type) {
            return new Integer(0);
        }
        if (Short.class == type || Short.TYPE == type) {
            return new Short((short) 0);
        }
        if (Long.class == type || Long.TYPE == type) {
            return new Long(0);
        }
        if (Float.class == type || Float.TYPE == type) {
            return new Float(0);
        }
        if (Double.class == type || Double.TYPE == type) {
            return new Double(0);
        }
        if (Byte.class == type || Byte.TYPE == type) {
            return new Integer(0);
        }
        if (Character.class == type || Character.TYPE == type) {
            return new Character('\0');
        }
        if (Boolean.class == type || Boolean.TYPE == type) {
            return Boolean.FALSE;
        }

        return value;
    }

    @SuppressWarnings("rawtypes")
    private static Number convertNumber(Class destType, Number value) {
        if (destType == BigDecimal.class) {
            return new BigDecimal(value.toString());
        }
        if (destType == BigInteger.class) {
            return new BigInteger(value.toString());
        }
        if (destType == Long.class || destType == Long.TYPE) {
            return new Long(value.longValue());
        }
        if (destType == Integer.class || destType == Integer.TYPE) {
            return new Integer(value.intValue());
        }
        if (destType == Short.class || destType == Short.TYPE) {
            return new Short(value.shortValue());
        }
        if (destType == Float.class || destType == Float.TYPE) {
            return new Float(value.floatValue());
        }
        if (destType == Double.class || destType == Double.TYPE) {
            return new Double(value.doubleValue());
        }
        if (destType == Byte.class || destType == Byte.TYPE) {
            return new Byte(value.byteValue());
        }

        return value;
    }

    /**
     * 方法功能描述
     *
     * @param ex 异常
     */
    private static void handleEx(IntrospectionException ex) {
        throw new RuntimeException("Access Error", ex);
    }

    /**
     * 方法功能描述
     *
     * @param ex 异常
     */
    private static void handleEx(IllegalAccessException ex) {
        throw new RuntimeException("Access Error", ex);
    }

    /**
     * 方法功能描述
     *
     * @param ex 异常
     */
    private static void handleEx(InvocationTargetException ex) {
        throw new RuntimeException("Invocation Error", ex);
    }

    // static {
    // DateConverter dc = new DateConverter();
    // dc.setUseLocaleFormat(true);
    // dc.setPatterns(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" });
    // ConvertUtils.register(dc, Date.class);
    // }

    /**
     * 调用Getter方法.
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static Object invokeGetterMethod(Object target, String propertyName) throws Exception {
        String getterMethodName = "get" + StringUtil.capitalize(propertyName);
        return invokeMethod(target, getterMethodName, new Class[]{}, new Object[]{});
    }

    /**
     * 调用Setter方法.使用value的Class来查找Setter方法.
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static void invokeSetterMethod(Object target, String propertyName, Object value)
            throws Exception {
        invokeSetterMethod(target, propertyName, value, null);
    }

    /**
     * 调用Setter方法.
     *
     * @param propertyType 用于查找Setter方法,为空时使用value的Class替代.
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static void invokeSetterMethod(Object target, String propertyName, Object value,
                                          Class<?> propertyType) throws Exception {
        Class<?> type = propertyType != null ? propertyType : value.getClass();
        String setterMethodName = "set" + StringUtil.capitalize(propertyName);
        invokeMethod(target, setterMethodName, new Class[]{type}, new Object[]{value});
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     *
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    public static Object getFieldValue(final Object object, final String fieldName)
            throws Exception {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName
                    + "] on target [" + object + "]");
        }

        makeAccessible(field);

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     *
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value)
            throws Exception {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName
                    + "] on target [" + object + "]");
        }

        makeAccessible(field);

        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符.
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static Object invokeMethod(final Object object, final String methodName,
                                      final Class<?>[] parameterTypes, final Object[] parameters) throws Exception {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName
                    + "] on target [" + object + "]");
        }
        method.setAccessible(true);
        return method.invoke(object, parameters);
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField.
     * <p>
     * 如向上转型到Object仍无法找到, 返回null.
     *
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    protected static Field getDeclaredField(final Object object, final String fieldName)
            throws Exception {
        if (object == null) {
            return null;
        }
        if (StringUtil.isBlank(fieldName)) {
            return null;
        }
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            /*
             * 优化向上转型查询子类属性不存在而直接抛异常的bug
             */
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                continue;
            }
        }
        return null;
    }

    /**
     * 强行设置Field可访问.
     */
    protected static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers())
                || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod.
     * <p>
     * 如向上转型到Object仍无法找到, 返回null.
     */
    protected static Method getDeclaredMethod(Object object, String methodName,
                                              Class<?>[] parameterTypes) {
        if (object == null) {
            return null;
        }
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {// NOSONAR
                // Method不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg. public UserDao
     * extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     * <p>
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings({"rawtypes"})
    public static Class getSuperClassGenricType(final Class clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 提取集合中的对象的属性(通过getter函数), 组合成List.
     *
     * @param collection   来源集合.
     * @param propertyName 要提取的属性名.
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List convertElementPropertyToList(final Collection collection,
                                                    final String propertyName) throws Exception {
        List list = new ArrayList();

        for (Object obj : collection) {
            list.add(PropertyUtils.getProperty(obj, propertyName));
        }

        return list;
    }

    /**
     * 提取集合中的对象的属性(通过getter函数), 组合成由分割符分隔的字符串.
     *
     * @param collection
     *            来源集合.
     * @param propertyName
     *            要提取的属性名.
     * @param separator
     *            分隔符.
     */
    // @SuppressWarnings("unchecked")
    // public static String convertElementPropertyToString(final Collection
    // collection, final String propertyName,
    // final String separator) {
    // List list = convertElementPropertyToList(collection, propertyName);
    // return StringUtil.join(list, separator);
    // }

    /**
     * 转换字符串到相应类型.
     *
     * @param value  待转换的字符串
     * @param toType 转换目标类型
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertStringToObject(String value, Class<T> toType) {
        return (T) ConvertUtils.convert(value, toType);
    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        return convertReflectionExceptionToUnchecked(null, e);
    }

    public static RuntimeException convertReflectionExceptionToUnchecked(String desc, Exception e) {
        desc = (desc == null) ? "Unexpected Checked Exception." : desc;
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(desc, e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(desc, ((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException(desc, e);
    }

    /**
     * 得到新的对象
     *
     * @param <T>
     * @param cls
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static final <T> T getNewInstance(Class<T> cls) throws Exception {
        return cls.newInstance();
    }

    /**
     * 根据类型获得对象
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public static final Object getObjectByClassName(String clazz) throws Exception {
        return Class.forName(clazz).newInstance();
    }

    /**
     * 将对象的所有null字段，赋值为empty(比如String为"").
     *
     * @param obj
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static final void fieldNullToEmpty(Object obj) {
        try {
            BeanInfo sourceInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] pds = sourceInfo.getPropertyDescriptors();

            for (int i = 0; i < pds.length; i++) {
                Method readMethod = pds[i].getReadMethod();
                Class propClazz = pds[i].getPropertyType();
                if (propClazz.isPrimitive()) {
                    continue;
                }
                Object propPre = readMethod.invoke(obj);
                if (propPre != null) {
                    continue;
                }
                Method writeMethod = pds[i].getWriteMethod();
                if (propClazz == String.class) {
                    writeMethod.invoke(obj, "");
                } else if (Number.class.isAssignableFrom(propClazz)) {
                    writeMethod
                            .invoke(obj, propClazz.getConstructor(String.class).newInstance("0"));
                } else if (!propClazz.isInterface()) {
                    writeMethod.invoke(obj, propClazz.newInstance());
                } else {
                    if (List.class.isAssignableFrom(propClazz)) {
                        writeMethod.invoke(obj, new ArrayList());
                    } else if (Set.class.isAssignableFrom(propClazz)) {
                        writeMethod.invoke(obj, new HashSet());
                    } else if (Map.class.isAssignableFrom(propClazz)) {
                        writeMethod.invoke(obj, new HashMap());
                    }
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
