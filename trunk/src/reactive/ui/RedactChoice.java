package reactive.ui;

import android.text.*;
import android.app.*;
import tee.binding.properties.*;
import tee.binding.properties.*;
import tee.binding.task.Task;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class RedactChoice extends Button implements Rake {
	public NumericProperty<RedactChoice> selection = new NumericProperty<RedactChoice>(this);
	public NoteProperty<RedactChoice> textLabel = new NoteProperty<RedactChoice>(this);
	private NumericProperty<Rake> width = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> height = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> left = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> top = new NumericProperty<Rake>(this);
	boolean initialized = false;
	//private boolean lock = false;
	private Vector<String> items = new Vector<String>();
	Task reFit = new Task() {
		@Override
		public void doTask() {
			//System.out.
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
					width.property.value().intValue()//
					, height.property.value().intValue());
			params.leftMargin = left.property.value().intValue();
			params.topMargin = top.property.value().intValue();
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			params.alignWithParent = true;
			RedactChoice.this.setLayoutParams(params);
			RedactChoice.this.setWidth(width.property.value().intValue());
			RedactChoice.this.setHeight(height.property.value().intValue());
			RedactChoice.this.setMaxWidth(width.property.value().intValue());
			RedactChoice.this.setMaxHeight(height.property.value().intValue());
			RedactChoice.this.setMinWidth(width.property.value().intValue());
			RedactChoice.this.setMinHeight(height.property.value().intValue());
			//System.out.println("params.topMargin: " + params.topMargin+" / "+Decor.this.getLeft()+"x"+Decor.this.getTop()+"/"+Decor.this.getWidth()+"x"+Decor.this.getHeight());
		}
	};

	public RedactChoice(Context context) {
		super(context);
		init();
	}
	public RedactChoice(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public RedactChoice(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	void init() {
		if (initialized) {
			return;
		}
		initialized = true;
		//format.is("yyyy-MM-dd");
		width.property.afterChange(reFit).value(100);
		height.property.afterChange(reFit).value(100);
		left.property.afterChange(reFit);
		top.property.afterChange(reFit);
		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (items.size() > 0) {
					String[] strings = new String[items.size()];
					for (int i = 0; i < items.size(); i++) {
						strings[i] = items.get(i);
					}
					Auxiliary.pick(RedactChoice.this.getContext(), strings, selection.property);
				}
			}
		});
		selection.property.afterChange(new Task() {
			@Override
			public void doTask() {
				resetLabel();
			}
		});
		//selection.is(-1);
	}
	public RedactChoice item(String s) {
		this.items.add(s);
		resetLabel();
		return this;
	}
	void resetLabel() {
		String s = "";
		int n = selection.property.value().intValue();
		if (n >= 0 && n < items.size()) {
			s = items.get(n);
		}
		setText(s);
	}
	@Override
	public NumericProperty<Rake> left() {
		return left;
	}
	@Override
	public NumericProperty<Rake> top() {
		return top;
	}
	@Override
	public NumericProperty<Rake> width() {
		return width;
	}
	@Override
	public NumericProperty<Rake> height() {
		return height;
	}
	@Override
	public View view() {
		return this;
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		textLabel.property.unbind();
		selection.property.unbind();
		width.property.unbind();
		height.property.unbind();
		left.property.unbind();
		top.property.unbind();
	}
}
