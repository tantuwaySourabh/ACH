package com.ebabu.ach.firebase;

import android.content.Context;

import com.ebabu.ach.Utils.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private Context context;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        context = this;
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
  //      Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Utils.generateNotification(context, remoteMessage.getData());
        //Calling method to generate notification
    }
 
   /* //This method is only generating push notification
    //It is same as we did in earlier posts 
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
 
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Firebase Push Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
 
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
 
        notificationManager.notify(0, notificationBuilder.build());
    }*/
}