package cn.com.cml.losefinder;

import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

/**
 * 定位前的基本准备,开启GPS
 * 
 * @author teamlab
 *
 */
public class LocationPreparedService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LocationManager location = (LocationManager) getApplicationContext()
				.getSystemService(LOCATION_SERVICE);
		location.setTestProviderEnabled("gps", true);
		return super.onStartCommand(intent, flags, startId);
	}

}
