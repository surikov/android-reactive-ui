package reactive.ui.library.views;

import java.util.*;
import reactive.ui.library.*;
import android.content.*;
import android.view.*;
import reactive.ui.library.views.cells.*;

public class SimpleTable extends View implements Unbind {
	public Vector<SimpleTableColumn> columns = new Vector<SimpleTableColumn>();
	public SimpleTable(Context context) {
		super(context);

	}

	@Override
	public void unbind() {

	}

}
