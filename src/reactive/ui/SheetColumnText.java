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
	Vector<String> cells = new Vector<String>();
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
		cell(s,null);
		return this;
	}
	public SheetColumnText cell(String s,Integer bg) {
		cells.add(s);
		backgrounds.add(bg);
		return this;
	}
	@Override
	public Rake header(Context c) {
		Decor header = new Decor(c).labelText.is(title.property);
		header.background.is(Layoutless.themeBlurColor);
		header.labelAlignCenterBottom();
		header.setPadding(3, 3, 3, 3);
		return header;
	}
}
