package com.spider.scheduler;

import java.io.Serializable;

import us.codecraft.webmagic.Request;

public class SchedulerService implements SechedulerApi, Serializable {

	private static final long serialVersionUID = -238779867459522083L;

	public void push(Request request) {
		BlogScheduler.getInstance().push(request, null);
	}

	public Request poll() {
		return BlogScheduler.getInstance().poll(null);
	}

}
