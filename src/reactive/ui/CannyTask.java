package reactive.ui;

import java.util.*;
import android.os.*;
import tee.binding.task.*;

public abstract class CannyTask extends Task {
	private double key = 0;
	private int laziness = 50;

	public CannyTask() {
	}
	public void start(int lazy) {
		laziness=lazy;
		start();
	}
	@Override
	public void start() {
		key = Math.random();
		final double started = key;
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(laziness);
				}
				catch (Throwable t) {
					//
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void v) {
				if (started == key) {
					CannyTask.this.doTask();
				}
			}
		}.execute();
	}
}
