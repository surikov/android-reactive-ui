package reactive.ui;

import tee.binding.properties.*;
import android.graphics.*;

import android.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.Handler;
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
	//public NumericProperty<Grid> maxPageCount;
	static final int maxPageCount = 3;
	public NumericProperty<Grid> pageSize;
	public NumericProperty<Grid> dataOffset;
	public NumericProperty<Grid> headerHeight;
	//public NumericProperty<Grid> selectedRow;
	private Decor selection;
	public NumericProperty<Grid> rowHeight;
	public ItProperty<Grid, Task> beforeNext = new ItProperty<Grid, Task>(this);
	public ItProperty<Grid, Task> beforePrevious = new ItProperty<Grid, Task>(this);
	private boolean lockAppend = false;
	//private boolean lockScroll = false;
	GridColumn[] data = null;
	ProgressBar progressBar;
	//boolean test=false;
	/*private CannyTask flip = new CannyTask() {
		@Override
		public void doTask() {
			if (offset < maxPageCount.property.value()) {
				offset++;
				flip();
			}
		}
	};*/
	private int currentPage = 0;
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
		//System.out.println("setData");
		currentPage = 0;
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
		//System.out.println("done add columns");
		append();
		return this;
	}
	public void flipData() {
		//System.out.println("flipData");
		tableLayout.removeAllViews();
		append();
	}
	private void append() {
		if (lockAppend) {
			return;
		}
		if (data.length > 0) {
			lockAppend = true;
			scrollView.setOverScrollMode(OVER_SCROLL_NEVER);
			//System.out.println("append");
			//tableLayout.removeAllViews();
			//tableLayout.removeAllViews();
			for (int y = (int) (currentPage * pageSize.property.value()); y < data[0].count() && y < (currentPage + 1) * pageSize.property.value(); y++) {
				TableRow tableRow = new TableRow(this.getContext());
				for (int x = 0; x < data.length; x++) {
					Rake r = data[x].item(x, y, getContext());
					View d = r.view();
					r.height().is(rowHeight.property.value());
					r.width().is(data[x].width.property.value());
					tableRow.addView(r.view());
				}
				tableLayout.addView(tableRow);
			}
			//scrollView.scrollTo(0, 0);
			/*
			new Handler().post(new Runnable() {
			    @Override
			    public void run() {
			    	scrollView.scrollTo(0, 0);
			    }
			});*/
			//System.out.println("done append");
			lockAppend = false;
			scrollView.setOverScrollMode(OVER_SCROLL_IF_CONTENT_SCROLLS);
		}
	}
	void tapRow(int row,int column){
		//System.out.println(column);
		for (int x = 0; x < data.length; x++) {
			data[x].highlight(row);
		}
		if(column< data.length){
			
			data[column].afterTap(row);
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
			//selectedRow = new NumericProperty<Grid>(this);
			//selectedRow.is(-1);
			pageSize = new NumericProperty<Grid>(this);
			pageSize.is(33);
			dataOffset = new NumericProperty<Grid>(this);
			dataOffset.is(0);
			//maxPageCount = new NumericProperty<Grid>(this);
			//maxPageCount.is(3);
			headerHeight = new NumericProperty<Grid>(this);
			headerHeight.is(Layoutless.tapSize);
			rowHeight = new NumericProperty<Grid>(this);
			rowHeight.is(Layoutless.tapSize);
			tableLayout = new TableLayout(this.getContext());
			scrollView = new ScrollView(this.getContext()) {
				float initialX = -1000;
				float initialY = -1000;

				@Override
				public boolean onTouchEvent(MotionEvent event) {
					//
					if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
						initialX = event.getX();
						initialY = event.getY();
					}
					else {
						if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
							float aX = event.getX();
							float aY = event.getY();
							float diff = 4;
							if (Math.abs(initialX - aX) < diff && Math.abs(initialY - aY) < diff) {
								//int nn = (int) (dataOffset.property.value() + (this.getScrollY() + aY) / rowHeight.property.value());
								int nn = (int) ((this.getScrollY() + aY) / rowHeight.property.value());
								//System.out.println("tap " + aX);
								double xx=0;
								for(int i=0;i<data.length;i++){
									xx=xx+data[i].width.property.value();
									//System.out.println("yy " + xx);
									if(xx>aX){
										tapRow(nn,i);
										break;
									}
								}
								
							}
						}
					}
					return super.onTouchEvent(event);
				}
				@Override
				protected void onScrollChanged(int left, int top, int oldLeft, int oldTop) {
					super.onScrollChanged(left, top, oldLeft, oldTop);
					if (progressBar.getVisibility() == View.VISIBLE) {
						//System.out.println("scroll locked");
						return;
					}
					//lockScroll = true;
					progressBar.setVisibility(View.VISIBLE);
					progressBar.postInvalidate();
					double scrollViewHeight = height().property.value() - headerHeight.property.value();
					double contentHeight = rowHeight.property.value() * (currentPage + 1) * pageSize.property.value();
					double limit = contentHeight - scrollViewHeight;
					//System.out.println(top+"/"+limit+"/"+scrollViewHeight+"/"+contentHeight+" /"+dataOffset.property.value()+"/"+currentPage);
					if (top > 0 && limit > 0 && top >= limit) {
						//if (t >= limit) {
						//flip.start();
						if (currentPage < maxPageCount - 1) {
							currentPage++;
							//System.out.println("scroll " + currentPage);
							append();
							//lockScroll = false;
							progressBar.setVisibility(View.INVISIBLE);
						}
						else {
							//System.out.println("next start");
							//if(test)return;
							//test=true;
							double off = dataOffset.property.value() + pageSize.property.value() * (maxPageCount - 1);
							dataOffset.is(off);
							//next();
							if (beforeNext.property.value() != null) {
								currentPage = 0;
								beforeNext.property.value().start();
								//System.out.println("scroll after next");
								//scrollView.scrollTo(0, 1);
								new Handler().post(new Runnable() {
									@Override
									public void run() {
										scrollView.scrollTo(0, (int) (pageSize.property.value() * rowHeight.property.value() - headerHeight.property.value()));
										//lockScroll = false;
										progressBar.setVisibility(View.INVISIBLE);
									}
								});
							}
							else {
								//lockScroll = false;
								progressBar.setVisibility(View.INVISIBLE);
							}
							//System.out.println("next done");							
						}
						//}
					}
					else {
						if (top <= 0) {
							if (dataOffset.property.value() > 0) {
								//System.out.println("prev start "+top);
								//System.out.println(top+"/"+limit+"/"+scrollViewHeight+"/"+contentHeight);
								double off = dataOffset.property.value() - pageSize.property.value() * (maxPageCount - 1);
								if (off < 0) {
									off = 0;
								}
								dataOffset.is(off);
								//prev();
								if (beforePrevious.property.value() != null) {
									currentPage = 0;
									beforePrevious.property.value().start();
									//System.out.println("beforePrevious done");
									//currentPage = 1;
									currentPage++;
									append();
									currentPage++;
									append();
									new Handler().post(new Runnable() {
										@Override
										public void run() {
											int nn = (int) (2 * pageSize.property.value() * rowHeight.property.value()
											//160
											//140-1200 
											//154.5 - 2400
											//154.5 - 3600
											//- headerHeight.property.value()
											//+height().property.value()
											);
											//nn=1;
											scrollView.scrollTo(0, nn);
											//System.out.println("scroll after prev "+nn);
											//lockScroll = false;
											progressBar.setVisibility(View.INVISIBLE);
										}
									});
								}
								else {
									//lockScroll = false;
									progressBar.setVisibility(View.INVISIBLE);
								}
							}
							else {
								//lockScroll = false;
								progressBar.setVisibility(View.INVISIBLE);
							}
						}
						else {
							//lockScroll = false;
							progressBar.setVisibility(View.INVISIBLE);
						}
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
			progressBar = new ProgressBar(this.getContext(), null, android.R.attr.progressBarStyleLarge);
			this.addView(progressBar);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (0.5 * Layoutless.tapSize), (int) (0.5 * Layoutless.tapSize));
			progressBar.setLayoutParams(params);
			progressBar.setVisibility(View.INVISIBLE);
		}
	}
	/*
	void next() {
		System.out.println("next: " + dataOffset.property.value());
		if(this.beforeNext.property.value()!=null){
			this.beforeNext.property.value().start();
		}
	}
	void prev() {
		System.out.println("prev: " + dataOffset.property.value());
		if(this.beforePrevious.property.value()!=null){
			this.beforePrevious.property.value().start();
		}
	}*/
}
