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
		view = new Layoutless(this);
		view//
		.child(new Fit(this)//
		.labelText.is("Very long string for testing purpose only.")//
		.left.is(0)//
		.top.is(0)//
		.width.is(240)//
		.height.is(100)//
		//.gravity.is(Gravity.CENTER)//
		.background.is(Color.RED)//
		//.foreground.is(Color.YELLOW)//
		//.textAppearance.is(android.R.style.TextAppearance_Large)//
		//.labelFace.is(Typeface.createFromAsset(getAssets(), "fonts/deftone.ttf"))//
		)//
		.child(new Fit(this)//
		.labelText.is("Very long string for testing purpose only. То-сё на русском.")//
		.left.is(0)//
		.top.is(100)//
		.width.is(700)//
		.height.is(500)//
		.gravity.is(Gravity.CENTER)//
		.background.is(0xff003300)//
		.labelSize.is(50)
		//.foreground.is(Color.YELLOW)//
		//.textAppearance.is(android.R.style.TextAppearance_Large)//
		.labelFace.is(Typeface.createFromAsset(getAssets(), "fonts/Rurintania.ttf"))//
		)//
		;
		setContentView(view);
	}
}
