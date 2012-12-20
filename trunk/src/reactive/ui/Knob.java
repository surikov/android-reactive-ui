package reactive.ui;

import tee.binding.properties.*;
import android.graphics.*;

import android.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.*;
import reactive.ui.*;
import reactive.ui.library.views.SimpleButton;

import android.view.animation.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;

import java.io.*;
import java.text.*;

public class Knob extends Button  implements Rake {
	public NoteProperty<Knob> labelText = new NoteProperty<Knob>(this);
	private NumericProperty<Rake> width = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> height = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> left = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> top = new NumericProperty<Rake>(this);
	public ItProperty<Knob, Task> tap = new ItProperty<Knob, Task>(this);
	boolean initialized = false;
	//Context context;
	Task reFit = new Task() {
		@Override
		public void doTask() {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
					width.property.value().intValue()//
					, height.property.value().intValue());
			params.leftMargin = left.property.value().intValue();
			params.topMargin = top.property.value().intValue();
			Knob.this.setLayoutParams(params);
		}
	};
	public Knob(Context context) {
		super(context);
		init();
	}
	public Knob(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public Knob(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	void init() {
		
		if (initialized){
			return;}
		initialized = true;
		//this.context = c;
		labelText.property.afterChange(new Task() {
			@Override
			public void doTask() {
				setText(labelText.property.value());
			}
		});
		width.property.afterChange(reFit).value(100);
		height.property.afterChange(reFit).value(100);
		left.property.afterChange(reFit);
		top.property.afterChange(reFit);
		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tap.property.value() != null) {
					tap.property.value().doTask();
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
}
