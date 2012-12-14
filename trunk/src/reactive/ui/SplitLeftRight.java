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

public class SplitLeftRight extends SubLayoutless {
	//private boolean initialized = false;
	public NumericProperty<SplitLeftRight> split;

	public SplitLeftRight(Context context) {
		super(context);
		//init();
	}
	public SplitLeftRight(Context context, AttributeSet attrs) {
		super(context, attrs);
		//init();
	}
	public SplitLeftRight(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//init();
	}
	@Override
	protected void onMeasureX() {
		//
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
/*
	Task reFit = new Task() {
		@Override
		public void doTask() {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
					width.property.value().intValue()//
					, height.property.value().intValue());
			params.leftMargin = left.property.value().intValue();
			params.topMargin = top.property.value().intValue();
			SplitLeftRight.this.setLayoutParams(params);
		}
	};*/

	protected void init() {
		super.init();
		//if (!initialized) {
			//initialized = true;
		//System.out.println();
		split = new NumericProperty<SplitLeftRight>(this);
		//left = new NumericProperty<SplitLeftRight>(this);
		//top = new NumericProperty<SplitLeftRight>(this);
			this.child(new Knob(this.getContext())//
			
			.width.is(split.property.plus(0.5 * Layoutless.tapSize))//
			.height.is(height.property)//
			.labelText.is("Left")//
			);
			this.child(new Knob(this.getContext())//
			.left.is(split.property.plus(0.5 * Layoutless.tapSize))//
			.width.is(width.property.minus(split.property).minus(0.5 * Layoutless.tapSize)).height.is(height.property)//
			.labelText.is("Rigth")//
			);
			split.is(444);
			this.child(new Decor(this.getContext())//
			.left.is(split.property.plus(0.5 * Layoutless.tapSize))//
			.width.is(1)//
			.height.is(height.property)//
			.background.is(0xffffffff)//
			);
			this.child(new Decor(this.getContext())//
			.width.is(60 * Layoutless.density)//
			.height.is(60 * Layoutless.density)//
			.shiftX.is(split.property)//
			.top.is(height.property.divide(2).minus(0.5 * Layoutless.tapSize))//
			.movableX.is(true)//
					.sketch(new SketchPlate()//
					.width.is(Layoutless.tapSize)//
					.height.is(Layoutless.tapSize)//
					.arcX.is(0.5 * Layoutless.tapSize)//
					.arcY.is(0.5 * Layoutless.tapSize)//
					.background.is(0xff000000)//
					)//
					.sketch(new SketchContour()//
					.width.is(Layoutless.tapSize - 2)//
					.height.is(Layoutless.tapSize - 2)//
					.left.is(1)//
					.top.is(1)//
					.arcX.is(0.5 * (Layoutless.tapSize - 2))//
					.arcY.is(0.5 * (Layoutless.tapSize - 2))//
					.strokeColor.is(0xffffffff)//
					.strokeWidth.is(1)//
					)//
					.sketch(new SketchLine()//
					.startX.is(0.6 * Layoutless.tapSize)//
					.startY.is(0.25 * Layoutless.tapSize)//
					.endX.is(0.8 * Layoutless.tapSize)//
					.endY.is(0.5 * Layoutless.tapSize)//
					.strokeColor.is(0xffffffff)//
					.strokeWidth.is(1 + 0.07 * Layoutless.tapSize)//
					)//
					.sketch(new SketchLine()//
					.startX.is(0.6 * Layoutless.tapSize)//
					.startY.is(0.75 * Layoutless.tapSize)//
					.endX.is(0.8 * Layoutless.tapSize)//
					.endY.is(0.5 * Layoutless.tapSize)//
					.strokeColor.is(0xffffffff)//
					.strokeWidth.is(1 + 0.07 * Layoutless.tapSize)//
					)//
					.sketch(new SketchLine()//
					.startX.is(0.4 * Layoutless.tapSize)//
					.startY.is(0.25 * Layoutless.tapSize)//
					.endX.is(0.2 * Layoutless.tapSize)//
					.endY.is(0.5 * Layoutless.tapSize)//
					.strokeColor.is(0xffffffff)//
					.strokeWidth.is(1 + 0.07 * Layoutless.tapSize)//
					)//
					.sketch(new SketchLine()//
					.startX.is(0.4 * Layoutless.tapSize)//
					.startY.is(0.75 * Layoutless.tapSize)//
					.endX.is(0.2 * Layoutless.tapSize)//
					.endY.is(0.5 * Layoutless.tapSize)//
					.strokeColor.is(0xffffffff)//
					.strokeWidth.is(1 + 0.07 * Layoutless.tapSize)//
					)//
			)//
			;
		//}
	}
}
