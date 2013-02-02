package reactive.ui;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.apache.http.params.*;
import org.apache.http.protocol.*;
import org.apache.http.util.*;
import android.os.*;
import tee.binding.properties.*;
import tee.binding.task.*;
import tee.binding.*;

public class RawSOAP {
	public NoteProperty<RawSOAP> url = new NoteProperty<RawSOAP>(this);
	public NoteProperty<RawSOAP> xml = new NoteProperty<RawSOAP>(this);
	public NoteProperty<RawSOAP> statusDescription = new NoteProperty<RawSOAP>(this);
	public NoteProperty<RawSOAP> responseEncoding = new NoteProperty<RawSOAP>(this);
	public NoteProperty<RawSOAP> requestEncoding = new NoteProperty<RawSOAP>(this);
	public NumericProperty<RawSOAP> statusCode = new NumericProperty<RawSOAP>(this);
	public NumericProperty<RawSOAP> timeout = new NumericProperty<RawSOAP>(this);
	public ItProperty<RawSOAP, Task> afterSuccess = new ItProperty<RawSOAP, Task>(this);
	public ItProperty<RawSOAP, Task> afterError = new ItProperty<RawSOAP, Task>(this);
	public ItProperty<RawSOAP, Throwable> exception = new ItProperty<RawSOAP, Throwable>(this);
	public Bough data;

	public void startNow() {
		//System.out.println(xml.property.value());
		try {
			HttpPost request = new HttpPost(url.property.value());
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeout.property.value().intValue());
			HttpConnectionParams.setSoTimeout(httpParameters, timeout.property.value().intValue());
			HttpClient client = new DefaultHttpClient(httpParameters);
			StringEntity stringEntity = new StringEntity(xml.property.value(), requestEncoding.property.value());
			stringEntity.setContentType("text/xml; charset=" + requestEncoding.property.value());
			request.setEntity(stringEntity);
			HttpResponse httpResponse = client.execute(request);
			statusCode.is(httpResponse.getStatusLine().getStatusCode());
			statusDescription.is(httpResponse.getStatusLine().getReasonPhrase());
			HttpEntity entity = httpResponse.getEntity();
			String res = EntityUtils.toString(entity, responseEncoding.property.value());
			data = tee.binding.Bough.parseXML(res);
			/*if (statusCode.property.value() >= 100 && statusCode.property.value() <= 300) {
				return true;
			}
			else {
				return false;
			}*/
		}
		catch (Throwable t) {
			exception.is(t);
		}
		//return false;
	}
	public void startLater() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... r) {
				startNow();
				return null;
			}
			@Override
			protected void onPostExecute(Void r) {
				if (exception.property.value() == null) {
					if (afterSuccess.property.value() != null) {
						afterSuccess.property.value().start();
					}
				}
				else {
					if (afterError.property.value() != null) {
						afterError.property.value().start();
					}
				}
			}
		}.execute();
	}
	public RawSOAP() {
		timeout.is(30 * 1000);
		responseEncoding.is("UTF-8");
		requestEncoding.is("UTF-8");
		statusCode.is(-1);
	}
}
