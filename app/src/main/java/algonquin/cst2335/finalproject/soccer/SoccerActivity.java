package algonquin.cst2335.finalproject.soccer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.finalproject.R;

public class SoccerActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private String TAG;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    float stars = -1f;
    TextView ed;
    SharedPreferences shared;
    TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soccer_activity_main);

        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar3);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Button button = findViewById(R.id.bt);
        button.setOnClickListener(click -> {
            Intent nextPage = new Intent(SoccerActivity.this, GoalRssActivity.class);
            startActivity(nextPage);
            Context context = this;
            CharSequence text = "loading news";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        });

        shared = getSharedPreferences(getString(R.string.SoccerPreferences), MODE_PRIVATE);
        stars = shared.getFloat(getString(R.string.Rating), -1f);
        ;


        ed = findViewById(R.id.myrating);
        if (stars >= 0f) {
            ed.setText(getString(R.string.RatingAn) + stars);
        } else {
            ed.setText("please rate this great application");

        }
        ed.setOnClickListener(click -> {
            rateapp();
        });
    }

    private void rateapp()
    {
        String editString = ed.getText().toString();

        final RatingBar rating = new RatingBar(this);
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rating.setLayoutParams(lp);
        rating.setNumStars(5);
        rating.setStepSize(1);
        if (stars >= 0f) {
            rating.setRating(stars);
        }
        //add ratingBar to linearLayout
        linearLayout.addView(rating);

        AlertDialog.Builder builder = new AlertDialog.Builder(this )
                .setMessage(getString(R.string.RatingQu))
                .setTitle(getString(R.string.Rating))
                .setIcon(android.R.drawable.btn_star_big_on)
                .setView(linearLayout)
                .setNegativeButton(getString(R.string.No), (dialog, cl) -> {
                    dialog.cancel();
                })
                .setPositiveButton(getString(R.string.Y), (dialog, cl) -> {

                    //Remove the dialog
                    dialog.dismiss();

                    Snackbar.make(ed, getString(R.string.RatingAn)+ rating.getRating(), Snackbar.LENGTH_LONG)
                            .show();
                    if ( stars != rating.getRating()) {
                        SharedPreferences.Editor sh = shared.edit();
                        sh.putFloat(getString(R.string.Rating), rating.getRating());
                        ;
                        sh.commit();
                        ed.setText(getString(R.string.RatingAn) + rating.getRating());
                    }
                    if ( rating.getRating() == 5) {

                    }
                });
        builder.create().show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.soccer_activity_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onNavigationItemSelected(item);
        return true;

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent nextPage;
        switch (item.getItemId()) {
            case R.id.mainmenu:
                break;
            case R.id.readnews:
                nextPage = new Intent(SoccerActivity.this, GoalRssActivity.class);
                nextPage.putExtra("override","news");
                startActivity( nextPage );
                break;
            case R.id.favouites:
                nextPage = new Intent(SoccerActivity.this, GoalRssActivity.class);
                nextPage.putExtra("override","favourites");
                startActivity( nextPage );
                break;
            case R.id.help:
                break;
            case R.id.rateapp:
                rateapp();
                break;
            default:
                return false;
        }
        return true;
    }
}
