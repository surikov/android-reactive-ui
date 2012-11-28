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
		final Numeric screenWidth = new Numeric();
		final Numeric screenHeight = new Numeric();
		final Numeric shiftX = new Numeric();
		final Numeric shiftY = new Numeric();
		final Numeric zoom = new Numeric().value(1);
		view = new Layoutless(this);
		final Fit t=new Fit(this);
		view//
		.width.is(screenWidth).height.is(screenHeight)//
		.innerWidth.is(2000).innerHeight.is(1000)//
		.shiftX.is(shiftX).shiftY.is(shiftY)//
		.maxZoom.is(3).zoom.is(zoom)
		//
				.child(new Fit(this)//
				.labelText.is("Very long string for testing purpose only.")//
				.left.is(shiftX)//
				.top.is(shiftY)//
				.width.is(240)//
				.height.is(100)//
				//.gravity.is(Gravity.CENTER)//
				.background.is(Color.RED)//
				//.foreground.is(Color.YELLOW)//
				//.textAppearance.is(android.R.style.TextAppearance_Large)//
				//.labelFace.is(Typeface.createFromAsset(getAssets(), "fonts/deftone.ttf"))//
				)//
				.child(t//
				.labelText.is("Very long string for testing purpose only. То-сё на русском.")//
				.left.is(0)//
				.top.is(100)//
				.width.is(700)//
				.height.is(500)//
				//.gravity.is(Gravity.CENTER)//
				.background.is(0xff003300)//
				.labelSize.is(zoom.multiply(20).plus(20))
				//.foreground.is(Color.YELLOW)//
				//.textAppearance.is(android.R.style.TextAppearance_Large)//
				//.labelFace.is(Typeface.createFromAsset(getAssets(), "fonts/Rurintania.ttf"))//
				)//
				.child(new Knob(this)//
				.labelText.is("botright")//
				.left.is(300)//
				.top.is(50)//
				.height.is(50)//
				.width.is(100)//
				.tap.is(new Task() {
					@Override
					public void doTask() {
						System.out.println("test");
						t.setGravity(Gravity.RIGHT|Gravity.BOTTOM);
						t.width.is(800);
						
					}
				}))//
				.child(new Knob(this)//
				.labelText.is("lefttop")//
				.left.is(500)//
				.top.is(50)//
				.height.is(50)//
				.width.is(100)//
				.tap.is(new Task() {
					@Override
					public void doTask() {
						System.out.println("lt");
						//t.setGravity(Gravity.LEFT|Gravity.TOP);
						t.labelText.is("qwdqfqefqewfewqf Very long string for testing purpose only. То-сё на русском.");
						//t.width.is(700);
					}
				}))//
				;
		setContentView(view);
		
	}
}
