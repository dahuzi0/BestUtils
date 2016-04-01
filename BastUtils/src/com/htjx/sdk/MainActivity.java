package com.htjx.sdk;

import com.htjx.sdk.net.ThreadPoolManager;
import com.htjx.sdk.utils.AppInfoUtil;
import com.htjx.sdk.utils.LogUtils;
import com.htjx.sdk.utils.MyAsyncTask;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LogUtils.d("imei:"+AppInfoUtil.getIMEI(this));
		new MyAsyncTask(ThreadPoolManager.getInstance()) {
			
			@Override
			public void onPreExecute() {
			///
				
			}
			@Override
			public void doInBackground() {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onPostExecute() {
				// TODO Auto-generated method stub
				
			}
			
		
		}.execute();
		new MyAsyncTask() {
			
			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPostExecute() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void doInBackground() {
				// TODO Auto-generated method stub
				
			}
		}.execute();
	}

}
