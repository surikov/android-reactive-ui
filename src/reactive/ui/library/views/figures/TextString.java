package reactive.ui.library.views.figures;

import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.view.*;
import tee.binding.task.*;
import tee.binding.it.*;
import java.util.*;


import reactive.ui.library.views.*;

public class TextString extends Figure {
	private Paint paint = new Paint();
	public NumericProperty<TextString> color = new NumericProperty<TextString>(this);
	public NoteProperty<TextString> text = new NoteProperty<TextString>(this);
	public NumericProperty<TextString> x = new NumericProperty<TextString>(this);
	public NumericProperty<TextString> y = new NumericProperty<TextString>(this);
	public NumericProperty<TextString> size = new NumericProperty<TextString>(this);
	public ItProperty<TextString, Typeface> face = new ItProperty<TextString, Typeface>(this);

	public TextString(View forPostInvalidate,WhiteBoard forShift) {
		super( forPostInvalidate,forShift);
		paint.setColor(0x99ff0000);
		color.is(0x99ff0000);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.ROUND);
		//paint.setTextAlign(align)
		//final float density =1; 
			//c.getResources().getDisplayMetrics().density;
		color.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (color.property.value().intValue() != paint.getColor()) {
					paint.setColor(color.property.value().intValue());
					postInvalidate.start();
				}
			}
		});
		size.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (size.property.value().floatValue() != paint.getTextSize()) {
					paint.setTextSize(size.property.value().floatValue());
					postInvalidate.start();
				}
			}
		});
		face.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (face.property.value() != paint.getTypeface()) {
					paint.setTypeface(face.property.value());
					postInvalidate.start();
				}
			}
		});
		x.property.afterChange(postInvalidate);
		x.property.afterChange(postInvalidate);
		text.property.afterChange(postInvalidate);
	}

	@Override
	public void cleanUp() {
		size.property.unbind();
		color.property.unbind();
		x.property.unbind();
		y.property.unbind();
	}

	@Override
	public void draw(Canvas canvas) {
		//Tools.log("" + paint.ascent());
		//Tools.log("" + paint.descent());
		canvas.drawText(text.property.value()//
				, getShiftX() +x.property.value().floatValue()//
				, getShiftY() +y.property.value().floatValue() - paint.ascent()//
				, paint);
	};
}
