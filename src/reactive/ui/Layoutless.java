package reactive.ui;

import android.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import tee.binding.*;
import tee.binding.properties.ToggleProperty;
import android.net.*;
import java.io.*;
import java.text.*;
import android.database.*;
import android.database.sqlite.*;

public class Layoutless extends RelativeLayout implements Rake {
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
	private ToggleProperty<Rake> hidden = new ToggleProperty<Rake>(this);
	private NumericProperty<Rake> left = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> top = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> width = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> height = new NumericProperty<Rake>(this);
	public NumericProperty<Layoutless> innerWidth = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> innerHeight = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> shiftX = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> shiftY = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> lastShiftX = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> lastShiftY = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> zoom = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> maxZoom = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> tapX = new NumericProperty<Layoutless>(this);
	public NumericProperty<Layoutless> tapY = new NumericProperty<Layoutless>(this);
	public ToggleProperty<Layoutless> solid = new ToggleProperty<Layoutless>(this);
	public ItProperty<Layoutless, Task> afterTap = new ItProperty<Layoutless, Task>(this);
	public ItProperty<Layoutless, Task> afterShift = new ItProperty<Layoutless, Task>(this);
	//public ItProperty<Layoutless, Task> afterScroll = new ItProperty<Layoutless, Task>(this);
	public ItProperty<Layoutless, Task> afterZoom = new ItProperty<Layoutless, Task>(this);
	//public ItProperty<Layoutless, Task> afterPress = new ItProperty<Layoutless, Task>(this);
	//public NumericProperty<Layoutless> mode = new NumericProperty<Layoutless>(this);
	//public static int themeForegroundColor = 0xffff0000;
	//public static int themeBlurColor = 0xff330000;
	//public static int themeBlurColor66 = 0xff666666;
	//public static int themeBlurColor99 = 0xff666666;
	//public static int themeFocusColor = 0xff669966;
	//public static int themeBackgroundColor = 0xffffffff;
	private static TextView colorTest;
	private boolean initialized = false;
	public Vector<Rake> children = new Vector<Rake>();
	//private Vector<Decor> fogs = new Vector<Decor>();
	//private Vector<SubLayoutless> dialogs = new Vector<SubLayoutless>();
	private boolean measured = false;

	/*
		public static void fillBaseColors(Context context) {
			if (colorTest == null) {
				colorTest = new TextView(context);
				colorTest.setTextAppearance(context, android.R.style.TextAppearance_Large);
				themeForegroundColor=colorTest.getCurrentTextColor();
				//themeForegroundColor = colorTest.labelStyleLargeNormal().getCurrentTextColor();
				//int c1=colorTest.labelStyleLargeNormal().getCurrentHintTextColor();
				if ((themeForegroundColor & 0x00ffffff) > 0x00666666) {
					themeBlurColor = (themeForegroundColor & 0x00ffffff) + 0x33000000;
				}
				else {
					themeBlurColor = (themeForegroundColor & 0x00ffffff) + 0x11000000;
				}
				//themeBlurColor66 = (themeForegroundColor & 0x00ffffff)+0x66000000;
				//themeBlurColor99 = (themeForegroundColor & 0x00ffffff)+0x99000000;
				//colorTest.labelStyleLargeNormal().getCurrentHintTextColor();
				//themeFocusColor = (themeForegroundColor & 0x00ffffff)+0x22000000;
				//colorTest.settr
				Drawable drawable = colorTest.getBackground();
				//System.out.println(drawable);
				if (drawable instanceof ColorDrawable) {
					ColorDrawable colorDrawable = (ColorDrawable) drawable;
					Rect mBounds = new Rect();
					mBounds.set(colorDrawable.getBounds()); // Save the original bounds.
					colorDrawable.setBounds(0, 0, 1, 1); // Change the bounds.
					Bitmap mBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
					Canvas mCanvas = new Canvas(mBitmap);
					colorDrawable.draw(mCanvas);
					themeBackgroundColor = mBitmap.getPixel(0, 0);
					System.out.println("/"+themeBackgroundColor);
					
				}
				else {
					colorTest.setTextAppearance(context, android.R.style.TextAppearance_Large_Inverse);
					themeBackgroundColor=colorTest.getCurrentTextColor();
					//themeBackgroundColor = colorTest.labelStyleLargeInverse().getCurrentTextColor();
				}
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
				density = context.getResources().getDisplayMetrics().density;
				tapSize = 60.0 * density;
			}
		}*/
	protected void init() {
		if (!initialized) {
			initialized = true;
			solid.is(true);
			//fillBaseColors(getContext());
			Auxiliary.initThemeConstants(this.getContext());
			setFocusable(true);
			setFocusableInTouchMode(true);
			hidden.property.afterChange(new Task() {
				@Override
				public void doTask() {
					if (hidden.property.value()) {
						setVisibility(View.INVISIBLE);
					}
					else {
						setVisibility(View.VISIBLE);
					}
				}
			});
		}
	}
	@Override
	public ToggleProperty<Rake> hidden() {
		return hidden;
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
	public Layoutless input(Context context, double row, double left,Note label, Rake content, Numeric contentWidth) {
		this.child(new Decor(context).labelText.is(label)//
				.left().is(this.shiftX.property.plus(left))//
				.top().is(this.shiftY.property.plus(1.5 * row * Auxiliary.tapSize))//
				.width().is(contentWidth)//
				.height().is(0.5 * Auxiliary.tapSize)//
		);
		this.child(content//
				.left().is(this.shiftX.property.plus(left))//
				.top().is(this.shiftY.property.plus((0.5 + 1.5 * row) * Auxiliary.tapSize))//
				.width().is(contentWidth)//
				.height().is(0.8 * Auxiliary.tapSize)//
		);
		return this;
	}
	public Layoutless input(Context context, double row, double left, Note label, Rake content) {
		return input(context, row, left,label, content, new Numeric().value(5 * Auxiliary.tapSize));
	}
	public Layoutless input(Context context, double row, double left, Note label, Rake content, int contentWidth) {
		return input(context, row, left,label, content, new Numeric().value(contentWidth));
	}
	public Layoutless input(Context context, double row, double left, String label, Rake content) {
		return input(context, row, left,new Note().value(label), content, new Numeric().value(5 * Auxiliary.tapSize));
	}
	public Layoutless input(Context context, double row, double left, String label, Rake content, int contentWidth) {
		return input(context, row, left,new Note().value(label), content, new Numeric().value(contentWidth));
	}
	public Layoutless input(Context context, double row, double left, String label, Rake content, Numeric contentWidth) {
		return input(context, row, left,new Note().value(label), content, contentWidth);
	}
	public Layoutless field(Context context, double row, Note label, Rake content, Numeric contentWidth) {
		this.child(new Decor(context).labelText.is(label)//
				.labelAlignRightCenter()//
				.left().is(this.shiftX.property)//
				.top().is(this.shiftY.property.plus(0.2 * Auxiliary.tapSize).plus(0.8 * row * Auxiliary.tapSize))//
				.width().is(this.width().property.multiply(0.3))//
				.height().is(0.8 * Auxiliary.tapSize)//
		);
		this.child(content//
				.left().is(this.shiftX.property.plus(this.width().property.multiply(0.3).plus(0.1 * Auxiliary.tapSize)))//
				.top().is(this.shiftY.property.plus(0.2 * Auxiliary.tapSize).plus(0.8 * row * Auxiliary.tapSize))//
				.width().is(contentWidth)//
				.height().is(0.8 * Auxiliary.tapSize)//
		);
		return this;
	}
	public Layoutless field(Context context, double row, Note label, Rake content) {
		return field(context, row, label, content, new Numeric().value(5 * Auxiliary.tapSize));
	}
	public Layoutless field(Context context, double row, Note label, Rake content, int contentWidth) {
		return field(context, row, label, content, new Numeric().value(contentWidth));
	}
	public Layoutless field(Context context, double row, String label, Rake content) {
		return field(context, row, new Note().value(label), content, new Numeric().value(5 * Auxiliary.tapSize));
	}
	public Layoutless field(Context context, double row, String label, Rake content, int contentWidth) {
		return field(context, row, new Note().value(label), content, new Numeric().value(contentWidth));
	}
	public Layoutless field(Context context, double row, String label, Rake content, Numeric contentWidth) {
		return field(context, row, new Note().value(label), content, contentWidth);
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
	/*
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
	}*/
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		//System.out.println(this.getClass().getCanonicalName() + ".onSizeChanged "+w+"/"+ h+" <- "+oldw+"/"+ oldh);
		super.onSizeChanged(w, h, oldw, oldh);
		//System.out.println(this.getClass().getCanonicalName() + ".onSizeChanged done");
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		onMeasureX();
	}
	protected void onMeasureX() {
		//System.out.println(this.getClass().getCanonicalName() + ".onMeasure: " + getMeasuredWidth() + " x " + getMeasuredHeight()+": "+measured);
		if (!measured) {
			measured = true;
			width.is(getMeasuredWidth());
			height.is(getMeasuredHeight());
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//System.out.println(shiftX.property.value());
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
		//System.out.println(newShiftX+"x"+newShiftY);
	}
	void finishDrag(float x, float y) {
		setShift(x, y);
		if (Math.abs(initialShiftX - shiftX.property.value()) < 1 + 0.1 * Auxiliary.tapSize// 
				&& Math.abs(initialShiftY - shiftY.property.value()) < 1 + 0.1 * Auxiliary.tapSize) {
			finishTap(x, y);
		}
		else {
			// lastShiftX.is( shiftX.property.value());
			//lastShiftY.is(shiftY.property.value());
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
			if (afterShift.property.value() != null) {
				lastShiftX.property.value(newShiftX);
				lastShiftY.property.value(newShiftY);
				afterShift.property.value().start();
			}
			else {
				shiftX.property.value(newShiftX);
				shiftY.property.value(newShiftY);
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
	/*
	@Override
	protected void onAttachedToWindow() {
		System.out.println("lock");
	}*/
}
