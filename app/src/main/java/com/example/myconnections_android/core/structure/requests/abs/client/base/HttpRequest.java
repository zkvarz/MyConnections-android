package com.example.myconnections_android.core.structure.requests.abs.client.base;


import com.example.myconnections_android.core.structure.manager.comunication.CommunicationService;
import com.example.myconnections_android.core.structure.requests.abs.Request;
import com.example.myconnections_android.core.structure.requests.abs.client.HttpRequestFactory;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.core.structure.requests.models.RemoteResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.Map;


public abstract class HttpRequest<T> extends Request<T> {

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

    protected final CommunicationService mCommunicationService = CommunicationService.get();

    protected abstract void parseResponse(RemoteResponse response);

    protected abstract String buildUrl();

    protected abstract HttpEntity buildEntity();

    protected abstract HttpRequestFactory.HttpMethod getHttpMethod();

    protected Map<String, String> buildHeaders() {
        return null;
    }

    public HttpRequest(ICallback<T> callback) {
        super(callback);
    }

    @Override
    public void doWork() {
        HttpRequestBase request = createHttpRequest(prepareUrl(buildUrl()));
        request.addHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);
        try {
            RemoteResponse response = mCommunicationService.executeRequest(request, buildHeaders(), isHttps());
            parseResponse(response);
        } catch (Exception e) {
            onError(e);
        }
    }

    private HttpRequestBase createHttpRequest(String url) {
        return HttpRequestFactory.createRequestBase(getHttpMethod(), url, buildEntity());
    }


    protected final boolean isHttps() {
        return buildUrl().startsWith("https");
    }

    protected String prepareUrl(String url) {
        // Logger.debug(getClass(), url);
        return url.replace(" ", "%20");
    }


}
