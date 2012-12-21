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

public class SubLayoutless extends Layoutless  {
	
	private boolean initialized = false;
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
	@Override
	protected void init() {
		super.init();
		if (!initialized) {
			initialized = true;
		//left = new NumericProperty<SubLayoutless>(this);
		//top = new NumericProperty<SubLayoutless>(this);
		Task reFit = new Task() {
			@Override
			public void doTask() {
				/*System.out.println("reFit " + width.property.value()//
						+ "/" + height.property.value()//
						+ "/" + left.property.value()//
						+ "/" + top.property.value());*/
				int w = width().property.value().intValue();
				/*if (w < 1) {
					w = 1;
				}*/
				int h = height().property.value().intValue();
				/*if (h < 1) {
					h = 1;
				}*/
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
				params.leftMargin = left().property.value().intValue();
				params.topMargin = top().property.value().intValue();
				setLayoutParams(params);
			}
		};
		left().property.afterChange(reFit);
		top().property.afterChange(reFit);
		width().property.afterChange(reFit);
		height().property.afterChange(reFit);
		//System.out.println(this.getClass().getCanonicalName()+".init "+left.property.value());
		//left.is(50);
		//System.out.println(this.getClass().getCanonicalName()+".now "+left.property.value());
		}
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		/*width().property.unbind();
		height().property.unbind();
		left().property.unbind();
		top().property.unbind();*/
	}
}
