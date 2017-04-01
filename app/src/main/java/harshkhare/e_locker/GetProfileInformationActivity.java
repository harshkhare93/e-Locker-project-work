package harshkhare.e_locker;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import static android.R.attr.data;
import static android.R.attr.onClick;

public class GetProfileInformationActivity extends AppCompatActivity {


    private ImageView imageView;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_profile_information);
        //Initialize ImageView
        imageView = (ImageView) findViewById(R.id.imageView);
        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
        //Loading image from below url into imageView

        Picasso.with(this)
                .load(user.getPhotoUrl())
                .into(imageView);


    }
}
