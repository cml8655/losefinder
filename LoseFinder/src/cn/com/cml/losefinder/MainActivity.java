package cn.com.cml.losefinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import cn.com.cml.losefinder.activity.RadarPenLocationActivity;
import cn.com.cml.losefinder.activity.RadarSearchActivity;

import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocation;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;

public class MainActivity extends ActionBarActivity {
	private ListView contactContainer;
	private SimpleAdapter adapter;

	/*
	 * Defines an array that contains column names to move from the Cursor to
	 * the ListView.
	 */
	private final static String[] FROM_COLUMNS = { Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? Contacts.DISPLAY_NAME_PRIMARY
			: Contacts.DISPLAY_NAME };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initActionBar();
		initComponent();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(new DownloadReceiver(), new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	public void startRadarSearch(View v) {
		startActivity(new Intent(this, RadarSearchActivity.class));
	}

	private void initComponent() {
		TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String deviceId = manager.getDeviceId();

		Log.d("ff", "获取到设备id：" + deviceId);

		String number = manager.getLine1Number();

		Log.d("ff", "获取到设备电话：" + number);
		Log.d("ff", "获取到getNetworkOperator：" + manager.getNetworkOperator());

		contactContainer = (ListView) findViewById(R.id.contact);

		Uri uri = ContactsContract.Contacts.CONTENT_URI;

		Cursor cursor = getContentResolver().query(uri, null, null, null, null);

		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

		while (cursor.moveToNext()) {

			Log.e("", "----" + cursor.getColumnCount() + ",联系人组：");

			String msg = "";
			// 取得联系人的名字索引
			int nameIndex = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			msg += cursor.getString(nameIndex);

			// 取得联系人的ID索引值
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			// 获取联系人电话号码
			Cursor phone = getContentResolver().query(Phone.CONTENT_URI, null,
					Phone.CONTACT_ID + "=" + contactId, null, null);

			msg += ",号码：";

			// 一个联系人有多个号码
			while (phone.moveToNext()) {
				msg += phone.getString(phone.getColumnIndex(Phone.NUMBER));
			}

			// 获取用户所在联系人组
			Cursor groupCursor = getContentResolver().query(
					Data.CONTENT_URI,
					new String[] { GroupMembership.GROUP_ROW_ID },
					GroupMembership.MIMETYPE + "='"
							+ GroupMembership.CONTENT_ITEM_TYPE + "' AND "
							+ Data.RAW_CONTACT_ID + " = " + contactId, null,
					null);

			// Second, get all the corresponding group names
			while (groupCursor.moveToNext()) {
				Cursor groupNameCursor = getContentResolver().query(
						ContactsContract.Groups.CONTENT_URI,
						new String[] { ContactsContract.Groups.TITLE,
								Groups.DATA_SET },
						ContactsContract.Groups._ID + "="
								+ groupCursor.getInt(0), null, null);
				groupNameCursor.moveToNext();
				Log.e("Test", groupNameCursor.getString(0));
				msg += "," + groupNameCursor.getString(0) + ":"
						+ groupNameCursor.getString(1);
				groupNameCursor.close();
			}
			groupCursor.close();

			// 查询联系人的组
			Cursor group = getContentResolver().query(
					ContactsContract.Groups.CONTENT_URI, null, "", null, null);

			if (group.moveToNext()) {
				String[] names = group.getColumnNames();
				for (String name : names) {
					Log.e("group",
							"用户组："
									+ name
									+ ","
									+ (contactId.equals(group.getString(group
											.getColumnIndex(name)))));

				}

			}

			phone.close();
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("text", msg);
			data.add(temp);
		}

		cursor.close();
		adapter = new SimpleAdapter(this, data,
				android.R.layout.simple_list_item_1, new String[] { "text" },
				new int[] { android.R.id.text1 });
		contactContainer.setAdapter(adapter);
	}

	private void initActionBar() {
		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(false);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayShowTitleEnabled(true);
	}

	public void startFence(View view) {
		startActivity(new Intent(this, RadarPenLocationActivity.class));
	}

	public void updateText(View v) {
		ScrollTextView vv = (ScrollTextView) findViewById(R.id.lll);
		vv.updateText(0, 500, 5000);
	}

	public void download(View v) {
		DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		String downloadUrl = "http://bs.baidu.com/appstore/apk_C68809C4FF0F010AE69F6BF4894F79DF.apk";

		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(downloadUrl));
		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE
				| Request.NETWORK_WIFI);
		request.setTitle("正在下载文件");

		manager.enqueue(request);
	}

	class DownloadReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
				long downId = intent.getLongExtra(
						DownloadManager.EXTRA_DOWNLOAD_ID, -1);
				Toast.makeText(context,
						"文件下载成功" + intent.getExtras() + ",下载id:" + downId,
						Toast.LENGTH_LONG).show();
			}
		}

	}

	private void cropView() {
		ImageView img = (ImageView) findViewById(R.id.cropWindow);
		View container = getWindow().getDecorView();
		// ------------------------方法可行-----
		container.setDrawingCacheEnabled(true);
		container.buildDrawingCache();
		Bitmap bitmap = container.getDrawingCache();
		Bitmap b = Bitmap.createBitmap(bitmap);
		// ---------------------
		Canvas canvas = new Canvas(b);
		img.draw(canvas);
		container.destroyDrawingCache();
		Toast.makeText(this, "截图成功：" + container.getWidth(), Toast.LENGTH_LONG)
				.show();
	}

	public void addContact(View view) {
		sendBroadcast(new Intent("com.cml.error"));
		// cropView();
		// if (1 == 1)
		// throw new RuntimeException("000000");

		ContentValues values = new ContentValues();

		long id = ContentUris.parseId(getContentResolver().insert(
				RawContacts.CONTENT_URI, new ContentValues()));

		values.put(StructuredName.DISPLAY_NAME, "我是添加的22");
		// values.put(StructuredName.FAMILY_NAME, "fam");
		values.put(Data.RAW_CONTACT_ID, id);

		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		values.put(StructuredName.GIVEN_NAME, "我是添加的");

		Uri uri = getContentResolver().insert(Data.CONTENT_URI, values);

		values.clear();
		values.put(Data.RAW_CONTACT_ID, id);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.NUMBER, "1234522678");
		values.put(Data.DATA2, "dddd");
		values.put(Phone.TYPE, Phone.TYPE_MOBILE);
		getContentResolver().insert(Data.CONTENT_URI, values);

		Toast.makeText(this, "插入用户：" + uri + "," + id, Toast.LENGTH_LONG)
				.show();
	}

	/**
	 * 开启定位功能
	 * 
	 * @param view
	 */
	public void startLocationMonitor(View view) {
		// 获取到定位客户端
		LocationClient mLocationClient = ((LocationApplication) getApplication()).mLocationClient;
		mLocationClient.start();

		// 建立距离提醒
		// NotifyLister listener = new NotifyLister();
		// listener.SetNotifyLocation(31.221014, 121.538641, 3000, "gcj02");//
		// 4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
		// mLocationClient.registerNotify(listener);
	}

	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {
			((LocationApplication) getApplication()).mVibrator.vibrate(1000);// 振动提醒已到设定位置附近
			Toast.makeText(getApplicationContext(), "震动提醒", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	protected void onStop() {
		((LocationApplication) getApplication()).mLocationClient.stop();
		super.onStop();
	}

	static class ContactLoaderCallBack implements LoaderCallbacks<Cursor> {

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
			return null;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor arg1) {

		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {

		}
	}

}
