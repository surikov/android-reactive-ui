package reactive.ui;

import android.view.View;
import java.util.Vector;

import reactive.ui.*;
import reactive.ui.library.views.SimpleIcon;
import reactive.ui.library.views.SimpleString;
import reactive.ui.library.views.WhiteBoard;
import reactive.ui.library.views.figures.Figure;
import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;
import tee.binding.task.*;
import tee.binding.it.*;
import android.text.*;
import java.io.*;

public class Picture extends View {
	Context context;
	boolean initialized = false;
	private Paint paint = new Paint();
	//public NumericProperty<Picture> width = new NumericProperty<Picture>(this);
	//public NumericProperty<Picture> height = new NumericProperty<Picture>(this);
	public NumericProperty<Picture> left = new NumericProperty<Picture>(this);
	public NumericProperty<Picture> top = new NumericProperty<Picture>(this);
	public ItProperty<Picture, Bitmap> bitmap = new ItProperty<Picture, Bitmap>(this);
	Bitmap bm;

	public Picture(Context context) {
		super(context);
		init(context);
	}
	public Picture(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public Picture(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	void init(Context c) {
		if (initialized)
			return;
		initialized = true;
		this.context = c;
		paint.setColor(0xff000000);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		bitmap.property.afterChange(new Task() {
			@Override
			public void doTask() {
				//if (icon.property.value()!=null) {
				//resetBitmap();
				//}
				//BitmapFactory.decodeResource(getResources(), reactive.layoutless.demo.R.drawable.lava);
			}
		});
		/*try {
			//bm = BitmapFactory.decodeResource(getResources(), R.drawable.rocket);
			InputStream in = getResources().openRawResource(R.drawable.rocket);
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, o);
			in.close();
			int origWidth = o.outWidth; //исходная ширина 
			int origHeight = o.outHeight; //исходная высота 
			int bytesPerPixel = 2;//соответствует RGB_555 конфигурации
			System.out.println(origWidth+"x"+origHeight+"x"+bytesPerPixel);
			//int maxSize = 480 * 800 * bytesPerPixel; //Максимально разрешенный размер Bitmap
			int scale = 1;
			o = new BitmapFactory.Options();
			o.inSampleSize = scale;
			o.inPreferredConfig = Bitmap.Config.RGB_565;
			            
			in = getResources().openRawResource(R.drawable.rocket); //Ваш InputStream. Важно - открыть его нужно еще раз, т.к второй раз читать из одного и того же InputStream не разрешается (Проверено на ByteArrayInputStream и FileInputStream).
			bm = BitmapFactory.decodeStream(in, null, o); //Полученный Bitmap
			
		}
		catch (Throwable t) {
			t.printStackTrace();
		}*/
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (bitmap.property.value() == null)
			return;
		//canvas.drawRect(new RectF(0, 0, w, h),paint);
		//canvas.save();
		//canvas.scale(1 / density, 1 / density, 0, 0);
		//float sw=bm.getWidth() / w;
		//float sh=bm.getHeight() / h;
		//canvas.drawBitmap(bm, null, new RectF(0, 0, 300, 100), paint);
		canvas.drawBitmap(bitmap.property.value(), left.property.value().floatValue(),top.property.value().floatValue(), paint);
		//canvas.restore();
		//bm.getWidth() / density
		//Tools.log("bm " + sw+"x"+sh);
	}
}
