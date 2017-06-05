package com.spider.scheduler;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.Scheduler;

import com.spider.entity.Common;
import com.spider.service.ShutdownHookService;
import com.spider.util.BloomFilter;
/**
 * 对添加的url进行去重处理
 * @author 孙洪亮
 */
@SuppressWarnings("unchecked")
public class BlogScheduler implements Scheduler{
	private static final Logger LOG = Logger.getLogger(BlogScheduler.class);
	private static ConcurrentLinkedQueue<Request> queue = null; 
	private static BloomFilter bf = null;
	static{
		Object fb = ShutdownHookService.readObjectFromFile(Common.BLOOM_FILE);
		Object qb = ShutdownHookService.readObjectFromFile(Common.QUEUE_FILE);
		if(fb != null)
			bf = (BloomFilter) fb;
		else{
			bf = new BloomFilter();
		}
		if(qb != null){
			queue = (ConcurrentLinkedQueue<Request>) qb;
		}else{
			queue =  new ConcurrentLinkedQueue<Request>();
		}
	}
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

	public static ConcurrentLinkedQueue<Request> getQueue() {
		return queue;
	}

	public static BloomFilter getBf() {
		return bf;
	}
}
