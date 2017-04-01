package harshkhare.e_locker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import static android.R.attr.id;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        // automatically handle clicks on the Home/Up button, so long
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
                break;
            case R.id.nav_upload:
                //todo upload documents
                break;

            case R.id.nav_download:
                // TODO users download documents
                break;

            case R.id.nav_profile:
                //// TODO: get users profile info.

                Intent profileinfo=new Intent(Home.this,GetProfileInformationActivity.class);
                startActivity(profileinfo);

                break;

            case R.id.nav_about:
                //TODO shows about app information
                Intent about = new Intent(Home.this, AboutActivity.class);
                startActivity(about);
                break;
            case R.id.nav_share:
                //// TODO: shareApp
                break;

            case R.id.nav_feedback:
                //// TODO: send feedback
                Intent emailIntent= new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");

                startActivity(Intent.createChooser(emailIntent,"Send your email in:"));
               /* String aEmailList[] = { "user1@host.com","user2@host.com" };
                String aEmailCCList[] = { "user3@host.com","user4@host.com"};
                String aEmailBCCList[] = { "user5@host.com" };

                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
                emailIntent.putExtra(android.content.Intent.EXTRA_CC, aEmailCCList);
                emailIntent.putExtra(android.content.Intent.EXTRA_BCC, aEmailBCCList);
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My subject");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "My message body.");
                emailIntent.setType("plain/text");
                startActivity(emailIntent);*/
                break;

            case R.id.action_settings:
                //// TODO: Reminder settings
                break;

            case R.id.nav_logout:
                //// TODO: user logout
                finish();
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
