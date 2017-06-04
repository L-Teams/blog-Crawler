package com.spider.entity;

import java.util.Date;

/**
 * Blog
 * @author 孙洪亮
 *
 */
public class Blog {

	private String id;// 编号

	private String url;// 博客url

	private String author;// 作者

	private String title;// 标题

	private Date date;// 日期

	private String tags;// 标签

	private String category;// 分类

	private int view;// 阅读人数

	private int comments;// 评论人数

	private int copyright;// 是否为转载文章（转载文章只存库不添加索引--防止搜索出很多转载一样的内容文章）

	private String summery;// 文章摘要

	private int digg;// 顶的人数

	private int bury;// 踩的人数

	private String content;// 文章内容

	private int type;// 博客的类型

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getView() {
		return view;
	}

	public void setView(int view) {
		this.view = view;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public int getCopyright() {
		return copyright;
	}

	public void setCopyright(int copyright) {
		this.copyright = copyright;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getDigg() {
		return digg;
	}

	public void setDigg(int digg) {
		this.digg = digg;
	}

	public int getBury() {
		return bury;
	}

	public void setBury(int bury) {
		this.bury = bury;
	}

	public String getSummery() {
		return summery;
	}

	public void setSummery(String summery) {
		this.summery = summery;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Blog [id=" + id + ", url=" + url + ", author=" + author
				+ ", title=" + title + ", date=" + date + ", tags=" + tags
				+ ", category=" + category + ", view=" + view + ", comments="
				+ comments + ", copyright=" + copyright + ", summery="
				+ summery + ", digg=" + digg + ", bury=" + bury + ", content="
				+ content + ", type=" + type + "]";
	}

}
