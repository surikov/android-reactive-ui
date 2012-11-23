package reactive.ui.library.views;

import reactive.ui.library.*;
import reactive.ui.library.views.cells.*;
import reactive.ui.library.views.figures.*;
import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.view.*;
import tee.binding.task.*;
import tee.binding.it.*;

public class SimpleRectangle extends View implements Unbind {
	public final static int GRADIENT_NONE=0;
	public final static int GRADIENT_TOPLEFT_TO_RIGHTBOTTOM=1;
	private Paint paint = new Paint();
	public NumericProperty<SimpleRectangle> arcX = new NumericProperty<SimpleRectangle>(this);
	public NumericProperty<SimpleRectangle> arcY = new NumericProperty<SimpleRectangle>(this);
	public NumericProperty<SimpleRectangle> color = new NumericProperty<SimpleRectangle>(this);
	public NumericProperty<SimpleRectangle> gradient = new NumericProperty<SimpleRectangle>(this);
	public NumericProperty<SimpleRectangle> gradientKind = new NumericProperty<SimpleRectangle>(this);
	//public ToggleProperty<SimpleRectangle> gradient = new ToggleProperty<SimpleRectangle>(this);
	private int w = 100;
	private int h = 100;
	private double ax = 0;
	private double ay = 0;
	SimpleRectangle me = this;

	@Override
	public void unbind() {
	}
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
	public SimpleRectangle(Context context) {
		super(context);
		color.is(0x99ff0000);
		gradientKind.is(GRADIENT_NONE);
		paint.setAntiAlias(true);
		paint.setColor(0x99ff0000);
		color.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (color.property.value().intValue() != paint.getColor()) {
					resetPaint();
				}
			}
		});
		gradient.property.afterChange(new Task() {
			@Override
			public void doTask() {
				resetPaint();
			}
		});
		gradientKind.property.afterChange(new Task() {
			@Override
			public void doTask() {
				resetPaint();
			}
		});
		arcX.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (arcX.property.value() != ax) {
					ax = arcX.property.value();
					//me.postInvalidate();
					resetPaint();
				}
			}
		});
		arcY.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (arcY.property.value() != ay) {
					ax = arcY.property.value();
					//me.postInvalidate();
					resetPaint();
				}
			}
		});
	}
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		w = right - left;
		h = bottom - top;
		resetPaint();
	}
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//paint.setColor(0xff00ff00);
		/*
				int w2 = getWidth() / 2;
				int h2 = getHeight() / 2;
				int r = w2;
				if (h2 > w2) {
					r = h2;
				}
				paint.setShader(new RadialGradient(getWidth() / 2, getHeight(), r, Color.BLACK, color.property.value().intValue(), Shader.TileMode.CLAMP));
				*/
		canvas.drawRoundRect(new RectF(0, 0, w, h)//
				, arcX.property.value().floatValue()//
				, arcY.property.value().floatValue()//
				, paint);
	}
}
