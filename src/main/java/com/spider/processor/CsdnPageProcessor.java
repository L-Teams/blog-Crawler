package com.spider.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import com.spider.service.CsdnService;

public class CsdnPageProcessor implements PageProcessor {
	private CsdnService csdnService = new CsdnService();
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).
			setTimeOut(10000).addHeader("Accept-Encoding", "gzip, deflate, sdch").
			addHeader("Accept-Language", "zh-CN,zh;q=0.8").addHeader("Connection", "keep-alive").
			setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

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
}
