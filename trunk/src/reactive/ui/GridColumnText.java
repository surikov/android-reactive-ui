package reactive.ui;

import android.content.*;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.*;

import tee.binding.it.Numeric;

public class GridColumnText extends GridColumn {
	private Vector<String> strings = new Vector<String>();
	Paint linePaint = new Paint();

	@Override
	public Rake item(final int column, int row, Context context) {
		Decor cell = new Decor(context, true) {
			//
			@Override
			protected void onDraw(Canvas canvas) {
				super.onDraw(canvas);
				if (column > 0) {
					//canvas.drawLine(0, 0, 0, height().property.value().floatValue(), linePaint);
					canvas.drawRect(new Rect(//left
							0//
							, 0//
							, 1//
							, height().property.value().intValue()//
							), linePaint);
				}
				//canvas.drawLine(0, height().property.value().floatValue() - 1, width().property.value().floatValue(), height().property.value().floatValue() - 1, linePaint);
				canvas.drawRect(new Rect(//under
						0//
						, height().property.value().intValue() - 1//
						, width().property.value().intValue()//
						, height().property.value().intValue() //
						), linePaint);
			}
		};
		cell.setPadding(3, 0, 3, 0);
		cell.labelStyleMediumNormal();
		/*Numeric w=new Numeric().bind(c.width().property);
		Numeric h=new Numeric().bind(c.height().property);
		c.sketch(new SketchLine().point(10, 10).point(20,20));*/
		//c.inTableRow=true;
		if (row > -1 && row < strings.size()) {
			cell.labelText.is(strings.get(row));
			//System.out.println("label "+strings.get(row));
		}
		//System.out.println("item "+c.inTableRow);
		return cell;
	}
	public GridColumnText cell(String s) {
		strings.add(s);
		return this;
	}
	@Override
	public int count() {
		return strings.size();
	}
	public GridColumnText() {
		this.width.is(150);
		linePaint.setColor(0x33666666);
		linePaint.setAntiAlias(true);
		linePaint.setFilterBitmap(true);
		linePaint.setDither(true);
		//linePaint.setStrokeWidth(1);
	}
	@Override
	public Rake header( Context context) {
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
}
