package ro.tav.pavgame.presentation.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class PavGameService extends Service {
    public static final String TYPE_KEY = "type";

    public static final int TYPE_BASIC = 0;
    public static final int TYPE_ADVANCED = 1;
    public static final int TYPE_FINISH = 2;

    private long startMillis;

    @Override
    public void onCreate() {
        super.onCreate();
        startMillis = System.currentTimeMillis();
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId ) {
        int type = intent.getIntExtra( TYPE_KEY, TYPE_BASIC );

        if ( type == TYPE_BASIC ) {
            startForeground( PavGameNotificationFactory.getServiceNotificationId(), PavGameNotificationFactory.createProcessingWorkNotification( this ) );

            Timber.i( "Basic work in progress" );

        } else if ( type == TYPE_ADVANCED ) {
            Timber.i( "Advanced work in progress" );

        } else if ( type == TYPE_FINISH ) {
            Timber.i( "Finishing service" );
            stopSelf();

        } else {
            Timber.w( "Unknown service type. Killing." );
            stopSelf();
        }

        return super.onStartCommand( intent, flags, startId );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        long currentMillis = System.currentTimeMillis();
        Timber.d( "Service was active for %d seconds", TimeUnit.MILLISECONDS.toSeconds( currentMillis - startMillis ) );
    }

    @Nullable
    @Override
    public IBinder onBind( Intent intent ) {
        // ignore, unused for now
        return null;
    }
}
