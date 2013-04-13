package reactive.ui;

import java.util.Date;
import java.util.Vector;
import tee.binding.properties.*;
import tee.binding.task.Task;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import java.text.*;


public class ColumnSketch extends Column {
	public Vector<Sketch> sketches = new Vector<Sketch>();
	public Vector<Task> tasks = new Vector<Task>();
	public Vector<Decor> cells = new Vector<Decor>();
	public Vector<Integer> backgrounds = new Vector<Integer>();
	//protected Paint linePaint = new Paint();
	protected Rect sz;
	int presell = -1;
	public NumericProperty<ColumnSketch> headerBackground = new NumericProperty<ColumnSketch>(this);
	@Override
	public void afterTap(int row) {
		if (row > -1 && row < tasks.size()) {
			if (tasks.get(row) != null) {
				tasks.get(row).start();
			}
		}
	}
	public ColumnSketch cell(Sketch s, Integer background, Task tap) {
		sketches.add(s);
		tasks.add(tap);
		backgrounds.add(background);
		return this;
	}
	public ColumnSketch cell(Sketch s) {
		return cell(s, null, null);
	}
	public ColumnSketch cell(Sketch s, Task tap) {
		return cell(s, null, tap);
	}
	public ColumnSketch cell(Sketch s, Integer background) {
		return cell(s, background, null);
	}
	@Override
	public Rake item(final int column, int row, Context context) {
		Decor cell = new Decor(context, true) {
			//
			@Override
			protected void onDraw(Canvas canvas) {
				super.onDraw(canvas);
				if (sz == null) {
					sz = new Rect();
				}
				if (!noVerticalBorder.property.value()) {
					if (column > 0) {
						sz.left = 0;
						sz.top = 0;
						sz.right = 1;
						sz.bottom = height().property.value().intValue();
						canvas.drawRect(sz, Auxiliary.paintLine);//left
					}
				}
				if (!noHorizontalBorder.property.value()) {
					sz.left = 0;
					sz.top = height().property.value().intValue() - 1;
					sz.right = width().property.value().intValue();
					sz.bottom = height().property.value().intValue();
					canvas.drawRect(sz, Auxiliary.paintLine);//under
				}
			}
		};
		if (row > -1 && row < backgrounds.size()) {
			if (backgrounds.get(row) != null) {
				cell.background.is(backgrounds.get(row));
			}
		}
		//cell.setPadding(3, 0, 3, 0);
		//cell.labelStyleMediumNormal();
		if (row > -1 && row < sketches.size()) {
			//cell.labelText.is(strings.get(row));
			cell.sketch(sketches.get(row));
		}
		cells.add(cell);
		return cell;
	}
	@Override
	public String export(int row) {
		return "";
	}
	@Override
	public void update(int row) {
		// TODO Auto-generated method stub
	}
	@Override
	public Rake header(Context context) {
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
						), Auxiliary.paintLine);
			}
		};
		header.setPadding(3, 0, 3, 2);
		header.labelStyleSmallNormal();
		header.labelAlignCenterBottom();
		header.labelText.is(title.property.value());
		return header;
	}
	@Override
	public int count() {
		return sketches.size();
	}
	@Override
	public void clear() {
		if (presell >= 0 && presell < cells.size()) {
			if (presell >= 0 && presell < backgrounds.size()) {
				if (backgrounds.get(presell) != null) {
					cells.get(presell).background.is(backgrounds.get(presell));
				} else {
					cells.get(presell).background.is(0);
				}
			}
		}
		sketches.removeAllElements();
		backgrounds.removeAllElements();
		tasks.removeAllElements();
	}
	@Override
	public void highlight(int row) {
		if (presell >= 0 && presell < cells.size()) {
			if (presell >= 0 && presell < backgrounds.size()) {
				if (backgrounds.get(presell) != null) {
					cells.get(presell).background.is(backgrounds.get(presell));
				} else {
					cells.get(presell).background.is(0);
				}
			}
		}
		if (row >= 0 && row < cells.size()) {
			presell = row;
			cells.get(row).background.is(Auxiliary.colorSelection);
		}
	}
}
