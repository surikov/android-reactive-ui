package reactive.ui.library;

import android.app.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
//import music.riff.studio.ii.R.drawable;
import tee.binding.task.*;
import tee.binding.it.*;
import tee.binding.*;
import tee.binding.properties.*;
import android.content.res.*;
import java.util.*;

import reactive.ui.library.views.*;
import reactive.ui.library.views.cells.*;
import reactive.ui.library.views.figures.*;

public class SlidePanel {
	ItemsList list;

	public SlidePanel(//
			LayoutlessView layoutless//
			, Activity activity//
			, Numeric spot//
			, Numeric topMargin//
			, Numeric splitIconTop//
			, Numeric splitPosition//
			, They<SimpleListCell> cells//
			, Numeric dropX//
			, Numeric dropY//
			, Numeric id//
			, Note uuid//
			, Task tap//
			, Task drop//
			
	) {

		WhiteBoard shadow = new WhiteBoard(activity);
		Numeric half = new Numeric().bind(spot.divide(2));
		layoutless.viewBox(new ViewBox().view.is(shadow //shadow
				.figure(new LineStroke(shadow,null).color.is(0x11000000).x1.is(half.plus(0)).y1.is(0).x2.is(half.plus(0)).y2.is(layoutless.height))//
				.figure(new LineStroke(shadow,null).color.is(0x22000000).x1.is(half.plus(1)).y1.is(0).x2.is(half.plus(1)).y2.is(layoutless.height))//
				.figure(new LineStroke(shadow,null).color.is(0x66000000).x1.is(half.plus(2)).y1.is(0).x2.is(half.plus(2)).y2.is(layoutless.height))//
				.figure(new LineStroke(shadow,null).color.is(0x99000000).x1.is(half.plus(3)).y1.is(0).x2.is(half.plus(3)).y2.is(layoutless.height))//				
				.figure(new LineStroke(shadow,null).color.is(0xcc000000).x1.is(half.plus(4)).y1.is(0).x2.is(half.plus(4)).y2.is(layoutless.height))//
				.figure(new FilledRectangle(shadow,null).fill.is(0xff000000).stroke.is(0x00000000).x.is(half.plus(4)).y.is(0).w.is(8).h.is(layoutless.height))//
				.figure(new FilledRectangle(shadow,null).fill.is(0x11000000).stroke.is(0x00000000).x.is(0).y.is(splitIconTop.minus(5)).w.is(spot.plus(5 * 2)).h.is(spot.plus(5 * 2)).arcX.is(half.plus(5)).arcY.is(half.plus(5)))//
				.figure(new FilledRectangle(shadow,null).fill.is(0x22000000).stroke.is(0x00000000).x.is(1).y.is(splitIconTop.minus(4)).w.is(spot.plus(4 * 2)).h.is(spot.plus(4 * 2)).arcX.is(half.plus(4)).arcY.is(half.plus(4)))//
				.figure(new FilledRectangle(shadow,null).fill.is(0x33000000).stroke.is(0x00000000).x.is(2).y.is(splitIconTop.minus(3)).w.is(spot.plus(3 * 2)).h.is(spot.plus(3 * 2)).arcX.is(half.plus(3)).arcY.is(half.plus(3)))//
				.figure(new FilledRectangle(shadow,null).fill.is(0x66000000).stroke.is(0x00000000).x.is(3).y.is(splitIconTop.minus(2)).w.is(spot.plus(2 * 2)).h.is(spot.plus(2 * 2)).arcX.is(half.plus(2)).arcY.is(half.plus(2)))//
				.figure(new FilledRectangle(shadow,null).fill.is(0x99000000).stroke.is(0x00000000).x.is(4).y.is(splitIconTop.minus(1)).w.is(spot.plus(1 * 2)).h.is(spot.plus(1 * 2)).arcX.is(half.plus(1)).arcY.is(half.plus(1)))//
				.figure(new FilledRectangle(shadow,null).fill.is(0xff333333).stroke.is(0x00000000).x.is(5).y.is(splitIconTop).w.is(spot).h.is(spot).arcX.is(half).arcY.is(half))//
				)//
				.width.is(spot)//
				.height.is(layoutless.height)//
				.left.is(splitPosition.minus(5))//
				.top.is(0)//
				);
		layoutless.viewBox(new ViewBox().view.is(new SimpleRectangle(activity)//background
				.color.is(0xff333333)//
				)//
				.width.is(layoutless.width.when(layoutless.width.more(layoutless.height)).otherwise(layoutless.height))//
				.height.is(layoutless.width.when(layoutless.width.more(layoutless.height)).otherwise(layoutless.height))//
				.left.is(spot.divide(2).plus(splitPosition))//
				.top.is(0)//
				);
		list = new ItemsList(activity, cells, layoutless.dragLayer())//list
		.drop.is(drop)//
		.tap.is(tap)//
		.dropX.is(dropX)//
		.dropY.is(dropY)//
		.id.is(id)//
		.uuid.is(uuid)//
		.topMargin.is(topMargin)//
		;
		layoutless.viewBox(new ViewBox().view.is(list).width.is(layoutless.width)//
				.height.is(layoutless.height)//
				.left.is(spot.divide(2).plus(splitPosition).plus(1))//
				.top.is(0)//
				);

		/*layoutless.viewBox(new ViewBox().view.is(new SimpleRectangle(activity)//line
				.color.is(0xffffffff)//
				)//
				.width.is(1)//
				.height.is(layoutless.height)//
				.left.is(splitPosition.plus(half))//
				.top.is(0)//
				);*/
		layoutless.viewBox(new ViewBox().view.is(new TouchHandler(activity)//splitter
				.shiftX.is(splitPosition)//
				.shiftY.is(splitIconTop)//
				.minShiftX.is(0)//
				.maxShiftX.is(layoutless.width.minus(half))//
				.minShiftY.is(splitIconTop)//
				.maxShiftY.is(splitIconTop)//
				)//
				.width.is(spot)//
				.height.is(spot)//
				.left.is(splitPosition.minus(half))//
				.top.is(splitIconTop)//
				);
	}

	public void resetRows() {
		list.resetRows();
	}
}
