package algonquin.cst2335.finalproject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainMenu extends AppCompatActivity {

    RecyclerView chatList;
    EditText messageTyped;
    private LinearLayoutManager LayoutManager;
    Button send;
    Button received;
    ImageView image;
    int row;
    ChatAdapter adapter;
    ArrayList<ChatMessage> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chatlayout);

        messageTyped = findViewById(R.id.editMessage);
        image = findViewById(R.id.imageView);
        send = findViewById(R.id.sendButton);
        received = findViewById(R.id.receiveButton);
        chatList = findViewById(R.id.myrecycler);

        //    Intent nextPage = new Intent(ChatRoom.this, SecondActivity2.class);
        //   nextPage.putExtra("Message", messageTyped.getText().toString());

        adapter = new ChatAdapter();
        chatList.setAdapter(adapter);
        chatList.setLayoutManager(new LinearLayoutManager(this));
        //adapter.notifyItemInserted(messages.size() - 1);

        //   LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //  chatList.setLayoutManager(layoutManager);


        send.setOnClickListener(click -> {

            ChatMessage nextMessage = new ChatMessage(image, messageTyped.getText().toString(), 1, new Date());
            messages.add(nextMessage);
            messageTyped.setText("");
            adapter.notifyItemInserted(messages.size() - 1);
        });

        received.setOnClickListener(click -> {
            ChatMessage nextMessage = new ChatMessage(image, messageTyped.getText().toString(), 2, new Date());
            messages.add(nextMessage);
            messageTyped.setText("");
            adapter.notifyItemInserted(messages.size() - 1);

        });

    }

    class ChatAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemViewType(int position) {
            return messages.get(position).sendOrReceive;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = getLayoutInflater();
            int layoutID;
            if (viewType == 1)
                layoutID = R.layout.sent_message;
            else
                layoutID = R.layout.receive_message;

            View loadedRow = inflater.inflate(layoutID, parent, false);
            return new MyRowViews(loadedRow);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            MyRowViews thisRow = (MyRowViews) holder;
            thisRow.rowMessage.setText(messages.get(position).getMessage());
            //show the time:
            thisRow.timeText.setText(messages.get(position).getTimeSent());
            //thisRow.image.setText(messages.get(position).getPix());
            thisRow.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

    }

    private class MyRowViews extends RecyclerView.ViewHolder{

        TextView rowMessage;
        TextView timeText;
        ImageView pix;

        public MyRowViews (View itemView)
        {
            super (itemView);
            int position = -1;

            itemView.setOnClickListener(click -> {

                AlertDialog.Builder builder = new AlertDialog.Builder( MainMenu.this );
                builder.setMessage("Do you want to watch a movie: " + rowMessage.getText())
                        .setTitle("Question: ")
                        .setPositiveButton("Yes", (dialog, cl)-> {

                            row = getAbsoluteAdapterPosition();
                            ChatMessage removedMessage = messages.get(row);
                            messages.remove(row);
                            adapter.notifyItemRemoved(row);
                            Snackbar.make(rowMessage, "You deleted movie #: " + row, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk -> {


                                        messages.add(row,removedMessage);
                                        adapter.notifyItemInserted(row);
                                    })
                                    .show();
                        })

                        .setNegativeButton("No", (dialog, cl)-> {})
                        .create().show();



            });
            rowMessage = itemView.findViewById(R.id.messageReceived);
            timeText = itemView.findViewById(R.id.timeText);
            image = itemView.findViewById(R.id.imageView);
        }

        public void setPosition(int position) {
        }
    }

    class ChatMessage
    {
        public String message;
        int sendOrReceive;
        String timeSent;
        ImageView image;


        public ChatMessage (String s) {message = s; }

        public ChatMessage(ImageView image, String message, int sendOrReceive, Date timeSent) {
            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());


            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = sdf.format(timeSent);
            this.image = image;
        }

        public String getMessage() {
            return message;
        }

        public int getSendOrReceive() {
            return sendOrReceive;
        }

        public String getTimeSent() {
            return timeSent;
        }

        public ImageView getPix() {
            return image;
        }
    }


}
