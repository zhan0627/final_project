package algonquin.cst2335.finalproject.soccer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;



import algonquin.cst2335.finalproject.R;


public class MessageDetailsFragment extends Fragment {

    RssItem choosenItem;
    int chosenPosition;

    public MessageDetailsFragment(RssItem message, int position){
        choosenItem = message;
        chosenPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        View detailsView = inflater.inflate(R.layout.soccer_details_layout, container, false);

        TextView soccerTitleView = detailsView.findViewById(R.id.soccerTitleView);
        TextView description = detailsView.findViewById(R.id.description);
        TextView sendView = detailsView.findViewById(R.id.sendView);
        TextView timeView = detailsView.findViewById(R.id.timeView);
        ImageView imageView = detailsView.findViewById(R.id.imageDetailView);
        TextView idView = detailsView.findViewById(R.id.databaseIdView);

        Button saveView = detailsView.findViewById(R.id.saveButton);
        if (choosenItem.getSaved() == 1)
            saveView.setText(getString(R.string.delete));
        else
            saveView.setText(getString(R.string.save));

        soccerTitleView.setText(getString(R.string.TitlePage) + choosenItem.getTitle());
        description.setText(choosenItem.description);
        sendView.setText(getString(R.string.database1) + choosenItem.getSaved());
        timeView.setText( getString(R.string.time)+ choosenItem.getPubDate());
        idView.setText("Database id is: " + choosenItem.getId());



        Button webButton = detailsView.findViewById(R.id.webButton);
        webButton.setOnClickListener(closeClicked -> {
            if (choosenItem.getUrl() != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(choosenItem.getUrl()));
                startActivity(browserIntent);
            }
        });

        Button saveButton = detailsView.findViewById(R.id.saveButton);

        Button backButton = detailsView.findViewById(R.id.backButton);
        backButton.setOnClickListener(closeClicked -> {
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });


        saveButton.setOnClickListener(closeClicked -> {
            GoalRssActivity parentActivity = (GoalRssActivity) getContext();
            parentActivity.notifyMessageDeleted(choosenItem, chosenPosition);
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });


        return detailsView;
    }

    }
