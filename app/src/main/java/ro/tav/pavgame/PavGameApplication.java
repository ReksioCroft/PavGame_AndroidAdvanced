package ro.tav.pavgame;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

import com.facebook.stetho.Stetho;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ro.tav.pavgame.presentation.notification.PavGameNotificationChannelFactory;
import timber.log.Timber;

public class PavGameApplication extends Application {
    private static PavGameApplication pavGameApplication;
    //private final List < Activity > activities;
    private NotificationManager notificationManager = null;

    public PavGameApplication() {
        super();
        pavGameApplication = this;
//        activities = new ArrayList <>();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setupLibs();

//        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
        notificationManager = ( NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE );
        notificationManager.createNotificationChannel( PavGameNotificationChannelFactory.createProcessingWorkNotificationChannel() );
//        }
    }

    private void setupLibs() {
        Stetho.initializeWithDefaults( this );

        if ( !BuildConfig.my_flag ) {
            Timber.plant( new Timber.DebugTree() );
        } else {
            Timber.plant( new Timber.Tree() {
                @Override
                protected void log( int priority, @Nullable String tag, @NonNull String message, @Nullable Throwable t ) {
                    if ( priority < Log.INFO ) {
                        return;
                    }

                    if ( t != null ) {
                        FirebaseCrashlytics.getInstance().recordException( t );
                    }

                    String crashlyticsMessage = String.format( "[%s] %s", tag, message );
                    FirebaseCrashlytics.getInstance().log( crashlyticsMessage );
                }
            } );
        }
    }

//    @Override
//    public void onTerminate() {
//        super.onTerminate();
//        if ( !activities.isEmpty() )
//            Timber.wtf( "aplicatia inca are activitati, desi a fost terminata" );
//        else
//            Timber.d( "Aplicatia -> onTerminate() nu mai are activitati" );
//    }

//    public void addActivity( Activity a ) {
//        pavGameApplication.activities.add( a );
//    }
//
//    public void removeActivity( Activity a ) {
//        pavGameApplication.activities.remove( a );
//    }

    public static PavGameApplication getApplication() {
        return pavGameApplication;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }
}
