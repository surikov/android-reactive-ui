package reactive.ui.library.views;

import reactive.ui.library.*;
import reactive.ui.library.views.cells.*;
import reactive.ui.library.views.figures.*;
import android.view.*;
import android.content.*;
import tee.binding.properties.*;
import android.widget.*;
import tee.binding.task.*;
import tee.binding.it.*;

public class SimpleButton extends Button implements Unbind {
	public NoteProperty<SimpleButton> text = new NoteProperty<SimpleButton>(this);
	public ItProperty<SimpleButton, Task> tap = new ItProperty<SimpleButton, Task>(this);
	SimpleButton me = this;

	/*@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Tools.log("SimpleButton " + widthMeasureSpec + "x" + heightMeasureSpec);
	}*/
	/*protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		//Tools.log("SimpleButton onLayout " + changed);
		//this.layout(l, t, r, b)
	}*/
	@Override
	public void unbind() {				
	}
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if(w!=oldw || h !=oldh)
		this.setText(text.property.value());
		//ViewGroup.LayoutParams newLayout = new LinearLayout.LayoutParams(w, h);
		//setLayoutParams(newLayout);
		//this.onLayout(changed, left, top, right, bottom)
		//this.requestLayout();
		//this.postInvalidate();
		//if (this.getParent() != null)
			//Tools.log("SimpleButton onSizeChanged " + w + "x" + h + " / " + this.getParent().isLayoutRequested());
	}

	public SimpleButton(Context c) {
		super(c);
		//Tools.log("SimpleButton ");
		text.property.afterChange(new Task() {
			@Override
			public void doTask() {
				me.setText(text.property.value());
				me.invalidate();
			}
		});
		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tap.property.value() != null) {
					tap.property.value().doTask();
				}
			}
		});
	}
}
