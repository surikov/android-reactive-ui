package reactive.ui;
import java.util.*;

import reactive.ui.*;

import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import tee.binding.task.*;
import tee.binding.it.*;
import android.text.*;
public class SheetColumnBitmap  extends SheetColumn{
//private Vector<String> items = new Vector<String>();
	
	Vector<Task> taps = new Vector<Task>();
	Vector<Integer> backgrounds = new Vector<Integer>();
	Vector<Bitmap> items = new Vector<Bitmap>();
	public NoteProperty<SheetColumnBitmap> title=new NoteProperty<SheetColumnBitmap> (this);
	public NumericProperty<SheetColumnBitmap> headerBackground=new NumericProperty<SheetColumnBitmap> (this);
	
	public SheetColumnBitmap(){
		headerBackground.is(0x44999999);
		//textSize.is(16);
	}
	@Override
	public Sketch cell(int row) {
		Sketches cell =new Sketches();
		SketchBitmap label = new SketchBitmap();
		SketchPlate bg=new SketchPlate().background.is(0x000000);
		cell.child(bg).child(label);
		label.left.is(cell.left.property.plus(3)).top.is(cell.top.property).width.is(cell.width.property.minus(6)).height.is(cell.height.property);
		bg.left.is(cell.left.property).top.is(cell.top.property).width.is(cell.width.property).height.is(cell.height.property);
		if (items.size() > row) {
			if (row > -1) {
				//System.out.println("x"+row);
				label
				.bitmap.is(items.get(row))//
				//.size.is(textSize.property.value())//
				;//
				if(backgrounds.get(row)!=null){
					bg.background.is(backgrounds.get(row));
				}
				/*.labelAlignLeftCenter();
				if(backgrounds.get(row)!=null){
					cell.background.is(backgrounds.get(row));
				}*/
			}
		}
		//cell.setPadding(3, 3, 3, 3);
		//cell.labelStyleMediumNormal();
		
		//cell.labelText.is("cells.get(row)");
		return cell;
	}
	@Override
	public int count() {
		return items.size();
	}
	
	public SheetColumnBitmap item(Bitmap s) {
		item(s,null,null);
		return this;
	}
	public SheetColumnBitmap item(Bitmap s,Task t) {
		item(s,null,t);
		return this;
	}
	public SheetColumnBitmap item(Bitmap s,Integer bg,Task t) {
		items.add(s);
		backgrounds.add(bg);
		taps.add(t);
		return this;
	}
	@Override
	public Rake header(Context c) {
		Decor header = new Decor(c).labelText.is(title.property);
		//header.background.is(Layoutless.themeBlurColor);
		header.background.is(headerBackground.property);
		header.labelAlignCenterBottom();
		header.setPadding(3, 3, 3, 3);
		return header;
	}
	@Override
	public  void unbind(){
		//textSize.property.unbind();
		title.property.unbind();
		headerBackground.property.unbind();
		clear();
	}
	@Override
	public  void clear(){
		taps.removeAllElements();
		//for(//int i=0;i<)
		items.removeAllElements();
		backgrounds.removeAllElements();
		
	}
	@Override
	public 	void afterTap(int row) {
		//System.out.println(this.title.property.value());
		if(row>-1 && row <taps.size()){
			Task t=taps.get(row);
			if(t!=null){
				t.start();
			}
		}
	}
}
