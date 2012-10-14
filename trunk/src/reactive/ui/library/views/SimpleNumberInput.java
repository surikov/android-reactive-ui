package reactive.ui.library.views;
import reactive.ui.library.*;
import reactive.ui.library.views.cells.*;
import reactive.ui.library.views.figures.*;
import android.content.*;
import tee.binding.properties.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import tee.binding.task.*;
import tee.binding.it.*;
import android.text.*;

public class SimpleNumberInput extends EditText implements Unbind{
    public NumericProperty<SimpleNumberInput> number = new NumericProperty<SimpleNumberInput>(this);
    SimpleNumberInput me = this;

    public SimpleNumberInput(Context c) {
	super(c);
	this.setInputType(InputType.TYPE_CLASS_NUMBER);
	number.property.afterChange(new Task() {
	    @Override
	    public void doTask() {
		double d = Numeric.string2double(me.getText().toString());
		if (d != number.property.value()) {
		    me.setText("" + number.property.value());
		    me.postInvalidate();
		}
	    }
	});
	addTextChangedListener(new TextWatcher() {
	    @Override
	    public void afterTextChanged(Editable arg0) {
		number.property.value(Numeric.string2double(me.getText().toString()));
	    }

	    @Override
	    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	    }

	    @Override
	    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	    }
	});
    }
    @Override
	public void unbind() {				
	}
}
