package reactive.ui.library.views;

import reactive.ui.library.*;
import reactive.ui.library.views.cells.*;
import reactive.ui.library.views.figures.*;
import android.content.*;
import tee.binding.properties.*;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.*;
import tee.binding.task.*;
import tee.binding.it.*;
import android.text.*;

public class SimpleSlider extends SeekBar implements Unbind {
	public NumericProperty<SimpleSlider> number = new NumericProperty<SimpleSlider>(this);
	//public NumericProperty<SimpleSlider> max = new NumericProperty<SimpleSlider>(this);
	SimpleSlider me = this;

	public SimpleSlider(Context c) {
		super(c);
		//max.property.value(100);
		this.setMax(100);
		number.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (me.getProgress() != number.property.value().intValue()) {
					me.setProgress(number.property.value().intValue());
					me.postInvalidate();
				}
			}
		});
		/*
		new Numeric().value(100).afterChange(new Task() {
		    @Override
		    public void doTask() {
			if (me.getMax() != max.property.value().intValue()) {
			    me.setMax(max.property.value().intValue());
			    me.postInvalidate();
			}
		    }
		}).bind(max.property);
		*/

		setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				number.property.value(me.getProgress());
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				//
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				//
			}
		});
		//Tools.log("max "+me.getMax());
	}

	@Override
	public void unbind() {
	}
}
