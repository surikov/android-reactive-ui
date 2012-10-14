package reactive.ui.library.views;

import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.view.*;
import tee.binding.task.*;
import tee.binding.it.*;
import java.util.*;

import reactive.ui.library.*;
import reactive.ui.library.views.cells.*;
import reactive.ui.library.views.figures.*;


public class WhiteBoard extends View  implements Unbind{
	/*private Paint paint = new Paint();
	public ToggleProperty<WhiteBoard> down = new ToggleProperty<WhiteBoard>(this);
	public NumericProperty<WhiteBoard> thickness = new NumericProperty<WhiteBoard>(this);
	public NumericProperty<WhiteBoard> color = new NumericProperty<WhiteBoard>(this);*/
	//private int w = 100;
	//private int h = 100;
	WhiteBoard me = this;
	Vector<Figure> figures = new Vector<Figure>();
	Task postInvalidate=new Task(){

		@Override
		public void doTask() {
			postInvalidate();
			
		}};
	public NumericProperty<WhiteBoard> shiftX = new NumericProperty<WhiteBoard>(this);
	public NumericProperty<WhiteBoard> shiftY = new NumericProperty<WhiteBoard>(this);
	public WhiteBoard(Context context) {
		super(context);
		shiftX.property.afterChange(postInvalidate);
		shiftY.property.afterChange(postInvalidate);
		/*paint.setColor(0x99ff0000);
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
		});*/
	}

	@Override
	public void unbind() {
		//super.onDetachedFromWindow();
		//Tools.log("onDetachedFromWindow");
		for (int i = 0; i < figures.size(); i++) {
			figures.get(i).cleanUp();
		}
	}

	public WhiteBoard figure(Figure f) {
		this.figures.add(f);
		this.postInvalidate();
		return this;
	}

	public void drop(Figure f) {
		this.figures.remove(f);
		this.postInvalidate();
	}

	public void clear() {
		this.figures.removeAllElements();
		this.postInvalidate();
	}

	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		//w = right - left;
		//h = bottom - top;
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/*float t = (float) (thickness.property.value() / 2);
		if (down.property.value()) {
			canvas.drawLine(t, t, w - t, h - t, paint);
		}
		else {
			canvas.drawLine(t, h - t, w - t, t, paint);
		}*/
		for (int i = 0; i < figures.size(); i++) {
			figures.get(i).draw(canvas);
		}
	}
	
}
