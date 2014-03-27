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
	Numeric xx = new Numeric();
	Numeric yy = new Numeric();
	double preX = 0;
	double preY = 0;
	double preZ = 0;

	public static String version() {
		return "1.40";
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("start onCreate");
		super.onCreate(savedInstanceState);
		layoutless = new Layoutless(this);
		this.setContentView(layoutless);
		layoutless.child(new Decor(this).background.is(0xffcc9966)//
				.left().is(10)//
				.top().is(10)//
				.width().is(1250)//
				.height().is(700)//
				);
		final BigGrid bg = new BigGrid(this);
		/*
		 * layoutless.child(bg//
		 * 
		 * //.data()// .left().is(10)// .top().is(10)// .width().is(1250)//
		 * .height().is(700)// );
		 */
		layoutless.child(new SplitLeftRight(this)//
				.left().is(0)//
				.top().is(0)//
				.width().is(layoutless.width().property)//
				.height().is(layoutless.height().property)//
				);
		SketchPlate sk = new SketchPlate()//
		.background.is(0x9900ffff)//
		.left.is(0)//
		.top.is(0)//
		.width.is(1000)//
		.height.is(1000)//
		;
		for (int i = 0; i < 999; i++) {
			sk.child(new SketchLine().strokeColor.is(0x66ff00ff).strokeWidth.is(5)//
					.point(Math.random() * 700, Math.random() * 500)//
					.point(Math.random() * 700, Math.random() * 500)//
			);
		}
		/*
		 * layoutless.child(new Decor(this)// .sketch(new Sketches()//
		 * .child(sk)// )// .left().is(layoutless.shiftX.property)//
		 * .top().is(layoutless.shiftY.property)// .width().is(700)//
		 * .height().is(500)// );
		 */
		layoutless.child(new Decor(this)//
				.background.is(0x660000ff)//
						.left().is(xx.multiply(2).plus(200))//
						.top().is(yy.multiply(2).plus(100))//
						.width().is(100)//
						.height().is(100)//
				);
		layoutless.child(new Decor(this)//
				.background.is(0x6600ff00)//
						.left().is(xx.plus(200))//
						.top().is(yy.plus(100))//
						.width().is(100)//
						.height().is(100)//
				);
		layoutless.child(new Decor(this)//
				.background.is(0x66ff0000)//
						.left().is(200)//
						.top().is(100)//
						.width().is(100)//
						.height().is(100)//
				);
		Bitmap b1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rocket), 100, 100, true);
		Bitmap b2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.email), 100, 100, true);
		// Bitmap b3 =
		// Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
		// R.drawable.refresh), 100, 100, true);
		NativeBitmap n1 = new NativeBitmap();
		n1.storeBitmap(b1);
		final NativeBitmap n2 = new NativeBitmap();
		n2.storeBitmap(b2);
		final It<Bitmap> b = new It<Bitmap>();
		b1.recycle();
		b2.recycle();
		b.value(n1.getBitmap());
		layoutless.child(new Decor(this)//
				.bitmap.is(b)//
				.background.is(0x6600ff00)//
						.left().is(xx.plus(250))//
						.top().is(yy.plus(200))//
						.width().is(100)//
						.height().is(100)//
				);
		layoutless.child(new Knob(this)//
				.labelText.is("test")//
				.afterTap.is(new Task() {

					@Override
					public void doTask() {
						b.value(n2.getBitmap());
						Vector<Bitmap> bs = new Vector<Bitmap>();
						Vector<NativeBitmap> nat = new Vector<NativeBitmap>();
						for (int nn = 0; nn < 10; nn++) {
							for (int i = 0; i < 10; i++) {
								Bitmap b0 = BitmapFactory.decodeResource(getResources(), R.drawable.rocket);
								Bitmap b1 = Bitmap.createScaledBitmap(b0, 500, 500, true);
								NativeBitmap na = new NativeBitmap();
								na.storeBitmap(b1);
								b0.recycle();
								b1.recycle();
								// bs.add(b1);
								nat.add(na);
							}
						}

					}
				})//
						.left().is(xx.plus(50))//
						.top().is(yy.plus(50))//
						.width().is(100)//
						.height().is(100)//
				);
		// System.out.println("NativeBitmap is "+new
		// NativeBitmap().getVersion());
		System.out.println("done onCreate");
	}

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
	protected void onPause() {
		Auxiliary.stopSensorEventListener(this);
		super.onPause();
	}

	@Override
	protected void onResume() {
		Auxiliary.startSensorEventListener(this, new Task() {
			@Override
			public void doTask() {
				xx.value(-Auxiliary.accelerometerX);
				yy.value(Auxiliary.accelerometerY);
				System.out.println("accelerometr " + Auxiliary.accelerometerX + ", " + Auxiliary.accelerometerY + ", " + Auxiliary.accelerometerZ);
			}
		});
		super.onResume();
	}
}
