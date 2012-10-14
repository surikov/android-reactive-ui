package reactive.ui.library;

import reactive.ui.library.views.*;
import android.app.*;
import android.os.*;

public class ReactiveUIActivity extends Activity {
	LayoutlessView layoutlessView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layoutlessView = new LayoutlessView(this)//
		//.zoom.is(Tools.zoomRiff)//
		//.maxZoom.is(4)
		;
		//layoutlessView.innerWidth.is(180);
		//layoutlessView.innerHeight.is(150);
		setContentView(layoutlessView);
		this.setTitle("Reactive UI Demo");
		//layoutlessView.viewBox(new ViewBox(new SimpleString(this)));
	}
}