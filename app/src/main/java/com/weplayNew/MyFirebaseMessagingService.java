package com.weplayNew;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.annotation.SuppressLint;
import android.app.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService";

    /* 이미지 URL 다운로드용 비트맵 */
    Bitmap bigPicture;

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /* 앱이 실행 중일 때 (Foreground 상황) 에서 푸쉬를 받으면 호출됩니다.
         * 백그라운드 상황에서는 호출되지 않고 그냥 알림목록에 알림이 추가됩니다. */
        super.onMessageReceived(remoteMessage);
        sendNotification(remoteMessage);

        /* Data 사용하지 않고 Notification만 사용하므로 주석처리 */
       /* if ( remoteMessage.getData().size() > 0 ) {
            Log.d(TAG, "FCM Data Message : " + remoteMessage    .getData());
        }
        if ( remoteMessage.getNotification() != null ) {
            String imgUrl = String.valueOf(remoteMessage.getNotification().getImageUrl());
            Log.d(TAG, "FCM Notification Message : " + remoteMessage    .getData());
        }*/
    }

    @Override
    public void onNewToken(String token) {
        Log.d("Refreshed token: ", token);

    }

    @SuppressLint("LongLogTag")
    private void sendNotification(RemoteMessage remoteMessage) {
        try {
            int notificationId = 0;
            /* 오레오 버전 이상부턴 채널필수 */
            String channelId = String.valueOf(R.string.default_notification_channel_id);
            String channelNm = String.valueOf(R.string.default_notification_channel_name);

            Context mContext = getApplicationContext();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, channelNm, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
                //builder.setVibrate(new long[] {200, 100, 200});       // 진동패턴
            }

            /* PendingIntent는 notification을 터치 시 이동 할 엑티비티를 구현
             * 안드로이드12 PendingIntent 이슈로 끝에 PendingIntent.FLAG_MUTABLE 추가 */
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

           /* 이미지 URL 처리 */
           String imgUrl = String.valueOf(remoteMessage.getNotification().getImageUrl());
            try {
                URL url = new URL(imgUrl);
                bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setLargeIcon(bigPicture)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bigPicture)
                            .bigLargeIcon(null));
            Log.d(TAG, "===== FCM notificationId Message");
            notificationManager.notify(notificationId, builder.build());

        } catch (NullPointerException nullException) {
            Toast.makeText(getApplicationContext(), "알림에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            Log.e("error Notify", nullException.toString());
        }
    }

}