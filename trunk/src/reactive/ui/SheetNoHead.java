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

public class SheetNoHead extends SubLayoutless {
	private boolean initialized = false;
	//private SubLayoutless data;
	private Decor data;
	private SubLayoutless body;
	private Decor selection;
	private SheetColumn[] columns;
	public NumericProperty<SheetNoHead> rowHeight;
	public NumericProperty<SheetNoHead> maxRowHeight;
	public NumericProperty<SheetNoHead> selectedRow;
	Numeric rowCount;
	Numeric columnCount;

	private void fill() {
		if (columns == null) {
			return;
		}
		columnCount.value(columns.length);
		if (columnCount.value() > 0) {
			rowCount.value(columns[0].count());
			int curLeft = 0;
			for (int x = 0; x < columnCount.value(); x++) {
				if (x > 0) {
					/*data.child(new Decor(this.getContext())//vertical line
					.background.is(Layoutless.themeBlurColor)//
							.width().is(1)//
							.height().is(rowHeight.property.multiply(rowCount))//
							.left().is(curLeft)//
					);*/
					data.sketch(new SketchPlate()//vertical line
					.background.is(Layoutless.themeBlurColor)//
					.width.is(1)//
					.top.is(0)//
					.height.is(rowHeight.property.multiply(rowCount))//
					.left.is(curLeft)//
					);
				}
				for (int y = 0; y < rowCount.value(); y++) {
					/*data.child(columns[x].cell(y, this.getContext())//
							.left().is(curLeft)//
							.top().is(rowHeight.property.multiply(y))//
							.height().is(rowHeight.property)//
							.width().is(columns[x].width.property.value())//
					);*/
					data.sketch(columns[x].cell(y)//cell
														.width.is(columns[x].width.property.value() - 6)//
							.height.is(rowHeight.property.minus(6))//
							.top.is(rowHeight.property.multiply(y).plus(3))//
							.left.is(curLeft + 3) //
							);
				}
				curLeft = curLeft + columns[x].width.property.value().intValue();
			}
			for (int y = 0; y < rowCount.value(); y++) {
				if (y > 0) {
					/*data.child(new Decor(this.getContext())//horizontal line
					.background.is(Layoutless.themeBlurColor)//
							.width().is(curLeft)//
							.height().is(1)//
							.top().is(rowHeight.property.multiply(y))//
					);*/
					data.sketch(new SketchPlate()//horizontal line
					.background.is(Layoutless.themeBlurColor)//
					.width.is(curLeft)//
					.height.is(1)//
					.top.is(rowHeight.property.multiply(y))//
					);
				}
			}
			data.width().is(curLeft);
			body.innerWidth.is(curLeft);
			this.postInvalidate();
			setZoom();
		}
	}
	public SheetNoHead(Context context) {
		super(context);
	}
	public SheetNoHead(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public SheetNoHead(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	private void clear() {
		//data.removeAllViews();
		//data.child(selection);
		data.clear();
	}
	public void refreshSelection() {
		if (selectedRow.property.value() < 0) {
			selection.setVisibility(INVISIBLE);
		}
		else {
			//selection.height().is(rowHeight.property.value());
			//selection.top().is(selectedRow.property.value() * rowHeight.property.value());
			//selection.width().is(data.width().property.value());
			selection.setVisibility(VISIBLE);
		}
	}
	public void data(SheetColumn[] data) {
		this.columns = data;
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
			rowCount = new Numeric();
			columnCount = new Numeric();
			selection = new Decor(this.getContext()).background.is(0x66999999);
			selection.setVisibility(INVISIBLE);
			selectedRow = new NumericProperty<SheetNoHead>(this);
			selectedRow.is(-1);
			rowHeight = new NumericProperty<SheetNoHead>(this);
			maxRowHeight = new NumericProperty<SheetNoHead>(this);
			rowHeight.is(Layoutless.tapSize);
			body = new SubLayoutless(this.getContext());
			this.child(body);
			body.width().is(this.width().property);
			body.height().is(this.height().property);
			//data = new SubLayoutless(this.getContext());
			data = new Decor(this.getContext());
			//data.child(selection);
			body.child(selection);
			selection.width().is(data.width().property);
			selection.height().is(rowHeight.property);
			selection.left().is(body.shiftX.property);
			selection.top().is(selectedRow.property.multiply(rowHeight.property).plus(body.shiftY.property));
			body.child(data);
			data.left().is(body.shiftX.property);
			data.top().is(body.shiftY.property);
			//data.solid.is(false);
			body.shiftX.property.bind(body.shiftX.property);
			body.afterTap.is(new Task() {
				@Override
				public void doTask() {
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
											tc = c;
											break;
										}
									}
									int r = (int) ((body.tapY.property.value() - body.shiftY.property.value()) / rowHeight.property.value());
									selectedRow.is(r);
									refreshSelection();
									if (tc > -1) {
										if (columns.length > tc) {
											columns[tc].afterTap(r);
										}
									}
								}
							}
						}
					}
				}
			});			
			body.maxZoom.is(maxRowHeight.property);
			body.afterZoom.is(new Task() {
				@Override
				public void doTask() {
					setZoom();
				}
			});
			data.height().is(rowHeight.property.multiply(rowCount));
			body.innerHeight.is(rowHeight.property.multiply(rowCount));
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
