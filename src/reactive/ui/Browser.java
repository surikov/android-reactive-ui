package reactive.ui;

import android.webkit.*;
import java.io.*;
import java.util.*;

import tee.binding.properties.*;
import tee.binding.task.*;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

public class Browser extends WebView implements Rake {
	boolean initialized = false;
	public NoteProperty<WebView> url = new NoteProperty<WebView>(this);
	public ItProperty<Browser, Task> afterLink = new ItProperty<Browser, Task>(this);
	//public NoteProperty<WebView> active = new NoteProperty<WebView>(this);
	private NumericProperty<Rake> width = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> height = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> left = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> top = new NumericProperty<Rake>(this);
	private boolean shouldOverrideUrlLoading = true;
	Task reFit = new Task() {
		@Override
		public void doTask() {
			//System.out.
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
					width.property.value().intValue()//
					, height.property.value().intValue());
			params.leftMargin = left.property.value().intValue();
			params.topMargin = top.property.value().intValue();
			Browser.this.setLayoutParams(params);
			//System.out.println("params: " + params.topMargin+" / "+Browser.this.getLeft()+"x"+Browser.this.getTop()+"/"+Browser.this.getWidth()+"x"+Browser.this.getHeight());
		}
	};
	/*public void debug() {
		System.out.println("Browser " + this.getLeft() + "x" + this.getTop() + "/" + this.getWidth() + "x" + this.getHeight());
		System.out.println("bind " + left().property.value() + "x" + top().property.value()+ "/" + width().property.value() + "x" + height().property.value());
	}*/
	public Browser(Context context) {
		super(context);
		init();
	}
	public Browser(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public Browser(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	void init() {
		if (initialized) {
			return;
		}
		initialized = true;
		width.property.afterChange(reFit).value(100);
		height.property.afterChange(reFit).value(100);
		left.property.afterChange(reFit);
		top.property.afterChange(reFit);
		//loadUrl("file://" + mReportFilePath);
		//loadUrl("http://www.yandex.ru");
		getSettings().setJavaScriptEnabled(true);
		getSettings().setSaveFormData(true);
		getSettings().setBuiltInZoomControls(true);
		setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String urlString) {
				
				if (shouldOverrideUrlLoading) {
					//System.out.println("true");
					//System.out.println("url "+urlString);
					url.is(urlString);
					if(afterLink.property.value()!=null){
						afterLink.property.value().start();
					}
					return true;
				}
				else {
					System.out.println("go "+urlString);
					return false;
				}
				//return shouldOverrideUrlLoading;
			}
			@Override
			public void onPageFinished(WebView view, String urlString) {
				//System.out.println("onPageFinished "+urlString);
				shouldOverrideUrlLoading = true;
				setEnabled(true);
			}
		});
	}
	public void go(String urlString) {
	//System.out.println("go "+urlString);
		shouldOverrideUrlLoading = false;
		this.setEnabled(false);
		loadUrl(urlString);
	}
	@Override
	public NumericProperty<Rake> left() {
		return left;
	}
	@Override
	public NumericProperty<Rake> top() {
		return top;
	}
	@Override
	public NumericProperty<Rake> width() {
		return width;
	}
	@Override
	public NumericProperty<Rake> height() {
		return height;
	}
	@Override
	public View view() {
		return this;
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
	
	/*
	@Override
	public void draw(Canvas canvas) {
		System.out.println("draw");
		super.draw(canvas);
		Path path=new Path();
		path.lineTo(width().property.value().floatValue(), height().property.value().floatValue());
		Paint paint=new Paint();
		paint.setColor(0x99ff0000);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		canvas.drawPath(path, paint);
		
	}*/
}
