package cn.com.cml.losefinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ErrorBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("ddd", "接收到广播");
		Toast.makeText(context, "接收到广播" + intent.getAction(), Toast.LENGTH_LONG)
				.show();
	}

}
