package reactive.ui;

import android.view.*;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.*;
import android.graphics.*;
import android.hardware.*;
import android.text.InputType;
import android.text.TextUtils;
import android.util.*;
import android.view.*;
import android.webkit.MimeTypeMap;
import android.widget.*;
import java.util.*;
import reactive.ui.*;
import android.content.res.*;
import android.view.animation.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;
import tee.binding.*;
import android.net.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.*;
import android.database.*;
import android.database.sqlite.*;
import tee.binding.Bough;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class Auxiliary {
	public static int colorBackground = 0x66ff0000;
	public static int textColorPrimary = 0x6600ff00;
	public static int textColorHint = 0x660000ff;
	public static int textColorHighlight = 0x66ffff00;
	public static int textColorLink = 0x6600ffff;
	public static int colorLine = 0x66ff00ff;
	public static Paint paintLine = null;
	public static int colorSelection = 0x663399ff;
	public static float density = 1;
	public static int tapSize = 8;
	public static SensorEventListener sensorEventListener = null;
	public static double accelerometerX = 0;
	public static double accelerometerY = 0;
	public static double accelerometerZ = 0;
	public static double accelerometerNoise = 1.0;
	private static final char[] FIRST_CHAR = new char[256];
	private static final char[] SECOND_CHAR = new char[256];
	private static final char[] HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static final byte[] DIGITS = new byte['f' + 1];
	static SimpleDateFormat sqliteTime = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss.SSS");
	static SimpleDateFormat sqliteDate = new SimpleDateFormat("yyyy-MM-DD");
	public final static String version = "1.34";
	static {
		for (int i = 0; i < 256; i++) {
			FIRST_CHAR[i] = HEX_DIGITS[(i >> 4) & 0xF];
			SECOND_CHAR[i] = HEX_DIGITS[i & 0xF];
		}
		for (int i = 0; i <= 'F'; i++) {
			DIGITS[i] = -1;
		}
		for (byte i = 0; i < 10; i++) {
			DIGITS['0' + i] = i;
		}
		for (byte i = 0; i < 6; i++) {
			DIGITS['A' + i] = (byte) (10 + i);
			DIGITS['a' + i] = (byte) (10 + i);
		}
	}
	public static String version(Context c) {
		String v = "?";
		try {
			v = c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionName;
		}
		catch (Throwable t) {
			v = t.toString();
		}
		return v;
	}
	public static String pad(String text, int length, char ch) {
		length = length - text.length();
		for (int i = 0; i < length; i++) {
			text = ch + text;
		}
		return text;
	}
	public static int transparent(int color, double transparency) {
		int r = color;
		int t = (int) (255.0 * transparency);
		r = (color & 0x00ffffff) + (t << 24);
		return r;
	}
	public static void initThemeConstants(Context context) {
		TypedArray array = context.getTheme().obtainStyledAttributes(new int[] { //
				android.R.attr.colorBackground//
						, android.R.attr.textColorPrimary//
						, android.R.attr.textColorHint//
						, android.R.attr.textColorHighlight//
						, android.R.attr.textColorLink //
				});
		colorBackground = array.getColor(0, colorBackground);
		textColorPrimary = array.getColor(1, textColorPrimary);
		textColorHint = array.getColor(2, textColorHint);
		textColorHighlight = array.getColor(3, textColorHighlight);
		textColorLink = array.getColor(4, textColorLink);
		array.recycle();
		if ((textColorPrimary & 0x00ffffff) > 0x00666666) {//darkonlight
			colorLine = transparent(textColorPrimary, 0.2);
			colorSelection = transparent(textColorLink, 0.3);
		}
		else {//lightondark
			colorLine = transparent(textColorPrimary, 0.1);
			colorSelection = transparent(textColorLink, 0.2);
		}
		paintLine = new Paint();
		paintLine.setColor(Auxiliary.colorLine);
		paintLine.setAntiAlias(true);
		paintLine.setFilterBitmap(true);
		paintLine.setDither(true);
		//colorLine=transparent(textColorPrimary,0.2);
		//colorSelection=transparent(textColorLink,0.2);
		density = context.getResources().getDisplayMetrics().density;
		tapSize = (int) (60.0 * density);
	}
	public static void hideSoftKeyboard(Activity activity) {
		try {
			InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}
	public static boolean isOnline(Context c) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
				return true;
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}
	//public static SQLiteDatabase connectSQLiteDatabase(String path, Context c//
	//	, int mode//Context.MODE_WORLD_WRITEABLE
	//) {
	/*
	SQLiteOpenHelper openHelper = new SQLiteOpenHelper(c, path, null, version) {
		@Override
		public void onCreate(SQLiteDatabase db) {
			//
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//
		}
		@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//
		}
	};
	SQLiteDatabase r = openHelper.getWritableDatabase();
	*/
	//SQLiteDatabase	db = c.openOrCreateDatabase(path, mode, null);
	//return db;
	//}
	public static byte[] string2Hex(String hexString) {
		int length = hexString.length();
		if ((length & 0x01) != 0) {
			//throw new IllegalArgumentException("Odd number of characters: " + hexString);
			hexString = "0" + hexString;
			length++;
		}
		//boolean badHex = false;
		byte[] out = new byte[length >> 1];
		for (int i = 0, j = 0; j < length; i++) {
			int c1 = hexString.charAt(j);
			int c2 = hexString.charAt(j + 1);
			j = j + 2;
			byte d1 = DIGITS[c1];
			byte d2 = DIGITS[c2];
			if (c1 > 'f') {
				//badHex = true;
				//break;
				c1 = '0';
			}
			if (d1 == -1) {
				//badHex = true;
				//break;
				d1 = '0';
			}
			if (c2 > 'f') {
				//badHex = true;
				//break;
				c2 = '0';
			}
			if (d2 == -1) {
				//badHex = true;
				//break;
				d2 = '0';
			}
			out[i] = (byte) (d1 << 4 | d2);
		}
		/*if (badHex) {
			throw new IllegalArgumentException("Invalid hexadecimal digit: " + hexString);
		}*/
		return out;
	}
	public static String hex2String(byte[] array) {
		try {
			char[] cArray = new char[array.length * 2];
			for (int i = 0, j = 0; i < array.length; i++) {
				int index = array[i] & 0xFF;
				cArray[j++] = FIRST_CHAR[index];
				cArray[j++] = SECOND_CHAR[index];
			}
			return new String(cArray);
		}
		catch (Throwable t) {
			return "";
		}
	}
	public static Bough fromCursor(Cursor cursor) {
		return fromCursor(cursor, false);
	}
	public static Bough fromCursor(Cursor cursor, boolean parseDate) {
		Bough bough = new Bough().name.is("cursor");
		while (cursor.moveToNext()) {
			Bough row = new Bough().name.is("row");
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				String name = cursor.getColumnName(i);
				String value = null;
				try {
					value = cursor.getString(i);
					if (parseDate) {
						try {
							java.util.Date d = null;
							if (value.length() > 12) {
								d = sqliteTime.parse(value);
							}
							else {
								d = sqliteDate.parse(value);
							}
							value = "" + d.getTime();
						}
						catch (Throwable t) {
							//nor date nor time
						}
					}
				}
				catch (Throwable t) {
					//can't getString due blob
					byte[] b = cursor.getBlob(i);
					value = hex2String(b);
				}
				if (value == null) {
					value = "";
				}
				row.child(new Bough().name.is(name).value.is(value));
			}
			bough.child(row);
		}
		return bough;
	}
	public static String cursorString(Cursor cursor, String name) {
		String value = "";
		try {
			value = cursor.getString(cursor.getColumnIndex(name));
			if (value == null) {
				return "";
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return value;
	}
	public static double cursorDouble(Cursor cursor, String name) {
		double value = 0;
		try {
			value = cursor.getDouble(cursor.getColumnIndex(name));
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return value;
	}
	public static String cursorDate(Cursor cursor, String name) {
		String value = "";
		try {
			value = cursor.getString(cursor.getColumnIndex(name));
			if (value == null) {
				return "";
			}
			if (value.length() > 9) {
				java.util.Date d = sqliteDate.parse(value);
				value = "" + d.getTime();
			}
			else {
				value = "0";
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return value;
	}
	public static String cursorTime(Cursor cursor, String name) {
		String value = "";
		try {
			value = cursor.getString(cursor.getColumnIndex(name));
			if (value == null) {
				return "";
			}
			if (value.length() > 12) {
				java.util.Date d = sqliteTime.parse(value);
				value = "" + d.getTime();
			}
			else {
				value = "0";
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return value;
	}
	public static String cursorBlob(Cursor cursor, String name) {
		String value = "";
		try {
			byte[] b = cursor.getBlob(cursor.getColumnIndex(name));
			if (b == null) {
				return "";
			}
			value = hex2String(b);
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return value;
	}
	public static Bough fromStrictCursor(Cursor cursor, String[] strings, String[] dates, String[] times, String[] blobs) {
		Bough bough = new Bough().name.is("cursor");
		while (cursor.moveToNext()) {
			Bough row = new Bough().name.is("row");
			if (strings != null) {
				for (int f = 0; f < strings.length; f++) {
					String name = strings[f];
					row.child(new Bough().name.is(name).value.is(cursorString(cursor, name)));
				}
			}
			if (dates != null) {
				for (int f = 0; f < dates.length; f++) {
					String name = dates[f];
					row.child(new Bough().name.is(name).value.is(cursorDate(cursor, name)));
				}
			}
			if (times != null) {
				for (int f = 0; f < times.length; f++) {
					String name = times[f];
					row.child(new Bough().name.is(name).value.is(cursorTime(cursor, name)));
				}
			}
			if (blobs != null) {
				for (int f = 0; f < blobs.length; f++) {
					String name = blobs[f];
					row.child(new Bough().name.is(name).value.is(cursorBlob(cursor, name)));
				}
			}
			bough.child(row);
		}
		return bough;
	}
	public static String loadTextFromResource(Context context, int resourceID) {
		String txt = "";
		try {
			InputStream inputStream = context.getResources().openRawResource(resourceID);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			int i;
			i = inputStream.read();
			while (i != -1) {
				byteArrayOutputStream.write(i);
				i = inputStream.read();
			}
			inputStream.close();
			txt = byteArrayOutputStream.toString();
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return txt;
	}
	public static byte[] loadFileFromURL(String pathurl) throws Exception {
		InputStream input = null;
		ByteArrayOutputStream output = null;
		HttpURLConnection connection = null;
		URL url = new URL(pathurl);
		connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new Exception("Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage());
		}
		input = connection.getInputStream();
		output = new ByteArrayOutputStream();
		byte data[] = new byte[1024];
		int nn;
		while ((nn = input.read(data)) != -1) {
			output.write(data, 0, nn);
		}
		input.close();
		return output.toByteArray();
	}
	public static Bitmap loadBitmapFromURL(String url) {
		Bitmap bitmap = null;
		/*URL m;
		InputStream i = null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream out = null;*/
		try {
			/*m = new URL(url);
			i = (InputStream) m.getContent();
			bis = new BufferedInputStream(i, 1024 * 8);
			out = new ByteArrayOutputStream();
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = bis.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.close();
			bis.close();
			byte[] data = out.toByteArray();*/
			byte[] data = loadFileFromURL(url);
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return bitmap;
	}
	public static void inform(String s, Context context) {
		System.out.println("inform: " + s);
		Toast.makeText(context, s, Toast.LENGTH_LONG).show();
	}
	public static void warn(String s, Context context) {
		System.out.println("warn: " + s);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(s);
		builder.create().show();
	}
	public static void pickText(Context context//
			, String title//
			, final Note text//
			, String positiveButtonTitle//
			, final Task callbackPositiveBtn) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		final EditText input = new EditText(context);
		input.setSingleLine(false);
		input.setMinLines(3);
		input.setGravity(android.view.Gravity.LEFT | android.view.Gravity.TOP);
		input.setText(text.value());
		//input.setInputType(EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
		builder.setView(input);
		builder.setPositiveButton(positiveButtonTitle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				text.value("" + input.getText());
				if (callbackPositiveBtn != null) {
					callbackPositiveBtn.start();
				}
			}
		});
		builder.create().show();
	}
	public static void pickString(Context context//
			, String title//
			, final Note text//
			, String positiveButtonTitle//
			, final Task callbackPositiveBtn) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		final EditText input = new EditText(context);
		input.setText(text.value());
		input.setSingleLine(true);
		//input.setEllipsize(TextUtils.TruncateAt.END);
		//input.setInputType(EditorInfo.type_text_fl.TYPE_TEXT_FLAG_MULTI_LINE);
		builder.setView(input);
		builder.setPositiveButton(positiveButtonTitle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				text.value("" + input.getText());
				if (callbackPositiveBtn != null) {
					callbackPositiveBtn.start();
				}
			}
		});
		builder.create().show();
	}
	public static void pickNumber(Context context//
			, String title//
			, final Numeric num//
			, String positiveButtonTitle//
			, final Task callbackPositiveBtn//
			, String neutralButtonTitle//
			, final Task callbackNeutralBtn//
	) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		final EditText input = new EditText(context);
		input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		input.setText("" + num.value());
		builder.setView(input);
		if (callbackPositiveBtn != null) {
			builder.setPositiveButton(positiveButtonTitle, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					double nn = num.value();
					try {
						nn = Double.parseDouble(input.getText().toString());
					}
					catch (Throwable t) {
						t.printStackTrace();
					}
					num.value(nn);
					callbackPositiveBtn.start();
				}
			});
		}
		if (callbackNeutralBtn != null) {
			builder.setNeutralButton(neutralButtonTitle, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					callbackNeutralBtn.start();
				}
			});
		}
		builder.create().show();
	}
	public static void pickSingleChoice(Context context, CharSequence[] items, final Numeric defaultSelection) {
		pickSingleChoice(context, items, defaultSelection, null, null, null, null, null, null);
	}
	public static void pickSingleChoice(Context context, CharSequence[] items, final Numeric defaultSelection//
			, String title//
			, final Task afterSelect//
			, String positiveButtonTitle//
			, final Task callbackPositiveBtn//
			, String neutralButtonTitle//
			, final Task callbackNeutralBtn//
	) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		int nn = -1;
		if (defaultSelection != null) {
			nn = defaultSelection.value().intValue();
		}
		if (title != null) {
			builder.setTitle(title);
		}
		builder.setSingleChoiceItems(items, nn, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (defaultSelection != null) {
					defaultSelection.value(which);
				}
				if (afterSelect != null) {
					afterSelect.start();
				}
			}
		});
		if (callbackPositiveBtn != null) {
			builder.setPositiveButton(positiveButtonTitle, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					callbackPositiveBtn.start();
				}
			});
		}
		if (callbackNeutralBtn != null) {
			builder.setNeutralButton(neutralButtonTitle, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					callbackNeutralBtn.start();
				}
			});
		}
		builder.create().show();
	}
	public static void pickMultiChoice(Context context, CharSequence[] items, final They<Integer> defaultSelection//
	) {
		pickMultiChoice(context, items, defaultSelection, null, null);
	}
	public static void pickMultiChoice(Context context, CharSequence[] items, final They<Integer> defaultSelection//
			, String positiveButtonTitle, final Task callbackPositiveBtn) {
		if (items.length > 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			boolean[] checks = new boolean[items.length];
			for (int i = 0; i < defaultSelection.size(); i++) {
				int n = defaultSelection.at(i);
				if (n >= 0 && n < checks.length) {
					checks[n] = true;
				}
			}
			builder.setMultiChoiceItems(items, checks, new DialogInterface.OnMultiChoiceClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					if (isChecked) {
						for (int i = 0; i < defaultSelection.size(); i++) {
							int n = defaultSelection.at(i);
							if (n == which) {
								return;
							}
						}
						//System.out.println("insert "+which);
						defaultSelection.insert(0, which);
					}
					else {
						for (int i = 0; i < defaultSelection.size(); i++) {
							int n = defaultSelection.at(i);
							if (n == which) {
								//System.out.println("drop "+n+" at "+i);
								defaultSelection.delete(defaultSelection.at(i));
								return;
							}
						}
					}
				}
			});
			if (callbackPositiveBtn != null) {
				builder.setPositiveButton(positiveButtonTitle, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (callbackPositiveBtn != null) {
							callbackPositiveBtn.start();
						}
					}
				});
			}
			builder.create().show();
		}
	}
	public static void pickConfirm(Context context//
			, String message//
			, String positiveButtonTitle//
			, final Task callbackPositiveBtn//
	) {
		pick3Choice(context, null, message, positiveButtonTitle, callbackPositiveBtn, null, null, null, null);
	}
	public static void pick3Choice(Context context//
			, String title//
			, String message//
			, String positiveButtonTitle//
			, final Task callbackPositiveBtn//
			, String neutralButtonTitle//
			, final Task callbackNeutralBtn//
			, String negativeButtonTitle//
			, final Task callbackNegativeBtn//
	) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		if (title != null) {
			dialogBuilder.setTitle(title);
		}
		if (message != null) {
			dialogBuilder.setMessage(message);
		}
		dialogBuilder.setNegativeButton(negativeButtonTitle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (callbackNegativeBtn != null) {
					callbackNegativeBtn.start();
				}
			}
		});
		dialogBuilder.setNeutralButton(neutralButtonTitle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (callbackNeutralBtn != null) {
					callbackNeutralBtn.start();
				}
			}
		});
		dialogBuilder.setPositiveButton(positiveButtonTitle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (callbackPositiveBtn != null) {
					callbackPositiveBtn.start();
				}
			}
		});
		dialogBuilder.create().show();
	}
	public static void pick(Context context//
			, String title//
			, final Rake rake//
			, String positiveButtonTitle//
			, final Task callbackPositiveBtn//
			, String neutralButtonTitle//
			, final Task callbackNeutralBtn//
			, String negativeButtonTitle//
			, final Task callbackNegativeBtn//
	) {
		//final Vector<Task>dumbGoogle=new  Vector<Task>();
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		final RelativeLayout rl = new RelativeLayout(context);
		dialogBuilder.setView(rl);
		//final View toRemove=null;
		if (title != null) {
			dialogBuilder.setTitle(title);
		}
		if (rake != null) {
			if (rake.view() != null) {
				//toRemove=rake.view();
				rl.setMinimumWidth(rake.width().property.value().intValue());
				rl.setMinimumHeight(rake.height().property.value().intValue());
				//dialogBuilder.setView(rake.view());
				//dialogBuilder.setView(null);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
						rake.width().property.value().intValue()//
						, rake.height().property.value().intValue());
				//params.leftMargin = (int) (left.property.value() + dragX.property.value());
				//params.topMargin = (int) (top.property.value() + dragY.property.value());
				rake.view().setLayoutParams(params);
				rl.addView(rake.view());
			}
		}
		dialogBuilder.setNegativeButton(negativeButtonTitle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//dumbGoogle.get(0).start();
				if (rake != null) {
					rl.removeView(rake.view());
				}
				dialog.dismiss();
				if (callbackNegativeBtn != null) {
					callbackNegativeBtn.start();
				}
			}
		});
		dialogBuilder.setNeutralButton(neutralButtonTitle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//dumbGoogle.get(0).start();
				if (rake != null) {
					rl.removeView(rake.view());
				}
				dialog.dismiss();
				if (callbackNeutralBtn != null) {
					callbackNeutralBtn.start();
				}
			}
		});
		dialogBuilder.setPositiveButton(positiveButtonTitle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//dumbGoogle.get(0).start();
				if (rake != null) {
					rl.removeView(rake.view());
				}
				dialog.dismiss();
				if (callbackPositiveBtn != null) {
					callbackPositiveBtn.start();
				}
			}
		});
		dialogBuilder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				//dumbGoogle.get(0).start();
				if (rake != null) {
					rl.removeView(rake.view());
				}
			}
		});
		final AlertDialog d = dialogBuilder.create();
		d.show();
	}
	public static String strings2text(Vector<String> s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.size(); i++) {
			sb.append(s.get(i));
			sb.append("\n");
		}
		return sb.toString();
	}
	public static Vector<String> readTextFromFile(File file) {
		return readTextFromFile(file, "UTF-8");
	}
	public static Vector<String> readTextFromFile(File file, String encoding) {
		Vector<String> result = new Vector<String>();
		try {
			//BufferedReader input = new BufferedReader(new FileReader(aFile));
			BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
			try {
				String line = null;
				while ((line = input.readLine()) != null) {
					result.add(line);
					//contents.append(System.getProperty("line.separator"));
				}
			}
			finally {
				input.close();
			}
		}
		catch (Throwable t) {
			System.out.println(t.getMessage());
		}
		return result;
	}
	public static boolean writeTextToFile(File aFile, String aContents) {
		try {
			Writer output = new BufferedWriter(new FileWriter(aFile));
			output.write(aContents);
			output.flush();
			output.close();
		}
		catch (Throwable t) {
			System.out.println(t.getMessage());
		}
		return false;
	}
	public static boolean writeTextToFile(File aFile, String aContents, String charset) {
		try {
			FileOutputStream fos = new FileOutputStream(aFile);
			fos.write(aContents.getBytes(charset));
			fos.flush();
			fos.close();
		}
		catch (Throwable t) {
			System.out.println(t.getMessage());
		}
		return false;
	}
	/*
		public static void pick(Context context, CharSequence[] items, final Numeric defaultSelection) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (defaultSelection != null) {
						defaultSelection.value(which);
					}
				}
			});
			builder.create().show();
		}*/
	public static void startFile(Activity activity, File file) {
		String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl("file://" + file.getAbsolutePath()));
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(android.net.Uri.fromFile(file), mime);
		Intent chooser = Intent.createChooser(intent, file.getAbsolutePath());
		activity.startActivity(chooser);
	}
	public static void startFile(Activity activity, String action, String mime, File file) {
		//String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl("file://" + path));
		Intent intent = new Intent();
		intent.setAction(action);//android.content.Intent.ACTION_VIEW
		intent.setDataAndType(android.net.Uri.fromFile(file), mime);//"text/html"
		//startActivity(intent);
		Intent chooser = Intent.createChooser(intent, file.getAbsolutePath());
		activity.startActivity(chooser);
	}
	public static int screenWidth(Activity activity) {
		int w = 0;
		try {
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			w = dm.widthPixels;
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return w;
	}
	public static Bitmap screenshot(View v) {
		v.setDrawingCacheEnabled(true);
		return v.getDrawingCache();
	}
	public static int screenHeight(Activity activity) {
		int h = 0;
		try {
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			h = dm.heightPixels;
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return h;
	}
	public static Bitmap scaledBitmapFromResource(Context context, int id, int width, int height) {
		Bitmap b = null;
		try {
			b = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), id), width, height, true);
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return b;
	}
	public static Bitmap bitmapFromResource(Context context, int id) {
		Bitmap b = null;
		try {
			b = BitmapFactory.decodeResource(context.getResources(), id);
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return b;
	}
	public static void createAbsolutePathForFolder(String path) {
		String s = File.separator;
		String[] names = path.split(File.separator);
		String r = names[0];
		for (int i = 1; i < names.length; i++) {
			r = r + File.separator + names[i];
			new File(r).mkdirs();
		}
	}
	public static void createAbsolutePathForFile(String path) {
		String s = File.separator;
		String[] names = path.split(File.separator);
		String r = names[0];
		for (int i = 1; i < names.length - 1; i++) {
			r = r + File.separator + names[i];
			new File(r).mkdirs();
		}
	}
	public static void exportResource(Context context, String path, int id) {
		if (!(new File(path)).exists()) {
			try {
				System.out.println("exportResource write " + path);
				/*if (!(new File(path)).exists()) {
				System.out.println(	path+": "+new File(path+name).mkdirs());
				}*/
				byte[] buffer = null;
				InputStream fIn = context.getResources().openRawResource(id);
				int size = 0;
				size = fIn.available();
				buffer = new byte[size];
				fIn.read(buffer);
				fIn.close();
				FileOutputStream save;
				save = new FileOutputStream(path);
				save.write(buffer);
				save.flush();
				save.close();
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
		}
		else {
			System.out.println("exportResource skip " + path);
		}
	}
	public static java.util.Date date(String mills) {
		if (mills == null) {
			return null;
		}
		if (mills.length() == 0) {
			return null;
		}
		java.util.Date d = new java.util.Date();
		d.setTime((long) Numeric.string2double(mills));
		return d;
	}
	public static boolean startSensorEventListener(Activity activity, final Task task) {
		try {
			SensorManager sensorManager = (SensorManager) activity.getSystemService(android.content.Context.SENSOR_SERVICE);
			sensorEventListener = new SensorEventListener() {
				@Override
				public void onSensorChanged(SensorEvent event) {
					//System.out.println("Auxiliary.startSensorEventListener.onSensorChanged " + event);
					try {
						if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
							if (Math.abs(accelerometerX - event.values[0]) > accelerometerNoise//
									|| Math.abs(accelerometerY - event.values[1]) > accelerometerNoise//
									|| Math.abs(accelerometerZ - event.values[2]) > accelerometerNoise//
							) {
								accelerometerX = event.values[0];
								accelerometerY = event.values[1];
								accelerometerZ = event.values[2];
								task.start();
							}
						}
					}
					catch (Throwable t) {
						t.printStackTrace();
					}
				}
				@Override
				public void onAccuracyChanged(Sensor sensor, int accuracy) {
					//System.out.println("Auxiliary.startSensorEventListener.onAccuracyChanged " + accuracy + ": " + sensor);
				}
			};
			sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
			return true;
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		sensorEventListener = null;
		return false;
	}
	public static boolean stopSensorEventListener(Activity activity) {
		if (sensorEventListener == null) {
			return true;
		}
		try {
			SensorManager sensorManager = (SensorManager) activity.getSystemService(android.content.Context.SENSOR_SERVICE);
			sensorManager.unregisterListener(sensorEventListener);
			return true;
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}
	public static Bough activityExatras(Activity activity) {
		Intent intent = activity.getIntent();
		Bundle extras = intent.getExtras();
		return bundle2bough(extras);
	}
	public static Bough bundle2bough(Bundle bundle) {
		Bough bough = new Bough();
		if (bundle == null) {
			bough.name.is("null");
		}
		else {
			bough.name.is("extra");
			for (String key : bundle.keySet()) {
				String value = bundle.getString(key);
				//System.out.println(key + ": " + value);
				bough.child(key).value.is(value);
			}
		}
		return bough;
	}
}
