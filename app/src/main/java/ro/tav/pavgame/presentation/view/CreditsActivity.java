package ro.tav.pavgame.presentation.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.R;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_credits );
        PavGameApplication.addActivity( this );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PavGameApplication.removeActivity( this );
    }
}
