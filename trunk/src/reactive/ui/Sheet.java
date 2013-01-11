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

public class Sheet extends SubLayoutless {
	private boolean initialized = false;
	private SubLayoutless data;
	private SubLayoutless body;
	private SubLayoutless header;
	private Decor selection;
	private SheetColumn[] columns;
	public NumericProperty<Sheet> rowHeight;
	public NumericProperty<Sheet> maxRowHeight;
	private NumericProperty<Sheet> headerHeight;
	public NumericProperty<Sheet> selectedRow;
	Numeric rowCount;
	Numeric columnCount;

	/*
	Task reWidth = new Task() {
		@Override
		public void doTask() {
			System.out.println("reWidth");
			int nData = 1;
			int nHeader = 0;
			int curLeft = 0;
			if (columns == null) {
				return;
			}
			columnCount.value(columns.length);
			System.out.println("count: " + data.count());
			if (columnCount.value() > 0) {
				for (int x = 0; x < columnCount.value(); x++) {
					System.out.println("x: " + x);
					if (x > 0) {
						System.out.println("nn: " + nData);
						data.child(nData)//vertical line
								.left().is(curLeft);
						System.out.println("vertical line: " + data.child(nData).left().property.value());
						nData++;
					}
					System.out.println("nn: " + nData);
					data.child(nHeader)//header
							.left().is(header.shiftX.property.plus(curLeft))//
							//.top().is(2)//
							.width().is(columns[x].width.property.value());
					nHeader++;
					System.out.println("header: " //+data.child(nn).getClass().getCanonicalName()+": "
							+ data.child(nData).width().property.value());
					for (int y = 0; y < rowCount.value(); y++) {
						System.out.println("nn: " + nData);
						data.child(nData)//cell
								.left().is(curLeft)//
								.width().is(columns[x].width.property.value());
						//System.out.println("cell: " + data.child(nn).width().property.value());
						nData++;
					}
					curLeft = curLeft + columns[x].width.property.value().intValue();
				}
				for (int y = 0; y < rowCount.value(); y++) {
					System.out.println("y: " + y);
					if (y > 0) {
						System.out.println("nn: " + nData);
						data.child(nData)//horizontal line
								.width().is(curLeft);
						System.out.println("horizontal: " + data.child(nData).width().property.value() + " / " + data.child(nData).top().property.value());
						nData++;
					}
				}
				data.width().is(curLeft);
				header.width().is(curLeft);
			}
		}
	};
	*/
	private void fill() {
		if (columns == null) {
			return;
		}
		columnCount.value(columns.length);
		if (columnCount.value() > 0) {
			rowCount.value(columns[0].count());
			//int columnWidth = 120;
			int curLeft = 0;
			//int rowHeight = 50;
			//System.out.println(columnCount+"x"+rowCount);
			for (int x = 0; x < columnCount.value(); x++) {
				if (x > 0) {
					data.child(new Decor(this.getContext())//vertical line
					.background.is(Layoutless.themeBlurColor)//
							.width().is(1)//
							.height().is(rowHeight.property.multiply(rowCount))//
							.left().is(curLeft)//
					);
				}
				//columns[x].width.property.afterChange(reWidth);
				header.child(columns[x].header(this.getContext())//
						.left().is(header.shiftX.property.plus(curLeft))//
						//.top().is(2)//
						.height().is(headerHeight.property)//
						.width().is(columns[x].width.property.value())//
				//
				);
				for (int y = 0; y < rowCount.value(); y++) {
					//System.out.println(x+"x"+y+": ");
					data.child(columns[x].cell(y, this.getContext())//
							.left().is(curLeft)//
							.top().is(rowHeight.property.multiply(y))//
							.height().is(rowHeight.property)//
							.width().is(columns[x].width.property.value())//
					);
				}
				curLeft = curLeft + columns[x].width.property.value().intValue();
			}
			for (int y = 0; y < rowCount.value(); y++) {
				if (y > 0) {
					data.child(new Decor(this.getContext())//horizontal line
					.background.is(Layoutless.themeBlurColor)//
							.width().is(curLeft)//
							.height().is(1)//
							.top().is(rowHeight.property.multiply(y))//
					);
				}
			}
			data.width().is(curLeft);
			body.innerWidth.is(curLeft);
			header.innerWidth.is(curLeft);
			//reWidth.start();
			this.postInvalidate();
			setZoom();
		}
	}
	//Numeric dataWidth;
	public Sheet(Context context) {
		super(context);
	}
	public Sheet(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public Sheet(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	private void clear() {
		data.removeAllViews();
		header.removeAllViews();
		//columns.removeAllElements();
		data.child(selection);
		//body.innerHeight.property.unbind();
		//body.innerWidth.property.unbind();
		//fill();
	}
	public void refreshSelection() {
		//System.out.println("row " + selectedRow.property.value());
		if (selectedRow.property.value() < 0) {
			selection.setVisibility(INVISIBLE);
		}
		else {
			selection.height().is(rowHeight.property.value());
			selection.top().is(selectedRow.property.value() * rowHeight.property.value());
			selection.width().is(data.width().property.value());
			//selection.top().is(selectedRow.property.multiply(rowHeight.property));
			/*System.out.println("selection " + selection.width().property.value()
					+" / "+ selection.height().property.value()
					+" / "+ selection.left().property.value()
					+" / "+ selection.top().property.value()
					);
			System.out.println("selectedRow " + selectedRow.property.value()
					+" / rowHeight "+ rowHeight.property.value()					
					);*/
			selection.setVisibility(VISIBLE);
		}
	}
	/*
		public Sheet column(SheetColumn c) {
		columns.add(c);
		return this;
		}*/
	public void data(SheetColumn[] data) {
		//this.clear();
		this.columns = data;
		//this.fill();
		//return this;
	}
	public void reset() {
		this.clear();
		this.fill();
	}
	@Override
	protected void init() {
		super.init();
		if (!initialized) {
			initialized = true;
			//System.out.println("go----------------------------------");
			rowCount = new Numeric();
			columnCount = new Numeric();
			//dataWidth=new Numeric();
			selection = new Decor(this.getContext()).background.is(0x66999999);
			selection.setVisibility(INVISIBLE);
			selectedRow = new NumericProperty<Sheet>(this);
			selectedRow.is(-1);
			rowHeight = new NumericProperty<Sheet>(this);
			maxRowHeight = new NumericProperty<Sheet>(this);
			headerHeight = new NumericProperty<Sheet>(this);
			rowHeight.is(Layoutless.tapSize);
			headerHeight.is(Layoutless.tapSize);
			header = new SubLayoutless(this.getContext());
			header.height().is(headerHeight.property);
			header.width().is(this.width().property);
			this.child(header);
			body = new SubLayoutless(this.getContext());
			this.child(body);
			//body.solid.is(false);
			body.top().is(headerHeight.property);
			body.width().is(this.width().property);
			body.height().is(this.height().property.minus(headerHeight.property));
			data = new SubLayoutless(this.getContext());
			data.child(selection);
			body.child(data);
			data.left().is(body.shiftX.property);
			data.top().is(body.shiftY.property);
			data.solid.is(false);
			body.shiftX.property.bind(header.shiftX.property);
			body.afterTap.is(new Task() {
				@Override
				public void doTask() {
					//int xx = 0;
					//int yy = 0;
					if (columns == null)
						return;
					if (columns.length > 0) {
						if (columns[0].count() > 0) {
							if (body.tapX.property.value() >= 0) {
								if (body.tapY.property.value() >= 0) {
									int columnCount = columns.length;
									int curLeft = 0;
									int tc = -1;
									for (int c = 0; c < columnCount; c++) {
										curLeft = curLeft + columns[c].width.property.value().intValue();
										if (body.tapX.property.value() <= curLeft) {
											//System.out.println("column " + c);
											tc = c;
											break;
										}
									}
									//int rowCount = columns.get(0).count();
									int r = (int) ((body.tapY.property.value() - body.shiftY.property.value()) / rowHeight.property.value());
									selectedRow.is(r);
									refreshSelection();
									//postInvalidate();
									if (tc > -1) {
										if (columns.length > tc) {
											columns[tc].afterTap(r);
											/*if (columns[tc].afterCellTap.property.value() != null) {
												columns[tc].afterCellTap.property.value().start();
											}*/
										}
									}
								}
							}
						}
					}
				}
			});
			header.afterTap.is(new Task() {
				@Override
				public void doTask() {
					if (columns == null)
						return;
					if (header.tapX.property.value() >= 0) {
						int columnCount = columns.length;
						int curLeft = 0;
						for (int x = 0; x < columnCount; x++) {
							curLeft = curLeft + columns[x].width.property.value().intValue();
							if (header.tapX.property.value() <= curLeft) {
								if (columns[x].afterHeaderTap.property.value() != null) {
									columns[x].afterHeaderTap.property.value().start();
								}
								//System.out.println("header " + x);
								break;
							}
						}
					}
				}
			});
			body.maxZoom.is(maxRowHeight.property);
			body.afterZoom.is(new Task() {
				@Override
				public void doTask() {
					//System.out.println("zoom " + body.zoom.property.value());
					//rowHeight.property.bind(body.zoom.property.multiply(Layoutless.tapSize));
					setZoom();
				}
			});
			data.height().is(rowHeight.property.multiply(rowCount));
			body.innerHeight.is(rowHeight.property.multiply(rowCount));
			//body.innerWidth.is(data.width().property);
			//header.innerWidth.is(data.width().property);
		}
	}
	void setZoom() {
		if (maxRowHeight.property.value() > body.zoom.property.value() && body.zoom.property.value() >= 0) {
			rowHeight.is((1 + body.zoom.property.value()) * Layoutless.tapSize);
			refreshSelection();
		}
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
}
