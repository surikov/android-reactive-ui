package reactive.ui;

import android.view.*;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.*;
import android.text.InputType;
import android.text.TextUtils;
import android.util.*;
import android.view.*;
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
	private static final char[] FIRST_CHAR = new char[256];
	private static final char[] SECOND_CHAR = new char[256];
	private static final char[] HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static final byte[] DIGITS = new byte['f' + 1];
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
		boolean first = true;
		Bough bough = new Bough().name.is("cursor");
		//cursor.moveToFirst();
		/*SimpleDateFormat dateFormat = null;
		if (datePattern != null) {
			try {
				dateFormat = new SimpleDateFormat(datePattern);
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
		}*/
		SimpleDateFormat bigDate = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss.SSS");
		SimpleDateFormat smallDate = new SimpleDateFormat("yyyy-MM-DD");
		while (cursor.moveToNext()) {
			if (first) {
				first = false;
				//System.out.println("Auxiliary.fromCursor started");
			}
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
								d = bigDate.parse(value);
							}
							else {
								d = smallDate.parse(value);
							}
							//value);
							value = "" + d.getTime();
							//System.out.println(name+": "+d);
						}
						catch (Throwable t) {
							//System.out.println(name+": "+t.getMessage());
						}
					}
				}
				catch (Throwable t) {
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
		//System.out.println("Auxiliary.fromCursor done");
		return bough;
	}
	public static Bitmap loadBitmapFromURL(String url) {
		Bitmap bitmap = null;
		URL m;
		InputStream i = null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream out = null;
		try {
			m = new URL(url);
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
			byte[] data = out.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		}
		catch (Throwable t) {
			//
		}
		return bitmap;
	}
	public static void inform(String s, Context context) {
		Toast.makeText(context, s, Toast.LENGTH_LONG).show();
	}
	public static void warn(String s, Context context) {
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
	public static void startFile(Activity activity, String action, String mime, File file) {
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
}
