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
		//this.or
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
		split.is(0.5 * Layoutless.tapSize);
		position = new NumericProperty<SplitLeftRight>(this);
		position.is(0);
		//leftSide = new SubLayoutless(this.getContext());
		//rightSide = new SubLayoutless(this.getContext());
		/*
		this.child(leftSide//
		.width().is(split.property)//
		.height().is(height().property)//
		.view()//
		);
		this.child(rightSide//
		.left().is(split.property)//
		.width().is(width().property.minus(split.property))//
		.height().is(height().property)//
		.view()//
		);
		*/
		/*
		this.child(new Decor(this.getContext())//
		//.left.is(left.property)//
		.width.is(width.property)//
		.height.is(height.property)//
				.sketch(new SketchPlate()//
				.width.is(width.property)//
				.height.is(height.property)//
				.arcX.is(50).arcY.is(50)//
				.background.is(0x9900ffff)//
				));*/
		this.child(new Decor(this.getContext())//
		.background.is(Layoutless.themeBlurColor)//
		.left().is(split.property)//
				.width().is(1)//
				.height().is(height().property)//
				.view()//
		);
		/*Paint p=new Paint();
		p.setColor(Layoutless.backColor);
		p.setAlpha(170);*/
		this.child(new Decor(this.getContext())//
		.dragX.is(split.property.minus(0.5 * Layoutless.tapSize))//
		.afterDrag.is(new Task() {
			@Override
			public void doTask() {
				if (split.property.value() < 0.5 * Layoutless.tapSize) {
					split.is(0.5 * Layoutless.tapSize);
				}
				if (split.property.value() > width().property.value() - 0.5 * Layoutless.tapSize) {
					split.is(width().property.value() - 0.5 * Layoutless.tapSize);
				}
			}
		})//
		.movableX.is(true)//
				.sketch(new SketchPlate()//
				.width.is(Layoutless.tapSize)//
				.height.is(Layoutless.tapSize)//
				.arcX.is(0.5 * Layoutless.tapSize)//
				.arcY.is(0.5 * Layoutless.tapSize)//
				.background.is(Layoutless.themeBackgroundColor)//
				)//
				.sketch(new SketchContour()//
				.width.is(Layoutless.tapSize - 2)//
				.height.is(Layoutless.tapSize - 2)//
				.left.is(1)//
				.top.is(1)//
				.arcX.is(0.5 * (Layoutless.tapSize - 2))//
				.arcY.is(0.5 * (Layoutless.tapSize - 2))//
				.strokeColor.is(Layoutless.themeBlurColor)//
				.strokeWidth.is(1)//
				)//
				.sketch(new SketchLine()//
				.startX.is(0.65 * Layoutless.tapSize)//
				.startY.is(0.3 * Layoutless.tapSize)//
				.endX.is(0.8 * Layoutless.tapSize)//
				.endY.is(0.5 * Layoutless.tapSize)//
				.strokeColor.is(Layoutless.themeBlurColor)//
				.strokeWidth.is(1 + 0.08 * Layoutless.tapSize)//
				)//
				.sketch(new SketchLine()//
				.startX.is(0.65 * Layoutless.tapSize)//
				.startY.is(0.7 * Layoutless.tapSize)//
				.endX.is(0.8 * Layoutless.tapSize)//
				.endY.is(0.5 * Layoutless.tapSize)//
				.strokeColor.is(Layoutless.themeBlurColor)//
				.strokeWidth.is(1 + 0.08 * Layoutless.tapSize)//
				)//
				.sketch(new SketchLine()//
				.startX.is(0.35 * Layoutless.tapSize)//
				.startY.is(0.3 * Layoutless.tapSize)//
				.endX.is(0.2 * Layoutless.tapSize)//
				.endY.is(0.5 * Layoutless.tapSize)//
				.strokeColor.is(Layoutless.themeBlurColor)//
				.strokeWidth.is(1 + 0.08 * Layoutless.tapSize)//
				)//
				.sketch(new SketchLine()//
				.startX.is(0.35 * Layoutless.tapSize)//
				.startY.is(0.7 * Layoutless.tapSize)//
				.endX.is(0.2 * Layoutless.tapSize)//
				.endY.is(0.5 * Layoutless.tapSize)//
				.strokeColor.is(Layoutless.themeBlurColor)//
				.strokeWidth.is(1 + 0.08 * Layoutless.tapSize)//
				)//
				.width().is(60 * Layoutless.density)//
				.height().is(60 * Layoutless.density)//
				.top().is(height().property.minus(position.property.plus(1).multiply(Layoutless.tapSize))
				.minus(0.5 * Layoutless.tapSize))//
				.view());
		/*rightSide.left.property.afterChange(new Task(){

			@Override
			public void doTask() {
				System.out.println(this.getClass().getCanonicalName()+".test "+rightSide.left.property.value());
				
			}});
		System.out.println(this.getClass().getCanonicalName()+".test "+rightSide.left.property.value());
		rightSide.left.is(500);
		System.out.println(this.getClass().getCanonicalName()+".test "+rightSide.left.property.value());*/
	}
}
