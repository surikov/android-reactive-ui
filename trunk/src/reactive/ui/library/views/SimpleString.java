package reactive.ui.library.views;

import reactive.ui.library.*;
import reactive.ui.library.views.cells.*;
import reactive.ui.library.views.figures.*;
import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;
import tee.binding.task.*;
import tee.binding.it.*;
import android.text.*;

public class SimpleString extends TextView implements Unbind {
	public final static double alphaPixelHeightPerFont = 1.8;
	public final static double alphaPixelWidthPerFont = 0.48;
	public NoteProperty<SimpleString> text = new NoteProperty<SimpleString>(this);
	public NumericProperty<SimpleString> size = new NumericProperty<SimpleString>(this);
	public NumericProperty<SimpleString> color = new NumericProperty<SimpleString>(this);
	public ItProperty<SimpleString, Typeface> face = new ItProperty<SimpleString, Typeface>(this); // .face.is(Typeface.createFromAsset(me.getAssets(), "fonts/PoiretOne-Regular.ttf"))
	SimpleString me = this;

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w != oldw || h != oldh) {
			this.setText(text.property.value());
			resetCenterShift();
		}
		//ViewGroup.LayoutParams newLayout = new LinearLayout.LayoutParams(w, h);
		//setLayoutParams(newLayout);
		//this.onLayout(changed, left, top, right, bottom)
		//this.requestLayout();
		//this.postInvalidate();
		//if (this.getParent() != null)
		//Tools.log("SimpleButton onSizeChanged " + w + "x" + h + " / " + this.getParent().isLayoutRequested());
		//Layout layout = this.getLayout();
		//System.out.println(text.property.value()+": onLayout");
		//System.out.println(text.property.value()+": onLayout "+layout);
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		resetCenterShift();
	}
	void resetCenterShift() {
		//setGravity(Gravity.CENTER);
		Rect currentBounds = new Rect();
		me.getPaint().getTextBounds(text.property.value() + "\n123", 0, text.property.value().length(), currentBounds);
		Layout layout = this.getLayout();
		//System.out.println(text.property.value()+": "+currentBounds.width() + "x" + currentBounds.height()+" /"+layout);
		if (layout != null) {
			System.out.println(text.property.value() + ": " + layout.getHeight() + "/" + layout.getLineCount());
		}
	}
	public SimpleString(Context c) {
		super(c);
		//ViewGroup.LayoutParams
		//android.view.WindowManager.LayoutParams
		//android.view.WindowManager.LayoutParams   params=new android.view.WindowManager.LayoutParams();
		//params.gravity= Gravity.CENTER_HORIZONTAL; 
		//this.setLayoutParams(params);
		//LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(				300,300 );
		//para.gravity = Gravity.CENTER;
		//setLayoutParams(para);
		setGravity(Gravity.CENTER);
		this.setBackgroundColor(0x6600dd00);
		//this.setl
		//this.setBackgroundColor(Color.RED);
		final float density = c.getResources().getDisplayMetrics().density;
		size.is(this.getTextSize());
		color.is(this.getCurrentTextColor());
		text.property.afterChange(new Task() {
			@Override
			public void doTask() {
				me.setText(text.property.value());
				resetCenterShift();
				me.postInvalidate();
			}
		});
		size.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (size.property.value().floatValue() != me.getTextSize()) {
					me.setTextSize((float) (size.property.value() / density));
					resetCenterShift();
					me.postInvalidate();
				}
			}
		});
		color.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (color.property.value().intValue() != me.getCurrentTextColor()) {
					me.setTextColor(color.property.value().intValue());
					me.postInvalidate();
				}
			}
		});
		face.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (face.property.value() != null) {
					me.setTypeface(face.property.value());
					resetCenterShift();
					me.postInvalidate();
				}
			}
		});
	}
	@Override
	public void unbind() {
	}
}
