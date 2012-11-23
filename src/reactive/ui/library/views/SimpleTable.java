package reactive.ui.library.views;

import java.util.*;
import reactive.ui.library.*;
import android.content.*;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.*;
import reactive.ui.library.views.cells.*;
import tee.binding.They;
import android.graphics.*;
import android.widget.*;

public class SimpleTable extends ViewGroup implements Unbind {
	public They<SimpleTableColumn> columns = new They<SimpleTableColumn>();
	int width = 100;
	int height = 100;
	LayoutlessView header;
	LayoutlessView body;
	Paint borderColor;
	float density;

	public SimpleTable(Context context) {
		super(context);
		TextView t = new TextView(context);
		borderColor = new Paint();
		borderColor.setColor(t.getCurrentHintTextColor());
		density = this.getResources().getDisplayMetrics().density;
		header = new LayoutlessView(context);
		header.viewBox(new ViewBox().view.is(new SimpleRectangle(context)//
				.color.is(0xff333300)//
				.gradient.is(0xff330000)//
				.gradientKind.is(SimpleRectangle.GRADIENT_TOPLEFT_TO_RIGHTBOTTOM)//
				)//
		.width.is(header.width)//
		.height.is(header.height)//
		);
		//= new SimpleRectangle(context).color.is(0xffffff00).gradient.is(0xff00ffff).gradientKind.is(SimpleRectangle.GRADIENT_TOPLEFT_TO_RIGHTBOTTOM);		
		this.addView(header);
		body = new LayoutlessView(context);
		//= new SimpleRectangle(context).color.is(0xff00ff00).gradient.is(0xff0000ff).gradientKind.is(SimpleRectangle.GRADIENT_TOPLEFT_TO_RIGHTBOTTOM);
		this.addView(body);
		body.viewBox(new ViewBox().view.is(new SimpleRectangle(context)//
				.color.is(0xff00ff00)//
				.gradient.is(0xff0000ff)//
				.gradientKind.is(SimpleRectangle.GRADIENT_TOPLEFT_TO_RIGHTBOTTOM)//
				)//
		.width.is(body.width)//
		.height.is(body.height)//
		);
	}
	void cleanup() {
	}
	public void requery() {
		int sz = columns.size();
		int x = 0;
		for (int i = 0; i < sz; i++) {
			//System.out.println(columns.at(i).title.property.value());
			header.viewBox(new ViewBox().view.is(new SimpleRectangle(this.getContext())//
					.color.is(0xff333300)//
					.gradient.is(0xff660000)//
					.gradientKind.is(SimpleRectangle.GRADIENT_TOPLEFT_TO_RIGHTBOTTOM))//
			.width.is(columns.at(i).width.property)//
			.height.is(60 * density)//
			.left.is(header.shiftX.property.plus(x))//
			);
			SimpleString ss = new SimpleString(this.getContext()).text.is(columns.at(i).title.property);
			//ss.setGravity(Gravity.CENTER);
			//ss.setText("123");
			header.viewBox(new ViewBox().view.is(ss)//
			.width.is(columns.at(i).width.property)//
			.height.is(60 * density)//
			.left.is(header.shiftX.property.plus(x))//
			);
			x = x + columns.at(i).width.property.value().intValue();
			//System.out.println(ss.getHeight());
			//ss.setGravity(Gravity.CENTER);
		}
		header.innerWidth.is(x);
		this.postInvalidate();
	}
	@Override
	public void unbind() {
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			width = r - l;
			height = b - t;
			header.layout(0, 0, width, (int) (60 * density));
			header.width.value(width);
			header.height.value(60 * density);
			//System.out.println(width+"x"+60 * density);
			body.layout(0, (int) (60 * density), width, height);
			body.width.value(width);
			body.height.value(height - 60 * density);
		}
	}
}
