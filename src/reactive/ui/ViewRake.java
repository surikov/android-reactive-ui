package reactive.ui;

import tee.binding.properties.*;
import android.view.*;
public interface ViewRake {
	public NumericProperty<ViewRake> left();
	public NumericProperty<ViewRake> top();
	public NumericProperty<ViewRake> width();
	public NumericProperty<ViewRake> height();
	public View view();
}
