package algonquin.cst2335.finalproject;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class MinYang  extends AppCompatActivity {
    ChargeFragment chargeFr;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle abt;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charge_fragment);

        /**
         * Toolbar
         */
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /**
         * Navigation drawer
         */
        drawerLayout = findViewById(R.id.drawer_layout);
        abt = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(abt);
        abt.syncState();
        navigationView  = findViewById(R.id.charge_popout_menu);
        navigationView.setNavigationItemSelectedListener((item)->{
            onOptionsItemSelected(item);
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });

        /**
         * fragment  list
         */
        chargeFr = new ChargeFragment();
        FragmentManager fram = getSupportFragmentManager();
        FragmentTransaction ft = fram.beginTransaction();
        ft.add(R.id.fragment, chargeFr);
        ft.commit();

    }

    /**
     * fragment detail
     * @param chargeInfo
     * @param position
     */
    public void userClickedInfo(ChargeFragment.ChargeInfo chargeInfo, int position) {
        ChargeDetailsFragment sdf = new ChargeDetailsFragment(chargeInfo, position);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, sdf).commit();
    }

    /**
     * @param chosenInfo
     * @param chosenPosition
     */
    public void notifyInfoDeleted(ChargeFragment.ChargeInfo chosenInfo, int chosenPosition) {
        chargeFr.notifyInfoDeleted(chosenInfo, chosenPosition);
    }


    /**
     *  menu/toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_min_yang, menu);
        return true;
    }

    /**
     * select an option of menu/toolbar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please enter latitude and longitude from -120 to 120, and search." +
                        " List display below. Click details." +
                        "You can add  your choose.")
                        .setTitle("Help Manual: ")
                        .create().show();
                break;
            case R.id.finder:
                chargeFr.notifyConvertToFavorite();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

