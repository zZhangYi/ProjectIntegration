package com.zhy.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum SystemTypeEnum {
	/**
	 * web模块
	 */
	WEB("WEB", "web模块"),

	/**
	 * anagement模块
	 */
	MANAGEMENT("MANAGEMENT", "management模块");

	/** 系统编码 */
	private String value;
	/** 系统名称 */
	private String name;

	/**
	 * 将枚举类型放进map
	 */
	private static final Map<String, SystemTypeEnum> cacheEnums = new HashMap<String, SystemTypeEnum>();

	static {
		for (SystemTypeEnum enumValue : SystemTypeEnum.values()) {
			cacheEnums.put(enumValue.getValue(), enumValue);
		}
	}

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	SystemTypeEnum(String value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * 【方法功能描述】根据value值获取枚举
	 *
	 * @param value value值
	 * @return 枚举值
	 * @comment
	 * @history
	 * @Version 1.0.0
	 */
	public static SystemTypeEnum getEnumByValue(String value) {
		if (StringUtils.isNotBlank(value)) {
			return cacheEnums.get(value);
		}
		return null;
	}

}
