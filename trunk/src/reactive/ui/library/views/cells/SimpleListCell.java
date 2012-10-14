package reactive.ui.library.views.cells;

import android.content.*;
import tee.binding.properties.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import tee.binding.task.*;
import tee.binding.it.*;
import android.text.*;
import android.util.*;
import android.app.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import tee.binding.task.*;
import tee.binding.it.*;
import android.content.res.*;
import java.util.*;

import reactive.ui.library.views.*;
import reactive.ui.library.views.*;
import reactive.ui.library.views.cells.*;
import reactive.ui.library.views.figures.*;

public class SimpleListCell {
	public NoteProperty<SimpleListCell> title = new NoteProperty<SimpleListCell>(this);
	public NoteProperty<SimpleListCell> description = new NoteProperty<SimpleListCell>(this);
	public NumericProperty<SimpleListCell> color = new NumericProperty<SimpleListCell>(this);
	public NumericProperty<SimpleListCell> background = new NumericProperty<SimpleListCell>(this);
	public ItProperty<SimpleListCell, Bitmap> bitmap = new ItProperty<SimpleListCell, Bitmap>(this);
	public ToggleProperty<SimpleListCell> pluckable = new ToggleProperty<SimpleListCell>(this);
	public NoteProperty<SimpleListCell> uuid = new NoteProperty<SimpleListCell>(this);
	public NumericProperty<SimpleListCell> id = new NumericProperty<SimpleListCell>(this);
	public Vector<Figure>figures=new Vector<Figure>();
	public SimpleListCell() {
		background.is(0x00000000);
		color.is(0xffffffff);
	}
	public SimpleListCell figure(Figure f){
		figures.add(f);
		return this;
	}
}
