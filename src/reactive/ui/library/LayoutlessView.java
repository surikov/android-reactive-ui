package reactive.ui.library;
/*
You can use RelativeLayout. Let's say you wanted a 30x40 ImageView at position (50,60) inside your layout. Somewhere in your activity:

// Some existing RelativeLayout from your layout xml
RelativeLayout rl = (RelativeLayout) findViewById(R.id.my_relative_layout);

ImageView iv = new ImageView(this);

RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 40);
params.leftMargin = 50;
params.topMargin = 60;
rl.addView(iv, params);
*/
import android.view.*;
import android.app.AlertDialog;
import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.View;
import android.widget.*;
import java.util.*;
import reactive.ui.library.views.*;
import android.view.animation.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;
import java.io.*;
import java.text .*;
public class LayoutlessView extends ViewGroup {
	private static double tapDiff = 8;
	public final static int UNKNOWN_ID = -123456789;
	private final static int NONE = 0;
	private final static int DRAG = 1;
	private final static int ZOOM = 2;
	private int dragMode = NONE;
	private float lastEventX = 0;
	private float lastEventY = 0;
	private float initialShiftX = 0;
	private float initialShiftY = 0;
	private float initialSpacing;
	private float currentSpacing;
	private LayoutlessView me = this;
	public Numeric width = new Numeric().value(100);
	public Numeric height = new Numeric().value(100);
	public NumericProperty<LayoutlessView> shiftX = new NumericProperty<LayoutlessView>(this);
	public NumericProperty<LayoutlessView> shiftY = new NumericProperty<LayoutlessView>(this);
	public NumericProperty<LayoutlessView> tapX = new NumericProperty<LayoutlessView>(this);
	public NumericProperty<LayoutlessView> tapY = new NumericProperty<LayoutlessView>(this);
	public ItProperty<LayoutlessView, Task> tap = new ItProperty<LayoutlessView, Task>(this);
	public NumericProperty<LayoutlessView> innerWidth = new NumericProperty<LayoutlessView>(this);
	public NumericProperty<LayoutlessView> innerHeight = new NumericProperty<LayoutlessView>(this);
	public NumericProperty<LayoutlessView> zoom = new NumericProperty<LayoutlessView>(this);
	public NumericProperty<LayoutlessView> maxZoom = new NumericProperty<LayoutlessView>(this);
	private Vector<ViewBox> children = new Vector<ViewBox>();
	private ViewBag dragLayer;
	Context context;
	SimpleDateFormat simpleDateFormat=new SimpleDateFormat();
	public LayoutlessView(Context icontext) {
		super(icontext);
		context = icontext;
		dragLayer = new ViewBag(context);
		this.addView(dragLayer);
		float density = context.getResources().getDisplayMetrics().density;
		tapDiff = 10 * density;

		/*
		Button b = new Button(context);
		this.addView(b);
		b.setText("sdb");
		b.setWidth(100);
		b.setHeight(100);
		b.layout(0, 0, 100, 100);*/
	}

	public ViewBag dragLayer() {
		return dragLayer;
	}

	public LayoutlessView viewBox(final ViewBox box) {
		//if(1==1)return this;
		if (box != null) {
			this.children.add(box);
			box.view.property.afterChange(new Task() {
				@Override
				public void doTask() {
					if (box.delete != null) {
						removeView(box.delete);
					}
					if (box.view.property.value() != null) {
						if (box.view.property.value().getParent() == null) {
							addView(box.view.property.value());
							box.delete = box.view.property.value();

						}
					}
				}
			});
			/*if (box.view.property.value() != null) {
				addView(box.view.property.value());
				box.delete = box.view.property.value();
			}*/
		}
		this.dragLayer.bringToFront();
		return this;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width.value(getMeasuredWidth());
		height.value(getMeasuredHeight());
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		//Tools.log("start onLayout " + changed);
		//if(changed){
		for (int i = 0; i < children.size(); i++) {
			ViewBox viewBox = children.get(i);
			View view = viewBox.view.property.value();
			viewBox.requestLayout.start();
			/*if (view != null)
				view.layout(viewBox.left.property.value().intValue()//
						, viewBox.top.property.value().intValue()//
						, (int) (viewBox.left.property.value() + viewBox.width.property.value())//
						, (int) (viewBox.top.property.value() + viewBox.height.property.value()));*/
		}
		//}
		//Tools.log("end onLayout " + changed);
		this.dragLayer.bringToFront();
		dragLayer.layout(0, 0, this.width.value().intValue(), this.height.value().intValue());
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	public void drop(ViewBox b) {
		b.unbind();
		if (b.view.property.value() != null) {
			removeView(b.view.property.value());
		}
		children.remove(b);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		for (int i = 0; i < children.size(); i++) {
			ViewBox b = children.get(i);
			b.unbind();
			if (b.view.property.value() != null) {
				removeView(b.view.property.value());
			}
		}
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//Tools.log("onDraw");

	}

	float spacing(float x0, float y0, float x1, float y1) {
		float x = x0 - x1;
		float y = y0 - y1;
		return FloatMath.sqrt(x * x + y * y);
	}

	void startDrag(float x, float y) {
		initialShiftX = shiftX.property.value().floatValue();
		initialShiftY = shiftY.property.value().floatValue();
		lastEventX = x;
		lastEventY = y;
		dragMode = DRAG;
	}

	void setShift(float x, float y) {
		double newShiftX = shiftX.property.value() + x - lastEventX;
		double newShiftY = shiftY.property.value() + y - lastEventY;
		if (innerWidth.property.value() > width.value()) {
			if (newShiftX < width.value() - innerWidth.property.value()) {
				newShiftX = width.value() - innerWidth.property.value();
			}
		} else {
			newShiftX = 0;
		}
		if (innerHeight.property.value() > height.value()) {
			if (newShiftY < height.value() - innerHeight.property.value()) {
				newShiftY = height.value() - innerHeight.property.value();
			}
		} else {
			newShiftY = 0;
		}
		if (newShiftX > 0) {
			newShiftX = 0;
		}
		if (newShiftY > 0) {
			newShiftY = 0;
		}
		shiftX.property.value(newShiftX);
		shiftY.property.value(newShiftY);

	}

	void proceedDrag(float x, float y) {
		setShift(x, y);
		lastEventX = x;
		lastEventY = y;
	}

	void finishDrag(float x, float y) {
		setShift(x, y);
		if (Math.abs(initialShiftX - shiftX.property.value()) < tapDiff// 
				&& Math.abs(initialShiftY - shiftY.property.value()) < tapDiff) {
			finishTap(x, y);
		} else {
		}
		dragMode = NONE;
	}

	void finishTap(float x, float y) {
		shiftX.property.value((double) initialShiftX);
		shiftY.property.value((double) initialShiftY);
		tapX.property.value((double) x);
		tapY.property.value((double) y);
		if (tap.property.value() != null) {
			tap.property.value().start();
		}
	}

	void startZoom(float x0, float y0, float x1, float y1) {
		initialSpacing = spacing(x0, y0, x1, y1);
		currentSpacing = initialSpacing;
		dragMode = ZOOM;
	}

	void proceedZoom(float x0, float y0, float x1, float y1) {
		currentSpacing = spacing(x0, y0, x1, y1);

	}

	void finishZoom() {//float x0, float y0, float x1, float y1) {

		//currentSpacing = spacing(x0, y0, x1, y1);
		if (currentSpacing > initialSpacing) {
			if (zoom.property.value() < maxZoom.property.value()) {
				zoom.is(zoom.property.value() + 1);
			}
		} else {
			if (zoom.property.value() > 0) {
				zoom.is(zoom.property.value() - 1);
			}
		}

		dragMode = NONE;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
			startDrag(event.getX(), event.getY());
		} else {
			if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
				if (event.getPointerCount() > 1) {
					if (dragMode == ZOOM) {
						proceedZoom(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
					} else {
						startZoom(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
					}
				} else {
					proceedDrag(event.getX(), event.getY());
				}
			} else {
				if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
					if (dragMode == DRAG) {
						finishDrag(event.getX(), event.getY());
					} else {
						if (dragMode == ZOOM) {
							finishZoom();//event.getX(0), event.getY(0), event.getX(1), event.getY(1));
						} else {
							//ignore event
						}
					}
				} else {
					//ignore event
				}
			}
		}
		return true;
	}
	public interface ItemChoose {
		public void choose(int item);
	}
	public void chooseDialog(String title, Vector<String> items, final ItemChoose itemChoose) {
		String[] arr = new String[items.size()];
		items.toArray(arr);
		chooseDialog(title, arr, itemChoose);

	}
	public void chooseDialog(String title, String[] items, final ItemChoose itemChoose) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if (itemChoose != null) {
					itemChoose.choose(arg1);
				}

			}
		});
		builder.setTitle(title);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	public interface FileChoose {
		public void choose(File file);
	}
	public void chooseDialog(final String base, final FileChoose fileChoose) {
		File b = new File(base);
		final File up = b.getParentFile();
		final File[] children = b.listFiles();
		if (children != null) {

			Arrays.sort(children, new Comparator<File>() {

				@Override
				public int compare(File first, File second) {
					if (first == null && second != null) {
						return -1;
					}
					if (second == null && first != null) {
						return 1;
					}
					if (first == null && second == null) {
						return 0;
					}

					//File f1 = (File) arg0;
					//File f2 = (File) arg1;

					if (first.isDirectory() && (!second.isDirectory())) {
						return -1;
					}
					if (second.isDirectory() && (!first.isDirectory())) {
						return 1;
					}
					return first.getName().compareToIgnoreCase(second.getName());
				}
			});
		}
		Vector<String> items = new Vector<String>();
		items.add("...");
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				File child = children[i];
				if (child.isDirectory()) {
					items.add("[" + child.getName() + "]");
					//folderCount++;
				} else {
					long sz = child.length();
					String q = "b";
					if (sz > 1000000) {
						sz = sz / 1000000;
						q = "Mb";
					} else {
						if (sz > 1000) {
							sz = sz / 1000;
							q = "Kb";
						}
					}
					Date d=new Date(child.lastModified());
					
					items.add(child.getName() + " (" + sz + q + ", " +simpleDateFormat.format(d)  + ")");
				}
			}
		}
		chooseDialog(base, items, new ItemChoose() {

			@Override
			public void choose(int item) {
				try {
					int c = 0;
					if (children != null) {
						c = children.length;
					}
					if (item > -1 && (item < c + 1)) {
						if (item == 0) {
							String upPath = "/";
							if (up != null) {
								upPath = up.getCanonicalPath();
							}
							//System.out.println("up " + upPath);
							chooseDialog(upPath, fileChoose);
						} else {
							File f = children[item - 1];
							if (f.isDirectory()) {
								//System.out.println("dir " + f.getCanonicalPath());
								chooseDialog(f.getCanonicalPath(), fileChoose);
							} else {
								//System.out.println("choose " + f.getCanonicalPath());
								fileChoose.choose(f);
							}
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
					chooseDialog(base, fileChoose);
				}
			}
		});
	}

}