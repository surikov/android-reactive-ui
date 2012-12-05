package reactive.ui;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import tee.binding.properties.*;
import tee.binding.task.Task;

public class SketchFill extends Sketch {
	public NumericProperty<SketchFill> width = new NumericProperty<SketchFill>(this);
	public NumericProperty<SketchFill> height = new NumericProperty<SketchFill>(this);
	public NumericProperty<SketchFill> left = new NumericProperty<SketchFill>(this);
	public NumericProperty<SketchFill> top = new NumericProperty<SketchFill>(this);
	public NumericProperty<SketchFill> background = new NumericProperty<SketchFill>(this);
	public NumericProperty<SketchFill> strokeColor = new NumericProperty<SketchFill>(this);
	public NumericProperty<SketchFill> strokeWidth = new NumericProperty<SketchFill>(this);
	public NumericProperty<SketchFill> arcX = new NumericProperty<SketchFill>(this);
	public NumericProperty<SketchFill> arcY = new NumericProperty<SketchFill>(this);
	
	public ItProperty<SketchFill,Paint> paint = new ItProperty<SketchFill,Paint>(this);
	//Paint paint = new Paint();
	protected Task postInvalidate = new Task() {
		@Override
		public void doTask() {
			if (forUpdate != null) {
				forUpdate.postInvalidate();
			}
		}
	};	
	public SketchFill() {
		paint.property.value(new Paint());
		paint.property.value().setAntiAlias(true);
		width.is(100);
		height.is(100);
		left.is(0);
		top.is(0);
		background.is(0xff990000);
		//paint.property.value().setColor(background.property.value().intValue());
		width.property.afterChange(postInvalidate);
		height.property.afterChange(postInvalidate);
		top.property.afterChange(postInvalidate);
		background.property.afterChange(new Task(){

			@Override
			public void doTask() {
				paint.property.value().setColor(background.property.value().intValue());
				System.out.println(background.property.value().intValue()+"/"+paint.property.value().getColor());
			}});
		strokeColor.property.afterChange(postInvalidate);
		strokeWidth.property.afterChange(postInvalidate);
		arcX.property.afterChange(postInvalidate);
		arcY.property.afterChange(postInvalidate);
		paint.property.afterChange(postInvalidate);
	}
	public void draw(Canvas canvas) {
		System.out.println("draw "+paint.property.value().getColor());
		int w = width.property.value().intValue();
		int h = height.property.value().intValue();
		int x = left.property.value().intValue();
		int y = top.property.value().intValue();
		//canvas.drawRect(new RectF(x, y, x+w, y+h), paint.property.value());
		canvas.drawRoundRect(new RectF(x, y, x+w, y+h)//
		, arcX.property.value().floatValue()//
		, arcY.property.value().floatValue()//
		, paint.property.value());
	};
}
