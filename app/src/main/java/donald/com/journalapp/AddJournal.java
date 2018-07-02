package donald.com.journalapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.Calendar;

import donald.com.journalapp.Models.JournalsModel;

public class AddJournal extends AppCompatActivity {
    private DatabaseReference mJournalsDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private EditText mJournalContent;
    private Button mSelectDate;
    private Spinner mCategory;
    private String uID;
    private int mYear, mMonth, mDay, mHour, mMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        mJournalContent = (EditText) findViewById(R.id.journal_content);
        mSelectDate = (Button) findViewById(R.id.pick_date);
        mCategory =  (Spinner) findViewById(R.id.journal_category);

        mJournalsDatabase = FirebaseDatabase.getInstance().getReference("My Journals");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        uID = mCurrentUser.getUid();
        mSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddJournal.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        mSelectDate.setText(dayOfMonth+ "-"+ (monthOfYear + 1)+ "-"+ year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
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
            submitJournal();
        }

        return super.onOptionsItemSelected(item);
    }

    private void submitJournal(){
        String journalContent = mJournalContent.getText().toString().trim();
        String category = mCategory.getItemAtPosition(mCategory.getSelectedItemPosition()).toString();
        String journalDate = mSelectDate.getText().toString().trim();

        if (TextUtils.isEmpty(journalContent)){
            MDToast.makeText(getApplicationContext(),getString(R.string.journal_content_error),
                    MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show();
        } else if (category.equalsIgnoreCase("Select category")){
            MDToast.makeText(getApplicationContext(),getString(R.string.journal_category_error),
                    MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show();
        } else if (TextUtils.isEmpty(journalDate)|journalDate.equalsIgnoreCase("Select Date")){
            MDToast.makeText(getApplicationContext(),getString(R.string.journal_date_error),
                    MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show();
        } else{

            String id = mJournalsDatabase.push().getKey();
            JournalsModel journals = new JournalsModel(journalContent,category,journalDate);

            mJournalsDatabase.child(uID).child(id).setValue(journals);


            Intent addJournal = new Intent(AddJournal.this, Home.class);

            startActivity(addJournal);

            finish();

        }
    }

}
