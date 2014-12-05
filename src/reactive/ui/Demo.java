package reactive.ui;

import android.view.*;
import android.app.*;
import android.app.KeyguardManager.*;
import android.app.admin.*;
import android.content.*;
import android.graphics.*;
import android.opengl.*;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
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
//import com.example.android.apis.R;
import android.database.*;
import android.database.sqlite.*;
import reactive.ui.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.channels.FileChannel;
import android.view.animation.*;
import android.view.inputmethod.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;
import tee.binding.*;
import java.io.*;
import java.text.*;
import javax.microedition.khronos.opengles.GL10;

public class Demo extends Activity {
	Layoutless la;
	Numeric bowX = new Numeric();
	Numeric bowY = new Numeric();
	void initall() {
		System.out.println("init a");
		//
		//Preferences.
		la = new Layoutless(this);
		setContentView(la);
		//
		int w = Auxiliary.screenWidth(this);
		int h = Auxiliary.screenHeight(this);
		Preferences.init(this);
		bowX.bind(Preferences.integer("bowX", Auxiliary.tapSize, 0, w - Auxiliary.tapSize * 5));
		bowY.bind(Preferences.integer("bowY", Auxiliary.tapSize, 0, h - Auxiliary.tapSize * 5));
		System.out.println("bowX " + bowX.value());
		System.out.println("bowY " + bowY.value());
		/*
		System.out.println("w " + w);
		System.out.println("h " + h);
		*/
		//
		int squareSize = w;
		int marginTop = (h - w) / 2;
		int marginLeft = 0;
		if (h < w) {
			marginLeft = (w - h) / 2;
			marginTop = 0;
			squareSize = h;
		}
		/*
		System.out.println("squareSize " + squareSize);
		System.out.println("marginTop " + marginTop);
		System.out.println("marginLeft " + marginLeft);
		System.out.println(Auxiliary.tapSize);
		*/
		//
		//
		la.child(new Decor(this)//
		.background.is(0xffffffff)//
				.left().is(0)//
				.top().is(0)//
				.width().is(la.width().property)//
				.height().is(la.height().property)//
		);
		la.child(new Decor(this)//
		.bitmap.is(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bowgo_draw)//
				, Auxiliary.tapSize, Auxiliary.tapSize, true))//
		.movableX.is(true)//
		.movableY.is(true)//
		.dragX.is(bowX)//
		.dragY.is(bowY)//
				.left().is(bowX)//
				.top().is(bowY)//
				.width().is(300)//
				.height().is(300)//
		);
		la.child(new Decor(this)//
		.bitmap.is(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bowgo_go)//
				, Auxiliary.tapSize, Auxiliary.tapSize, true))//
		.afterTap.is(new Task() {
			@Override
			public void doTask() {
				System.out.println("bowX " + bowX.value());
				System.out.println("bowY " + bowY.value());
			}
		})//
				.left().is(la.width().property.multiply(0.5).minus(Auxiliary.tapSize * 0.5))//
				.top().is(la.height().property.minus(new Numeric().value(Auxiliary.tapSize).multiply(1.5)))//
				.width().is(Auxiliary.tapSize)//
				.height().is(Auxiliary.tapSize)//
		);
		la.child(new Decor(this)//
		.bitmap.is(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bowgo_archer)//
				, 200, 200, true))//
				.left().is(marginLeft)//
				.top().is(la.height().property.minus(200 + 100).minus(marginTop))//
				.width().is(200)//
				.height().is(200)//
		);
		la.child(new Decor(this)//
		.bitmap.is(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bowgo_target)//
				, 200, 200, true))//
				.left().is(la.width().property.minus(200).minus(marginLeft))//
				.top().is(la.height().property.minus(200 + 100).minus(marginTop))//
				.width().is(200)//
				.height().is(200)//
		);
		la.child(new Knob(this)//
		.labelText.is("test")//
				.left().is(100)//
				.top().is(50)//
				.width().is(200)//
				.height().is(50)//
		);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate");
		super.onCreate(savedInstanceState);
		initall();
	}
	@Override
	protected void onPause() {
		System.out.println("onPause");
		super.onPause();
		Preferences.save();
		System.out.println("bowX " + bowX.value());
		System.out.println("bowY " + bowY.value());
	}
	@Override
	protected void onResume() {
		System.out.println("onResume");
		super.onResume();
	}
}
