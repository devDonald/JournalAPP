package donald.com.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditJournal extends AppCompatActivity {
    private static final String TAG ="EditJournal" ;
    private EditText mJournalContent;
    private DatabaseReference mAllJournalsDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String uID;
    private String position;
    private TextView mCategory, mDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journal);

        mJournalContent = (EditText) findViewById(R.id.edit_content);
        mCategory = (TextView) findViewById(R.id.edit_category);
        mDate = (TextView) findViewById(R.id.edit_date);

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

                        mJournalContent.setText(dataSnapshot.child("content").getValue(String.class));
                        mCategory.setText(dataSnapshot.child("category").getValue(String.class));
                        mDate.setText(dataSnapshot.child("date").getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "loadJournals:onCancelled", databaseError.toException());

                    }
                };
                userRef.addListenerForSingleValueEvent(valueEventListener);


            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.journal_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            updateJournal();
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateJournal() {
        String updateContent = mJournalContent.getText().toString().trim();
        if (!TextUtils.isEmpty(updateContent)){

            mAllJournalsDatabase.child(position).child("content").setValue(updateContent);

            Intent viewJournal = new Intent(EditJournal.this, Home.class);
            viewJournal.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            viewJournal.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            viewJournal.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(viewJournal);
            finish();
        }
    }

}
