package ru.vetatto.investing.investing;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class TestFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "TestFbseMsgngSvc";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        /*if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());*/

            int color = (1<<16)|(1<<8)|(0);
            ShowNotification("Обновление",remoteMessage.getNotification().getBody(),color);
        //}

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }


    @Override
    public void onDeletedMessages() {
        // In some situations, FCM may not deliver a message. This occurs when there are too many messages (>100) pending for your app on a particular device
        // at the time it connects or if the device hasn't connected to FCM in more than one month. In these cases, you may receive a callback
        // to FirebaseMessagingService.onDeletedMessages() When the app instance receives this callback, it should perform a full sync with your app server.
        // If you haven't sent a message to the app on that device within the last 4 weeks, FCM won't call onDeletedMessages().
    }


    void ShowNotification(String title, String text, int color) {
        NotificationCompat.Builder mNotify = new NotificationCompat.Builder(getApplicationContext(), "");
        mNotify.setLights(color, 100, 200);
        mNotify.setSmallIcon(R.drawable.ic_briefcase_outline);
        mNotify.setContentTitle(title);
        mNotify.setContentText(text);
        mNotify.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int mId = 1001;
        try { mNotificationManager.notify(mId, mNotify.build()); }
        catch (Exception e) { e.printStackTrace(); }
    }


}