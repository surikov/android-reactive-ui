package reactive.ui.library.views;

import reactive.ui.library.*;
import reactive.ui.library.views.cells.*;
import reactive.ui.library.views.figures.*;
import android.R;
import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.view.*;
import tee.binding.task.*;
import tee.binding.it.*;

public class SimpleIcon extends View implements Unbind {

	private Paint paint = new Paint();
	private int w = 100;
	private int h = 100;
	SimpleIcon me = this;
	Bitmap bm = null;
	//float density;
	public ItProperty<SimpleIcon, Bitmap> bitmap = new ItProperty<SimpleIcon, Bitmap>(this);

	public SimpleIcon(Context context) {
		super(context);
		//density = context.getResources().getDisplayMetrics().density;
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
			if (w > 0 && h > 0) {
				bm = Bitmap.createScaledBitmap(bitmap.property.value(), w, h, true);
				me.postInvalidate();
			}
		}
	}

	@Override
	public void unbind() {
		//super.onDetachedFromWindow();
		//Tools.log("SimpleIcon onDetachedFromWindow");

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

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (bm == null)
			return;
		//canvas.drawRect(new RectF(0, 0, w, h),paint);
		//canvas.save();
		//canvas.scale(1 / density, 1 / density, 0, 0);
		//float sw=bm.getWidth() / w;
		//float sh=bm.getHeight() / h;
		canvas.drawBitmap(bm, 0, 0, paint);
		//canvas.restore();
		//bm.getWidth() / density
		//Tools.log("bm " + sw+"x"+sh);
	}
}
