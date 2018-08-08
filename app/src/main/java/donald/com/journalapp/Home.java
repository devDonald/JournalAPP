package donald.com.journalapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.List;

import donald.com.journalapp.Models.JournalsModel;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mJournalRecycler;
    private DatabaseReference mAllJournalsDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String uID;
    private static final String TAG = Home.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;
    private List<JournalsModel> list;
    FirebaseRecyclerAdapter<JournalsModel,RecyclerAdapter> firebaseRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();
        mAllJournalsDatabase = FirebaseDatabase.getInstance().getReference("My Journals").child(uID);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handles addition of all journals
                startActivity(new Intent(Home.this,AddJournal.class));
                finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mJournalRecycler = (RecyclerView)findViewById(R.id.all_journals_recycler);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(Home.this);
        mJournalRecycler.setLayoutManager(manager);
        mJournalRecycler.setItemAnimator( new DefaultItemAnimator());

        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData data) {
                        if (data == null) {
                            Log.d(TAG, "getInvitation: no data");
                            return;
                        }

                        // Get the deep link
                        Uri deepLink = data.getLink();

                        // Extract invite
                        FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(data);
                        if (invite != null) {
                            String invitationId = invite.getInvitationId();
                        }

                        // Handle the deep link
                        // [START_EXCLUDE]
                        Log.d(TAG, "deepLink:" + deepLink);
                        if (deepLink != null) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setPackage(getPackageName());
                            intent.setData(deepLink);

                            startActivity(intent);
                        }
                        // [END_EXCLUDE]
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_my_journal) {
            //handles display of all Journals
            Intent myJournals = new Intent(Home.this,Home.class);
            startActivity(myJournals);
            finish();

        } else if (id == R.id.nav_add_journal) {
            //handles addition of all journals
            startActivity(new Intent(Home.this,AddJournal.class));
            finish();

        } else if (id == R.id.nav_share) {
            //handles sharing of app
            onInviteClicked();

        } else if (id == R.id.nav_exit_app) {
            //handles exiting app
            FirebaseAuth.getInstance().signOut();
            Intent signout = new Intent(Home.this,LandingPage.class);
            startActivity(signout);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<JournalsModel, RecyclerAdapter>(
                JournalsModel.class,
                R.layout.all_journals_layout,
                RecyclerAdapter.class,
                mAllJournalsDatabase
        ) {
            @Override
            protected void populateViewHolder(RecyclerAdapter viewHolder,JournalsModel model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setContent(model.getContent());
                viewHolder.setCategory(model.getCategory());
                viewHolder.setDate(model.getDate());
            }

            @Override
            public RecyclerAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                RecyclerAdapter viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new RecyclerAdapter.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //Toast.makeText(getApplicationContext(), "Item clicked at " + position, Toast.LENGTH_SHORT).show();

                        Intent wholeProfile=new Intent(Home.this,ViewJournal.class);
                        wholeProfile.putExtra("position",firebaseRecyclerAdapter.getRef(position).getKey());

                        startActivity(wholeProfile);

                    }

                });

                return viewHolder;
            }
        };
        mJournalRecycler.setAdapter(firebaseRecyclerAdapter);
    }

    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
    // [END on_invite_clicked]

    // [START on_activity_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // [START_EXCLUDE]
                MDToast.makeText(getApplicationContext(),getString(R.string.send_failed),
                        MDToast.TYPE_ERROR,MDToast.LENGTH_LONG).show();
                // [END_EXCLUDE]
            }
        }
    }

}
