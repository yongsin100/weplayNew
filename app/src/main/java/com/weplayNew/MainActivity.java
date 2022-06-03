package com.weplayNew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private WebView web;
    private String url = "http://weplay.pe.kr/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        web = (WebView) findViewById(R.id.webView);
        /* 웹 세팅 작업하기 */
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setSupportMultipleWindows(false);//true로 하면 라이브리 로그인 안됨.
        webSettings.setUseWideViewPort(true);  //html 컨텐츠가 웹뷰에 맞게 나타남
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSaveFormData(true);

        if(Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        web.loadUrl(url);
        web.setWebViewClient(new WebViewClientClass());
        web.setWebChromeClient(new WebChromeClient());

    }

    private class WebViewClientClass extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //Log.e("url : ",url);

            try {
                /**
                 * 카카오링크 오류 수정을 위해 아래 if문을 추가함.
                 */
                Log.v("TESTTESTTESTTESTTEST1 ", url);
                if (url != null && url.startsWith("intent:kakaolink:")) {
                    try {
                        Log.v("TESTTESTTESTTESTTEST2 ", url);
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                        Log.v("TESTTESTTESTTESTTEST3 ", existPackage.toString());
                        if (existPackage != null) {
                            startActivity(intent);
                        } else {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                            startActivity(marketIntent);
                        }
                        Log.v("TESTTESTTESTTESTTEST4 ", url);
                        view.loadUrl(url);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (web.canGoBack()) {
                web.goBack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}