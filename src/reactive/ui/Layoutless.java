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

public class Layoutless extends RelativeLayout {
	public static float density = 1;
	public static double tapSize = 8;
	//private final static int UNKNOWN_ID = -123456789;
	public final static int NONE = 0;
	public final static int DRAG = 1;
	public final static int ZOOM = 2;
	//private int dragMode = NONE;
	private int mode = NONE;
	private float lastEventX = 0;
	private float lastEventY = 0;
	private float initialShiftX = 0;
	private float initialShiftY = 0;
	private float initialSpacing;
	private float currentSpacing;
	//
	public NumericProperty<Layoutless> width = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> height = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> innerWidth = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> innerHeight = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> shiftX = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> shiftY = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> zoom = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> maxZoom = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> tapX = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> tapY = new NumericProperty<Layoutless>(this);
	public ItProperty<Layoutless, Task> afterTap = new ItProperty<Layoutless, Task>(this);
	public ItProperty<Layoutless, Task> afterShift = new ItProperty<Layoutless, Task>(this);
	public ItProperty<Layoutless, Task> afterZoom = new ItProperty<Layoutless, Task>(this);
	//public ItProperty<Layoutless, Task> afterPress = new ItProperty<Layoutless, Task>(this);
	//public NumericProperty<Layoutless> mode = new NumericProperty<Layoutless>(this);
	public static int foreColor = 0xff00ff00;
	public static int blurColor = 0xffff0000;
	public static int backColor = 0xff0000ff;
	private boolean initialized = false;

	void fillBaseColors() {
		Decor colorTest = new Decor(this.getContext());
		foreColor = colorTest.labelStyleLargeNormal().getCurrentTextColor();
		blurColor = colorTest.labelStyleLargeNormal().getCurrentHintTextColor();
		backColor = colorTest.labelStyleLargeInverse().getCurrentTextColor();
	}
	protected void init() {
		if (!initialized) {
			initialized = true;
			density = this.getContext().getResources().getDisplayMetrics().density;
			tapSize = 60.0 * density;
		}
	}
	public Layoutless(Context context) {
		super(context);
		init();
	}
	public Layoutless(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public Layoutless(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	public Layoutless child(View v) {
		this.addView(v);
		return this;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		onMeasureX();
	}
	
	protected void onMeasureX() {
		//System.out.println(this.getClass().getCanonicalName() + ".onMeasure: " + getMeasuredHeight());
		width.is(getMeasuredWidth());
		height.is(getMeasuredHeight());
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
			//System.out.println("startDrag");
			initialShiftX = shiftX.property.value().floatValue();
			initialShiftY = shiftY.property.value().floatValue();
			lastEventX = event.getX();
			lastEventY = event.getY();
			mode = DRAG;
		}
		else {
			if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
				if (event.getPointerCount() > 1) {
					if (mode == ZOOM) {
						//System.out.println("proceedZoom");
						currentSpacing = spacing(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
					}
					else {
						//System.out.println("startZoom");
						initialSpacing = spacing(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
						currentSpacing = initialSpacing;
						mode = ZOOM;
					}
				}
				else {
					//System.out.println("proceedDrag");
					setShift(event.getX(), event.getY());
					lastEventX = event.getX();
					lastEventY = event.getY();
				}
			}
			else {
				if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
					if (mode == DRAG) {
						//System.out.println("finishDrag");
						finishDrag(event.getX(), event.getY());
					}
					else {
						if (mode == ZOOM) {
							//System.out.println("finishZoom");
							finishZoom();
						}
						else {
							//System.out.println("not ZOOM");
						}
					}
				}
				else {
					//System.out.println("not ACTION_UP");
				}
			}
		}
		return true;
	}
	public static float spacing(float x0, float y0, float x1, float y1) {
		float x = x0 - x1;
		float y = y0 - y1;
		return FloatMath.sqrt(x * x + y * y);
	}
	void setShift(float x, float y) {
		double newShiftX = shiftX.property.value() + x - lastEventX;
		double newShiftY = shiftY.property.value() + y - lastEventY;
		/*if (innerWidth.property.value() > width.property.value()) {
			if (newShiftX < width.property.value() - innerWidth.property.value()) {
				newShiftX = width.property.value() - innerWidth.property.value();
			}
		}
		else {
			newShiftX = 0;
		}
		if (innerHeight.property.value() > height.property.value()) {
			if (newShiftY < height.property.value() - innerHeight.property.value()) {
				newShiftY = height.property.value() - innerHeight.property.value();
			}
		}
		else {
			newShiftY = 0;
		}
		if (newShiftX > 0) {
			newShiftX = 0;
		}
		if (newShiftY > 0) {
			newShiftY = 0;
		}*/
		shiftX.property.value(newShiftX);
		shiftY.property.value(newShiftY);
	}
	void finishDrag(float x, float y) {
		setShift(x, y);
		if (Math.abs(initialShiftX - shiftX.property.value()) < 1 + 0.1 * tapSize// 
				&& Math.abs(initialShiftY - shiftY.property.value()) < 1 + 0.1 * tapSize) {
			finishTap(x, y);
		}
		else {
			double newShiftX = shiftX.property.value();
			double newShiftY = shiftY.property.value();
			if (innerWidth.property.value() > width.property.value()) {
				if (newShiftX < width.property.value() - innerWidth.property.value()) {
					newShiftX = width.property.value() - innerWidth.property.value();
				}
			}
			else {
				newShiftX = 0;
			}
			if (innerHeight.property.value() > height.property.value()) {
				if (newShiftY < height.property.value() - innerHeight.property.value()) {
					newShiftY = height.property.value() - innerHeight.property.value();
				}
			}
			else {
				newShiftY = 0;
			}
			if (newShiftX > 0) {
				newShiftX = 0;
			}
			if (newShiftY > 0) {
				newShiftY = 0;
			}
			shiftX.property.value(newShiftX);
			shiftY.property.value(newShiftY);
			if (afterShift.property.value() != null) {
				afterShift.property.value().start();
			}
		}
		mode = NONE;
	}
	void finishTap(float x, float y) {
		shiftX.property.value((double) initialShiftX);
		shiftY.property.value((double) initialShiftY);
		tapX.property.value((double) x);
		tapY.property.value((double) y);
		if (afterTap.property.value() != null) {
			afterTap.property.value().start();
		}
	}
	void finishZoom() {
		if (currentSpacing > initialSpacing) {
			if (zoom.property.value() < maxZoom.property.value()) {
				zoom.is(zoom.property.value() + 1);
			}
		}
		else {
			if (zoom.property.value() > 0) {
				zoom.is(zoom.property.value() - 1);
			}
		}
		shiftX.property.value((double) initialShiftX);
		shiftY.property.value((double) initialShiftY);
		if (afterZoom.property.value() != null) {
			afterZoom.property.value().start();
		}
		mode = NONE;
	}
}
