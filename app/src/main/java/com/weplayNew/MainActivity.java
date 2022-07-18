package com.weplayNew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.time.Clock;

public class MainActivity extends AppCompatActivity {

    private WebView web;
    private String url = "http://weplay.pe.kr/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            //System.out.println("========== Fetching FCM registration token failed ==========" + task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        //Toast.makeText(MainActivity.this, "Your Device registration token is " + token, Toast.LENGTH_SHORT).show();
                        FirebaseMessaging.getInstance().subscribeToTopic("ALL");
                    }
                });


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


        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient());
        web.loadUrl(url);
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