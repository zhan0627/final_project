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
    double currentTemp;
    double minTemp;
    double maxTemp;
    int humidity;
    String description;
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
                TextView tv = findViewById(R.id.temp);
                tv.setVisibility(View.INVISIBLE);

                tv = findViewById(R.id.minTemp);
                tv.setVisibility(View.INVISIBLE);

                tv = findViewById(R.id.maxTemp);
                tv.setVisibility(View.INVISIBLE);

                tv = findViewById(R.id.humidity);
                tv.setVisibility(View.INVISIBLE);

                tv = findViewById(R.id.description);
                tv.setVisibility(View.INVISIBLE);

                iv.setVisibility(View.INVISIBLE);
                cityField.setText("");
                break;

            case R.id.increase:
            case R.id.nav_increase:
                txtSize++;
                TextView text = findViewById(R.id.temp);
                text.setTextSize(txtSize);

                text = findViewById(R.id.minTemp);
                text.setTextSize(txtSize);

                text = findViewById(R.id.maxTemp);
                text.setTextSize(txtSize);

                text = findViewById(R.id.humidity);
                text.setTextSize(txtSize);

                text = findViewById(R.id.description);
                text.setTextSize(txtSize);
                iv.setVisibility(View.INVISIBLE);

                break;

            case R.id.decrease:
            case R.id.nav_decrease:
                txtSize = Float.max(txtSize-1, 5);
                TextView tV = findViewById(R.id.temp);
                tV.setTextSize(txtSize);

                tV = findViewById(R.id.minTemp);
                tV.setTextSize(txtSize);

                tV = findViewById(R.id.maxTemp);
                tV.setTextSize(txtSize);

                tV = findViewById(R.id.humidity);
                tV.setTextSize(txtSize);

                tV = findViewById(R.id.description);
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

        cityField = findViewById(R.id.cityTextField);
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


                String stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                        + URLEncoder.encode (cityName, "UTF-8")
                        + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";


                URL url = new URL(stringURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                String text = (new BufferedReader(
                        new InputStreamReader(in, StandardCharsets.UTF_8)))
                        .lines()
                        .collect(Collectors.joining("\n"));

                JSONObject theDocument = new JSONObject( text );
                JSONObject mainObj = theDocument.getJSONObject("main");

                currentTemp = mainObj.getDouble("temp");
                minTemp = mainObj.getDouble ("temp_min");
                maxTemp = mainObj.getDouble ("temp_max");
                humidity = mainObj.getInt("humidity");

                JSONArray weatherArray = theDocument.getJSONArray ( "weather" );
                JSONObject firstObj = weatherArray.getJSONObject(0);
                description = firstObj.getString("description");
                iconName = firstObj.getString("icon");

                URL imgUrl = new URL( "https://openweathermap.org/img/w/" + iconName + ".png" );
                HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());



                }
                iv = findViewById(R.id.icon);
                runOnUiThread( (  )  -> {
                    TextView tv = findViewById(R.id.temp);
                    tv.setText("The current Temperature is " + currentTemp);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.minTemp);
                    tv.setText("The min Temperature is " + minTemp);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.maxTemp);
                    tv.setText("The max Temperature is " + maxTemp);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.humidity);
                    tv.setText("The min Temperature is " + humidity + "%");
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.description);
                    tv.setText(description);
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