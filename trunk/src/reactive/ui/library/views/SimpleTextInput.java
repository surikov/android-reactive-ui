package reactive.ui.library.views;

import reactive.ui.library.*;
import reactive.ui.library.views.cells.*;
import reactive.ui.library.views.figures.*;
import android.content.*;
import tee.binding.properties.*;
import android.widget.*;
import tee.binding.task.*;
import tee.binding.it.*;
import android.text.*;

public class SimpleTextInput extends EditText  implements Unbind {
    
    SimpleTextInput me = this;
    public NoteProperty<SimpleTextInput> text = new NoteProperty<SimpleTextInput>(this);
    public SimpleTextInput(Context c) {
	super(c);
	text.property.afterChange(new Task() {
	    @Override
	    public void doTask() {
		if (!me.getText().toString().equals(text.property.value())) {
		    me.setText(text.property.value());
		    me.postInvalidate();
		}
	    }
	});
	addTextChangedListener(new TextWatcher() {
	    @Override
	    public void afterTextChanged(Editable arg0) {
		text.property.value(me.getText().toString());
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
