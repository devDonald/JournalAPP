package donald.com.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.valdesekamdem.library.mdtoast.MDToast;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private TextView mForgetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.login_email);
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
        mForgetPassword = (TextView) findViewById(R.id.tv_forgot_password);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        if (mCurrentUser!=null){
            MDToast.makeText(getApplicationContext(),getString(R.string.login_success),
                    MDToast.LENGTH_LONG,MDToast.TYPE_SUCCESS).show();
            startActivity(new Intent(Login.this,Home.class));
            finish();
        }

        mForgetPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,PasswordRecovery.class));
                finish();
            }
        });
        mPasswordView = (EditText) findViewById(R.id.login_password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));

        } else if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));

        } else if (!EmailValidator.getInstance().validate(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));

        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mProgressView.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgressView.setVisibility(View.INVISIBLE);

                            if (task.isSuccessful()){
                                MDToast.makeText(getApplicationContext(),getString(R.string.login_success),
                                        MDToast.LENGTH_LONG,MDToast.TYPE_SUCCESS).show();
                                startActivity(new Intent(Login.this,Home.class));
                                finish();
                            } else{
                                MDToast.makeText(getApplicationContext(),getString(R.string.error_invalid_email_password),
                                        MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show();
                            }
                        }
                    });


        }
    }

}

