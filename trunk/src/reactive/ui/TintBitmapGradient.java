package reactive.ui;

import reactive.ui.*;

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

public class TintBitmapGradient extends Tint {
	public ItProperty<TintBitmapGradient, Bitmap> bitmap = new ItProperty<TintBitmapGradient, Bitmap>(this);//Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rocket),200,100,true);

	public TintBitmapGradient() {
		super();
		setAntiAlias(true);
		bitmap.property.afterChange(postInvalidate);
	}
	@Override
	public void unbind() {
		bitmap.property.unbind();
	}

	public Task postInvalidate = new Task() {
		@Override
		public void doTask() {
			if (bitmap.property.value() != null) {
				setShader(new BitmapShader(bitmap.property.value(), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
				if (forUpdate != null) {
					if (forUpdate.postInvalidate != null) {
						forUpdate.postInvalidate.start();
					}
				}
			}
		}
	};
}
