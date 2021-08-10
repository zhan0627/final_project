package algonquin.cst2335.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

/**
 * Purpose: Android Mobile Interface Project, Part: OcTransportation
 * This is the OcTransportation page,mainly used for Random Route when the user is able to choose different type of Route
 * the result is shown in the list view
 * once you click on the selected route type, the the number of route base on their choice will be shown in another page
 * All the information is shown in the page
 */

public class MainRoute extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    SharedPreferences pref_name;
    private EditText enterName;
    private Button homeStartButton;
    private SeekBar seekbar;
    int RouteType;
    int Difficulty;
    int progressChangedValue;
    private RadioButton multipleRadio, trueFalseRadio, easyRadio, mediumRadio, hardRadio;

    private DrawerLayout drawer;
    private NavigationView navigationView;


    /***this is on create for MainRoute class
     *  overriding the class appCompatActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_route);

        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        homeStartButton = findViewById(R.id.home_start);

        TextView questions = findViewById(R.id.question);
        seekbar = findViewById(R.id.SeekBar);
        trueFalseRadio = findViewById(R.id.radio1);
        multipleRadio = findViewById(R.id.radio2);
        easyRadio = findViewById(R.id.radio4);
        mediumRadio = findViewById(R.id.radio5);
        hardRadio = findViewById(R.id.radio6);

        enterName = findViewById(R.id.EnterName);

        pref_name = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        String namedef = pref_name.getString("name", "");


        if (pref_name.contains("userName")) {
            enterName.setText(pref_name.getString("userName", ""));
        }
        if (pref_name.contains("routeCount")) {
            seekbar.setProgress(pref_name.getInt("routeCount", -1));
        }
        if (pref_name.contains("RouteType")) {
            RouteType = pref_name.getInt("RouteType", -1);
        }
        if (pref_name.contains("Difficulty")) {
            Difficulty = pref_name.getInt("Difficulty", -1);
        }

        progressChangedValue = seekbar.getProgress();
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                questions.setText(getResources().getString(R.string.AskQuNum) + progressChangedValue);
            }
        });


        homeStartButton.setOnClickListener(click ->
        {
            // validation
            if (enterName.getText().toString().equals("")) {
                Toast.makeText(this, R.string.please_enter_name, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!trueFalseRadio.isChecked() && !multipleRadio.isChecked()) {
                Toast.makeText(this, R.string.please_select_type, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!easyRadio.isChecked() && !mediumRadio.isChecked() && !hardRadio.isChecked()) {
                Toast.makeText(this, R.string.please_select_difficulty, Toast.LENGTH_SHORT).show();
                return;
            }


            Intent goToProfile = new Intent(MainRoute.this, RouteInformation.class);
            goToProfile.putExtra("name", enterName.getText().toString());
            goToProfile.putExtra("routeCount", progressChangedValue);
            goToProfile.putExtra("RouteType", RouteType);
            goToProfile.putExtra("Difficulty", Difficulty);
            startActivity(goToProfile);
        });

        Button highScore = findViewById(R.id.highScore);
        highScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainRoute.this, HighScoreActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        pref_name = getSharedPreferences("sharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = pref_name.edit();
        TextView userName = findViewById(R.id.EnterName);

        if (multipleRadio.isChecked()) RouteType = 0;
        else RouteType = 1;
        if (easyRadio.isChecked()) Difficulty = 0;
        else if (mediumRadio.isChecked()) Difficulty = 1;
        else if (hardRadio.isChecked()) Difficulty = 2;

        myEdit.putString("userName", userName.getText().toString());

        myEdit.putInt("routeCount", progressChangedValue);
        myEdit.putInt("RouteType", RouteType);
        myEdit.putInt("Difficulty", Difficulty);
        myEdit.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.openhighscore:
                startActivity(new Intent(this, HighScoreActivity.class));
                break;
            case R.id.gotomain:
                finish();
                break;
            case R.id.help:
                new AlertDialog.Builder(MainRoute.this)
                        .setTitle(R.string.help)
                        .setMessage(R.string.help_message)
                        .setNeutralButton(R.string.close, null)
                        .show();
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onNavigationItemSelected(item);
        return true;

    }
}

