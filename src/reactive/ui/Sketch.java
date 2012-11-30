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
	public Fit forUpdate;
	abstract public void draw(Canvas canvas);/* {
		int w = 100;
		int h = 100;
		Paint paint = new Paint();
		paint.setColor(0x99ff0000);
		canvas.drawRect(new RectF( 0,  0,  w,  h), paint);
	};*/
}
