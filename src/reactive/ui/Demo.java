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
import reactive.ui.library.LayoutlessView;
import android.view.animation.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;
import java.io.*;
import java.text.*;

public class Demo extends Activity {
	Layoutless view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("go----------------------------------");
		view = new Layoutless(this);
		//view .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Bitmap b = Bitmap.createScaledBitmap(//
				BitmapFactory.decodeResource(getResources(), R.drawable.rocket)//
				, 200, 100//
				, true//
				);
		Numeric ix = new Numeric();
		Numeric iy = new Numeric();
		//Layoutless.fillBaseColors();
		final Sheet testSheet = new Sheet(this);
		view//
		.maxZoom.is(3).zoom.is(view.zoom.property).innerHeight.is(1000).innerWidth.is(1500)
		//
				.child(new Decor(this)//
				.labelText.is("Very long string for testing purpose only.")//
						.labelStyleLargeNormal()//
				//.left.is(100)
				//.gravity.is(Gravity.CENTER)//
				.bitmap.is(b)//
				.background.is(Color.RED)//
						//.foreground.is(Color.YELLOW)//
						//.textAppearance.is(android.R.style.TextAppearance_Large)//
						//.labelFace.is(Typeface.createFromAsset(getAssets(), "fonts/deftone.ttf"))//
						.sketch(new SketchPlate()//
						//.strokeWidth.is(5)//
						.background.is(0x6600ffff)//
						//.strokeColor.is(0xff00ff00)//
						.width.is(250)//
						.height.is(350)//
						.top.is(10)//
						.left.is(30)//
						)//
						.sketch(new SketchContour()//
						.strokeWidth.is(1)//
						//.background.is(0x6600ffff)//
						.strokeColor.is(0xff00ff00)//
						.width.is(250)//
						.height.is(150)//
						.top.is(10)//
						.left.is(30)//
						.arcX.is(48)//
						.arcY.is(48)//
						)//
				/*.afterTap.is(new Task() {
					@Override
					public void doTask() {
						//System.out.println("tap decor");
					}
				})*/
				.afterDrag.is(new Task() {
					@Override
					public void doTask() {
						//System.out.println("shift decor");
					}
				})//
						.left().is(view.shiftX.property.plus(100))//
						.top().is(view.shiftY.property)//
						.width().is(view.zoom.property.multiply(50).plus(540))//
						.height().is(view.zoom.property.multiply(50).plus(400))//
						.view())//
		.afterTap.is(new Task() {
			@Override
			public void doTask() {
				//System.out.println("tap view " + view.tapX.property.value() + "x" + view.tapY.property.value());
			}
		})//
		.afterShift.is(new Task() {
			@Override
			public void doTask() {
				//System.out.println("shift view " + view.shiftX.property.value() + "x" + view.shiftY.property.value());
			}
		})//
		.afterZoom.is(new Task() {
			@Override
			public void doTask() {
				//System.out.println("zoom view " + view.zoom.property.value());
			}
		})//
				.child(new Decor(this)//
				.background.is(0xff00ff66)
				//.active.is(true)//
				.movableX.is(true)//
				.movableY.is(true)//
						/*.afterTap.is(new Task() {
							@Override
							public void doTask() {
								System.out.println("tap green");
							}
						})*/
						.top().is(view.shiftY.property.plus(200))//
						.left().is(view.shiftX.property.plus(400))//
						.width().is(view.zoom.property.multiply(50).plus(140))//
						.height().is(view.zoom.property.multiply(50).plus(100))//
						.view()//
				)//
				.child(new Decor(this)//
				.background.is(0xff3300ff)
				//.active.is(true)//
				.movableY.is(true)//
						/*.afterTap.is(new Task() {
							@Override
							public void doTask() {
								System.out.println("tap green");
							}
						})*/
						.top().is(view.shiftY.property.plus(250))//
						.left().is(view.shiftX.property.plus(350))//
						.width().is(view.zoom.property.multiply(50).plus(140))//
						.height().is(view.zoom.property.multiply(50).plus(100))//
						.view()//
				)//
				.child(new SplitLeftRight(this)//
						.leftSide(new SubLayoutless(this)//
								.shiftX.is(ix)//
								.shiftY.is(iy)//
										.child(new Knob(this)//
										.labelText.is("clear")//
										.tap.is(new Task() {
											@Override
											public void doTask() {
												System.out.println("clear");
												testSheet.clear();
											}
										})//
												.left().is(ix)//
												.top().is(iy)//
												.view()//
										)//
										.child(new Knob(this)//
										.labelText.is("test data")//
										.tap.is(new Task() {
											@Override
											public void doTask() {
												System.out.println("test data");
												testSheet.clear();
												testSheet.column(new SheetColumnText()//
														.title.is("Number")
																
																.cell("1z")//
																.cell("2z")//
																.cell("3z")//
																.cell("4z")//
																.cell("5z")//
														.width.is(400)//
														);
												testSheet.fill();
											}
										})//
												.left().is(ix.plus(100))//
												.top().is(iy)//
												.view()//
										)//
										.child(new Knob(this)//
										.labelText.is("3")//
												.left().is(ix.plus(200))//
												.top().is(iy)//
												.view()//
										)//
								.innerHeight.is(500)//
								.innerWidth.is(500)//
						)//
						.rightSide(
								testSheet//
										.column(new SheetColumnText()//
										.title.is("Number")
												//						
												.cell("1")
												//
												.cell("2")
												//
												.cell("3. В лесу родилась ёлочка, в лесу она росла. Зимой и летом стройная, зелёная была. Метель ей пела песенку: \"Спи ёлочка, бай-бай\", мороз снежком укутывал: \"Смотри, не замерзай!\".")//
												.cell("4")//
												.cell("5")//
												.cell("1")//
												.cell("2")//
												.cell("3")//
												.cell("4")//
												.cell("5")//
												.cell("1")//
												.cell("2")//
												.cell("3")//
												.cell("4")//
												.cell("5")//
										.width.is(200)//
										)//
										.column(new SheetColumnText()//
										.title.is("Text")//						
												.cell("aa")//
												.cell("bb")//
												.cell("cc")//
												.cell("dd")//
												.cell("ee")//
												.cell("aa")//
												.cell("bb")//
												.cell("cc")//
												.cell("dd")//
												.cell("ee")//
												.cell("aa")//
												.cell("bb")//
												.cell("cc")//
												.cell("dd")//
												.cell("ee")//
										.width.is(250)//
										)//
										.column(new SheetColumnText()//
										.title.is("Any")//						
												.cell("aa11")//
												.cell("bb22", 0x99009900)//
												.cell("cc33")//
												.cell("dd44", 0x99009900)//
												.cell("ee55", 0x99009900)//
												.cell("aa11", 0x99009900)//
												.cell("bb22", 0x99000099)//
												.cell("cc33", 0x99009900)//
												.cell("dd44")//
												.cell("ee55")//
												.cell("aa11")//
												.cell("bb22", 0x99990000)//
												.cell("cc33")//
												.cell("dd44")//
												.cell("ee55")//
										.width.is(170)//
										.afterHeaderTap.is(new Task() {
											@Override
											public void doTask() {
												System.out.println("header Any");
											}
										}).afterCellTap.is(new Task() {
											@Override
											public void doTask() {
												System.out.println("cell Any");
											}
										})))
						//.solid.is(false)
						/*
								.leftChild((new Knob(this)//
								.width.is(200)//
								.height.is(200)//
								.labelText.is("left")//
								)//
								)//
								.rightChild((new Knob(this)//
								.width.is(100)//
								.height.is(100)//
								.labelText.is("right")//
								)//
								)//
								*/
						//.left.is(200)
						//.top.is(50)
						.width().is(view.width().property)//
						.height().is(view.height().property)//
						.view())//
		;
		setContentView(view);
		//testSheet.clear();
		testSheet.fill();
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
