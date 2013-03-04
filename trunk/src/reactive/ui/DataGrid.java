package reactive.ui;

import tee.binding.properties.*;
import android.view.*;
import android.app.Activity;
import android.content.*;
import android.os.*;
import android.util.*;
import android.widget.*;

import java.io.File;
import java.util.*;
import tee.binding.task.*;
import tee.binding.it.*;

public class DataGrid extends SubLayoutless {
	public ToggleProperty<DataGrid> noHead;
	static final int maxPageCount = 3;
	public NumericProperty<DataGrid> pageSize;
	public NumericProperty<DataGrid> dataOffset;
	public NumericProperty<DataGrid> headerHeight;
	public NumericProperty<DataGrid> rowHeight;
	public ToggleProperty<DataGrid> center;
	private Numeric margin;
	public ItProperty<DataGrid, Task> beforeFlip = new ItProperty<DataGrid, Task>(this);
	private boolean lockAppend = false;
	Column[] columnsArray = null;
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
	public DataGrid columns(Column[] indata) {
		columnsArray = indata;
		if (columnsArray.length < 1) {
			return this;
		}
		int left = 0;
		for (int x = 0; x < columnsArray.length; x++) {
			if (!noHead.property.value()) {
				Rake headerCell = columnsArray[x].header(getContext());
				headerCell.height().is(headerHeight.property.value());
				headerCell.width().is(columnsArray[x].width.property);
				headerCell.left().is(header.shiftX.property.plus(left).plus(margin));
				header.child(headerCell);
			}
			left = left + columnsArray[x].width.property.value().intValue();
		}
		header.innerWidth.is(left);
		reset();
		flip();
		reFitGrid();
		return this;
	}
	public void clearColumns() {
		for (int i = 0; i < columnsArray.length; i++) {
			columnsArray[i].clear();
		}
	}
	public void refresh() {
		flip();
		scrollView.scrollTo(0, 0);
	}
	private void flip() {
		//System.out.println("flip");
		currentPage = 0;
		append();
		//currentPage = 0;
	}
	private void append() {
		if (lockAppend) {
			//System.out.println("append locked");
			return;
		}
		if (columnsArray.length > 0) {
			lockAppend = true;
			//currentPage++;
			//System.out.println("append "+currentPage);
			scrollView.setOverScrollMode(OVER_SCROLL_NEVER);
			int start = (int) (currentPage * pageSize.property.value());
			for (int y = start; y < columnsArray[0].count() && y < (currentPage + 1) * pageSize.property.value(); y++) {
				TableRow tableRow;
				if (y < rows.size()) {
					tableRow = rows.get(y);
					tableRow.setVisibility(View.VISIBLE);
					for (int x = 0; x < columnsArray.length; x++) {
						this.columnsArray[x].update(y);
					}
				}
				else {
					tableRow = new TableRow(this.getContext());
					rows.add(tableRow);
					tableLayout.addView(tableRow);
					for (int x = 0; x < columnsArray.length; x++) {
						Rake r = columnsArray[x].item(x, y, getContext());
						r.height().is(rowHeight.property.value());
						r.width().is(columnsArray[x].width.property);
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
			for (int i = lastFilled; i < rowSize; i++) {
				rows.get(i).setVisibility(View.GONE);
			}
			lockAppend = false;
			scrollView.setOverScrollMode(OVER_SCROLL_IF_CONTENT_SCROLLS);
		}
	}
	void tapRow(int row, int column) {
		for (int x = 0; x < columnsArray.length; x++) {
			columnsArray[x].highlight(row);
		}
		if (column < columnsArray.length) {
			columnsArray[column].afterTap(row);
		}
	}
	private void flipNext() {
		double off = dataOffset.property.value() + pageSize.property.value() * (maxPageCount - 1);
		dataOffset.is(off);
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					clearColumns();
					beforeFlip.property.value().start();
				}
				catch (Throwable t) {
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void v) {
				//currentPage = 0;
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
	private void flipPrev() {
		double off = dataOffset.property.value() - pageSize.property.value() * (maxPageCount - 1);
		if (off < 0) {
			off = 0;
		}
		dataOffset.is(off);
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					clearColumns();
					beforeFlip.property.value().start();
				}
				catch (Throwable t) {
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void v) {
				//currentPage = 0;
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
	private void reFitGrid() {
		int left = 0;
		if (center.property.value()) {
			if (columnsArray != null) {
				for (int x = 0; x < columnsArray.length; x++) {
					left = left + columnsArray[x].width.property.value().intValue();
				}
			}
			margin.value((width().property.value() - left) / 2);
			if (margin.value() < 0) {
				margin.value(0);
			}
		}
		else {
			margin.value(0);
		}
		double hh = headerHeight.property.value();
		if (noHead.property.value()) {
			hh = 0;
		}
		if (scrollView != null) {
			int scrw = width().property.value().intValue();
			int scrh = (int) (height().property.value() - hh);
			int scrl = 0;
			int scrt = (int) hh;
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(scrw, scrh);
			params.topMargin = scrt;
			params.leftMargin = scrl;
			scrollView.setLayoutParams(params);
		}
		if (tableLayout != null) {
			FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) tableLayout.getLayoutParams();
			p.leftMargin = (int) (header.shiftX.property.value() + margin.value());
			tableLayout.setLayoutParams(p);
		}
	}
	public void exportCurrentDataCSV(Activity activity, String fileNameInDownloadFolder, String encoding) {//encoding=windows-1251,utf-8
		try {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (int i = 0; i < this.columnsArray.length; i++) {
				if (!first) {
					sb.append(",");
				}
				else {
					first = false;
				}
				sb.append(columnsArray[i].title.property.value());
			}
			sb.append("\n");
			if (columnsArray.length > 0) {
				for (int r = 0; r < columnsArray[0].count(); r++) {
					first = true;
					for (int i = 0; i < this.columnsArray.length; i++) {
						if (!first) {
							sb.append(",");
						}
						else {
							first = false;
						}
						sb.append(columnsArray[i].export(r));
					}
					sb.append("\n");
				}
			}
			String f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + fileNameInDownloadFolder;
			File file = new File(f);
			Auxiliary.writeTextToFile(file, sb.toString(), "windows-1251");
			Auxiliary.inform(file.getAbsolutePath(), activity);
			Auxiliary.startFile(activity, android.content.Intent.ACTION_VIEW, "text/plain", file);
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}
	@Override
	protected void init() {
		super.init();
		if (!initialized) {
			initialized = true;
			noHead = new ToggleProperty<DataGrid>(this);
			center = new ToggleProperty<DataGrid>(this);
			margin = new Numeric();
			/*Task reFit = new Task() {
				@Override
				public void doTask() {
					refit();
				}
			};*/
			pageSize = new NumericProperty<DataGrid>(this);
			pageSize.is(33);
			dataOffset = new NumericProperty<DataGrid>(this);
			dataOffset.is(0);
			headerHeight = new NumericProperty<DataGrid>(this);
			headerHeight.is(Auxiliary.tapSize);
			header = new SubLayoutless(this.getContext());
			header.width().is(width().property);
			header.height().is(headerHeight.property);
			this.child(header);
			rowHeight = new NumericProperty<DataGrid>(this);
			rowHeight.is(Auxiliary.tapSize);
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
							int columnsWidth = 0;
							if (columnsArray != null) {
								for (int x = 0; x < columnsArray.length; x++) {
									columnsWidth = columnsWidth + columnsArray[x].width.property.value().intValue();
								}
							}
							float aX = event.getX();
							float aY = event.getY();
							if (aX > margin.value() && aX < margin.value() + columnsWidth) {
								//System.out.println(event.getX()+" / "+margin.value());
								float diff = 4;
								if (Math.abs(initialX - aX) < diff && Math.abs(initialY - aY) < diff) {
									int nn = (int) ((this.getScrollY() + aY) / rowHeight.property.value());
									double xx = 0;
									for (int i = 0; i < columnsArray.length; i++) {
										xx = xx + columnsArray[i].width.property.value();
										if (xx > aX - margin.value()) {
											tapRow(nn, i);
											break;
										}
									}
								}
							}
						}
					}
					return super.onTouchEvent(event);
				}
				@Override
				protected void onScrollChanged(int left, int top, int oldLeft, int oldTop) {
					if (progressBar.getVisibility() == View.VISIBLE) {
						return;
					}
					super.onScrollChanged(left, top, oldLeft, oldTop);
					progressBar.setVisibility(View.VISIBLE);
					progressBar.postInvalidate();
					double scrollViewHeight = height().property.value() - headerHeight.property.value();
					if (noHead.property.value()) {
						scrollViewHeight = height().property.value();
					}
					double contentHeight = rowHeight.property.value() * (currentPage + 1) * pageSize.property.value();
					//double contentHeight = rowHeight.property.value() * currentPage * pageSize.property.value();
					double limit = contentHeight - scrollViewHeight;
					//System.out.println(top +" / "+ limit +" - "+currentPage);
					if (top > 0 && limit > 0 && top >= limit) {
						//System.out.println("currentPage "+currentPage);
						if (currentPage < maxPageCount - 1) {
							currentPage++;
							append();
							progressBar.setVisibility(View.INVISIBLE);
						}
						else {
							//System.out.println("next");
							if (beforeFlip.property.value() != null) {
								flipNext();
							}
							else {
								progressBar.setVisibility(View.INVISIBLE);
							}
						}
					}
					else {
						if (top <= 0) {
							if (dataOffset.property.value() > 0) {
								if (beforeFlip.property.value() != null) {
									flipPrev();
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
			new Numeric().bind(width().property).afterChange(new Task() {
				@Override
				public void doTask() {
					reFitGrid();
				}
			});
			new Numeric().bind(height().property).afterChange(new Task() {
				@Override
				public void doTask() {
					reFitGrid();
				}
			});
			progressBar = new ProgressBar(this.getContext(), null, android.R.attr.progressBarStyleLarge);
			this.addView(progressBar);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
					(int) (0.5 * Auxiliary.tapSize)//
					, (int) (0.5 * Auxiliary.tapSize));
			progressBar.setLayoutParams(params);
			progressBar.setVisibility(View.INVISIBLE);
			new Numeric().bind(header.shiftX.property).afterChange(new Task() {
				@Override
				public void doTask() {
					reFitGrid();
				}
			});
		}
	}
}
