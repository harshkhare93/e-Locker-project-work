package harshkhare.e_locker;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static android.R.attr.data;
import static android.R.attr.onClick;

public class GetProfileInformationActivity extends AppCompatActivity {


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_profile_information);
        //Initialize ImageView
        imageView = (ImageView) findViewById(R.id.imageView);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        //  progressBar = (ProgressBar) findViewById(R.id.progressbar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Loading image from below url into imageView

        Picasso.with(this)
                .load(user.getPhotoUrl())
                .into(imageView);
        if (user!=null)
        {
            username = user.getDisplayName();
            email1 = user.getEmail();
            emailVerified = user.isEmailVerified();
            uid = user.getUid();
        }





    }
}
