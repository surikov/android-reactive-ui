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
		layoutlessView.innerWidth.is(180);
		layoutlessView.innerHeight.is(150);
		setContentView(layoutlessView);
		this.setTitle("Reactive UI Demo");
		layoutlessView.viewBox(new ViewBox()//
				.view.is(new SimpleString(this)//
				.text.is("asdvasdfva asdfvasdfv asdf vzasfv89898asdfv sadfvl,m llf")//				
				)//
				.width.is(100)//
				.height.is(100)//
				);
		layoutlessView.viewBox(new ViewBox()//
				.view.is(new SimpleRectangle(this)//
				.color.is(0x99ff0000)//				
				)//
				.width.is(100)//
				.height.is(100)//
				);
	}
}