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

public class Knob extends Button {
	public NoteProperty<Knob> labelText = new NoteProperty<Knob>(this);
	public NumericProperty<Knob> width = new NumericProperty<Knob>(this);
	public NumericProperty<Knob> height = new NumericProperty<Knob>(this);
	public NumericProperty<Knob> left = new NumericProperty<Knob>(this);
	public NumericProperty<Knob> top = new NumericProperty<Knob>(this);
	public ItProperty<Knob, Task> tap = new ItProperty<Knob, Task>(this);
	boolean initialized = false;
	Context context;
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
		init(context);
	}
	public Knob(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public Knob(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	void init(Context c) {
		
		if (initialized){
			return;}
		initialized = true;
		this.context = c;
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
	
}
