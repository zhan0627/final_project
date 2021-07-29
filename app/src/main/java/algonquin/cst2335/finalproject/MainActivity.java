package algonquin.cst2335.finalproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    EditText et;
    Bitmap mBitmap;
    private Object SecondActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginBtn = findViewById(R.id.loginButton);
        Log.w("MainActivity", "In onCreate() - Loading Widgets" );

        loginBtn.setOnClickListener( clk -> {
            et = findViewById(R.id.inputEditText);
            Intent nextPage = new Intent(MainActivity.this, algonquin.cst2335.finalproject.SecondActivity.class);


            nextPage.putExtra("EmailAddress", et.getText().toString());

            startActivityForResult(nextPage, 900);
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 900)
        {
            if(resultCode == RESULT_OK) {
                Bitmap thumbnail = data.getParcelableExtra("data");
                FileOutputStream fOut = null;
                try {
                    fOut = openFileOutput( "Picture.png", Context.MODE_PRIVATE);
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }



            Log.w( "MainActivity", "Coming back from previous" );
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );

        String TAG = "MainActivity";
        Log.d( TAG, "The application is now visible on screen");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );

        String TAG = "MainActivity";
        Log.d( TAG, "The application is no longer visible.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );

        String TAG = "MainActivity";
        Log.d( TAG, "Any memory used by the application is freed.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );

        String TAG = "MainActivity";
        Log.d( TAG, "he application is restarting.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );

        String TAG = "MainActivity";
        Log.d( TAG, "The application is now responding on user input");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );

        String TAG = "MainActivity";
        Log.d( TAG, "The application no longer responds to user input");
    }
}