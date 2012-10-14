package reactive.ui.library.views.figures;

import reactive.ui.library.views.WhiteBoard;
import android.graphics.*;
import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.view.*;
import tee.binding.task.*;
import tee.binding.it.*;

public abstract class Figure {
	protected View watcher;
	protected WhiteBoard whiteboard;
	protected Task postInvalidate = new Task() {

		@Override
		public void doTask() {
			if (watcher != null) {
				watcher.postInvalidate();
			}
		}
	};

	public Figure(View forPostInvalidate, WhiteBoard forShift) {
		watcher = forPostInvalidate;
		whiteboard = forShift;
	}

	public void cleanUp() {
	}

	public int getShiftX() {
		if (this.whiteboard != null) {
			return this.whiteboard.shiftX.property.value().intValue();
		}
		else {
			return 0;
		}
	}

	public int getShiftY() {
		if (this.whiteboard != null) {
			return this.whiteboard.shiftY.property.value().intValue();
		}
		else {
			return 0;
		}
	}

	public void draw(Canvas canvas) {
		int w = 100;
		int h = 100;
		Paint paint = new Paint();
		paint.setColor(0x99ff0000);
		canvas.drawRect(new RectF(getShiftX() + 0, getShiftY() + 0, getShiftX() + w, getShiftY() + h), paint);
	};
}
