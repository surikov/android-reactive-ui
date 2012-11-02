package reactive.ui.library.views;

import java.util.*;
import reactive.ui.library.*;
import android.content.*;
import android.view.*;
import reactive.ui.library.views.cells.*;
import tee.binding.They;

public class SimpleTable extends View implements Unbind {
	public They <SimpleTableColumn>columns = new They<SimpleTableColumn>();
	public SimpleTable(Context context) {
		super(context);
		
		
	}
public void requery(){}
	@Override
	public void unbind() {

	}

}
