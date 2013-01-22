package reactive.ui;

import android.text.*;
import tee.binding.properties.*;
import tee.binding.task.Task;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;

public class RedactNumber extends EditText implements Rake {
	public NumericProperty<RedactNumber> number = new NumericProperty<RedactNumber>(this);
	private NumericProperty<Rake> width = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> height = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> left = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> top = new NumericProperty<Rake>(this);
	boolean initialized = false;
	private boolean lock = false;
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
			RedactNumber.this.setLayoutParams(params);
			RedactNumber.this.setWidth(width.property.value().intValue());
			RedactNumber.this.setHeight(height.property.value().intValue());
			RedactNumber.this.setMaxWidth(width.property.value().intValue());
			RedactNumber.this.setMaxHeight(height.property.value().intValue());
			RedactNumber.this.setMinWidth(width.property.value().intValue());
			RedactNumber.this.setMinHeight(height.property.value().intValue());
			//System.out.println("params.topMargin: " + params.topMargin+" / "+Decor.this.getLeft()+"x"+Decor.this.getTop()+"/"+Decor.this.getWidth()+"x"+Decor.this.getHeight());
		}
	};

	public RedactNumber(Context context) {
		super(context);
		init();
	}
	public RedactNumber(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public RedactNumber(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	void init() {
		if (initialized) {
			return;
		}
		initialized = true;
		width.property.afterChange(reFit).value(100);
		height.property.afterChange(reFit).value(100);
		left.property.afterChange(reFit);
		top.property.afterChange(reFit);
		setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if (!lock) {
					lock = true;
					try {
						number.property.value(Double.parseDouble(s.toString()));
					}
					catch (Throwable t) {
						t.printStackTrace();
					}
					lock = false;
				}
			}
		});
		number.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (!lock) {
					lock = true;
					setText("" + number.property.value());
					lock = false;
				}
			}
		});
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
		number.property.unbind();
		width.property.unbind();
		height.property.unbind();
		left.property.unbind();
		top.property.unbind();
	}
}