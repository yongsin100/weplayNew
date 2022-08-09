package com.weplayNew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.URISyntaxException;


/**
 * MainActivity
 */
public class MainActivity extends AppCompatActivity {

    private WebView web;
    private String url = "http://weplay.pe.kr/";

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        FirebaseMessaging.getInstance().subscribeToTopic("ALL");
                        // Get new FCM registration token
                        /*String token = task.getResult();
                        Toast.makeText(MainActivity.this, "" + , Toast.LENGTH_SHORT).show();*/
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
        webSettings.setSupportMultipleWindows(false); //true로 하면 라이브리 로그인 안됨. kakao = true ??
        webSettings.setUseWideViewPort(true);  //html 컨텐츠가 웹뷰에 맞게 나타남
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSaveFormData(true);

        if(Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        web.setWebViewClient(new CustomsWebViewClient());
        web.setWebChromeClient(new WebChromeClient());
        web.loadUrl(url);
    }

    /**
     * onKeyDown
     * @param keyCode
     * @param event
     * @return
     */
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

    /**
     * CustomsWebViewClient
     */
    public class CustomsWebViewClient extends WebViewClient {
        private static final String TAG = "CustomsWebViewClient";

        /**
         * shouldOverrideUrlLoading
         * @param view
         * @param request
         * @return
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d(TAG, " ========== shouldOverrideUrlLoading ========== ");

            if (request.getUrl().getScheme().equals("intent")) {
                try {
                    // Intent 생성
                    Intent intent = Intent.parseUri(request.getUrl().toString(), Intent.URI_INTENT_SCHEME);
                    PackageManager packageManager = getPackageManager();

                    // 실행 가능한 앱이 있으면 앱 실행
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent);
                        return true;
                    }else if(request.getUrl().toString().startsWith("intent:kakao") ||
                            request.getUrl().toString().startsWith("intent://plusfriend/talk")){
                        view.loadUrl("https://play.google.com/store/apps/details?id=com.kakao.talk");
                    }

                    // Fallback URL이 있으면 현재 웹뷰에 로딩
                   String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                    if (fallbackUrl != null) {
                        view.loadUrl(fallbackUrl);
                        return true;
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Invalid intent request");
                }
            }
            //true는 호스트가 제어를 하고 처리했다를 반환, false을 반환하여 WebView가 평소와 같이 URL로드를 진행하도록 처리
            return false;
        }
    }

}