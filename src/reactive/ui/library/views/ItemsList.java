package reactive.ui.library.views;

import android.content.*;
import android.graphics.*;
import tee.binding.*;
import tee.binding.properties.*;
import android.view.*;
import tee.binding.task.*;
import tee.binding.it.*;
import android.util.*;

import java.util.*;

import reactive.ui.library.*;
import reactive.ui.library.views.cells.*;
import reactive.ui.library.views.figures.*;

public class ItemsList extends ViewGroup implements Unbind {
	//public Vector<BitmapTitleDescription> items = new Vector<BitmapTitleDescription>();
	private Vector<View> children = new Vector<View>();
	Context context;
	private static double tapDiff = 8;
	private static double pluckDiff = 8;
	private final static int NONE = 0;
	private final static int SCROLL = 1;
	private final static int PLUCK = 2;
	private int dragMode = NONE;
	private float lastEventY = 0;
	private int initialX = 0;
	private int initialShiftY = 0;
	private int initialY = 0;
	private Numeric shiftY = new Numeric();
	public ItProperty<ItemsList, Task> tap = new ItProperty<ItemsList, Task>(this);
	public ItProperty<ItemsList, Task> drop = new ItProperty<ItemsList, Task>(this);
	private int innerHeight = 0;
	private float density = 1;
	public NumericProperty<ItemsList> cellHeight = new NumericProperty<ItemsList>(this);
	//public NumericProperty<SimpleList> iconWidth = new NumericProperty<SimpleList>(this);
	public Numeric iconMargin = new Numeric();
	public Numeric selection = new Numeric();
	public NumericProperty<ItemsList> id = new NumericProperty<ItemsList>(this);
	public NoteProperty<ItemsList> uuid = new NoteProperty<ItemsList>(this);
	public NumericProperty<ItemsList> highlight = new NumericProperty<ItemsList>(this);
	public NumericProperty<ItemsList> pluckX = new NumericProperty<ItemsList>(this);
	public NumericProperty<ItemsList> pluckY = new NumericProperty<ItemsList>(this);
	//public NumericProperty<SimpleList> leftMargin = new NumericProperty<SimpleList>(this);
	//public NumericProperty<SimpleList> rightMargin = new NumericProperty<SimpleList>(this);
	public NumericProperty<ItemsList> topMargin = new NumericProperty<ItemsList>(this);
	public NumericProperty<ItemsList> bottomMargin = new NumericProperty<ItemsList>(this);
	public NumericProperty<ItemsList> dropX = new NumericProperty<ItemsList>(this);
	public NumericProperty<ItemsList> dropY = new NumericProperty<ItemsList>(this);
	//public They<Bitmap> icons = new They<Bitmap>();
	//public They<String> titles = new They<String>();
	//public They<String> descriptions = new They<String>();
	private Numeric listWidth = new Numeric().value(100);
	private ViewBag dragLayer;
	public They<SimpleListCell> cells = new They<SimpleListCell>();
	private Numeric bgTop = new Numeric();
	private View pluckBackground;
	private View pluckTitle;
	private View pluckDescription;
	private View pluckBitmap;
	private View pluckWhiteboard;
	private Task hlTask = new Task() {

		@Override
		public void doTask() {
			bgTop.value(shiftY.value() + topMargin.property.value() + cellHeight.property.value() * selection.value());

		}
	};

	public ItemsList(Context c, They<SimpleListCell> incells, ViewBag inDragLayer) {
		super(c);
		dragLayer = inDragLayer;
		context = c;
		this.cells.bind(incells);
		//this.icons.bind(inicons);
		//this.titles.bind(intitles);
		//this.descriptions.bind(indescriptions);
		density = context.getResources().getDisplayMetrics().density;
		tapDiff = 16 * density;
		pluckDiff = 50 * density;
		cellHeight.is(60 * density);
		//iconWidth.is(60 * density);
		iconMargin.value(4 * density);
		highlight.property.value(0xff000000);
		/*leftMargin.property.afterChange(new Task() {

			@Override
			public void doTask() {
				reLayoutChildren();

			}
		});*/
		/*rightMargin.property.afterChange(new Task() {

			@Override
			public void doTask() {
				reLayoutChildren();

			}
		});*/
		topMargin.property.afterChange(new Task() {

			@Override
			public void doTask() {
				reLayoutChildren();

			}
		});
		bottomMargin.property.afterChange(new Task() {

			@Override
			public void doTask() {
				reLayoutChildren();

			}
		});
		cells.afterChange(new Task() {

			@Override
			public void doTask() {
				resetRows();

			}
		});

		//resetRows();
		SimpleRectangle selectionBackground = new SimpleRectangle(c).color.is(highlight.property);
		//selectionBackground.layout(0, 0, 100, 50);

		shiftY.afterChange(hlTask);
		ViewBox box = new ViewBox().view.is(selectionBackground)//
		.width.is(listWidth)//
		.height.is(cellHeight.property)//
		.top.is(bgTop)//
		;
		this.addView(selectionBackground);
	}

	/*
		public SimpleList rows(They<BitmapTitleDescription> data) {
			this.rows.bind(data);
			return this;
		}*/
	public void removeChildren() {
		int sz = children.size();
		for (int i = 0; i < sz; i++) {
			//Tools.log("removeView " + children.get(i));
			this.removeView(children.get(i));
		}
		children.removeAllElements();
	}

	public void resetRows() {
		//Tools.log("resetRows start");
		removeChildren();
		this.innerHeight = (int) (cellHeight.property.value() * cells.size());
		for (int i = 0; i < cells.size(); i++) {
			//String title = "";
			//title = cells.at(i).title.property.value();
			//Tools.log("add row " + cells.at(i).title.property.value());
			SimpleRectangle rectangle = new SimpleRectangle(context).color.is(cells.at(i).background.property.value());
			SimpleString titleString = new SimpleString(context)//
			.text.is(cells.at(i).title.property)//
			.size.is(18 * density)//
			.color.is(cells.at(i).color.property);
			//String description = "";
			//description = cells.at(i).description.property.value();
			SimpleString descriptionString = new SimpleString(context)//
			.text.is(cells.at(i).description.property)//
			.size.is(11 * density)//
			.color.is(cells.at(i).color.property.value());
			SimpleIcon icon = new SimpleIcon(context)//
			.bitmap.is(cells.at(i).bitmap.property);
			WhiteBoard wb = new WhiteBoard(context);
			//wb.figure(new LineStroke(wb));
			Vector<Figure> figures = cells.at(i).figures;
			for (int f = 0; f < figures.size(); f++) {
				wb.figure(figures.get(f));
			}
			this.addView(rectangle);
			this.addView(titleString);
			this.addView(descriptionString);
			this.addView(icon);
			this.addView(wb);
			children.add(rectangle);
			children.add(titleString);
			children.add(descriptionString);
			children.add(icon);
			children.add(wb);
		}
		this.shiftY.value(0);
		this.selection.value(0);
		setID();
		reLayoutChildren();
		/*for (int i = 0; i < cells.size(); i++) {
			Tools.log(i + ": " + children.get(i * 5).getLeft() + "/" + children.get(i * 5).getTop() + "/" + children.get(i * 5).getWidth() + "/" + children.get(i * 5).getHeight());
		}*/
		//Tools.log("resetRows done");

	}

	private void setID() {
		int nn = this.selection.value().intValue();
		if (nn >= 0 && nn < this.cells.size()) {
			this.id.is(this.cells.at(nn).id.property.value());
			this.uuid.is(this.cells.at(nn).uuid.property.value());
		}
		else {
			this.id.is(LayoutlessView.UNKNOWN_ID);
			this.uuid.is("");
		}
	}

	/*
		public SimpleList item(BitmapTitleDescription item) {
			this.items.add(item);
			innerHeight = (int) (cellHeight.property.value() * items.size());
			SimpleString s = new SimpleString(context).text.is(item.title.property).size.is(14 * density);
			this.addView(s);
			children.add(s);
			SimpleString d = new SimpleString(context).text.is(item.description.property).size.is(11 * density);
			this.addView(d);
			children.add(d);
			SimpleIcon i = new SimpleIcon(context).bitmap.is(item.bitmap.property.value());
			this.addView(i);
			children.add(i);
			return this;
		}*/
	/*
		public void clear() {
			innerHeight = 0;
			this.removeAllViews();
			this.children.removeAllElements();
			this.items.removeAllElements();
		}*/

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		reLayoutChildren();
	}

	private void adjustRow(int n, int x, int y) {
		if (n * 5 + 4 < children.size()) {
			int rowX = (int) (x);//+this.leftMargin.property.value());
			int rowY = (int) (y + this.topMargin.property.value());
			//Tools.log("adjust "+rowY);
			View background = children.get(n * 5 + 0);
			background.layout((int) (rowX + iconMargin.value())//
					, (int) (rowY)//
					, (int) (rowX + this.getWidth() //- this.rightMargin.property.value()
					)//
					, (int) (rowY + cellHeight.property.value()) //
					);
			View title = children.get(n * 5 + 1);
			title.layout((int) (rowX + cellHeight.property.value() + iconMargin.value())//
					, (int) (rowY)//
					, (int) (rowX + this.getWidth() //- this.rightMargin.property.value()
					)//
					, (int) (rowY + cellHeight.property.value()) //
			);
			View description = children.get(n * 5 + 2);
			description.layout((int) (rowX + cellHeight.property.value() + iconMargin.value())//
					, (int) (rowY + cellHeight.property.value() / 2)//
					, (int) (rowX + this.getWidth() //- this.rightMargin.property.value()
					)//
					, (int) (rowY + cellHeight.property.value())//
					);
			View bitmap = children.get(n * 5 + 3);
			bitmap.layout(rowX//
					, rowY//
					, (int) (rowX + cellHeight.property.value().intValue())//
					, (int) (rowY + cellHeight.property.value())//
			);

			View wb = children.get(n * 5 + 4);
			wb.layout(rowX//
					, rowY//
					, (int) (rowX + cellHeight.property.value().intValue())//
					, (int) (rowY + cellHeight.property.value())//
			);

			children.get(n * 5 + 0).bringToFront();
			children.get(n * 5 + 1).bringToFront();
			children.get(n * 5 + 2).bringToFront();
			children.get(n * 5 + 3).bringToFront();
			children.get(n * 5 + 4).bringToFront();
			children.get(n * 5 + 0).requestLayout();
			children.get(n * 5 + 1).requestLayout();
			children.get(n * 5 + 2).requestLayout();
			children.get(n * 5 + 3).requestLayout();
			children.get(n * 5 + 4).requestLayout();
		}
	}

	private void reLayoutChildren() {

		for (int i = 0; i < cells.size(); i++) {
			//Tools.log(i+". "+shiftY.value()+"/"+cellHeight.property.value());
			adjustRow(i, 0, (int) (cellHeight.property.value() * i + shiftY.value()));
		}
		listWidth.value(this.getWidth());
	}

	float spacing(float x0, float y0, float x1, float y1) {
		float x = x0 - x1;
		float y = y0 - y1;
		return FloatMath.sqrt(x * x + y * y);
	}

	boolean startScroll(float x, float y) {
		if (x < 0//this.leftMargin.property.value()// 
				|| x > this.getWidth() //- this.rightMargin.property.value()// 
				|| y < this.topMargin.property.value() + shiftY.value()// 
				|| y > this.getHeight()// - this.bottomMargin.property.value()+ shiftY//
		) {
			return false;
		}
		initialX = (int) x;
		initialY = (int) y;
		initialShiftY = shiftY.value().intValue();
		lastEventY = y;
		dragMode = SCROLL;
		int s = (int) ((y - shiftY.value() - topMargin.property.value()) / cellHeight.property.value());
		if (s < 0) {
			s = -1;
		}
		if (s > cells.size() - 1) {
			s = cells.size() - 1;
		}
		selection.value(s);
		setID();
		//if(hlTask!=)
		hlTask.start();
		return true;
	}

	void setShift(float x, float y) {
		double newShiftY = shiftY.value() + y - lastEventY;
		if (innerHeight > this.getHeight() - topMargin.property.value() - bottomMargin.property.value()) {
			if (newShiftY < this.getHeight() - innerHeight - topMargin.property.value() - bottomMargin.property.value()) {
				newShiftY = this.getHeight() - innerHeight - topMargin.property.value() - bottomMargin.property.value();
			}
			else {
				//
			}
		}
		else {
			newShiftY = 0;
		}
		if (newShiftY > 0) {
			newShiftY = 0;
		}
		shiftY.value(newShiftY);
		reLayoutChildren();
	}

	void proceedScroll(float x, float y) {
		setShift(x, y);
		lastEventY = y;
		/*if (Math.abs(initialX - x) > pluckDiff && drop.property.value() != null) {
			
			int n = selection.property.value().intValue();
			if (n > -1 && n < this.cells.size()) {
				if (!this.cells.at(n).locked.property.value()) {
					startPluck(x, y);
				}
			}
			
			
		}*/

		if (Math.abs(initialX - x) > pluckDiff) {
			int n = selection.value().intValue();
			if (n > -1 && n < this.cells.size()) {
				if (this.cells.at(n).pluckable.property.value()) {
					startPluck(x, y);
				}
			}
		}
	}

	void finishScroll(float x, float y) {
		setShift(x, y);
		//Tools.log(y + "/" + shiftY + "/" + initialShiftY+"/"+this.topMargin.property.value());
		if ((Math.abs(initialShiftY - shiftY.value()) < tapDiff) && (Math.abs(initialY - y) < tapDiff)) {

			finishTap(x, y);

		}
		else {
			//
		}
		dragMode = NONE;
	}

	void startPluck(float x, float y) {
		shiftY.value(initialShiftY);
		dragMode = PLUCK;
		setShift(x, y);
		if (this.dragLayer != null) {
			//this.dragLayer.bringToFront();
			//Tools.log(this.dragLayer.getWidth()+"/"+this.dragLayer.getHeight()+"/"+this.dragLayer.getLeft()+"/"+this.dragLayer.getTop());
			int n = selection.value().intValue();
			pluckBackground = children.get(n * 5 + 0);
			pluckTitle = children.get(n * 5 + 1);
			pluckDescription = children.get(n * 5 + 2);
			pluckBitmap = children.get(n * 5 + 3);
			pluckWhiteboard = children.get(n * 5 + 4);
			this.removeView(pluckBackground);
			this.removeView(pluckTitle);
			this.removeView(pluckDescription);
			this.removeView(pluckBitmap);
			this.removeView(pluckWhiteboard);
			this.dragLayer.addView(pluckBackground);
			this.dragLayer.addView(pluckTitle);
			this.dragLayer.addView(pluckDescription);
			this.dragLayer.addView(pluckBitmap);
			this.dragLayer.addView(pluckWhiteboard);
		}
		proceedPluck(x, y);
	}

	void proceedPluck(float x, float y) {
		//Tools.log(x+"/"+y);
		int i = selection.value().intValue();
		if (i > -1) {
			adjustRow(i, (int) (x + this.getLeft() - cellHeight.property.value() / 2
			//- this.leftMargin.property.value()
					), (int) (y - topMargin.property.value() - cellHeight.property.value() / 2 + this.getTop()));
		}
	}

	void finishPluck(float x, float y) {
		int s = selection.value().intValue();
		if (s > -1) {
			this.reLayoutChildren();
			dropX.is(x + this.getLeft() - cellHeight.property.value() / 2);
			dropY.is(y + this.getTop() - cellHeight.property.value() / 2);
			//Tools.log("finishPluck " + x + "x" + y);
			if (drop.property.value() != null) {
				drop.property.value().start();
			}
		}
		if (this.dragLayer != null) {
			this.dragLayer.removeView(pluckBackground);
			this.addView(pluckBackground);
			this.dragLayer.removeView(pluckTitle);
			this.addView(pluckTitle);
			this.dragLayer.removeView(pluckDescription);
			this.addView(pluckDescription);
			this.dragLayer.removeView(pluckBitmap);
			this.addView(pluckBitmap);
			this.dragLayer.removeView(pluckWhiteboard);
			this.addView(pluckWhiteboard);
		}
	}

	void finishTap(float x, float y) {
		shiftY.value(initialShiftY);
		if (tap.property.value() != null) {
			tap.property.value().start();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
			return startScroll(event.getX(), event.getY());
		}
		else {
			if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
				if (dragMode == SCROLL) {
					proceedScroll(event.getX(), event.getY());
				}
				else {
					if (dragMode == PLUCK) {
						proceedPluck(event.getX(), event.getY());
					}
					else {
						//ignore
					}
				}
			}
			else {
				if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
					if (dragMode == SCROLL) {
						finishScroll(event.getX(), event.getY());
					}
					else {
						if (dragMode == PLUCK) {
							finishPluck(event.getX(), event.getY());
						}
						else {
							//ignore event
						}
					}
				}
				else {
					//ignore event
				}
			}
			return true;
		}
	}

	@Override
	public void unbind() {
	}
	/*@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setColor(0x99ff0000);
		canvas.drawRect(new RectF(0, 0, 100, 100), paint);
		Tools.log("paint");
		
	}*/
}
