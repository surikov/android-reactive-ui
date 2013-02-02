package reactive.ui;

import tee.binding.properties.*;
import android.graphics.*;

import android.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.*;
import reactive.ui.*;

import android.view.animation.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;

import java.io.*;
import java.text.*;

public class Grid extends SubLayoutless {
	public ToggleProperty<Grid> noHead = new ToggleProperty<Grid>(this);
	public NumericProperty<Grid> maxPageCount;
	public NumericProperty<Grid> pageSize;
	public NumericProperty<Grid> headerHeight;
	public NumericProperty<Grid> rowHeight;
	private boolean lock = false;
	GridColumn[] data = null;
	/*private CannyTask flip = new CannyTask() {
		@Override
		public void doTask() {
			if (offset < maxPageCount.property.value()) {
				offset++;
				flip();
			}
		}
	};*/
	int offset = 0;
	TableLayout tableLayout;
	ScrollView scrollView;
	private boolean initialized = false;

	public Grid(Context context) {
		super(context);
	}
	public Grid(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public Grid(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public void reset() {
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
	public Grid setData(GridColumn[] indata) {
		offset = 0;
		tableLayout.removeAllViews();
		data = indata;
		if (data.length < 1) {
			return this;
		}
		int left = 0;
		for (int x = 0; x < data.length; x++) {
			Rake r = data[x].header(getContext());
			r.height().is(headerHeight.property.value());
			r.width().is(data[x].width.property.value());
			r.left().is(left);
			left = left + data[x].width.property.value().intValue();
			this.child(r);
		}
		return this;
	}
	public void flip() {
		if (lock) {
			return;
		}
		if (data.length > 0) {
			lock = true;
			scrollView.setOverScrollMode(OVER_SCROLL_NEVER);
			System.out.println("flip");
			//tableLayout.removeAllViews();
			for (int y = (int) (offset * pageSize.property.value()); y < data[0].count() && y < (offset + 1) * pageSize.property.value(); y++) {
				TableRow tableRow = new TableRow(this.getContext());
				for (int x = 0; x < data.length; x++) {
					Rake r = data[x].item(x, y, getContext());
					Decor d = ((Decor) r.view());
					r.height().is(rowHeight.property.value());
					r.width().is(data[x].width.property.value());
					tableRow.addView(r.view());
				}
				tableLayout.addView(tableRow);
			}
			System.out.println("done flip");
			lock = false;
			scrollView.setOverScrollMode(OVER_SCROLL_IF_CONTENT_SCROLLS);
		}
	}
	@Override
	protected void init() {
		super.init();
		if (!initialized) {
			initialized = true;
			Task reFit = new Task() {
				@Override
				public void doTask() {
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
							width().property.value().intValue()//
							, (int) (height().property.value() - headerHeight.property.value()));
					params.topMargin = headerHeight.property.value().intValue();
					if (scrollView != null) {
						scrollView.setLayoutParams(params);
					}
				}
			};
			pageSize = new NumericProperty<Grid>(this);
			pageSize.is(20);
			maxPageCount = new NumericProperty<Grid>(this);
			maxPageCount.is(15);
			headerHeight = new NumericProperty<Grid>(this);
			headerHeight.is(Layoutless.tapSize);
			rowHeight = new NumericProperty<Grid>(this);
			rowHeight.is(Layoutless.tapSize);
			tableLayout = new TableLayout(this.getContext());
			scrollView = new ScrollView(this.getContext()) {
				@Override
				protected void onScrollChanged(int l, int t, int oldl, int oldt) {
					super.onScrollChanged(l, t, oldl, oldt);
					double scrollViewHeight=height().property.value()-headerHeight.property.value();
					double contentHeight=rowHeight.property.value() *( offset+1) * pageSize.property.value();
					double limit = contentHeight -scrollViewHeight ;
					//System.out.println(t+"/"+limit+"/"+scrollViewHeight+"/"+contentHeight);
					if (t > 0 && limit>0 && t >= limit) {
						//if (t >= limit) {
							//flip.start();
							if (offset < maxPageCount.property.value()) {
								offset++;
								flip();
							}
						//}
					}
				}
				/*
				@Override
				public void computeScroll() {
					super.computeScroll();
				}*/
			};
			scrollView.addView(tableLayout);
			this.addView(scrollView);
			new Numeric().bind(width().property).afterChange(reFit);
			new Numeric().bind(height().property).afterChange(reFit);
		}
	}
}
