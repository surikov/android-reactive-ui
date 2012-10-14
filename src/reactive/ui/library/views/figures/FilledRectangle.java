package reactive.ui.library.views.figures;

import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.view.*;
import tee.binding.task.*;
import tee.binding.it.*;
import java.util.*;
import android.*;

import reactive.ui.library.views.*;

public class FilledRectangle extends Figure {

	private Paint strokePaint = new Paint();
	private Paint fillPaint = new Paint();
	public NumericProperty<FilledRectangle> arcX = new NumericProperty<FilledRectangle>(this);
	public NumericProperty<FilledRectangle> arcY = new NumericProperty<FilledRectangle>(this);
	public NumericProperty<FilledRectangle> thickness = new NumericProperty<FilledRectangle>(this);
	public NumericProperty<FilledRectangle> stroke = new NumericProperty<FilledRectangle>(this);
	public NumericProperty<FilledRectangle> fill = new NumericProperty<FilledRectangle>(this);
	public NumericProperty<FilledRectangle> x = new NumericProperty<FilledRectangle>(this);
	public NumericProperty<FilledRectangle> y = new NumericProperty<FilledRectangle>(this);
	public NumericProperty<FilledRectangle> w = new NumericProperty<FilledRectangle>(this);
	public NumericProperty<FilledRectangle> h = new NumericProperty<FilledRectangle>(this);

	public FilledRectangle(View forPostInvalidate,WhiteBoard forShift) {
		super( forPostInvalidate,forShift);

		stroke.is(0x99ff0000);
		fill.is(0x99ff00ff);
		w.is(100);
		h.is(100);
		strokePaint.setColor(0x99ff0000);
		strokePaint.setStyle(Paint.Style.STROKE);
		strokePaint.setAntiAlias(true);
		strokePaint.setStrokeCap(Paint.Cap.ROUND);
		stroke.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (stroke.property.value().intValue() != strokePaint.getColor()) {
					strokePaint.setColor(stroke.property.value().intValue());
					postInvalidate.start();
				}
			}
		});
		fillPaint.setAntiAlias(true);
		fillPaint.setColor(0x99ff00ff);
		fill.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (fill.property.value().intValue() != fillPaint.getColor()) {
					fillPaint.setColor(fill.property.value().intValue());
					postInvalidate.start();
				}
			}
		});
		thickness.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (thickness.property.value().floatValue() != strokePaint.getStrokeWidth()) {
					strokePaint.setStrokeWidth(thickness.property.value().floatValue());
					postInvalidate.start();
				}
			}
		});
		x.property.afterChange(postInvalidate);
		y.property.afterChange(postInvalidate);
		w.property.afterChange(postInvalidate);
		h.property.afterChange(postInvalidate);
		arcX.property.afterChange(postInvalidate);
		arcY.property.afterChange(postInvalidate);
	}

	@Override
	public void cleanUp() {
		thickness.property.unbind();
		fill.property.unbind();
		stroke.property.unbind();
		x.property.unbind();
		y.property.unbind();
		w.property.unbind();
		h.property.unbind();
		arcX.property.unbind();
		arcY.property.unbind();
	}

	@Override
	public void draw(Canvas canvas) {
		/*canvas.drawLine(x1.property.value().floatValue(), y1.property.value().floatValue()//
				, x2.property.value().floatValue(), y2.property.value().floatValue()//
				, paint//
		);*/
		canvas.drawRoundRect(new RectF(//
				getShiftX() +x.property.value().floatValue()//
				, getShiftY() +y.property.value().floatValue()//
				, getShiftX() +x.property.value().floatValue()+w.property.value().floatValue()//
				, getShiftY() +y.property.value().floatValue()+h.property.value().floatValue())//
				, arcX.property.value().floatValue()//
				, arcY.property.value().floatValue()//
				, fillPaint);
		if (thickness.property.value() > 0) {
			canvas.drawRoundRect(new RectF(//
					getShiftX() +x.property.value().floatValue()//
					, getShiftY() +y.property.value().floatValue()//
					, getShiftX() +x.property.value().floatValue()+w.property.value().floatValue()//
					, getShiftY() +y.property.value().floatValue()+h.property.value().floatValue())//
					, arcX.property.value().floatValue()//
					, arcY.property.value().floatValue()//
					, strokePaint);
		}
	};
}
