package com.spider.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import com.spider.entity.Common;
import com.spider.scheduler.BlogScheduler;

/**
 * 虚拟机关闭是注册服务类
 * 
 * @author 孙洪亮
 *
 */
public class ShutdownHookService {
	private static Logger LOG = Logger.getLogger(ShutdownHookService.class);

	/**
	 * 注册一个关机钩，当系统被退出或被异常中断时，启动这个关机钩线程
	 */
	public static void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				// 添入你想在退出JVM之前要处理的必要操作代码
				writeObject(Common.BLOOM_FILE,BlogScheduler.getBf());
				writeObject(Common.QUEUE_FILE,BlogScheduler.getQueue());
			}
		});
	}

	/**
	 * 关闭时执行的服务
	 */
	public static void writeObject(String fileName,Object object) {
		LOG.info("序列化数据到文件["+fileName+"]中");
		File file = new File(fileName);
		FileOutputStream out = null;
		ObjectOutputStream objOut = null;
		try {
			out = new FileOutputStream(file);
			objOut = new ObjectOutputStream(out);
			objOut.writeObject(BlogScheduler.getBf());
			objOut.flush();
		} catch (IOException e) {
			LOG.error("write object failed "+fileName , e);
		}finally{
			free(objOut,out);
		}
	}
	
	/**
	 * 从文件中读取对象（反序列化）
	 */
	public static Object readObjectFromFile(String fileName) {
		LOG.info("从文件["+fileName+"]中读取数据");
		Object temp = null;
		File file = new File(fileName);
		if(!file.exists()) return null;
		FileInputStream in = null;
		ObjectInputStream objIn = null;
		try {
			in = new FileInputStream(file);
			objIn = new ObjectInputStream(in);
			temp = objIn.readObject();
			LOG.info("read object success! " + fileName);
		} catch (Exception e) {
			LOG.error("read object failed! " + fileName);
		}finally{
			free(objIn,in);
		}
		return temp;
	}
	
	private static void free(AutoCloseable... autoCloseable){
		for(AutoCloseable c : autoCloseable){
			if(c != null){
				try {
					c.close();
				} catch (Exception e) {
					LOG.error("关闭异常",e);
				}
			}
		}
	}
}
