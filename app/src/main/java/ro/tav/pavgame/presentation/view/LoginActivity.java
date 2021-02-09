package ro.tav.pavgame.presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import ro.tav.pavgame.R;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    static FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        mFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById( R.id.email );
        password = findViewById( R.id.password );

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged( @NonNull FirebaseAuth firebaseAuth ) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if ( mFirebaseUser != null ) {
                    Toast.makeText( LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT ).show();
                    MainActivity.setUserName( mFirebaseUser.getEmail() );
                    Intent i = new Intent( LoginActivity.this, MainActivity.class );
                    startActivity( i );
                } else
                    Toast.makeText( LoginActivity.this, "Please Login", Toast.LENGTH_SHORT ).show();

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
                            MainActivity.setUserName( Objects.requireNonNull( mFirebaseAuth.getCurrentUser() ).getEmail() );
                            Intent intToMain = new Intent( LoginActivity.this, MainActivity.class );
                            startActivity( intToMain );
                        }
                    }
                } );

            }
        } );

        TextView login_notRegistered = findViewById( R.id.registerFromLogin );
        login_notRegistered.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Intent intent = new Intent( LoginActivity.this, RegisterActivity.class );
                startActivity( intent );
            }
        } );
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if ( mFirebaseAuth.getCurrentUser() != null )
            mFirebaseAuth.addAuthStateListener( mAuthStateListener );
    }

    @Override
    protected void onStop() {
        super.onStop();
        email = findViewById( R.id.email );
        email.setText( "" );
        password = findViewById( R.id.password );
        password.setText( "" );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAuth.signOut();
        finish();
    }

    public static FirebaseAuth getFireBaseCurrentInstance() {
        return mFirebaseAuth;
    }
}
