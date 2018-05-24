package com.organization.jeptagsalpha.wordpress.rest.interceptor;


import com.organization.jeptagsalpha.wordpress.util.LogUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

/**
 * @author Jan-Louis Crafford
 *         Created on 2016/01/14.
 */
public class OkHttpDebugInterceptor implements Interceptor {

    private boolean mShowResponse = true;
    private boolean mShowRequest = true;
    private boolean mShowHeader = false;

    private static final String DEBUG_TAG = "OkHttpRequest";

    public OkHttpDebugInterceptor() {
        this(false,false,false);
    }

    public OkHttpDebugInterceptor(boolean showHeader,boolean showRequest, boolean showResponse) {
        mShowResponse = showResponse;
        mShowHeader = showHeader;
        mShowRequest = showRequest;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();
        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter("XDEBUG_SESSION_START", "netbeans-xdebug")
                .build();
        Request.Builder requestBuilder = original.newBuilder()
                .url(url);
        Request request = requestBuilder.build();
       // chain.proceed(request);
     //   request.url().newBuilder().addQueryParameter("XDEBUG_SESSION_START","netbeans-xdebug");

//?XDEBUG_SESSION_START=netbeans-xdebug
        LogUtils.d(DEBUG_TAG, "******** [REQUEST START] ********");
        LogUtils.d(DEBUG_TAG, "** URL : " + request.url().toString());
        LogUtils.d(DEBUG_TAG, "** HTTP Method : " + request.method());
        if(mShowHeader) {
            for (String head : request.headers().names()) {
                LogUtils.d(DEBUG_TAG, "** HEADER : " + head + " = " + request.header(head));
            }
        }
        if(mShowRequest) {
        if (request.body() != null) {
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            String bodyStr = buffer.readUtf8();
            LogUtils.d(DEBUG_TAG, "** BODY : " + bodyStr);
        }
        }
        LogUtils.d(DEBUG_TAG, "******** [REQUEST END] ********");

        Response response = chain.proceed(request);

        LogUtils.d(DEBUG_TAG, "******** [RESPONSE START] ********");
        LogUtils.d(DEBUG_TAG, "** (" + response.code() + ") " + response.message());
        if(mShowHeader) {
            for (String head : response.headers().names()) {
                LogUtils.d(DEBUG_TAG, "** HEADER : " + head + " = " + response.header(head));
            }
        }
        // Enabling this stops the callbacks from being able to read the response body because the inputstream gets closed.
        // only really useful to check what responses are to create proper handlers
        if (mShowResponse) {
            if (response.body() != null) {
                LogUtils.d(DEBUG_TAG, "** BODY : " + response.body().string());
            }
        }

        LogUtils.d(DEBUG_TAG, "******** [RESPONSE END ********");
        LogUtils.d(DEBUG_TAG, "");

        return response;
    }
}
