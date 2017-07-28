package com.spider.start;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.caucho.hessian.client.HessianProxyFactory;
import com.spider.entity.Common;
import com.spider.pipeline.DbPipeline;
import com.spider.processor.CsdnPageProcessor;
import com.spider.scheduler.BlogScheduler;
import com.spider.scheduler.RomteScheduler;
import com.spider.scheduler.SechedulerApi;
import com.spider.service.ShutdownHookService;
import com.spider.task.SerializeBloomFilterTaskService;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.scheduler.Scheduler;

public class CsdnServiceStart extends HttpServlet {
	private static final long serialVersionUID = 6964726643803509902L;
	private static Spider sp = null;
	private static Logger logger = Logger.getLogger(CsdnServiceStart.class);
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String control = req.getParameter("c");
		String model = req.getParameter("m");
		String servletContext = req.getServletContext().getContextPath();
		if ("run".equals(control)) {
			sp = start(model,servletContext);
		} else if ("await".equals(control)) {
			try {
				if (sp != null)
					sp.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if ("stop".equals(control)) {
			if (sp != null)
				sp.stop();
			ShutdownHookService.writeObject(Common.BLOOM_FILE,BlogScheduler.getBf());
			ShutdownHookService.writeObject(Common.QUEUE_FILE,BlogScheduler.getQueue());
		} else if("start".equalsIgnoreCase(control)){
			if (sp != null)
				sp.start();
		}
	}

	private Spider start(String model,String contextPath) {
		ShutdownHookService.addShutdownHook();
		System.out.println("【爬虫开始】请耐心等待一大波数据到你碗里来...");
		List<Pipeline> pipelines = new ArrayList<Pipeline>();
		pipelines.add(new DbPipeline());
		Spider s = null;
		Scheduler scheduler = null;
		if ("m".equals(model)) {
			scheduler = BlogScheduler.getInstance();
			SerializeBloomFilterTaskService task = new SerializeBloomFilterTaskService();
			task.startSerializeBloomFilterTask();
			s = Spider.create(new CsdnPageProcessor())
					.addUrl("http://blog.csdn.net/jiankunking").thread(30)
					.setScheduler(scheduler).setPipelines(pipelines);
			s.runAsync();
		} else {
			InputStream inStream = null;
			try {
				Properties properties = new Properties();
				inStream = this.getClass().getClassLoader()
						.getResourceAsStream("base.properties");
				properties.load(inStream);
				String url = "http://{ip}:{port}/"+contextPath+"hessianService";
				String ip = properties.getProperty("server.ip");
				String port = properties.getProperty("port");
				url = url.replace("{ip}", ip).replace("{port}", port);
				HessianProxyFactory factory = new HessianProxyFactory();
				SechedulerApi api = (SechedulerApi) factory.create(SechedulerApi.class, url);
				s = Spider.create(new CsdnPageProcessor())
						.addUrl("http://blog.csdn.net/jiankunking").thread(15)
						.setScheduler(new RomteScheduler(api))
						.setPipelines(pipelines);
				s.runAsync();
			} catch (Exception e) {
				logger.error("爬虫配置获取异常", e);
			} finally {
				if (inStream != null) {
					try {
						inStream.close();
					} catch (IOException e) {
						logger.info("文件流关闭异常");
					}
				}
			}

		}
		return s;
	}
}
