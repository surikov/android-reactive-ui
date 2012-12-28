package reactive.ui;

import tee.binding.properties.NoteProperty;
import tee.binding.properties.NumericProperty;
import tee.binding.task.Task;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;

public class RedactText extends EditText implements Rake {
	public NoteProperty<RedactText> text = new NoteProperty<RedactText>(this);
	private NumericProperty<Rake> width = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> height = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> left = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> top = new NumericProperty<Rake>(this);
	boolean initialized = false;
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
			params.alignWithParent=true;
			
			
			RedactText.this.setLayoutParams(params);
			
			
			RedactText.this.setWidth(width.property.value().intValue());
			RedactText.this.setHeight(height.property.value().intValue());
			RedactText.this.setMaxWidth(width.property.value().intValue());
			RedactText.this.setMaxHeight(height.property.value().intValue());
			RedactText.this.setMinWidth(width.property.value().intValue());
			RedactText.this.setMinHeight(height.property.value().intValue());
			
			//System.out.println("params.topMargin: " + params.topMargin+" / "+Decor.this.getLeft()+"x"+Decor.this.getTop()+"/"+Decor.this.getWidth()+"x"+Decor.this.getHeight());
		}
	};

	public RedactText(Context context) {
		super(context);
		init();
	}
	public RedactText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public RedactText(Context context, AttributeSet attrs, int defStyle) {
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
		this.setText("");
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
		text.property.unbind();
		width.property.unbind();
		height.property.unbind();
		left.property.unbind();
		top.property.unbind();
	}
}
