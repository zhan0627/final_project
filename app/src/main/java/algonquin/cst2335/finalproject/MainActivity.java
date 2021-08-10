package algonquin.cst2335.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TextView mytext = findViewById(R.id.textview);

        Button Button1 = findViewById(R.id.button1);
        Button MinYangBtn = findViewById(R.id.MinYang);
        Button Button3 = findViewById(R.id.button3);
        Button Button4 = findViewById(R.id.button4);


        MinYangBtn.setOnClickListener( view -> {
            startActivity(new Intent(MainActivity.this, MinYang.class));
        });

    }
}