package com.spider.dao;

import java.util.Date;

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
		String sql = "insert into blog(id,url,author,title,date,tags,category,view,comments,copyright,"
				+ "summery,digg,bury,content,type,createtime,updatetime) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		boolean executeUpdate = JdbcUtils.executeUpdate(sql, b.getId(), b.getUrl(), b.getAuthor(),
				b.getTitle(), b.getDate(), b.getTags(), b.getCategory(),
				b.getView(), b.getComments(), b.getCopyright(), b.getSummery(),
				b.getDigg(), b.getBury(), b.getContent(), b.getType(),
				new Date(), new Date());
		return executeUpdate;
	}
	
	/**
	 * 判断是否存在当前书籍库
	 */
	public boolean isExist(String id) {
		String sql = "select id from blog where id = ?";
		String dbId = JdbcUtils.executeQuerySingle(sql, String.class, id);
		boolean result = true;
		if(StringUtils.isEmpty(dbId)){
			result = false;
		}
		return result;
	}
}
