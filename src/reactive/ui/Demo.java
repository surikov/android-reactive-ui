package reactive.ui;

//http://www3.ntu.edu.sg/home/ehchua/programming/android/Android_3D.html
import android.view.*;
import android.app.*;
import android.app.KeyguardManager.*;
import android.app.admin.*;
import android.content.*;
import android.graphics.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.*;
import android.net.*;
import android.widget.*;
import java.util.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.params.*;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.database.*;
import android.database.sqlite.*;
import reactive.ui.*;
import java.net.*;
import java.nio.channels.FileChannel;
import android.view.animation.*;
import android.view.inputmethod.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;
import tee.binding.*;
import java.io.*;
import java.text.*;


public class Demo extends Activity {
	Layoutless layoutless;
	public static String version() {
		return "1.09";
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layoutless = new Layoutless(this);
		this.setContentView(layoutless);
		layoutless//
				.child(new Knob(this)//
				.afterTap.is(new Task() {
					@Override
					public void doTask() {
						System.out.println("lock");
						lockAll();
						System.out.println("lock done");
					}
				})//
				.labelText.is("lock").width().is(200).height().is(70)//
				)//
				.child(new Knob(this)//
				.afterTap.is(new Task() {
					@Override
					public void doTask() {
						System.out.println("unlock");
						unlockAll();
						System.out.println("unlock done");
					}
				})//
				.labelText.is("exit").top().is(70).width().is(200).height().is(70)//
				)//
		;
		System.out.println("done onCreate");
	}
	public void lockAll() {
	}
	public void unlockAll() {
	}
	public void exitHome() {
		Intent selector = new Intent("android.intent.action.MAIN");
		selector.addCategory("android.intent.category.HOME");
		selector.setComponent(new ComponentName("android", "com.android.internal.app.ResolverActivity"));
		startActivity(selector);
		
		//finish this activity
		finish();
	}
	//override some buttons
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//Log.d("button", new Integer(keyCode).toString());
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			System.out.println("KEYCODE_BACK");
			return true;
		} else {
			if ((keyCode == KeyEvent.KEYCODE_CALL)) {
				System.out.println("KEYCODE_CALL");
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	public void lockTest() {
		KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
		KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
		//lock.reenableKeyguard();
		lock.disableKeyguard();
	}
	public void _lockTest() {
		ComponentName devAdminReceiver; // this would have been declared in your class body
		// then in your onCreate
		DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		devAdminReceiver = new ComponentName(this, DeviceAdminReceiver.class);
		//then in your onResume
		boolean admin = mDPM.isAdminActive(devAdminReceiver);
		if (admin) {
			mDPM.lockNow();
		} else {
			System.out.println("Not an admin");
		}
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		if (!mDPM.isAdminActive(devAdminReceiver)) {
			intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, devAdminReceiver);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "is locked");
			intent.putExtra("force-locked", DeviceAdminInfo.USES_POLICY_FORCE_LOCK);
			startActivityForResult(intent, 1);
			System.out.println("The Device Could not lock because device admin not enabled");
			//mDPM.lockNow();
		} else {
			System.out.println("The Device  device admin enabled");
			intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, devAdminReceiver);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "onEnabled");
			mDPM.lockNow();
			mDPM.setMaximumTimeToLock(devAdminReceiver, 0);
			intent.putExtra("force-locked", DeviceAdminInfo.USES_POLICY_FORCE_LOCK);
			startActivityForResult(intent, 1);
		}
		System.out.println("done");
	}
	/*
	SQLiteDatabase db() {
		if (cacheSQLiteDatabase == null || (!cacheSQLiteDatabase.isOpen())) {
			//cacheSQLiteDatabase = Auxiliary.connectSQLiteDatabase("/sdcard/horeca/swlife_database", this);
			cacheSQLiteDatabase = this.openOrCreateDatabase("/sdcard/horeca/swlife_database", Context.MODE_PRIVATE, null);
		}
		//cacheSQLiteDatabase.setVersion(2);
		return cacheSQLiteDatabase;
	}*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("First");
		menu.add("Second");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
}
