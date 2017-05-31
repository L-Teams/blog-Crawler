package com.spider.util;


/**
 * blog工具类
 * 
 * @author 孙洪亮
 * 
 */
public class BlogUtils {

	public static void main(String[] args) throws Throwable {
		String url = "https://www.baidu.com/s?ie=你";
		byte[] bytes = url.getBytes("utf-8");
		StringBuilder sb = new StringBuilder();
		for(byte b : bytes){
			sb.append(b);
		}
		System.out.println(sb);
	}
}
