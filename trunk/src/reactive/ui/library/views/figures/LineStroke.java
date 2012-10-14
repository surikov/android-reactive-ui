package reactive.ui.library.views.figures;

import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.view.*;
import tee.binding.task.*;
import tee.binding.it.*;
import java.util.*;


import reactive.ui.library.views.*;

public class LineStroke extends Figure {

	private Paint paint = new Paint();
	public NumericProperty<LineStroke> thickness = new NumericProperty<LineStroke>(this);
	public NumericProperty<LineStroke> color = new NumericProperty<LineStroke>(this);
	public NumericProperty<LineStroke> x1 = new NumericProperty<LineStroke>(this);
	public NumericProperty<LineStroke> y1 = new NumericProperty<LineStroke>(this);
	public NumericProperty<LineStroke> x2 = new NumericProperty<LineStroke>(this);
	public NumericProperty<LineStroke> y2 = new NumericProperty<LineStroke>(this);

	public LineStroke(View forPostInvalidate, WhiteBoard forShift) {
		super(forPostInvalidate, forShift);
		paint.setColor(0x99ff0000);
		color.is(0x99ff0000);
		x2.is(100);
		y2.is(100);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.ROUND);
		color.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (color.property.value().intValue() != paint.getColor()) {
					paint.setColor(color.property.value().intValue());
					postInvalidate.start();
				}
			}
		});
		thickness.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (thickness.property.value().floatValue() != paint.getStrokeWidth()) {
					paint.setStrokeWidth(thickness.property.value().floatValue());
					postInvalidate.start();
				}
			}
		});
		x1.property.afterChange(postInvalidate);
		x2.property.afterChange(postInvalidate);
		y1.property.afterChange(postInvalidate);
		y2.property.afterChange(postInvalidate);
	}

	@Override
	public void cleanUp() {
		thickness.property.unbind();
		color.property.unbind();
		x1.property.unbind();
		x2.property.unbind();
		y1.property.unbind();
		y2.property.unbind();
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawLine(//
				getShiftX() + x1.property.value().floatValue()//
				, getShiftY() + y1.property.value().floatValue()//
				, getShiftX() + x2.property.value().floatValue()//
				, getShiftY() + y2.property.value().floatValue()//
				, paint//
		);
	};
}
