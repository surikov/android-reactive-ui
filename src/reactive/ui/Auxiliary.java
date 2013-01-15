package reactive.ui;

import android.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.text.InputType;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.*;
import reactive.ui.*;

import android.content.res.*;
import android.view.animation.*;
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
	public static SQLiteDatabase connectSQLiteDatabase(String path, Context c, int version) {
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
		return r;
	}
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
		char[] cArray = new char[array.length * 2];
		for (int i = 0, j = 0; i < array.length; i++) {
			int index = array[i] & 0xFF;
			cArray[j++] = FIRST_CHAR[index];
			cArray[j++] = SECOND_CHAR[index];
		}
		return new String(cArray);
	}
	public static Bough fromCursor(Cursor cursor) {
		Bough bough = new Bough().name.is("cursor");
		//cursor.moveToFirst();
		while (cursor.moveToNext()) {
			Bough row = new Bough().name.is("row");
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				String name = cursor.getColumnName(i);
				String value = "";
				try {
					value = cursor.getString(i);
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
	public static void pick(Context context, String title, final Note text, String positiveButtonTitle, final Task callbackPositiveBtn) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		final EditText input = new EditText(context);
		input.setText(text.value());
		builder.setView(input);
		builder.setPositiveButton(positiveButtonTitle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				text.value("" + input.getText());
				callbackPositiveBtn.start();
			}
		});
		builder.create().show();
	}
	public static void pick(Context context, String title, final Numeric num//
			, String positiveButtonTitle, final Task callbackPositiveBtn//
			, String neutralButtonTitle, final Task callbackNeutralBtn//
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
	public static void pick(Context context, CharSequence[] items, final Numeric defaultSelection) {
		pick(context, items, defaultSelection, null, null, null, null, null, null);
	}
	public static void pick(Context context, CharSequence[] items, final Numeric defaultSelection//
			, String title//
			, final Task afterSelect, String positiveButtonTitle//
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
				if (defaultSelection != null) {
					defaultSelection.value(which);
				}
				dialog.dismiss();
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
	public static void pick(Context context, CharSequence[] items, final They<Integer> defaultSelection//
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
					//System.out.println("onClick "+which+"/"+isChecked);
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
	public static void pick(Context context//
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
				if (callbackNegativeBtn != null) {
					callbackNegativeBtn.start();
				}
				dialog.dismiss();
			}
		});
		dialogBuilder.setNeutralButton(neutralButtonTitle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (callbackNeutralBtn != null) {
					callbackNeutralBtn.start();
				}
				dialog.dismiss();
			}
		});
		dialogBuilder.setPositiveButton(positiveButtonTitle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (callbackPositiveBtn != null) {
					callbackPositiveBtn.start();
				}
				dialog.dismiss();
			}
		});
		dialogBuilder.create().show();
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
}
