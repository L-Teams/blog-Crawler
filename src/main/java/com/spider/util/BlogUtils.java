package com.spider.util;

import com.spider.entity.Blog;
import com.spider.entity.BlogEnum;



/**
 * blog工具类
 * 
 * @author 孙洪亮
 * 
 */
public class BlogUtils {

	/**
	 * 将url按照自定义规则编码
	 */
	public static void encodeUrl(Blog blog){
		String url = blog.getUrl();
		BlogEnum type = blog.getType();
		String encode = type.getCode() + url.substring(type.getHost().length()-1).replace("/", "-").replace(".", "-");
		blog.setId(encode);
	}
}
