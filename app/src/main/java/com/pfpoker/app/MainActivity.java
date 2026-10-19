package com.pfpoker.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Color;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MainActivity extends Activity {
    private WebView web;
    private static final String ORIGIN = "https://appassets.androidplatform.net/";
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        web = new WebView(this);
        web.setBackgroundColor(Color.rgb(12,16,24));
        web.setOnApplyWindowInsetsListener((view, insets) -> {
            view.setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
            return insets;
        });
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        web.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return !request.getUrl().toString().startsWith(ORIGIN);
            }
            @Override public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith(ORIGIN)) {
                    String file = request.getUrl().getPath();
                    if (file != null && file.startsWith("/assets/") && !file.contains("..")) {
                        file = file.substring(8);
                        String mime = file.endsWith(".html") ? "text/html" : file.endsWith(".css") ? "text/css" : file.endsWith(".js") ? "application/javascript" : "application/octet-stream";
                        try { return new WebResourceResponse(mime, "UTF-8", getAssets().open(file)); }
                        catch (IOException ignored) { }
                    }
                }
                return new WebResourceResponse("text/plain", "UTF-8", 404, "Not Found", null, new ByteArrayInputStream(new byte[0]));
            }
        });
        setContentView(web);
        web.loadUrl(ORIGIN + "assets/index.html");
    }
    @Override public void onBackPressed() {
        web.evaluateJavascript("(function(){const d=document.querySelector('dialog[open]');if(d){d.close();return 'handled';}if(typeof page!=='undefined'&&page!=='lobby'){navigate('lobby');return 'handled';}return 'exit';})()", result -> {
            if ("\"exit\"".equals(result)) new AlertDialog.Builder(this).setTitle("Выйти из PF poker?").setMessage("Баланс и история останутся на этом устройстве.").setPositiveButton("Выйти", (dialog, which) -> finish()).setNegativeButton("Остаться", null).show();
        });
    }
    @Override protected void onPause() { super.onPause(); web.evaluateJavascript("if(typeof save==='function')save();", null); web.onPause(); }
    @Override protected void onResume() { super.onResume(); if (web != null) web.onResume(); }
    @Override protected void onDestroy() { web.destroy(); super.onDestroy(); }
}
