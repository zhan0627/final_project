package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * Blank activity with just a frame layout to support the fragment
 */
public class RouteEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_empty);

        Bundle dataToPass = getIntent().getExtras();

        RouteFragment tg_dFragment = new RouteFragment();
        tg_dFragment.setArguments( dataToPass ); //pass data to the the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.tg_fragmentLocation, tg_dFragment)
                .commit();
    }


}
