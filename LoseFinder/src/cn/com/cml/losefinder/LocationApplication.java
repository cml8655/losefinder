package cn.com.cml.losefinder;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/**
 * 主Application
 */
public class LocationApplication extends Application {
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;

	public TextView mLocationResult, logMsg;
	public TextView trigger, exit;
	public Vibrator mVibrator;

	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化定位信息
		initLocationOptions();
	}

	private void initLocationOptions() {
		mLocationClient = new LocationClient(this.getApplicationContext());
		// 设置参数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 高精度
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(5000);// 设置发起定位时间为5s
		option.setIsNeedAddress(true);// 需要地址位置、
		option.setIgnoreKillProcess(true);
		option.setNeedDeviceDirect(true);
		option.setOpenGps(true);
		mLocationClient.setLocOption(option);
		mLocationClient.requestOfflineLocation();

		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());

		mVibrator = (Vibrator) getApplicationContext().getSystemService(
				Service.VIBRATOR_SERVICE);
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			sb.append("\n方向：").append(location.getDirection());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			if (location.hasAddr()) {
				sb.append("\n").append("地址：").append(location.getAddrStr());
			}
			if (location.hasRadius()) {
				sb.append("\n").append("精确半径：").append(location.getRadius());
			}
			logMsg(sb.toString());
			Log.i("BaiduLocationApiDem", sb.toString());
		}

	}

	/**
	 * 显示请求字符串
	 * 
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			if (mLocationResult != null)
				mLocationResult.setText(str);
			Toast.makeText(this, "获取到定位信息：" + str, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 高精度地理围栏回调
	 * 
	 * @author jpren
	 *
	 */

}
