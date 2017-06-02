package com.spider.pipeline;

import com.spider.entity.BlobCommon;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 将爬到的数据持久化到数据库中
 * 
 * @author 孙洪亮
 *
 */
public class DbPipeline implements Pipeline{

	public void process(ResultItems result, Task task) {
		System.out.println("持久化:"+result.get(BlobCommon.ID));
	}

}
