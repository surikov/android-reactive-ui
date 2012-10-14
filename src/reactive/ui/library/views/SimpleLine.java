package reactive.ui.library.views;

import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.view.*;
import tee.binding.task.*;
import tee.binding.it.*;

public class SimpleLine extends View {
    private Paint paint = new Paint();
    public ToggleProperty<SimpleLine> down = new ToggleProperty<SimpleLine>(this);
    public NumericProperty<SimpleLine> thickness = new NumericProperty<SimpleLine>(this);
    public NumericProperty<SimpleLine> color = new NumericProperty<SimpleLine>(this);
    private int w = 100;
    private int h = 100;
    SimpleLine me = this;

    public SimpleLine(Context context) {
	super(context);
	paint.setColor(0x99ff0000);
	color.is(0x99ff0000);
	down.is(true);
	paint.setAntiAlias(true);
	paint.setStrokeCap(Paint.Cap.ROUND);
	color.property.afterChange(new Task() {
	    @Override
	    public void doTask() {
		if (color.property.value().intValue() != paint.getColor()) {
		    paint.setColor(color.property.value().intValue());
		    me.postInvalidate();
		}
	    }
	});
	thickness.property.afterChange(new Task() {
	    @Override
	    public void doTask() {
		if (thickness.property.value().floatValue() != paint.getStrokeWidth()) {
		    paint.setStrokeWidth(thickness.property.value().floatValue());
		    me.postInvalidate();
		}
	    }
	});
	down.property.afterChange(new Task() {
	    @Override
	    public void doTask() {
		me.postInvalidate();
	    }
	});
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
	super.onLayout(changed, left, top, right, bottom);
	w = right - left;
	h = bottom - top;
    }

    protected void onDraw(Canvas canvas) {
	super.onDraw(canvas);
	float t = (float) (thickness.property.value() / 2);
	if (down.property.value()) {
	    canvas.drawLine(t, t, w - t, h - t, paint);
	} else {
	    canvas.drawLine(t, h - t, w - t, t, paint);
	}
    }
}
