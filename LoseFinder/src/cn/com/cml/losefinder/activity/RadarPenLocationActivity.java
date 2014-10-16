package cn.com.cml.losefinder.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import cn.com.cml.losefinder.LocationApplication;
import cn.com.cml.losefinder.R;

import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocationStatusCodes;
import com.baidu.location.GeofenceClient;
import com.baidu.location.GeofenceClient.OnAddBDGeofencesResultListener;
import com.baidu.location.GeofenceClient.OnGeofenceTriggerListener;
import com.baidu.location.GeofenceClient.OnRemoveBDGeofencesResultListener;
import com.baidu.location.LocationClient;

/**
 * 雷达指示界面定位
 * 
 * @author teamlab
 *
 */
public class RadarPenLocationActivity extends ActionBarActivity {

	private static final String TAG = RadarPenLocationActivity.class
			.getSimpleName();

	private LocationClient mLocationClient;
	private GeofenceClient mGeofenceClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radar_pen);

		mLocationClient = ((LocationApplication) getApplicationContext()).mLocationClient;
		mGeofenceClient = ((LocationApplication) getApplication()).mGeofenceClient;
	}

	public void createFence(View view) {
		double longtitude = 121.538641;
		double latotide = 31.221014;

		BDGeofence fence = new BDGeofence.Builder()
				.setGeofenceId("RadarPenLocationActivity")
				.setCircularRegion(longtitude, latotide,
						BDGeofence.RADIUS_TYPE_SMALL)
				.setExpirationDruation(10L * (3600 * 1000))
				.setCoordType(BDGeofence.COORD_TYPE_BD09LL).build();
		
		mGeofenceClient.setInterval(199009999);
		mGeofenceClient.addBDGeofence(fence, new AddFenceListener());

		Geofence triggerFence = new Geofence();
		mGeofenceClient.registerGeofenceTriggerListener(triggerFence);

		Log.d(TAG, "围栏创建成功了！");
	}

	public class AddFenceListener implements OnAddBDGeofencesResultListener {

		@Override
		public void onAddBDGeofencesResult(int statusCode, String geofenceId) {
			Log.d(TAG, "添加围栏状态：" + geofenceId + ":" + statusCode + ":"
					+ BDLocationStatusCodes.SUCCESS);
			try {
				if (statusCode == BDLocationStatusCodes.SUCCESS) {
					// // 开发者实现创建围栏成功的功能逻辑
					// Message msg = MessageHandler.obtainMessage();
					// Bundle bundle = new Bundle();
					// bundle.putString("msg", "围栏" + geofenceId + "添加成功");
					// msg.setData(bundle);
					// MessageHandler.sendMessage(msg);
					// Toast.makeText(GeoFenceActivity.this, "围栏" + geofenceId +
					// "添加成功", Toast.LENGTH_SHORT).show();
					if (mGeofenceClient != null) {
						// setData(geofenceId);
						// 在添加地理围栏成功后，开启地理围栏服务，对本次创建成功且已进入的地理围栏，可以实时的提醒
						mGeofenceClient.start();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public class Geofence implements OnGeofenceTriggerListener {

		@Override
		public void onGeofenceTrigger(String arg0) {
			// TODO Auto-generated method stub
			Log.d(TAG, "进入围栏：" + arg0);
			// String temp = logMsg.getText().toString();
			// temp += "\n进入围栏" + arg0;
			// Message msg = MessageHandler.obtainMessage();
			// Bundle bundle = new Bundle();
			// bundle.putString("msg", temp);
			// msg.setData(bundle);
			// MessageHandler.sendMessage(msg);
		}

		@Override
		public void onGeofenceExit(String arg0) {
			Log.d(TAG, "退出围栏：" + arg0);
			// TODO Auto-generated method stub
			// String temp = logMsg.getText().toString();
			// temp += "\n退出围栏" + arg0;
			// Message msg = MessageHandler.obtainMessage();
			// Bundle bundle = new Bundle();
			// bundle.putString("msg", temp);
			// msg.setData(bundle);
			// MessageHandler.sendMessage(msg);
		}

	}

	public class RemoveFenceListener implements
			OnRemoveBDGeofencesResultListener {
		@Override
		public void onRemoveBDGeofencesByRequestIdsResult(int statusCode,
				String[] geofenceRequestIds) {
			if (statusCode == BDLocationStatusCodes.SUCCESS) {
				Log.d(TAG, "退出围栏");
				// Message msg = MessageHandler.obtainMessage();
				// Bundle bundle = new Bundle();
				// bundle.putString("msg", "围栏" + "删除成功");
				// msg.setData(bundle);
				// MessageHandler.sendMessage(msg);
				// for (int i = 0; i < geofenceRequestIds.length; i++) {
				// if (getIDList.contains(geofenceRequestIds[i])) {
				// getIDList.remove(geofenceRequestIds[i]);
				// }
				// }
			}
		}
	}
}
