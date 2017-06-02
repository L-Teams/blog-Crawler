package com.spider.processor;

import java.util.ArrayList;
import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import com.spider.pipeline.DbPipeline;
import com.spider.scheduler.BlogScheduler;
import com.spider.service.CsdnService;

public class CsdnPageProcessor implements PageProcessor {
	private CsdnService csdnService = new CsdnService();
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
			.setTimeOut(10000);

	public Site getSite() {
		return site;
	}

	public void process(Page page) {
		// 文章链接
		boolean flg = page.getUrl().regex("/article/details/\\d+").match();
		if (flg) {
			// 提取文章内容元素
			csdnService.putBlogField(page);
			// 将符合规则的url添加到待爬队列中
			csdnService.addSeedTargetRequest(page);
		} else {
			page.setSkip(true);
			csdnService.addDetailTargetRequest(page);
		}
	}

	public static void main(String[] args) {
		System.out.println("【爬虫开始】请耐心等待一大波数据到你碗里来...");
		List<Pipeline> pipelines = new ArrayList<Pipeline>();
		pipelines.add(new DbPipeline());
		// 从用户博客首页开始抓，开启5个线程，启动爬虫
		Spider.create(new CsdnPageProcessor())
				.addUrl("http://blog.csdn.net/hongliang_sun").thread(5)
				.setScheduler(new BlogScheduler()).setPipelines(pipelines).run();
	}
	

}
