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
	public static float density =1;
	private static double tapDiff = 8;
	private final static int UNKNOWN_ID = -123456789;
	private final static int NONE = 0;
	private final static int DRAG = 1;
	private final static int ZOOM = 2;
	private int dragMode = NONE;
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
	//public ItProperty<Layoutless, Task> afterTap = new ItProperty<Layoutless, Task>(this);

	public Layoutless(Context context) {
		super(context);
		 density = context.getResources().getDisplayMetrics().density;
	}
	public Layoutless(Context context, AttributeSet attrs) {
		super(context, attrs);
		 density = context.getResources().getDisplayMetrics().density;
	}
	public Layoutless(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		 density = context.getResources().getDisplayMetrics().density;
	}
	public Layoutless child(View v) {
		this.addView(v);
		return this;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
			dragMode = DRAG;
		}
		else {
			if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
				if (event.getPointerCount() > 1) {
					if (dragMode == ZOOM) {
						//System.out.println("proceedZoom");
						currentSpacing = spacing(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
					}
					else {
						//System.out.println("startZoom");
						initialSpacing = spacing(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
						currentSpacing = initialSpacing;
						dragMode = ZOOM;
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
					if (dragMode == DRAG) {
						//System.out.println("finishDrag");
						finishDrag(event.getX(), event.getY());
					}
					else {
						if (dragMode == ZOOM) {
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
	float spacing(float x0, float y0, float x1, float y1) {
		float x = x0 - x1;
		float y = y0 - y1;
		return FloatMath.sqrt(x * x + y * y);
	}

	void setShift(float x, float y) {
		double newShiftX = shiftX.property.value() + x - lastEventX;
		double newShiftY = shiftY.property.value() + y - lastEventY;
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
	}
	void finishDrag(float x, float y) {
		setShift(x, y);
		if (Math.abs(initialShiftX - shiftX.property.value()) < tapDiff// 
				&& Math.abs(initialShiftY - shiftY.property.value()) < tapDiff) {
			finishTap(x, y);
		}
		else {
			//
		}
		dragMode = NONE;
	}
	void finishTap(float x, float y) {
		shiftX.property.value((double) initialShiftX);
		shiftY.property.value((double) initialShiftY);
		tapX.property.value((double) x);
		tapY.property.value((double) y);
		/*if (afterTap.property.value() != null) {
			afterTap.property.value().start();
		}*/
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
		dragMode = NONE;
	}
}
