package com.htjx.sdk;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

import com.htjx.sdk.utils.AppInfoUtil;
import com.htjx.sdk.utils.LogUtils;
import com.htjx.sdk.utils.MyDateUtils;

import android.content.Context;
import android.os.Environment;

public class CrashHandler implements UncaughtExceptionHandler {   
	 /** 是否开启日志输出,在Debug状态下开启,  
     * 在Release状态下关闭以提示程序性能  
     * */  
    public static final boolean DEBUG = true;   
    /** 系统默认的UncaughtException处理类 */  
    private Thread.UncaughtExceptionHandler mDefaultHandler;   
    /** CrashHandler实例 */  
    private static CrashHandler INSTANCE;   
    /** 程序的Context对象 */  
   private Context context;   
    /** 保证只有一个CrashHandler实例 */  
    private CrashHandler() {}   
    /** 获取CrashHandler实例 ,单例模式*/  
    public static synchronized CrashHandler getInstance() {   
        if (INSTANCE == null) {   
            INSTANCE = new CrashHandler();   
        }   
        return INSTANCE;   
    }   
    
    /**  
     * 初始化,注册Context对象,  
     * 获取系统默认的UncaughtException处理器,  
     * 设置该CrashHandler为程序的默认处理器  
     *   
     * @param context 上下文  
     */  
    public void init(Context context) {   
    	this.context = context;   
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();   
        Thread.setDefaultUncaughtExceptionHandler(this);   
    }   
    
    /**  
     * 当UncaughtException发生时会转入该函数来处理  
     */  
    @Override  
    public void uncaughtException(Thread thread, Throwable ex) {   
        if (!handleException(ex) && mDefaultHandler != null) {   
            //如果用户没有处理则让系统默认的异常处理器来处理   
            mDefaultHandler.uncaughtException(thread, ex);   
        } else {  //如果自己处理了异常，则不会弹出系统自带错误对话框，则需要手动退出app 
			//别忘了手动退出,自已杀死自已的线程
            android.os.Process.killProcess(android.os.Process.myPid());   
            System.exit(10); 
           
        }   
    }   
    
    /**  
     * 自定义错误处理,收集错误信息  
     * 发送错误报告等操作均在此完成.  
     * 开发者可以根据自己的情况来自定义异常处理逻辑  
     * @return  
     * true代表处理该异常，不再向上抛异常， 
     * false代表不处理该异常(可以将该log信息存储起来)然后交给上层(这里就到了系统的异常处理)去处理， 
     * 简单来说就是true不会弹出那个错误提示框，false就会弹出 
     */  
    private boolean handleException(final Throwable ex) {   
        if (ex == null) {   
            return false;   
        }   
//        final String msg = ex.getLocalizedMessage();   
        final StackTraceElement[] stack = ex.getStackTrace(); 
        final String message = ex.getMessage();
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < stack.length; i++) { 
        	sb.append(stack[i].toString());
        } 
        LogUtils.d("CrashHandler:"+sb.toString()+"-----message:"+message);
        if(AppInfoUtil.isExistSDCard()){
        	FileWriter fileWriter =null;
        	try {
				File file = new File(Environment.getExternalStorageDirectory(),"sdk.log");
				if (!file.exists()) {
					file.createNewFile();
				}
				fileWriter = new FileWriter(file, true);
				fileWriter.write(MyDateUtils.formatDateAndTime(System.currentTimeMillis())+"/r/n"+sb.toString()+"-----message:"+message+"/n");
				fileWriter.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        }
        HtjxSDK.onError(context, MyDateUtils.formatDateAndTime(System.currentTimeMillis())+"/r/n"+sb.toString()+"-----message:"+message+"/n");
        return false;   
    }  
}