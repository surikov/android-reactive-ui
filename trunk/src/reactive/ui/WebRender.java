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

public class WebRender extends WebView implements Rake {
	boolean initialized = false;
	public NoteProperty<WebView> url = new NoteProperty<WebView>(this);
	public ItProperty<WebRender, Task> afterLink = new ItProperty<WebRender, Task>(this);
	//public NoteProperty<WebView> active = new NoteProperty<WebView>(this);
	private NumericProperty<Rake> width = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> height = new NumericProperty<Rake>(this);
	int maxH=0;
	private NumericProperty<Rake> left = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> top = new NumericProperty<Rake>(this);
	private boolean shouldOverrideUrlLoading = true;
	Task reFit ;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		//System.out.println(this.getClass().getCanonicalName() + ".onSizeChanged "+w+"/"+ h+" <- "+oldw+"/"+ oldh);
		super.onSizeChanged(w, h, oldw, oldh);
		//System.out.println(this.getClass().getCanonicalName() + ".onSizeChanged done");
	}
	/*public void debug() {
		System.out.println("Browser " + this.getLeft() + "x" + this.getTop() + "/" + this.getWidth() + "x" + this.getHeight());
		System.out.println("bind " + left().property.value() + "x" + top().property.value()+ "/" + width().property.value() + "x" + height().property.value());
	}*/
	public WebRender(Context context) {
		super(context);
		init();
	}
	public WebRender(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public WebRender(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	void init() {
		if (initialized) {
			return;
		}
	
		
		//loadUrl("file://" + mReportFilePath);
		//loadUrl("http://www.yandex.ru");
		getSettings().setJavaScriptEnabled(true);
		getSettings().setSaveFormData(true);
		getSettings().setBuiltInZoomControls(true);
		width.property.value(100);
		height.property.value(100);
		setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String urlString) {
				if (shouldOverrideUrlLoading) {
					//System.out.println("true");
					System.out.println("shouldOverrideUrlLoading "+urlString);
					url.is(urlString);
					if (afterLink.property.value() != null) {
						afterLink.property.value().start();
					}
					return true;
				}
				else {
					System.out.println("(go) " + urlString);
					return false;
				}
				//return shouldOverrideUrlLoading;
			}
			@Override
			public void onPageFinished(WebView view, String urlString) {
				System.out.println("onPageFinished "+urlString);
				shouldOverrideUrlLoading = true;
				setEnabled(true);
			}
		});
		initialized = true;
	}
	public void go(String urlString) {
		System.out.println("go "+urlString);
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
	@Override
    protected void onAttachedToWindow() {
		
		System.out.println("onAttachedToWindow ");
		super.onAttachedToWindow();
		reFit= new Task() {
			@Override
			public void doTask() {
				if(maxH>=height.property.value().intValue()){//hack for 01-03 11:04:39.918: W/webcore(1539): skip viewSizeChanged as w is 0
					return;
				}
				maxH=height.property.value().intValue();
				//System.out.println("reFit");
				int ww = width.property.value().intValue();
				if (ww <= 0) {
					ww = 300;
				}
				int hh = height.property.value().intValue();
				if (hh <= 0) {
					hh = 200;
				}
				//System.out.println("reFit "+ww+"x"+hh);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ww, hh);
				params.leftMargin = left.property.value().intValue();
				params.topMargin = top.property.value().intValue();
				//WebRender.this.setMinimumWidth(ww);
				//WebRender.this.setMinimumHeight(hh);
				setLayoutParams(params);
				//WebRender.this.setp
				//System.out.println("reFit params: " + params.width + "x" + params.height + ", get size is " + getWidth() + "x" + getHeight());
				
			}
		};
		width.property.afterChange(reFit);
		height.property.afterChange(reFit);
		left.property.afterChange(reFit);
		top.property.afterChange(reFit);
		System.out.println("onAttachedToWindow done");
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
