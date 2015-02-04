package reactive.ui;

import android.view.*;
import android.app.*;
import android.app.KeyguardManager.*;
import android.app.admin.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
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
	Layoutless layoutless;
	Decor d;
	void initAll() {
		layoutless.child(new Knob(this).labelText.is("a " + new Date())//
				.afterTap.is(new Task() {
					@Override
					public void doTask() {
						//Demo.this.startActivity(new Intent(Demo.this, Demo.class));				
						//Demo.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
						Intent intent = new Intent(Demo.this, Demo.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						Bundle bundle = new Bundle();
						bundle.putString("key", "value");
						intent.putExtras(bundle);
						Demo.this.startActivity(intent);
					}
				}).width().is(400).height().is(100));
		layoutless.child(new Knob(this).labelText.is("d " + new Date())//
				.afterTap.is(new Task() {
					@Override
					public void doTask() {
						Bitmap b = Auxiliary.screenshot(layoutless);
						
						d.bitmap.is(Bitmap.createScaledBitmap(b,200,200,true));
					}
				}).left().is(400).top().is(0).width().is(400).height().is(100));
		
		 d=new Decor(this);
		layoutless.child(d.labelText.is("d " + new Date())//
//				.bitmap.is(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rocket),200,200,true))
				.left().is(200).top().is(200).width().is(200).height().is(200));
		
		
		layoutless.child(new Knob(this).labelText.is("1").left().is(Math.random() * 1000).top().is(Math.random() * 500 + 100).width().is(Math.random() * 100 + 50).height()
				.is(Math.random() * 100 + 50));
		layoutless.child(new Knob(this).labelText.is("2").left().is(Math.random() * 1000).top().is(Math.random() * 500 + 100).width().is(Math.random() * 100 + 50).height()
				.is(Math.random() * 100 + 50));
		layoutless.child(new Knob(this).labelText.is("3").left().is(Math.random() * 1000).top().is(Math.random() * 500 + 100).width().is(Math.random() * 100 + 50).height()
				.is(Math.random() * 100 + 50));
		layoutless.child(new Knob(this).labelText.is("4").left().is(Math.random() * 1000).top().is(Math.random() * 500 + 100).width().is(Math.random() * 100 + 50).height()
				.is(Math.random() * 100 + 50));
		layoutless.child(new Knob(this).labelText.is("5").left().is(Math.random() * 1000).top().is(Math.random() * 500 + 100).width().is(Math.random() * 100 + 50).height()
				.is(Math.random() * 100 + 50));
		layoutless.child(new Knob(this).labelText.is("6").left().is(Math.random() * 1000).top().is(Math.random() * 500 + 100).width().is(Math.random() * 100 + 50).height()
				.is(Math.random() * 100 + 50));
		layoutless.child(new Knob(this).labelText.is("7").left().is(Math.random() * 1000).top().is(Math.random() * 500 + 100).width().is(Math.random() * 100 + 50).height()
				.is(Math.random() * 100 + 50));
		layoutless.child(new Knob(this).labelText.is("8").left().is(Math.random() * 1000).top().is(Math.random() * 500 + 100).width().is(Math.random() * 100 + 50).height()
				.is(Math.random() * 100 + 50));
		layoutless.child(new Knob(this).labelText.is("9").left().is(Math.random() * 1000).top().is(Math.random() * 500 + 100).width().is(Math.random() * 100 + 50).height()
				.is(Math.random() * 100 + 50));
		layoutless.child(new Knob(this).labelText.is("10").left().is(Math.random() * 1000).top().is(Math.random() * 500 + 100).width().is(Math.random() * 100 + 50).height()
				.is(Math.random() * 100 + 50));
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate");
		System.out.println(Auxiliary.bundle2bough(this.getIntent().getExtras()).dumpXML());
		//super.setTheme( android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		super.setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
		//super.setTheme( android.R.style.Theme_Translucent_NoTitleBar);
		//getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		super.onCreate(savedInstanceState);
		layoutless = new Layoutless(this);
		setContentView(layoutless);
		Animation anim = new TranslateAnimation(Auxiliary.screenWidth(this), 0, 0, 0);
		//Animation anim = new TranslateAnimation(0, 0, Auxiliary.screenHeight(this), 0);
		//ScaleAnimation(0, 1, 0, 1, 200, 200);
		anim.setDuration(300); // duration = 300
		//getWindow().getDecorView().startAnimation(anim);
		initAll();
		layoutless.startAnimation(anim);
		//getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}
	@Override
	protected void onPause() {
		System.out.println("onPause");
		super.onPause();
		//Preferences.save();
	}
	@Override
	protected void onResume() {
		System.out.println("onResume");
		super.onResume();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		System.out.println("onCreateOptionsMenu");
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		System.out.println("onOptionsItemSelected");
		return this.onOptionsItemSelected(item);
	}
}
