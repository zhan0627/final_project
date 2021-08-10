package algonquin.cst2335.finalproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class movie_app_main_menu extends AppCompatActivity {
    ImageView iv ;
    /**Describes the result the variable movie holds */
    private EditText movie;
    Bitmap image = null;
    /**This represents the result of the complexity*/
    private TextView textView;
    /**The Button the user clicks to change user interface*/
    private Button forecastBtn ;
    /**This represents result of the title of the movie*/
    private String title;
    /**This represents the results of how long the runtime is*/
    private String runtime;
    /**This represents result of the ratings of the movie*/
    private String ratings;
    /**This represents the result of the actors in the movie*/
    private String actors;
    /**This represents the result of the plot in the movie */
    private String plot;
    /**This represents the result of the poster of the movie*/
    private String iconName;

    float txtSize = 14.0f;
    boolean isVisible = true;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            //  case R.id.views:
            case R.id.nav_views:
                isVisible = ! isVisible;
                // forecastBtn.setVisibility(isVisible?View.VISIBLE:View.INVISIBLE);
                TextView tv = findViewById(R.id.movie_title);
                tv.setVisibility(View.INVISIBLE);

                tv = findViewById(R.id.runtime);
                tv.setVisibility(View.INVISIBLE);

                tv = findViewById(R.id.ratings);
                tv.setVisibility(View.INVISIBLE);

                tv = findViewById(R.id.actors);
                tv.setVisibility(View.INVISIBLE);

                tv = findViewById(R.id.plot);
                tv.setVisibility(View.INVISIBLE);

                iv.setVisibility(View.INVISIBLE);
                movie.setText("");
                break;

            // case R.id.increase:
            case R.id.nav_increase:
                txtSize++;
                TextView text = findViewById(R.id.movie_title);
                text.setTextSize(txtSize);

                text = findViewById(R.id.runtime);
                text.setTextSize(txtSize);

                text = findViewById(R.id.ratings);
                text.setTextSize(txtSize);

                text = findViewById(R.id.actors);
                text.setTextSize(txtSize);

                text = findViewById(R.id.plot);
                text.setTextSize(txtSize);
                iv.setVisibility(View.INVISIBLE);

                break;

            //  case R.id.decrease:
            case R.id.nav_decrease:
                txtSize = Float.max(txtSize-1, 5);
                TextView tV = findViewById(R.id.movie_title);
                tV.setTextSize(txtSize);

                tV = findViewById(R.id.runtime);
                tV.setTextSize(txtSize);

                tV = findViewById(R.id.ratings);
                tV.setTextSize(txtSize);

                tV = findViewById(R.id.actors);
                tV.setTextSize(txtSize);

                tV = findViewById(R.id.plot);
                tV.setTextSize(txtSize);
                iv.setVisibility(View.INVISIBLE);

                movie.setTextSize(txtSize);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        movie = findViewById(R.id.movieTextField);
        forecastBtn = findViewById(R.id.forecastButton);

        NavigationView popout_menu = findViewById(R.id.popout_menu);
        popout_menu.setNavigationItemSelectedListener((item) ->
        {
            onOptionsItemSelected(item);
            drawer.closeDrawer(GravityCompat.START);
            return false;

        });

        forecastBtn.setOnClickListener(click -> {
            String cityName = movie.getText().toString();
            myToolbar.getMenu().add(1,5,10,cityName).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            runForecast(cityName);

            Context context = getApplicationContext();
            CharSequence txt = "Your about to watch a movie! ";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, txt, duration);

            toast.show();


        });




    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void runForecast(String cityName)
    {
        forecastBtn.setOnClickListener((click)-> {
            String movietime = movie.getText().toString();

            AlertDialog dialog = new AlertDialog.Builder (movie_app_main_menu.this)
                    .setTitle ("Box Office Hit")
                    .setMessage("173 Million views:" + movietime )
                    .setView (new ProgressBar(movie_app_main_menu.this))
                    .show();

        });

        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute(() -> {

            try {


                String stringURL = "https://www.omdbapi.com/?apikey=6c9862c2&r=xml&t="
                        + URLEncoder.encode (cityName, "UTF-8");


                URL url = new URL(stringURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( in  , "UTF-8");
/*
                URL imgUrl = new URL( "https://m.media-amazon.com/images/M/" + iconName + ".jpg" );
                HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());
*/
                while (xpp.next() != XmlPullParser.END_DOCUMENT)
                {
                    switch(xpp.getEventType())
                    {
                        case XmlPullParser.START_TAG:
                            if (xpp.getName().equals("Movie"))
                            {
                                title = xpp.getAttributeValue(null, "title");
                                runtime = xpp.getAttributeValue(null, "runtime");
                                actors = xpp.getAttributeValue(null, "actors");
                                plot = xpp.getAttributeValue(null, "plot");
                                ratings = xpp.getAttributeValue(null, "rated");
                            }

                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            break;
                    }
                }




                iv = findViewById(R.id.icon);
                runOnUiThread( (  )  -> {
                    TextView tv = findViewById(R.id.movie_title);
                    tv.setText("The movie title is " + title);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.runtime);
                    tv.setText("The min runtime is " + runtime);
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

            } catch (IOException | InterruptedException | XmlPullParserException mf) {
                mf.getMessage();
            }

        });

        Intent fromPrevious = getIntent();
        String movieText = fromPrevious.getStringExtra("back");

        movie.setText(movieText);

        Button loginBtn = findViewById(R.id.backbutton);
        Log.w("MainActivity", "In onCreate() - Loading Widgets" );

        loginBtn.setOnClickListener( clk -> {

            Intent nextPage = new Intent(ThirdActivity.this, algonquin.cst2335.finalproject.ChatRoom.class);

            nextPage.putExtra("textEmailAddress", movie.getText().toString());

            startActivityForResult(nextPage, 900);
        });

        Button btn = findViewById(R.id.continuebutton);
        Log.w("MainActivity", "In onCreate() - Loading Widgets" );

        TextView line = findViewById(R.id.editTextTextEmailAddress);
        btn.setOnClickListener( clk -> {
            Intent sendBack = new Intent ();
            sendBack.putExtra("textEmailAddress", line.getText().toString());
            setResult(200, sendBack);

            finish();
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