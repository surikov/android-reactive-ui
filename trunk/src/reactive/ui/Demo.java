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
	ReactiveLayout view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new ReactiveLayout(this);
		view.child(new ReactiveLabel(this)//
		.text.is("Very long string for testing purpose only.")//
		.left.is(200)//
		.top.is(50)//
		.width.is(120)//
		.height.is(300)//
		//.gravity.is(Gravity.CENTER)//
		.background.is(Color.RED)//
		//.foreground.is(Color.YELLOW)//
		.textAppearance.is(android.R.style.TextAppearance_DialogWindowTitle)
		)//
		;
		setContentView(view);
	}
}
