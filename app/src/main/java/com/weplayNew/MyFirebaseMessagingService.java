package com.weplayNew;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService";

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "================= onMessageReceived ===================");
        // 앱이 실행 중일 때 (Foreground 상황) 에서 푸쉬를 받으면 호출됩니다. // 백그라운드 상황에서는 호출되지 않고 그냥 알림목록에 알림이 추가됩니다. Log.d(TAG,"Message Arrived");
        /*if ( remoteMessage.getData().size() > 0 ) {
            Log.d(TAG, "FCM Data Message : " + remoteMessage    .getData());

        }*/
        if ( remoteMessage.getNotification() != null ) {
            Notification notification = new NotificationCompat.Builder(this, "ALL")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    /*.setLargeIcon(remoteMessage.getNotification().getImageUrl())*/
                    /*.setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(myBitmap)
                            .bigLargeIcon(null))*/
                    .build();
           /* Notification.Builder notificationBuilder = new Notification.Builder()
                    .set

            NotificationCompat.Builder notificationBuilder =new NotificationCompat.Builder()
                    .set*/
            /*final String messageBody = remoteMessage.getNotification().getBody();
            Log.d(TAG, "FCM Notification Message Body : " + messageBody);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), messageBody, Toast.LENGTH_SHORT).show();
                }
            });*/
        }

    }

       /* private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(this,
                m,intent,PendingIntent.FLAG_ONE_SHOT);
        String channelId =    getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
        notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.geekhaven_transparent)
                        .setContentTitle(remoteMessage.getNotification.getTitle)
                        .setContentText(remoteMessage.getNotification.getBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(m, notificationBuilder.build());*/

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