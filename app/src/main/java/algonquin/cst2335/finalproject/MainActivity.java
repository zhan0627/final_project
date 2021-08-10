package algonquin.cst2335.finalproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.final_project.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView mytext = findViewById(R.id.textview);

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.MinYang);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);

  

    }
}