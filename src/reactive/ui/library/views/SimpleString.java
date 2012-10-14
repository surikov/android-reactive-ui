package reactive.ui.library.views;

import reactive.ui.library.*;
import reactive.ui.library.views.cells.*;
import reactive.ui.library.views.figures.*;
import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.widget.*;
import tee.binding.task.*;
import tee.binding.it.*;

public class SimpleString extends TextView  implements Unbind {
	public final static double alphaPixelHeightPerFont = 1.8;
	public final static double alphaPixelWidthPerFont = 0.48;
	public NoteProperty<SimpleString> text = new NoteProperty<SimpleString>(this);
	public NumericProperty<SimpleString> size = new NumericProperty<SimpleString>(this);
	public NumericProperty<SimpleString> color = new NumericProperty<SimpleString>(this);
	public ItProperty<SimpleString, Typeface> face = new ItProperty<SimpleString, Typeface>(this); // .face.is(Typeface.createFromAsset(me.getAssets(), "fonts/PoiretOne-Regular.ttf"))
	SimpleString me = this;

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w != oldw || h != oldh)
			this.setText(text.property.value());
		//ViewGroup.LayoutParams newLayout = new LinearLayout.LayoutParams(w, h);
		//setLayoutParams(newLayout);
		//this.onLayout(changed, left, top, right, bottom)
		//this.requestLayout();
		//this.postInvalidate();
		//if (this.getParent() != null)
		//Tools.log("SimpleButton onSizeChanged " + w + "x" + h + " / " + this.getParent().isLayoutRequested());
	}

	public SimpleString(Context c) {
		super(c);
		//this.setBackgroundColor(0x6600dd00);
		final float density = c.getResources().getDisplayMetrics().density;
		size.is(this.getTextSize());
		color.is(this.getCurrentTextColor());
		text.property.afterChange(new Task() {
			@Override
			public void doTask() {
				me.setText(text.property.value());
				me.postInvalidate();
			}
		});
		size.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (size.property.value().floatValue() != me.getTextSize()) {
					me.setTextSize((float) (size.property.value() / density));
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
					me.postInvalidate();
				}
			}
		});
	}
	@Override
	public void unbind() {
	}
}
