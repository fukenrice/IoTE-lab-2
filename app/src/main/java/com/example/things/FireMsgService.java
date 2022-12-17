package com.example.things;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
//import android.support.v4.app.NotificationCompat;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.things.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
public class FireMsgService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);



        Log.d("Msg", "Message received ["+remoteMessage+"]");
// Create Notification
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                intent,
                PendingIntent.FLAG_MUTABLE);//update 2022! раньше был
        String info = null;


        Notification notification = null;
        NotificationChannel notchannel1= null;


        if (remoteMessage.getData().size() > 0) {
            info = remoteMessage.getData().get("message");
        }

        if (remoteMessage.getNotification() != null) {
            info = remoteMessage.getNotification().getBody();
        }

        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Message")
                .setContentText(info)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1410, notificationBuilder.build());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notchannel1 = new NotificationChannel("mychannel","mychannel",
                    NotificationManager.IMPORTANCE_HIGH);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this,"mychannel")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Message")
                    .setContentText(info)
                    .setAutoCancel(true)
                    .setTicker(info)
                    .setChannelId("mychannel")
                    .setContentIntent(pendingIntent)
                    .build();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notchannel1);
            notificationManager.notify(1410, notification);
        }

    }
}
