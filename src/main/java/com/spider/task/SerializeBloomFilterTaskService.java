package com.spider.task;

import java.util.Timer;
import java.util.TimerTask;

import com.spider.entity.Common;
import com.spider.scheduler.BlogScheduler;
import com.spider.service.ShutdownHookService;

public class SerializeBloomFilterTaskService {
	private Timer timer = new Timer();
	/**
	 * 开启定时任务将BloomFilter内容存储到文件中
	 * 1分钟后执行，每5分钟执行一次
	 */
	public void startSerializeBloomFilterTask() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				ShutdownHookService.writeObject(Common.BLOOM_FILE,BlogScheduler.getBf());
				ShutdownHookService.writeObject(Common.QUEUE_FILE,BlogScheduler.getQueue());
			}
		}, 60*1000, 5*60*1000);
	}
	
	public void cancleSerializeBloomFilterTask() {
		timer.cancel();
	}
}
