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

public class DataGrid extends SubLayoutless {
	//create & fill columns
	//Grid.setColumns(
	//Grid.flipData
	//beforeFlip: refill columns from Grid.dataOffset
	public ToggleProperty<DataGrid> noHead;
	static final int maxPageCount = 3;
	public NumericProperty<DataGrid> pageSize;
	public NumericProperty<DataGrid> dataOffset;
	public NumericProperty<DataGrid> headerHeight;
	//private Decor selection;
	public NumericProperty<DataGrid> rowHeight;
	public ItProperty<DataGrid, Task> beforeFlip = new ItProperty<DataGrid, Task>(this);
	private boolean lockAppend = false;
	GridColumn[] columnsArray = null;
	ProgressBar progressBar;
	private int currentPage = 0;
	TableLayout tableLayout;
	ScrollView scrollView;
	Vector<TableRow> rows = new Vector<TableRow>();
	private boolean initialized = false;
	private SubLayoutless header;

	public DataGrid(Context context) {
		super(context);
	}
	public DataGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public DataGrid(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public void reset() {
		//System.out.println(this.getClass().getCanonicalName() + ".reset");
		for (int i = 0; i < rows.size(); i++) {
			rows.get(i).removeAllViews();
		}
		tableLayout.removeAllViews();
		rows.removeAllElements();
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
	public DataGrid columns(GridColumn[] indata) {
		//System.out.println(this.getClass().getCanonicalName() + ".columns");
		//currentPage = 0;
		//tableLayout.removeAllViews();
		columnsArray = indata;
		if (columnsArray.length < 1) {
			return this;
		}
		int left = 0;
		if (!noHead.property.value()) {
			
			for (int x = 0; x < columnsArray.length; x++) {
				Rake headerCell = columnsArray[x].header(getContext());
				headerCell.height().is(headerHeight.property.value());
				headerCell.width().is(columnsArray[x].width.property.value());
				headerCell.left().is(header.shiftX.property.plus(
						left));
				left = left + columnsArray[x].width.property.value().intValue();
				header.child(headerCell);
			}
		}
		//append();
		header.innerWidth.is(left);
		reset();
		flip();
		return this;
	}
	public void flip() {
		//System.out.println(this.getClass().getCanonicalName() + ".flipData");
		currentPage = 0;
		append();
	}
	private void append() {
		//System.out.println(this.getClass().getCanonicalName() + ".append start");
		if (lockAppend) {
			return;
		}
		if (columnsArray.length > 0) {
			lockAppend = true;
			scrollView.setOverScrollMode(OVER_SCROLL_NEVER);
			int start = (int) (currentPage * pageSize.property.value());
			for (int y = start; y < columnsArray[0].count() && y < (currentPage + 1) * pageSize.property.value(); y++) {
				TableRow tableRow;
				if (y < rows.size()) {
					tableRow = rows.get(y);
					//tableRow.removeAllViews();
					//System.out.println("get row " + (y));
					tableRow.setVisibility(View.VISIBLE);
					for (int x = 0; x < columnsArray.length; x++) {
						//Rake r = data[x].item(x, y, getContext());
						//View d = r.view();
						//r.height().is(rowHeight.property.value());
						//r.width().is(data[x].width.property.value());
						//tableRow.addView(r.view());
						this.columnsArray[x].update(y);
					}
				}
				else {
					tableRow = new TableRow(this.getContext());
					rows.add(tableRow);
					tableLayout.addView(tableRow);
					//System.out.println("add row " + (y));
					//tableRow.setVisibility(View.VISIBLE);
					for (int x = 0; x < columnsArray.length; x++) {
						Rake r = columnsArray[x].item(x, y, getContext());
						//View d = r.view();
						r.height().is(rowHeight.property.value());
						r.width().is(columnsArray[x].width.property.value());
						tableRow.addView(r.view());
					}
				}
			}
			int lastDataRow = columnsArray[0].count();
			int lastPageRow = (int) ((currentPage + 1) * pageSize.property.value());
			int lastFilled = 0;
			if (lastDataRow < lastPageRow) {
				lastFilled = lastDataRow;
			}
			else {
				lastFilled = lastPageRow;
			}
			int rowSize = rows.size();
			//System.out.println("hide lines " + lastFilled + " - " + rowSize);
			/*for (int i = lastFilled; i < rowSize; i++) {
				rows.get(i).removeAllViews();
				tableLayout.removeView(rows.get(i));
			}*/
			for (int i = lastFilled; i < rowSize; i++) {
				//rows.remove(lastFilled);
				rows.get(i).setVisibility(View.GONE);
			}
			lockAppend = false;
			scrollView.setOverScrollMode(OVER_SCROLL_IF_CONTENT_SCROLLS);
		}
		//System.out.println(this.getClass().getCanonicalName() + ".append done");
	}
	void tapRow(int row, int column) {
		for (int x = 0; x < columnsArray.length; x++) {
			columnsArray[x].highlight(row);
		}
		if (column < columnsArray.length) {
			columnsArray[column].afterTap(row);
		}
	}
	@Override
	protected void init() {
		super.init();
		if (!initialized) {
			initialized = true;
			noHead = new ToggleProperty<DataGrid>(this);
			Task reFit = new Task() {
				@Override
				public void doTask() {
					double hh = headerHeight.property.value();
					if (noHead.property.value()) {
						hh = 0;
					}
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
							width().property.value().intValue()//
							, (int) (height().property.value() - hh));
					params.topMargin = (int) hh;
					params.leftMargin=header.shiftX.property.value().intValue();
					
					if (scrollView != null) {
						scrollView.setLayoutParams(params);
					}
				}
			};
			
			pageSize = new NumericProperty<DataGrid>(this);
			pageSize.is(33);
			dataOffset = new NumericProperty<DataGrid>(this);
			dataOffset.is(0);
			headerHeight = new NumericProperty<DataGrid>(this);
			headerHeight.is(Layoutless.tapSize);
			header = new SubLayoutless(this.getContext());
			header.width().is(width().property);
			header.height().is(headerHeight.property);
			
			
			//header.left().is(-200);
			this.child(header);
			rowHeight = new NumericProperty<DataGrid>(this);
			rowHeight.is(Layoutless.tapSize);
			tableLayout = new TableLayout(this.getContext());
			scrollView = new ScrollView(this.getContext()) {
				float initialX = -1000;
				float initialY = -1000;

				@Override
				public boolean onTouchEvent(MotionEvent event) {
					if (columnsArray == null) {
						return false;
					}
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
								for (int i = 0; i < columnsArray.length; i++) {
									xx = xx + columnsArray[i].width.property.value();
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
					if (noHead.property.value()) {
						scrollViewHeight = height().property.value();
					}
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
										flip();
										new Handler().post(new Runnable() {
											@Override
											public void run() {
												double hh = headerHeight.property.value();
												if (noHead.property.value()) {
													hh = 0;
												}
												scrollView.scrollTo(0, (int) (//
														pageSize.property.value() * rowHeight.property.value() - hh//
														));
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
											flip();
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
			new Numeric().bind(header.shiftX.property).afterChange(reFit);
		}
	}
}
