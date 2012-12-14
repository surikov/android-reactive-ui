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

public class SubLayoutless extends Layoutless {
	public NumericProperty<SubLayoutless> left;
	public NumericProperty<SubLayoutless> top;
	Task reFit = new Task() {
		@Override
		public void doTask() {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
					width.property.value().intValue()//
					, height.property.value().intValue());
			params.leftMargin = left.property.value().intValue();
			params.topMargin = top.property.value().intValue();
			setLayoutParams(params);
		}
	};
	public SubLayoutless(Context context) {
		super(context);
	}
	public SubLayoutless(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public SubLayoutless(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	protected void onMeasureX() {
		//
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	protected void init() {
		super.init();
		left = new NumericProperty<SubLayoutless>(this);
		top = new NumericProperty<SubLayoutless>(this);
	}
}
