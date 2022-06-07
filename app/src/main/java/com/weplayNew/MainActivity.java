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
    /*private String CHANNEL_ID = "channel_id_test";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*createNotificationChannel();    // 채널생성*/

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("========== Fetching FCM registration token failed ==========" + task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        /*String msg = getString(R.string.msg_token_fmt, token);*/
                        System.out.println(" ========== token ==========" + token);
                        /*Log.d(TAG, msg);*/
                        /*Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();*/
                        Toast.makeText(MainActivity.this, "Your Device registration token is " + token, Toast.LENGTH_SHORT).show();
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

    // 채널 생성
    /*private void createNotificationChannel() {
        // 채널 만들기
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // 시스템에 채널 등록하기.
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }*/

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