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

public class Menu extends AppCompatActivity {

    private static String TAG = "MainActivity";
    EditText et;
    Bitmap mBitmap;
    private Object SecondActivity2;

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
            sendBack.putExtra("Number", line.getText().toString());
            setResult(200, sendBack);

            finish();
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
