package reactive.ui;

import java.util.Vector;

import android.graphics.*;
import android.graphics.*;
import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.view.*;
import tee.binding.task.*;
import tee.binding.it.*;


abstract public class Sketch {
	//Vector<Sketch> figures = new Vector<Sketch>();
	public Decor forUpdate;
	public Task postInvalidate = new Task() {
		@Override
		public void doTask() {
			if (forUpdate != null) {
				forUpdate.postInvalidate();
			}
		}
	};	
	abstract public void unbind();
	abstract public void draw(Canvas canvas);/* {
		int w = 100;
		int h = 100;
		Paint paint = new Paint();
		paint.setColor(0x99ff0000);
		canvas.drawRect(new RectF( 0,  0,  w,  h), paint);
	};*/
}
