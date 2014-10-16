package cn.com.cml.losefinder;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

public class UnCatchExceptionResolver implements
		Thread.UncaughtExceptionHandler {

	private Context context;
	private UncaughtExceptionHandler handler;

	public void init(Context context) {
		this.context = context;
		handler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		Log.d("UnCatchExceptionResolver", "初始化了");
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// Intent intent=new Intent(context, ErrorService.class);
		// context.startService(intent);
		context.sendBroadcast(new Intent("com.cml.error"));
		Log.e("UnCatchExceptionResolver", "获取到未捕获的信息！" + ex.getMessage() + ","
				+ context);
		// Process.killProcess(Process.myPid());
		 handler.uncaughtException(thread, ex);
		// AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// builder.setMessage("死囚了，发送日志帮我我们好吗？");
		// builder.setTitle("系统提示");
		// builder.create().show();
		// Toast.makeText(context, "死了，发送信息给我们？", Toast.LENGTH_LONG).show();

	}

}
