package reactive.ui;

import tee.binding.properties.*;
import tee.binding.task.*;
import android.graphics.*;

public class SketchLine extends Sketch {
	public NumericProperty<SketchLine> strokeColor = new NumericProperty<SketchLine>(this);
	public NumericProperty<SketchLine> strokeWidth = new NumericProperty<SketchLine>(this);
	public NumericProperty<SketchLine> startX = new NumericProperty<SketchLine>(this);
	public NumericProperty<SketchLine> startY = new NumericProperty<SketchLine>(this);
	public NumericProperty<SketchLine> endX = new NumericProperty<SketchLine>(this);
	public NumericProperty<SketchLine> endY = new NumericProperty<SketchLine>(this);
	private Paint paint = new Paint();

	public SketchLine() {
		// paint.setAntiAlias(true);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStyle(Paint.Style.STROKE);
		strokeColor.property.afterChange(new Task() {
			@Override
			public void doTask() {
				// TODO Auto-generated method stub
				paint.setColor(strokeColor.property.value().intValue());
				postInvalidate.start();
			}
		});
		strokeWidth.property.afterChange(new Task() {
			@Override
			public void doTask() {
				// TODO Auto-generated method stub
				paint.setStrokeWidth(strokeWidth.property.value().floatValue());
				postInvalidate.start();
			}
		});
		startX.property.afterChange(postInvalidate);
		startY.property.afterChange(postInvalidate);
		endX.property.afterChange(postInvalidate);
		endY.property.afterChange(postInvalidate);
	}
	@Override
	public void draw(Canvas canvas) {
		canvas.drawLine(//
				startX.property.value().floatValue()//
				,  startY.property.value().floatValue()//
				,  endX.property.value().floatValue()//
				,  endY.property.value().floatValue()//
				, paint//
		);
	}
}
