package reactive.ui;
import android.view.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.*;
import android.net.*;
import android.widget.*;

import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.params.*;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.database.*;
import android.database.sqlite.*;
import reactive.ui.*;

import java.net.*;
import java.nio.channels.FileChannel;

import android.view.animation.*;
import android.view.inputmethod.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.it.*;
import tee.binding.*;
import java.io.*;
import java.text.*;
public abstract class Column {
	public NumericProperty<Column> width = new NumericProperty<Column>(this);
	public ToggleProperty<Column> noVerticalBorder = new ToggleProperty<Column>(this);
	public ToggleProperty<Column> noHorizontalBorder = new ToggleProperty<Column>(this);
	public abstract void afterTap(int row);
	public NoteProperty<Column> title = new NoteProperty<Column>(this);
	public NoteProperty<Column> footer = new NoteProperty<Column>(this);
	public abstract Rake item(int column,int row, Context context);
	public abstract String export(int row);
	public abstract void update(int row);
	public abstract Rake header( Context context);
	public abstract int count();
	public abstract void clear( );
	public abstract void highlight(int row );
	
}
