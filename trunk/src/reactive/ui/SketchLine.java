package reactive.ui;

import tee.binding.properties.*;
import tee.binding.task.*;
import android.graphics.*;
import java.util.*;

public class SketchLine extends Sketch {
	public NumericProperty<SketchLine> strokeColor = new NumericProperty<SketchLine>(this);
	public NumericProperty<SketchLine> strokeWidth = new NumericProperty<SketchLine>(this);
	/*public NumericProperty<SketchLine> startX = new NumericProperty<SketchLine>(this);
	public NumericProperty<SketchLine> startY = new NumericProperty<SketchLine>(this);
	public NumericProperty<SketchLine> endX = new NumericProperty<SketchLine>(this);
	public NumericProperty<SketchLine> endY = new NumericProperty<SketchLine>(this);*/
	private Paint paint = new Paint();
	private Path path = new Path();
	private boolean first = true;

	//private Vector<Integer>xx=new Vector<Integer>();
	//private Vector<Integer>yy=new Vector<Integer>();
	@Override
	public void unbind() {
		super.unbind();
		strokeColor.property.unbind();
		strokeWidth.property.unbind();
	}
	public SketchLine point(double x, double y) {
		if (first) {
			path.moveTo((float) x, (float) y);
			first = false;
			//System.out.println("moveTo "+x+"x"+y);
		}
		else {
			path.lineTo((float) x, (float) y);
			postInvalidate.start();
			//System.out.println("lineTo "+x+"x"+y);
		}
		//xx.add(x);
		//yy.add(y);
		return this;
	}
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
		strokeWidth.is(3);
		strokeColor.is(0xffff0000);
		/*startX.property.afterChange(postInvalidate);
		startY.property.afterChange(postInvalidate);
		endX.property.afterChange(postInvalidate);
		endY.property.afterChange(postInvalidate);*/
	}
	@Override
	public void draw(Canvas canvas) {
		/*canvas.drawLine(//
				startX.property.value().floatValue()//
				, startY.property.value().floatValue()//
				, endX.property.value().floatValue()//
				, endY.property.value().floatValue()//
				, paint//
		);*/
		canvas.drawPath(path, paint);
	}
}
