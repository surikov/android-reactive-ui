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

	private boolean invoke() throws Exception {
		HttpPost request = new HttpPost(url.property.value());
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeout.property.value().intValue());
		HttpConnectionParams.setSoTimeout(httpParameters, timeout.property.value().intValue());
		HttpClient client = new DefaultHttpClient(httpParameters);
		StringEntity stringEntity = new StringEntity(xml.property.value(), requestEncoding.property.value());
		stringEntity.setContentType("text/xml; charset="+requestEncoding.property.value());
		request.setEntity(stringEntity);
		HttpResponse httpResponse = client.execute(request);
		statusCode.is(httpResponse.getStatusLine().getStatusCode());
		statusDescription.is(httpResponse.getStatusLine().getReasonPhrase());
		if (statusCode.property.value() > 100 && statusCode.property.value() < 300) {
			HttpEntity entity = httpResponse.getEntity();
			String res = EntityUtils.toString(entity, responseEncoding.property.value());
			data = tee.binding.Bough.parseXML(res);
			return true;
		}
		else {
			return false;
		}
	}
	public void start() {
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... r) {
				try {
					return invoke();
				}
				catch (Throwable t) {
					exception.is(t);
				}
				return false;
			}
			@Override
			protected void onPostExecute(Boolean noError) {
				if (noError) {
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
	}
}
