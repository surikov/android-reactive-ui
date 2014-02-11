package reactive.ui;

import android.os.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.*;

abstract class RealAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	public void realPublishProgress(Progress... values) {
		publishProgress( values);
	}
}

public class Expect //extends AsyncTask<Void, Void, Void>
{
	public ItProperty<Expect, Task> afterDone = new ItProperty<Expect, Task>(this);
	public ItProperty<Expect, Task> afterCancel = new ItProperty<Expect, Task>(this);
	public ItProperty<Expect, Task> task = new ItProperty<Expect, Task>(this);
	public NoteProperty<Expect> status = new NoteProperty<Expect>(this);
	public ToggleProperty<Expect> cancel = new ToggleProperty<Expect>(this);
	private AlertDialog dialog;
	private boolean lock = false;
	RealAsyncTask<Void, Void, Void> asyncTask;

	public Expect() {
		status.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (asyncTask != null) {
					asyncTask.realPublishProgress();
				}
			}
		});
	}
	/*@Override
	protected Void doInBackground(Void... params) {
		if (task.property.value() != null) {
			try {
				task.property.value().start();
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
		}
		return null;
	}
	@Override
	protected void onProgressUpdate(Void... values) {
		if (dialog != null) {
			dialog.setMessage(status.property.value());
		}
	}
	@Override
	protected void onPostExecute(Void v) {
		if (dialog != null) {
			dialog.dismiss();
		}
		if (!cancel.property.value()) {
			if (afterDone.property.value() != null) {
				afterDone.property.value().start();
			}
		}
		unbind();
		lock = false;
	}*/
	private void executeTask() {
		asyncTask = new RealAsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				if (task.property.value() != null) {
					try {
						task.property.value().start();
					}
					catch (Throwable t) {
						t.printStackTrace();
					}
				}
				return null;
			}
			@Override
			protected void onProgressUpdate(Void... values) {
				if (dialog != null) {
					dialog.setMessage(status.property.value());
				}
			}
			@Override
			protected void onPostExecute(Void v) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (!cancel.property.value()) {
					if (afterDone.property.value() != null) {
						afterDone.property.value().start();
					}
				}
				unbind();
				lock = false;
			}
		};
		//System.out.println(this.getClass().getCanonicalName()+" execute");
		asyncTask.execute();
	}
	public boolean start(Context context) {
		if (lock) {
			//System.out.println(this.getClass().getCanonicalName()+" is locked");
			return false;
		}
		lock = true;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				cancel.is(true);
				if (afterCancel.property.value() != null) {
					afterCancel.property.value().start();
				}
				unbind();
				lock = false;
			}
		});
		builder.setMessage(status.property.value());
		//System.out.println(this.getClass().getCanonicalName()+" show status");
		dialog = builder.show();
		//execute();
		executeTask();
		return true;
	}
	public void unbind() {
		afterDone.property.unbind();
		afterCancel.property.unbind();
		task.property.unbind();
		status.property.unbind();
		cancel.property.unbind();
	}
}
