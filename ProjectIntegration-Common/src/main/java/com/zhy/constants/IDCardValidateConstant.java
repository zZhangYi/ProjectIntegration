package com.zhy.constants;

/**
 * 【类或接口功能描述】
 * 身份证验证常量
 *
 * @version 1.0.0
 * @comment
 */
public class IDCardValidateConstant {
	
	/** 身份证号长度必须为18位 */
	public static final String LENGTH_ILLEGAL = "the length of identity number must be 18";
	
	/** 18位身份证号除最后一位外的其它字符必须为数值型 */
	public static final String NUMERIC_ILLEGAL = "all value except last one of the indetity number must be numeric which length is 18";
	
	/** 身份证号出生日期不合法 */
	public static final String BIRTHDAY_ILLEGAL= "the birthday of identity number is illegal";
	
	/** 身份证号月份不合法 */
	public static final String MONTH_ILLEGAL= "the month of identity number is illegal";
	
	/** 身份证号日期不合法 */
	public static final String DATE_ILLEGAL= "the date of identity number is illegal";
	
	/** 身份证号地区编码不合法 */
	public static final String AREA_CODE_ILLEGAL= "the area of identity number is illegal";
	
	/** 身份证号出生日期超过有效范围 */
	public static final String BIRTHDAY_OUT_OF_RANGE = "the birthday of identity number is out of effective range";
	
	/** 数据格式有误 */
	public static final String DATA_FORMAT_ERROR = "data format error";
	
	/** 身份证号不合法 */
	public static final String ID_NUMBER_ILLEGAL = "the identity number is illegal";
}
