package com.htjx.sdk.utils;



import com.htjx.sdk.net.ThreadPoolManager;

import android.os.Handler;

/**
 * 异步任务
 * @author fada
 *
 */
public abstract class MyAsyncTask {
	private ThreadPoolManager threadPoolManager = null;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			onPostExecute();
		};
	};

	public MyAsyncTask() {
	}
	
	public MyAsyncTask(ThreadPoolManager threadPoolManager) {
		this.threadPoolManager = threadPoolManager;
	}
	/**
	 * 1.耗时任务开启之前调用的方法.
	 */
	public abstract void onPreExecute();

	/**
	 * 在后台执行的耗时的任务 运行在子线程.
	 */
	public abstract void doInBackground();

	/**
	 * 3. 耗时任务执行之后调用的方法
	 */
	public abstract void onPostExecute();

	/**
	 * 执行一个异步任务.
	 */
	public void execute() {
		onPreExecute();
		if(threadPoolManager!=null){
			threadPoolManager.addTask(new MyAsyncRunnable());
		}else{
			new Thread(new MyAsyncRunnable()).start();//应急任务
		}
	}
	class MyAsyncRunnable implements Runnable{

		@Override
		public int hashCode() {
			return 0;
		}
		@Override
		public void run() {
			doInBackground();
			handler.sendEmptyMessage(0);

		}
	
		
	}
}
