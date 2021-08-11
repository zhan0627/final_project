package algonquin.cst2335.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView mytext = findViewById(R.id.textview);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(v -> {
            Intent gotobutton1 = new Intent(MainActivity.this,MainRoute.class);
            startActivity(gotobutton1);
        });


        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);







        /*button2.setOnClickListener(click -> {});
        button3.setOnClickListener(click -> {});
        button4.setOnClickListener(click -> {});*/


    }
}