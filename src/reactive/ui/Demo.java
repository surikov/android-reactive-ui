package reactive.ui;

import android.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.Bundle;
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

public class Demo extends Activity {
	Browser b;
	Layoutless view;

	void trafikiPoTP() {
		b.go("http://78.40.186.186/ReportAndroid.1cws");
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("go----------------------------------");
		view = new Layoutless(this);
		final Sheet testSheet = new Sheet(this).headerHeight.is(0);
		b = new Browser(this);
		/*b.active.property.afterChange(new Task(){

			@Override
			public void doTask() {
				System.out.println("afterChange "+b.active.property.value());
			}});*/
		SheetColumnText links = new SheetColumnText();
		Task open = new Task() {
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
		};
		//Numeric s=new Numeric();
		//s.value(1000);
		links.title.is("") //						
				.cell("yandex") //
				.cell("google") //
				.cell("ТрафикиПоТП")//
		.afterCellTap.is(open)//
		.width.is(500)//
		;
		view//
		.child(new SplitLeftRight(this)//
				.leftSide(b//
				)
				//
				.rightSide(testSheet//
				//.data(new SheetColumn[] { links})//
				)//
		.split.is(1000)//
				.width().is(view.width().property)//
				.height().is(view.height().property)//
		)//
		;
		testSheet.data(new SheetColumn[] { links });
		testSheet.reset();
		setContentView(view);
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
}
