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

public class SplitTopDown extends SubLayoutless {
	public NumericProperty<SplitTopDown> split;
	public NumericProperty<SplitTopDown> position;
	private Rake topSide;
	private Rake downSide;
	private boolean initialized = false;
	private boolean firstOnSizeChanged = true;

	@Override
	public void requestLayout() {
		super.requestLayout();
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (firstOnSizeChanged) {
			firstOnSizeChanged = false;
		}
	}
	public SplitTopDown(Context context) {
		super(context);
	}
	public SplitTopDown(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public SplitTopDown(Context context, AttributeSet attrs, int defStyle) {
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
	public SplitTopDown topSide(Rake v) {
		if (topSide != null) {
			this.removeView(topSide.view());
		}
		topSide = v;
		this.addView(topSide//
				.width().is(width().property)//
				.height().is(split.property)//
				.view()//
				, 0//
		);
		return this;
	}
	public SplitTopDown downSide(Rake v) {
		if (downSide != null) {
			this.removeView(downSide.view());
		}
		downSide = v;
		this.addView(downSide//
				.top().is(split.property)//
				.width().is(width().property)//
				.height().is(height().property.minus(split.property))//
				.view()//
				, 0//
		);
		return this;
	}
	protected void init() {
		super.init();
		if (!initialized) {
			initialized = true;
			this.solid.is(false);
			split = new NumericProperty<SplitTopDown>(this);
			split.is(50);
			position = new NumericProperty<SplitTopDown>(this);
			position.is(0);
			Task adjustSplit = new Task() {
				@Override
				public void doTask() {
					if (split.property.value() < 0) {
						split.is(0);
					}
					if (split.property.value() > height().property.value()) {
						split.is(height().property.value());
					}
				}
			};
			this.child(new Decor(this.getContext()).background.is(0x33999999)//
					.top().is(split.property.minus(2))//
					.width().is(width().property)//
					.height().is(5)//
			);
			this.child(new Decor(this.getContext()).background.is(0x99999999)//
					.top().is(split.property.minus(1))//
					.width().is(width().property)//
					.height().is(3)//
			);
			this.child(new Decor(this.getContext()).background.is(Layoutless.themeForegroundColor)//
					.top().is(split.property)//
					.width().is(width().property)//
					.height().is(1)//
			);
			this.child(new Decor(this.getContext())//
			.dragY.is(split.property.minus(0.5 * Layoutless.tapSize))//
			.afterDrag.is(adjustSplit)//
			.movableY.is(true)//
					.sketch(new SketchPlate()//
					.arcX.is(0.5 * (4 + Layoutless.tapSize))//
					.arcY.is(0.5 * (4 + Layoutless.tapSize))//
					.background.is(0x66999999)//
					.width.is(4 + Layoutless.tapSize)//
					.height.is(4 + Layoutless.tapSize)//
					)//
					.sketch(new SketchPlate()//
					.arcX.is(0.5 * (2 + Layoutless.tapSize))//
					.arcY.is(0.5 * (2 + Layoutless.tapSize))//
					.background.is(0x99999999)//
					.left.is(1).top.is(1)//
					.width.is(2 + Layoutless.tapSize)//
					.height.is(2 + Layoutless.tapSize)//
					)//
					.sketch(new SketchPlate()//
					.arcX.is(0.5 * Layoutless.tapSize)//
					.arcY.is(0.5 * Layoutless.tapSize)//
					.background.is(Layoutless.themeForegroundColor)//
					.left.is(2).top.is(2)//
					.width.is(Layoutless.tapSize)//
					.height.is(Layoutless.tapSize)//
					)//
					.sketch(new SketchPlate()//
					.arcX.is(0.5 * (Layoutless.tapSize - 2))//
					.arcY.is(0.5 * (Layoutless.tapSize - 2))//
					.background.is(Layoutless.themeBackgroundColor)//
					.left.is(3).top.is(3)//
					.width.is(Layoutless.tapSize - 2)//
					.height.is(Layoutless.tapSize - 2)//
					)//
					.sketch(new SketchLine()//
							.point( 2 + 0.3 * Layoutless.tapSize,2 + 0.65 * Layoutless.tapSize)//
							.point( 2 + 0.5 * Layoutless.tapSize,2 + 0.8 * Layoutless.tapSize)//
							.point( 2 + 0.7 * Layoutless.tapSize,2 + 0.65 * Layoutless.tapSize)//
					.strokeColor.is(Layoutless.themeBlurColor)//
					.strokeWidth.is(1 + 0.08 * Layoutless.tapSize)//
					)//
					
					.sketch(new SketchLine()//
							.point( 2 + 0.3 * Layoutless.tapSize,2 + 0.35 * Layoutless.tapSize)//
							.point( 2 + 0.5 * Layoutless.tapSize,2 + 0.2 * Layoutless.tapSize)//
							.point( 2 + 0.7 * Layoutless.tapSize,2 + 0.35 * Layoutless.tapSize)//
					.strokeColor.is(Layoutless.themeBlurColor)//
					.strokeWidth.is(1 + 0.08 * Layoutless.tapSize)//
					)//
					
					.width().is(Layoutless.tapSize + 4)//
					.height().is(Layoutless.tapSize + 4)//
					.left().is(position.property.plus(1).multiply(Layoutless.tapSize).minus(0.5 * Layoutless.tapSize))//
			);
		}
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
}
