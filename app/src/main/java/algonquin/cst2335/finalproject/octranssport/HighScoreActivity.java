package algonquin.cst2335.finalproject;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * This is HighScore Activity
 * The main reason of this class is getting data from database and load them here
 * Also has AlterDialog which is able to delete the data from database
 */

public class HighScoreActivity extends AppCompatActivity {

    private ListView listView;
    private MyListAdapter adapter;
    private List<HighScoreObject> highScoreList = new ArrayList<>();
    private RouteMyOpenner dbOpener;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        listView = findViewById(R.id.listView);
        adapter = new MyListAdapter();
        listView.setAdapter(adapter);

        dbOpener = new RouteMyOpenner(this);
        db = dbOpener.getWritableDatabase();
        loadDataFromDatabase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(HighScoreActivity.this)
                        .setTitle(R.string.delete_title)
                        .setMessage(R.string.delete_message)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                long id = highScoreList.get(position).getId();
                                db.delete(RouteMyOpenner.TABLE_NAME, RouteMyOpenner.COL_ID + " = ?", new String[]{Long.toString(id)});

                                highScoreList.remove(position);
                                adapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
            }
        });
    }

    /**
     * Load the high score data from the database into the list view
     */
    private void loadDataFromDatabase() {
        String[] columns = {RouteMyOpenner.COL_ID, RouteMyOpenner.COL_USERNAME, RouteMyOpenner.COL_DIFFICULTY_LEVEL, RouteMyOpenner.COL_CORRECT_ANSWERS, RouteMyOpenner.COL_TOTAL};
        Cursor results = db.query(false, RouteMyOpenner.TABLE_NAME, columns, null, null, null, null, null, null);
        highScoreList.clear();
        int idColIndex = results.getColumnIndex(RouteMyOpenner.COL_ID);
        int usernameColIndex = results.getColumnIndex(RouteMyOpenner.COL_USERNAME);
        int difficultyLevelColIndex = results.getColumnIndex(RouteMyOpenner.COL_DIFFICULTY_LEVEL);
        int correctAnswersColIndex = results.getColumnIndex(RouteMyOpenner.COL_CORRECT_ANSWERS);
        int totalColIndex = results.getColumnIndex(RouteMyOpenner.COL_TOTAL);
        // iterate over the results
        while (results.moveToNext()) {
            HighScoreObject h = new HighScoreObject(
                    results.getLong(idColIndex),
                    results.getString(usernameColIndex),
                    results.getString(difficultyLevelColIndex),
                    results.getInt(correctAnswersColIndex),
                    results.getInt(totalColIndex)
            );
            highScoreList.add(h);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Adapter for the list view
     */
    private class MyListAdapter extends BaseAdapter {

        /**
         * @return
         */
        public int getCount() {
            return highScoreList.size();
        }

        /**
         * @param position
         * @return
         */
        public Object getItem(int position) {

            return position;
        }

        /**
         * @param position
         * @return
         */
        public long getItemId(int position) {

            return position;
        }

        /**
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            /**
             * LayoutInflater is a class used to instantiate layout XML file into its corresponding
             * view objects which can be used in Java program
             */
            TextView textViewUserName, textViewDifficulty, textViewScore;
            View newView;
            LayoutInflater inflater = getLayoutInflater();
            HighScoreObject h = highScoreList.get(position);
            newView = inflater.inflate(R.layout.high_score_listview, parent, false);

            textViewUserName = newView.findViewById(R.id.textViewUserName);
            textViewDifficulty = newView.findViewById(R.id.textViewDifficulty);
            //textViewScore = newView.findViewById(R.id.textViewScore);

            textViewUserName.setText(h.getUserName());
            textViewDifficulty.setText(h.getDifficultyLevel());
            //textViewScore.setText(h.getCorrectAnswers() + "/" + h.getTotal());

            //Return the view
            return newView;
        }
    }
}
