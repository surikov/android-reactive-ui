package reactive.ui;

import java.util.Vector;

import tee.binding.task.Task;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class GridColumnBitmap extends GridColumn {
	protected Vector<Task> tasks = new Vector<Task>();
	protected Vector<Decor> cells = new Vector<Decor>();
	protected Vector<Bitmap> bitmaps = new Vector<Bitmap>();
	protected Vector<Integer> backgrounds = new Vector<Integer>();
	protected Paint linePaint = new Paint();
	protected Rect sz;
	int presell = -1;

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
			if (row > -1 && row < bitmaps.size()) {
				cell.bitmap.is(bitmaps.get(row));
			}
			else {
				cell.bitmap.property.value(null);
			}
		}
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
	public GridColumnBitmap() {
		this.width.is(150);
		linePaint.setColor(0x33666666);
		linePaint.setAntiAlias(true);
		linePaint.setFilterBitmap(true);
		linePaint.setDither(true);
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
				}}
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
		if (row > -1 && row < bitmaps.size()) {
			cell.bitmap.is(bitmaps.get(row));
		}
		cells.add(cell);
		return cell;
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
	public int count() {
		return bitmaps.size();
	}
	@Override
	public void clear() {
		bitmaps.removeAllElements();
		backgrounds.removeAllElements();
		tasks.removeAllElements();
		//cells.removeAllElements();
	}
	@Override
	public void highlight(int row) {
		if (presell >= 0 && presell < cells.size()) {
			if (backgrounds.get(presell) != null) {
				cells.get(presell).background.is(backgrounds.get(presell));
			}
			else {
				cells.get(presell).background.is(0);
			}
		}
		if (row >= 0 && row < cells.size()) {
			presell = row;
			cells.get(row).background.is(Layoutless.themeBlurColor);
		}
	}
	public GridColumnBitmap cell(Bitmap s, Integer background, Task tap) {
		bitmaps.add(s);
		tasks.add(tap);
		backgrounds.add(background);
		return this;
	}
	public GridColumnBitmap cell(Bitmap s, Integer background) {
		return cell(s, background, null);
	}
	public GridColumnBitmap cell(Bitmap s, Task tap) {
		return cell(s, null, tap);
	}
	public GridColumnBitmap cell(Bitmap s) {
		return cell(s, null, null);
	}
}
