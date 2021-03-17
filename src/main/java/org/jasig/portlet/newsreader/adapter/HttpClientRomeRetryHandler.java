package org.jasig.portlet.newsreader.adapter;

import org.apache.http.NoHttpResponseException;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

class HttpClientRomeRetryHandler extends DefaultHttpRequestRetryHandler {

    HttpClientRomeRetryHandler(int timesToRetry) {
        super(timesToRetry, true);
    }

    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        if (executionCount >= super.getRetryCount()) {
            // Do not retry if over max retry count
            return false;
        }
        if (exception instanceof NoHttpResponseException) {
            // Retry if the server dropped connection on us
            return true;
        }
        if (exception instanceof SocketException) {
            // Retry if the server reset connection on us
            return true;
        }
        if (exception instanceof SocketTimeoutException) {
            // Retry if the read timed out
            return true;
        }
        return super.retryRequest(exception, executionCount, context);
    }
}
