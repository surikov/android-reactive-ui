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
	Vector<SheetColumn> columns = new Vector<SheetColumn>();
	public NumericProperty<Sheet> rowHeight;
	public NumericProperty<Sheet> headerHeight;

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
	}
	public void fill() {
		int columnCount = columns.size();
		int rowCount = columns.get(0).count();
		//int columnWidth = 120;
		int curLeft = 0;
		//int rowHeight = 50;
		//System.out.println(columnCount+"x"+rowCount);
		for (int x = 0; x < columnCount; x++) {
			if (x > 0) {
				data.child(new Decor(this.getContext())//
				.background.is(Layoutless.themeBlurColor)//
						.width().is(1)//
						.height().is(rowCount * rowHeight.property.value())//
						.left().is(curLeft)//
						.view());
			}
			header.child(columns.get(x).header(this.getContext())//
					.left().is(header.shiftX.property.plus(curLeft))//
					//.top().is(2)//
					.height().is(headerHeight.property.value()).width().is(columns.get(x).width.property.value()).view()//
			);
			for (int y = 0; y < rowCount; y++) {
				//System.out.println(x+"x"+y+": ");
				data.child(columns.get(x).cell(y, this.getContext())//
						.left().is(curLeft)//
						.top().is(y * rowHeight.property.value())//
						.height().is(rowHeight.property.value()).width().is(columns.get(x).width.property.value()).view());
			}
			curLeft = curLeft + columns.get(x).width.property.value().intValue();
		}
		for (int y = 0; y < rowCount; y++) {
			if (y > 0) {
				data.child(new Decor(this.getContext())//
				.background.is(Layoutless.themeBlurColor)//
						.width().is(curLeft)//
						.height().is(1)//
						.top().is(y * rowHeight.property.value())//
						.view());
			}
		}
		data.width().is(curLeft);
		data.height().is(rowHeight.property.value() * rowCount);
		body.innerHeight.is(rowHeight.property.value() * rowCount);
		body.innerWidth.is(curLeft);
		header.innerWidth.is(curLeft);
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
			body.child(data);
			data.left().is(body.shiftX.property);
			data.top().is(body.shiftY.property);
			data.solid.is(false);
			body.shiftX.property.bind(header.shiftX.property);
			header.afterTap.is(new Task() {
				@Override
				public void doTask() {
					if (header.tapX.property.value() >= 0) {
						int columnCount = columns.size();
						int curLeft = 0;
						for (int x = 0; x < columnCount; x++) {
							curLeft = curLeft + columns.get(x).width.property.value().intValue();
							if (header.tapX.property.value() <= curLeft) {
								if(columns.get(x).afterHeaderTap.property.value()!=null){
									columns.get(x).afterHeaderTap.property.value().start();
								}
								//System.out.println("header " + x);
								break;
							}
						}
					}
				}
			});
		}
	}
}
