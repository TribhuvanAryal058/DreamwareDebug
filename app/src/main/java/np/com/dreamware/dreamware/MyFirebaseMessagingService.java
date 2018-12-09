package np.com.dreamware.dreamware;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FMS";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, remoteMessage.toString());
    }

}
