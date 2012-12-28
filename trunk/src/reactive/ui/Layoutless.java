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
import android.content.res.*;
import android.view.animation.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;

import java.io.*;
import java.text.*;

public class Layoutless extends RelativeLayout implements Rake {
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
	private NumericProperty<Rake> left = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> top = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> width = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> height = new NumericProperty<Rake>(this);
	public NumericProperty<Layoutless> innerWidth = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> innerHeight = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> shiftX = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> shiftY = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> zoom = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> maxZoom = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> tapX = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> tapY = new NumericProperty<Layoutless>(this);
	public ToggleProperty<Layoutless> solid = new ToggleProperty<Layoutless>(this);
	public ItProperty<Layoutless, Task> afterTap = new ItProperty<Layoutless, Task>(this);
	public ItProperty<Layoutless, Task> afterShift = new ItProperty<Layoutless, Task>(this);
	public ItProperty<Layoutless, Task> afterZoom = new ItProperty<Layoutless, Task>(this);
	//public ItProperty<Layoutless, Task> afterPress = new ItProperty<Layoutless, Task>(this);
	//public NumericProperty<Layoutless> mode = new NumericProperty<Layoutless>(this);
	public static int themeForegroundColor = 0xff00ff00;
	public static int themeBlurColor = 0xffff0000;
	public static int themeBackgroundColor = 0xff0000ff;
	private static Decor colorTest;
	private boolean initialized = false;
	private Vector<Rake> children = new Vector<Rake>();
	private Vector<Decor> fogs = new Vector<Decor>();
	private Vector<SubLayoutless> dialogs = new Vector<SubLayoutless>();

	public void fillBaseColors() {
		if (colorTest == null) {
			colorTest = new Decor(getContext());
			themeForegroundColor = colorTest.labelStyleLargeNormal().getCurrentTextColor();
			themeBlurColor = colorTest.labelStyleLargeNormal().getCurrentHintTextColor();
			themeBackgroundColor = colorTest.labelStyleLargeInverse().getCurrentTextColor();
			//ColorStateList colorStateList=colorTest.getTextColors();
			//colorStateList.
			//TypedValue tv = new TypedValue();
			//getContext().getTheme().resolveAttribute(android.R.attr.textColorSecondary, tv, true);
			//getContext().getTheme().resolveAttribute(android.R.attr.textColorHighlight, tv, true);
			//getContext().getTheme().resolveAttribute(android.R.attr.textColorPrimary, tv, true);
			//getContext().getTheme().resolveAttribute(android.R.attr.textColorHint, tv, true);
			//getContext().getTheme().resolveAttribute(android.R.attr.textColorPrimaryDisableOnly, tv, true);
			//getContext().getTheme().resolveAttribute(android.R.attr.background, tv, true);
			//themeBlurColor = getResources().getColor(tv.resourceId);
			//System.out.println();
		}
	}
	protected void init() {
		if (!initialized) {
			initialized = true;
			density = this.getContext().getResources().getDisplayMetrics().density;
			tapSize = 60.0 * density;
			solid.is(true);
			fillBaseColors();
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
	public Layoutless child(Rake v) {
		this.addView(v.view());
		children.add(v);
		return this;
	}
	public Rake child(int nn) {
		if (nn < children.size()) {
			return children.get(nn);
		}
		else {
			return null;
		}
	}
	public int count() {
		return children.size();
	}
	public void addDialog(SubLayoutless sub) {
		Decor fog = new Decor(this.getContext()) {
			@Override
			public boolean onTouchEvent(MotionEvent event) {
				Layoutless.this.removeDialog();
				return true;
			}
		};
		fog.background.is(0x99666666).width().is(width.property).height().is(height.property);
		fogs.add(fog);
		int xx = (int) (0.5 * (width.property.value() - sub.width().property.value()));
		int yy = (int) (0.5 * (height.property.value() - sub.height().property.value()));
		if (xx < 0) {
			xx = 0;
		}
		sub.left().is(xx);
		if (yy < 0) {
			yy = 0;
		}
		sub.top().is(yy);
		if (width.property.value() < sub.width().property.value()) {
			sub.width().property.value(width.property.value());
		}
		if (height.property.value() < sub.height().property.value()) {
			sub.height().property.value(height.property.value());
		}
			
		dialogs.add(sub);
		this.child(fog);
		this.child(sub);
	}
	public boolean removeDialog() {
		if (fogs.size() > 0) {
			this.removeView(dialogs.get(dialogs.size() - 1));
			dialogs.remove(dialogs.get(dialogs.size() - 1));
			this.removeView(fogs.get(fogs.size() - 1));
			fogs.remove(fogs.get(fogs.size() - 1));
			return true;
		}
		return false;
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
		if (!solid.property.value()) {
			return false;
		}
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
		//System.out.println(this.getClass().getCanonicalName()+".finishZoom");
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
	@Override
	public NumericProperty<Rake> left() {
		return left;
	}
	@Override
	public NumericProperty<Rake> top() {
		return top;
	}
	@Override
	public NumericProperty<Rake> width() {
		return width;
	}
	@Override
	public NumericProperty<Rake> height() {
		return height;
	}
	@Override
	public View view() {
		return this;
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		left.property.unbind();
		top.property.unbind();
		width.property.unbind();
		height.property.unbind();
		innerWidth.property.unbind();
		innerHeight.property.unbind();
		shiftX.property.unbind();
		shiftY.property.unbind();
		zoom.property.unbind();
		maxZoom.property.unbind();
		tapX.property.unbind();
		tapY.property.unbind();
		solid.property.unbind();
		afterTap.property.unbind();
		afterShift.property.unbind();
		afterZoom.property.unbind();
	}
	
}
