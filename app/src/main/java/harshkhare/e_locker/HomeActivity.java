package harshkhare.e_locker;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static harshkhare.e_locker.R.id.nav_logout;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "App Sharing";
    public static final int REQUEST_INVITE = 232;
    private MenuItem logout;
    private FloatingActionButton fabscandoc;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase db;
    private DatabaseReference myref;
    List<ScanModel> doumentList;
    private ArrayList<Object> documentList;
    private RecyclerView rvDesc;
    private ProgressBar pb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        logout = (MenuItem) findViewById(R.id.nav_logout);
        fabscandoc = (FloatingActionButton) findViewById(R.id.fabScandoc);
        fabscandoc.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener =new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user==null){
                    Intent logint =new Intent(HomeActivity.this,LoginMainActivity.class);
                    startActivity(logint);
                    finish();
                }
            }
        };
        initGoogleApi();


        //Recycler View Code
        pb = (ProgressBar) findViewById(R.id.pbbar);
        db = FirebaseDatabase.getInstance();
        myref = db.getReference();
        //Getting data from firebase code (Recycler view)
        myref = db.getReference("docs_db").child(mAuth.getCurrentUser().getUid());
        //creating blank list in memory
        documentList =new ArrayList<>();
        //Recycler View Object
        final RecyclerView rvDescription= (RecyclerView) findViewById(R.id.rvDescription);
        LinearLayoutManager manager= new LinearLayoutManager(this);
        //passing layput manager
        rvDescription.setLayoutManager(manager);
        //setup Listener
        //using anonymous class
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Data is in data Snapshot obj
                documentList.clear();
                if (dataSnapshot.hasChildren()){
                    //here write datasnapshot.children().{then write iterator and chose for loop then next line automaticaaly genrated}
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        documentList.add(new ScanModel(snapshot));
                    }
                    Toast.makeText(HomeActivity.this, "Data loaded Successfully", Toast.LENGTH_SHORT).show();

                    DocumentAdapter adapter=new DocumentAdapter(documentList);
                    rvDescription.setAdapter(adapter);
                }
                else {
                    Toast.makeText(HomeActivity.this, "Data not Available", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });






    }

    private void initGoogleApi() {
        //App Sharing code
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .addApi(AppInvite.API)
                .build();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_camera:
                // todo Handle the camera action
                Intent camera = new Intent(HomeActivity.this, Scanning_OCR.class);
                startActivity(camera);
                break;
            case R.id.nav_fav:
                //todo Favorites documents
                Intent fav=new Intent(HomeActivity.this,FavoritesActivity.class);
                startActivity(fav);
                break;

            case R.id.nav_download:
                // TODO users download documents
                break;

            case R.id.nav_profile:
                //// TODO: get users profile info.

                Intent profileinfo = new Intent(HomeActivity.this, GetProfileInformationActivity.class);
                startActivity(profileinfo);

                break;

            case R.id.nav_about:
                //TODO shows about app information
                Intent about = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(about);
                break;
            case R.id.nav_share:
                //// TODO: shareApp
                sendInvitation();
                return true;


            case R.id.nav_developer:
                //// TODO: send email direct Developer
               /* Intent emailIntent= new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");*/
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"harshkhare93@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT, "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.nav_feed_rate:
                //Todo feedback and Rate app
                Intent feedbackrate = new Intent(this, FeedbackRateus.class);
                startActivity(feedbackrate);
                break;

            case R.id.action_settings:
                //// TODO: Reminder settings


                break;

            case nav_logout:
                //// TODO: user logout
                mAuth.signOut();
                try {
                    LoginManager.getInstance().logOut();
                    AccessToken.setCurrentAccessToken(null);
                } catch (Exception ignored){
                }
                Intent lgtIntent=new Intent(this,LoginMainActivity.class);
                startActivity(lgtIntent);

                finish();
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, Scanning_OCR.class);
        startActivity(i);
    }

    //App Sharing Code Methods
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void sendInvitation() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Check how many invitations were sent and log.
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                Log.d(TAG, "Invitations sent: " + ids.length);
            } else {
                // Sending failed or it was canceled, show failure message to the user
                Log.d(TAG, "Failed to send invitation.");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


}
