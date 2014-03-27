package reactive.ui;

import java.nio.*;

import android.graphics.*;

public class NativeBitmap {
	ByteBuffer _handler = null;

	public NativeBitmap() {
	}
/*
	public NativeBitmap(final Bitmap bitmap) {
		storeBitmap(bitmap);
	}
*/
	static {
		System.loadLibrary("ReactiveUI");
	}

	private native int version();

	private native ByteBuffer store(Bitmap bitmap);

	private native Bitmap get(ByteBuffer handler);

	private native void drop(ByteBuffer handler);

	public int getVersion() {

		return version();

	}

	public void storeBitmap(final Bitmap bitmap) {
		if (_handler != null) {
			freeBitmap();
		}
		_handler = store(bitmap);
	}

	public Bitmap getBitmap() {
		if (_handler == null) {
			return null;
		}
		return get(_handler);
	}

	public void freeBitmap() {
		if (_handler == null) {
			return;
		}
		drop(_handler);
		_handler = null;
	}

	@Override
	protected void finalize() throws Throwable {
		freeBitmap();
		super.finalize();
	}
}
