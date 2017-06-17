package com.spider.dao;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.spider.entity.Blog;
import com.spider.util.JdbcUtils;
/**
 * 操作博客表的dao
 * @author 孙洪亮
 *
 */
public class BlogDao {
	public boolean insert(Blog b) {
		String sql = "insert into csdnblog(id,url,author,title,date,tags,category,view,comments,copyright,"
				+ "summery,digg,bury,content,createtime,updatetime) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		boolean executeUpdate = JdbcUtils.executeUpdate(sql, UUID.randomUUID().toString().replace("-", ""),b.getUrl(), b.getAuthor(),
				b.getTitle(), b.getDate(), b.getTags(), b.getCategory(),
				b.getView(), b.getComments(), b.getCopyright(), b.getSummery(),
				b.getDigg(), b.getBury(), b.getContent(),
				new Date(), new Date());
		return executeUpdate;
	}
	
	/**
	 * 判断是否存在当前书籍库
	 */
	public boolean isExist(String url) {
		String sql = "select id from csdnblog where url = ?";
		String dbId = JdbcUtils.executeQuerySingle(sql, String.class, url);
		boolean result = true;
		if(StringUtils.isEmpty(dbId)){
			result = false;
		}
		return result;
	}
}
