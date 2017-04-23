package harshkhare.e_locker;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;


public class GetProfileInformationActivity extends AppCompatActivity {


    public static final String TAG = "Profile";
    private ImageView imageView;
    private FirebaseAuth auth;
    private TextView email;
    private TextView name;
    private ProgressBar progressBar;
    private String username;
    private String email1;
    private boolean emailVerified;
    private String uid;
    private String providerId;
    private UserInfo profile;
    private Uri photoUrl;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_profile_information);
        //Initialize ImageView
        imageView = (ImageView) findViewById(R.id.imageView);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        getCurrentUser();//Calling method

        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener mAuthListener;
        mAuthListener=new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if (user!=null){
                    //User is signed in
                    Log.d(TAG,"onAuthStateChanged : signed_in");



                }else{
                    //User is Signed out
                    Log.d(TAG,"onAuthStateChanged : signed_out ");

                }
            }
        };


        //Loading image from below url into imageView
        Picasso.with(this)
                .load(user.getPhotoUrl())
                .into(imageView);


    }
    public void getCurrentUser()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            String username=user.getDisplayName();
            String useremail=user.getEmail();
            String uid=user.getUid();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
