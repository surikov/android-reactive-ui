package reactive.ui;

import android.content.*;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.text.Html;

import java.util.*;

import tee.binding.it.Numeric;
import tee.binding.task.*;

public class GridColumnDescription extends GridColumnText {
	protected Vector<String> descriptions = new Vector<String>();

	public GridColumnDescription() {
		/*this.width.is(150);
		linePaint.setColor(0x33666666);
		linePaint.setAntiAlias(true);
		linePaint.setFilterBitmap(true);
		linePaint.setDither(true);*/
		//linePaint.setStrokeWidth(0);
		//linePaint.setStyle(Style.STROKE);
		super();
	}
	public GridColumnText cell(String s, Integer background, Task tap, String descr) {
		descriptions.add(descr);
		return super.cell(s, background, tap);
	}
	@Override
	public GridColumnText cell(String s, Integer background, Task tap) {
		return cell(s, background, tap, null);
	}
	public GridColumnText cell(String s, String descr) {
		return cell(s, null, null, descr);
	}
	public GridColumnText cell(String s, Integer background, String descr) {
		return cell(s, background, null, descr);
	}
	public GridColumnText cell(String s, Task tap, String descr) {
		return cell(s, null, tap, descr);
	}
	@Override
	public Rake item(final int column, int row, Context context) {
		HTMLText cell = new HTMLText(context, true) {
			@Override
			protected void onDraw(Canvas canvas) {
				super.onDraw(canvas);
				if (sz == null) {
					sz = new Rect();
				}
				if (column > 0) {
					sz.left = 0;
					sz.top = 0;
					sz.right = 1;
					sz.bottom = height().property.value().intValue();
					canvas.drawRect(sz, linePaint);//left
				}
				sz.left = 0;
				sz.top = height().property.value().intValue() - 1;
				sz.right = width().property.value().intValue();
				sz.bottom = height().property.value().intValue();
				canvas.drawRect(sz, linePaint);//under
			}
		};
		if (row > -1 && row < backgrounds.size()) {
			if (backgrounds.get(row) != null) {
				cell.background.is(backgrounds.get(row));
			}
		}
		cell.setPadding(3, 0, 3, 0);
		//cell.labelStyleMediumNormal();
		if (row > -1 && row < strings.size()) {
			cell.html.is(Html.fromHtml("<p>" + strings.get(row) + "<br/><small>" + descriptions.get(row) + "</small></p>"));
		}
		return cell;
	}
}
