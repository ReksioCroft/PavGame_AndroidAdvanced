package ro.tav.pavgame.presentation.notification;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ro.tav.pavgame.PavGameApplication;
import timber.log.Timber;

public class PavGameNotificationFirebase extends FirebaseMessagingService {
    @Override
    public void onMessageReceived( @NonNull RemoteMessage remoteMessage ) {
        Timber.d("onMessageReceived: %s", remoteMessage.toString());
        if ( remoteMessage.getData().containsKey( "deep_link" ) ) {
            String deepLink = remoteMessage.getData().get( "deep_link" );
            // Handle the deep link here
            // You can start an activity or perform any other action based on the deep link
            PavGameApplication.getApplication().getNotificationManager().notify( PavGameNotificationFactory.getFirebaseNotificationId(),
                    PavGameNotificationFactory.createCustomHelloNotification( this, "firebase", deepLink ) );

        } else {
            PavGameApplication.getApplication().getNotificationManager().notify( PavGameNotificationFactory.getFirebaseNotificationId(),
                    PavGameNotificationFactory.createCustomHelloNotification( this, "onMessageReceived", "tavi was here" ) );
        }
    }

    @Override
    public void onNewToken( @NonNull String token ) {
        super.onNewToken( token );
        Timber.d("onNewToken received");
        Timber.d( token );
    }
}
