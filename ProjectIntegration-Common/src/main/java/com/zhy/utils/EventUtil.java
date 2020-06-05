package com.zhy.utils;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EventUtil {
	
	private static EventBus eb;
	private static int corePoolSize = 2;
	private static int maxPoolSize = 20;
	private static long keepAliveTime = 60;
	private static TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS; 
	private static LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
	
	public static void regist(Object listener) {
		init();
		eb.register(listener);
	}
	
	public static void post(Object event) {
		init();
		eb.post(event);
	}
	
	public static void init(String filePath) throws IOException {
		if (eb == null) {
			Executor excutor = initExcutor(filePath);
			eb = new AsyncEventBus(excutor);
		}
	}
	
	public static void init() {
		if (eb == null) {
			Executor excutor = initExcutor();
			eb = new AsyncEventBus(excutor);
		}
	}
	
	public static void refresh(String filePath) throws IOException {
		Executor excutor = initExcutor(filePath);
		eb = new AsyncEventBus(excutor);
	}
	
	public static void refresh() throws IOException {
		Executor excutor = initExcutor();
		eb = new AsyncEventBus(excutor);
	}
	
	private static Executor initExcutor(String filePath) throws IOException {
		Properties properties = new Properties();
		InputStream is = new FileInputStream(filePath);
		properties.load(is);
		
		if (properties.get("corePoolSize") != null) {
			corePoolSize = Integer.parseInt(properties.get("eventbus.corePoolSize").toString());
		}
		
		if (properties.get("maxPoolSize") != null) {
			maxPoolSize = Integer.parseInt(properties.get("eventbus.maxPoolSize").toString());
		}
		
		if (properties.get("keepAliveTime") != null) {
			keepAliveTime = Integer.parseInt(properties.get("eventbus.keepAliveTime").toString());
		}
		
		return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, keepAliveTimeUnit, queue);
	}
	
	private static Executor initExcutor() {
		return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, keepAliveTimeUnit, queue);
	}
	
}
