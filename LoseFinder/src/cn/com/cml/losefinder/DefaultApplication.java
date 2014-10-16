package cn.com.cml.losefinder;

import android.app.Application;

public class DefaultApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		new UnCatchExceptionResolver().init(getApplicationContext());
	}
}
