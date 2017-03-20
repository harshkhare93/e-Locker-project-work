package harshkhare.e_locker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Button;

public class LoginMainActivity extends AppCompatActivity  implements View.OnClickListener {

    private Button googlebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        googlebtn = (Button) findViewById(R.id.btng);
        googlebtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        /*googlebtn.animate()
                .alpha(1)
                .translationYBy(100)
                .setDuration(500)
                .setInterpolator(new AccelerateInterpolator())
                .start();*/
        Intent i=new Intent(LoginMainActivity.this , Home.class);
        startActivity(i);
    }
}
