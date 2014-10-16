package cn.com.cml.losefinder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ErrorService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("UnCatchExceptionResolver", "服务启动了。，");
		Toast.makeText(getApplication(), "死了，发送信息给我们？", Toast.LENGTH_LONG)
				.show();
		return super.onStartCommand(intent, flags, startId);
	}
}
