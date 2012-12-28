package reactive.ui;

import android.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.Bundle;
import android.os.Environment;
import android.util.*;
import android.view.*;
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

public class Demo extends Activity {
	Browser brwsr;
	Layoutless view;
	boolean lock = false;
	String tekushieLimityTP = "tekushieLimityTP";
	String gruppadogovorov = "gruppadogovorov";

	/*
		void trafikiPoTP() {
			b.go("http://78.40.186.186/ReportAndroid.1cws");
		}*/
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
		//System.out.println("requestLimityChange " + nn);
		Numeric lim = new Numeric().value(22);
		//Dialogs.prompt("Лимит клиента " + nn, this, null, lim, "Запросить");
		/*
		final String file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/horeca/report_" + tekushieLimityTP + ".html";
		String show = new File(file).toURL().toString();		
		brwsr.go(show);
		*/
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("go----------------------------------" + Environment.getExternalStorageDirectory().getAbsolutePath());
		view = new Layoutless(this);
		final Sheet testSheet = new Sheet(this).headerHeight.is(0);
		brwsr = new Browser(this).afterLink.is(new Task() {
			@Override
			public void doTask() {
				// TODO Auto-generated method stub
				//System.out.println("url " + brwsr.url.property.value());
				try {
					//URL url = new URL(brwsr.url.property.value());
					//System.out.println(url.getQuery());
					android.net.Uri uri = android.net.Uri.parse(brwsr.url.property.value());
					if (uri.getQueryParameter("kind").equals(tekushieLimityTP)) {
						int nn = Integer.parseInt(uri.getQueryParameter(gruppadogovorov));
						//System.out.println("gruppadogovorov " + nn);
						requestLimityChange(nn);
					}
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
				//brwsr.go("http://yandex.ru");
			}
		});
		/*b.active.property.afterChange(new Task(){

			@Override
			public void doTask() {
				System.out.println("afterChange "+b.active.property.value());
			}});*/
		SheetColumnText links = new SheetColumnText();
		/*Task open = new Task() {
			@Override
			public void doTask() {
				//System.out.println(testSheet.selectedRow.property.value());
				int nn = testSheet.selectedRow.property.value().intValue();
				if (nn == 0) {
					b.go("http://yandex.ru");
				}
				if (nn == 1) {
					b.go("http://google.com");
				}
				if (nn == 2) {
					trafikiPoTP();
				}
			}
		};*/
		//Numeric s=new Numeric();
		//s.value(1000);
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
						view.addDialog(d);
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
										/*
										//System.out.println(str);
										StringBuilder html = new StringBuilder(str);
										//html.append(html);
										int start = html.indexOf("№");
										while (start > -1) {
											int end = html.indexOf("<", start + 1);
											if (end > start) {
												String nn = html.substring(start + 1, end);
												nn = nn.replaceAll(" ", "");
												html.replace(start, end, "<a href='?report=tekushieLimityTP&n=" + nn + "'>" + nn);
											}
											start = html.indexOf("№");
										}
										//System.out.println(html.toString());
										//return s.replace("№", "<a href=\"ops\">№</a>");
										return html.toString();
										*/
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
		;
		final SplitLeftRight slr=new SplitLeftRight(this);
		
		view//
		.child(slr//
				
				.leftSide(
				//new EditString(this)
						brwsr//
				)
				//
				//.rightSide(testSheet//
				//.data(new SheetColumn[] { links})//
				//)//
				
		.split.is(1000)//
				.width().is(view.width().property)//
				.height().is(view.height().property)//
		)//
				.child(new Decor(this).labelText.is("xzdfvfvsdvf").left().is(300).top().is(120).width().is(300).height().is(50))//
				.child(new RedactText(this).left().is(100).top().is(20).width().is(300).height().is(50))//
				.child(new SubLayoutless(this)//
						.child(new Decor(this).labelText.is("inner").background.is(0x99ff00ff)//
								.width().is(300).height().is(50))//
								.left().is(400).top().is(200).width().is(300).height().is(200))//
				.child(new Knob(this).tap.is(new Task() {
					@Override
					public void doTask() {
						//System.out.println("afbvsfbsfbv");
						//slr.debug();
					}
				}).left().is(50).top().is(220).width().is(100).height().is(50))//
		;
		//view.child(nn)
		testSheet.data(new SheetColumn[] { links });
		testSheet.reset();
		setContentView(view);
	}
	void showPage(String xml, String url, String name, final UIHook hook) {
		if (lock) {
			System.out.println("locked");
			return;
		}
		lock = true;
		System.out.println("showPage " + name);
		final String file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/horeca/report_" + name + ".html";
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
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					//fileOutputStream.write(pdfAsBytes);
					fileOutputStream.write(html.getBytes("UTF-8"));
					fileOutputStream.flush();
					fileOutputStream.close();
					String show = new File(file).toURL().toString();
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
		if (view.removeDialog()) {
			//System.out.println("onBackPressed removeDialog");
		}
		else {
			//System.out.println("onBackPressed super");
			super.onBackPressed();
		}
	}
}
