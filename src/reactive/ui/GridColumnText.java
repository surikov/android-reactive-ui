package reactive.ui;

import android.content.*;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import java.util.*;

import tee.binding.it.Numeric;
import tee.binding.properties.NumericProperty;
import tee.binding.task.*;

public class GridColumnText extends GridColumn {
	protected Vector<String> strings = new Vector<String>();
	protected Vector<Task> tasks = new Vector<Task>();
	protected Vector<Decor> cells = new Vector<Decor>();
	protected Vector<Integer> backgrounds = new Vector<Integer>();
	protected Paint linePaint = new Paint();
	protected Rect sz;
	int presell = -1;
	public NumericProperty<GridColumnText> headerBackground = new NumericProperty<GridColumnText>(this);

	@Override
	public void update(int row) {
		if (row >= 0 && row < cells.size()) {
			Decor cell = cells.get(row);
			if (row > -1 && row < backgrounds.size()) {
				if (backgrounds.get(row) != null) {
					cell.background.is(backgrounds.get(row));
				}
				else {
					cell.background.is(null);
				}
			}
			if (row > -1 && row < strings.size()) {
				cell.labelText.is(strings.get(row));
			}
			else {
				cell.labelText.is("");
			}
		}
	}
	@Override
	public Rake item(final int column, int row, Context context) {
		linePaint.setColor((int) (Layoutless
		//.themeForegroundColor));
				.themeBlurColor));
		Decor cell = new Decor(context, true) {
			//
			@Override
			protected void onDraw(Canvas canvas) {
				super.onDraw(canvas);
				if (sz == null) {
					sz = new Rect();
				}
				//linePaint.setStrokeWidth(11);
				//linePaint.setColor(0xff6600ff);
				if (!noVerticalBorder.property.value()) {
					if (column > 0) {
						sz.left = 0;
						sz.top = 0;
						sz.right = 1;
						sz.bottom = height().property.value().intValue();
						canvas.drawRect(sz, linePaint);//left
					}
				}
				if (!noHorizontalBorder.property.value()) {
					sz.left = 0;
					sz.top = height().property.value().intValue() - 1;
					sz.right = width().property.value().intValue();
					sz.bottom = height().property.value().intValue();
					canvas.drawRect(sz, linePaint);//under
				}
			}
		};
		if (row > -1 && row < backgrounds.size()) {
			if (backgrounds.get(row) != null) {
				cell.background.is(backgrounds.get(row));
			}
		}
		cell.setPadding(3, 0, 3, 0);
		cell.labelStyleMediumNormal();
		if (row > -1 && row < strings.size()) {
			cell.labelText.is(strings.get(row));
		}
		cells.add(cell);
		return cell;
	}
	public GridColumnText cell(String s, Integer background, Task tap) {
		strings.add(s);
		tasks.add(tap);
		backgrounds.add(background);
		return this;
	}
	public GridColumnText cell(String s) {
		return cell(s, null, null);
	}
	public GridColumnText cell(String s, Task tap) {
		return cell(s, null, tap);
	}
	public GridColumnText cell(String s, Integer background) {
		return cell(s, background, null);
	}
	@Override
	public int count() {
		return strings.size();
	}
	public GridColumnText() {
		this.width.is(150);
		//linePaint.setColor(0x33666666);
		/*int c1 = 0xffff0000;
		int transp=c1 & 0xff000000;
		transp=transp/32;
		transp=transp & 0xff000000;
		transp=0x33000000;
		int pure=c1 & 0x00ffffff;
		c1=transp+pure;*/
		linePaint.setAntiAlias(true);
		linePaint.setFilterBitmap(true);
		linePaint.setDither(true);
		//linePaint.setStrokeWidth(0);
		//linePaint.setst
		//linePaint.setStyle(Style.STROKE);
	}
	@Override
	public Rake header(Context context) {
		//Knob k = new Knob(context).labelText.is(title.property.value());
		Decor header = new Decor(context) {
			//
			@Override
			protected void onDraw(Canvas canvas) {
				super.onDraw(canvas);
				canvas.drawRect(new Rect(//under
						0//
						, height().property.value().intValue() - 1//
						, width().property.value().intValue()//
						, height().property.value().intValue() //
						), linePaint);
			}
		};
		header.setPadding(3, 0, 3, 2);
		header.labelStyleSmallNormal();
		header.labelAlignCenterBottom();
		header.labelText.is(title.property.value());
		return header;
	}
	@Override
	public void clear() {
		strings.removeAllElements();
		backgrounds.removeAllElements();
		tasks.removeAllElements();
		//cells.removeAllElements();
	}
	@Override
	public void afterTap(int row) {
		if (row > -1 && row < tasks.size()) {
			if (tasks.get(row) != null) {
				tasks.get(row).start();
			}
			//System.out.println("label "+strings.get(row));
		}
	}
	@Override
	public void highlight(int row) {
		if (presell >= 0 && presell < cells.size()) {
			if (presell >= 0 && presell < backgrounds.size()) {
				//System.out.println(cells.get(presell));
				//int b=backgrounds.get(presell);
				//System.out.println(backgrounds.get(presell));
				//System.out.println(backgrounds.get(presell));
				if (backgrounds.get(presell) != null) {
					cells.get(presell).background.is(backgrounds.get(presell));
				}
				else {
					cells.get(presell).background.is(0);
				}
			}
		}
		if (row >= 0 && row < cells.size()) {
			presell = row;
			cells.get(row).background.is(Layoutless.themeBlurColor);
		}
	}
}
