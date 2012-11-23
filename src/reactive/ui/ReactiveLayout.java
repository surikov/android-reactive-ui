package reactive.ui;

import android.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import reactive.ui.*;
import android.view.animation.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;
import java.io.*;
import java.text.*;

public class ReactiveLayout extends RelativeLayout {
	public ReactiveLayout(Context context) {
		super(context);
	}
	public ReactiveLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public ReactiveLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public ReactiveLayout child(View v) {
		this.addView(v);
		return this;
	}
}
