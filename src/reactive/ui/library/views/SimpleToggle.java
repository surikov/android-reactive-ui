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

public class SimpleToggle extends CheckBox implements Unbind  {
    public ToggleProperty<SimpleToggle> check = new ToggleProperty<SimpleToggle>(this);
    SimpleToggle me = this;

    public SimpleToggle(Context c) {
	super(c);
	check.property.afterChange(new Task() {
	    @Override
	    public void doTask() {
		if (me.isChecked() != check.property.value()) {
		    me.setChecked(check.property.value());
		    me.postInvalidate();
		}
	    }
	});
	this.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		check.property.value(me.isChecked());

	    }
	});
    }
    @Override
	public void unbind() {
	}
}
