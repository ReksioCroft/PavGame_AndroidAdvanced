package ro.tav.pavgame.presentation.viewmodel;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import ro.tav.pavgame.BuildConfig;
import ro.tav.pavgame.domain.DaggerPavGameDependencyProviderComponent;
import ro.tav.pavgame.domain.PavGameDependencyProviderComponent;
import ro.tav.pavgame.domain.PavGameDependencyProviderModule;
import ro.tav.pavgame.presentation.notification.PavGameNotificationChannelFactory;
import timber.log.Timber;

public class PavGameApplication extends Application {
    //private final List < Activity > activities;
    private static NotificationManager notificationManager = null;
    private PavGameDependencyProviderComponent pavGameDependencyProviderComponent = null;

    public PavGameApplication() {
        super();
//        activities = new ArrayList <>();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setupLibs();

        pavGameDependencyProviderComponent = DaggerPavGameDependencyProviderComponent
                .builder()
                .application( this )
                .pavGameDependencyProviderModule( new PavGameDependencyProviderModule( this ) )
                .build();
    }

    private void setupLibs() {
        //firebase google play security check
        FirebaseApp.initializeApp(/*context=*/ this );
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory( PlayIntegrityAppCheckProviderFactory.getInstance() );

        //stetho for debug from chromieum web browser
        if ( !isRoboUnitTest() ) { //it does not work with roboelectric test framework
            Stetho.initializeWithDefaults( this );
        }

        //timber debugging
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

        //notifications ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O )
        notificationManager = ( NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE );
        notificationManager.createNotificationChannel( PavGameNotificationChannelFactory.createProcessingWorkNotificationChannel() );
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


    public static NotificationManager getNotificationManager() {
        return notificationManager;
    }


    PavGameDependencyProviderComponent getPavGameDependencyProviderComponent() {
        return pavGameDependencyProviderComponent;
    }

    private static boolean isRoboUnitTest() {
        return "robolectric".equals( Build.FINGERPRINT );
    }
}
