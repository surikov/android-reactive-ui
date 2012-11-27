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

public class Fit extends TextView {
	public NoteProperty<Fit> labelText = new NoteProperty<Fit>(this);
	public NumericProperty<Fit> width = new NumericProperty<Fit>(this);
	public NumericProperty<Fit> height = new NumericProperty<Fit>(this);
	public NumericProperty<Fit> left = new NumericProperty<Fit>(this);
	public NumericProperty<Fit> top = new NumericProperty<Fit>(this);
	public NumericProperty<Fit> gravity = new NumericProperty<Fit>(this); //android.view.Gravity.CENTER
	public NumericProperty<Fit> labelColor = new NumericProperty<Fit>(this);
	public NumericProperty<Fit> background = new NumericProperty<Fit>(this);
	public NumericProperty<Fit> textAppearance = new NumericProperty<Fit>(this); //android.R.style.TextAppearance_Small_Inverse
	public ItProperty<Fit, Typeface> labelFace = new ItProperty<Fit, Typeface>(this); // .face.is(Typeface.createFromAsset(me.getAssets(), "fonts/PoiretOne-Regular.ttf"))
	public NumericProperty<Fit> labelSize = new NumericProperty<Fit>(this);
	Context context;
	Vector<Sketch> figures = new Vector<Sketch>();
	boolean initialized = false;
	Task reFit = new Task() {
		@Override
		public void doTask() {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
					width.property.value().intValue()//
					, height.property.value().intValue());
			params.leftMargin = left.property.value().intValue();
			params.topMargin = top.property.value().intValue();
			Fit.this.setLayoutParams(params);
		}
	};
	Task postInvalidate = new Task() {
		@Override
		public void doTask() {
			postInvalidate();
		}
	};

	public Fit(Context context) {
		super(context);
		init(context);
	}
	public Fit(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public Fit(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	void init(Context c) {
		if (initialized)
			return;
		initialized = true;
		this.context = c;
		//final float density = context.getResources().getDisplayMetrics().density;
		textAppearance.property.afterChange(new Task() {
			@Override
			public void doTask() {
				setTextAppearance(context, textAppearance.property.value().intValue());
			}
		});
		labelText.property.afterChange(new Task() {
			@Override
			public void doTask() {
				setText(labelText.property.value());
			}
		});
		gravity.property.afterChange(new Task() {
			@Override
			public void doTask() {
				setGravity(gravity.property.value().intValue());
			}
		});
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
	public Fit sketch(Sketch f) {
		this.figures.add(f);
		this.postInvalidate();
		return this;
	}
	public void drop(Sketch f) {
		this.figures.remove(f);
		this.postInvalidate();
	}
	public void clear() {
		this.figures.removeAllElements();
		this.postInvalidate();
	}
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < figures.size(); i++) {
			figures.get(i).draw(canvas);
		}
	}
}
