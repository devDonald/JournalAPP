package donald.com.journalapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewJournal extends AppCompatActivity {
    private DatabaseReference mAllJournalsDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String uID;
    private TextView mContent, mCategory, mDate;
    private String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journal);

        mContent = (TextView) findViewById(R.id.text_full_content);
        mCategory = (TextView) findViewById(R.id.full_category);
        mDate = (TextView)findViewById(R.id.full_date);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();
        mAllJournalsDatabase = FirebaseDatabase.getInstance().getReference("My Journals").child(uID);


        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            position = extras.getString("position");
            if (position != null) {
                DatabaseReference userRef = mAllJournalsDatabase.child(position);

                Log.d("userref", "" + userRef);
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Log.d("ds",""+ds);

                        mContent.setText(dataSnapshot.child("content").getValue(String.class));
                        mCategory.setText(dataSnapshot.child("category").getValue(String.class));
                        mDate.setText(dataSnapshot.child("date").getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                userRef.addListenerForSingleValueEvent(valueEventListener);


            }

        }
    }
}
