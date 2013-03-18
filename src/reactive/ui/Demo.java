package reactive.ui;

//http://www3.ntu.edu.sg/home/ehchua/programming/android/Android_3D.html
import android.view.*;
import android.app.*;
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
	
public static String version(){return "1.04";}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layoutless = new Layoutless(this);
		this.setContentView(layoutless);
		layoutless//
				.child(new Decor(this)//
				.background.is(0x33ff0000).width().is(200).height().is(200)//
				)//
				.child(new Decor(this)//
				.background.is(0x3300ff00).width().is(200).height().is(200).left().is(150).top().is(50)//
				)//
				.child(new SplitTopDown(this)//
				.topSide(new Decor(this).labelText.is("top").background.is(0x6633ff66))//
				.downSide(new Decor(this).labelText.is("down").background.is(0x3300ffff))//
				.width().is(700)//
				.height().is(500)//
				)
		;
		
		System.out.println("done onCreate");
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
