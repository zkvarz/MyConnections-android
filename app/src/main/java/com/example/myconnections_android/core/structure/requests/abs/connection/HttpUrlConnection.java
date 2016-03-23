package com.example.myconnections_android.core.structure.requests.abs.connection;

import android.util.Log;

import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.core.structure.requests.models.RemoteResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by kvarivoda on 23.03.2016.
 */
public abstract class HttpUrlConnection<T> extends ConnectionRequest<T> {

    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String ENCODING_GZIP = "gzip";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_ACCEPT_CHARSET = "Accept-Charset";
    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_EXPIRES = "Expires";
    private static final String BOUNDARY = "00content0boundary00";
    private static final String CONTENT_TYPE_MULTIPART = "multipart/form-data; boundary="
            + BOUNDARY;

    public HttpUrlConnection(ICallback<T> callback) {
        super(callback);
    }

    protected abstract String getHttpMethod();

    @Override
    public void doWork() {
        try {
            URL url = new URL(buildUrl());
            String param = buildRequestBody();

            // Open a connection using HttpURLConnection
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            con.setReadTimeout(7000);
            con.setConnectTimeout(7000);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod(getHttpMethod());
            con.setFixedLengthStreamingMode(param.getBytes().length);
            con.setRequestProperty(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);
            con.connect();

            //Send request
            OutputStream os = con.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_UTF8);
            osw.write(param);
            osw.flush();
            osw.close();

            StringBuilder response = new StringBuilder("");
            String line;
            BufferedReader in;
            if (con.getResponseCode() != 200) {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                Log.d("TAG", "!=200: " + in);
            } else {
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), CHARSET_UTF8));
            }

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            Log.d("TAG", "POST request response: " + response);
            Log.d("TAG", "POST request response STATUS: " + con.getResponseCode());
            Log.d("TAG", "POST request response STATUS MESSAGE: " + con.getResponseMessage());

            parseResponse(new RemoteResponse(response.toString(), con.getResponseCode()));

        } catch (Exception e) {
            Log.d("TAG", "Exception " + e.getMessage());
            e.printStackTrace();
            onError(e);
        }
    }

    protected abstract String buildUrl();

    protected abstract String buildRequestBody();

    protected abstract void parseResponse(RemoteResponse remoteResponse);

    public interface HttpMethod {
        String GET = "GET";
        String POST = "POST";
        String PUT = "PUT";
        String DELETE = "DELETE";
    }

}
