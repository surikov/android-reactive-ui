package reactive.ui;

import android.view.*;
import android.app.*;
import android.app.KeyguardManager.*;
import android.app.admin.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.opengl.*;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.Html;
import android.util.*;
import android.net.*;
import android.widget.*;
import java.util.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.params.*;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
//import com.example.android.apis.R;
import android.database.*;
import android.database.sqlite.*;
import reactive.ui.*;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.channels.FileChannel;
import android.view.animation.*;
import android.view.inputmethod.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;
import tee.binding.*;
import uniform.DataActivity;
import uniform.DataEnvironment;

import java.io.*;
import java.text.*;
import javax.microedition.khronos.opengles.GL10;

public class Demo extends Activity {
	Layoutless layoutless;
	DataGrid dataGrid;
	int gridPageSize = 30;
	Bough gridData;
	Numeric gridOffset = new Numeric();
	ColumnDescription columnName = new ColumnDescription();
	Expect requery = new Expect().status.is("...").task.is(new Task() {
		@Override
		public void doTask() {
			requeryData();
		}
	})//
	.afterDone.is(new Task() {
		@Override
		public void doTask() {
			refreshGUI();
		}
	})//
	;
	public void refreshGUI() {
		System.out.println("refreshGUI");
		flipGrid();
		dataGrid.refresh();
	}
	void requeryGridData() {
		gridData = new Bough();
		for (int i = 0; i < gridPageSize * 3; i++) {
			Bough row = new Bough();
			row.child("cell").value.property.value("cell " + (i + gridOffset.value()));
			gridData.child(row);
		}
	}
	void flipGrid() {
		dataGrid.clearColumns();
		if (gridData != null) {
			for (int i = 0; i < gridData.children.size(); i++) {
				Bough row = gridData.children.get(i);
				columnName.cell(row.child("cell").value.property.value());
			}
		}
	}
	public void requeryData() {
		System.out.println("requeryData");
		requeryGridData();
	}
	void initAll() {
		Auxiliary.startSensorEventListener(this,new Task(){

			@Override
			public void doTask() {
				//System.out.println(Auxiliary.accelerometerX+"x"+Auxiliary.accelerometerY+"x"+Auxiliary.accelerometerZ);
				
			}});
		dataGrid = new DataGrid(this).center.is(true)//
		.pageSize.is(gridPageSize)//
		.dataOffset.is(gridOffset)//
		.beforeFlip.is(new Task() {
			@Override
			public void doTask() {
				requeryGridData();
				flipGrid();
			}
		});
		layoutless.child(dataGrid.noHead.is(true).center.is(true)//
				.columns(new Column[] { //
						columnName.width.is(Auxiliary.tapSize * 7) //
						})//
				.left().is(0)//
				.top().is(Auxiliary.tapSize)//
				.width().is(layoutless.width().property)//
				.height().is(layoutless.height().property.minus(Auxiliary.tapSize))//
				);
		layoutless.child(new Knob(this).afterTap.is(new Task(){

			@Override
			public void doTask() {
				System.out.println("test");
				DataEnvironment.variables=Bough.parseXML(Auxiliary.loadTextFromResource(Demo.this, R.raw.test));
				
				//DataEnvironment.variables.child("store").child("messageFillGUI").value.property.value("Подождите...");
				//Bough formCfg=DataEnvironment.variables.child("forms").child("testForm");
				//formCfg.child("title").value.property.value("Маршрут");
				//System.out.println(DataEnvironment.variables.dumpXML());
				
				
				
				Intent intent = new Intent(Demo.this, uniform.DataActivity.class);
				intent.putExtra("form", "testForm");
				startActivity(intent);
				
				
				
				//System.out.println(Auxiliary.loadTextFromResource(Demo.this, R.raw.test));
			}
			
		})
		.width().is(100).height().is(100)
		);
		requery.start(Demo.this);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate");
		//System.out.println(Auxiliary.bundle2bough(this.getIntent().getExtras()).dumpXML());
		super.onCreate(savedInstanceState);
		layoutless = new Layoutless(this);
		setContentView(layoutless);
		initAll();
		//DataActivity.replaceVariables("{0123}456{78}{{901}2}");
	}
	@Override
	protected void onPause() {
		System.out.println("onPause");
		super.onPause();
		//Preferences.save();
		Auxiliary.stopSensorEventListener(this);
	}
	@Override
	protected void onResume() {
		System.out.println("onResume");
		super.onResume();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		System.out.println("onCreateOptionsMenu");
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		System.out.println("onOptionsItemSelected");
		return this.onOptionsItemSelected(item);
	}
}
