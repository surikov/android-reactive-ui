package reactive.ui;

import tee.binding.properties.*;
import android.view.*;
public interface Rake {
	public NumericProperty<Rake> left();
	public NumericProperty<Rake> top();
	public NumericProperty<Rake> width();
	public NumericProperty<Rake> height();
	public View view();
}
