package com.lanyuan.picking.common;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Timer;
import java.util.TimerTask;

public abstract class WebViewTask {

    WebView webView;

    Long lastTime;

    Timer timer;

    public WebViewTask(Context context) {
        webView = new WebView(context);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "get_source");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:window.get_source.getSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBlockNetworkImage(true);
        webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.109 Safari/537.36");
    }

    public final void execute(String url) {
        webView.loadUrl(url);
        Log.e("WebViewTask", "execute: ");
        lastTime = System.currentTimeMillis();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Long now = System.currentTimeMillis();
                Log.e("WebViewTask", "run: now" + now);
                Log.e("WebViewTask", "run: last" + lastTime);
                if (now - lastTime > 10000) {
                    cancel();
                    onFailureExecute();
                }
            }
        }, 0, 1000);
    }

    abstract protected void onPostExecute(String result);

    abstract protected void onFailureExecute();

    public void cancel() {
        Log.e("WebViewTask", "cancel: ");
        webView.loadUrl("about:blank");
        webView.clearHistory();
        webView.clearCache(true);
        webView.destroy();
    }

    private final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void getSource(String html) {
            lastTime = System.currentTimeMillis();
            Log.e("InJavaScriptLocalObj", "getSource: " + html);
            onPostExecute(html);
        }
    }

}
