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

public class Grid extends SubLayoutless {
	public ToggleProperty<Grid> noHead = new ToggleProperty<Grid>(this);
	public NumericProperty<Grid> maxRowHeight = new NumericProperty<Grid>(this);
	TableLayout tableLayout;
	ScrollView scrollView;
	//Button b;
	private boolean initialized = false;

	public Grid(Context context) {
		super(context);
	}
	public Grid(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public Grid(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public void reset() {
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		//this.clear();
	}
	public Grid data(SheetColumn[] data) {
		System.out.println("data grid start");
		//TableLayout.LayoutParams p=new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f);
		for (int y = 0; y < 100; y++) {
			TableRow tableRow = new TableRow(this.getContext());
			for (int x = 0; x < 5; x++) {
				TextView textView = new TextView(this.getContext());
				//textView.setLayoutParams(p);
				textView.setWidth(100);
				textView.setHeight(50);
				textView.setText("Cell for testing purpose: "+x + "x" + y + ";");
				tableRow.addView(textView);
			}
			//tableRow
			tableLayout.addView(tableRow);
		}
		System.out.println("data grid done");
		return this;
	}
	@Override
	protected void init() {
		super.init();
		if (!initialized) {
			initialized = true;
			Task reFit = new Task() {
				@Override
				public void doTask() {
					//if (b != null) {
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
							width().property.value().intValue()//
							, height().property.value().intValue());
					//params.leftMargin = left.property.value().intValue();
					//params.topMargin = top.property.value().intValue();
					//b.setLayoutParams(params);
					if (scrollView != null) {
						scrollView.setLayoutParams(params);
					}
					//}
					//tableLayout.setLayoutParams(params);
				}
			};
			tableLayout = new TableLayout(this.getContext());
			scrollView = new ScrollView(this.getContext());
			//this.addView(tableLayout);
			//b = new Button(this.getContext());
			//b.setText("Test");
			//this.addView(b);
			scrollView.addView(tableLayout);
			this.addView(scrollView);
			//System.out.println(reFit);
			new Numeric().bind(width().property).afterChange(reFit);
			new Numeric().bind(height().property).afterChange(reFit);
		}
	}
}
