package donald.com.journalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.valdesekamdem.library.mdtoast.MDToast;

public class LandingPage extends AppCompatActivity {
    private Button mLoginButton;
    private Button mRegisterButton;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        mLoginButton = findViewById(R.id.bt_login);
        mRegisterButton = findViewById(R.id.bt_register);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        if (mCurrentUser!=null){
            MDToast.makeText(getApplicationContext(),getString(R.string.login_success),
                    MDToast.LENGTH_LONG,MDToast.TYPE_SUCCESS).show();
            startActivity(new Intent(LandingPage.this,Home.class));
            finish();
        }

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(LandingPage.this, Login.class);
                startActivity(login);
                finish();
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(LandingPage.this, Register.class);
                startActivity(login);
                finish();
            }
        });
    }
}
