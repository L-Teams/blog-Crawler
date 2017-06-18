package com.spider.scheduler;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedQueue;

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
public class BlogScheduler implements Scheduler ,Serializable{
	private static final BlogScheduler bs = new BlogScheduler();
	private static final long serialVersionUID = -8352923253868026434L;
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
	
	private BlogScheduler(){}
	
	public static BlogScheduler getInstance(){
		return bs;
	}
	
	public void push(Request request, Task task) {
		String url = request.getUrl();
		if(!bf.isExistUrl(url)){
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
