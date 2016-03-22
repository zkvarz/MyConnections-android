package com.example.myconnections_android.core.structure.requests.abs.client;


import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

public class HttpRequestFactory {

    public enum HttpMethod {
        GET, POST, PUT, DELETE
    }

    public static HttpRequestBase createRequestBase(HttpMethod method, String url, HttpEntity httpEntity) {
        HttpRequestBase requestBase = null;
        switch (method) {
            case GET:
                requestBase = createHttpGet(url);
                break;
            case POST:
                requestBase = createHttpPost(url, httpEntity);
                break;
            case PUT:
                requestBase = createHttpPut(url, httpEntity);
                break;
            case DELETE:
                requestBase = createHttpDelete(url);
                break;
            default:
                break;

        }

        return requestBase;
    }

    private static HttpDelete createHttpDelete(String url) {
        return new HttpDelete(url);
    }

    private static HttpPut createHttpPut(String url, HttpEntity httpEntity) {
        HttpPut httpPut = new HttpPut(url);
        setEntity(httpPut, httpEntity);
        return httpPut;
    }

    private static HttpPost createHttpPost(String url, HttpEntity httpEntity) {
        HttpPost httpPost = new HttpPost(url);
        setEntity(httpPost, httpEntity);
        return httpPost;
    }

    private static HttpRequestBase createHttpGet(String url) {
        return new HttpGet(url);
    }

    private static void setEntity(HttpEntityEnclosingRequest request, HttpEntity httpEntity) {
        if (httpEntity != null && request != null) {
            request.setEntity(httpEntity);
        }
    }
}
