package reactive.ui.library.views.cells;

import reactive.ui.library.views.*;
import tee.binding.properties.*;
import tee.binding.properties.*;
import java.util.*;
public class SimpleTableColumn {
	public NoteProperty<SimpleTableColumn> title = new NoteProperty<SimpleTableColumn>(this);
	public NumericProperty<SimpleTableColumn> width = new NumericProperty<SimpleTableColumn>(this);
	public Vector<String>values=new Vector<String>();
}
