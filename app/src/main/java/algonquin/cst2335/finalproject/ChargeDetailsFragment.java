package algonquin.cst2335.finalproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 *  charger finder station detail
 * author:MinYang
 */
public class ChargeDetailsFragment extends Fragment {
    ChargeFragment.ChargeInfo chosenInfo;
    int chosenPosition;
    SQLiteDatabase db;

    public  ChargeDetailsFragment(ChargeFragment.ChargeInfo info, int position) {
        chosenInfo = info;
        chosenPosition = position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailView = inflater.inflate(R.layout.charge_detail_fragment, container, false);
        ChargeOpenHelper opener = new ChargeOpenHelper(getContext());
        db = opener.getWritableDatabase();
        TextView title_details = detailView.findViewById(R.id.title_details);
        TextView lat_details = detailView.findViewById(R.id.lat_details);
        TextView long_details = detailView.findViewById(R.id.long_details);
        TextView telephone_details = detailView.findViewById(R.id.telephon_details);

        title_details.setText("Title is: " + chosenInfo.getTitle());
        lat_details.setText("Latitude is: " + chosenInfo.getLatitude());
        long_details.setText("Longitude is: " + chosenInfo.getLongitude());
        telephone_details.setText("Telphone is: " + chosenInfo.getTelphone());
        /**
         * click to redirect to google maps
         */
        Button btn_googlemap = detailView.findViewById(R.id.btn_googlemap);
        btn_googlemap.setOnClickListener(click->{
            Uri gmmIntentUri = Uri.parse("geo:" + chosenInfo.getLatitude() + "," + chosenInfo.getLongitude());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            startActivity(mapIntent);

        });
        /**
         *  add to finder
         */
        Button btn_finder = detailView.findViewById(R.id.btn_finder);
        btn_finder.setOnClickListener(click->{

            ContentValues newRow = new ContentValues();
            newRow.put(ChargeOpenHelper.col_title, chosenInfo.getTitle());
            newRow.put(ChargeOpenHelper.col_latitude, chosenInfo.getLatitude());
            newRow.put(ChargeOpenHelper.col_longitude, chosenInfo.getLongitude());
            newRow.put(ChargeOpenHelper.col_telphone, chosenInfo.getTelphone());
            db.insert(ChargeOpenHelper.TABLE_NAME, null, newRow);
        });

        /**
         * delete button
         */
        Button btn_delete = detailView.findViewById(R.id.button_delete);
        btn_delete.setOnClickListener(click_delete->{
            MinYang parentActicity = (MinYang)getContext();
            parentActicity.notifyInfoDeleted(chosenInfo, chosenPosition);
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });
        /**
         * close button
         */
        Button button_close = detailView.findViewById(R.id.button_close);
        button_close.setOnClickListener(click_close->{
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        return detailView;
    }
}

