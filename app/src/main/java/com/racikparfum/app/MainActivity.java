package com.racikparfum.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                String url = uri.toString();

                // 1. Jika link adalah halaman web biasa (http/https), buka di dalam aplikasi
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    return false;
                }

                // 2. Jika link adalah perintah cetak ke RawBT (rawbt: atau intent:)
                try {
                    Intent intent;
                    if (url.startsWith("intent:")) {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    } else {
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                    }
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Aplikasi RawBT belum terinstal. Silakan instal di Play Store.", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient());

        // Alamat web GitHub Pages aplikasi kasir parfum kamu
        webView.loadUrl("https://github.io");
    }

    // Agar tombol "Back" (Kembali) di HP tidak langsung menutup aplikasi
    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
