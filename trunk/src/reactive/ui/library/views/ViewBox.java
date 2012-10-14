package reactive.ui.library.views;

import reactive.ui.library.*;
import reactive.ui.library.views.cells.*;
import reactive.ui.library.views.figures.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class ViewBox implements Unbind {
	public ItProperty<ViewBox, View> view = new ItProperty<ViewBox, View>(this);
	public NumericProperty<ViewBox> width = new NumericProperty<ViewBox>(this);
	public NumericProperty<ViewBox> height = new NumericProperty<ViewBox>(this);
	public NumericProperty<ViewBox> left = new NumericProperty<ViewBox>(this);
	public NumericProperty<ViewBox> top = new NumericProperty<ViewBox>(this);
	public ToggleProperty<ViewBox> hidden = new ToggleProperty<ViewBox>(this);
	public View delete;
	public Task requestLayout = new Task() {

		@Override
		public void doTask() {
			//Tools.log("requestLayout");
			//double r=Math.random();
			if (view.property.value() != null) {
				View v = view.property.value();
				//Tools.log("start requestLayout " + v+"/"+r);
				v.layout(//
						left.property.value().intValue()//
						, top.property.value().intValue()//
						, (int) (left.property.value() + width.property.value())//
						, (int) (top.property.value() + height.property.value()));
				//view.property.value().measure(width.property.value().intValue(), height.property.value().intValue());
				//view.property.value().requestLayout();
				//view.property.value().postInvalidate();
				/*if (v.getParent() != null) {
					Tools.log("requestLayout " + v.getParent().isLayoutRequested() + "/" + v);
				}*/
			}

		}
	};

	//public LayoutlessView layoutlessView;
	public ViewBox() {
		width.is(100);
		height.is(100);
		/*requestLayout=new Task() {

		@Override
		public void doTask() {
			if (view.property.value() != null) {
				view.property.value().layout(//
						left.property.value().intValue()//
						, top.property.value().intValue()//
						, (int) (left.property.value() + width.property.value())//
						, (int) (top.property.value() + height.property.value()));
				view.property.value().requestLayout();
				//view.property.value().postInvalidate();
				
				//Tools.log("invalidate");
			}

		}
		};*/
		width.property.afterChange(requestLayout);
		height.property.afterChange(requestLayout);
		left.property.afterChange(requestLayout);
		top.property.afterChange(requestLayout);
		hidden.property.afterChange(new Task() {

			@Override
			public void doTask() {
				if (view.property.value() != null) {
					View v = view.property.value();
					if (hidden.property.value()) {
						v.setVisibility(View.INVISIBLE);
					}
					else {
						v.setVisibility(View.VISIBLE);
					}
				}

			}
		});
	}

	public void unbind() {
		width.property.unbind();
		height.property.unbind();
		left.property.unbind();
		top.property.unbind();
		view.property.unbind();
	}
	/*	view.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (layoutlessView != null)
					if (view != null)
						if (view.property != null)
							layoutlessView.addView(view.property.value());
			}
		});
	}*/
}
