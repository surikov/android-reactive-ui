package reactive.ui.library;

import reactive.ui.library.views.*;
import reactive.ui.library.views.cells.*;
import reactive.ui.library.views.figures.*;
import tee.binding.task.*;
import android.app.*;
import android.content.DialogInterface;
import android.os.*;
import java.io.*;

public class OldReactiveUIActivity extends Activity {
	LayoutlessView layoutlessView;

	//WhiteBoard grid;
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
		SimpleTable table = new SimpleTable(this);
		layoutlessView.viewBox(new ViewBox()//
				.view.is(table)//				
				.width.is(700	)//
				.height.is(400)//
				.left.is(80)//
				.top.is(80)//
				);
		//grid = new WhiteBoard(this);
		/*layoutlessView.viewBox(new ViewBox()//
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
				.view.is(table)//				
				.width.is(layoutlessView.innerWidth.property)//
				.height.is(layoutlessView.innerHeight.property)//
				.left.is(layoutlessView.shiftX.property)//
				.top.is(layoutlessView.shiftY.property)//
				);
		
		layoutlessView.viewBox(new ViewBox().view.is(new SimpleString(this).text.is("First Column")//				
				).width.is(100).height.is(60 * density).left.is(layoutlessView.shiftX.property));
		layoutlessView.viewBox(new ViewBox().view.is(new SimpleString(this).text.is("Second Column")//				
				).width.is(200).height.is(60 * density).left.is(layoutlessView.shiftX.property.plus(100)));
		layoutlessView.viewBox(new ViewBox().view.is(new SimpleString(this).text.is("Third Column")//				
				).width.is(150).height.is(60 * density).left.is(layoutlessView.shiftX.property.plus(300)));
		//rebuildGrid(9);
		*/
		SimpleTableColumn c1 = new SimpleTableColumn().title.is("First").width.is(320);
		SimpleTableColumn c2 = new SimpleTableColumn().title.is("Second column looong title that fit whole height of header").width.is(250);
		SimpleTableColumn c3 = new SimpleTableColumn().title.is("Third").width.is(200);
		c1.values.value("1");
		c2.values.value("2");
		c3.values.value("3");
		c1.values.value("4");
		c2.values.value("5");
		c3.values.value("6");
		c1.values.value("7");
		c2.values.value("8");
		c3.values.value("9");
		c1.values.value("10");
		c2.values.value("11");
		c3.values.value("12");
		table.columns.value(c1).value(c2).value(c3);
		table.requery();
		final String[] items = new String[] { "One", "Second", "Third", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21" };
		layoutlessView.viewBox(new ViewBox().view.is(new SimpleButton(this)//
				.text.is("Choose text")//	
				.tap.is(new Task() {
					@Override
					public void doTask() {
						//new TextListChooser(ReactiveUIActivity.this);
						layoutlessView.chooseDialog("Test list of string items without meaningles", items, new LayoutlessView.ItemChoose() {
							public void choose(int which) {
								System.out.println("tapped item " + which + ": " + items[which]);
							}
						});
						//System.out.println("tap");
					}
				})).width.is(100)//
				.height.is(60 * density)//
				.left.is(20)//
				);
		layoutlessView.viewBox(new ViewBox().view.is(new SimpleButton(this)//
				.text.is("Choose file")//	
				.tap.is(new Task() {
					@Override
					public void doTask() {
						layoutlessView.chooseDialog("/sdcard/", new LayoutlessView.FileChoose() {
							@Override
							public void choose(File path) {
								System.out.println("selected " + path.getAbsolutePath());
							}
						});
					}
				}))//
				.width.is(100)//
				.height.is(60 * density)//
				.left.is(20)//
				.top.is(100)//
				);
		layoutlessView.viewBox(new ViewBox().view.is(new SimpleString(this)//
				.text.is("Simple String")//	
				.size.is(72)
				)//
				.width.is(300)//
				.height.is(300 )//
				.left.is(300)//
				.top.is(300)
				);
	}
	/*void rebuildGrid(int rows) {
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
	}*/
}
