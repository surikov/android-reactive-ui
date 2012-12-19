package reactive.ui;

import tee.binding.properties.*;
import android.graphics.*;

import android.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.*;
import reactive.ui.*;
import reactive.ui.library.views.SimpleButton;

import android.view.animation.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;

import java.io.*;
import java.text.*;

public class Sheet extends SubLayoutless implements ViewRake {
	/*private NumericProperty<ViewRake> width = new NumericProperty<ViewRake>(this);
	private NumericProperty<ViewRake> height = new NumericProperty<ViewRake>(this);
	private NumericProperty<ViewRake> left = new NumericProperty<ViewRake>(this);
	private NumericProperty<ViewRake> top = new NumericProperty<ViewRake>(this);*/
	//boolean initialized = false;
	/*Task reFit = new Task() {
		@Override
		public void doTask() {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
					width.property.value().intValue()//
					, height.property.value().intValue());
			params.leftMargin = left.property.value().intValue();
			params.topMargin = top.property.value().intValue();
			Sheet.this.setLayoutParams(params);
		}
	};
	Context context;*/

	public Sheet(Context context) {
		super(context);
		//init(context);
	}
	public Sheet(Context context, AttributeSet attrs) {
		super(context, attrs);
		//init(context);
	}
	public Sheet(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//init(context);
	}
	/*@Override
	public NumericProperty<ViewRake> left() {
		return left;
	}
	@Override
	public NumericProperty<ViewRake> top() {
		return top;
	}
	@Override
	public NumericProperty<ViewRake> width() {
		return width;
	}
	@Override
	public NumericProperty<ViewRake> height() {
		return height;
	}
	@Override
	public View view() {
		return this;
	}*/
	@Override
	protected void init() {
		/*if (initialized) {
			return;
		}
		initialized = true;
		width.property.afterChange(reFit).value(100);
		height.property.afterChange(reFit).value(100);
		left.property.afterChange(reFit);
		top.property.afterChange(reFit);*/
		this.child(new Knob(this.getContext()));
	}
}
