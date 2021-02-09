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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ro.tav.pavgame.presentation.notification.PavGameNotificationChannelFactory;
import timber.log.Timber;

public class PavGameApplication extends Application {
    private static PavGameApplication context;
    private List < Activity > activities;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        activities = new ArrayList <>();

        setupLibs();

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            NotificationManager notificationManager = ( NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE );
            notificationManager.createNotificationChannel( PavGameNotificationChannelFactory.createProcessingWorkNotificationChannel() );
        }
    }

    private void setupLibs() {
        Stetho.initializeWithDefaults( this );

        if ( !BuildConfig.my_flag ) {
            Timber.plant( new Timber.DebugTree() );
        } else {
            Timber.plant( new Timber.Tree() {
                @Override
                protected void log( int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t ) {
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

    public static Context getContext() {
        return context;
    }

    public static void addActivity(Activity a){
        context.activities.add( a );
    }
}
