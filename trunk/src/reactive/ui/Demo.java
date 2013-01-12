package reactive.ui;

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

import android.view.animation.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;
import tee.binding.*;
import java.io.*;
import java.text.*;

abstract class UIHook {
	public abstract String replace(String s);
}

class KontragentInfo {
	public String _idrref = "";
	public String kod = "";
	public String naimenovanie = "";
}

/*
Балланс партнёров
- Период с
- по
- Только договора группы
- Вид отчёта (Развёрнутый, Объединённый, Свёрнутый, Свёрнутый по договорам)
- Подбор по справочнику Контрагентов
- Подбор по группе договоров
Статистика заказов
- Дата отгрузки
- по
- Дата заказа -1000
- по +1000
- Контрагент
Статусы заказов
- Период с
- по
- Только непроведённые
Дистрибуция
- Период с
- по
Трафики
- Период с
- по
- Все, Только отгруженные, Только неотгруженные
Статусы распоряжений
- Период с
- по
Фиксированные цены
- Период с
- по
Показатели KPI
- Период с
- по
- Дата отгрузки +1
Доставка по водителям
- Период с
- по
Предзаказы на трафики
- Период с
- по
Лимиты
Долги по накладным
*/
public class Demo extends Activity {
	String cacheHRC = null;
	String cachePolzovateli_IDRRef = null;
	String cachePolzovateliPodrazdelenie = null;
	Vector<KontragentInfo> cacheKontragents = new Vector<KontragentInfo>();
	//String[] cacheKontragentLabels;
	SQLiteDatabase cacheSQLiteDatabase = null;
	String dbPath = "/sdcard/horeca/swlife_database";
	WebRender brwsr;
	Layoutless layoutless;
	String tekushieLimityTP = "tekushieLimityTP";
	String gruppadogovorov = "gruppadogovorov";
	Numeric ballansPartnerovFrom = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric ballansPartnerovTo = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric ballansPartnerovVid = new Numeric().value(0);
	Toggle ballansPartnerovTolkoDogGrup = new Toggle().value(true);
	Vector<Integer> ballansPartnerovList = new Vector<Integer>();
	String ballansPartnerovFile = "ballansPartnerov";
	String ballansPartnerovLabel = "Балланс партнёров";
	SubLayoutless ballansPartnerovBox;
	Numeric statistikaZakazovOtgruzkaFrom = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric statistikaZakazovOtgruzkaTo = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric statistikaZakazovFrom = new Numeric().value(dateOnly(Calendar.getInstance(), -100));
	Numeric statistikaZakazovTo = new Numeric().value(dateOnly(Calendar.getInstance(), +100));
	//Numeric statistikaZakazovKontragent = new Numeric().value(dateOnly(Calendar.getInstance()));
	RedactChoice statistikaZakazovKontragent;
	String statistikaZakazovFile = "statistikaZakazov";
	SubLayoutless statistikaZakazovBox;
	Numeric statusyZakazovFrom = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric statusyZakazovTo = new Numeric().value(dateOnly(Calendar.getInstance()));
	Toggle statusyZakazovNepr = new Toggle().value(false);
	String statusyZakazovFile = "statusyZakazov";
	SubLayoutless statusyZakazovBox;
	Numeric distribuciaFrom = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric distribuciaTo = new Numeric().value(dateOnly(Calendar.getInstance()));
	String distribuciaFile = "distribucia";
	SubLayoutless distribuciaBox;
	Numeric traficiFrom = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric traficiTo = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric traficiVseOtgNot = new Numeric().value(0);
	String traficiFile = "trafici";
	SubLayoutless traficiBox;
	Numeric statusyRasporyajeniyFrom = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric statusyRasporyajeniyTo = new Numeric().value(dateOnly(Calendar.getInstance()));
	String statusyRasporyajeniyFile = "statusyRasporyajeniy";
	SubLayoutless statusyRasporyajeniyBox;
	Numeric fixirovannyeCenyFrom = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric fixirovannyeCenyTo = new Numeric().value(dateOnly(Calendar.getInstance()));
	String fixirovannyeCenyFile = "fixirovannyeCeny";
	SubLayoutless fixirovannyeCenyBox;
	Numeric pokazateliKPIFrom = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric pokazateliKPITo = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric pokazateliKPIOtgr = new Numeric().value(dateOnly(Calendar.getInstance(), +1));
	String pokazateliKPIFile = "pokazateliKPI";
	SubLayoutless pokazateliKPIBox;
	Numeric dostavkaPoVoditelyamFrom = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric dostavkaPoVoditelyamTo = new Numeric().value(dateOnly(Calendar.getInstance()));
	String dostavkaPoVoditelyamFile = "dostavkaPoVoditelyam";
	SubLayoutless dostavkaPoVoditelyamBox;
	Numeric predzakazyNaTrafikiFrom = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric predzakazyNaTrafikiTo = new Numeric().value(dateOnly(Calendar.getInstance()));
	String predzakazyNaTrafikiFile = "predzakazyNaTrafiki";
	SubLayoutless predzakazyNaTrafikiBox;
	//Numeric limityFrom = new Numeric().value(dateOnly(Calendar.getInstance()));
	//Numeric limityTo = new Numeric().value(dateOnly(Calendar.getInstance()));
	String limityFile = "limity";
	SubLayoutless limityBox;
	String dolgyPoNakladnymFile = "dolgyPoNakladnym";
	SubLayoutless dolgyPoNakladnymBox;
	SheetColumnText reports = new SheetColumnText();
	Sheet sheet;

	SQLiteDatabase db() {
		if (cacheSQLiteDatabase == null || (!cacheSQLiteDatabase.isOpen())) {
			cacheSQLiteDatabase = Auxiliary.connectSQLiteDatabase("/sdcard/horeca/swlife_database", this, 2);
		}
		cacheSQLiteDatabase.setVersion(2);
		return cacheSQLiteDatabase;
	}
	/*String currentHRC() {
		if (cacheHRC == null) {
			//Bough b = Auxiliary.fromCursor(db().rawQuery("select * from Cur_Users where type=2 order by _id limit 1;", null));
			//cacheHRC = b.child("row").child("Name").value.property.value();
			fillCache();
		}
		return cacheHRC;
	}
	String currentPolzovateli_IDRRef() {
		if (cachePolzovateli_IDRRef == null) {
			fillCache();
		}
		return cachePolzovateli_IDRRef;
	}
	String currentPolzovateliPodrazdelenie() {
		if (cachePolzovateliPodrazdelenie == null) {
			fillCache();
		}
		return cachePolzovateliPodrazdelenie;
	}*/
	void fillCache() {
		System.out.println("fillCache");
		Bough bough = Auxiliary.fromCursor(db().rawQuery(//
				"select Cur_Users.Name,Polzovateli._IDRRef,Polzovateli.Podrazdelenie" //
						+ "\n	from Cur_Users" //
						+ "\n	join Polzovateli on Cur_Users.name=trim(Polzovateli.kod)"//
						+ "\n	where Cur_Users.type=2" //
						+ "\n	order by Polzovateli ._IDRRef" //
						+ "\n	limit 1;"//
				, null));
		cacheHRC = bough.child("row").child("Name").value.property.value();
		cachePolzovateli_IDRRef = bough.child("row").child("_IDRRef").value.property.value();
		cachePolzovateliPodrazdelenie = bough.child("row").child("Podrazdelenie").value.property.value();
		//System.out.println("kontragenty");
		bough = Auxiliary.fromCursor(db().rawQuery(//
				"select Kontragenty._idrref,Kontragenty.kod,Kontragenty.Naimenovanie" //
						+ "\n	from MarshrutyAgentov"//
						+ "\n	join Kontragenty on Kontragenty._idrref=MarshrutyAgentov.Kontragent" //
						+ "\n	where MarshrutyAgentov.agent=X'" + cachePolzovateli_IDRRef + "'"//
						+ "\n	group by Kontragenty._idrref" //
						+ "\n	order by Kontragenty.Naimenovanie"//
						+ "\n	limit 999;"//
				, null));
		/*StringBuilder sb=new StringBuilder();
		System.out.println("dump");
		Bough.dumpXML(sb, bough, "");
		System.out.println(sb.toString());
		System.out.println("done");*/
		/*int n = bough.children.size();
		if (n < 1) {
			n = 1;
		}
		cacheKontragentLabels = new String[n];*/
		for (int i = 0; i < bough.children.size(); i++) {
			Bough row = bough.children.get(i);
			KontragentInfo kontragentInfo = new KontragentInfo();
			kontragentInfo._idrref = row.child("_IDRRef").value.property.value();
			kontragentInfo.kod = row.child("Kod").value.property.value();
			kontragentInfo.naimenovanie = row.child("Naimenovanie").value.property.value();
			//cacheKontragentLabels[i] = kontragentInfo.naimenovanie;
			//System.out.println(kontragentInfo.naimenovanie);
			cacheKontragents.add(kontragentInfo);
		}
	}
	void showBox(SubLayoutless box, String file) {
		ballansPartnerovBox.setVisibility(View.INVISIBLE);
		statistikaZakazovBox.setVisibility(View.INVISIBLE);
		statusyZakazovBox.setVisibility(View.INVISIBLE);
		distribuciaBox.setVisibility(View.INVISIBLE);
		traficiBox.setVisibility(View.INVISIBLE);
		statusyRasporyajeniyBox.setVisibility(View.INVISIBLE);
		fixirovannyeCenyBox.setVisibility(View.INVISIBLE);
		pokazateliKPIBox.setVisibility(View.INVISIBLE);
		dostavkaPoVoditelyamBox.setVisibility(View.INVISIBLE);
		predzakazyNaTrafikiBox.setVisibility(View.INVISIBLE);
		limityBox.setVisibility(View.INVISIBLE);
		dolgyPoNakladnymBox.setVisibility(View.INVISIBLE);
		if (box != null) {
			box.setVisibility(View.VISIBLE);
			final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/horeca/report_" + file + ".html";
			try {
				String show = new File(path).toURL().toString();
				brwsr.go(show);
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
	String replaceLinks(String str, String kind, String p1) {
		StringBuilder html = new StringBuilder(str);
		int start = html.indexOf("№");
		while (start > -1) {
			int end = html.indexOf("<", start + 1);
			if (end > start) {
				String nn = html.substring(start + 1, end);
				nn = nn.replaceAll(" ", "");
				html.replace(start, end, "<a href='?kind=" + kind + "&" + p1 + "=" + nn + "'>" + nn);
			}
			start = html.indexOf("№");
		}
		return html.toString();
	}
	void requestLimityChange(int nn) throws MalformedURLException {
		Numeric lim = new Numeric().value(22);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//System.out.println("go----------------------------------" + Environment.getExternalStorageDirectory().getAbsolutePath());
		layoutless = new Layoutless(this);
		Preferences.init(this);
		statistikaZakazovKontragent = new RedactChoice(this).item("Все");
		new Expect().status.is("Проверка...")//
		.task.is(new Task() {
			@Override
			public void doTask() {
				fillCache();
			}
		})//
		.afterDone.is(new Task() {
			@Override
			public void doTask() {
				//statistikaZakazovKontragent
				for (int i = 0; i < cacheKontragents.size(); i++) {
					statistikaZakazovKontragent.item(cacheKontragents.get(i).naimenovanie);
					//System.out.println(cacheKontragents.get(i).naimenovanie);
				}
			}
		}).start(this);
		brwsr = new WebRender(this)//
		.afterLink.is(new Task() {
			@Override
			public void doTask() {
				try {
					android.net.Uri uri = android.net.Uri.parse(brwsr.url.property.value());
					if (uri.getQueryParameter("kind").equals(tekushieLimityTP)) {
						int nn = Integer.parseInt(uri.getQueryParameter(gruppadogovorov));
						requestLimityChange(nn);
					}
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
		});
		reports.title.is("Отчёты") //		
				.cell("Балланс партнёров", new Task() {
					@Override
					public void doTask() {
						showBox(ballansPartnerovBox, ballansPartnerovFile);
					}
				}) //
				.cell("Статистика заказов", new Task() {
					@Override
					public void doTask() {
						showBox(statistikaZakazovBox, statistikaZakazovFile);
					}
				}) //
				.cell("Статусы заказов", new Task() {
					@Override
					public void doTask() {
						showBox(statusyZakazovBox, statusyZakazovFile);
					}
				}) //
				.cell("Дистрибуция", new Task() {
					@Override
					public void doTask() {
						showBox(distribuciaBox, distribuciaFile);
					}
				}) //
				.cell("Трафики", new Task() {
					@Override
					public void doTask() {
						showBox(traficiBox, traficiFile);
					}
				}) //
				.cell("Статусы распоряжений", new Task() {
					@Override
					public void doTask() {
						showBox(statusyRasporyajeniyBox, statusyRasporyajeniyFile);
					}
				}) //
				.cell("Фиксированные цены", new Task() {
					@Override
					public void doTask() {
						showBox(fixirovannyeCenyBox, fixirovannyeCenyFile);
					}
				}) //
				.cell("Показатели KPI", new Task() {
					@Override
					public void doTask() {
						showBox(pokazateliKPIBox, pokazateliKPIFile);
					}
				}) //
				.cell("Доставка по водителям", new Task() {
					@Override
					public void doTask() {
						showBox(dostavkaPoVoditelyamBox, dostavkaPoVoditelyamFile);
					}
				}) //
				.cell("Предзаказы на трафики", new Task() {
					@Override
					public void doTask() {
						showBox(predzakazyNaTrafikiBox, predzakazyNaTrafikiFile);
					}
				}) //
				.cell("Лимиты", new Task() {
					@Override
					public void doTask() {
						showBox(limityBox, limityFile);
					}
				}) //
				.cell("Долги по накладным", new Task() {
					@Override
					public void doTask() {
						showBox(dolgyPoNakladnymBox, dolgyPoNakladnymFile);
					}
				}) //
		;
		reports.width.is(500);
		int margin = 4;
		int firstColumnStart = (int) (margin + Layoutless.tapSize);
		int firstColumnWidth = 150;
		int secondColumnStart = firstColumnStart + margin + firstColumnWidth + margin;
		int secondColumnWidth = 150;
		int thirdColumnStart = secondColumnStart + secondColumnWidth + margin;
		int thirdColumnWidth = 150;
		int forthColumnStart = thirdColumnStart + thirdColumnWidth + margin;
		int forthColumnWidth = 150;
		int fifthColumnStart = forthColumnStart + forthColumnWidth + margin;
		int fifthColumnWidth = 150;
		int seekBoxHeight = (int) (Layoutless.tapSize + margin) * 2;
		layoutless.child(brwsr//
				.left().is(0)//
				.top().is(seekBoxHeight)//
				.height().is(layoutless.height().property.minus(seekBoxHeight))//
				.width().is(layoutless.width().property.minus(4 + 0.5 * Layoutless.tapSize))//
				);
		layoutless.child(new Decor(this).background.is(Layoutless.themeBlurColor)//
				.top().is(seekBoxHeight - 1).height().is(1).width().is(layoutless.width().property));
		final Numeric split = Preferences.integer("test1", 300);
		new Numeric().bind(layoutless.width().property).afterChange(new Task() {
			@Override
			public void doTask() {
				split.value(0.8 * layoutless.width().property.value());
			}
		});
		ballansPartnerovBox = new SubLayoutless(this);
		ballansPartnerovBox//
				.child(new Decor(this).labelText.is("Период с").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(ballansPartnerovFrom)//
						.left().is(secondColumnStart).top().is(margin).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new Decor(this).labelText.is("по").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin + Layoutless.tapSize).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(ballansPartnerovTo)//
						.left().is(secondColumnStart).top().is(margin + Layoutless.tapSize).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactChoice(this).selection.is(ballansPartnerovVid)//
						.item("Развёрнутый")//
						.item("Объединённый")//
						.item("Свёрнутый")//
						.item("Свёрнутый по договорам")//
						.left().is(thirdColumnStart).top().is(margin).width().is(thirdColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactToggle(this).labelText.is("Договора группы").yes.is(ballansPartnerovTolkoDogGrup)
						//
						.left().is(thirdColumnStart).top().is(margin + Layoutless.tapSize).width().is( thirdColumnWidth).height()
						.is(0.9 * Layoutless.tapSize))//
				.width().is(layoutless.width().property).height().is(seekBoxHeight);
		//ballansPartnerovBox.child(new Decor(this).labelText.is("ballansPartnerovBox").width().is(500).height().is(100)).width().is(500).height().is(200);
		layoutless.child(ballansPartnerovBox);
		statistikaZakazovBox = new SubLayoutless(this);
		statistikaZakazovBox//
				.child(new Decor(this).labelText.is("Дата отгрузки").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(statistikaZakazovOtgruzkaFrom)//
						.left().is(secondColumnStart).top().is(margin).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new Decor(this).labelText.is("по").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin + Layoutless.tapSize).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(statistikaZakazovOtgruzkaTo)//
						.left().is(secondColumnStart).top().is(margin + Layoutless.tapSize).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new Decor(this).labelText.is("Дата заказа").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(thirdColumnStart).top().is(margin).width().is(thirdColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(statistikaZakazovFrom)//
						.left().is(forthColumnStart).top().is(margin).width().is(forthColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new Decor(this).labelText.is("по").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(thirdColumnStart).top().is(margin + Layoutless.tapSize).width().is(thirdColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(statistikaZakazovTo)//
						.left().is(forthColumnStart).top().is(margin + Layoutless.tapSize).width().is(forthColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(statistikaZakazovKontragent//new RedactChoice(this).item("Контрагент").item("Все")//
						.left().is(fifthColumnStart).top().is(margin).width().is(300).height().is(0.9 * Layoutless.tapSize))//
				.width().is(layoutless.width().property).height().is(seekBoxHeight);
		layoutless.child(statistikaZakazovBox);
		statusyZakazovBox = new SubLayoutless(this);
		statusyZakazovBox//
				.child(new Decor(this).labelText.is("Период с").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(statusyZakazovFrom)//
						.left().is(secondColumnStart).top().is(margin).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new Decor(this).labelText.is("по").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin + Layoutless.tapSize).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(statusyZakazovTo)//
						.left().is(secondColumnStart).top().is(margin + Layoutless.tapSize).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactToggle(this).labelText.is("Только непроведённые").yes.is(statusyZakazovNepr)//
						.left().is(thirdColumnStart).top().is(margin).width().is(forthColumnWidth + margin + thirdColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.width().is(layoutless.width().property).height().is(seekBoxHeight);
		layoutless.child(statusyZakazovBox);
		distribuciaBox = new SubLayoutless(this);
		distribuciaBox//
				.child(new Decor(this).labelText.is("Период с").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(distribuciaFrom)//
						.left().is(secondColumnStart).top().is(margin).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new Decor(this).labelText.is("по").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin + Layoutless.tapSize).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(distribuciaTo)//
						.left().is(secondColumnStart).top().is(margin + Layoutless.tapSize).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.width().is(layoutless.width().property).height().is(seekBoxHeight);
		layoutless.child(distribuciaBox);
		traficiBox = new SubLayoutless(this);
		traficiBox//
				.child(new Decor(this).labelText.is("Период с").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(traficiFrom)//
						.left().is(secondColumnStart).top().is(margin).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new Decor(this).labelText.is("по").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin + Layoutless.tapSize).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(traficiTo)//
						.left().is(secondColumnStart).top().is(margin + Layoutless.tapSize).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactChoice(this)//
						.item("Все")//
						.item("Только отгруженные")//
						.item("Только неотгруженные")//
				.selection.is(traficiVseOtgNot)//
						.left().is(thirdColumnStart).top().is(margin).width().is(thirdColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.width().is(layoutless.width().property).height().is(seekBoxHeight);
		layoutless.child(traficiBox);
		statusyRasporyajeniyBox = new SubLayoutless(this);
		statusyRasporyajeniyBox//
				.child(new Decor(this).labelText.is("Период с").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(statusyRasporyajeniyFrom)//
						.left().is(secondColumnStart).top().is(margin).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new Decor(this).labelText.is("по").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin + Layoutless.tapSize).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(statusyRasporyajeniyTo)//
						.left().is(secondColumnStart).top().is(margin + Layoutless.tapSize).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.width().is(layoutless.width().property).height().is(seekBoxHeight);
		layoutless.child(statusyRasporyajeniyBox);
		fixirovannyeCenyBox = new SubLayoutless(this);
		fixirovannyeCenyBox//
				.child(new Decor(this).labelText.is("Период с").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(fixirovannyeCenyFrom)//
						.left().is(secondColumnStart).top().is(margin).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new Decor(this).labelText.is("по").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin + Layoutless.tapSize).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(fixirovannyeCenyTo)//
						.left().is(secondColumnStart).top().is(margin + Layoutless.tapSize).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.width().is(layoutless.width().property).height().is(seekBoxHeight);
		layoutless.child(fixirovannyeCenyBox);
		pokazateliKPIBox = new SubLayoutless(this);
		pokazateliKPIBox//
				.child(new Decor(this).labelText.is("Период с").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(pokazateliKPIFrom)//
						.left().is(secondColumnStart).top().is(margin).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new Decor(this).labelText.is("по").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin + Layoutless.tapSize).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(pokazateliKPITo)//
						.left().is(secondColumnStart).top().is(margin + Layoutless.tapSize).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new Decor(this).labelText.is("Дата отгрузки").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(thirdColumnStart).top().is(margin).width().is(thirdColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(pokazateliKPIOtgr)//
						.left().is(forthColumnStart).top().is(margin).width().is(forthColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.width().is(layoutless.width().property).height().is(seekBoxHeight);
		layoutless.child(pokazateliKPIBox);
		dostavkaPoVoditelyamBox = new SubLayoutless(this);
		dostavkaPoVoditelyamBox//
				.child(new Decor(this).labelText.is("Период с").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(dostavkaPoVoditelyamFrom)//
						.left().is(secondColumnStart).top().is(margin).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new Decor(this).labelText.is("по").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin + Layoutless.tapSize).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(dostavkaPoVoditelyamTo)//
						.left().is(secondColumnStart).top().is(margin + Layoutless.tapSize).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.width().is(layoutless.width().property).height().is(seekBoxHeight);
		layoutless.child(dostavkaPoVoditelyamBox);
		predzakazyNaTrafikiBox = new SubLayoutless(this);
		predzakazyNaTrafikiBox//
				.child(new Decor(this).labelText.is("Период с").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(predzakazyNaTrafikiFrom)//
						.left().is(secondColumnStart).top().is(margin).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new Decor(this).labelText.is("по").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin + Layoutless.tapSize).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(predzakazyNaTrafikiTo)//
						.left().is(secondColumnStart).top().is(margin + Layoutless.tapSize).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.width().is(layoutless.width().property).height().is(seekBoxHeight);
		layoutless.child(predzakazyNaTrafikiBox);
		limityBox = new SubLayoutless(this);
		limityBox//
				.width().is(layoutless.width().property).height().is(seekBoxHeight);
		layoutless.child(limityBox);
		dolgyPoNakladnymBox = new SubLayoutless(this);
		dolgyPoNakladnymBox//
				.width().is(layoutless.width().property).height().is(seekBoxHeight);
		layoutless.child(dolgyPoNakladnymBox);
		layoutless.child(new KnobImage(this)//.labelText.is("Послать") //
				.bitmap.is(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.email), (int) (0.6 * Layoutless.tapSize),
						(int) (0.6 * Layoutless.tapSize), true))//
				.tap.is(new Task() {
					@Override
					public void doTask() {
						if (statusyZakazovBox.getVisibility() == View.VISIBLE) {
							sendFile(statusyZakazovFile);
						}
					}
				}).left().is(margin).top().is(margin).width().is(0.9 * Layoutless.tapSize).height().is(0.9 * Layoutless.tapSize));
		layoutless.child(new KnobImage(this).bitmap.is(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.refresh),
				(int) (0.6 * Layoutless.tapSize), (int) (0.6 * Layoutless.tapSize), true))//
				.tap.is(new Task() {
					@Override
					public void doTask() {
						if (statistikaZakazovBox.getVisibility() == View.VISIBLE) {
							Calendar calendar = Calendar.getInstance();
							calendar.setTimeInMillis(statistikaZakazovFrom.value().longValue());
							int fromY = calendar.get(Calendar.YEAR);
							int fromM = calendar.get(Calendar.MONTH) + 1;
							int fromD = calendar.get(Calendar.DAY_OF_MONTH);
							calendar.setTimeInMillis(statistikaZakazovTo.value().longValue());
							int toY = calendar.get(Calendar.YEAR);
							int toM = calendar.get(Calendar.MONTH) + 1;
							int toD = calendar.get(Calendar.DAY_OF_MONTH);
							String kontragent = "";
							int s = statistikaZakazovKontragent.selection.property.value().intValue() - 1;
							if (s >= 0 && s < cacheKontragents.size()) {
								kontragent = "\n				<Param xmlns=\"http://ws.swl/Param\">"//
										+ "\n					<Name>Контрагент</Name>"//
										+ "\n					<Value>" + cacheKontragents.get(s).kod + "</Value>"//
										+ "\n					<Tipe>Значение</Tipe>"//
										+ "\n					<TipeElem>Число</TipeElem>"//
										+ "\n				</Param>";
							}
							//System.out.println(fromY +"/"+ pad2(fromM) +"/"+ pad2(fromD));
							invokeReportAndroid(//
									statistikaZakazovOtgruzkaFrom.value().longValue(), statistikaZakazovOtgruzkaTo.value().longValue()//
									, "СтатистикеЗаказовHRC", statistikaZakazovFile, statistikaZakazovBox//
									, "<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"//
											+ "\n				<Param xmlns=\"http://ws.swl/Param\">"//
											+ "\n					<Name>ЗаказыПокупателей</Name>"//
											+ "\n					<Value>Истина</Value>"//
											+ "\n					<Tipe>Значение</Tipe>"//
											+ "\n					<TipeElem>Булево</TipeElem>"//
											+ "\n				</Param>"//
											+ "\n				<Param xmlns=\"http://ws.swl/Param\">"//
											+ "\n					<Name>НачЗабития</Name>"//
											+ "\n					<Value>" + fromY + pad2(fromM) + pad2(fromD) + "000000</Value>"//
											+ "\n					<Tipe>Значение</Tipe>"//
											+ "\n					<TipeElem>Дата</TipeElem>"//
											+ "\n				</Param>"//
											+ "\n				<Param xmlns=\"http://ws.swl/Param\">"//
											+ "\n					<Name>КонЗабития</Name>"//
											+ "\n					<Value>" + toY + pad2(toM) + pad2(toD) + "235959</Value>"//
											+ "\n					<Tipe>Значение</Tipe>"//
											+ "\n					<TipeElem>Дата</TipeElem>"//
											+ "\n				</Param>"//
											+ kontragent//
											+ "\n			</m:Параметры>");
						}
						if (statusyZakazovBox.getVisibility() == View.VISIBLE) {
							invokeReportAndroid(//
									statusyZakazovFrom.value().longValue(), statusyZakazovTo.value().longValue()//
									, "СтатусыЗаказов", statusyZakazovFile, statusyZakazovBox//
									, "					<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"//
											+ "						<Param xmlns=\"http://ws.swl/Param\">" //
											+ "							<Name>ТолькоНеПроведенные</Name>" //
											+ "							<Value>" + (statusyZakazovNepr.value() ? "Истина" : "Ложь") + "</Value>" //
											+ "							<Tipe>Значение</Tipe>" //
											+ "							<TipeElem>Булево</TipeElem>"// 
											+ "						</Param>"// 
											+ "					</m:Параметры>"//
							);
						}
						if (distribuciaBox.getVisibility() == View.VISIBLE) {
							invokeReportAndroid(//
									distribuciaFrom.value().longValue(), distribuciaTo.value().longValue()//
									, "Дистрибуция", distribuciaFile, distribuciaBox//
									, "<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" />");
						}
						if (traficiBox.getVisibility() == View.VISIBLE) {
							//
							String otgr = "Ложь";
							String neotgr = "Ложь";
							if (traficiVseOtgNot.value() == 1) {
								otgr = "Истина";
							}
							if (traficiVseOtgNot.value() == 2) {
								neotgr = "Истина";
							}
							invokeReportAndroid(//
									traficiFrom.value().longValue(), traficiTo.value().longValue()//
									, "ТрафикиПоТП", traficiFile, traficiBox//
									, "<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"//
											+ "				<Param xmlns=\"http://ws.swl/Param\">"//
											+ "					<Name>ТолькоОтгружено</Name>"//
											+ "					<Value>" + otgr + "</Value>"//
											+ "					<Tipe>Значение</Tipe>"//
											+ "					<TipeElem>Булево</TipeElem>"//
											+ "				</Param>"//
											+ "				<Param xmlns=\"http://ws.swl/Param\">"//
											+ "					<Name>ТолькоНеОтгружено</Name>"//
											+ "					<Value>" + neotgr + "</Value>"//
											+ "					<Tipe>Значение</Tipe>"//
											+ "					<TipeElem>Булево</TipeElem>"//
											+ "				</Param>"//
											+ "			</m:Параметры>");
						}
						if (statusyRasporyajeniyBox.getVisibility() == View.VISIBLE) {
							invokeReportAndroid(//
									statusyRasporyajeniyFrom.value().longValue(), statusyRasporyajeniyTo.value().longValue()//
									, "СтатусыРаспоряжений", statusyRasporyajeniyFile, statusyRasporyajeniyBox//
									, "<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" />");
						}
						if (fixirovannyeCenyBox.getVisibility() == View.VISIBLE) {
							//Date f = new Date();
							//f.setTime(fixirovannyeCenyFrom.value().longValue());
							//System.out.println(f);
							invokeReportAndroid(//
									fixirovannyeCenyFrom.value().longValue(), fixirovannyeCenyTo.value().longValue()//
									, "ЗаявкиНаФиксированныеЦены", fixirovannyeCenyFile, fixirovannyeCenyBox//
									, "<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" />");
						}
						if (pokazateliKPIBox.getVisibility() == View.VISIBLE) {
							Calendar calendar = Calendar.getInstance();
							calendar.setTimeInMillis(pokazateliKPIOtgr.value().longValue());
							int fromY = calendar.get(Calendar.YEAR);
							int fromM = calendar.get(Calendar.MONTH) + 1;
							int fromD = calendar.get(Calendar.DAY_OF_MONTH);
							invokeReportAndroid(//
									pokazateliKPIFrom.value().longValue(), pokazateliKPITo.value().longValue()//
									, "Показатели", pokazateliKPIFile, pokazateliKPIBox//
									, "<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"//
											+ "				<Param xmlns=\"http://ws.swl/Param\">"//
											+ "					<Name>ДатаОтгрузкиСегодня</Name>"//
											+ "					<Value>" + fromY + pad2(fromM) + pad2(fromD) + "235959</Value>"//
											+ "					<Tipe>Значение</Tipe>"//
											+ "					<TipeElem>Дата</TipeElem>"//
											+ "				</Param>"//
											+ "			</m:Параметры>");
						}
						if (dostavkaPoVoditelyamBox.getVisibility() == View.VISIBLE) {
							invokeReportAndroid(//
									dostavkaPoVoditelyamFrom.value().longValue(), dostavkaPoVoditelyamTo.value().longValue()//
									, "ДоставкаПоВодителям", dostavkaPoVoditelyamFile, dostavkaPoVoditelyamBox//
									, "<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" />");
						}
						if (predzakazyNaTrafikiBox.getVisibility() == View.VISIBLE) {
							invokeReportAndroid(//
									predzakazyNaTrafikiFrom.value().longValue(), predzakazyNaTrafikiTo.value().longValue()//
									, "ОтчетПоТрафикамHRC", predzakazyNaTrafikiFile, predzakazyNaTrafikiBox//
									, "<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" />");
						}
						if (limityBox.getVisibility() == View.VISIBLE) {
							invokeReportAndroid(//
									0, 0//
									, "ТекущиеЛимитыТП", limityFile, limityBox//
									, "<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" />");
						}
						//dolgyPoNakladnym
						if (dolgyPoNakladnymBox.getVisibility() == View.VISIBLE) {
							invokeReportAndroid(//
									0, 0//
									, "ДолгиПоНакладным", dolgyPoNakladnymFile, dolgyPoNakladnymBox//
									, "<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" />");
						}
					}
				}).left().is(margin).top().is(margin + Layoutless.tapSize).width().is(0.9 * Layoutless.tapSize).height().is(0.9 * Layoutless.tapSize));
		layoutless.child(new Decor(this)//
				.background.is(Layoutless.themeBackgroundColor)//
						.left().is(split)//
						.width().is(layoutless.width().property).height().is(layoutless.height().property));
		sheet = new Sheet(this);
		layoutless.child(new SplitLeftRight(this)//
				.split.is(split).rightSide(sheet)//
						.height().is(layoutless.height().property).width().is(layoutless.width().property));
		showBox(statistikaZakazovBox, statistikaZakazovFile);
		setContentView(layoutless);
		sheet.data(new SheetColumn[] { reports });
		sheet.reset();
		sheet.selectedRow.is(1);
		sheet.refreshSelection();
	}
	double dateOnly(Calendar c) {
		return dateOnly(c, 0);
	}
	double dateOnly(Calendar c, int days) {
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTimeInMillis();
	}
	String pad2(int n) {
		String r = "" + n;
		if (n < 10) {
			r = "0" + r;
		}
		return r;
	}
	void sendFile(String name) {
		String path = reportPath(name);
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		//intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
		intent.putExtra(Intent.EXTRA_SUBJECT, "Отчёт");
		//intent.putExtra(Intent.EXTRA_TEXT, "body text");
		Uri uri = Uri.fromFile(new File(path));
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		//startActivity(Intent.createChooser(intent, "Send email..."));
		startActivity(intent);
	}
	String currentReportLabel() {
		String r = "?";
		int num = sheet.selectedRow.property.value().intValue();
		if (num >= 0 && num < reports.cells.size()) {
			r = reports.cells.get(num);
		}
		return r;
	}
	/*void invokeReportAndroid(long from, long to, String reportName, String reportFile, SubLayoutless reportBox) {
		invokeReportAndroid(from, to, reportName, reportFile, reportBox,
				"<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" />");
	}*/
	void invokeReportAndroid(final long from, final long to, final String reportName, final String reportFile, final SubLayoutless reportBox, final String parameters) {
		final Toggle cancel = new Toggle();
		//final Toggle ok = new Toggle();
		final RawSOAP rawSOAP = new RawSOAP();
		new Expect().status.is(currentReportLabel() + "...")//
		.cancel.is(cancel)//
		.afterDone.is(new Task() {
			@Override
			public void doTask() {
				//System.out.println(" r.statusCode " + rawSOAP.statusCode.property.value());
				//System.out.println(" r.statusDescription " + rawSOAP.statusDescription.property.value());
				//System.out.println(" r.exception " + rawSOAP.exception.property.value());
				if (rawSOAP.statusCode.property.value() >= 100 //
						&& rawSOAP.statusCode.property.value() <= 300//
						&& rawSOAP.exception.property.value() == null//
				) {
					showBox(reportBox, reportFile);
				}
				else {
					String descr = "Ошибка: " + rawSOAP.statusCode.property.value() + ": " + rawSOAP.statusDescription.property.value();
					if (rawSOAP.exception.property.value() != null) {
						descr = descr + ": " + rawSOAP.exception.property.value().toString();
					}
					Auxiliary.warn(descr, Demo.this);
				}
			}
		})//
		.task.is(new Task() {
			@Override
			public void doTask() {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(from);
				int fromY = calendar.get(Calendar.YEAR);
				int fromM = calendar.get(Calendar.MONTH) + 1;
				int fromD = calendar.get(Calendar.DAY_OF_MONTH);
				calendar = Calendar.getInstance();
				calendar.setTimeInMillis(to);
				int toY = calendar.get(Calendar.YEAR);
				int toM = calendar.get(Calendar.MONTH) + 1;
				int toD = calendar.get(Calendar.DAY_OF_MONTH);
				rawSOAP.xml.is("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>"//
						+ "\n		<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"//
						+ "\n			<soap:Body>"//
						+ "\n				<m:getReport xmlns:m=\"http://ws.swl/fileHRC\">"//
						+ "\n					<m:Имя xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"//
						+ reportName + "</m:Имя>"//
						+ "\n					<m:НачалоПериода xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"// 
						+ ((fromY + "-" + pad2(fromM) + "-" + pad2(fromD)) + "T00:00:00</m:НачалоПериода>")//
						+ "\n					<m:КонецПериода xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"// 
						+ ((toY + "-" + pad2(toM) + "-" + pad2(toD)) + "T23:59:59</m:КонецПериода>")//
						+ "\n					<m:КодПользователя xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"//
						+ cacheHRC.toUpperCase() + "</m:КодПользователя>"//
						+ "\n" + parameters//
						+ "\n				</m:getReport>" //
						+ "\n			</soap:Body>"//
						+ "\n		</soap:Envelope>")//
				.url.is("http://78.40.186.186/ReportAndroid.1cws")//				
				.responseEncoding.is("cp-1251")//
				;
				//ok.value(
				rawSOAP.startNow();
				//);
				if (rawSOAP.statusCode.property.value() >= 100 //
						&& rawSOAP.statusCode.property.value() <= 300//
						&& rawSOAP.exception.property.value() == null//
				) {
					if (!cancel.value()) {
						//System.out.println(" r.statusCode " + r.statusCode.property.value());
						String s = rawSOAP.data.child("soap:Body")//
								.child("m:getReportResponse")//
								.child("m:return")//
								.child("m:Data")//
						.value.property.value();
						//System.out.println("response "+s);
						saveTextToFile(Base64.decode(s, Base64.DEFAULT), reportPath(reportFile));
					}
				}
				//else {
				//Auxiliary.warn("ops", Demo.this);
				//System.out.println(" r.statusCode " + r.statusCode.property.value());
				//System.out.println(" r.statusDescription " + r.statusDescription.property.value());
				//}
			}
		}).start(Demo.this);
	}
	boolean saveTextToFile(byte[] bytes, String file) {
		try {
			String txt = new String(bytes, "UTF-8");
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(txt.getBytes("UTF-8"));
			fileOutputStream.flush();
			fileOutputStream.close();
			return true;
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}
	String reportPath(String name) {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + "/horeca/report_" + name + ".html";
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("First");
		menu.add("Second");
		menu.add("Third");
		MenuItem mi = menu.add("item4");
		SubMenu sm = menu.addSubMenu("Sub...");
		sm.add("subitem 1");
		sm.add("subitem 2");
		SubMenu sm2 = sm.addSubMenu("another submenu...");
		sm2.add("another subitem");
		menu.add("item5");
		menu.add("item6");
		menu.add("item7");
		menu.add("item8");
		menu.add("item9");
		menu.add("item10");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//System.out.println("onOptionsItemSelected " + item.getTitle());
		// Handle item selection
		/* switch (item.getItemId()) {
		     case R.id.new_game:
		         newGame();
		         return true;
		     case R.id.help:
		         showHelp();
		         return true;
		     default:
		         return super.onOptionsItemSelected(item);
		 }*/
		return false;
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		/*
		//System.out.println("onBackPressed");
		if (layoutless.removeDialog()) {
			//System.out.println("onBackPressed removeDialog");
		}
		else {
			//System.out.println("onBackPressed super");
			super.onBackPressed();
		}*/
	}
	@Override
	protected void onPause() {
		//System.out.println("onPause");
		Preferences.save();
		if (cacheSQLiteDatabase != null) {
			//System.out.println("CLOSE DB");
			cacheSQLiteDatabase.close();
			cacheSQLiteDatabase = null;
		}
		super.onPause();
	}
}
