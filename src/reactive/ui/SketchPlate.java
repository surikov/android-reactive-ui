package reactive.ui;

import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.StaticLayout;
import android.text.Layout.Alignment;
import tee.binding.it.Toggle;
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
	public Vector<Sketch> sketches = new Vector<Sketch>();
	private Tint paint = new Tint();
	NativeBitmap bitmapCache = new NativeBitmap();
	Rect src = null;
	Rect dest = null;
	Paint bmPaint = new Paint();
	public ToggleProperty<SketchPlate> cached =  new ToggleProperty<SketchPlate>(this);
	Task reFit = new Task() {
		@Override
		public void doTask() {
			cached.property.value( false);
			postInvalidate.start();
		}
	};
	public SketchPlate child(Sketch s) {
		sketches.add(s);
		return this;
	}
	@Override
	public void unbind() {
		/*width.property.unbind();
		height.property.unbind();
		left.property.unbind();
		top.property.unbind();*/
		super.unbind();
		arcX.property.unbind();
		arcY.property.unbind();
		paint.unbind();
		width.property.unbind();
		height.property.unbind();
		left.property.unbind();
		top.property.unbind();
		for (int i = 0; i < sketches.size(); i++) {
			sketches.get(i).unbind();
		}
		if (bitmapCache != null) {
			bitmapCache.freeBitmap();
			bitmapCache = null;
		}
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
		background.is(0x00990000);
		//paint.property.value().setColor(background.property.value().intValue());
		width.property.afterChange(reFit);
		height.property.afterChange(reFit);
		left.property.afterChange(reFit);
		top.property.afterChange(reFit);
		background.property.afterChange(new Task() {
			@Override
			public void doTask() {
				
				paint.setColor(background.property.value().intValue());
				reFit.start();
			}
		});
		//strokeColor.property.afterChange(postInvalidate);
		//strokeWidth.property.afterChange(postInvalidate);
		arcX.property.afterChange(reFit);
		arcY.property.afterChange(reFit);
		//paint.property.afterChange(postInvalidate);
		
	}
	public SketchPlate tint(Tint p) {
		paint = p;
		paint.forUpdate = this;
		reFit.start();
		return this;
	}
	public void reCache(){
		if (cached.property.value()) {
			return;
		}
		cached.property.value(true);
		int w = width.property.value().intValue();
		int h = height.property.value().intValue();
		int l = left.property.value().intValue();
		int t = top.property.value().intValue();
		if (w > 0 && h > 0) {
			if (bitmapCache != null) {
				bitmapCache.freeBitmap();
				bitmapCache = null;
			}
			try{
			Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			src = new Rect(0, 0, w, h);
			dest = new Rect(l, t, l + w, t + h);
			Canvas canvas = new Canvas(bm);
			for (int i = 0; i < sketches.size(); i++) {
				if (sketches.get(i) != null) {
					sketches.get(i).draw(canvas);
				}
			}
			bitmapCache.storeBitmap(bm);
			bm.recycle();
			}catch(Throwable tr){
				tr.printStackTrace();
			}
		}
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
		reCache();
		/*
		for (int i = 0; i < sketches.size(); i++) {
			if (sketches.get(i) != null) {
				sketches.get(i).draw(canvas);
			}
		}
		*/
		if (bitmapCache != null) {
			canvas.drawBitmap(bitmapCache.getBitmap(), src, dest, bmPaint);
		}
	};
}
