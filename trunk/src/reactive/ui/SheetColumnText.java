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

public class SheetColumnText extends SheetColumn {
	public Vector<String> cells = new Vector<String>();
	Vector<Task> taps = new Vector<Task>();
	Vector<Integer> backgrounds = new Vector<Integer>();
	public NoteProperty<SheetColumnText> title=new NoteProperty<SheetColumnText> (this);
	@Override
	public Rake cell(int row, Context c) {
		Decor cell = new Decor(c);
		
		if (cells.size() > row) {
			if (row > -1) {
				//System.out.println("x"+row);
				cell.labelText.is(cells.get(row)).labelAlignLeftCenter();
				if(backgrounds.get(row)!=null){
					cell.background.is(backgrounds.get(row));
				}
			}
		}
		cell.setPadding(3, 3, 3, 3);
		cell.labelStyleMediumNormal();
		//cell.labelText.is("cells.get(row)");
		return cell;
	}
	@Override
	public int count() {
		return cells.size();
	}
	
	public SheetColumnText cell(String s) {
		cell(s,null,null);
		return this;
	}
	public SheetColumnText cell(String s,Task t) {
		cell(s,null,t);
		return this;
	}
	public SheetColumnText cell(String s,Integer bg,Task t) {
		cells.add(s);
		backgrounds.add(bg);
		taps.add(t);
		return this;
	}
	@Override
	public Rake header(Context c) {
		Decor header = new Decor(c).labelText.is(title.property);
		//header.background.is(Layoutless.themeBlurColor);
		header.background.is(0x44999999);
		header.labelAlignCenterBottom();
		header.setPadding(3, 3, 3, 3);
		return header;
	}
	@Override
	void afterTap(int row) {
		// TODO Auto-generated method stub
		if(row>-1 && row <taps.size()){
			Task t=taps.get(row);
			if(t!=null){
				t.start();
			}
		}
	}
}
