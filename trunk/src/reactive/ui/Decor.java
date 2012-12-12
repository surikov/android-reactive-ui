package reactive.ui;

import java.util.Vector;

import reactive.ui.*;
import reactive.ui.library.views.*;
import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import tee.binding.task.*;
import tee.binding.it.*;
import android.text.*;

public class Decor extends TextView {
	private int mode = Layoutless.NONE;
	private float startEventX = 0;
	private float startEventY = 0;
	private float initialShiftX = 0;
	private float initialShiftY = 0;
	private float initialSpacing;
	private float currentSpacing;
	public NumericProperty<Decor> shiftX = new NumericProperty<Decor>(this);
	public NumericProperty<Decor> shiftY = new NumericProperty<Decor>(this);
	public NoteProperty<Decor> labelText = new NoteProperty<Decor>(this);
	public NumericProperty<Decor> width = new NumericProperty<Decor>(this);
	public NumericProperty<Decor> height = new NumericProperty<Decor>(this);
	public NumericProperty<Decor> left = new NumericProperty<Decor>(this);
	public NumericProperty<Decor> top = new NumericProperty<Decor>(this);
	public NumericProperty<Decor> labelColor = new NumericProperty<Decor>(this);
	public NumericProperty<Decor> background = new NumericProperty<Decor>(this);
	public ItProperty<Decor, Typeface> labelFace = new ItProperty<Decor, Typeface>(this); // .face.is(Typeface.createFromAsset(me.getAssets(), "fonts/PoiretOne-Regular.ttf"))
	public NumericProperty<Decor> labelSize = new NumericProperty<Decor>(this);
	public ToggleProperty<Decor> movableX = new ToggleProperty<Decor>(this);
	public ToggleProperty<Decor> movableY = new ToggleProperty<Decor>(this);
	public ItProperty<Decor, Bitmap> bitmap = new ItProperty<Decor, Bitmap>(this);//Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rocket),200,100,true);
	public ItProperty<Decor, Task> afterTap = new ItProperty<Decor, Task>(this);
	public ItProperty<Decor, Task> afterShift = new ItProperty<Decor, Task>(this);
	Vector<Sketch> sketches = new Vector<Sketch>();
	Context context;
	Paint paint = new Paint();
	boolean initialized = false;
	Task reFit = new Task() {
		@Override
		public void doTask() {
			//System.out.
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
					width.property.value().intValue()//
					, height.property.value().intValue());
			params.leftMargin = (int) (left.property.value() + shiftX.property.value());
			params.topMargin = (int) (top.property.value() + shiftY.property.value());
			
			Decor.this.setLayoutParams(params);
			//System.out.println("params.topMargin: " + params.topMargin+" / "+Decor.this.getLeft()+"x"+Decor.this.getTop()+"/"+Decor.this.getWidth()+"x"+Decor.this.getHeight());
		}
	};
	Task postInvalidate = new Task() {
		@Override
		public void doTask() {
			postInvalidate();
		}
	};

	public Decor labelAlignLeftTop() {
		setGravity(android.view.Gravity.LEFT | android.view.Gravity.TOP);
		setText(labelText.property.value(), BufferType.SPANNABLE);
		return this;
	}
	public Decor labelAlignLeftCenter() {
		setGravity(android.view.Gravity.LEFT | android.view.Gravity.CENTER_VERTICAL);
		setText(labelText.property.value(), BufferType.SPANNABLE);
		return this;
	}
	public Decor labelAlignLeftBottom() {
		setGravity(android.view.Gravity.LEFT | android.view.Gravity.BOTTOM);
		setText(labelText.property.value(), BufferType.SPANNABLE);
		return this;
	}
	public Decor labelAlignRightTop() {
		setGravity(android.view.Gravity.RIGHT | android.view.Gravity.TOP);
		setText(labelText.property.value(), BufferType.SPANNABLE);
		return this;
	}
	public Decor labelAlignRightCenter() {
		setGravity(android.view.Gravity.RIGHT | android.view.Gravity.CENTER_VERTICAL);
		setText(labelText.property.value(), BufferType.SPANNABLE);
		return this;
	}
	public Decor labelAlignRightBottom() {
		setGravity(android.view.Gravity.RIGHT | android.view.Gravity.BOTTOM);
		setText(labelText.property.value(), BufferType.SPANNABLE);
		return this;
	}
	public Decor labelAlignCenterTop() {
		setGravity(android.view.Gravity.CENTER_HORIZONTAL | android.view.Gravity.TOP);
		setText(labelText.property.value(), BufferType.SPANNABLE);
		return this;
	}
	public Decor labelAlignCenterCenter() {
		setGravity(android.view.Gravity.CENTER_HORIZONTAL | android.view.Gravity.CENTER_VERTICAL);
		setText(labelText.property.value(), BufferType.SPANNABLE);
		return this;
	}
	public Decor labelAlignCenterBottom() {
		setGravity(android.view.Gravity.CENTER_HORIZONTAL | android.view.Gravity.BOTTOM);
		setText(labelText.property.value(), BufferType.SPANNABLE);
		return this;
	}
	public Decor labelStyleSmallNormal() {
		setTextAppearance(context, android.R.style.TextAppearance_Small);
		return this;
	}
	public Decor labelStyleMediumNormal() {
		setTextAppearance(context, android.R.style.TextAppearance_Medium);
		return this;
	}
	public Decor labelStyleLargeNormal() {
		setTextAppearance(context, android.R.style.TextAppearance_Large);
		return this;
	}
	public Decor labelStyleSmallInverse() {
		setTextAppearance(context, android.R.style.TextAppearance_Small_Inverse);
		return this;
	}
	public Decor labelStyleMediumInverse() {
		setTextAppearance(context, android.R.style.TextAppearance_Medium_Inverse);
		return this;
	}
	public Decor labelStyleLargeInverse() {
		setTextAppearance(context, android.R.style.TextAppearance_Large_Inverse);
		return this;
	}
	public Decor(Context context) {
		super(context);
		init(context);
	}
	public Decor(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public Decor(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	void init(Context c) {
		if (initialized) {
			return;
		}
		initialized = true;
		this.context = c;
		movableX.is(false);
		movableY.is(false);
		paint.setColor(0xff000000);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		labelText.property.afterChange(new Task() {
			@Override
			public void doTask() {
				setText(labelText.property.value(), BufferType.SPANNABLE);
			}
		});
		labelColor.is(this.getCurrentTextColor());
		labelColor.property.afterChange(new Task() {
			@Override
			public void doTask() {
				setTextColor(labelColor.property.value().intValue());
			}
		});
		background.property.afterChange(new Task() {
			@Override
			public void doTask() {
				setBackgroundColor(background.property.value().intValue());
			}
		});
		labelFace.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (labelFace.property.value() != null) {
					setTypeface(labelFace.property.value());
				}
			}
		});
		labelSize.is(this.getTextSize());
		labelSize.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (labelSize.property.value().floatValue() != getTextSize()) {
					setTextSize(labelSize.property.value().floatValue());
				}
			}
		});
		width.property.afterChange(reFit).value(100);
		height.property.afterChange(reFit).value(100);
		left.property.afterChange(reFit);
		top.property.afterChange(reFit);
		shiftY.property.afterChange(reFit);
		shiftX.property.afterChange(reFit);
	}
	public Decor sketch(Sketch f) {
		this.sketches.add(f);
		f.forUpdate = this;
		this.postInvalidate();
		return this;
	}
	public void drop(Sketch f) {
		this.sketches.remove(f);
		f.forUpdate = null;
		this.postInvalidate();
	}
	public void clear() {
		for (int i = 0; i < sketches.size(); i++) {
			this.sketches.get(i).forUpdate = null;
		}
		this.sketches.removeAllElements();
		this.postInvalidate();
	}
	void setShift(float x, float y) {
		if (movableX.property.value()) {
			double newShiftX = shiftX.property.value() + x - startEventX;
			//double newShiftY = shiftY.property.value() + y - startEventY;
			shiftX.property.value(newShiftX);
			//shiftY.property.value(newShiftY);
		}
		if (movableY.property.value()) {
			//double newShiftX = shiftX.property.value() + x - startEventX;
			double newShiftY = shiftY.property.value() + y - startEventY;
			//shiftX.property.value(newShiftX);
			shiftY.property.value(newShiftY);
		}
	}
	void finishDrag(float x, float y) {
		setShift(x, y);
		if (Math.abs(initialShiftX - shiftX.property.value()) < 1+0.7*Layoutless.tapSize// 
				&& Math.abs(initialShiftY - shiftY.property.value()) < 1+0.1*Layoutless.tapSize) {
			finishTap(x, y);
		}
		else {
			if (movableX.property.value() || movableY.property.value()) {
				if (afterShift.property.value() != null) {
					afterShift.property.value().start();
				}
			}
		}
		mode = Layoutless.NONE;
	}
	void finishTap(float x, float y) {
		shiftX.property.value((double) initialShiftX);
		shiftY.property.value((double) initialShiftY);
		if (afterTap.property.value() != null) {
			afterTap.property.value().start();
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!(afterTap.property.value() != null || movableX.property.value() || movableY.property.value())) {
			return false;
		}
		if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN //
				&& (afterTap.property.value() != null //
						|| movableX.property.value() //
				|| movableY.property.value()//
				)) {
			initialShiftX = shiftX.property.value().floatValue();
			initialShiftY = shiftY.property.value().floatValue();
			startEventX = event.getX();
			startEventY = event.getY();
			mode = Layoutless.DRAG;
			//this.bringToFront();
		}
		else {
			if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE //
					&& (afterTap.property.value() != null //
							|| movableX.property.value()//
					|| movableY.property.value()//
					)) {
				setShift(event.getX(), event.getY());
			}
			else {
				if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
					if (mode == Layoutless.DRAG) {
						finishDrag(event.getX(), event.getY());
					}
					else {
						//
					}
				}
				else {
					//
				}
			}
		}
		return true;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (bitmap.property.value() != null) {
			canvas.drawBitmap(bitmap.property.value(), 0, 0, paint);
		}
		for (int i = 0; i < sketches.size(); i++) {
			sketches.get(i).draw(canvas);
		}
	}
}
