package com.spider.scheduler;

import us.codecraft.webmagic.Request;

public interface SechedulerApi {
	
	public void push(Request request);

	public Request poll();
}
