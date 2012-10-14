package reactive.ui.library.views;

import java.util.Vector;

import reactive.ui.library.*;
import reactive.ui.library.views.cells.*;
import reactive.ui.library.views.figures.*;

import android.content.*;
import android.graphics.*;
import android.util.FloatMath;
import android.view.*;
import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.view.*;
import tee.binding.task.*;
import tee.binding.it.*;

public class TouchHandler extends View implements Unbind {
	public final static int tapDiff = 8;
	public final static int NONE = 0;
	public final static int DRAG = 1;
	public final static int ZOOM = 2;
	public int dragMode = NONE;
	private float lastEventX = 0;
	private float lastEventY = 0;
	private float initialShiftX = 0;
	private float initialShiftY = 0;
	private float initialSpacing;
	private float currentSpacing;
	private TouchHandler me = this;
	public static Numeric width = new Numeric().value(320);
	public static Numeric height = new Numeric().value(240);
	public NumericProperty<TouchHandler> shiftX = new NumericProperty<TouchHandler>(this);
	public NumericProperty<TouchHandler> shiftY = new NumericProperty<TouchHandler>(this);
	public ToggleProperty<TouchHandler> pressed = new ToggleProperty<TouchHandler>(this);
	public NumericProperty<TouchHandler> pressX = new NumericProperty<TouchHandler>(this);
	public NumericProperty<TouchHandler> pressY = new NumericProperty<TouchHandler>(this);
	
	public NumericProperty<TouchHandler> tapX = new NumericProperty<TouchHandler>(this);
	public NumericProperty<TouchHandler> tapY = new NumericProperty<TouchHandler>(this);
	public ItProperty<TouchHandler, Task> tap = new ItProperty<TouchHandler, Task>(this);
	public ItProperty<TouchHandler, Task> drop = new ItProperty<TouchHandler, Task>(this);
	public NumericProperty<TouchHandler> zoom = new NumericProperty<TouchHandler>(this);
	public NumericProperty<TouchHandler> maxZoom = new NumericProperty<TouchHandler>(this);
	public NumericProperty<TouchHandler> minShiftX = new NumericProperty<TouchHandler>(this);
	public NumericProperty<TouchHandler> maxShiftX = new NumericProperty<TouchHandler>(this);
	public NumericProperty<TouchHandler> minShiftY = new NumericProperty<TouchHandler>(this);
	public NumericProperty<TouchHandler> maxShiftY = new NumericProperty<TouchHandler>(this);
	public ItProperty<TouchHandler, Bitmap> bitmap = new ItProperty<TouchHandler, Bitmap>(this);
	private int lastLeft = 0;
	private int lastTop = 0;
	private int differenceLeft = 0;
	private int differenceTop = 0;
	private Paint paint = new Paint();
	private int w = 100;
	private int h = 100;
	Vector<Figure> figures = new Vector<Figure>();
	Bitmap bm = null;

	public TouchHandler(Context context) {
		super(context);
		paint.setColor(0xff000000);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);

		//bm = BitmapFactory.decodeResource(getResources(), reactive.layoutless.demo.R.drawable.lava);

		bitmap.property.afterChange(new Task() {
			@Override
			public void doTask() {
				//if (icon.property.value()!=null) {
				resetBitmap();
				//}
			}
		});
	}

	protected void resetBitmap() {
		if (bitmap.property.value() != null) {
			bm = Bitmap.createScaledBitmap(bitmap.property.value(), w, h, true);
			me.postInvalidate();
		}

	}

	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int nw = right - left;
		int nh = bottom - top;
		if (nw != w || nh != h) {
			w = nw;
			h = nh;
			resetBitmap();
		}
	}

	float spacing(float x0, float y0, float x1, float y1) {
		float x = x0 - x1;
		float y = y0 - y1;
		return FloatMath.sqrt(x * x + y * y);
	}

	void startDrag(float x, float y) {
		pressX.is(x);
		pressY.is(y);
		pressed.is(true);
		lastLeft = this.getLeft();
		lastTop = this.getTop();
		initialShiftX = shiftX.property.value().floatValue();
		initialShiftY = shiftY.property.value().floatValue();
		lastEventX = x;
		lastEventY = y;
		dragMode = DRAG;
	}

	void setShift(float x, float y) {
		differenceLeft = lastLeft - this.getLeft();
		differenceTop = lastTop - this.getTop();
		lastLeft = this.getLeft();
		lastTop = this.getTop();
		double newShiftX = shiftX.property.value() + x - lastEventX - differenceLeft;
		double newShiftY = shiftY.property.value() + y - lastEventY - differenceTop;
		if (newShiftX < minShiftX.property.value()) {
			newShiftX = minShiftX.property.value();
		}
		if (newShiftX > maxShiftX.property.value()) {
			newShiftX = maxShiftX.property.value();
		}
		if (newShiftY < minShiftY.property.value()) {
			newShiftY = minShiftY.property.value();
		}
		if (newShiftY > maxShiftY.property.value()) {
			newShiftY = maxShiftY.property.value();
		}
		shiftX.property.value(newShiftX);
		shiftY.property.value(newShiftY);
	}

	void proceedDrag(float x, float y) {
		setShift(x, y);
		lastEventX = x;
		lastEventY = y;
		tapX.property.value((double) x);
		tapY.property.value((double) y);
	}

	void finishDrag(float x, float y) {
		setShift(x, y);
		if (Math.abs(initialShiftX - shiftX.property.value()) < tapDiff// 
				&& Math.abs(initialShiftY - shiftY.property.value()) < tapDiff) {
			finishTap(x, y);
		}
		else {
			tapX.property.value((double) x);
			tapY.property.value((double) y);
			if (drop.property.value() != null) {
				drop.property.value().start();
			}
		}
		dragMode = NONE;
		pressed.is(false);
	}

	void finishTap(float x, float y) {
		shiftX.property.value((double) initialShiftX);
		shiftY.property.value((double) initialShiftY);
		tapX.property.value((double) x);
		tapY.property.value((double) y);
		if (tap.property.value() != null) {
			tap.property.value().start();
		}
	}

	void startZoom(float x0, float y0, float x1, float y1) {
		initialSpacing = spacing(x0, y0, x1, y1);
		currentSpacing = initialSpacing;
		dragMode = ZOOM;
	}

	void proceedZoom(float x0, float y0, float x1, float y1) {
		currentSpacing = spacing(x0, y0, x1, y1);

	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (bm != null) {
			canvas.drawBitmap(bm, 0, 0, paint);
		}
		for (int i = 0; i < figures.size(); i++) {
			figures.get(i).draw(canvas);
		}
	}

	void finishZoom() {//float x0, float y0, float x1, float y1) {
		//currentSpacing = spacing(x0, y0, x1, y1);
		if (currentSpacing > initialSpacing) {
			if (zoom.property.value() < maxZoom.property.value()) {
				zoom.is(zoom.property.value() + 1);
			}
		}
		else {
			if (zoom.property.value() > 0) {
				zoom.is(zoom.property.value() - 1);
			}
		}
		dragMode = NONE;
		pressed.is(false);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Tools.log("event "+event);
		if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
			startDrag(event.getX(), event.getY());
		}
		else {
			if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
				if (event.getPointerCount() > 1) {
					if (dragMode == ZOOM) {
						proceedZoom(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
					}
					else {
						startZoom(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
					}
				}
				else {
					proceedDrag(event.getX(), event.getY());
				}
			}
			else {
				if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
					if (dragMode == DRAG) {
						finishDrag(event.getX(), event.getY());
					}
					else {
						if (dragMode == ZOOM) {
							finishZoom();//event.getX(0), event.getY(0), event.getX(1), event.getY(1));
						}
						else {
							//ignore event
						}
					}
				}
				else {
					//ignore event
				}
			}
		}
		return true;
	}

	@Override
	public void unbind() {
	}

	public TouchHandler figure(Figure f) {
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
}
