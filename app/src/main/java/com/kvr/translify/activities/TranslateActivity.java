package com.kvr.translify.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.kvr.translify.R;

public class TranslateActivity extends AppCompatActivity {
    private WebView translate;

    private String base_url = "https://translate.google.com/#view=home&op=translate&sl=auto&tl=";
    private String append = "&text=";
    private String language, text;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        Bundle bundle = getIntent().getExtras();
        text = bundle.getString("textDetected");
        text.replace(" ", "+");
        language = bundle.getString("key");

        String newUA = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        translate = findViewById(R.id.webView);
        translate.getSettings().setJavaScriptEnabled(true);
        translate.setWebChromeClient(new WebChromeClient());
        translate.getSettings().setPluginState(WebSettings.PluginState.ON);
        translate.clearHistory();
        translate.clearCache(true);
        translate.getSettings().setUserAgentString(newUA);
        translate.loadUrl(base_url + language + append + text);

    }


}
