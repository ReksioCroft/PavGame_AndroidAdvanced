package ro.tav.pavgame.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.cardview.widget.CardView;

import ro.tav.pavgame.R;

public class CreditsActivity extends Activity {
    private final static String github = "https://github.com/ReksioCroft";
    private final static String[] emails = { "octavian.staicu@s.unibuc.ro", "octavian-florin.staicu@my.fmi.unibuc.ro" };

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_credits );
//        PavGameApplication.getApplication().addActivity( this );

        CardView view = findViewById( R.id.cardViewProgrammer );
        view.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent();
                intent.setAction( Intent.ACTION_SEND );
                intent.putExtra( Intent.EXTRA_EMAIL, emails );
                intent.putExtra( Intent.EXTRA_SUBJECT, "[PAV] feedback" );
                intent.putExtra( Intent.EXTRA_TEXT, emails[ 0 ] );
                intent.setType( "text/*" );
                startActivity( Intent.createChooser( intent, null ) );
            }
        } );

        view = findViewById( R.id.cardViewCredits );
        view.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( github ) );
                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                getApplicationContext().startActivity( intent );
            }
        } );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        PavGameApplication.getApplication().removeActivity( this );
    }
}
