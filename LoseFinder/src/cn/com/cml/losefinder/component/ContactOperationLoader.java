package cn.com.cml.losefinder.component;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.widget.Adapter;

/**
 * 实现对联系人的增删查改功能
 * 
 * @author teamlab
 *
 */
public class ContactOperationLoader implements LoaderCallbacks<Cursor> {

	public static final int QUERY_CONTACT_LOADER = 1000;
	public static final int UPDATE_CONTACT_LOADER = 1001;
	public static final int INSERT_CONTACT_LOADER = 1002;
	public static final int DEL_CONTACT_LOADER = 1003;

	/**
	 * 参数类别
	 * 
	 * @author teamlab
	 *
	 */
	public static interface Params {
		String TYPE = "type";
		String URI = "uri_param";
		int TYPE_ALL = 1;
		int TYPE_ITEM = 2;
		int TYPE_ITEM_INDICATOR = 2;
	}

	private Adapter adapter;

	public ContactOperationLoader(Adapter adapter) {
		this.adapter = adapter;
	}

	public ContactOperationLoader() {
	}

	@Override
	public Loader<Cursor> onCreateLoader(int key, Bundle param) {

		switch (key) {
		case INSERT_CONTACT_LOADER:

			break;
		case DEL_CONTACT_LOADER:

			break;
		case QUERY_CONTACT_LOADER:

			break;
		case UPDATE_CONTACT_LOADER:

			break;

		default:
			break;
		}

		Uri queryUri = param.getParcelable(Params.URI);
		
		
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}
}
