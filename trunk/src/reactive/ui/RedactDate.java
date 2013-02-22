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

public class RedactDate extends Button implements Rake {
	private ToggleProperty<Rake> hidden = new ToggleProperty<Rake>(this);
	public NumericProperty<RedactDate> date = new NumericProperty<RedactDate>(this);
	public NoteProperty<RedactDate> format = new NoteProperty<RedactDate>(this);//dd.MM.yyyy, yyyy-MM-dd
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
			RedactDate.this.setLayoutParams(params);
			RedactDate.this.setWidth(width.property.value().intValue());
			RedactDate.this.setHeight(height.property.value().intValue());
			RedactDate.this.setMaxWidth(width.property.value().intValue());
			RedactDate.this.setMaxHeight(height.property.value().intValue());
			RedactDate.this.setMinWidth(width.property.value().intValue());
			RedactDate.this.setMinHeight(height.property.value().intValue());
			//System.out.println("params.topMargin: " + params.topMargin+" / "+Decor.this.getLeft()+"x"+Decor.this.getTop()+"/"+Decor.this.getWidth()+"x"+Decor.this.getHeight());
		}
	};

	public RedactDate(Context context) {
		super(context);
		init();
	}
	public RedactDate(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public RedactDate(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	void init() {
		if (initialized) {
			return;
		}
		initialized = true;
		format.is("yyyy-MM-dd");
		width.property.afterChange(reFit).value(100);
		height.property.afterChange(reFit).value(100);
		left.property.afterChange(reFit);
		top.property.afterChange(reFit);
		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(date.property.value().longValue());
				c.set(Calendar.HOUR, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);
				//java.util.Date d=new java.util.Date ();
				//d.setTime(date.property.value().longValue());
				//System.out.println("was " + c+" / "+c.get(Calendar.YEAR)+"-"+ c.get(Calendar.MONTH)+"-"+ c.get(Calendar.DAY_OF_MONTH));
				new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						//System.out.println(year+"/"+monthOfYear+"/"+dayOfMonth);
						//java.util.Date d = new java.util.Date(year-1900, monthOfYear, dayOfMonth);
						//System.out.println(d+" / " + d.getTime());
						Calendar c = Calendar.getInstance();
						c.set(Calendar.YEAR, year);
						c.set(Calendar.MONTH, monthOfYear);
						c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						c.set(Calendar.HOUR, 0);
						c.set(Calendar.MINUTE, 0);
						c.set(Calendar.SECOND, 0);
						c.set(Calendar.MILLISECOND, 0);
						date.property.value((double) c.getTimeInMillis());
						//System.out.println(date.property.value());
						//d.setTime(date.property.value().longValue());
						//System.out.println("choosen " + c);
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		date.property.afterChange(new Task() {
			@Override
			public void doTask() {
				resetLabel();
			}
		});
		format.property.afterChange(new Task() {
			@Override
			public void doTask() {
				resetLabel();
			}
		});
		hidden.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (hidden.property.value()) {
					setVisibility(View.INVISIBLE);
				}
				else {
					setVisibility(View.VISIBLE);
				}
			}
		});
	}
	@Override
	public ToggleProperty<Rake> hidden() {
		return hidden;
	}
	void resetLabel() {
		DateFormat to = new SimpleDateFormat(format.property.value());
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date.property.value().longValue());
		setText(to.format(c.getTime()));
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
		date.property.unbind();
		format.property.unbind();
		width.property.unbind();
		height.property.unbind();
		left.property.unbind();
		top.property.unbind();
	}
}
