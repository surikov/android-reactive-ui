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
	public NumericProperty<SplitLeftRight> split;
	public NumericProperty<SplitLeftRight> position;
	private Rake leftSide;
	private Rake rightSide;
/*
	public void debug() {
		
		System.out.println("me " + this.getLeft() + "x" + this.getTop() + "/" + this.getWidth() + "x" + this.getHeight());
		System.out.println("bind " + left().property.value() + "x" + top().property.value()+ "/" + width().property.value() + "x" + height().property.value());
	}*/
	
	@Override
	public void requestLayout() {
		super.requestLayout();
		System.out.println("requestLayout");
		//reFit.start();
	}
	
	@Override
	 protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		System.out.println("onSizeChanged " + w + "x" + h + " <- " + oldw + "x" + oldh);
	    }
	
	
	
	
	
	public SplitLeftRight(Context context) {
		super(context);
	}
	public SplitLeftRight(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public SplitLeftRight(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}
	@Override
	protected void onMeasureX() {
		//
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//System.out.println(this.getClass().getCanonicalName() + ".onMeasure: "+ getMeasuredWidth()+"x" + getMeasuredHeight());
		//System.out.println( "spec "+ widthMeasureSpec+"x" + heightMeasureSpec);
	}
	public SplitLeftRight leftSide(Rake v) {
		if (leftSide != null) {
			this.removeView(leftSide.view());
		}
		leftSide = v;
		this.addView(leftSide//
				.width().is(split.property)//
				.height().is(height().property)//
				.view()//
				, 0//
		);
		return this;
	}
	public SplitLeftRight rightSide(Rake v) {
		if (rightSide != null) {
			this.removeView(rightSide.view());
		}
		rightSide = v;
		this.addView(rightSide//
				.left().is(split.property)//
				.width().is(width().property.minus(split.property))//
				.height().is(height().property)//
				.view()//
				, 0//
		);
		return this;
	}
	protected void init() {
		super.init();
		split = new NumericProperty<SplitLeftRight>(this);
		position = new NumericProperty<SplitLeftRight>(this);
		position.is(0);
		Task adjustSplit = new Task() {
			@Override
			public void doTask() {
				if (split.property.value() < 0.5 * Layoutless.tapSize) {
					split.is(0.5 * Layoutless.tapSize);
				}
				if (split.property.value() > width().property.value() - 0.5 * Layoutless.tapSize - 4) {
					split.is(width().property.value() - 0.5 * Layoutless.tapSize - 4);
				}
			}
		};
		//split.property.afterChange(adjustSplit);
		//split.property.m
		this.child(new Decor(this.getContext())//
		.background.is(0x33999999)//
				.left().is(split.property.minus(2))//
				.width().is(5)//
				.height().is(height().property)//
		//
		);
		this.child(new Decor(this.getContext())//
		.background.is(0x99999999)//
				.left().is(split.property.minus(1))//
				.width().is(3)//
				.height().is(height().property)//
		//
		);
		this.child(new Decor(this.getContext())//
		.background.is(Layoutless.themeForegroundColor)//
				.left().is(split.property)//
				.width().is(1)//
				.height().is(height().property)//
		//
		);
		this.child(new Decor(this.getContext())//
		.dragX.is(split.property.minus(0.5 * Layoutless.tapSize))//
		.afterDrag.is(adjustSplit)//
		.movableX.is(true)//
				.sketch(new SketchPlate()//
				.width.is(4 + Layoutless.tapSize)//
				.height.is(4 + Layoutless.tapSize)//
				.arcX.is(0.5 * (4 + Layoutless.tapSize))//
				.arcY.is(0.5 * (4 + Layoutless.tapSize))//
				.background.is(0x66999999)//
				)//
				.sketch(new SketchPlate()//
				.width.is(2 + Layoutless.tapSize)//
				.height.is(2 + Layoutless.tapSize)//
				.arcX.is(0.5 * (2 + Layoutless.tapSize))//
				.arcY.is(0.5 * (2 + Layoutless.tapSize))//
				.background.is(0x99999999)//
				.left.is(1).top.is(1))//
				.sketch(new SketchPlate()//
				.width.is(Layoutless.tapSize)//
				.height.is(Layoutless.tapSize)//
				.arcX.is(0.5 * Layoutless.tapSize)//
				.arcY.is(0.5 * Layoutless.tapSize)//
				.background.is(Layoutless.themeForegroundColor)//
				.left.is(2).top.is(2))//
				.sketch(new SketchPlate()//
				.width.is(Layoutless.tapSize - 2)//
				.height.is(Layoutless.tapSize - 2)//
				.arcX.is(0.5 * (Layoutless.tapSize - 2))//
				.arcY.is(0.5 * (Layoutless.tapSize - 2))//
				.background.is(Layoutless.themeBackgroundColor)//
				.left.is(3).top.is(3))//
				/*.sketch(new SketchContour()//
				.width.is(Layoutless.tapSize - 2)//
				.height.is(Layoutless.tapSize - 2)//
				.left.is(2)//
				.top.is(2)//
				.arcX.is(0.5 * (Layoutless.tapSize - 2))//
				.arcY.is(0.5 * (Layoutless.tapSize - 2))//
				.strokeColor.is(Layoutless.themeBlurColor)//
				.strokeWidth.is(1)//
				)//
				*/
				.sketch(new SketchLine()//
						.point(2 + 0.65 * Layoutless.tapSize, 2 + 0.3 * Layoutless.tapSize)//
						.point(2 + 0.8 * Layoutless.tapSize, 2 + 0.5 * Layoutless.tapSize)//
						.point(2 + 0.65 * Layoutless.tapSize, 2 + 0.7 * Layoutless.tapSize)//
				.strokeColor.is(Layoutless.themeBlurColor)//
				.strokeWidth.is(1 + 0.08 * Layoutless.tapSize)//
				).sketch(new SketchLine()//
						.point(2 + 0.35 * Layoutless.tapSize, 2 + 0.3 * Layoutless.tapSize)//
						.point(2 + 0.2 * Layoutless.tapSize, 2 + 0.5 * Layoutless.tapSize)//
						.point(2 + 0.35 * Layoutless.tapSize, 2 + 0.7 * Layoutless.tapSize)//
						.strokeColor.is(Layoutless.themeBlurColor)//
						.strokeWidth.is(1 + 0.08 * Layoutless.tapSize)//
				)//
				.width().is(Layoutless.tapSize + 4)//
				.height().is(Layoutless.tapSize + 4)//
				.top().is(height().property.minus(position.property.plus(1).multiply(Layoutless.tapSize)).minus(0.5 * Layoutless.tapSize))//
		);
	}
}
