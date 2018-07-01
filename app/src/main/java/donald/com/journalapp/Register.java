package donald.com.journalapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.valdesekamdem.library.mdtoast.MDToast;

public class Register extends AppCompatActivity {
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = (EditText) findViewById(R.id.register_email);
        mProgressView = (ProgressBar) findViewById(R.id.register_progress);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        if (mCurrentUser!=null){
            MDToast.makeText(getApplicationContext(),getString(R.string.register_success),
                    MDToast.LENGTH_LONG,MDToast.TYPE_SUCCESS).show();
            startActivity(new Intent(Register.this,Home.class));
            finish();
        }

        mPasswordView = (EditText) findViewById(R.id.register_password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_register_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

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
            // perform the user Register attempt.
            mProgressView.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgressView.setVisibility(View.INVISIBLE);

                            if (task.isSuccessful()){
                                MDToast.makeText(getApplicationContext(),getString(R.string.register_success),
                                        MDToast.LENGTH_LONG,MDToast.TYPE_SUCCESS).show();
                                startActivity(new Intent(Register.this,Login.class));
                                finish();
                            } else{
                                MDToast.makeText(getApplicationContext(),getString(R.string.register_error),
                                        MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show();
                            }
                        }
                    });


        }
    }
}
