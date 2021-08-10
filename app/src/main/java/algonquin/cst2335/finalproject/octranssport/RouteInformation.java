package algonquin.cst2335.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * The Route Information for the Oc called after the Start Screen Oc
 * Holds static final variables as a column name for the DB
 * Load questions from the xml
 * Arraylist to store all questions loaded
 */

public class RouteInformation extends AppCompatActivity {

    SharedPreferences pref_name;
    int QuestionType;
    int Difficulty;
    String DifficultyStr;
    ProgressBar loadPbar;
    Boolean showResultFlag = false;
    MyListAdapter adapter;
    float correctAnswers = 0;
    ListView myList;
    Button finish;
    //private ArrayList<RouteInformation> RouteNameList = new ArrayList<>();


    public static ArrayList<RouteObject> RouteNameList = new ArrayList<>();
    //public static ArrayList<TriviaUsersObject> usersList = new ArrayList<>();

    public static final String ITEM_SELECTED = "QuestionTxt";
    public static final String CorrectAnswer = "CorrectAnswer";
    public static final String IncorrectAnswer = "IncorrectAnswer";

    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final String userAnser = "USERANSWER";

    /**
     * find the ProgressBar and make it visible
     * Store variable to show if we are running on Phone
     * find the show result button and implement it's code
     * Build the URL with the user inputs and call the RouteQuery class to
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        pref_name = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        //setup the listView
        myList = findViewById(R.id.ListView);

        Intent fromMain = getIntent();
        //Button showResultBtn = findViewById(R.id.buttonFinish);
        loadPbar = findViewById(R.id.loadPbar);

        adapter = new MyListAdapter();
        myList.setAdapter(adapter);

        //defining list items in a new array list
        String UserName = fromMain.getStringExtra("userName");


        myList.setOnItemLongClickListener((parent, view, position, id) -> {
            RouteObject data = RouteNameList.get(position);
            Bundle dataToPass = new Bundle();

            dataToPass.putInt(ITEM_ID, RouteNameList.get(position).getId());
            dataToPass.putString(ITEM_SELECTED, RouteNameList.get(position).getQuestion());

            dataToPass.putString(CorrectAnswer, RouteNameList.get(position).getcorrect());

            dataToPass.putStringArray(IncorrectAnswer, RouteNameList.get(position).getinCorrect());

            String message = getResources().getString(R.string.QuestionTexts) + RouteNameList.get(position).getId();
            boolean sendSide = false;

            return sendSide;
        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RouteObject triviaObject = RouteNameList.get(position);
                if (triviaObject.getState() != RouteObject.STATE_UNANSWERED) return;

                Bundle dataToPass = new Bundle();
                dataToPass.putInt("POSITION", position);

                Intent i = new Intent(RouteInformation.this, RouteEmptyActivity.class);
                i.putExtras(dataToPass);
                startActivityForResult(i, 78);


            }
        });


        finish = findViewById(R.id.buttonFinish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add high-score into the database
                ContentValues cv = new ContentValues();
                cv.put(RouteMyOpenner.COL_USERNAME, pref_name.getString("userName", "UNKNOWN USER"));
                if (pref_name.getInt("Difficulty", -1) == 0)
                    cv.put(RouteMyOpenner.COL_DIFFICULTY_LEVEL, "easy");
                else if (pref_name.getInt("Difficulty", -1) == 1)
                    cv.put(RouteMyOpenner.COL_DIFFICULTY_LEVEL, "medium");
                else if (pref_name.getInt("Difficulty", -1) == 2)
                    cv.put(RouteMyOpenner.COL_DIFFICULTY_LEVEL, "hard");

                int correctAnswerCount = 0;
                for (RouteObject triviaObject : RouteNameList) {
                    if (triviaObject.getState() == RouteObject.STATE_RIGHT) {
                        correctAnswerCount++;
                    }
                }

               //cv.put(RouteMyOpenner.COL_CORRECT_ANSWERS, correctAnswerCount);
                cv.put(RouteMyOpenner.COL_TOTAL, RouteNameList.size());

                SQLiteDatabase db = (new RouteMyOpenner(RouteInformation.this)).getWritableDatabase();
                db.insert(RouteMyOpenner.TABLE_NAME, null, cv);

                Intent i = new Intent(RouteInformation.this, HighScoreActivity.class);
                startActivity(i);
            }
        });

        StringBuilder sb = new StringBuilder("https://opentdb.com/api.php?amount=");
        if (pref_name.contains("routeCount")) {
            sb.append(pref_name.getInt("routeCount", -1));
        }
        sb.append("&type=");
        if (pref_name.contains("RouteType")) {
            if (pref_name.getInt("RouteType", -1) == 0) sb.append("multiple");
            else sb.append("boolean");
        }
        sb.append("&difficulty=");
        if (pref_name.contains("Difficulty")) {
            if (pref_name.getInt("Difficulty", -1) == 0) sb.append("easy");
            else if (pref_name.getInt("Difficulty", -1) == 1) sb.append("medium");
            else if (pref_name.getInt("Difficulty", -1) == 2) sb.append("hard");
        }

        RouteNameList.clear();
        TriviaQuery triviaQuery = new TriviaQuery();
        triviaQuery.execute(sb.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 78 && resultCode == RESULT_OK) {
            adapter.notifyDataSetChanged();

            if (allQuestionsAnswered()) {
                finish.setVisibility(View.VISIBLE);
            }

        }
    }

    private boolean allQuestionsAnswered() {
        for (RouteObject triviaObject : RouteNameList) {
            if (triviaObject.getState() == RouteObject.STATE_UNANSWERED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();

    }

    private class MyListAdapter extends BaseAdapter {

        /**
         * @return
         */
        public int getCount() {

            return RouteInformation.RouteNameList.size();
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
        @RequiresApi(api = Build.VERSION_CODES.M)
        public View getView(int position, View convertView, ViewGroup parent) {
            //LayoutInflater is a class used to instantiate layout XML file into its corresponding
            // view objects which can be used in Java program
            TextView ListViewLabel;
            ImageView answered;
            View newView;
            LayoutInflater inflater = getLayoutInflater();
            RouteObject msg = RouteInformation.RouteNameList.get(position);
            newView = inflater.inflate(R.layout.activity_route_listview, parent, false);

            ListViewLabel = newView.findViewById(R.id.TriviaListViewLabel);
            answered = newView.findViewById(R.id.TriviaAnsewring);


            ListViewLabel.setText(getString(R.string.question_label) + " " + (position + 1));

            switch (msg.getState()) {
                case RouteObject.STATE_UNANSWERED:
                    answered.setBackgroundColor(getColor(R.color.colorstateunanswered));
                    break;
                case RouteObject.STATE_RIGHT:
                    answered.setBackgroundColor(getColor(R.color.colorstateright));
                    break;
                case RouteObject.STATE_WRONG:
                    answered.setBackgroundColor(getColor(R.color.colorstatewrong));
                    break;
            }


            return newView;
        }
    }

    /**
     * class RouteInformation query inheriting from AsyncTask
     */
    class TriviaQuery extends AsyncTask<String, Integer, String> {

        /**
         *
         *
         * @param args
         * @return
         */
        @Override
        protected String doInBackground(String... args) {
            int id = 0;
            int progress = 0;
            try {
                URL url = new URL(args[0]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //waiting for data
                InputStream response = urlConnection.getInputStream();
                //building the string response
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                String result = sb.toString();

                Log.d("JSON", "doInBackground() returned: " + result);

                JSONObject joResult = new JSONObject(result);
                JSONArray jaResults = joResult.getJSONArray("results");

                for (int i = 0; i < jaResults.length(); i++) {
                    JSONObject anObject = jaResults.getJSONObject(i);
                    String category = anObject.getString("category");
                    String qu = Html.fromHtml(anObject.getString("question")).toString();
                    String correct = Html.fromHtml(anObject.getString("correct_answer")).toString();
                    JSONArray inCorrect = anObject.getJSONArray("incorrect_answers");
                    String[] inAnswer = new String[inCorrect.length()];
                    for (int j = 0; j < inCorrect.length(); j++) {
                        inAnswer[j] = Html.fromHtml(inCorrect.getString(j)).toString();
                    }
                    RouteObject question = new RouteObject(id, correct, qu, category, inAnswer);
                    RouteNameList.add(question);
                    publishProgress(((i + 1) / jaResults.length()) * 100);
                    id++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "DONE";
        }


        /**
         * @param progress
         */
        @Override
        public void onProgressUpdate(Integer... progress) {
            loadPbar.setProgress(progress[0]);
        }


        /**
         * @param work
         */
        public void onPostExecute(String work) {

            loadPbar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();

            Snackbar.make(myList, R.string.snackbar_message, Snackbar.LENGTH_LONG).show();

        }
    }
}
