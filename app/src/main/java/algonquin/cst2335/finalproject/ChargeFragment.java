package algonquin.cst2335.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ChargeFragment extends Fragment {
    EditText EditText_lat;
    EditText EditText_long;
    Button btn_search;
    RecyclerView rev_charge;
    ArrayList<ChargeInfo> infos = new ArrayList<>();
    ArrayList<ChargeFragment.ChargeInfo> infos_finder = new ArrayList<>();
    ChargeAdapter adapter;
    ChargeAdapter adapter_finder;
    //    OpenHelper opener;
    SQLiteDatabase db;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * views in activity_min_yang.xml
         */
        View chargeLayout = inflater.inflate(R.layout.activity_min_yang, container, false);
        EditText_lat = chargeLayout.findViewById(R.id.EditText_lat);
        EditText_long = chargeLayout.findViewById(R.id.EditText_long);
        btn_search = chargeLayout.findViewById(R.id.btn_search);
        rev_charge = chargeLayout.findViewById(R.id.rev_charge);

        /**
         *  put edit texts into file system
         */
        SharedPreferences sp = getContext().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String sp_lat = sp.getString("Latitude","");
        String sp_long = sp.getString("Longitude","");
        EditText_lat.setText(sp_lat);
        EditText_long.setText(sp_long);

        /**
         * database to hold finder
         */

        ChargeOpenHelper opener = new ChargeOpenHelper(getContext());
        db = opener.getWritableDatabase();

        /**
         * put infos into recycler view
         */
        adapter = new ChargeAdapter(infos);
        adapter_finder = new ChargeAdapter(infos_finder);
        rev_charge.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        /**
         * click search button for charging station infos
         */
        btn_search.setOnClickListener(click->{
            rev_charge.setAdapter(adapter);
            Executor newThread = Executors.newSingleThreadExecutor();
            /**
             * searched edit texts
             */
            String latitude = EditText_lat.getText().toString();
            String longitude = EditText_long.getText().toString();
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Latitude", latitude);
            editor.putString("Longitude", longitude);
            editor.apply();

            if (rev_charge.getChildCount() > 0 ) {
                rev_charge.removeAllViews();
                adapter.notifyDataSetChanged();
            }
            /**
             * new thread
             */
            newThread.execute(()->{
                try {
                    /**
                     * clear infos list
                     */
                    infos.clear();
                    /**
                     * get  file and  data
                     */
                    String stringURL = "https://api.openchargemap.io/v3/poi/?countrycode=CA&latitude="+latitude+"&longitude="+longitude+"&maxresults=10&key=123";
                    URL url = new URL(stringURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    String text = (new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8)))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    JSONArray jsonArray = new JSONArray(text);
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject st = jsonArray.getJSONObject(i);
                        JSONObject addressInfo = st.getJSONObject("AddressInfo");
                        String info_title = addressInfo.getString("Title");
                        String info_telphone = addressInfo.getString("ContactTelephone");
                        String info_latitude = addressInfo.getString("Latitude");
                        String info_longitude = addressInfo.getString("Longitude");

                        ChargeInfo si = new ChargeInfo(info_title, info_telphone,info_latitude, info_longitude);

//
                        infos.add(si);
                    }
                    /**
                     * insert  data
                     */
                    MinYang parentActivity = (MinYang)getContext();
                    parentActivity.runOnUiThread(()->{
                        adapter.notifyItemInserted(infos.size() - 1);
                    });
                } catch (IOException | JSONException e) {
                    Log.e("Connection error: ", e.getMessage());
                }
            });

            EditText_lat.setText("");
            EditText_long.setText("");
        });
        return chargeLayout;
    }
    public void notifyConvertToFavorite() {
        infos_finder.clear();
        Cursor results = db.rawQuery("select * from " + ChargeOpenHelper.TABLE_NAME + ";", null);
        int _idCol = results.getColumnIndex("_id");
        int titleCol = results.getColumnIndex(ChargeOpenHelper.col_title);
        int latitudeCol = results.getColumnIndex(ChargeOpenHelper.col_latitude);
        int longitudeCol = results.getColumnIndex(ChargeOpenHelper.col_longitude);
        int telphoneCol = results.getColumnIndex(ChargeOpenHelper.col_telphone);
        while(results.moveToNext()) {
            long id = results.getInt(_idCol);
            String results_title = results.getString(titleCol);
            String results_latitude = results.getString(latitudeCol);
            String results_longitude = results.getString(longitudeCol);
            String results_telphone = results.getString(telphoneCol);
            infos_finder.add(new ChargeInfo(id,results_title, results_latitude,results_longitude, results_telphone));
        }

        rev_charge.setAdapter(adapter_finder);

    }
    /**
     * @param chosenInfo
     * @param chosenPosition
     */
    public void notifyInfoDeleted(ChargeInfo chosenInfo, int chosenPosition) {
        /**
         * alert dialog to delete or cancel delete
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to delete info #" + chosenInfo.getTitle())
                .setTitle("Question: ")
                .setNegativeButton("No", (dialog, click)->{
                    //position = getAbsoluteAdapterPosition();
                    Toast.makeText(getContext(), "You did not delete info #" + chosenPosition, Toast.LENGTH_LONG).show();
                })
                .setPositiveButton("Yes", (dialog, click)->{
                    //position = getAbsoluteAdapterPosition();
                    infos_finder.remove(chosenPosition);
                    adapter_finder.notifyItemRemoved(chosenPosition);
                    db.delete(ChargeOpenHelper.TABLE_NAME, "_id=?", new String[] {Long.toString(chosenInfo.getId())});

                    /**
                     * snack bar
                     */
                    Snackbar.make(btn_search, "You deleted info #" + chosenPosition, Snackbar.LENGTH_LONG)
                            .setAction("Undo", click2->{
                                infos_finder.add(chosenPosition, chosenInfo);

                                ContentValues newRow = new ContentValues();
                                newRow.put("_id", chosenInfo.getId());
                                newRow.put(ChargeOpenHelper.col_title, chosenInfo.getTitle());
                                newRow.put(ChargeOpenHelper.col_latitude, chosenInfo.getLatitude());
                                newRow.put(ChargeOpenHelper.col_longitude, chosenInfo.getLongitude());
                                newRow.put(ChargeOpenHelper.col_telphone, chosenInfo.getTelphone());
                                db.insert(ChargeOpenHelper.TABLE_NAME, null, newRow);

                                adapter_finder.notifyItemInserted(chosenPosition);
                            })
                            .show();
                })
                .create().show();
    }



    /**
     * rows in recycler view
     */
    private class RowView extends RecyclerView.ViewHolder {

        TextView info_title;

        int position = -1;

        public RowView(View itemView, List<ChargeInfo> infos) {
            super(itemView);

            itemView.setOnClickListener(click->{
                MinYang parentActivity = (MinYang)getContext();
                int position = getAbsoluteAdapterPosition();
                parentActivity.userClickedInfo(infos.get(position), position);
            });

            info_title = itemView.findViewById(R.id.info_title);
        }

        private int getAbsoluteAdapterPosition() {
            return getAbsoluteAdapterPosition();
        }

        public void setPosition(int p) {
        position = p;
    }
}


    /**
     *  rows of recycler view
     */
    class ChargeAdapter extends RecyclerView.Adapter<RowView> {
        List<ChargeInfo> infos;
        ChargeAdapter(List<ChargeInfo> infos){
            this.infos=infos;
        }

        @Override
        public void onBindViewHolder(RowView holder, int position) {
            holder.info_title.setText(infos.get(position).getTitle());
            holder.setPosition(position);
        }
        @Override
        public RowView onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View loadedRow = inflater.inflate(R.layout.charge_info, parent, false);
            return new RowView(loadedRow, infos);
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return infos.size();
        }


    }

    /**
     * station info
     */
    class ChargeInfo{
        long id;
        String telphone;
        String title;
        String longitude;
        String latitude;

        public ChargeInfo(String title,String telphone,String longitude,String latitude) {
            this.title = title;
            this.latitude = latitude;
            this.longitude = longitude;
            this.telphone = telphone;
        }

        public ChargeInfo(long id, String telphone,String title,String longitude,String latitude) {
            this.id = id;
            this.telphone = telphone;
            this.longitude = longitude;
            this.title = title;
            this.latitude = latitude;

        }

        public void setId(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

        public String getTelphone() {
            return telphone;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getTitle() {
            return title;
        }

        public String getLatitude() {
            return latitude;
        }

    }


}
