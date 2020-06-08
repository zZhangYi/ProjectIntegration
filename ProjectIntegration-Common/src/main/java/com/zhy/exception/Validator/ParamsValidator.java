package com.zhy.exception.Validator;

import com.zhy.utils.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

/**
 * 参数合法性校验类
 *
 * @version 1.0.0
 */
public class ParamsValidator {

    /**
     * 校验参数是否为null
     *
     * @param reference-字符
     * @param errorCode-错误码
     * @param errorMessage -错误信息
     *
     */
    public static void checkNotNull(Object reference, int errorCode, String errorMessage) {
        if (reference == null) {
            throw new ParamsCheckException(errorCode, errorMessage);
        }
    }

    /**
     * 校验集合是否为空
     *
     * @param collection- 集合
     * @param errorCode-错误码
     * @param errorMessage -错误信息
     *
     */
    public static <T> void checkListNotEmpty(Collection<T> collection, int errorCode, String errorMessage) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ParamsCheckException(errorCode, errorMessage);
        }
    }

    /**
     * 校验参数是否为空
     *
     * @param reference-字符
     * @param errorCode-错误码
     * @param errorMessage -错误信息
     *
     */
    public static void checkNotEmpty(String reference, int errorCode, String errorMessage) {
        if (StringUtils.isEmpty(reference)) {
            throw new ParamsCheckException(errorCode, errorMessage);
        }
    }

    /**
     *校验是否为汉字
     * @param reference-字符
     * @param errorCode-错误码
     * @param errorMessage -错误信息
     *
     */
    public static void checkNameZn(String reference, int errorCode, String errorMessage) {
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < reference.length(); i++) {
            // 获取一个字符
            String temp = reference.substring(i, i + 1);
            // 判断是否为中文字符
            if (!temp.matches(chinese)) {
                if (temp.equals("·")) {
                    continue;
                }
                throw new ParamsCheckException(errorCode, errorMessage);
            }
        }
    }

    /**
     * 校验字符串是否在枚举中
     * @param reference-字符
     * @param enums-数组
     * @param errorCode-错误码
     * @param errorMessage-错误信息
     */
    public static void checkEnum(String reference, String[] enums, int errorCode, String errorMessage) {
        boolean passFlag = false;
        // 循环比较
        for (String enumsVal : enums) {
            if (enumsVal.equals(reference)) {
                passFlag = true;
                break;
            }
        }
        if (!passFlag) {
            throw new ParamsCheckException(errorCode, errorMessage);
        }
    }

    /**
     * 校验数字
     * @param reference-字符
     * @param errorCode-错误码
     * @param errorMessage -错误信息
     *
     */
    public static void checkNumeric(String reference, int errorCode, String errorMessage) {
        // 校验是否为空
        checkNotEmpty(reference, errorCode, errorMessage);
        // 校验是否是数字
        if (!StringUtils.isNumeric(reference)) {
            throw new ParamsCheckException(errorCode, errorMessage);
        }
    }

    /**
     * 校验身份证号
     *
     * @param idno-证件号
     * @param birthday-生日
     * @param sex-性别
     * @param errorCode-错误码
     * @param errorMessage-错误
     *
     */
    public static void checkIDCard(String idno, Date birthday, String sex, int errorCode, String errorMessage) throws RuntimeException {
        HashMap<String, String> hashMap = IDCardValidator.validate(idno);
        String idCardStatus = hashMap.get("status");

        if ("0".equals(idCardStatus)) {
            throw new ParamsCheckException(errorCode, errorMessage + ". illegal id card.");
        }

        int idCardYear = Integer.parseInt(hashMap.get("year"));
        int idCardMonth = Integer.parseInt(hashMap.get("month"));
        int idCardDay = Integer.parseInt(hashMap.get("day"));
        int idCardSex = Integer.parseInt(hashMap.get("sex")) % 2 == 0 ? 1 : 0;

        // 拆离生日
        int year = Integer.parseInt(DateUtil.getYear(birthday));
        int month = Integer.parseInt(DateUtil.getMonth(birthday));
        int day = Integer.parseInt(DateUtil.getDay(birthday));

        if (idCardYear != year) {
            throw new ParamsCheckException(errorCode, errorMessage + ". the birthday does not match the id card.");
        }
        if (idCardMonth != month) {
            throw new ParamsCheckException(errorCode, errorMessage + ". the birthday does not match the id card.");
        }
        if (idCardDay != day) {
            throw new ParamsCheckException(errorCode, errorMessage + ". the birthday does not match the id card.");
        }
        if (idCardSex != Integer.parseInt(sex)) {
            throw new ParamsCheckException(errorCode, errorMessage + ". the sex does not match the id card.");
        }
    }

}