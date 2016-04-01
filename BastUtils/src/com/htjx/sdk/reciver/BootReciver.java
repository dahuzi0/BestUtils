package com.htjx.sdk.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 开机启动广播接收者
 * @author fada
 *
 */
public class BootReciver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent();
		service.setAction("android.intent.action.STARTSDK");
		context.startService(service);
		
	}

}
