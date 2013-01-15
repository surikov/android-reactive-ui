package reactive.ui;

import android.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.Bundle;
import android.os.Environment;
import android.util.*;
import android.net.*;
import android.widget.*;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.apache.http.params.*;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.database.*;
import android.database.sqlite.*;
import reactive.ui.*;

import java.net.*;
import java.nio.channels.FileChannel;

import android.view.animation.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;
import tee.binding.*;
import java.io.*;
import java.text.*;

public class Demo extends Activity {
	Layoutless layoutless;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//System.out.println("go----------------------------------" + Environment.getExternalStorageDirectory().getAbsolutePath());
		layoutless = new Layoutless(this);
		
		Preferences.init(this);
		setContentView(layoutless);
	}
	/*
	double dateOnly(Calendar c) {
		return dateOnly(c, 0);
	}
	double dateOnly(Calendar c, int days) {
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTimeInMillis();
	}
	String pad2(int n) {
		String r = "" + n;
		if (n < 10) {
			r = "0" + r;
		}
		return r;
	}
	void sendFile(String name) {
		String path = reportPath(name);
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		//intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
		intent.putExtra(Intent.EXTRA_SUBJECT, "Отчёт");
		//intent.putExtra(Intent.EXTRA_TEXT, "body text");
		Uri uri = Uri.fromFile(new File(path));
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		//startActivity(Intent.createChooser(intent, "Send email..."));
		startActivity(intent);
	}
	String currentReportLabel() {
		String r = "?";
		int num = sheet.selectedRow.property.value().intValue();
		if (num >= 0 && num < reports.cells.size()) {
			r = reports.cells.get(num);
		}
		return r;
	}*/
	/*void invokeReportAndroid(long from, long to, String reportName, String reportFile, SubLayoutless reportBox) {
		invokeReportAndroid(from, to, reportName, reportFile, reportBox,
				"<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" />");
	}*/
	/*
	void copyFile(String from) {
		String fromPath = reportPath(from);
		String toPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + from + ".html";
		File source = new File(fromPath);
		File destination = new File(toPath);
		try {
			FileChannel src = new FileInputStream(source).getChannel();
			FileChannel dst = new FileOutputStream(destination).getChannel();
			dst.transferFrom(src, 0, src.size());
			src.close();
			dst.close();
			Auxiliary.inform("Файл " + from + " сохранён в папку Download", this);
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}
	boolean saveTextToFile(byte[] bytes, String file) {
		try {
			String txt = new String(bytes, "UTF-8");
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(txt.getBytes("UTF-8"));
			fileOutputStream.flush();
			fileOutputStream.close();
			return true;
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}
	boolean saveTextToFile(String txt, String file) {
		try {
			//String txt = new String(bytes, "UTF-8");
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(txt.getBytes("UTF-8"));
			fileOutputStream.flush();
			fileOutputStream.close();
			return true;
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}
	String reportPath(String name) {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + "/horeca/report_" + name + ".html";
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
		//System.out.println("onPause");
		Preferences.save();
		
		super.onPause();
	}
	
}
