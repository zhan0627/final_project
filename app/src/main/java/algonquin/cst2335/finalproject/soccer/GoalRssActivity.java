package algonquin.cst2335.finalproject.soccer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import algonquin.cst2335.finalproject.R;

public class GoalRssActivity extends AppCompatActivity {

    boolean isTablet = false;
    MessageListFragment rssFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent fromPrevious = getIntent();
        setContentView(R.layout.soccer_empty_layout);
        String override = fromPrevious.getStringExtra("override");

        isTablet = findViewById(R.id.detailsRoom) != null;
        rssFragment = new MessageListFragment(override);
        rssFragment.setHasOptionsMenu(true);
        FragmentManager fMgr = getSupportFragmentManager();
        FragmentTransaction tx = fMgr.beginTransaction();
        tx.add(R.id.fragmentRoom, rssFragment);
        tx.commit();

    }

    public void userClickedMessage(RssItem rssMessage, int position) {

        MessageDetailsFragment mdFragment = new MessageDetailsFragment(rssMessage, position);
        if(isTablet){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailsRoom, mdFragment).commit();

        }
        else{
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, mdFragment).commit();
        }

    }

    public void notifyMessageDeleted(RssItem choosenItem, int chosenPosition) {
        rssFragment.notifyMessageDeleted(choosenItem, chosenPosition);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){

            case R.id.id_help:

                AlertDialog dialog = new AlertDialog.Builder(GoalRssActivity.this)
                        .setTitle(getString(R.string.help))
                        .setMessage(getString(R.string.instruction))
                        .show();


        }
        return super.onOptionsItemSelected(item);
    }
}
