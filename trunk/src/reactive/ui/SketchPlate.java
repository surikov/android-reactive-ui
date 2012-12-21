package reactive.ui;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import tee.binding.properties.*;
import tee.binding.task.Task;

public class SketchPlate extends Sketch {
	public NumericProperty<SketchPlate> width = new NumericProperty<SketchPlate>(this);
	public NumericProperty<SketchPlate> height = new NumericProperty<SketchPlate>(this);
	public NumericProperty<SketchPlate> left = new NumericProperty<SketchPlate>(this);
	public NumericProperty<SketchPlate> top = new NumericProperty<SketchPlate>(this);
	public NumericProperty<SketchPlate> background = new NumericProperty<SketchPlate>(this);
	//public NumericProperty<SketchPlate> strokeColor = new NumericProperty<SketchPlate>(this);
	//public NumericProperty<SketchPlate> strokeWidth = new NumericProperty<SketchPlate>(this);
	public NumericProperty<SketchPlate> arcX = new NumericProperty<SketchPlate>(this);
	public NumericProperty<SketchPlate> arcY = new NumericProperty<SketchPlate>(this);
	
	private Tint paint = new Tint();
	
	@Override
	 public void unbind(){
		width.property.unbind();
		height.property.unbind();
		left.property.unbind();
		top.property.unbind();
		
		arcX.property.unbind();
		arcY.property.unbind();
		paint.unbind();
	 }
	//Paint paint = new Paint();
	public SketchPlate() {
		//paint.property.value(new Paint());
		paint.setAntiAlias(true);
		//paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.ROUND);
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
				// TODO Auto-generated method stub
				paint.setColor(background.property.value().intValue());
				postInvalidate.start();
			}});
		//strokeColor.property.afterChange(postInvalidate);
		//strokeWidth.property.afterChange(postInvalidate);
		arcX.property.afterChange(postInvalidate);
		arcY.property.afterChange(postInvalidate);
		//paint.property.afterChange(postInvalidate);
	}
	public SketchPlate tint(Tint p) {
		paint = p;
		paint.forUpdate = this;
		postInvalidate.start();
		return this;
	}
	public void draw(Canvas canvas) {
		//System.out.println("draw "+paint.property.value().getColor());
		int w = width.property.value().intValue();
		int h = height.property.value().intValue();
		int x = left.property.value().intValue();
		int y = top.property.value().intValue();
		//canvas.drawRect(new RectF(x, y, x+w, y+h), paint.property.value());
		//canvas.dr
		//paint.setStrokeWidth(strokeWidth.property.value().floatValue());
		//paint.setStyle(Paint.Style.FILL);
		//paint.setColor(background.property.value().intValue());
		canvas.drawRoundRect(new RectF(x, y, x + w, y + h)//
				, arcX.property.value().floatValue()//
				, arcY.property.value().floatValue()//
				, paint);
		
	};
}
