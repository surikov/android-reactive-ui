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
	//private SubLayoutless data;
	private Decor data;
	private SubLayoutless body;
	private SubLayoutless header;
	private Decor selection;
	private SheetColumn[] columns;
	public NumericProperty<Sheet> rowHeight;
	public NumericProperty<Sheet> scroll;
	public NumericProperty<Sheet> maxRowHeight;
	public NumericProperty<Sheet> headerHeight;
	public NumericProperty<Sheet> selectedRow;
	public ToggleProperty<Sheet> noHead;
	public ItProperty<Sheet, Task> afterScroll = new ItProperty<Sheet, Task>(this);
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
				header.child(columns[x].header(this.getContext())//
						.left().is(header.shiftX.property.plus(curLeft))//
						.height().is(headerHeight.property)//
						.width().is(columns[x].width.property.value())//
				);
				for (int y = 0; y < rowCount.value(); y++) {
					/*data.child(columns[x].cell(y, this.getContext())//
							.left().is(curLeft)//
							.top().is(rowHeight.property.multiply(y))//
							.height().is(rowHeight.property)//
							.width().is(columns[x].width.property.value())//
					);*/
					//System.out.println(x+"x"+y+"/"+curLeft+"/"+rowHeight.property.multiply(y).value()+"/"+columns[x].width.property.value()+"/"+rowHeight.property.value());
					data.sketch(columns[x].cell(y)//cell
					//data.sketch(new SketchText()//cell
					//.size.is(16).text.is("x" + Math.random())//
					.width.is(columns[x].width.property.value())//
					.height.is(rowHeight.property)//
					.top.is(rowHeight.property.multiply(y))//
					.left.is(curLeft) //
					);
					/*data.sketch(new SketchPlate()//fill
					.background.is(0x3300ff00)//
					.width.is(columns[x].width.property.value()-9)//
					.height.is(rowHeight.property.value()-9)//
					.top.is(rowHeight.property.multiply(y))//
					.left.is(curLeft) //
					);*/
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
			header.innerWidth.is(curLeft);
			this.postInvalidate();
			setZoom();
		}
		this.refreshSelection();
	}
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
		//data.removeAllViews();
		data.clear();
		header.removeAllViews();
		//data.child(selection);
	}
	public void refreshSelection() {
		if (columns.length > 0) {
			if (columns[0].count() <= selectedRow.property.value()) {
				selectedRow.is(-1);
			}
		}
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
	public Sheet data(SheetColumn[] data) {
		this.columns = data;
		this.reset();
		return this;
	}
	public void reset() {
		this.clear();
		this.fill();
		//resetYScroll();
	}
	@Override
	protected void init() {
		super.init();
		if (!initialized) {
			initialized = true;
			rowCount = new Numeric();
			columnCount = new Numeric();
			noHead = new ToggleProperty<Sheet>(this);
			selection = new Decor(this.getContext()).background.is(0x66999999);
			selection.setVisibility(INVISIBLE);
			selectedRow = new NumericProperty<Sheet>(this);
			selectedRow.is(-1);
			rowHeight = new NumericProperty<Sheet>(this);
			scroll = new NumericProperty<Sheet>(this);
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
			body.top().is(headerHeight.property);
			body.width().is(this.width().property);
			body.height().is(this.height().property.minus(headerHeight.property));
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
			/*this.child(new Decor(this.getContext()).sketch(new SketchText()//cell
					.size.is(50).text.is("x" + Math.random())//
					.width.is(1000)//columns[x].width.property.value())//
					.height.is(1000)//rowHeight.property)//
					.top.is(100)//rowHeight.property.multiply(y))//
					.left.is(100)//curLeft) //
					)
					.width().is(1000)//columns[x].width.property.value())//
					.height().is(1000)//rowHeight.property)//
					.top().is(0)//rowHeight.property.multiply(y))//
					.left().is(0)//curLeft) //
					);*/
			//data.solid.is(false);
			body.shiftX.property.bind(header.shiftX.property);
			body.afterTap.is(new Task() {
				@Override
				public void doTask() {
					if (columns == null)
						return;
					if (columns.length > 0) {
						if (columns[0].count() > 0) {
							double bodyX = body.tapX.property.value() - body.shiftX.property.value();
							if (bodyX >= 0) {
								if (body.tapY.property.value() >= 0) {
									int columnCount = columns.length;
									int curLeft = 0;
									int tapColumn = -1;
									for (int c = 0; c < columnCount; c++) {
										curLeft = curLeft + columns[c].width.property.value().intValue();
										if (bodyX <= curLeft) {
											tapColumn = c;
											break;
										}
									}
									int r = (int) ((body.tapY.property.value() - body.shiftY.property.value()) / rowHeight.property.value());
									selectedRow.is(r);
									refreshSelection();
									if (tapColumn > -1) {
										if (columns.length > tapColumn) {
											columns[tapColumn].afterTap(r);
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
								break;
							}
						}
					}
				}
			});
			//noHead
			body.maxZoom.is(maxRowHeight.property);
			body.afterZoom.is(new Task() {
				@Override
				public void doTask() {
					setZoom();
				}
			});
			data.height().is(rowHeight.property.multiply(rowCount));
			body.innerHeight.is(rowHeight.property.multiply(rowCount));
			noHead.property.afterChange(new Task() {
				@Override
				public void doTask() {
					if (noHead.property.value()) {
						header.setVisibility(View.INVISIBLE);
						body.top().property.unbind();
						body.height().property.unbind();
						body.top().is(0);
						body.height().is(Sheet.this.height().property);
					}
					else {
						header.setVisibility(View.VISIBLE);
						body.top().property.unbind();
						body.height().property.unbind();
						body.top().is(headerHeight.property);
						body.height().is(Sheet.this.height().property.minus(headerHeight.property));
					}
				}
			});
		}
		body.afterShift.is(new Task() {
			@Override
			public void doTask() {
				double rowH = rowHeight.property.value();
				if (rowH < 1) {
					rowH = 1.0;
				}
				double step = (body.lastShiftY.property.value() - body.shiftY.property.value()) / rowH;
				//System.out.println(rowH+": "+step+" / "+Math.round(step)+", "+body.lastShiftY.property.value() +"/"+ body.shiftY.property.value());
				//if(step>0){
				//	step--;
				//}
				scroll.property.value((double) Math.round(step));
				if (afterScroll.property.value() == null) {
					body.shiftY.property.value(body.lastShiftY.property.value());
				}
				else {
					afterScroll.property.value().start();
				}
				//body.shiftY.property.value(body.lastShiftY.property.value());
				body.shiftX.property.value(body.lastShiftX.property.value());
			}
		});
	}
	public void resetYScroll() {
		body.shiftY.property.value(body.lastShiftY.property.value());
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
		this.clear();
	}
}
