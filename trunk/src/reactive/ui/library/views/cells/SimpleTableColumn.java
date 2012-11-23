package reactive.ui.library.views.cells;

import reactive.ui.library.views.*;
import tee.binding.properties.*;
import tee.binding.*;
import java.util.*;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;

public class SimpleTableColumn {
	public NoteProperty<SimpleTableColumn> title = new NoteProperty<SimpleTableColumn>(this);
	public NumericProperty<SimpleTableColumn> width = new NumericProperty<SimpleTableColumn>(this);
	public They <String>values = new They<String>();
	
}
