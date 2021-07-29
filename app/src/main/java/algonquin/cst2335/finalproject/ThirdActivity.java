package algonquin.cst2335.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    ImageView iv ;
    /**Describes the result the variable, passwordText holds what the user typed*/
    private EditText cityField;
    Bitmap image = null;
    /**This represents the result of the password complexity check*/
    private TextView textView;

    /**The Button the user clicks to login*/
    private Button forecastBtn ;
    double year;
    double releaseDate;
    double ratings;
    int actors;
    String plot;
    String iconName;

    float txtSize = 14.0f;
    boolean isVisible = true;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.views:
            case R.id.nav_views:
                isVisible = ! isVisible;
                // forecastBtn.setVisibility(isVisible?View.VISIBLE:View.INVISIBLE);
                TextView tv = findViewById(R.id.year);
                tv.setVisibility(View.INVISIBLE);

                tv = findViewById(R.id.releaseDate);
                tv.setVisibility(View.INVISIBLE);

                tv = findViewById(R.id.ratings);
                tv.setVisibility(View.INVISIBLE);

                tv = findViewById(R.id.actors);
                tv.setVisibility(View.INVISIBLE);

                tv = findViewById(R.id.plot);
                tv.setVisibility(View.INVISIBLE);

                iv.setVisibility(View.INVISIBLE);
                cityField.setText("");
                break;

            case R.id.increase:
            case R.id.nav_increase:
                txtSize++;
                TextView text = findViewById(R.id.year);
                text.setTextSize(txtSize);

                text = findViewById(R.id.releaseDate);
                text.setTextSize(txtSize);

                text = findViewById(R.id.ratings);
                text.setTextSize(txtSize);

                text = findViewById(R.id.actors);
                text.setTextSize(txtSize);

                text = findViewById(R.id.plot);
                text.setTextSize(txtSize);
                iv.setVisibility(View.INVISIBLE);

                break;

            case R.id.decrease:
            case R.id.nav_decrease:
                txtSize = Float.max(txtSize-1, 5);
                TextView tV = findViewById(R.id.year);
                tV.setTextSize(txtSize);

                tV = findViewById(R.id.releaseDate);
                tV.setTextSize(txtSize);

                tV = findViewById(R.id.ratings);
                tV.setTextSize(txtSize);

                tV = findViewById(R.id.actors);
                tV.setTextSize(txtSize);

                tV = findViewById(R.id.plot);
                tV.setTextSize(txtSize);
                iv.setVisibility(View.INVISIBLE);

                cityField.setTextSize(txtSize);
                textView.setTextSize(txtSize);
                forecastBtn.setTextSize(txtSize);
                break;

            case 5:
                String cityName = item.getTitle().toString();
                runForecast(cityName);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        cityField = findViewById(R.id.movieTextField);
        forecastBtn = findViewById(R.id.forecastButton);

        NavigationView popout_menu = findViewById(R.id.popout_menu);
        popout_menu.setNavigationItemSelectedListener((item) ->
        {
            onOptionsItemSelected(item);
            drawer.closeDrawer(GravityCompat.START);
            return false;

        });

        forecastBtn.setOnClickListener(click -> {
            String cityName = cityField.getText().toString();
            myToolbar.getMenu().add(1,5,10,cityName).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            runForecast(cityName);


        });

    }

    void runForecast(String cityName)
    {
        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute(() -> {

            try {


                String stringURL = "http://www.omdbapi.com/?"
                        + URLEncoder.encode (cityName, "UTF-8")
                        + "apikey=6c9862c2";


                URL url = new URL(stringURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                String text = (new BufferedReader(
                        new InputStreamReader(in, StandardCharsets.UTF_8)))
                        .lines()
                        .collect(Collectors.joining("\n"));

                JSONObject theDocument = new JSONObject( text );
                JSONObject mainObj = theDocument.getJSONObject("main");

                year = mainObj.getDouble("year");
                releaseDate = mainObj.getDouble ("runtime");
                ratings = mainObj.getDouble ("rated");
                actors = mainObj.getInt("actors");

                JSONArray weatherArray = theDocument.getJSONArray ( "weather" );
                JSONObject firstObj = weatherArray.getJSONObject(0);
                plot = firstObj.getString("plot");
                iconName = firstObj.getString("icon");

                URL imgUrl = new URL( "https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_SX300" + iconName + ".jpg" );
                HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());



                }
                iv = findViewById(R.id.icon);
                runOnUiThread( (  )  -> {
                    TextView tv = findViewById(R.id.year);
                    tv.setText("The year of release is " + year);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.releaseDate);
                    tv.setText("The min runtime is " + releaseDate);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.ratings);
                    tv.setText("The rating is " + ratings);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.actors);
                    tv.setText("The actors are " + actors);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.plot);
                    tv.setText(plot);
                    tv.setVisibility(View.VISIBLE);
                    iv.setImageBitmap(image);
                });


                Thread.sleep(10000);

            } catch (IOException | InterruptedException | JSONException mf) {
                mf.getMessage();
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        try{
            Thread.sleep(10000);
        }catch (Throwable t)
        {

        }
    }
}