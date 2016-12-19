package mau.restaurantapp.utils.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Yoouughurt on 19-12-2016.
 */

public class FirebaseNotificationService extends FirebaseMessagingService {

    /**
     * On message received will be called whenever a notification is sent to the device while app is in foreground.
     * If in background it will be send to the notificationTray
     *
     * @param remoteMessage The message received
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("FCM", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getNotification() != null) {
            Log.d("FCM", "Notification Message Body: " + remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().containsKey("post_id") && remoteMessage.getData().containsKey("post_title")) {
            Log.d("Post ID", remoteMessage.getData().get("post_id").toString());
            Log.d("Post Title", remoteMessage.getData().get("post_title").toString());
            // eg. Server Send Structure data:{"post_id":"12345","post_title":"A Blog Post"}
        }
    }
}
