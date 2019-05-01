package berger.mitchell.ece563.Activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import berger.mitchell.ece563.Fragments.HistoryFragment;
import berger.mitchell.ece563.Fragments.MaxFragment;
import berger.mitchell.ece563.Fragments.ProfileFragment;
import berger.mitchell.ece563.Fragments.WorkoutFragment;
import berger.mitchell.ece563.R;
import berger.mitchell.ece563.SharedPref;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPref.init(this);

        drawer = findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportActionBar().setTitle("Today's Workout");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new WorkoutFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_workout);
        }
    }

    @Override
    public void onBackPressed() {
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
        //setSupportActionBar(toolbar);

        switch (item.getItemId()) {
            case R.id.nav_workout:
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("MMdyyyy");
                SharedPref.write("Date",df.format(c));
                getSupportActionBar().setTitle("Today's Workout");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WorkoutFragment()).commit();
                break;
            case R.id.nav_history:
                getSupportActionBar().setTitle("History");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistoryFragment()).commit();
                break;

            case R.id.nav_profile:
                getSupportActionBar().setTitle("Profile");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;

            case R.id.nav_max:
                getSupportActionBar().setTitle("Max Lifts");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MaxFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
