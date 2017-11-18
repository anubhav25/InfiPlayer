package com.ANUBHAV.INFI_Player;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class YouTube extends Activity {

    static WebView w;
    public static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);


        w=(WebView) findViewById(R.id.webView);

        progressBar=(ProgressBar)findViewById(R.id.pBar);
        go();
    }

    public void go()
    {
        String url = "http://www.youtube.com";
        w.setWebViewClient(new Mybrowser());
        w.getSettings().setLoadsImagesAutomatically(true);
        w.getSettings().setJavaScriptEnabled(true);
        w.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        w.loadUrl(url);

    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && YouTube.w.canGoBack()) {
            YouTube.w.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

class Mybrowser extends WebViewClient {

    public boolean shouldOverrideUrlLoading(WebView view,String url)
    {	view.loadUrl(url);
        YouTube.progressBar.setProgress(100);

        return true;
    }
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view,url);

        YouTube.progressBar.setVisibility(View.INVISIBLE);
    }

}


