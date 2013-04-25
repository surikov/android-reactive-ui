package reactive.ui;

import tee.binding.it.Numeric;
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
	private Vector<Numeric> xs = new Vector<Numeric>();
	private Vector<Numeric> ys = new Vector<Numeric>();

	//private boolean first = true;
	//private Vector<Integer>xx=new Vector<Integer>();
	//private Vector<Integer>yy=new Vector<Integer>();
	@Override
	public void unbind() {
		super.unbind();
		strokeColor.property.unbind();
		strokeWidth.property.unbind();
		for (int i = 0; i < xs.size(); i++) {
			xs.get(i).unbind();
		}
		xs.removeAllElements();
		for (int i = 0; i < ys.size(); i++) {
			ys.get(i).unbind();
		}
		ys.removeAllElements();
	}
	void resetPath() {
		path = new Path();
		if (xs.size() > 0) {
			path.moveTo(xs.get(0).value().floatValue(), ys.get(0).value().floatValue());
			for (int i = 0; i < xs.size(); i++) {
				path.lineTo(xs.get(i).value().floatValue(), ys.get(i).value().floatValue());
			}
		}
	}
	public SketchLine point(double x, double y) {
		return point(new Numeric().value(x), new Numeric().value(y));
	}
	public SketchLine point(Numeric x, Numeric y) {
		xs.add(new Numeric().bind(x).afterChange(new Task() {
			@Override
			public void doTask() {
				resetPath();
				postInvalidate.start();
			}
		}, true));
		ys.add(new Numeric().bind(y).afterChange(new Task() {
			@Override
			public void doTask() {
				resetPath();
				postInvalidate.start();
			}
		}, true));
		resetPath();
		postInvalidate.start();
		/*
		if (first) {
		path.moveTo((float) x, (float) y);
		first = false;
		//System.out.println("moveTo "+x+"x"+y);
		}
		else {
		path.lineTo((float) x, (float) y);
		postInvalidate.start();
		//System.out.println("lineTo "+x+"x"+y);
		}*/
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
