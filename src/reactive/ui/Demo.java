package reactive.ui;

import android.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.Bundle;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import reactive.ui.*;
import reactive.ui.library.LayoutlessView;
import android.view.animation.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;
import java.io.*;
import java.text.*;

public class Demo extends Activity {
	Layoutless view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("go----------------------------------");
		view = new Layoutless(this);
		Bitmap b = Bitmap.createScaledBitmap(//
				BitmapFactory.decodeResource(getResources(), R.drawable.rocket)//
				, 200, 100//
				, true//
				);
		view//
		.maxZoom.is(3).zoom.is(view.zoom.property).innerHeight.is(1000).innerWidth.is(1500)
		//
				.child(new Decor(this)//
				.labelText.is("Very long string for testing purpose only.")//
						.labelStyleLargeNormal()//
				.left.is(view.shiftX.property.plus(100))//
				.top.is(view.shiftY.property)//
				.width.is(view.zoom.property.multiply(50).plus(540))//
				.height.is(view.zoom.property.multiply(50).plus(400))//
				//.left.is(100)
				//.gravity.is(Gravity.CENTER)//
				.bitmap.is(b)//
				.background.is(Color.RED)//
						//.foreground.is(Color.YELLOW)//
						//.textAppearance.is(android.R.style.TextAppearance_Large)//
						//.labelFace.is(Typeface.createFromAsset(getAssets(), "fonts/deftone.ttf"))//
						.sketch(new SketchPlate()//
						//.strokeWidth.is(5)//
						.background.is(0x6600ffff)//
						//.strokeColor.is(0xff00ff00)//
						.width.is(250)//
						.height.is(350)//
						.top.is(10)//
						.left.is(30)//
						)//
						.sketch(new SketchContour()//
						.strokeWidth.is(1)//
						//.background.is(0x6600ffff)//
						.strokeColor.is(0xff00ff00)//
						.width.is(250)//
						.height.is(150)//
						.top.is(10)//
						.left.is(30)//
						.arcX.is(48)//
						.arcY.is(48)//
						)//
				/*.afterTap.is(new Task() {
					@Override
					public void doTask() {
						//System.out.println("tap decor");
					}
				})*/
				.afterShift.is(new Task() {
					@Override
					public void doTask() {
						//System.out.println("shift decor");
					}
				})//
				)//
		.afterTap.is(new Task() {
			@Override
			public void doTask() {
				//System.out.println("tap view " + view.tapX.property.value() + "x" + view.tapY.property.value());
			}
		})//
		.afterShift.is(new Task() {
			@Override
			public void doTask() {
				//System.out.println("shift view " + view.shiftX.property.value() + "x" + view.shiftY.property.value());
			}
		})//
		.afterZoom.is(new Task() {
			@Override
			public void doTask() {
				//System.out.println("zoom view " + view.zoom.property.value());
			}
		})//
				.child(new Decor(this)//
				.top.is(view.shiftY.property.plus(200))//
				.left.is(view.shiftX.property.plus(400))//
				.width.is(view.zoom.property.multiply(50).plus(140))//
				.height.is(view.zoom.property.multiply(50).plus(100))//
				.background.is(0xff00ff66)
				//.active.is(true)//
				.movableX.is(true)//
				.movableY.is(true)//
				/*.afterTap.is(new Task() {
					@Override
					public void doTask() {
						System.out.println("tap green");
					}
				})*/
				)//
				.child(new Decor(this)//
				.top.is(view.shiftY.property.plus(250))//
				.left.is(view.shiftX.property.plus(350))//
				.width.is(view.zoom.property.multiply(50).plus(140))//
				.height.is(view.zoom.property.multiply(50).plus(100))//
				.background.is(0xff3300ff)
				//.active.is(true)//
				.movableY.is(true)//
				/*.afterTap.is(new Task() {
					@Override
					public void doTask() {
						System.out.println("tap green");
					}
				})*/
				)//
				.child(new SplitLeftRight(this)//
				.width.is(view.width.property)
				.height.is(view.height.property)
				)//
				;
		setContentView(view);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("First");
		menu.add("Second");
		menu.add("Third");
		MenuItem mi = menu.add("item4");
		SubMenu sm = menu.addSubMenu("Sub...");
		sm.add("subitem 1");
		sm.add("subitem 2");
		SubMenu sm2 = sm.addSubMenu("another submenu...");
		sm2.add("another subitem");
		menu.add("item5");
		menu.add("item6");
		menu.add("item7");
		menu.add("item8");
		menu.add("item9");
		menu.add("item10");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		System.out.println("onOptionsItemSelected " + item.getTitle());
		// Handle item selection
		/* switch (item.getItemId()) {
		     case R.id.new_game:
		         newGame();
		         return true;
		     case R.id.help:
		         showHelp();
		         return true;
		     default:
		         return super.onOptionsItemSelected(item);
		 }*/
		return false;
	}
}
