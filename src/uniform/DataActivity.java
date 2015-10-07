package uniform;

import java.util.*;
import reactive.ui.*;
import tee.binding.*;
import tee.binding.it.Numeric;
import tee.binding.task.*;
import android.app.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.*;
import android.os.*;

public class DataActivity extends Activity {
	Layoutless layoutless;
	Bough extras = new Bough();
	Bough formConfiguration = new Bough();
	Vector<ColumnText> columns = new Vector<ColumnText>();
	DataGrid dataGrid;
	int gridPageSize = 30;
	Bough gridData;
	Numeric gridOffset = new Numeric();
	static private SQLiteDatabase _SQLiteDatabase = null;
	String errorMessageForRequery = "";
	Expect requery = new Expect().task.is(new Task() {
		@Override
		public void doTask() {
			errorMessageForRequery = "";
			try {
				requeryData();
			}
			catch (Throwable t) {
				t.printStackTrace();
				errorMessageForRequery = t.getMessage().substring(0, 99);
			}
		}
	})//
	.afterDone.is(new Task() {
		@Override
		public void doTask() {
			refreshGUI();
			if (errorMessageForRequery.length() > 0) {
				Auxiliary.warn(errorMessageForRequery, DataActivity.this);
			}
		}
	})//
	;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createGUI();
	}
	@Override
	protected void onPause() {
		super.onPause();
		saveGUI();
	}
	@Override
	protected void onResume() {
		super.onResume();
		fillGUI();
	}
	public synchronized static SQLiteDatabase db(ContextWrapper contextWrapper, String dbFile) {
		initDB(contextWrapper, dbFile);
		return _SQLiteDatabase;
	}
	private synchronized static void initDB(ContextWrapper contextWrapper, String dbFile) {
		if (_SQLiteDatabase == null) {
			String dbPath = Environment.getExternalStorageDirectory().getPath() + dbFile;//"/horeca/swlife_database";
			System.out.println("initDB " + dbPath);
			CursorFactory cursorFactory = null;
			DatabaseErrorHandler databaseErrorHandler = new DatabaseErrorHandler() {
				@Override
				public void onCorruption(SQLiteDatabase dbObj) {
					System.out.println(dbObj);
				}
			};
			_SQLiteDatabase = contextWrapper.openOrCreateDatabase(dbPath, android.content.Context.MODE_ENABLE_WRITE_AHEAD_LOGGING, cursorFactory, databaseErrorHandler);
		}
	}
	public synchronized static void closeDB() {
		if (_SQLiteDatabase != null) {
			//System.out.println("close dbPath");
			_SQLiteDatabase.close();
			_SQLiteDatabase = null;
		}
	}
	void createGUI() {
		extras = Auxiliary.activityExatras(this);
		String formName = extras.child("form").value.property.value();
		formConfiguration = DataEnvironment.variables.child("forms").child(formName);
		//System.out.println("DataActivity createGUI " + formConfiguration.dumpXML());
		this.setTitle(formConfiguration.child("title").value.property.value());
		layoutless = new Layoutless(this);
		setContentView(layoutless);
		dataGrid = new DataGrid(this).center.is(true)//
		.pageSize.is(gridPageSize)//
		.dataOffset.is(gridOffset)//
		.beforeFlip.is(new Task() {
			@Override
			public void doTask() {
				requeryData();
				flipGrid();
			}
		});
	}
	void fillGUI() {
		//System.out.println("DataActivity fillGUI");
		String msg = DataEnvironment.variables.child("store").child("messageFillGUI").value.property.value();
		if (msg.trim().length() > 1) {
			requery.status.is(msg).start(this);
		}
		else {
			requery.start(this);
		}
	}
	void saveGUI() {
		//System.out.println("DataActivity saveGUI " + DataEnvironment.variables.dumpXML());
	}
	public static String findVariable(String what){
		String r="";
		if(what.equals("marshrutMode")){r="1";}
		if(what.equals("currentDayOfWeek")){r="1";}
		if(what.equals("marshrutGridLimit")){r="30";}
		if(what.equals("marshrutGridOffset")){r="0";}
		return r;
	}
	public static String replaceVariables(String text) {
		//System.out.println(text);
		StringBuilder r = new StringBuilder();
		String[] parts = text.split("\\{");
		r.append(parts[0]);
		for (int i = 1; i < parts.length; i++) {
			//System.out.println(i + ":" + parts[i]);
			String[] subs = parts[i].split("\\}");
			//if (subs.length > 1) {
				if(subs[0].length()>0){
					//r.append("<"+subs[0]+">");
					r.append(findVariable(subs[0]));
				}
				
				for (int s = 1; s < subs.length; s++) {
					//System.out.println("..." + s + ":" + subs[s]);
					r.append(subs[s]);
				}
			/*}
			else {
				r.append(parts[i]);
			}*/
		}
		//System.out.println(r.toString());
		return r.toString();
	}
	public void requeryData() {
		//System.out.println("requeryData");
		//try {
		String cmd = formConfiguration.child("grid").child("sql").value.property.value();
		String sql = replaceVariables(cmd);
		System.out.println("sql "+sql);
		SQLiteDatabase db = db(DataActivity.this, formConfiguration.child("db").value.property.value());
		Cursor cursor = db.rawQuery(sql, null);
		gridData = Auxiliary.fromCursor(cursor);
		//}
		//catch (Throwable t) {
		// TODO Auto-generated catch block
		//t.printStackTrace();
		//Auxiliary.inform(t.getMessage(), DataActivity.this);
		//}
	}
	public void refreshGUI() {
		//System.out.println("refreshGUI");
	}
	void flipGrid() {
		dataGrid.clearColumns();
		if (gridData != null) {
			for (int i = 0; i < gridData.children.size(); i++) {
				Bough row = gridData.children.get(i);
				for (int c = 0; c < columns.size(); c++) {
				}
				/*final String m = row.child("klientKod").value.property.value();
				Task task = new Task() {
					@Override
					public void doTask() {
						Intent intent = new Intent(ActivityMarshrutGrid.this, ActivityKlient.class);
						intent.putExtra("klientKod", m);
						System.out.println("start intent");
						startActivity(intent);
						System.out.println("done start intent");
					}
				};
				columnName.cell(row.child("klientName").value.property.value(), task);
				columnKod.cell(row.child("klientKod").value.property.value(), task);
				columnPn.cell(row.child("d1").value.property.value().trim().length() == 0 ? "" : "*", task);
				columnVt.cell(row.child("d2").value.property.value().trim().length() == 0 ? "" : "*", task);
				columnSr.cell(row.child("d3").value.property.value().trim().length() == 0 ? "" : "*", task);
				columnCh.cell(row.child("d4").value.property.value().trim().length() == 0 ? "" : "*", task);
				columnPt.cell(row.child("d5").value.property.value().trim().length() == 0 ? "" : "*", task);
				columnSb.cell(row.child("d6").value.property.value().trim().length() == 0 ? "" : "*", task);*/
			}
		}
	}
}
