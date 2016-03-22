package com.example.myconnections_android.core.structure.manager.comunication;


import com.example.myconnections_android.core.structure.helpers.Connections;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.main.CoreApplication;
import com.example.myconnections_android.core.structure.manager.comunication.models.CommunicationException;
import com.example.myconnections_android.core.structure.manager.comunication.ssl.ExSSLSocketFactory;
import com.example.myconnections_android.core.structure.manager.comunication.ssl.ExX509TrustManager;
import com.example.myconnections_android.core.structure.requests.models.RemoteResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.InputStream;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;


/**
 * Note: HttpClient is not multi-threaded.. each of the methods here must be used synchronously. Special care must
 * be used where there is an inputStream returned.
 *
 * @author jsimonelis
 */
public class CommunicationService {

    private static CommunicationService communicationService;

    private static final int TIMEOUT = 2000;

    private CommunicationService() {
        HttpParams myHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(myHttpParams, TIMEOUT);
    }

    public static CommunicationService get() {
     /*   if (communicationService == null) {
            communicationService = new CommunicationService();
        }*/
        return new CommunicationService();
    }

    private boolean isConnectedToInternet() {
        return Connections.isInternetAvailable(CoreApplication.getApplication());
    }

    public static HttpClient createHttpsClient() {
        HttpClient client=createHttpClient();
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new ExX509TrustManager()}, null);
            SSLSocketFactory sslSocketFactory = new ExSSLSocketFactory(sslContext);
            sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager clientConnectionManager = client.getConnectionManager();
            SchemeRegistry schemeRegistry = clientConnectionManager.getSchemeRegistry();
            schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
            return new DefaultHttpClient(clientConnectionManager, client.getParams());

           /* HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(defaultHttpClient.getParams(), registry);
            client = new DefaultHttpClient(mgr, defaultHttpClient.getParams());
            return client;*/
        } catch (Exception ex) {
            return null;
        }
    }

    public static HttpClient createClient(boolean isHttps) {
        return isHttps ? createHttpsClient() : createHttpClient();
    }

    public static HttpClient createHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
        return new DefaultHttpClient();
    }

    public RemoteResponse executeRequest(HttpRequestBase request, Map<String, String> headers, boolean isHttps) throws CommunicationException {
        if (!isConnectedToInternet()) {
            throw new CommunicationException("Internet is not available");
        }

        if (headers != null) {
            for (String key : headers.keySet()) {
                request.setHeader(key, headers.get(key));
            }
        }
        int countResponse = 0;
        HttpResponse response;
        do {
            if (countResponse > 0) {
                Logger.error(getClass(), "Repeating Request #" + countResponse);
            }
            response = executeRequest(createClient(isHttps), request);
            countResponse++;
        } while (response == null && countResponse < 3);

        if (response == null) {
            throw new CommunicationException("Error executing request");
        }

        InputStream is = null;
        int statusCode = 0;
        try {
            is = response.getEntity().getContent();
            statusCode = response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            Logger.error(getClass(), "Error reading response", e);
        }
        return new RemoteResponse(is, statusCode);
    }

    private HttpResponse executeRequest(HttpClient client, HttpRequestBase request) {
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (Exception e) {
            Logger.error(getClass(), "Error posting request: " + e + ", to url: " + request.getURI());
        }
        return response;
    }
}
