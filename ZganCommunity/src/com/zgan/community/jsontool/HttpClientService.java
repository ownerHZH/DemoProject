package com.zgan.community.jsontool;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;



import android.util.Log;


public class HttpClientService {
	String url;
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	boolean multipart = false;
	MultipartEntity mpEntity = null;
	static String charset = HTTP.UTF_8;
	private static final int REQUEST_TIMEOUT = AppConstants.REQUEST_TIMEOUT * 1000;// 设置请求超时10秒钟

	private static final int SO_TIMEOUT = AppConstants.SO_TIMEOUT * 1000; //
	// 设置等待数据超时时间10秒钟
	BasicHttpParams httpParams = new BasicHttpParams();

	public HttpClientService(String url) {
		this.url = url;
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
	}

	public HttpClientService(String url, boolean m) {
		this.url = url;
		multipart = m;
		if (m) {
			mpEntity = new MultipartEntity();
			//Log.e("--------->new", mpEntity.toString());
		}
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
	}

	public void addParameter(String name, int value) {
		if (!multipart) {
			params.add(new BasicNameValuePair(name, String.valueOf(value)));
		} else {
			try {
				mpEntity.addPart(name, new StringBody(String.valueOf(value),
						Charset.forName(charset)));

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	public void addParameter(String name, File file) {
		// Log.e("--------->", mpEntity.toString());
		mpEntity.addPart(name, new FileBody(file));

	}

	public void addParameter(String name, String value) {
		if (!multipart) {
			params.add(new BasicNameValuePair(name, value));
		} else {
			try {
				if (value != null) {
					mpEntity.addPart(name,
							new StringBody(value, Charset.forName(charset)));
				} else {
					mpEntity.addPart(name, new StringBody(""));
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	public String getResponse() {
		return this.getDefaultResponse("");
	}

	public String getDefaultResponse(String type) {
		// HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		DefaultHttpClient client = new DefaultHttpClient(httpParams);
		URI uri;
		String result = null;
		try {
			uri = new URI(url);
			HttpPost httpRequest = new HttpPost(uri);
			if (!multipart) {
				httpRequest
						.setEntity(new UrlEncodedFormEntity(params, charset));
			} else {
				httpRequest.setEntity(mpEntity);
			}
			HttpResponse httpResponse = null;
			try {
				httpResponse = client.execute(httpRequest);
			} catch (ConnectTimeoutException e4) {
				result = "ConnectTimeoutException";
				return result;
			} catch (IllegalStateException e5) {
				result = AppConstants.TARGET_NOT_FOUND_EXCEPTION;
				return result;
			} catch (HttpHostConnectException e) {
				//Log.e("---------->", "HttpHostConnectException");
				result = AppConstants.HTTPHOST_CONNECT_EXCEPTION;
				return result;
			}
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 取出回应字串
				HttpEntity entity = httpResponse.getEntity();
				ByteArrayOutputStream jsonHolder = new ByteArrayOutputStream();

				entity.writeTo(jsonHolder);
				jsonHolder.flush();

				result = jsonHolder.toString(getEncoding(entity));

			} else {
				Log.i("http", "出错了:["
						+ httpResponse.getStatusLine().getStatusCode() + "]"
						+ this.url);
				Log.i("http", "出错了:["
						+ new UrlEncodedFormEntity(params, charset));
				// return null;
			}

		} catch (URISyntaxException e) {
			e.printStackTrace();
			Log.i("http", e.toString());
			// return null;
		} catch (IOException e2) {
			e2.printStackTrace();
			Log.i("http", e2.toString());
			// return null;
		} finally {
			if (client != null) {
				// client.close();
				client = null;
			}
		}
		return result;

	}

	// public JSONObject getJsonObject() {
	// AndroidHttpClient client = null;
	// URI uri;
	// try {
	// uri = new URI(url);
	// HttpPost httpRequest = new HttpPost(uri);
	//
	// httpRequest.setEntity(new UrlEncodedFormEntity(params, charset));
	// // httpRequest.setHeader("cookie", getCookie(uri.toString()));
	// client = AndroidHttpClient.newInstance("Android");
	// HttpResponse httpResponse = client.execute(httpRequest);
	//
	// if (httpResponse.getStatusLine().getStatusCode() == 200) {
	// // 取出回应字串
	// HttpEntity entity = httpResponse.getEntity();
	//
	// ByteArrayOutputStream jsonHolder = new ByteArrayOutputStream();
	// entity.writeTo(jsonHolder);
	// jsonHolder.flush();
	// Log.i("http", url);
	// Log.i("http", jsonHolder.toString(getEncoding(entity)));
	// JSONObject json = new JSONObject(
	// jsonHolder.toString(getEncoding(entity)));
	// jsonHolder.close();
	// return json;
	// } else {
	// return null;
	// }
	//
	// } catch (URISyntaxException e) {
	// e.printStackTrace();
	// return null;
	// } catch (IOException e2) {
	// e2.printStackTrace();
	// return null;
	// } catch (JSONException e3) {
	// e3.printStackTrace();
	// return null;
	// } finally {
	// if (client != null) {
	// client.close();
	// }
	// }
	//
	// }

	private static String getEncoding(HttpEntity entity) {

		return charset;
		//return "GBK";

	}
}
