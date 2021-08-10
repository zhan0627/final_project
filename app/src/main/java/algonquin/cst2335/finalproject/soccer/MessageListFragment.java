package algonquin.cst2335.finalproject.soccer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;

import algonquin.cst2335.finalproject.MainActivity;
import algonquin.cst2335.finalproject.R;

import static android.content.Context.MODE_PRIVATE;

public class MessageListFragment extends Fragment {

    MyRssAdapter adt;
    ArrayList<RssItem> messages = new ArrayList<>();
    RecyclerView rssList;
    SQLiteDatabase db;
    Boolean favourite = null;
    EditText rssFilter;

    Button send;
    Toolbar myToolbar;

    public MessageListFragment(String override) {
        if (override !=null) {
            if (override.equals("news")) {
                favourite = false;
            } else if (override.equals("favourites")) {
                favourite = true;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rssLayout = inflater.inflate(R.layout.soccer_rsslayout, container, false);
        //send = rssLayout.findViewById(R.id.sendbutton);
        //setContentView(R.layout.rsslayout);
        rssList = rssLayout.findViewById(R.id.myrecycler);
        rssList.setAdapter(new MyRssAdapter());

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        myToolbar = rssLayout.findViewById(R.id.toolbar);
        activity.setSupportActionBar(myToolbar);
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences shared = this.getActivity().getSharedPreferences(getString(R.string.SoccerPreferences), MODE_PRIVATE);
        if (favourite == null) {
            favourite = shared.getBoolean(getString(R.string.favourite), false);;
        }

        rssFilter = rssLayout.findViewById(R.id.rssFilter);
        send = rssLayout.findViewById(R.id.sendbutton);
        RecyclerView rssList = rssLayout.findViewById(R.id.myrecycler);
        setListText();
        Button receive = rssLayout.findViewById(R.id.receive);

        adt = new MyRssAdapter();
        rssList.setAdapter(adt);
        //rssList.setLayoutManager(new LinearLayoutManager(this));
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rssList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        dataChanged();
        send.setOnClickListener(click -> {
                    Fragment details =  getParentFragmentManager().findFragmentById(R.id.detailsRoom);
                    if (details != null)
                        getParentFragmentManager ().beginTransaction(). remove(details).commit();

                    favourite = !favourite;
                    if (messages.size() > 0 ) {
                        adt.clear();
                    }
                    //SharedPreferences sh = this.getActivity().getSharedPreferences("SoccerPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor myedit = shared.edit();
                    myedit.putBoolean(getString(R.string.favourite), favourite);
                    myedit.commit();

                    dataChanged();
                    setListText();
                    adt.notifyDataSetChanged();
                }
        );

        receive.setOnClickListener(click -> {
            //Context context = getApplicationContext();
            CharSequence text = getString(R.string.BackTo);
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getContext(), text, duration);
            toast.show();
            Intent nextPage = new Intent(getActivity(), MainActivity.class);
            startActivity( nextPage );


        });

        rssFilter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (event != null && event.getAction() == KeyEvent.ACTION_DOWN  && event.getKeyCode() == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                    dataChanged();
                    adt.notifyDataSetChanged();
                    handled = true;
                }
                return handled;
            }
        });
        return rssLayout;
    }

    private void setListText() {
        if (favourite) {
            myToolbar.setTitle("Favourites");
            send.setText(getString(R.string.List));
        }
        else {
            myToolbar.setTitle("Soccer news");
            send.setText(getString(R.string.Fav));
        }

    }


    public void dataChanged() {
/**
 *
 * Once the changes have been made,
 * we need to commit to apply those changes made,
 * otherwise, it will throw an error
 *
 */
        messages.clear();
        if (favourite) {
            MyOpenHelper opener = new MyOpenHelper(getContext());
            db = opener.getWritableDatabase();

            Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);

            while (results.moveToNext()) {
                RssItem favourite = new RssItem(results);
                if (favourite.checkFilter(String.valueOf(rssFilter.getText())))
                    messages.add(favourite);
            }
        } else {
            GoalCom goal = new GoalCom(getContext());

            goal.loadFeed("en");
            while (goal.ready == false) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            RssItem rss;
            for (HashMap<String, String> item : goal.goalList) {
                rss = new RssItem(item);
                if (rss.checkFilter(String.valueOf(rssFilter.getText())))
                    messages.add(rss);
            }
        }
        rssFilter.setText(null);
    }

    private class MyRowViews extends RecyclerView.ViewHolder {
        TextView messageText;
        ImageView thumb;
        int position = -1;
        int row =  getAdapterPosition();

        public MyRowViews(View itemView) {
            super(itemView);
            itemView.setOnClickListener(click -> {
                GoalRssActivity parentActivity = (GoalRssActivity)getContext();
                int position = getAdapterPosition();
                parentActivity.userClickedMessage(messages.get(position), position);
            });

            messageText = itemView.findViewById(R.id.message);
        }

        public void setPosition(int p) {
            position = p;
        }
    }

    private class MyRssAdapter extends RecyclerView.Adapter<MyRowViews> {

        @Override
        public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            int layoutID = R.layout.soccer_sent_message;
            if (viewType == 1)
                layoutID = R.layout.soccer_sent_message;
            View loadedRow = inflater.inflate(layoutID, parent, false);
            return new MyRowViews(loadedRow);
        }

        @Override
        public void onBindViewHolder(MyRowViews holder, int position) {
            holder.messageText.setText(messages.get(position).getTitle());

        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        @Override
        public int getItemViewType(int position) {
            RssItem thisRow = messages.get(position);
            return thisRow.getSaved();
        }
        public void clear()
        {
            adt.notifyItemRangeRemoved(0, getItemCount());
            messages.clear();
        }


        ;
    }

    public void notifyMessageDeleted (RssItem choosenItem, int chosenPosition){
        if (choosenItem.getSaved() == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setMessage(getString(R.string.DeleteM) + choosenItem.getTitle())
                    .setTitle(getString(R.string.Qe))
                    .setNegativeButton(getString(R.string.No), (dialog, cl) -> {
                    })
                    .setPositiveButton(getString(R.string.Y), (dialog, cl) -> {
                        MyOpenHelper opener = new MyOpenHelper(getContext());
                        SQLiteDatabase db = opener.getWritableDatabase();
                        db.execSQL("delete from " + MyOpenHelper.TABLE_NAME + " where _id = " + choosenItem.getId());

                        RssItem removedMsg = messages.get(chosenPosition);

                        messages.remove(chosenPosition);
                        adt.notifyItemRemoved(chosenPosition);

                        Snackbar.make(send,getString(R.string.DE) + chosenPosition, Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.Undo), clk -> {
                                    messages.add(choosenItem);
                                    adt.notifyItemInserted(chosenPosition);
                                })
                                .show();
                    });
            builder.create().show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setMessage(getString(R.string.SF)+ choosenItem.getTitle())
                    .setTitle(getString(R.string.Qe))
                    .setNegativeButton(getString(R.string.No), (dialog, cl) -> {
                    })
                    .setPositiveButton(getString(R.string.Y), (dialog, cl) -> {
                        choosenItem.saveItem(getActivity());
                    });
            builder.create().show();
        }
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.soccer_main_activity_actions, menu);
    }

}
