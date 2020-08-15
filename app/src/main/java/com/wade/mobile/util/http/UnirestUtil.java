package com.wade.mobile.util.http;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import java.io.InputStream;
import java.util.Map;

public class UnirestUtil {
    private static HttpRequestWithBody getUnirestRequest(String url, Map<String, Object> param) {
        HttpRequestWithBody request = Unirest.post(url);
        request.fields(param);
        return request;
    }

    public static String requestByPost(String url, Map<String, Object> param) throws UnirestException {
        return (String) getUnirestRequest(url, param).asString().getBody();
    }

    public static InputStream downloadByPost(String url, Map<String, Object> param) throws UnirestException {
        return getUnirestRequest(url, param).asBinary().getRawBody();
    }

    public static String uploadByPost(String url, Map<String, Object> param) throws UnirestException {
        return (String) getUnirestRequest(url, param).asString().getBody();
    }
}