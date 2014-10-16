package cn.com.cml.losefinder.activity;

import cn.com.cml.losefinder.component.RadarTranslateView;
import android.app.Activity;
import android.os.Bundle;

public class RadarSearchActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setContentView(new RadarTranslateView(getApplicationContext()));
	}
}
