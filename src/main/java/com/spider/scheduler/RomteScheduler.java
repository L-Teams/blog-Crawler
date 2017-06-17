package com.spider.scheduler;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.Scheduler;

public class RomteScheduler implements Scheduler {
	private SechedulerApi api = null;
	public RomteScheduler(SechedulerApi api) {
		this.api = api;
	}
	public void push(Request request, Task task) {
		api.push(request);
	}

	public Request poll(Task task) {
		Request poll = api.poll();
		return poll;
	}

}
