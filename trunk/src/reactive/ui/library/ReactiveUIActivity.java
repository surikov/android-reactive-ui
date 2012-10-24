package reactive.ui.library;

import reactive.ui.library.views.*;
import reactive.ui.library.views.figures.*;
import android.app.*;
import android.os.*;

public class ReactiveUIActivity extends Activity {
	LayoutlessView layoutlessView;
	WhiteBoard grid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		float density = this.getResources().getDisplayMetrics().density;
		layoutlessView = new LayoutlessView(this)//
		//.zoom.is(Tools.zoomRiff)//
		//.maxZoom.is(4)
		.innerWidth.is(700)//
		.innerHeight.is(900);
		setContentView(layoutlessView);
		this.setTitle("Reactive UI Demo");
		grid = new WhiteBoard(this);
		layoutlessView.viewBox(new ViewBox()//
				.view.is(new SimpleRectangle(this)//
				.color.is(0xffff0000)//	
				.arcX.is(160)//
				.arcY.is(160)//
				)//
				.width.is(layoutlessView.innerWidth.property)//
				.height.is(layoutlessView.innerHeight.property)//
				.left.is(layoutlessView.shiftX.property)//
				.top.is(layoutlessView.shiftY.property.plus(60 * density))//
				);
		layoutlessView.viewBox(new ViewBox()//
				.view.is(grid)//				
				.width.is(layoutlessView.innerWidth.property)//
				.height.is(layoutlessView.innerHeight.property)//
				.left.is(layoutlessView.shiftX.property)//
				.top.is(layoutlessView.shiftY.property.plus(60 * density))//
				);
		layoutlessView.viewBox(new ViewBox().view.is(new SimpleString(this).text.is("First Column")//				
				).width.is(100).height.is(60 * density).left.is(layoutlessView.shiftX.property));
		layoutlessView.viewBox(new ViewBox().view.is(new SimpleString(this).text.is("Second Column")//				
				).width.is(200).height.is(60 * density).left.is(layoutlessView.shiftX.property.plus(100)));
		layoutlessView.viewBox(new ViewBox().view.is(new SimpleString(this).text.is("Third Column")//				
				).width.is(150).height.is(60 * density).left.is(layoutlessView.shiftX.property.plus(300)));
		rebuildGrid(9);
	}
	void rebuildGrid(int rows) {
		grid.clear();
		for (int r = 0; r < 10; r++) {
			grid.figure(new FilledRectangle(layoutlessView, grid)//
			.x.is(0)//
			.y.is(r * 20)//
			.w.is(200)//
			.h.is(1)//
			.fill.is(0xff00ff00)//
			);
		}
		for (int c = 0; c < 10; c++) {
			grid.figure(new FilledRectangle(layoutlessView, grid)//
			.x.is(c * 20)//
			.y.is(0)//
			.w.is(1)//
			.h.is(200)//
			.fill.is(0xff00ff00)//
			);
		}
	}
}
