package reactive.ui;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.apache.http.params.*;
import org.apache.http.protocol.*;
import org.apache.http.util.*;

import android.net.http.AndroidHttpClient;
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
	//public NoteProperty exceptionDescription = new NoteProperty(this);
	public Bough data;
public String rawResponse=null;
	public void startNow() {
		//System.out.println(xml.property.value());
		try {
			statusCode.is(-2);
			HttpPost request = new HttpPost(url.property.value());
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeout.property.value().intValue());
			HttpConnectionParams.setSoTimeout(httpParameters, timeout.property.value().intValue());
			HttpConnectionParams.setSocketBufferSize(httpParameters, 8192);
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeout.property.value().intValue());
			HttpClient client = new DefaultHttpClient(httpParameters);
			//HttpClient client = new AndroidHttpClient(httpParameters);
			StringEntity stringEntity = new StringEntity(xml.property.value(), requestEncoding.property.value());
			stringEntity.setContentType("text/xml; charset=" + requestEncoding.property.value());
			request.setEntity(stringEntity);
			statusCode.is(-3);
			HttpResponse httpResponse = client.execute(request);
			statusCode.is(-4);
			statusCode.is(httpResponse.getStatusLine().getStatusCode());
			statusDescription.is(httpResponse.getStatusLine().getReasonPhrase());
			HttpEntity entity = httpResponse.getEntity();
			rawResponse = EntityUtils.toString(entity, responseEncoding.property.value());
			data = tee.binding.Bough.parseXML(rawResponse);
			/*if (statusCode.property.value() >= 100 && statusCode.property.value() <= 300) {
				return true;
			}
			else {
				return false;
			}*/
		}
		catch (Throwable t) {
			//exceptionDescription.is(t.getMessage());
			exception.is(t);
			//t.printStackTrace();
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
