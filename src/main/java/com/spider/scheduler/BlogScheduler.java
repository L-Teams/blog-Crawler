package com.spider.scheduler;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.spider.util.BloomFilter;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.Scheduler;

public class BlogScheduler implements Scheduler{
	private static final Logger LOG = Logger.getLogger(BlogScheduler.class);
	private ConcurrentLinkedQueue<Request> queue = new ConcurrentLinkedQueue<Request>(); 
	private BloomFilter bf = new BloomFilter();
	public void push(Request request, Task task) {
		String url = request.getUrl();
		if(!bf.isExistUrl(url)){
			if(queue.size() > 10000){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					LOG.error("线程sleep出现异常", e);
				}
				LOG.info("待爬取链接已经超过10000个");
			}
			queue.add(request);
		}
	}

	public Request poll(Task task) {
		return queue.poll();
	}

}
