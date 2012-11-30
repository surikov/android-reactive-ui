package reactive.ui;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import tee.binding.properties.*;
import tee.binding.task.Task;

public class Plate extends Sketch {
	public NumericProperty<Plate> width = new NumericProperty<Plate>(this);
	public NumericProperty<Plate> height = new NumericProperty<Plate>(this);
	public NumericProperty<Plate> left = new NumericProperty<Plate>(this);
	public NumericProperty<Plate> top = new NumericProperty<Plate>(this);
	public NumericProperty<Plate> background = new NumericProperty<Plate>(this);
	Paint paint = new Paint();
	protected Task postInvalidate = new Task() {
		@Override
		public void doTask() {
			if (forUpdate != null) {
				forUpdate.postInvalidate();
			}
		}
	};
	
/*
private void resetPaint() {
		paint.setColor(color.property.value().intValue());
		if (gradientKind.property.value() ==GRADIENT_TOPLEFT_TO_RIGHTBOTTOM) {
			paint.setShader(new LinearGradient(0, 0, getWidth(), getHeight()//
					, color.property.value().intValue()//
					, gradient.property.value().intValue()//
					, Shader.TileMode.MIRROR));
			//Tools.log(getWidth()+"/"+getHeight());
		}
		me.postInvalidate();
	}

*/
	public Plate() {
		width.is(100);
		height.is(100);
		left.is(0);
		top.is(0);
		background.is(0xff990000);
		
		width.property.afterChange(new Task() {
			@Override
			public void doTask() {
				postInvalidate.start();
			}
		});
		height.property.afterChange(new Task() {
			@Override
			public void doTask() {
				postInvalidate.start();
			}
		});
		top.property.afterChange(new Task() {
			@Override
			public void doTask() {
				postInvalidate.start();
			}
		});
		background.property.afterChange(new Task() {
			@Override
			public void doTask() {
				paint.setColor(background.property.value().intValue());
				postInvalidate.start();
			}
		});
	}
	public void draw(Canvas canvas) {
		int w = width.property.value().intValue();
		int h = height.property.value().intValue();
		int x = left.property.value().intValue();
		int y = top.property.value().intValue();
		canvas.drawRect(new RectF(x, y, x+w, y+h), paint);
	};
}
