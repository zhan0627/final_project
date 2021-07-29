package algonquin.cst2335.finalproject;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;

public class SecondActivity extends AppCompatActivity {
    ImageView pict;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView line = findViewById(R.id.messageReceived);

        Intent fromPrevious = getIntent();
        String text = fromPrevious.getStringExtra("Some Info");
        String email = fromPrevious.getStringExtra("EmailAddress");

        //check if picture.png exists:
        String sandbox = getFilesDir().getPath();
        File file = new File( sandbox + "/Picture.png");
        if(file.exists())
        {
            Bitmap theImage = BitmapFactory.decodeFile(sandbox+"/Picture.png");
            pict = findViewById(R.id.imageView);
            pict.setImageBitmap( theImage );
        }

        Button loginBtn = findViewById(R.id.button);
        loginBtn.setOnClickListener( clk -> {
            et = findViewById(R.id.inputEditText);
            Intent nextPage = new Intent(SecondActivity.this, algonquin.cst2335.finalproject.ThirdActivity.class);


            nextPage.putExtra("EmailAddress", et.getText().toString());

            startActivityForResult(nextPage, 900);
        });

        Float fromMain = fromPrevious.getFloatExtra("MyFloat", 3.14f);
        Boolean t = fromPrevious.getBooleanExtra("IsTrue", false);

        TextView txt = findViewById(R.id.textView3);
        txt.setText(text);

        line.setText(email);

        Button btn = findViewById(R.id.button);
        Log.w("MainActivity", "In onCreate() - Loading Widgets" );

        btn.setOnClickListener( clk -> {
            Intent sendBack = new Intent ();
            sendBack.putExtra("Number", line.getText().toString());
            setResult(200, sendBack);

            finish();
        });

        Button callBtn = findViewById(R.id.button2);
        Log.w("MainActivity", "In onCreate() - Loading Widgets" );

        callBtn.setOnClickListener( clk -> {

            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + line.getText().toString()));
            //save line.getText().toString() to sharedpreferences
            SharedPreferences prefs = getSharedPreferences("MyCall", Context.MODE_PRIVATE);

            String called = getFilesDir().getPath();
            File files = new File( called +"/shared_prefs");
            if(file.exists())
            {
                prefs.getString("MyCall", line.getText().toString());
                String missCall = prefs.getString("MyCall", line.getText().toString());
                SharedPreferences.Editor  editor = prefs.edit();
                editor.putString("MyCall", missCall);
                editor.apply();
            }
            startActivityForResult(call, 1233);
        });

        Button picBtn = findViewById(R.id.button3);
        Log.w("MainActivity", "In onCreate() - Loading Widgets" );

        picBtn.setOnClickListener( clk -> {

            Intent pix = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult( pix, 5678 );
        });

        Button geoBtn = findViewById(R.id.button4);
        Log.w("MainActivity", "In onCreate() - Loading Widgets" );

        geoBtn.setOnClickListener( clk -> {

            Intent geo = new Intent (Intent.ACTION_WEB_SEARCH);
            geo.putExtra(SearchManager.QUERY,"Ottawa, Ontario" );
            startActivityForResult( geo, 6789 );
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String path = getFilesDir().getPath();
        if (requestCode == 5678)
        {
            if(resultCode == RESULT_OK) {
                Bitmap thumbnail = data.getParcelableExtra("data");
                FileOutputStream fOut = null;
                pict.setImageBitmap(thumbnail);

                try {
                    fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
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
}