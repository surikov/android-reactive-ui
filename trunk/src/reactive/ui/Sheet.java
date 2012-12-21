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
import reactive.ui.library.views.SimpleButton;

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
	Vector<SheetColumn> columns = new Vector<SheetColumn>();
	public NumericProperty<Sheet> rowHeight;
	public NumericProperty<Sheet> headerHeight;
	public NumericProperty<Sheet> selectedRow;
	Numeric rowCount;
	Numeric columnCount;
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
	public void clear() {
		data.removeAllViewsInLayout();
		header.removeAllViews();
		columns.removeAllElements();
		//body.innerHeight.property.unbind();
		//body.innerWidth.property.unbind();
		fill();
	}
	void refreshSelection() {
		if (selectedRow.property.value() < 0) {
			selection.setVisibility(INVISIBLE);
		}
		else {
			//System.out.println("selection " + selectedRow.property.value()*rowHeight.property.value());
			selection.setVisibility(VISIBLE);
		}
	}
	public void fill() {
		 columnCount.value( columns.size());
		if (columnCount.value() > 0) {
			 rowCount.value(columns.get(0).count());
			//int columnWidth = 120;
			int curLeft = 0;
			//int rowHeight = 50;
			//System.out.println(columnCount+"x"+rowCount);
			for (int x = 0; x < columnCount.value(); x++) {
				if (x > 0) {
					data.child(new Decor(this.getContext())//
					.background.is(Layoutless.themeBlurColor)//
							.width().is(1)//
							.height().is(rowHeight.property.multiply(rowCount))//
							.left().is(curLeft)//
							.view());
				}
				header.child(columns.get(x).header(this.getContext())//
						.left().is(header.shiftX.property.plus(curLeft))//
						//.top().is(2)//
						.height().is(headerHeight.property.value())//
						.width().is(columns.get(x).width.property.value())//
						.view()//
				);
				for (int y = 0; y < rowCount.value(); y++) {
					//System.out.println(x+"x"+y+": ");
					data.child(columns.get(x).cell(y, this.getContext())//
							.left().is(curLeft)//
							.top().is(rowHeight.property.multiply(y))//
							.height().is(rowHeight.property)//
							.width().is(columns.get(x).width.property.value())//
							.view());
				}
				curLeft = curLeft + columns.get(x).width.property.value().intValue();
			}
			for (int y = 0; y < rowCount.value(); y++) {
				if (y > 0) {
					data.child(new Decor(this.getContext())//
					.background.is(Layoutless.themeBlurColor)//
							.width().is(curLeft)//
							.height().is(1)//
							.top().is(rowHeight.property.multiply(y))//
							.view());
				}
			}
			data.width().is(curLeft);
			
			this.postInvalidate();
			setZoom();
		}
	}
	public Sheet column(SheetColumn c) {
		columns.add(c);
		return this;
	}
	@Override
	protected void init() {
		super.init();
		if (!initialized) {
			initialized = true;
			 rowCount=new Numeric();
			 columnCount=new Numeric();
			//dataWidth=new Numeric();
			selection = new Decor(this.getContext()).background.is(0x66999999);
			selection.setVisibility(INVISIBLE);
			selectedRow = new NumericProperty<Sheet>(this);
			selectedRow.is(-1);
			rowHeight = new NumericProperty<Sheet>(this);
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
					int xx = 0;
					int yy = 0;
					if (columns.size() > 0) {
						if (columns.get(0).count() > 0) {
							if (body.tapX.property.value() >= 0) {
								if (body.tapY.property.value() >= 0) {
									int columnCount = columns.size();
									int curLeft = 0;
									int tc = -1;
									for (int c = 0; c < columnCount; c++) {
										curLeft = curLeft + columns.get(c).width.property.value().intValue();
										if (body.tapX.property.value() <= curLeft) {
											//System.out.println("column " + c);
											tc = c;
											break;
										}
									}
									//int rowCount = columns.get(0).count();
									int r = (int) ((body.tapY.property.value() - body.shiftY.property.value()) / rowHeight.property.value());
									//System.out.println("row " + r);
									selectedRow.is(r);
									refreshSelection();
									if (tc > -1) {
										if (columns.size() > tc) {
											if (columns.get(tc).afterCellTap.property.value() != null) {
												columns.get(tc).afterCellTap.property.value().start();
											}
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
					if (header.tapX.property.value() >= 0) {
						int columnCount = columns.size();
						int curLeft = 0;
						for (int x = 0; x < columnCount; x++) {
							curLeft = curLeft + columns.get(x).width.property.value().intValue();
							if (header.tapX.property.value() <= curLeft) {
								if (columns.get(x).afterHeaderTap.property.value() != null) {
									columns.get(x).afterHeaderTap.property.value().start();
								}
								//System.out.println("header " + x);
								break;
							}
						}
					}
				}
			});
			body.maxZoom.is(3);
			body.afterZoom.is(new Task() {
				@Override
				public void doTask() {
					System.out.println("zoom " + body.zoom.property.value());
					//rowHeight.property.bind(body.zoom.property.multiply(Layoutless.tapSize));
					setZoom();
				}
			});
			selection.height().is(rowHeight.property);
			selection.top().is(selectedRow.property.multiply(rowHeight.property));
			
data.height().is(rowHeight.property.multiply(rowCount));
			
			body.innerHeight.is(rowHeight.property.multiply(rowCount));
			body.innerWidth.is(data.width().property);
			header.innerWidth.is(data.width().property);
			selection.width().is(data.width().property);
		}
	}
	void setZoom() {
		rowHeight.is((1 + body.zoom.property.value()) * Layoutless.tapSize);
	}
}
