package reactive.ui;

import android.graphics.*;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.*;
import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.view.*;
import tee.binding.task.*;
import tee.binding.it.*;


public class Sketch {
	public void draw(Canvas canvas) {
		int w = 100;
		int h = 100;
		Paint paint = new Paint();
		paint.setColor(0x99ff0000);
		canvas.drawRect(new RectF( 0,  0,  w,  h), paint);
	};
}
