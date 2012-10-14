package reactive.ui.library;

import android.view.*;
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

public class ViewBag extends ViewGroup {
	public ViewBag(Context context) {
		super(context);
		/*Button b = new Button(context);
		this.addView(b);
		b.setText("ViewBag");
		b.setWidth(100);
		b.setHeight(100);
		b.layout(0, 0, 190, 100);*/
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

	}

}
