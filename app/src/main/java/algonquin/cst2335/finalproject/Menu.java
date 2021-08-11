package algonquin.cst2335.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;

public class Menu extends AppCompatActivity {

    private static String TAG = "MainActivity";
    EditText et;
    Bitmap mBitmap;
    private Object SecondActivity;
    ImageView pict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Button searchBtn = findViewById(R.id.button5);
        Log.w("Menu", "In onCreate() - Loading Widgets" );

        searchBtn.setOnClickListener( clk -> {
            et = findViewById(R.id.inputEditText);
            Intent nextPage = new Intent(Menu.this, movie_app_main_menu.class);

            nextPage.putExtra("search", et.getText().toString());

            startActivityForResult(nextPage, 800);
        });

        Intent fromPrevious = getIntent();
        EditText line = findViewById(R.id.editTextPhone);
        String email = fromPrevious.getStringExtra("search");
        line.setText(email);

        Button btn = findViewById(R.id.button5);
        Log.w("Menu", "In onCreate() - Loading Widgets" );

        btn.setOnClickListener( clk -> {
            Intent sendBack = new Intent ();
            sendBack.putExtra("search", line.getText().toString());
            setResult(200, sendBack);

            finish();
        });

        String sandbox = getFilesDir().getPath();
        File file = new File( sandbox + "/Picture.png");
        if(file.exists())
        {
            Bitmap theImage = BitmapFactory.decodeFile(sandbox+"/Picture.png");
            pict = findViewById(R.id.imageView);
            pict.setImageBitmap( theImage );
        }

        Button callBtn = findViewById(R.id.button2);
        Log.w("MainActivity", "In onCreate() - Loading Widgets" );

        callBtn.setOnClickListener( clk -> {

            Intent movie = new Intent(Intent.ACTION_WEB_SEARCH);
            movie.setData(Uri.parse("tel:" + line.getText().toString()));
            //save line.getText().toString() to sharedpreferences
            SharedPreferences prefs = getSharedPreferences("MySearch", Context.MODE_PRIVATE);

            String called = getFilesDir().getPath();
            File files = new File( called +"/shared_prefs");
            if(file.exists())
            {
                prefs.getString("MySearch", line.getText().toString());
                String mySearch = prefs.getString("MySearch", line.getText().toString());
                SharedPreferences.Editor  editor = prefs.edit();
                editor.putString("MySearch", mySearch);
                editor.apply();
            }
            startActivityForResult(movie, 1233);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 800) {
            if (resultCode == RESULT_OK) {
                Bitmap thumbnail = data.getParcelableExtra("data");
                FileOutputStream fOut = null;
                try {
                    fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }
    }
}
