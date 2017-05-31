package com.spider.entity;

public enum BlogEnum {
	CSDN(101, "CSDN博客","http://blog.csdn.net/");
	private int code;
	private String name;
	private String host;

	BlogEnum(int code, String name,String host) {
		this.code = code;
		this.name = name;
		this.host = host;
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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}
