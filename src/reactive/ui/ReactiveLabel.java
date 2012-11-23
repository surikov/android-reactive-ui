package reactive.ui;

import reactive.ui.*;
import reactive.ui.library.views.SimpleString;
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

public class ReactiveLabel extends TextView {
	public NoteProperty<ReactiveLabel> text = new NoteProperty<ReactiveLabel>(this);
	public NumericProperty<ReactiveLabel> width = new NumericProperty<ReactiveLabel>(this);
	public NumericProperty<ReactiveLabel> height = new NumericProperty<ReactiveLabel>(this);
	public NumericProperty<ReactiveLabel> left = new NumericProperty<ReactiveLabel>(this);
	public NumericProperty<ReactiveLabel> top = new NumericProperty<ReactiveLabel>(this);
	public NumericProperty<ReactiveLabel> gravity = new NumericProperty<ReactiveLabel>(this);
	public NumericProperty<ReactiveLabel> foreground = new NumericProperty<ReactiveLabel>(this);
	public NumericProperty<ReactiveLabel> background = new NumericProperty<ReactiveLabel>(this);

	public ReactiveLabel(Context context) {
		super(context);
		init();
	}
	public ReactiveLabel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public ReactiveLabel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	void init() {
		text.property.afterChange(new Task() {
			@Override
			public void doTask() {
				setText(text.property.value());
			}
		});
		gravity.property.afterChange(new Task() {
			@Override
			public void doTask() {
				setGravity(gravity.property.value().intValue());
			}
		});
		foreground.is(this.getCurrentTextColor());
		foreground.property.afterChange(new Task() {
			@Override
			public void doTask() {
				setTextColor(foreground.property.value().intValue());
			}
		});
		background.property.afterChange(new Task() {
			@Override
			public void doTask() {
				setBackgroundColor(background.property.value().intValue());
			}
		});
		Task reLayout = new Task() {
			@Override
			public void doTask() {
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
						width.property.value().intValue()//
						, height.property.value().intValue());
				params.leftMargin = left.property.value().intValue();
				params.topMargin = top.property.value().intValue();
				ReactiveLabel.this.setLayoutParams(params);
			}
		};
		width.property.afterChange(reLayout).value(100);
		height.property.afterChange(reLayout).value(100);
		left.property.afterChange(reLayout);
		top.property.afterChange(reLayout);
	}
}
