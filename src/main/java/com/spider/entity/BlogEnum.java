package com.spider.entity;

public enum BlogEnum {
	CSDN(101, "CSDN博客");
	
	private int code;
	private String name;

	BlogEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
