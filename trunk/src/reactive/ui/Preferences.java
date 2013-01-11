package reactive.ui;

import android.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.*;
import android.view.*;
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

import android.content.*;

import java.net.*;

import android.view.animation.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;
import java.io.*;
import java.text.*;

public class Preferences {
	static Hashtable<String, Numeric> integers = new Hashtable<String, Numeric>();
	//static Preferences me;
	static SharedPreferences preferences;

	private Preferences() {
	}
	public static void init(Context context) {
		if (preferences == null) {
			preferences = PreferenceManager.getDefaultSharedPreferences(context);
		}
	}
	public static void save() {
		//System.out.println("save preferences");
		SharedPreferences.Editor editor = preferences.edit();
		for (Enumeration<String> e = integers.keys(); e.hasMoreElements();) {
			String k = e.nextElement();
			editor.putInt(k, integers.get(k).value().intValue());
			//System.out.println("save preference: "+k+"="+integers.get(k).value().intValue());
		}
		editor.commit();
	}
	public static Numeric integer(String name, int defaultValue) {
		//System.out.println("read preference: "+name);
		Numeric n = integers.get(name);
		if (n == null) {
			int storedPreference = preferences.getInt(name, defaultValue);
			n = new Numeric().value(storedPreference);
			integers.put(name, n);
			//System.out.println("create preference: "+name+"="+n.value().intValue());
		}
		return n;
	}
}
