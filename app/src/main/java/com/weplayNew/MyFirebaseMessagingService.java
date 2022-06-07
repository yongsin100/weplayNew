package com.weplayNew;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService";

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // 앱이 실행 중일 때 (Foreground 상황) 에서 푸쉬를 받으면 호출됩니다. // 백그라운드 상황에서는 호출되지 않고 그냥 알림목록에 알림이 추가됩니다. Log.d(TAG,"Message Arrived");
        if ( remoteMessage.getData().size() > 0 ) {
            Log.d(TAG, "FCM Data Message : " + remoteMessage.getData());
        }
        if ( remoteMessage.getNotification() != null ) {
            final String messageBody = remoteMessage.getNotification().getBody();
            Log.d(TAG, "FCM Notification Message Body : " + messageBody);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), messageBody, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d("Refreshed token: ", token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }

    /** token 값을 서버에 등록할 때 사용 */
    private void sendRegistrationToServer(String token) {

    }


}