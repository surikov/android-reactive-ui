package reactive.ui.library.views;

import reactive.ui.library.*;
import reactive.ui.library.views.cells.*;
import reactive.ui.library.views.figures.*;
import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.*;
import tee.binding.task.*;
import tee.binding.it.*;
import android.text.*;
import android.view.inputmethod.*;
import android.text.method.*;

public class SimplePasswordInput extends EditText implements Unbind {
    public NoteProperty<SimplePasswordInput> text = new NoteProperty<SimplePasswordInput>(this);
    SimplePasswordInput me = this;
    @Override
	public void unbind() {				
	}
    public SimplePasswordInput(Context c) {
	super(c);
	// this.setInputType(InputType.TYPE_CLASS_PHONE );
	// .TYPE_CLASS_NUMBER);
	setTransformationMethod(PasswordTransformationMethod.getInstance());
	setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
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
}
