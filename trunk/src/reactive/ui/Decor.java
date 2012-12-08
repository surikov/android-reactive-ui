package reactive.ui;

import java.util.Vector;

import reactive.ui.*;
import reactive.ui.library.views.SimpleString;
import reactive.ui.library.views.WhiteBoard;
import reactive.ui.library.views.figures.Figure;
import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
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
	//public NumericProperty<Fit> gravity = new NumericProperty<Fit>(this); //android.view.Gravity.CENTER
	public NumericProperty<Decor> labelColor = new NumericProperty<Decor>(this);
	public NumericProperty<Decor> background = new NumericProperty<Decor>(this);
	//public NumericProperty<Fit> textAppearance = new NumericProperty<Fit>(this); //android.R.style.TextAppearance_Small_Inverse
	public ItProperty<Decor, Typeface> labelFace = new ItProperty<Decor, Typeface>(this); // .face.is(Typeface.createFromAsset(me.getAssets(), "fonts/PoiretOne-Regular.ttf"))
	public NumericProperty<Decor> labelSize = new NumericProperty<Decor>(this);
	public ToggleProperty<Decor> active = new ToggleProperty<Decor>(this);
	//public ItProperty<Fit, Task> afterDrag = new ItProperty<Fit, Task>(this);
	public ItProperty<Decor, Bitmap> bitmap = new ItProperty<Decor, Bitmap>(this);
	public ItProperty<Decor, Task> afterTap = new ItProperty<Decor, Task>(this);
	public ItProperty<Decor, Task> afterShift = new ItProperty<Decor, Task>(this);
	Vector<Sketch> sketches = new Vector<Sketch>();
	Context context;
	Paint paint = new Paint();
	boolean initialized = false;
	Task reFit = new Task() {
		@Override
		public void doTask() {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
					width.property.value().intValue()//
					, height.property.value().intValue());
			params.leftMargin = (int) (left.property.value() + shiftX.property.value());
			params.topMargin = (int) (top.property.value()+shiftY.property.value());
			Decor.this.setLayoutParams(params);
			//System.out.println("reFit " + shiftX.property.value() + "x" + shiftY.property.value());
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
		if (initialized)
			return;
		initialized = true;
		this.context = c;
		paint.setColor(0xff000000);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		//final float density = context.getResources().getDisplayMetrics().density;
		/*textAppearance.property.afterChange(new Task() {
			@Override
			public void doTask() {
				setTextAppearance(context, textAppearance.property.value().intValue());
			}
		});*/
		labelText.property.afterChange(new Task() {
			@Override
			public void doTask() {
				setText(labelText.property.value(), BufferType.SPANNABLE);
				//setText(getText(),BufferType.SPANNABLE);
			}
		});
		/*gravity.property.afterChange(new Task() {
			@Override
			public void doTask() {
				setGravity(gravity.property.value().intValue());
				setText(labelText.property.value(),BufferType.SPANNABLE);
			}
		});*/
		//gravity.is(Gravity.LEFT|Gravity.TOP);
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
					//resetCenterShift();
					//labelFace.postInvalidate();
				}
			}
		});
		labelSize.is(this.getTextSize());
		//System.out.println(getTextSize()+": "+density);
		//System.out.println(getTextSize());
		labelSize.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (labelSize.property.value().floatValue() != getTextSize()) {
					//System.out.println("set "+labelSize.property.value());
					//setTextSize((float) (labelSize.property.value() / density));
					setTextSize(labelSize.property.value().floatValue());
					//System.out.println("now "+getTextSize());
					//resetCenterShift();
					//postInvalidate();
					//Fit.this.requestLayout();
					//reFit.start();
					//setGravity(Gravity.CENTER);
					//setGravity(Gravity.LEFT|Gravity.TOP);
					//Fit.this.requestLayout();
				}
			}
		});
		width.property.afterChange(reFit).value(100);
		height.property.afterChange(reFit).value(100);
		left.property.afterChange(reFit);
		top.property.afterChange(reFit);
		shiftY.property.afterChange(reFit);
		shiftX.property.afterChange(reFit);
		//labelSize.property.afterChange(reLayout);
		//this.setTextAppearance(context, android.R.style.TextAppearance_Small_Inverse);
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
		//System.out.println("start setShift " + x + "x" + y);
		double newShiftX = shiftX.property.value() + x -startEventX;
		double newShiftY = shiftY.property.value() + y - startEventY;
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
		//System.out.println("result setShift " + newShiftX + "x" + newShiftY);
		shiftX.property.value(newShiftX);
		shiftY.property.value(newShiftY);
		//reFit.start();
	}
	void finishDrag(float x, float y) {
		setShift(x, y);
		if (Math.abs(initialShiftX - shiftX.property.value()) < Layoutless.tapDiff// 
				&& Math.abs(initialShiftY - shiftY.property.value()) < Layoutless.tapDiff) {
			finishTap(x, y);
		}
		else {
			if (afterShift.property.value() != null) {
				afterShift.property.value().start();
			}
		}
		mode = Layoutless.NONE;
	}
	void finishTap(float x, float y) {
		shiftX.property.value((double) initialShiftX);
		shiftY.property.value((double) initialShiftY);
		//tapX.property.value((double) x);
		//tapY.property.value((double) y);
		if (afterTap.property.value() != null) {
			afterTap.property.value().start();
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!active.property.value()) {
			return false;
		}
		
		if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
			//System.out.println("startDrag "+event.getX()+"x"+ event.getY());
			initialShiftX = shiftX.property.value().floatValue();
			initialShiftY = shiftY.property.value().floatValue();
			startEventX = event.getX();
			startEventY = event.getY();
			mode = Layoutless.DRAG;
		}
		else {
			if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
				//System.out.println("proceedDrag");
				setShift(event.getX(), event.getY());
				//lastEventX = event.getX();
				//lastEventY = event.getY();
			}
			else {
				if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
					if (mode == Layoutless.DRAG) {
						//System.out.println("finishDrag");
						finishDrag(event.getX(), event.getY());
					}
					else {
						//
					}
				}
				else {
					//System.out.println("not ACTION_UP");
				}
			}
		}
		return true;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (bitmap.property.value() != null) {
			canvas.drawBitmap(bitmap.property.value()//
					, 0//
					, 0//
					, paint);
		}
		for (int i = 0; i < sketches.size(); i++) {
			sketches.get(i).draw(canvas);
		}
	}
}
