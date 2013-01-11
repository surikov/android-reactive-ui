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

import reactive.ui.*;

import java.net.*;

import android.view.animation.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;
import java.io.*;
import java.text.*;

abstract class UIHook {
	public abstract String replace(String s);
}

/*
Балланс партнёров
- Период с
- по
- Только договора группы
- Вид отчёта (Развёрнутый, Объединённый, Свёрнутый, Свёрнутый по договрам)
- Подбор по справочнику Контрагентов
- Подбор по группе договоров
Статистика заказов
- Дата отгрузки
- по
- Дата заказа
- по
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
- Дата отгрузки
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
	WebRender brwsr;
	Layoutless layoutless;
	//boolean lock = false;
	String tekushieLimityTP = "tekushieLimityTP";
	String gruppadogovorov = "gruppadogovorov";
	String ballansPartnerovFile = "ballansPartnerov";
	String ballansPartnerovLabel = "Балланс партнёров";
	SubLayoutless ballansPartnerovBox;
	Task ballansPartnerov = new Task() {
		@Override
		public void doTask() {
			System.out.println("ballansPartnerov");
			showBox(ballansPartnerovBox, ballansPartnerovFile);
		}
	};
	Numeric statistikaZakazovOtgruzkaFrom = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric statistikaZakazovOtgruzkaTo = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric statistikaZakazovFrom = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric statistikaZakazovTo = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric statistikaZakazovKontragent = new Numeric().value(dateOnly(Calendar.getInstance()));
	String statistikaZakazovFile = "statistikaZakazov";
	//String statistikaZakazovLabel = "Статистика заказов";
	SubLayoutless statistikaZakazovBox;
	Task statistikaZakazov = new Task() {
		@Override
		public void doTask() {
			System.out.println("statistikaZakazov");
			showBox(statistikaZakazovBox, statistikaZakazovFile);
		}
	};
	Numeric statusyZakazovFrom = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric statusyZakazovTo = new Numeric().value(dateOnly(Calendar.getInstance()));
	Toggle statusyZakazovNepr = new Toggle().value(false);
	String statusyZakazovFile = "statusyZakazov";
	//String statusyZakazovLabel = "Статусы заказов";
	SubLayoutless statusyZakazovBox;
	Task statusyZakazov = new Task() {
		@Override
		public void doTask() {
			showBox(statusyZakazovBox, statusyZakazovFile);
		}
	};
	Numeric distribuciaS = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric distribuciaPo = new Numeric().value(dateOnly(Calendar.getInstance()));
	String distribuciaFile = "distribucia";
	//String distribuciaLabel = "Дистрибуция";
	SubLayoutless distribuciaBox;
	Task distribucia = new Task() {
		@Override
		public void doTask() {
			System.out.println("distribucia");
			showBox(distribuciaBox, distribuciaFile);
		}
	};
	Numeric traficiFrom = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric traficiTo = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric traficiVseOtgNot = new Numeric().value(dateOnly(Calendar.getInstance()));
	String traficiFile = "trafici";
	//String traficiLabel = "Трафики";
	SubLayoutless traficiBox;
	Task trafici = new Task() {
		@Override
		public void doTask() {
			System.out.println("trafici");
			showBox(traficiBox, traficiFile);
		}
	};
	Numeric statusyRasporyajeniyFrom = new Numeric().value(dateOnly(Calendar.getInstance()));
	Numeric statusyRasporyajeniyTo = new Numeric().value(dateOnly(Calendar.getInstance()));
	String statusyRasporyajeniyFile = "statusyRasporyajeniy";
	//String statusyRasporyajeniyLabel = "Статусы распоряжений";
	SubLayoutless statusyRasporyajeniyBox;
	Task statusyRasporyajeniy = new Task() {
		@Override
		public void doTask() {
			System.out.println("statusyRasporyajeniy");
			showBox(statusyRasporyajeniyBox, statusyRasporyajeniyFile);
		}
	};
	String fixirovannyeCenyFile = "fixirovannyeCeny";
	//String fixirovannyeCenyLabel = "Фиксированные цены";
	SubLayoutless fixirovannyeCenyBox;
	Task fixirovannyeCeny = new Task() {
		@Override
		public void doTask() {
			System.out.println("fixirovannyeCeny");
			showBox(fixirovannyeCenyBox, fixirovannyeCenyFile);
		}
	};
	String pokazateliKPIFile = "pokazateliKPI";
	//String pokazateliKPILabel = "Показатели KPI";
	SubLayoutless pokazateliKPIBox;
	Task pokazateliKPI = new Task() {
		@Override
		public void doTask() {
			System.out.println("pokazateliKPI");
			showBox(pokazateliKPIBox, pokazateliKPIFile);
		}
	};
	String dostavkaPoVoditelyamFile = "dostavkaPoVoditelyam";
	//String dostavkaPoVoditelyamLabel = "Доставка по водителям";
	SubLayoutless dostavkaPoVoditelyamBox;
	Task dostavkaPoVoditelyam = new Task() {
		@Override
		public void doTask() {
			System.out.println("dostavkaPoVoditelyam");
			showBox(dostavkaPoVoditelyamBox, dostavkaPoVoditelyamFile);
		}
	};
	String predzakazyNaTrafikiFile = "predzakazyNaTrafiki";
	//String predzakazyNaTrafikiLabel = "Предзаказы на трафики";
	SubLayoutless predzakazyNaTrafikiBox;
	Task predzakazyNaTrafiki = new Task() {
		@Override
		public void doTask() {
			System.out.println("predzakazyNaTrafiki");
			showBox(predzakazyNaTrafikiBox, predzakazyNaTrafikiFile);
		}
	};
	String limityFile = "limity";
	//String limityLabel = "Лимиты";
	SubLayoutless limityBox;
	Task limity = new Task() {
		@Override
		public void doTask() {
			System.out.println("limity");
			showBox(limityBox, limityFile);
		}
	};
	String dolgyPoNakladnymFile = "dolgyPoNakladnym";
	//String dolgyPoNakladnymLabel = "Долги по накладным";
	SubLayoutless dolgyPoNakladnymBox;
	Task dolgyPoNakladnym = new Task() {
		@Override
		public void doTask() {
			System.out.println("dolgyPoNakladnym");
			showBox(dolgyPoNakladnymBox, dolgyPoNakladnymFile);
		}
	};
	SheetColumnText reports = new SheetColumnText();
	Sheet sheet;

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
		System.out.println("go----------------------------------" + Environment.getExternalStorageDirectory().getAbsolutePath());
		layoutless = new Layoutless(this);
		Preferences.init(this);
		//final Sheet testSheet = new Sheet(this).headerHeight.is(0);
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
		//System.out.println(brwsr.getContext());
		reports.title.is("Отчёты") //		
				.cell("Балланс партнёров", ballansPartnerov) //
				.cell("Статистика заказов", statistikaZakazov) //
				.cell("Статусы заказов", statusyZakazov) //
				.cell("Дистрибуция", distribucia) //
				.cell("Трафики", trafici) //
				.cell("Статусы распоряжений", statusyRasporyajeniy) //
				.cell("Фиксированные цены", fixirovannyeCeny) //
				.cell("Показатели KPI", pokazateliKPI) //
				.cell("Доставка по водителям", dostavkaPoVoditelyam) //
				.cell("Предзаказы на трафики", predzakazyNaTrafiki) //
				.cell("Лимиты", limity) //
				.cell("Долги по накладным", dolgyPoNakladnym) //
		//.width.is(500)//
		;
		reports.width.is(500);
		/*SheetColumnText links = new SheetColumnText();
		links.title.is("Reports from 1C") //	
				.cell("yandex", new Task() {
					@Override
					public void doTask() {
						System.out.println("ya");
						//Dialogs.prompt("Лимит клиента ",Demo.this, null, null, "Запросить");
						SubLayoutless d = new SubLayoutless(Demo.this);
						d.width().is(500).height().is(300);
						d.child(new Decor(Demo.this)//
						.background.is(Layoutless.themeBackgroundColor)//
								.width().is(d.width().property.value())//
								.height().is(d.height().property.value())//
						);
						d.child(new Decor(Demo.this)//
						.labelText.is("Клиент")//
						.background.is(Layoutless.themeBlurColor)//
								.width().is(d.width().property.value())//
								.height().is(0.5 * Layoutless.tapSize)//
								.left().is(8)//
								.top().is(4)//
						);
						d.child(new Decor(Demo.this)//
						.labelText.is("Новый лимит, т.р.")//
						.background.is(Layoutless.themeBlurColor)//
								.labelAlignRightBottom().width().is(200)//
								.height().is(Layoutless.tapSize)//
								.left().is(8)//
								.top().is(4 + 0.5 * Layoutless.tapSize)//
						);
						d.child(new RedactText(Demo.this)//
								//.labelText.is("Новый лимит, т.р.")//
								//.background.is(Layoutless.themeBlurColor)//
								//.labelAlignRightBottom()
								.width().is(300)//
								.height().is(Layoutless.tapSize)//
								.left().is(8)//
								.top().is(200 + 4 + 0.5 * Layoutless.tapSize)//
						);
						layoutless.addDialog(d);
					}
				}) //
				.cell("google", new Task() {
					@Override
					public void doTask() {
						System.out.println("google");
					}
				}) //
				.cell("ТрафикиПоТП", new Task() {
					@Override
					public void doTask() {
						showPage(
								"<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>"//
										+ "\n		<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"//
										+ "\n			<soap:Body>"//
										+ "\n				<m:getReport xmlns:m=\"http://ws.swl/fileHRC\">"//
										+ "\n					<m:Имя xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">ТрафикиПоТП</m:Имя>"//
										+ "\n					<m:НачалоПериода xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">2012-12-25T00:00:00</m:НачалоПериода>"//
										+ "\n					<m:КонецПериода xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">2012-12-25T23:59:59</m:КонецПериода>"//
										+ "\n					<m:КодПользователя xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">HRC260</m:КодПользователя>"//
										+ "\n					<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"//
										+ "\n						<Param xmlns=\"http://ws.swl/Param\">"//
										+ "\n							<Name>ТолькоОтгружено</Name>"//
										+ "\n							<Value>Ложь</Value>"//
										+ "\n							<Tipe>Значение</Tipe>"//
										+ "\n							<TipeElem>Булево</TipeElem>"//
										+ "\n						</Param>"//
										+ "\n						<Param xmlns=\"http://ws.swl/Param\">"//
										+ "\n							<Name>ТолькоНеОтгружено</Name>"//
										+ "\n							<Value>Ложь</Value>"//
										+ "\n							<Tipe>Значение</Tipe>"//
										+ "\n							<TipeElem>Булево</TipeElem>"//
										+ "\n						</Param>"//
										+ "\n					</m:Параметры>"//
										+ "\n				</m:getReport>"//
										+ "\n			</soap:Body>"//
										+ "\n		</soap:Envelope>"//
								, "http://78.40.186.186/ReportAndroid.1cws"//
								, "trafikiPoTP"//
								, null//
						);
					}
				})//
				.cell("ТекущиеЛимитыТП", new Task() {
					@Override
					public void doTask() {
						//showTekushieLimityTP();
						showPage(
								"<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>"//
										+ "\n		<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"//
										+ "\n			<soap:Body>"//
										+ "\n				<m:getReport xmlns:m=\"http://ws.swl/fileHRC\">"//
										+ "\n					<m:Имя xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">ТекущиеЛимитыТП</m:Имя>"//
										+ "\n					<m:НачалоПериода xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">2012-12-25T00:00:00</m:НачалоПериода>"//
										+ "\n					<m:КонецПериода xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">2012-12-25T23:59:59</m:КонецПериода>"//
										+ "\n					<m:КодПользователя xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">HRC260</m:КодПользователя>"//
										+ "\n					<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"//
										+ "\n						<Param xmlns=\"http://ws.swl/Param\">"//
										+ "\n							<Name>ТолькоОтгружено</Name>"//
										+ "\n							<Value>Ложь</Value>"//
										+ "\n							<Tipe>Значение</Tipe>"//
										+ "\n							<TipeElem>Булево</TipeElem>"//
										+ "\n						</Param>"//
										+ "\n						<Param xmlns=\"http://ws.swl/Param\">"//
										+ "\n							<Name>ТолькоНеОтгружено</Name>"//
										+ "\n							<Value>Ложь</Value>"//
										+ "\n							<Tipe>Значение</Tipe>"//
										+ "\n							<TipeElem>Булево</TipeElem>"//
										+ "\n						</Param>"//
										+ "\n					</m:Параметры>"//
										+ "\n				</m:getReport>"//
										+ "\n			</soap:Body>"//
										+ "\n		</soap:Envelope>"//
								, "http://78.40.186.186/ReportAndroid.1cws"//
								, tekushieLimityTP//
								, new UIHook() {
									@Override
									public String replace(String str) {
										return replaceLinks(str, tekushieLimityTP, gruppadogovorov);
										
										//System.out.println(str);
										//StringBuilder html = new StringBuilder(str);
										//html.append(html);
										//int start = html.indexOf("№");
										//while (start > -1) {
		//											int end = html.indexOf("<", start + 1);
											//if (end > start) {
		//												String nn = html.substring(start + 1, end);
												//nn = nn.replaceAll(" ", "");
												//html.replace(start, end, "<a href='?report=tekushieLimityTP&n=" + nn + "'>" + nn);
											//}
											//start = html.indexOf("№");
										//}
										//System.out.println(html.toString());
										//return s.replace("№", "<a href=\"ops\">№</a>");
		//										return html.toString();
										
									}
								}//
						);
					}
				})//
				.cell("1111111111111111111111", null)//
				.cell("222222222", null)//
				.cell("3333333333333333", null)//
				.cell("444444444444", null)//
				.cell("55555555555555", null)//
				.cell("66666666666666666", null)//
				.cell("7777777777777777", null)//
				.cell("888888888888888", null)//
				.cell("99999999999999", null)//
				.cell("00000000000000000000", null)//
				.cell("1111111111111111111111", null)//
				.cell("2222222222222", null)//
				.cell("33333333333", null)//
				.cell("44444444444444", null)//
		//.afterCellTap.is(open)//
		.width.is(500)//
		;*/
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
		/*
		layoutless.child(new Decor(this).labelText.is("from:").labelAlignRightCenter().labelStyleMediumNormal()//
				.left().is(16).top().is(16).height().is(0.8 * Layoutless.tapSize).width().is(200));
		layoutless.child(new RedactText(this)//
				.left().is(200 + 16 + 8).top().is(16).height().is(0.8 * Layoutless.tapSize).width().is(250));
		layoutless.child(new Decor(this).labelText.is("to:").labelAlignRightCenter().labelStyleMediumNormal()//
				.left().is(16).top().is(16 + Layoutless.tapSize).height().is(0.8 * Layoutless.tapSize).width().is(200));
		layoutless.child(new RedactText(this)//
				.left().is(200 + 16 + 8).top().is(16 + Layoutless.tapSize).height().is(0.8 * Layoutless.tapSize).width().is(250));
		layoutless.child(new Knob(this).labelText.is("Refresh").tap.is(new Task() {
			@Override
			public void doTask() {
				brwsr.go("http://yandex.ru");
			}
		})//
		
				.left().is(200 + 16 + 8 + 250 + 8).top().is(16 + Layoutless.tapSize).height().is(0.8 * Layoutless.tapSize).width().is(100));
				*/
		layoutless.child(new Decor(this).background.is(Layoutless.themeBlurColor)//
				.top().is(seekBoxHeight - 1).height().is(1).width().is(layoutless.width().property));
		//System.out.println(layoutless.width().property.value());
		final Numeric split = Preferences.integer("test1", 300);
		new Numeric().bind(layoutless.width().property).afterChange(new Task() {
			@Override
			public void doTask() {
				split.value(0.8 * layoutless.width().property.value());
			}
		});
		/*
		*/
		//
		ballansPartnerovBox = new SubLayoutless(this);
		ballansPartnerovBox.child(new Decor(this).labelText.is("ballansPartnerovBox").width().is(500).height().is(100)).width().is(500).height().is(200);
		layoutless.child(ballansPartnerovBox);
		//
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
				.child(new Knob(this).labelText.is("Контрагент")//
						.left().is(fifthColumnStart).top().is(margin).width().is(300).height().is(0.9 * Layoutless.tapSize))//
				.width().is(layoutless.width().property).height().is(seekBoxHeight);
		layoutless.child(statistikaZakazovBox);
		//
		statusyZakazovBox = new SubLayoutless(this);
		//statusyZakazovBox.child(new Decor(this).labelText.is("statusyZakazovBox").width().is(500).height().is(100));
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
		//
		distribuciaBox = new SubLayoutless(this);
		distribuciaBox//
				.child(new Decor(this).labelText.is("Период с").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(distribuciaS)//
						.left().is(secondColumnStart).top().is(margin).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new Decor(this).labelText.is("по").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(firstColumnStart).top().is(margin + Layoutless.tapSize).width().is(firstColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new RedactDate(this).format.is("dd.MM.yyyy").date.is(distribuciaPo)//
						.left().is(secondColumnStart).top().is(margin + Layoutless.tapSize).width().is(secondColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.width().is(layoutless.width().property).height().is(seekBoxHeight);
		layoutless.child(distribuciaBox);
		//
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
				.child(new Decor(this).labelText.is("Тип").labelAlignRightCenter().labelStyleMediumNormal()//
						.left().is(thirdColumnStart).top().is(margin).width().is(thirdColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.child(new Knob(this).labelText.is("------")//
						.left().is(forthColumnStart).top().is(margin).width().is(forthColumnWidth).height().is(0.9 * Layoutless.tapSize))//
				.width().is(layoutless.width().property).height().is(seekBoxHeight);
		layoutless.child(traficiBox);
		//
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
		//
		fixirovannyeCenyBox = new SubLayoutless(this);
		//
		pokazateliKPIBox = new SubLayoutless(this);
		//
		dostavkaPoVoditelyamBox = new SubLayoutless(this);
		//
		predzakazyNaTrafikiBox = new SubLayoutless(this);
		//
		limityBox = new SubLayoutless(this);
		//
		dolgyPoNakladnymBox = new SubLayoutless(this);
		//
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
				//.labelText.is("Обновить") //
				.tap.is(new Task() {
					@Override
					public void doTask() {
						if (statusyZakazovBox.getVisibility() == View.VISIBLE) {
							statusyZakazovShow(statusyZakazovFrom.value().longValue(), statusyZakazovTo.value().longValue(), statusyZakazovNepr.value());
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
		//System.out.println("setContentView");
		showBox(statistikaZakazovBox, statistikaZakazovFile);
		setContentView(layoutless);
		//System.out.println(brwsr.getContext());
		//System.out.println(layoutless.height().property.value());
		//System.out.println("done onCreate");
		sheet.data(new SheetColumn[] { reports });
		sheet.reset();
		sheet.selectedRow.is(1);
		sheet.refreshSelection();
	}
	double dateOnly(Calendar c) {
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
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
			//System.out.println(reports.cells.get(num));
			r=reports.cells.get(num);
		}
		return r;
	}
	void statusyZakazovShow(final long from, final long to, final boolean nepr) {
		final Toggle cancel = new Toggle();
		new Expect().status.is(currentReportLabel()+"...")//
		.cancel.is(cancel).afterDone.is(new Task() {
			@Override
			public void doTask() {
				//System.out.println("refresh start");
				showBox(statusyZakazovBox, statusyZakazovFile);
			}
		})//
		.task.is(new Task() {
			@Override
			public void doTask() {
				//System.out.println("request start");
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(from);
				int fromY = calendar.get(Calendar.YEAR);
				int fromM = calendar.get(Calendar.MONTH) + 1;
				int fromD = calendar.get(Calendar.DAY_OF_YEAR);
				calendar = Calendar.getInstance();
				calendar.setTimeInMillis(to);
				int toY = calendar.get(Calendar.YEAR);
				int toM = calendar.get(Calendar.MONTH) + 1;
				int toD = calendar.get(Calendar.DAY_OF_YEAR);
				final RawSOAP r = new RawSOAP();
				r.xml.is("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>"//
						+ "		<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"//
						+ "			<soap:Body>"//
						+ "				<m:getReport xmlns:m=\"http://ws.swl/fileHRC\">"//
						+ "					<m:Имя xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">СтатусыЗаказов</m:Имя>"//
						+ "					<m:НачалоПериода xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"// 
						+ ((fromY + "-" + pad2(fromM) + "-" + pad2(fromD)) + "T00:00:00</m:НачалоПериода>")//
						+ "					<m:КонецПериода xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"// 
						+ ((toY + "-" + pad2(toM) + "-" + pad2(toD)) + "T23:59:59</m:КонецПериода>")//
						+ "					<m:КодПользователя xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">HRC267</m:КодПользователя>"//
						+ "					<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"//
						+ "						<Param xmlns=\"http://ws.swl/Param\">" //
						+ "							<Name>ТолькоНеПроведенные</Name>" //
						+ "							<Value>" + (nepr ? "Истина" : "Ложь") + "</Value>" + "							<Tipe>Значение</Tipe>" //
						+ "							<TipeElem>Булево</TipeElem>"// 
						+ "						</Param>"// 
						+ "					</m:Параметры>"// 
						+ "				</m:getReport>" //
						+ "			</soap:Body>"//
						+ "		</soap:Envelope>")//
				.url.is("http://78.40.186.186/ReportAndroid.1cws")//				
				.responseEncoding.is("cp-1251")//
				;
				r.startNow();
				if (!cancel.value()) {
					saveTextToFile(Base64.decode(r.data.child("soap:Body")//
							.child("m:getReportResponse")//
							.child("m:return")//
							.child("m:Data")//
							.value.property.value()//
							, Base64.DEFAULT), reportPath(statusyZakazovFile));
				}
				//lock = false;
				//System.out.println("request done");
			}
		}).start(Demo.this);
		/*
		Calendar fCalendar = Calendar.getInstance();
		fCalendar.setTimeInMillis(from);
		int fromY = fCalendar.get(Calendar.YEAR);
		int fromM = fCalendar.get(Calendar.MONTH) + 1;
		int fromD = fCalendar.get(Calendar.DAY_OF_YEAR);
		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTimeInMillis(to);
		int toY = toCalendar.get(Calendar.YEAR);
		int toM = toCalendar.get(Calendar.MONTH) + 1;
		int toD = toCalendar.get(Calendar.DAY_OF_YEAR);
		String xml = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>"//
				+ "		<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"//
				+ "			<soap:Body>"//
				+ "				<m:getReport xmlns:m=\"http://ws.swl/fileHRC\">"//
				+ "					<m:Имя xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">СтатусыЗаказов</m:Имя>"//
				+ "					<m:НачалоПериода xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"// 
				+ fromY + "-" + pad2(fromM) + "-" + pad2(fromD) + "T00:00:00</m:НачалоПериода>"//
				+ "					<m:КонецПериода xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"// 
				+ toY + "-" + pad2(toM) + "-" + pad2(toD) + "T23:59:59</m:КонецПериода>"//
				+ "					<m:КодПользователя xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">HRC267</m:КодПользователя>"//
				+ "					<m:Параметры xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"//
				+ "						<Param xmlns=\"http://ws.swl/Param\">" //
				+ "							<Name>ТолькоНеПроведенные</Name>" //
				+ "							<Value>" + (nepr ? "Истина" : "Ложь") + "</Value>" + "							<Tipe>Значение</Tipe>" //
				+ "							<TipeElem>Булево</TipeElem>"// 
				+ "						</Param>"// 
				+ "					</m:Параметры>"// 
				+ "				</m:getReport>" //
				+ "			</soap:Body>"//
				+ "		</soap:Envelope>"//
		;*/
		//showPage(xml, "http://78.40.186.186/ReportAndroid.1cws", "test", null);
	}
	boolean saveTextToFile(byte[] bytes, String file) {
		try {
			//System.out.println("saveTextToFile "+file+"\n"+txt);
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
	/*
	void showPage(String xml, String url, final String name, final UIHook hook) {
		if (lock) {
			System.out.println("locked");
			return;
		}
		lock = true;
		System.out.println("showPage " + name + "\n" + xml);
		//final String file = reportPath(name);
		//final String show =new File(file).toURL().toString(); 
		//"file:///sdcard/horeca/report" + name + ".html";
		final RawSOAP r = new RawSOAP();
		r.xml.is(xml)//
		.url.is(url)//
		.afterError.is(new Task() {
			@Override
			public void doTask() {
				System.out.println("afterError "//
						+ r.statusCode.property.value()//
						+ " / " + r.statusDescription.property.value()//
						+ " / " + r.exception.property.value());
				lock = false;
			}
		})//
		.afterSuccess.is(new Task() {
			@Override
			public void doTask() {
				System.out.println("afterSuccess");
				String data = r.data.child("soap:Body").child("m:getReportResponse").child("m:return").child("m:Data").value.property.value();
				byte[] bytes = Base64.decode(data, Base64.DEFAULT);
				try {
					String html = new String(bytes, "UTF-8");
					if (hook != null) {
						html = hook.replace(html);
					}
					FileOutputStream fileOutputStream = new FileOutputStream(reportPath(name));
					//fileOutputStream.write(pdfAsBytes);
					fileOutputStream.write(html.getBytes("UTF-8"));
					fileOutputStream.flush();
					fileOutputStream.close();
					String show = new File(reportPath(name)).toURL().toString();
					brwsr.go(show);
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
				lock = false;
			}
		})//
		.responseEncoding.is("cp-1251")//
		;
		r.start();
	}
	void exec() {
	}
	void lock() {
	}
	void unlock() {
	}*/
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
		System.out.println("onOptionsItemSelected " + item.getTitle());
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
		//System.out.println("onBackPressed");
		if (layoutless.removeDialog()) {
			//System.out.println("onBackPressed removeDialog");
		}
		else {
			//System.out.println("onBackPressed super");
			super.onBackPressed();
		}
	}
	@Override
	protected void onPause() {
		Preferences.save();
		super.onPause();
	}
}
