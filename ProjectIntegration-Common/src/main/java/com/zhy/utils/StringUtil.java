package com.zhy.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串工具类
 *
 * @version 1.0
 */
public class StringUtil extends StringUtils {

    /**
     * 存放国标一级汉字不同读音的起始区位码
     */
    static final int[] secPosValueList = {1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787,
            3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925, 5249,
            5600};

    /**
     * 存放国标一级汉字不同读音的起始区位码对应读音
     */
    static final char[] firstLetter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z'};

    /**
     * 去除字符串中的空格、回车、换行符、制表符
     *
     * @param str 源字符串
     * @return 处理后的字符串
     */
    public static String replaceBlank(String str) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(str);
        String after = m.replaceAll("");
        return after;
    }

    /**
     * 得到c在s中的出现的索引列表
     *
     * @param s 原字符串
     * @param c 子字符串
     * @return c在s中出现的索引列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List getIndexList(String s, String c) {
        int x = s.indexOf(c);
        int replaceLenght = 0;
        List list = new ArrayList();
        while (x != -1) {
            list.add(x + "");
            s = s.replaceFirst(c, "");
            replaceLenght = replaceLenght + c.length();
            x = s.indexOf(c);
            if (x != -1) {
                x = s.indexOf(c) + replaceLenght;
            }
        }
        return list;
    }

    /**
     * 判断是否为数字（包括小数和整数）
     *
     * @param str 要判断的字符串
     * @return true/flase（是/否）
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]*\\.?[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 获取一个字符串的拼音码
     *
     * @param oriStr 要操作的字符串
     * @return 拼音码
     */
    public static String getFirstLetter(String oriStr) {
        String str = oriStr.toLowerCase();
        StringBuffer buffer = new StringBuffer();
        char ch;
        char[] temp;
        for (int i = 0; i < str.length(); i++) {
            // 依次处理str中每个字符
            ch = str.charAt(i);
            temp = new char[]{ch};
            byte[] uniCode = new String(temp).getBytes();
            if (uniCode[0] < 128 && uniCode[0] > 0) {
                // 非汉字
                buffer.append(temp);
            } else {
                buffer.append(convert(uniCode));
            }
        }
        return buffer.toString();
    }

    /**
     * 获取一个汉字的拼音首字母
     *
     * @param bytes 要操作的字符串
     * @return 拼音首字母
     */
    public static char convert(byte[] bytes) {
        char result = '-';
        int secPosValue = 0;
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= 160;
        }
        secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= secPosValueList[i] && secPosValue < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }
        return result;
    }

    /**
     * 比较两个字符串的大小,按拼音顺序
     *
     * @param str1 要操作的字符串
     * @param str2 要操作的字符串
     * @return -1:表示str1<str2 ; 1:表示str1>str2 ;0:表示str1=str2
     */
    public static int compareString(String str1, String str2) {
        int m = str1.length();
        int n = str2.length();
        for (int i = 0; i < m; i++) {
            if (i < n) {
                if (str1.charAt(i) > str2.charAt(i)) {
                    return 1;
                } else if (str1.charAt(i) == str2.charAt(i)) {
                    if (m == n && i + 1 == m) {
                        return 0;
                    } else {
                        continue;
                    }
                } else {
                    return -1;
                }
            } else {
                return 1;
            }
        }
        return -1;
    }

    /**
     * 替换字符串
     *
     * @param resource 要操作的字符串
     * @param target   要替换的目标子串
     * @param result   用来替换目标子串的字符串
     * @return 替换后的字符串
     */
    public static String replaceAllStr(String resource, String target, String result) {
        resource = resource.replaceAll(target, result);
        return resource;
    }

    /**
     * 将object 转为 string value并去空格 若object为null返回空字串
     *
     * @param value
     * @return 转换后的字符串
     */
    public static String getString(Object value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value).trim();
    }

    /**
     * 将字符串转为整形值
     *
     * @param value
     * @return 转换后的int数字
     */
    public static int parseStringToInt(String value) {
        try {
            if (value == null || value.trim().equals("")) {
                return 0;
            }
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * 根据分割符','，将输入字符串转换为String数组
     *
     * @param value
     * @return
     */

    public static String arrayToCSV(String[] value) {
        return arrayToDelimited(value, ",", true, true);
    }

    /**
     * 根据分割符，将输入字符串转换为String数组,以','分割
     *
     * @param value
     * @return
     */

    public static String arrayToDelimited(Object[] value) {
        return arrayToDelimited(value, ",");
    }

    /**
     * 根据分割符，将输入字符串转换为String数组
     *
     * @param value
     * @param delimiter
     * @return
     */

    public static String arrayToDelimited(Object[] value, String delimiter) {
        return arrayToDelimited(value, delimiter, false, false, false);
    }

    /**
     * 根据分割符，将输入字符串转换为String数组
     *
     * @param value
     * @param delimiter
     * @return
     */

    public static String arrayToDelimited(String[] value, String delimiter) {
        return arrayToDelimited(value, delimiter, true, true);
    }

    /**
     * 根据分割符，将输入字符串转换为String数组
     *
     * @param value
     * @param delimiter
     * @param prepend
     * @param append
     * @return
     */
    public static String arrayToDelimited(String[] value, String delimiter, boolean prepend,
                                          boolean append) {
        return arrayToDelimited(value, delimiter, prepend, append, false);
    }

    /**
     * 根据分割符，将输入字符串转换为String数组
     *
     * @param value
     * @param delimiter
     * @param prepend
     * @param append
     * @param eliminateDuplicates
     * @return
     */
    public static String arrayToDelimited(Object[] value, String delimiter, boolean prepend,
                                          boolean append, boolean eliminateDuplicates) {
        if (delimiter == null)
            delimiter = ",";
        String retVal = null;
        if (value != null) {
            StringBuffer buff = new StringBuffer();
            int length = value.length;
            if (length > 0) {
                if (prepend)
                    buff.append(delimiter);
                boolean isDuplicateValue = false;
                buff.append(delimiter); // Always make sure the buff starts with
                // a delimiter for duplicate checking
                for (int i = 0; i < length; i++) {
                    isDuplicateValue = (eliminateDuplicates ? (buff.indexOf(delimiter + value[i]
                            + delimiter) != -1) : false);
                    if (!isDuplicateValue) {
                        buff.append(value[i]);
                        if (i < length - 1)
                            buff.append(delimiter);
                    }
                }
                buff.deleteCharAt(0); // remove the delimiter added for checking
                // duplicates
                // If the last value is a duplicate value, remove the delimiter
                // added to the end of the string
                if (isDuplicateValue) {
                    buff.deleteCharAt(buff.length() - 1);
                }
                if (append)
                    buff.append(delimiter);
            }
            retVal = buff.toString();
        }
        return retVal;
    }

    /**
     * 根据数组返回String
     *
     * @param array 数组
     * @return
     */
    public static String transformStringByArray(String array[]) {
        StringBuffer buff = new StringBuffer();
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                if (i == 0) {
                    buff.append(array[i]);
                } else {
                    buff.append(",");
                    buff.append(array[i]);
                }
            }
        }
        return buff.toString();
    }

    /**
     * 根据对象返回String
     *
     * @param obj
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String transformStringObject(Object obj) throws Exception {
        StringBuffer buff = new StringBuffer();
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getMethodName = "get" + firstLetter + fieldName.substring(1);
            Method getMethod = cls.getMethod(getMethodName, new Class[]{});
            Object value = getMethod.invoke(obj, new Object[]{});
            buff.append(fieldName);
            buff.append(":");
            if (value instanceof Object[]) {
                buff.append(StringUtil.transformStringByArray((String[]) value));
            } else {
                buff.append(value);
            }
            buff.append("\n");
        }
        return buff.toString();
    }

    public static String replaceParams(String str, Map<String, String> paramsMap) throws Exception {
        Set<String> keySet = paramsMap.keySet();
        if (str.contains("?")) {
            String[] strs = str.split("\\?");
            for (String s : strs) {
                if (s != null && !"".equals(s)) {
                    if (keySet.contains(s)) {
                        str = str.replace("?" + s + "?", paramsMap.get(s));
                    }
                }
            }
        }
        return str;
    }

    /**
     * 依次截取字符串的每个字符，根据字符编码获取其字节数temp_len,byetTotal用以记录每次截取字符后及之前截取字符字节数之和,然后判断
     * byetTotal是否小于等于所需要截取字节长度
     * （length）,如果小于说明还没超过所需要截取字节长度,那么截取字符的长度（charLen）+1，如果
     * byetTotal>length说明已经超出所需要截取的字节长度，此时的charLen就是所需要截取原字符串的长度
     *
     * @param input  需要截取的字符串
     * @param length 需要截取的字节数
     * @return 截取后的字符串
     * @throws UnsupportedEncodingException
     */
    public static String subStringByByteGbk(String input, int length)
            throws UnsupportedEncodingException {

        if (null == input) {
            return null;
        }

        // 字符长度
        int characterNum = input.length();

        // 截取到当前字符时的字节数
        int byetTotal = 0;

        // 应当截取到的字符的长度
        int charLen = 0;

        /**
         * 循环传入字符串比较字节长度和字符长度
         */
        for (int i = 0; i < characterNum; i++) {
            String temp = input.substring(i, i + 1);
            int temp_len = temp.getBytes("GBK").length;
            byetTotal += temp_len;
            if (byetTotal <= length) {
                charLen++;
            } else {
                break;
            }
        }
        return input.substring(0, charLen);
    }

    /**
     * 证件号星号加密
     *
     * @param str
     * @return
     */
    public static String asteriskByIdNo(String str) {
        if (!StringUtils.isBlank(str)) {
            str = str.trim();
            if (str.length() == 18) {
                String star = "";
                for (int i = 0; i < str.length() - 8; i++) {
                    star += "*";
                }
                str = str.substring(0, 7) + star + str.substring(str.length() - 1);
            } else if (str.length() == 15) {
                String star2 = "";
                for (int i = 0; i < str.length() - 4; i++) {
                    star2 += "*";
                }
                str = str.substring(0, 3) + star2 + str.substring(str.length() - 1);
            }
        }
        return str;
    }

    /**
     * 手机号星号加密
     *
     * @param str
     * @return
     */
    public static String asteriskByMobile(String str) {
        if (!StringUtils.isBlank(str)) {
            str = str.trim();
            if (str.length() >= 11) {
                str = str.substring(0, 3) + "*****" + str.substring(str.length() - 3);
            } else {
                str = str.substring(0, 1) + "*****";
            }
        }
        return str;
    }

    /**
     * 账户号星号加密
     *
     * @param str
     * @return
     */
    public static String asteriskByAccountNo(String str) {
        if (!StringUtils.isBlank(str)) {
            str = str.trim();
            if (str.indexOf("@") > 0) {
                String[] strs = str.split("@");
                if (strs[0].length() > 3) {
                    str = strs[0].substring(0, 3) + "***@" + strs[1];
                } else {
                    str = strs[0] + "***@" + strs[1];
                }
            } else {
                String star = "";
                if (str.length() < 4) {
                    for (int i = 0; i < 11 - str.length(); i++) {
                        star += "*";
                    }
                    str = star + str;
                } else if (str.length() >= 4 && str.length() <= 11) {
                    str = "*******" + str.substring(str.length() - 4);
                } else {
                    String star2 = "";
                    for (int i = 0; i < str.length() - 8; i++) {
                        star2 += "*";
                    }
                    str = str.substring(0, 4) + star2 + str.substring(str.length() - 4);
                }
            }
        }
        return str;
    }

    /**
     * 姓名星号加密
     *
     * @param str
     * @return
     */
    public static String asteriskByName(String str) {
        if (!StringUtils.isBlank(str)) {
            str = str.trim();
            if (str.length() >= 3) {
                str = str.substring(0, 1) + "**";
            } else {
                str = str.substring(0, 1) + "*";
            }
        }
        return str;
    }

    /**
     * 【方法功能描述】
     * 子符串首字母小写
     *
     * @return
     * @comment
     * @history
     * @Version 1.0.0
     */
    public static String getFirstLowerCase(String str) {
        if (StringUtil.isNotBlank(str)) {
            String str0 = str.substring(0, 1).toLowerCase();
            return str0.concat(str.substring(1));
        }
        return null;
    }

    /**
     * 【方法功能描述】 获取参数
     *
     * @param obj
     * @param paramPaths
     * @return
     * @throws Exception
     * @comment
     * @history
     * @Version 1.0.0
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<List<?>> getParamValue(Object obj, String paramPaths) throws Exception {
        // 多个参数以','号分割
        String[] paramNameList = StringUtils.split(paramPaths, ",");

        // 各个参数的list集合
        List<List<?>> paramValueList = new ArrayList<List<?>>();

        // 校验参数对象-从根路径到校验参数对象的所有对象List
        Map<Object, ArrayList<Object>> paramToPath = new HashMap<Object, ArrayList<Object>>();

        for (String paramNames : paramNameList) {
            List<Object> params = new ArrayList<Object>();
            Object lastObj = obj;
            String[] param = StringUtils.split(paramNames, ".");
            ArrayList<Object> path = new ArrayList<Object>();
            for (int i = 0; i < param.length; i++) {
                if (obj.getClass().getSimpleName().equalsIgnoreCase(paramNames)) {
                    continue;
                }
                if (lastObj instanceof Collection) {// 容器类型
                    List<Object> tempLastList = new ArrayList<Object>();
                    for (Object oneInColl : (Collection<?>) lastObj) {
                        ArrayList<Object> pathi = null;
                        if (paramToPath.get(oneInColl) != null) {
                            pathi = (ArrayList<Object>) paramToPath.get(oneInColl).clone();
                        }
                        if (pathi == null) {
                            pathi = (ArrayList<Object>) path.clone();
                        }
                        paramToPath.put(oneInColl, (ArrayList<Object>) pathi.clone());
                        pathi.add(oneInColl);
                        Object innerObj = ReflectionUtil.invokeGetterMethod(oneInColl, param[i]);
                        // 支持多层容器类型属性
                        if (innerObj instanceof Collection) {
                            pathi.add(innerObj);
                            tempLastList.addAll((Collection) innerObj);
                            for (Object innerInnerObj : (Collection) innerObj) {
                                paramToPath.put(innerInnerObj, pathi);
                            }
                        } else if (innerObj instanceof Object[]) {
                            pathi.add(innerObj);
                            tempLastList.addAll(Arrays.asList((Object[]) innerObj));
                            for (Object innerInnerObj : (Object[]) innerObj) {
                                paramToPath.put(innerInnerObj, pathi);
                            }
                        } else {
                            tempLastList.add(innerObj);
                            paramToPath.put(innerObj, pathi);
                        }
                    }
                    lastObj = tempLastList;
                } else if (lastObj instanceof Object[]) {// 数组类型
                    List<Object> tempLastList = new ArrayList<Object>();
                    for (Object oneInColl : (Object[]) lastObj) {
                        ArrayList<Object> pathi = null;
                        if (paramToPath.get(oneInColl) != null) {
                            pathi = (ArrayList<Object>) paramToPath.get(oneInColl).clone();
                        }
                        if (pathi == null) {
                            pathi = (ArrayList<Object>) path.clone();
                        }
                        paramToPath.put(oneInColl, (ArrayList<Object>) pathi.clone());
                        pathi.add(oneInColl);
                        Object innerObj = ReflectionUtil.invokeGetterMethod(oneInColl, param[i]);

                        // 支持多层容器类型属性
                        if (innerObj instanceof Collection) {
                            pathi.add(innerObj);
                            tempLastList.addAll((Collection) innerObj);
                            for (Object innerInnerObj : (Collection) innerObj) {
                                paramToPath.put(innerInnerObj, pathi);
                            }
                        } else if (innerObj instanceof Object[]) {
                            pathi.add(innerObj);
                            tempLastList.addAll(Arrays.asList((Object[]) innerObj));
                            for (Object innerInnerObj : (Object[]) innerObj) {
                                paramToPath.put(innerInnerObj, pathi);
                            }
                        } else {
                            paramToPath.put(innerObj, pathi);
                            tempLastList.add(innerObj);
                        }
                    }
                    lastObj = tempLastList;
                } else if (lastObj instanceof Map) {
                    List<Object> tempLastList = new ArrayList<Object>();
                    tempLastList.add(lastObj);
                } else {
                    ArrayList<Object> pathi = null;
                    if (paramToPath.get(lastObj) != null) {
                        pathi = (ArrayList<Object>) paramToPath.get(lastObj).clone();
                    }
                    if (pathi == null) {
                        pathi = path;
                    }
                    pathi.add(lastObj);
                    lastObj = ReflectionUtil.invokeGetterMethod(lastObj, param[i]);
                    paramToPath.put(lastObj, pathi);
                }
            }
            if (lastObj instanceof Collection) {
                if (lastObj instanceof List) {
                    params.add(lastObj);
                } else {
                    params.addAll((Collection) lastObj);
                }
            } else if (lastObj instanceof Object[]) {
                params.addAll(Arrays.asList((Object[]) lastObj));
            } else if (lastObj instanceof Map) {
                params.add(lastObj);
            } else {
                params.add(lastObj);
            }

            paramValueList.add(params);
        }

        // 整理，同路径的对象放到一起
        // 找出数量最多的参数，相同参数个数的，取路径深的
        int maxIndex = -1;
        int maxNum = -1;
        for (int i = 0; i < paramValueList.size(); i++) {
            List<?> paramList = paramValueList.get(i);
            if (paramList.size() > maxNum) {
                maxIndex = i;
                maxNum = paramList.size();
            }
        }

        // 根据paramToPath中各个参数对象路径整理参数
        List<List<?>> rtnParamValueList = new ArrayList<List<?>>(maxNum);
        for (int i = 0; i < maxNum; i++) {
            List<Object> addedParamList = new ArrayList<Object>(paramValueList.size());
            Object maxObj = paramValueList.get(maxIndex).get(i);
            for (int j = 0; j < paramValueList.size(); j++) {
                if (j == maxIndex) {// 最多个数的参数不需要整理
                    addedParamList.add(maxObj);
                } else {// 根据maxObj的路径得到paramValueList.get(j)中合适的对象
                    List<Object> maxObjPath = (ArrayList<Object>) paramToPath.get(maxObj).clone();
                    maxObjPath.add(maxObj);
                    // 找出和maxobj路径最近的对象
                    Object maxLikeObj = null;
                    int maxLikeNum = -1;
                    for (Object curObj : paramValueList.get(j)) {
                        if (curObj == obj) {
                            maxLikeObj = curObj;
                            break;
                        }
                        List<Object> curObjPath = (ArrayList<Object>) paramToPath.get(curObj).clone();
                        curObjPath.add(curObj);
                        // 如果比maxLikeObj关联的path更和maxObj相似
                        int curLikeNum = StringUtil.getSameObjNum(curObjPath, maxObjPath);
                        if (curLikeNum > maxLikeNum) {
                            maxLikeObj = curObj;
                            maxLikeNum = curLikeNum;
                        }
                    }
                    addedParamList.add(maxLikeObj);
                }
            }
            rtnParamValueList.add(addedParamList);
        }

        return rtnParamValueList;
    }

    /**
     * 【方法功能描述】
     * 取两个列表从开始一直相同元素的数量
     *
     * @param l1
     * @param l2
     * @return
     * @comment
     * @history
     * @Version 1.0.0
     */
    private static int getSameObjNum(List<Object> l1, List<Object> l2) {
        if (CollectionUtils.isEmpty(l1)) {
            return 0;
        }
        if (CollectionUtils.isEmpty(l2)) {
            return 0;
        }
        int compareSize = Math.min(l1.size(), l2.size());
        for (int i = 0; i < compareSize; i++) {
            if (l2.get(i) != l1.get(i)) {
                return i;
            }
        }
        return compareSize;
    }

    /**
     * 【方法功能描述】
     * 比较两个字符串是否相同
     *
     * @param
     * @param s2
     * @return boolean
     * @comment
     * @history
     * @Version 1.0.0
     */
    public static boolean isEqual(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return false;
        }
        return s1.equals(s2);
    }
}
