package com.spider.pipeline;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import com.spider.dao.BlogDao;
import com.spider.entity.Blog;
import com.spider.entity.Common;
import com.spider.util.DateUtils;

/**
 * 将爬到的数据持久化到数据库中
 * 
 * @author 孙洪亮
 *
 */
public class DbPipeline implements Pipeline{
	public static AtomicLong totalNum = new AtomicLong(0);
	private static Executor executor = Executors.newFixedThreadPool(3);
	private BlogDao dao = new BlogDao();
	private Logger LOG = Logger.getLogger(DbPipeline.class);
	public void process(final ResultItems result, Task task) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					Blog b = new Blog();
					String author = result.get(Common.AUTHOR);
					b.setAuthor(author);
					int bury = result.get(Common.BURY);
					b.setBury(bury);
					String category = result.get(Common.CATEGORY);
					b.setCategory(category);
					int comments = result.get(Common.COMMENTS);
					b.setComments(comments);
					String content = result.get(Common.CONTENT);
					b.setContent(removeTag(content));
					int copyright = result.get(Common.COPYRIGHT);
					b.setCopyright(copyright);
					String date = result.get(Common.DATE);
					b.setDate(DateUtils.parseStr2Date(date, DateUtils.DATE_LONG_FORMAT_NOSECOND).orElse(null));
					int digg = result.get(Common.DIGG);
					b.setDigg(digg);
					String summery = result.get(Common.SUMMERY);
					b.setSummery(summery);
					String tags = result.get(Common.TAGS);
					b.setTags(tags);
					String title = result.get(Common.TITLE);
					b.setTitle(title);
					String url = result.get(Common.URL);
					b.setUrl(url);
					int view = result.get(Common.VIEW);
					b.setView(view);
					if(dao.isExist(b.getUrl())){
						return;
					}
					boolean insert = dao.insert(b);
					if(insert){
						totalNum.incrementAndGet();
					}
				} catch (Exception e) {
					LOG.error("填数据异常",e);
				}
			}
		});
	}

	private static String removeTag(String htmlStr) {
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // script
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // style
		String regEx_html = "<[^>]+>"; // HTML tag
		String regEx_space = "\\s+|\t|\r|\n";// other characters
		Pattern p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll("");
		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll("");
		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll("");
		Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll(" ");
		htmlStr = htmlStr.replace("&nbsp;", "").replace("&lt", "");
		return htmlStr;
	}
	
}
