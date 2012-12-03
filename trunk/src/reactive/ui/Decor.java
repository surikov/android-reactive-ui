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
import android.view.ViewGroup;
import android.widget.*;
import tee.binding.task.*;
import tee.binding.it.*;
import android.text.*;

public class Decor extends TextView {
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
	//public ItProperty<Fit, Task> afterDrag = new ItProperty<Fit, Task>(this);
	Vector<Sketch> sketches = new Vector<Sketch>();
	Context context;
	boolean initialized = false;
	Task reFit = new Task() {
		@Override
		public void doTask() {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
					width.property.value().intValue()//
					, height.property.value().intValue());
			params.leftMargin = left.property.value().intValue();
			params.topMargin = top.property.value().intValue();
			Decor.this.setLayoutParams(params);
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
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < sketches.size(); i++) {
			sketches.get(i).draw(canvas);
		}
	}
}
