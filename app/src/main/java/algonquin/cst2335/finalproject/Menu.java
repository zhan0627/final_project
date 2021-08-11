package algonquin.cst2335.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {

    private static String TAG = "MainActivity";
    EditText et;
    Bitmap mBitmap;
    private Object SecondActivity2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginBtn = findViewById(R.id.loginButton);
        Log.w("MainActivity", "In onCreate() - Loading Widgets" );

        loginBtn.setOnClickListener( clk -> {
            et = findViewById(R.id.inputEditText);
            Intent nextPage = new Intent(Menu.this, movie_app_main_menu.class);

            nextPage.putExtra("EmailAddress", et.getText().toString());
            nextPage.putExtra("SomeInfo", "Welcome!");
            nextPage.putExtra("MyFloat", 3.14f);

            startActivityForResult(nextPage, 900);
        });


    }

}
