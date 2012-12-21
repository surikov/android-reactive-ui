package reactive.ui;

import tee.binding.properties.*;

import reactive.ui.*;
import reactive.ui.library.views.*;
import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import tee.binding.task.*;
import tee.binding.it.*;
import android.text.*;

public abstract class SheetColumn {
	//public NoteProperty<SheetColumn> title=new NoteProperty<SheetColumn> (this);
	public NumericProperty<SheetColumn> width=new NumericProperty<SheetColumn> (this);
	public ItProperty<SheetColumn, Task> afterHeaderTap = new ItProperty<SheetColumn, Task>(this);
	public ItProperty<SheetColumn, Task> afterCellTap = new ItProperty<SheetColumn, Task>(this);
	//public NoteProperty<SheetColumn> labelText = new NoteProperty<SheetColumn>(this);
	abstract public Rake cell(int row,Context c);
	abstract public int count();
	//public int width();
	abstract public Rake header(Context c);
}
