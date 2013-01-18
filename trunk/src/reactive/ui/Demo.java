package reactive.ui;

//http://www3.ntu.edu.sg/home/ehchua/programming/android/Android_3D.html
import android.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.Bundle;
import android.os.Environment;
import android.util.*;
import android.net.*;
import android.widget.*;

import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.apache.http.params.*;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.database.*;
import android.database.sqlite.*;
import reactive.ui.*;

import java.net.*;
import java.nio.channels.FileChannel;

import android.view.animation.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;
import tee.binding.*;
import java.io.*;
import java.text.*;

public class Demo extends Activity {
	Layoutless layoutless;
	SQLiteDatabase cacheSQLiteDatabase = null;
	Sheet gridHistory;
	//OpenGL2 glView;
	SheetColumnText historyArtikul = new SheetColumnText();
	SheetColumnText historyNomenklatura = new SheetColumnText();
	SheetColumnText historyProizvoditel = new SheetColumnText();
	SheetColumnText historyMinKol = new SheetColumnText();
	SheetColumnText historyKolMest = new SheetColumnText();
	SheetColumnText historyEdIzm = new SheetColumnText();
	SheetColumnText historyCena = new SheetColumnText();
	SheetColumnText historyRazmSkidki = new SheetColumnText();
	SheetColumnText historyVidSkidki = new SheetColumnText();
	SheetColumnText historyPoslCena = new SheetColumnText();
	SheetColumnText historyMinCena = new SheetColumnText();
	SheetColumnText historyMaxCena = new SheetColumnText();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("go----------------------------------");
		layoutless = new Layoutless(this);
		Preferences.init(this);
		/*
		historyArtikul.title.is("Артикул").width.is(90);
		historyNomenklatura;
		historyProizvoditel;
		historyMinKol;
		historyKolMest;
		historyEdIzm;
		historyCena;
		historyRazmSkidki;
		historyVidSkidki;
		historyPoslCena;
		historyMinCena;
		historyMaxCena;
		*/
		gridHistory = new Sheet(this);
		layoutless.child(new Decor(this)//
				.background.is(0x9999ff99)//
						.sketch(new SketchLine().strokeWidth.is(1).strokeColor.is(0x660000ff).point(100, 50).point(100, 150))//
						.sketch(new Sketches()//
								.child(new SketchPlate()//
								.left.is(10).top.is(50).width.is(150).height.is(50)//
								)//
								.child(new SketchText().color.is(0x99ff00ff)//
								.text.is("asbstb sb sbtgfsgndrfgndrfgns nsdftgn drsfgnsdf gdfvaesbf")//
								.size.is(21)//
								.left.is(10).top.is(50).width.is(150).height.is(50)//
								)//
						)//
						.width().is(200).height().is(200).left().is(300)//
				);
		layoutless.child(new Decor(this)//
				.sketch(new SketchText()//
				.color.is(0x99ff0000)//
				.text.is("12345678901234567890")//
				.size.is(21)//
				.left.is(10)//
				.top.is(50)//
				.width.is(150)//
				.height.is(50)//
				)//
				.width().is(200).height().is(200).left().is(50).top().is(50)//
				);
		/*layoutless.child(new Decor(this)//
				.sketch(new SketchText()//cell
				//.color.is(0x99660000)//
				.size.is(50)//
				.text.is("x1234567890123456789")//
				.width.is(1000)//columns[x].width.property.value())//
				.height.is(1000)//rowHeight.property)//
				.top.is(0)//rowHeight.property.multiply(y))//
				.left.is(0)//curLeft) //
				//.color.is(0x99660000)
				)//
				.background.is(0x33990099)//
						.width().is(1000)//columns[x].width.property.value())//
						.height().is(1000)//rowHeight.property)//
						.top().is(0)//rowHeight.property.multiply(y))//
						.left().is(0)//curLeft) //
				);*/
		/*
		layoutless.child(new OpenGL2(this)//
		.left().is(20).top().is(10)
				.width().is(1200).height().is(700)//
		);
		*/
		layoutless.child(new SplitLeftRight(this)//
				.rightSide(gridHistory//
						.data(new SheetColumn[] { historyArtikul.title.is("Артикул").width.is(90) //
								, historyNomenklatura.title.is("Номенклатура").width.is(270)//
								, historyProizvoditel.title.is("Производитель").width.is(140)//
								, historyMinKol.title.is("Мин. кол.").width.is(90)//
								, historyKolMest.title.is("кол. мест").width.is(90)//
								, historyEdIzm.title.is("Ед. изм.").width.is(90)//
								, historyCena.title.is("Цена").width.is(90)//
								, historyRazmSkidki.title.is("Разм. скидки").width.is(90)//
								, historyVidSkidki.title.is("Вид скидки").width.is(120)//
								, historyPoslCena.title.is("Посл. цена").width.is(120)//
								, historyMinCena.title.is("Мин. цена").width.is(90)//
								, historyMaxCena.title.is("Макс. цена").width.is(90) //
						})//
						.maxRowHeight.is(2)//
				)//
				.width().is(layoutless.width().property)//
				.height().is(layoutless.height().property)//
				);
		setContentView(layoutless);
		//glView = new OpenGL2(this);
		//this.setContentView(glView); 
		new Expect()//
		.task.is(new Task() {
			@Override
			public void doTask() {
				historyRequestData();
			}
		})//
		.afterDone.is(new Task() {
			@Override
			public void doTask() {
				historyFillGrid();
			}
		})//
		.status.is("Обработка...").start(this);
	}
	void historyRequestData() {
		System.out.println("start query");
		Cursor c = db().rawQuery("select nomenklatura.artikul as artikul,nomenklatura.naimenovanie as naimenovanie"//
				+ " from Prodazhi"// 
				+ " join nomenklatura on nomenklatura._idrref=Prodazhi.nomenklatura"// 
				+ " order by nomenklatura.naimenovanie"//
				+ " limit 20;", null);
		System.out.println("load tree query");
		Bough b = Auxiliary.fromCursor(c);
		System.out.println("fill cells");
		for (int i = 0; i < b.children.size(); i++) {
			Bough row = b.children.get(i);
			historyArtikul.cell(row.child("artikul").value.property.value());
			historyNomenklatura.cell(i + ": " + row.child("naimenovanie").value.property.value());
			historyProizvoditel.cell("a" + i);
			historyMinKol.cell("b" + i);
			historyKolMest.cell("v" + i);
			historyEdIzm.cell("d" + i);
			historyCena.cell("e" + i);
			historyRazmSkidki.cell("f" + i);
			historyVidSkidki.cell("g" + i);
			historyPoslCena.cell("h" + i);
			historyMinCena.cell("i" + i);
			historyMaxCena.cell("j" + i);
		}
		System.out.println("done fill cells");
	}
	void historyFillGrid() {
		System.out.println("start reset grid");
		gridHistory.reset();
		System.out.println("done reset grid");
	}
	SQLiteDatabase db() {
		if (cacheSQLiteDatabase == null || (!cacheSQLiteDatabase.isOpen())) {
			cacheSQLiteDatabase = Auxiliary.connectSQLiteDatabase("/sdcard/horeca/swlife_database", this, 2);
		}
		cacheSQLiteDatabase.setVersion(2);
		return cacheSQLiteDatabase;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("First");
		menu.add("Second");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	@Override
	protected void onPause() {
		//System.out.println("onPause");
		Preferences.save();
		super.onPause();
		//glView.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
		//glView.onResume();
	}
}
