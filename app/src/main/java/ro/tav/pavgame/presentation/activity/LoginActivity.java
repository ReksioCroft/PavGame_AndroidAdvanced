package ro.tav.pavgame.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ro.tav.pavgame.PavGameApplication;
import ro.tav.pavgame.R;
import ro.tav.pavgame.presentation.PavGameViewModel;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {
    private static boolean doLogout = false;
    private EditText email, password;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher < Intent > googleSignInLauncher;


    private void googleSignInLaunch( GoogleSignInAccount googleSignInAccount ) {
        Toast.makeText( LoginActivity.this, getString( R.string.loggedIn ), Toast.LENGTH_SHORT ).show();
        PavGameViewModel.setGoogleSignInAccount( googleSignInAccount );
        Intent i = new Intent( LoginActivity.this, MainActivity.class );
        startActivity( i );
    }

    private void handleGoogleSignInTask( Task < GoogleSignInAccount > completedTask ) {


        try {
            GoogleSignInAccount account = completedTask.getResult( ApiException.class );

            // Signed in successfully, show authenticated UI.
            googleSignInLaunch( account );
        } catch ( ApiException e ) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Timber.e( "signInResult:failed code=%s", e.getStatusCode() );
        }

    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
//        PavGameApplication.getApplication().addActivity( this );

        // Firebase auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById( R.id.email );
        password = findViewById( R.id.password );

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged( @NonNull FirebaseAuth firebaseAuth ) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if ( mFirebaseUser != null ) {
                    Toast.makeText( LoginActivity.this, getString( R.string.loggedIn ), Toast.LENGTH_SHORT ).show();
                    PavGameViewModel.setFirebaseUser( mFirebaseAuth.getCurrentUser() );
                    Intent i = new Intent( LoginActivity.this, MainActivity.class );
                    startActivity( i );
                } else
                    Toast.makeText( LoginActivity.this, getString( R.string.loginRequired ), Toast.LENGTH_SHORT ).show();
            }
        };

        Button loginButton = findViewById( R.id.logInButton );
        loginButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                String emailString = email.getText().toString().trim();
                String passwordString = password.getText().toString().trim();

                if ( emailString.isEmpty() ) {
                    email.setError( "Email is required" );
                    email.requestFocus();
                    return;
                }

                if ( !Patterns.EMAIL_ADDRESS.matcher( emailString ).matches() ) {
                    email.setError( "Invalid email" );
                    email.requestFocus();
                    return;
                }

                if ( passwordString.isEmpty() ) {
                    password.setError( "Password is required" );
                    password.requestFocus();
                    return;
                }

                if ( passwordString.length() < 6 ) {
                    password.setError( "Password should be atleast 6 character long" );
                    password.requestFocus();
                    return;
                }

                mFirebaseAuth.signInWithEmailAndPassword( emailString, passwordString ).addOnCompleteListener( LoginActivity.this, new OnCompleteListener < AuthResult >() {
                    @Override
                    public void onComplete( @NonNull Task < AuthResult > task ) {
                        if ( !task.isSuccessful() ) {
                            Toast.makeText( LoginActivity.this, "Login Error. Please try again", Toast.LENGTH_SHORT ).show();
                        } else {
                            PavGameViewModel.setFirebaseUser( mFirebaseAuth.getCurrentUser() );
                            Intent intent = new Intent( LoginActivity.this, MainActivity.class );
                            startActivity( intent );
                        }
                    }
                } );

            }
        } );

        TextView login_notRegistered = findViewById( R.id.registerFromLogin );
        login_notRegistered.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                PavGameViewModel.setFirebaseUser( mFirebaseAuth.getCurrentUser() );
                Intent intent = new Intent( LoginActivity.this, RegisterActivity.class );
                startActivity( intent );
            }
        } );


        // Google gmail auth
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN )
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient( this, gso );

        /// google sign in handle

        googleSignInLauncher = registerForActivityResult( new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if ( result.getResultCode() == Activity.RESULT_OK ) {
                        // There are no request codes
                        Intent data = result.getData();
                        if ( data != null ) {
                            Task < GoogleSignInAccount > task = GoogleSignIn.getSignedInAccountFromIntent( data );
                            handleGoogleSignInTask( task );
                        }
                    }
                } );

        SignInButton googleButton = findViewById( R.id.google_sign_in_button );
        googleButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                googleSignInLauncher.launch( signInIntent );
            }
        } );
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ( !doLogout ) {
            if ( mFirebaseAuth.getCurrentUser() != null ) {
                mFirebaseAuth.addAuthStateListener( mAuthStateListener );
            } else {
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount( this );
                if ( account != null ) {
                    googleSignInLaunch( account );
                }
            }
        }
    }

    public static void setDoLogout( boolean newDoLogout ) {
        doLogout = newDoLogout;
    }


    @Override
    protected void onPause() {
        super.onPause();
        email = findViewById( R.id.email );
        email.setText( "" );
        password = findViewById( R.id.password );
        password.setText( "" );
    }

    @Override
    protected void onResume() {
        super.onResume();

        if ( doLogout ) {
            if ( mFirebaseAuth != null ) {
                while ( mFirebaseAuth.getCurrentUser() != null )
                    mFirebaseAuth.signOut();
                PavGameViewModel.setFirebaseUser( null );
            }
            if ( mGoogleSignInClient != null ) {
                mGoogleSignInClient.revokeAccess();
                PavGameViewModel.setGoogleSignInAccount( null );
            }
        }

        doLogout = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        PavGameApplication.getApplication().removeActivity( this );
    }
}
