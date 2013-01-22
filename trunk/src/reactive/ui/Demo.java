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
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.params.*;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.database.*;
import android.database.sqlite.*;
import reactive.ui.*;

import java.net.*;
import java.nio.channels.FileChannel;

import android.view.animation.*;
import android.view.inputmethod.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;
import tee.binding.*;
import java.io.*;
import java.text.*;

interface ISklady {
	String HORECA_ID = "x'AAFFF658AE67DCE94696B419219D8E1C'";
	String HORECA_sklad_1 = "x'967E50505450303011DA00E63437669D'";
	String HORECA_sklad_2 = "x'967E50505450303011DA00E6343766A0'";
	String HORECA_sklad_3 = "x'970E001438C58CB411DAFF8AE294A2D7'";
	String KAZAN_ID = "x'A756BEA77AB71E2F45CD824C4AB4178F'";
	String KAZAN_sklad_1 = "X'82AE002264FA89D811E0E3C13D6A574C'";
	String EMPTY_sklad = "X'00000000000000000000000000000000'";
	/*
	"301","X'A756BEA77AB71E2F45CD824C4AB4178F'","X'00000000000000000000000000000000'"
	"1147","X'A756BEA77AB71E2F45CD824C4AB4178F'","X'82AE002264FA89D811E0E3C13D6A574C'"
	"1","X'A756BEA77AB71E2F45CD824C4AB4178F'","X'970E001438C58CB411DAFF8AE294A2D7'"
	
	"12772","X'AAFFF658AE67DCE94696B419219D8E1C'","X'00000000000000000000000000000000'"
	"3322","X'AAFFF658AE67DCE94696B419219D8E1C'","X'967E50505450303011DA00E63437669D'"
	"415","X'AAFFF658AE67DCE94696B419219D8E1C'","X'967E50505450303011DA00E6343766A0'"
	"5906","X'AAFFF658AE67DCE94696B419219D8E1C'","X'970E001438C58CB411DAFF8AE294A2D7'"
	*/
}

public class Demo extends Activity {
	Layoutless layoutless;
	SQLiteDatabase cacheSQLiteDatabase = null;
	Sheet gridHistory;
	public static int dataPageSize = 10;
	//Numeric historySplit=new Numeric();
	//OpenGL2 glView;
	Note seekStringHistory = new Note();
	String seekPreHistory = "";
	int currentPageHistory = 0;
	boolean historyHasMoreData = false;
	Numeric historyFrom = new Numeric();
	Numeric historyTo = new Numeric();
	SheetColumnBitmap historyPhoto = new SheetColumnBitmap();
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
	Bitmap photoIcon = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("go----------------------------------");
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		layoutless = new Layoutless(this);
		Preferences.init(this);
		Calendar historyFromDate = Calendar.getInstance();
		historyFromDate.add(Calendar.DAY_OF_YEAR, -90);
		historyFrom.value((double) historyFromDate.getTimeInMillis());
		new Numeric().bind(historyFrom).afterChange(new Task() {
			@Override
			public void doTask() {
				//System.out.println("historyFrom "+historyFrom);
				currentPageHistory = 0;
				//startRefreshHistoryGrid();
				startRefreshHistoryGrid.start(Demo.this);
			}
		}, true);
		/*.bind(new Numeric().value((double) historyFromDate.getTimeInMillis()).afterChange(new Task(){

			@Override
			public void doTask() {
				System.out.println();
				
			}}));*/
		Calendar historyToDate = Calendar.getInstance();
		historyTo.value((double) historyToDate.getTimeInMillis());
		new Numeric().bind(historyTo).afterChange(new Task() {
			@Override
			public void doTask() {
				//System.out.println("historyFrom "+historyFrom);
				currentPageHistory = 0;
				//startRefreshHistoryGrid();
				startRefreshHistoryGrid.start(Demo.this);
			}
		}, true);
		photoIcon = BitmapFactory.decodeResource(getResources(), R.drawable.picture);
		//System.out.println(photoIcon.getWidth());
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
		/*layoutless.child(new Decor(this)//
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
				);*/
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
		seekStringHistory.afterChange(new Task() {
			@Override
			public void doTask() {
				//System.out.println(seekStringHistory.value().length() + " / " + seekPreHistory.length());
				if ((seekStringHistory.value().length() == 0 && seekPreHistory.length() > 0)) {
					//System.out.println("seekString: " + seekStringHistory.value() + " /" + seekStringHistory.value().length());
					currentPageHistory = 0;
					historyRequestData();
					historyFillGrid();
				}
				else {
					if (seekStringHistory.value().length() > 2) {
						if (seekStringHistory.value().length() > seekPreHistory.length()) {
							currentPageHistory = 0;
							historyRequestData();
							historyFillGrid();
						}
					}
				}
				seekPreHistory = seekStringHistory.value();
			}
		}, true);
		//historyFrom.bind(new Numeric());
		SubLayoutless gridHistoryPanel = new SubLayoutless(this);
		SplitLeftRight slr = new SplitLeftRight(this);//.split.is(historySplit);
		layoutless.child(slr//
				.rightSide(gridHistoryPanel//
						.child(new KnobImage(this)//
						//.labelText.is("Дальше")
						.bitmap.is(BitmapFactory.decodeResource(getResources(), R.drawable.goprev)).tap.is(new Task() {
							@Override
							public void doTask() {
								if (!seekPreHistory.equals(seekStringHistory.value())) {
									currentPageHistory = 0;
									Auxiliary.hideSoftKeyboard(Demo.this);
									//InputMethodManager inputManager = (InputMethodManager) Demo.this.getSystemService(Context.INPUT_METHOD_SERVICE);
									//inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
									//startRefreshHistoryGrid();
									startRefreshHistoryGrid.start(Demo.this);
								}
								else {
									if (historyHasMoreData) {
										currentPageHistory++;
									}
									Auxiliary.hideSoftKeyboard(Demo.this);
									//InputMethodManager inputManager = (InputMethodManager) Demo.this.getSystemService(Context.INPUT_METHOD_SERVICE);
									//inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
									//startRefreshHistoryGrid();
									startRefreshHistoryGrid.start(Demo.this);
								}
							}
						}//
								)//
								.width().is(0.8 * Layoutless.tapSize)//
								.height().is(0.8 * Layoutless.tapSize)//
								.left().is(0.1 * Layoutless.tapSize)//
								.top().is(0.1 * Layoutless.tapSize)//
						)//
						.child(new RedactText(this).text.is(seekStringHistory)//
						.singleLine.is(true).width().is(5 * Layoutless.tapSize)//
								.height().is(0.8 * Layoutless.tapSize)//
								.left().is(0.9 * Layoutless.tapSize)//
								.top().is(0.1 * Layoutless.tapSize))//
						.child(new KnobImage(this)//
						//.labelText.is("Пред.")
						.bitmap.is(BitmapFactory.decodeResource(getResources(), R.drawable.gonext)).tap.is(new Task() {
							@Override
							public void doTask() {
								if (!seekPreHistory.equals(seekStringHistory.value())) {
									currentPageHistory = 0;
									Auxiliary.hideSoftKeyboard(Demo.this);
									//InputMethodManager inputManager = (InputMethodManager) Demo.this.getSystemService(Context.INPUT_METHOD_SERVICE);
									//inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
									//startRefreshHistoryGrid();
									startRefreshHistoryGrid.start(Demo.this);
								}
								else {
									if (currentPageHistory > 0) {
										currentPageHistory--;
									}
									Auxiliary.hideSoftKeyboard(Demo.this);
									//InputMethodManager inputManager = (InputMethodManager) Demo.this.getSystemService(Context.INPUT_METHOD_SERVICE);
									//inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
									//startRefreshHistoryGrid();
									startRefreshHistoryGrid.start(Demo.this);
								}
							}
						}//
								)//
								.width().is(0.8 * Layoutless.tapSize)//
								.height().is(0.8 * Layoutless.tapSize)//
								.left().is(5.9 * Layoutless.tapSize)//
								.top().is(0.1 * Layoutless.tapSize)//
						)//
						.child(new Decor(this)//
						.labelText.is("Период с").labelAlignRightCenter()//
								.width().is(2 * Layoutless.tapSize)//
								.height().is(0.8 * Layoutless.tapSize)//
								.left().is(6.1 * Layoutless.tapSize)//
								.top().is(0.1 * Layoutless.tapSize)//
						)//
						.child(new RedactDate(this)//
						.date.is(historyFrom).format.is("dd.MM.yyyy")//
								.width().is(2 * Layoutless.tapSize)//
								.height().is(0.8 * Layoutless.tapSize)//
								.left().is(8.1 * Layoutless.tapSize)//
								.top().is(0.1 * Layoutless.tapSize)//
						)//
						.child(new Decor(this)//
						.labelText.is("по").labelAlignRightCenter()//
								.width().is(1 * Layoutless.tapSize)//
								.height().is(0.8 * Layoutless.tapSize)//
								.left().is(10.1 * Layoutless.tapSize)//
								.top().is(0.1 * Layoutless.tapSize)//
						)//
						.child(new RedactDate(this)//
						.date.is(historyTo).format.is("dd.MM.yyyy")//
								.width().is(2 * Layoutless.tapSize)//
								.height().is(0.8 * Layoutless.tapSize)//
								.left().is(11.1 * Layoutless.tapSize)//
								.top().is(0.1 * Layoutless.tapSize)//
						)//
						.child(gridHistory//
						.maxRowHeight.is(2)//
								.data(new SheetColumn[] { historyArtikul.title.is("Артикул").textSize.is(18).width.is(90) //
										, historyNomenklatura.title.is("Номенклатура").textSize.is(18).width.is(290)//
										, historyProizvoditel.title.is("Производитель").textSize.is(18).width.is(140)//
										, historyMinKol.title.is("Мин. кол.").textSize.is(18).width.is(50)//
										, historyKolMest.title.is("Кол. мест").textSize.is(18).width.is(50)//
										, historyEdIzm.title.is("Ед. изм.").textSize.is(18).width.is(50)//
										, historyCena.title.is("Цена").textSize.is(18).width.is(80)//
										, historyRazmSkidki.title.is("Разм. скидки").textSize.is(18).width.is(80)//
										, historyVidSkidki.title.is("Вид скидки").textSize.is(18).width.is(120)//
										, historyPoslCena.title.is("Посл. цена").textSize.is(18).width.is(80)//
										, historyMinCena.title.is("Мин. цена").textSize.is(18).width.is(80)//
										, historyMaxCena.title.is("Макс. цена").textSize.is(18).width.is(80) //
										, historyPhoto.title.is("Фото").width.is(Layoutless.tapSize) //
								})//
								.top().is(Layoutless.tapSize)
								//.width().is(layoutless.width().property.minus(0.5 * Layoutless.tapSize).read())//
								.width().is(layoutless.width().property.minus(slr.split.property).read())//
								.height().is(layoutless.height().property.minus(Layoutless.tapSize).read())//
						)//
				)//
				.width().is(layoutless.width().property)//
				.height().is(layoutless.height().property)//
				);
		setContentView(layoutless);
		//glView = new OpenGL2(this);
		//this.setContentView(glView); 
		//startRefreshHistoryGrid();
		startRefreshHistoryGrid.start(Demo.this);
		/*seekStringHistory.afterChange(new Task() {
			@Override
			public void doTask() {
				System.out.println("seekStringHistory: " + seekStringHistory.value());
			}
		}, true);*/
	}

	//void startRefreshHistoryGrid() {
	Expect startRefreshHistoryGrid = new Expect()//
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
			//System.out.println("slr.split.property.value()) " + slr.split.property.value());
		}
	})//
	.status.is("Подождите...");

	//.start(this);
	//}
	void historyRequestData() {
		System.out.println("start query");
		if (!seekPreHistory.equals(seekStringHistory.value())) {
			currentPageHistory = 0;
		}
		seekPreHistory = seekStringHistory.value();
		if (currentPageHistory < 0) {
			currentPageHistory = 0;
		}
		/*String sql = "select nomenklatura.artikul as artikul,nomenklatura.naimenovanie as naimenovanie"//
				+ " from Prodazhi"// 
				+ " join nomenklatura on nomenklatura._idrref=Prodazhi.nomenklatura"// 
				+ " where nomenklatura.uppername like '%" + seekStringHistory.value().trim().toUpperCase() + "%'"// 
				+ " order by nomenklatura.naimenovanie"//
				+ " limit 20 offset " + (currentPageHistory * 20)//
				+ ";";*/
		//System.out.println(dataOtgruzki);
		Date f = new Date();
		f.setTime(historyFrom.value().longValue());
		Date t = new Date();
		t.setTime(historyTo.value().longValue());
		String sql = historySQL(//
				//new Date(2012-1900, 11 - 1, 22) //
				f
				//, new Date(2013-1900, 1 - 1, 21)//
				, t, seekStringHistory.value().trim().toUpperCase()//?
				, "x'8D1B18A90562E07411E1CA6CA7A8B12A'"//
				, "x'8BB100304885BA0D11DD8BBE2CD8D3BD'"//
				, new Date(2013 - 1900, 1 - 1, 22)//"2013-01-22"
				, "x'AAFFF658AE67DCE94696B419219D8E1C'"//
				, currentPageHistory//
		);
		System.out.println("exec query ");
		Cursor c = db().rawQuery(sql, null);
		System.out.println("load tree ");
		Bough b = Auxiliary.fromCursor(c);
		System.out.println("fill cells");
		historyArtikul.clear();
		historyNomenklatura.clear();
		historyProizvoditel.clear();
		historyMinKol.clear();
		historyKolMest.clear();
		historyEdIzm.clear();
		historyCena.clear();
		historyRazmSkidki.clear();
		historyVidSkidki.clear();
		historyPoslCena.clear();
		historyMinCena.clear();
		historyMaxCena.clear();
		historyPhoto.clear();
		historyHasMoreData = false;
		String[] mDateFormatStrings = new String[] { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
				//				"yyyy-MM-dd hh:mm:ss",
				"yyyy-MM-dd hh:mm", "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd hh:mm:ss.sss", "yyyy-MM-dd'T'hh:mm", "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd'T'hh:mm:ss.sss", "hh:mm",
				"hh:mm:ss", "hh:mm:ss.sss", "yyyyMMdd'T'hh:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" };
		for (int i = 0; i < b.children.size(); i++) {
			//hasData=true;
			Bough row = b.children.get(i);
			final String a = row.child("Artikul").value.property.value() + ": " + row.child("Naimenovanie").value.property.value();
			Task click = new Task() {
				@Override
				public void doTask() {
					Auxiliary.warn(a, Demo.this);
				}
			};
			Task photo = new Task() {
				@Override
				public void doTask() {
					Auxiliary.warn("photo " + a, Demo.this);
				}
			};
			int artikulBG = 0;
			try {
				String LastSell = row.child("LastSell").value.property.value();
				if (LastSell != null) {
					if (LastSell.length() > 0) {
						java.util.Date d = DateUtils.parseDate(LastSell, mDateFormatStrings);
						//System.out.println(d);
						java.util.Calendar now = Calendar.getInstance();
						now.add(Calendar.DAY_OF_YEAR, -14);
						if (d.before(now.getTime())) {
							artikulBG = 0xffff6666;
							//System.out.println("yes "+now.getTime());
						}
					}
				}
			}
			catch (Throwable tr) {
				tr.printStackTrace();
			}
			int naimenovanieBG = 0;
			try {
				double CenyNomenklaturySklada = Double.parseDouble(row.child("Cena").value.property.value());
				double TekuschieCenyOstatkovPartiy = Double.parseDouble(row.child("BasePrice").value.property.value());
				//cursor.getDouble(cursor.getColumnIndex("BasePrice"));
				int procent = (int) (100.0 * (CenyNomenklaturySklada - TekuschieCenyOstatkovPartiy) / TekuschieCenyOstatkovPartiy);
				//System.out.println("100.0 * (" + CenyNomenklaturySklada + " - " + TekuschieCenyOstatkovPartiy + ") / " + TekuschieCenyOstatkovPartiy + ")=" + procent);
				if (procent >= 18) {
					//return true;
					naimenovanieBG = 0xffff9966;
				}
			}
			catch (Throwable tr) {
				tr.printStackTrace();
			}
			historyArtikul.item(//currentPageHistory + "/" + i + ": " + 
					row.child("Artikul").value.property.value(), artikulBG, click);
			historyNomenklatura.item(row.child("Naimenovanie").value.property.value(), naimenovanieBG, click);
			historyProizvoditel.item(row.child("ProizvoditelNaimenovanie").value.property.value(), click);
			historyMinKol.item(row.child("MinNorma").value.property.value(), click);
			historyKolMest.item(row.child("Koephphicient").value.property.value(), click);
			historyEdIzm.item(row.child("EdinicyIzmereniyaNaimenovanie").value.property.value(), click);
			historyCena.item(row.child("BasePrice").value.property.value(), click);
			historyRazmSkidki.item(row.child("razmskidki").value.property.value(), click);
			historyVidSkidki.item(row.child("vidskidki").value.property.value(), click);
			historyPoslCena.item(row.child("LastPrice").value.property.value(), click);
			historyMinCena.item(row.child("MinCena").value.property.value(), click);
			historyMaxCena.item(row.child("MaxCena").value.property.value(), click);
			historyPhoto.item(photoIcon, photo);
			if (i == dataPageSize - 1) {
				historyHasMoreData = true;
			}
		}
		System.out.println("done fill cells " + currentPageHistory);
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
	public static String uslovieSkladaPodrazdeleniaNeTraphik(Date dataOtgruzki, String skladPodrazdelenia) {
		boolean letuchka = false;
		//DateTimeHelper.SQLDateString(dataOtgruzki).equals(DateTimeHelper.SQLDateString(new Date()));
		skladPodrazdelenia = skladPodrazdelenia.toUpperCase();
		//String traphikCondition = " and Traphik=x'01'";
		/*if (!traphik) {
			traphikCondition = " and Traphik=x'00'";
		}*/
		//skladPodrazdelenia = ISklady.HORECA_ID.toUpperCase();
		if (skladPodrazdelenia.equals(ISklady.HORECA_ID.toUpperCase())) {
			return "\n	on (select sklad from AdresaPoSkladam horeca where horeca.nomenklatura=n._idrref and horeca.period"//
					+ "\n		=(select max(period) from adresaposkladam where nomenklatura=n._idrref"//
					+ "\n			and baza=" + ISklady.HORECA_ID + " and Traphik=x'00') "//
					+ "\n		)=" + ISklady.HORECA_sklad_1//
					+ "\n	or (select sklad from AdresaPoSkladam horeca where horeca.nomenklatura=n._idrref and horeca.period"//
					+ "\n		=(select max(period) from adresaposkladam where nomenklatura=n._idrref"//
					+ "\n			and baza=" + ISklady.HORECA_ID + " and Traphik=x'00') "//
					+ "\n		)=" + ISklady.HORECA_sklad_2//
					+ "\n	or (select sklad from AdresaPoSkladam horeca where horeca.nomenklatura=n._idrref and horeca.period"//
					+ "\n		=(select max(period) from adresaposkladam where nomenklatura=n._idrref"//
					+ "\n			and baza=" + ISklady.HORECA_ID + " and Traphik=x'00') "//
					+ "\n		)=" + ISklady.HORECA_sklad_3//
					+ "\n";
		}
		else {
			if (skladPodrazdelenia.equals(ISklady.KAZAN_ID.toUpperCase())) {
				if (letuchka) {
					return "\n	on (select sklad from AdresaPoSkladam kazan where kazan.nomenklatura=n._idrref and kazan.period"//
							+ "\n		=(select max(period) from adresaposkladam where nomenklatura=n._idrref"//
							+ "\n			and baza=" + ISklady.KAZAN_ID + " and Traphik=x'00') "//
							+ "\n		)=" + ISklady.KAZAN_sklad_1//
							+ "\n";
				}
				else {
					return "\n	on (select sklad from AdresaPoSkladam horeca where horeca.nomenklatura=n._idrref and horeca.period"//
							+ "\n		=(select max(period) from adresaposkladam where nomenklatura=n._idrref"//
							+ "\n			and baza=" + ISklady.HORECA_ID + " and Traphik=x'00') "//
							+ "\n		)=" + ISklady.HORECA_sklad_1//
							+ "\n	or (select sklad from AdresaPoSkladam horeca where horeca.nomenklatura=n._idrref and horeca.period"//
							+ "\n		=(select max(period) from adresaposkladam where nomenklatura=n._idrref"//
							+ "\n			and baza=" + ISklady.HORECA_ID + " and Traphik=x'00') "//
							+ "\n		)=" + ISklady.HORECA_sklad_2//
							+ "\n	or (select sklad from AdresaPoSkladam horeca where horeca.nomenklatura=n._idrref and horeca.period"//
							+ "\n		=(select max(period) from adresaposkladam where nomenklatura=n._idrref"//
							+ "\n			and baza=" + ISklady.HORECA_ID + " and Traphik=x'00') "//
							+ "\n		)=" + ISklady.HORECA_sklad_3//
							+ "\n	or (select sklad from AdresaPoSkladam kazan where kazan.nomenklatura=n._idrref and kazan.period"//
							+ "\n		=(select max(period) from adresaposkladam where nomenklatura=n._idrref"//
							+ "\n			and baza=" + ISklady.KAZAN_ID + " and Traphik=x'00') "//
							+ "\n		)=" + ISklady.KAZAN_sklad_1//
							+ "\n";
				}
			}
			else {
				//LogHelper.debug("Unknown skladPodrazdelenia");
				return "";
			}
		}
	}
	String historySQL(Date fromDate//2012-11-22
			, Date toDate//2013-01-21
			, String searchString//?
			, String agentID//x'8BB100304885BA0D11DD8BBE2CD8D3BD'
			, String clientID//x'8D1B18A90562E07411E1CA6CA7A8B12A'
			, Date dataOtgruzki//2013-01-22
			, String skladPodrazdelenia//x'AAFFF658AE67DCE94696B419219D8E1C'
			, int page) {
		String queryStr = "	select					\n" //
				+ "	n._id					\n" //
				+ "	,n.[_IDRRef]					\n" //
				+ "	,n.[Artikul]					\n" //
				+ "	,n.[Naimenovanie]					\n" //
				+ "	,n.[OsnovnoyProizvoditel]					\n" //
				+ " ,ifnull((select Naimenovanie from Proizvoditel \n"
				+ " 		where n.[OsnovnoyProizvoditel] = Proizvoditel ._IDRRef limit 1 \n"
				+ " 		),1) as [ProizvoditelNaimenovanie] \n"
				+ "	,(select max(Cena) from CenyNomenklaturySklada 					\n" //
				+ "			  where CenyNomenklaturySklada.nomenklatura=n.[_IDRRef] 			\n" //
				+ "			  and Period=(select max(Period) from CenyNomenklaturySklada 			\n" //
				+ "				where nomenklatura=n.[_IDRRef] and date(period)<=date(parameters.dataOtgruzki)))		\n" //
				+ "		as [Cena]	"//
				+ "	,0 as Skidka					\n" //
				+ "	,0 as CenaSoSkidkoy					\n" //
				+ "	,x'00' as VidSkidki					\n" //
				+ "	,eho.[Naimenovanie] as [EdinicyIzmereniyaNaimenovanie]					\n" //
				+ " ,ifnull((select max(VelichinaKvantovNomenklatury.Kolichestvo) from VelichinaKvantovNomenklatury \n"
				+ "	 		where VelichinaKvantovNomenklatury.Nomenklatura = n.[_IDRRef] \n"
				+ "			),1) as [MinNorma] \n"
				+ "	,ei.Koephphicient as [Koephphicient]					\n" //
				+ "	,ei._IDRRef as [EdinicyIzmereniyaID]					\n" //
				+ "	,n.Roditel as Roditel					\n" //
				+ "\n ,(select (1.0+(select ifnull(nacenka1,ifnull(nacenka2,ifnull(nacenka3,ifnull(nacenka4,nacenka5))))"
				+ "\n 	from (select (select nacenka from MinimalnyeNacenkiProizvoditeley_1 where podrazdelenie=p1._idrref and NomenklaturaProizvoditel_2=n._idrref"
				+ "\n and period=(select max(period) from MinimalnyeNacenkiProizvoditeley_1 where podrazdelenie=p1._idrref and NomenklaturaProizvoditel_2=n._idrref)"
				+ "\n ) as nacenka1"
				+ "\n 		,(select nacenka from MinimalnyeNacenkiProizvoditeley_1 where podrazdelenie=p2._idrref and NomenklaturaProizvoditel_2=n._idrref"
				+ "\n and period=(select max(period) from MinimalnyeNacenkiProizvoditeley_1 where podrazdelenie=p2._idrref and NomenklaturaProizvoditel_2=n._idrref)"
				+ "\n ) as nacenka2"
				+ "\n 		,(select nacenka from MinimalnyeNacenkiProizvoditeley_1 where podrazdelenie=p3._idrref and NomenklaturaProizvoditel_2=n._idrref"
				+ "\n and period=(select max(period) from MinimalnyeNacenkiProizvoditeley_1 where podrazdelenie=p3._idrref and NomenklaturaProizvoditel_2=n._idrref)"
				+ "\n ) as nacenka3"
				+ "\n 		,(select nacenka from MinimalnyeNacenkiProizvoditeley_1 where podrazdelenie=p4._idrref and NomenklaturaProizvoditel_2=n._idrref"
				+ "\n and period=(select max(period) from MinimalnyeNacenkiProizvoditeley_1 where podrazdelenie=p3._idrref and NomenklaturaProizvoditel_2=n._idrref)"
				+ "\n ) as nacenka4"
				+ "\n 		,(select nacenka from MinimalnyeNacenkiProizvoditeley_1 where podrazdelenie=X'00000000000000000000000000000000' and NomenklaturaProizvoditel_2=n._idrref"
				+ "\n and period=(select max(period) from MinimalnyeNacenkiProizvoditeley_1 where podrazdelenie=X'00000000000000000000000000000000' and NomenklaturaProizvoditel_2=n._idrref)"
				+ "\n ) as nacenka5"
				+ "\n 		from Podrazdeleniya p1"
				+ "\n 		left join Podrazdeleniya p2 on p1.roditel=p2._idrref"
				+ "\n 		left join Podrazdeleniya p3 on p2.roditel=p3._idrref"
				+ "\n 		left join Podrazdeleniya p4 on p3.roditel=p4._idrref"
				+ "\n 		join Polzovateli on p1._idrref=Polzovateli.podrazdelenie and Polzovateli._idrref= parameters.polzovatel"
				+ "\n 		)"
				+ "\n 	)/100.0)*max(TekuschieCenyOstatkovPartiy.Cena) from TekuschieCenyOstatkovPartiy"
				+ "\n  			where TekuschieCenyOstatkovPartiy.nomenklatura=n.[_IDRRef]"
				+ "\n ) as [MinCena]\n"
				+ "	,(select (1.0+c.[MaksNacenkaCenyPraysa]/100.0)*max(Cena) from CenyNomenklaturySklada 					\n" //
				+ "			where CenyNomenklaturySklada.nomenklatura=n.[_IDRRef] 			\n" //
				+ "			and Period=(select max(Period) from CenyNomenklaturySklada			\n" //
				+ "				where nomenklatura=n.[_IDRRef] and date(period)<=date(parameters.dataOtgruzki)))		\n" //
				+ "		as [MaxCena]				\n" //
				+ "	,(select max(Cena) from TekuschieCenyOstatkovPartiy 					\n" //
				+ "			  where TekuschieCenyOstatkovPartiy.nomenklatura=n.[_IDRRef]) 			\n" //
				+ "		as [BasePrice]				\n" //
				+ "	,(0.0+Prodazhi.Stoimost/Prodazhi.Kolichestvo) as [LastPrice]  				\n" //				
				+ " ,(select max(ProcentSkidkiNacenki) from NacenkiKontr"// 
				+ "			where PoluchatelSkidki=parameters.kontragent"//
				+ "			and Period=(select max(Period) from NacenkiKontr"// 
				+ "				where PoluchatelSkidki=parameters.kontragent and date(period)<=date(parameters.dataOtgruzki)))"//		
				+ "		as [Nacenka]\n"//	
				+ "	 ,(select Individualnye from ZapretSkidokTov where Nomenklatura=n.[_IDRRef]					\n"//
				+ "				and Period=(select max(Period) from ZapretSkidokTov		\n"//
				+ "					where Nomenklatura=n.[_IDRRef] and date(period)<=date(parameters.dataOtgruzki)))	\n"//
				+ "			as [ZapretSkidokTovIndividualnye]			\n"//
				+ "	 ,(select Nokopitelnye from ZapretSkidokTov where Nomenklatura=n.[_IDRRef]					\n"//
				+ "				and Period=(select max(Period) from ZapretSkidokTov		\n"//
				+ "					where Nomenklatura=n.[_IDRRef] and date(period)<=date(parameters.dataOtgruzki)))	\n"//
				+ "			as [ZapretSkidokTovNokopitelnye ]			\n"//
				+ "	 ,(select Partner from ZapretSkidokTov where Nomenklatura=n.[_IDRRef]					\n"//
				+ "				and Period=(select max(Period) from ZapretSkidokTov		\n"//
				+ "					where Nomenklatura=n.[_IDRRef] and date(period)<=date(parameters.dataOtgruzki)))	\n"//
				+ "			as [ZapretSkidokTovPartner ]			\n"//
				+ "	 ,(select Razovie from ZapretSkidokTov where Nomenklatura=n.[_IDRRef]					\n"//
				+ "				and Period=(select max(Period) from ZapretSkidokTov		\n"//
				+ "					where Nomenklatura=n.[_IDRRef] and date(period)<=date(parameters.dataOtgruzki)))	\n"//
				+ "			as [ZapretSkidokTovRazovie ]			\n"//
				+ "	 ,(select Nacenki from ZapretSkidokTov where Nomenklatura=n.[_IDRRef]					\n"//
				+ "				and Period=(select max(Period) from ZapretSkidokTov		\n"//
				+ "					where Nomenklatura=n.[_IDRRef] and date(period)<=date(parameters.dataOtgruzki)))	\n"//
				+ "			as [ZapretSkidokTovNacenki ]			\n"//
				+ "	 ,(select Individualnye from ZapretSkidokProizv where Proizvoditel=n.[OsnovnoyProizvoditel]					\n"//
				+ "				and Period=(select max(Period) from ZapretSkidokProizv 		\n"//
				+ "					where Proizvoditel=n.[OsnovnoyProizvoditel] and date(period)<=date(parameters.dataOtgruzki)))	\n"//
				+ "			as [ZapretSkidokProizvIndividualnye]			\n"//
				+ "	 ,(select Nokopitelnye  from ZapretSkidokProizv where Proizvoditel=n.[OsnovnoyProizvoditel]					\n"//
				+ "				and Period=(select max(Period) from ZapretSkidokProizv 		\n"//
				+ "					where Proizvoditel=n.[OsnovnoyProizvoditel] and date(period)<=date(parameters.dataOtgruzki)))	\n"//
				+ "			as [ZapretSkidokProizvNokopitelnye]			\n"//
				+ "	 ,(select Partner  from ZapretSkidokProizv where Proizvoditel=n.[OsnovnoyProizvoditel]					\n"//
				+ "				and Period=(select max(Period) from ZapretSkidokProizv 		\n"//
				+ "					where Proizvoditel=n.[OsnovnoyProizvoditel] and date(period)<=date(parameters.dataOtgruzki)))	\n"//
				+ "			as [ZapretSkidokProizvPartner ]			\n"//
				+ "	 ,(select Razovie  from ZapretSkidokProizv where Proizvoditel=n.[OsnovnoyProizvoditel]					\n"//
				+ "				and Period=(select max(Period) from ZapretSkidokProizv 		\n"//
				+ "					where Proizvoditel=n.[OsnovnoyProizvoditel] and date(period)<=date(parameters.dataOtgruzki)))	\n"//
				+ "			as [ZapretSkidokProizvRazovie ]			\n"//
				+ "	 ,(select Nacenki from ZapretSkidokProizv where Proizvoditel=n.[OsnovnoyProizvoditel]					\n"//
				+ "				and Period=(select max(Period) from ZapretSkidokProizv 		\n"//
				+ "					where Proizvoditel=n.[OsnovnoyProizvoditel] and date(period)<=date(parameters.dataOtgruzki)))	\n"//
				+ "			as [ZapretSkidokProizvNacenki ]			\n"//
				+ "	 ,(select FixCena from FiksirovannyeCeny where Nomenklatura=n.[_IDRRef] and PoluchatelSkidki=parameters.kontragent					\n"//
				+ "				and Period=(select max(Period) from FiksirovannyeCeny		\n"//
				+ "					where Nomenklatura=n.[_IDRRef] and PoluchatelSkidki=parameters.kontragent and date(period)<=date(parameters.dataOtgruzki) and date(dataokonchaniya)>=date(parameters.dataOtgruzki)))	\n"//
				+ "			as [FiksirovannyeCeny]			\n"//
				+ "	 ,(select ProcentSkidkiNacenki from SkidkaPartneraKarta where PoluchatelSkidki=n.[_IDRRef]					\n"//
				+ "				and Period=(select max(Period) from SkidkaPartneraKarta		\n"//
				+ "					where PoluchatelSkidki=n.[_IDRRef] and date(period)<=date(parameters.dataOtgruzki) and date(DataOkonchaniya)>=date(parameters.dataOtgruzki) ))	\n"//
				+ "			as [SkidkaPartneraKarta]			\n"//
				+ "	 ,(select ProcentSkidkiNacenki from NakopitelnyeSkidki where PoluchatelSkidki=parameters.kontragent					\n"//
				+ "				and Period=(select max(Period) from NakopitelnyeSkidki		\n"//
				+ "					where PoluchatelSkidki=parameters.kontragent and date(period)<=date(parameters.dataOtgruzki)))	\n"//
				+ "			as [NakopitelnyeSkidki]			\n"//
				+ "\n 				,(select max(period) from Prodazhi"
				+ "\n 					join DogovoryKontragentov on Prodazhi .DogovorKontragenta = DogovoryKontragentov._IDRref"
				+ "\n 					where Prodazhi.nomenklatura=n.[_IDRRef] and DogovoryKontragentov .vladelec=parameters.kontragent" + "\n 				) as [LastSell] "
				+ "\n "
				+ "	from Nomenklatura n					\n" //
				+ "	join EdinicyIzmereniya eho on n.EdinicaKhraneniyaOstatkov = eho._IDRRef					\n" //
				+ "	join EdinicyIzmereniya ei on n.EdinicaDlyaOtchetov = ei._IDRRef					\n" //
				+ "	join Consts c \n" //
				//+ uslovieSkladaPodrazdeleniaNeTraphik(ApplicationHoreca.getInstance().getShippingDate().getTime(), ApplicationHoreca.getInstance().getCurrentAgent().skladPodrazdeleniya)
				+ uslovieSkladaPodrazdeleniaNeTraphik(dataOtgruzki, skladPodrazdelenia) + "\n	join (select '"// 
				+ new SimpleDateFormat("yyyy-MM-dd").format(dataOtgruzki)//
				+ "' as dataOtgruzki,"//
				+ clientID//
				+ " as kontragent,"//
				+ agentID//
				+ " as polzovatel"//
				+ ") parameters					\n" //
				+ " join Prodazhi on Prodazhi.Nomenklatura = n._IDRref \n"//
				+ " join DogovoryKontragentov dk on Prodazhi.DogovorKontragenta = dk._IDRref" //
				+ " and date(Prodazhi.period) <= date('" + new SimpleDateFormat("yyyy-MM-dd").format(toDate) //
				+ "') and date(Prodazhi.period) >= date('" + new SimpleDateFormat("yyyy-MM-dd").format(fromDate)//
				+ "') " + " and dk.vladelec=kontragent \n";
		if (searchString.length() > 0) {
			queryStr = queryStr + " and n.[UpperName] like '%" + searchString + "%'";
		}
		queryStr = queryStr + "\n group by n._IDRref";
		queryStr = queryStr + "\n order by n.[Naimenovanie] ";
		queryStr = queryStr + "\n limit " + dataPageSize + " offset " + dataPageSize * page + ";";
		return queryStr;
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
		//historySplit.value(layoutless.width().property.value()-Layoutless.tapSize);
		//System.out.println(layoutless.width().property.value());
	}
}
