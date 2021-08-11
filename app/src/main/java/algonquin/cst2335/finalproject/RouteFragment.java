package algonquin.cst2335.finalproject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RouteFragment extends Fragment {

    private AppCompatActivity parentActivity;
    private TextView question;
    private Button answer1, answer2, answer3, answer4;
    private List<String> options;
    private RouteObject RoueObject;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int position = getArguments().getInt("POSITION");
        RoueObject = RouteInformation.RouteNameList.get(position);

        View result = inflater.inflate(R.layout.activity_route_fragment, container, false);

        //display question
        question = result.findViewById(R.id.question);
        answer1 = result.findViewById(R.id.answer1);
        answer2 = result.findViewById(R.id.answer2);
        answer3 = result.findViewById(R.id.answer3);
        answer4 = result.findViewById(R.id.answer4);

        question.setText(RoueObject.getQuestion());

        if ((RoueObject.getinCorrect().length + 1) <= 3) {
            answer4.setVisibility(View.GONE);
        }

        if ((RoueObject.getinCorrect().length + 1) <= 2) {
            answer3.setVisibility(View.GONE);
        }

        options = new ArrayList<>();
        options.add(RoueObject.getcorrect());
        // https://stackoverflow.com/a/6026854
        options.addAll(Arrays.asList(RoueObject.getinCorrect()));

        // https://www.geeksforgeeks.org/shuffle-or-randomize-a-list-in-java/
        Collections.shuffle(options);

        answer1.setText(options.get(0));
        answer2.setText(options.get(1));

        if (options.size() >= 3) {
            answer3.setText(options.get(2));
        }

        if (options.size() >= 4) {
            answer4.setText(options.get(3));
        }

        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionSelected(1);
            }
        });
        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionSelected(2);
            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionSelected(3);
            }
        });
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionSelected(4);
            }
        });

        return result;

    }

    /**
     * Processes any answer selected by the user and mark it as correct/incorrect
     * @param buttonNumber
     */
    private void optionSelected(int buttonNumber) {
        String userAnswer = options.get(buttonNumber - 1);
        if (userAnswer.equals(RouteObject.getcorrect())) {
            RouteObject.setState(RouteObject.STATE_RIGHT);
        } else {
            RouteObject.setState(RouteObject.STATE_WRONG);
        }
        parentActivity.setResult(Activity.RESULT_OK);
        parentActivity.finish();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity) context;
    }
}
