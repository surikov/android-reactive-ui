package reactive.ui;

import tee.binding.properties.*;
import android.graphics.*;

import android.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.AsyncTask;
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
	static final int maxPageCount = 3;
	public NumericProperty<Grid> pageSize;
	public NumericProperty<Grid> dataOffset;
	public NumericProperty<Grid> headerHeight;
	private Decor selection;
	public NumericProperty<Grid> rowHeight;
	public ItProperty<Grid, Task> beforeFlip = new ItProperty<Grid, Task>(this);
	private boolean lockAppend = false;
	GridColumn[] data = null;
	ProgressBar progressBar;
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
	public Grid setColumns(GridColumn[] indata) {
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
		append();
		return this;
	}
	public void flipData() {
		System.out.println("flipData");
		currentPage = 0;
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
			lockAppend = false;
			scrollView.setOverScrollMode(OVER_SCROLL_IF_CONTENT_SCROLLS);
		}
	}
	void tapRow(int row, int column) {
		for (int x = 0; x < data.length; x++) {
			data[x].highlight(row);
		}
		if (column < data.length) {
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
			pageSize = new NumericProperty<Grid>(this);
			pageSize.is(33);
			dataOffset = new NumericProperty<Grid>(this);
			dataOffset.is(0);
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
								int nn = (int) ((this.getScrollY() + aY) / rowHeight.property.value());
								double xx = 0;
								for (int i = 0; i < data.length; i++) {
									xx = xx + data[i].width.property.value();
									if (xx > aX) {
										tapRow(nn, i);
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
						return;
					}
					progressBar.setVisibility(View.VISIBLE);
					progressBar.postInvalidate();
					double scrollViewHeight = height().property.value() - headerHeight.property.value();
					double contentHeight = rowHeight.property.value() * (currentPage + 1) * pageSize.property.value();
					double limit = contentHeight - scrollViewHeight;
					if (top > 0 && limit > 0 && top >= limit) {
						if (currentPage < maxPageCount - 1) {
							currentPage++;
							append();
							progressBar.setVisibility(View.INVISIBLE);
						}
						else {
							double off = dataOffset.property.value() + pageSize.property.value() * (maxPageCount - 1);
							dataOffset.is(off);
							if (beforeFlip.property.value() != null) {
								new AsyncTask<Void, Void, Void>() {
									@Override
									protected Void doInBackground(Void... params) {
										try {
											//Thread.sleep(5000);
											beforeFlip.property.value().start();
										}
										catch (Throwable t) {
											//
										}
										return null;
									}
									@Override
									protected void onPostExecute(Void v) {
										currentPage = 0;
										flipData();
										new Handler().post(new Runnable() {
											@Override
											public void run() {
												scrollView.scrollTo(0, (int) (pageSize.property.value() * rowHeight.property.value() - headerHeight.property.value()));
												progressBar.setVisibility(View.INVISIBLE);
											}
										});
									}
								}.execute();
							}
							else {
								progressBar.setVisibility(View.INVISIBLE);
							}
						}
					}
					else {
						if (top <= 0) {
							if (dataOffset.property.value() > 0) {
								double off = dataOffset.property.value() - pageSize.property.value() * (maxPageCount - 1);
								if (off < 0) {
									off = 0;
								}
								dataOffset.is(off);
								if (beforeFlip.property.value() != null) {
									new AsyncTask<Void, Void, Void>() {
										@Override
										protected Void doInBackground(Void... params) {
											try {
												beforeFlip.property.value().start();
											}
											catch (Throwable t) {
												//
											}
											return null;
										}
										@Override
										protected void onPostExecute(Void v) {
											currentPage = 0;
											flipData();
											currentPage++;
											append();
											currentPage++;
											append();
											new Handler().post(new Runnable() {
												@Override
												public void run() {
													int nn = (int) (2 * pageSize.property.value() * rowHeight.property.value());
													scrollView.scrollTo(0, nn);
													progressBar.setVisibility(View.INVISIBLE);
												}
											});
										}
									}.execute();
								}
								else {
									progressBar.setVisibility(View.INVISIBLE);
								}
							}
							else {
								progressBar.setVisibility(View.INVISIBLE);
							}
						}
						else {
							progressBar.setVisibility(View.INVISIBLE);
						}
					}
				}
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
}
