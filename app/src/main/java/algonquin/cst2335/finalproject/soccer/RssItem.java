package algonquin.cst2335.finalproject.soccer;

import android.content.Context;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.HashMap;
import java.util.Locale;

public class RssItem {
    int saved;
    long  recordId;
    String title;
    String description;
    String pubDate;
    String url;
    String imageUrl;
    String thumbUrl;

    long id;

    public RssItem(String s) {
        this.title = s;
    }

    public RssItem(HashMap<String, String> item) {
        this.saved = 0;
        this.title = item.get("title");
        this.description = item.get("description");
        this.pubDate = item.get("pubDate");
        this.url = item.get("link");
        this.imageUrl = item.get("image");
        this.thumbUrl = item.get("thumbnail");
    }

    public RssItem(Cursor row) {
        this.saved = 1;
        try {
            this.title = row.getString(row.getColumnIndex(MyOpenHelper.title));
            this.description = row.getString(row.getColumnIndex(MyOpenHelper.description));
            this.pubDate = row.getString(row.getColumnIndex(MyOpenHelper.pubDate));
            this.url = row.getString(row.getColumnIndex(MyOpenHelper.url));
            this.imageUrl = row.getString(row.getColumnIndex(MyOpenHelper.imageUrl));
            this.thumbUrl = row.getString(row.getColumnIndex(MyOpenHelper.thumbUrl));
            this.id = row.getLong(row.getColumnIndex(MyOpenHelper.id));
        } catch ( Exception ex){
            //
        }
    }

    public Boolean checkFilter(String filterValue) {
        Boolean filter = false;
        if (!filterValue.equals(""))
        {
            if (description.toLowerCase(Locale.ROOT).contains(filterValue) || title.toLowerCase(Locale.ROOT).contains(filterValue))
            {
                filter = true;
            }

        }
        else {
            filter = true;
        }
        return filter;
    }


    public RssItem(String title, int saved, String pubDate) {
        this.saved = saved;
        this.title = title;
        this.pubDate = pubDate;
    }


    public String getTitle() {
        return title;
    }

    public int getSaved() {
        return saved;
    }

    public String getPubDate() {
        return pubDate;
    }

    public Uri getThumb() {
        //return thumbUrl;
        return Uri.parse(thumbUrl.replace("http:", "https:"));
    }

    public String getUrl() {
        return url;
    }

    public String  getImageUrl() {
        return imageUrl.replace("http:", "https:");
        //return Uri.parse(imageUrl.replace("http:", "https:"));
    }

    public long saveItem(Context context) {
        MyOpenHelper opener = new MyOpenHelper(context);
        SQLiteDatabase db = opener.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyOpenHelper.title, title);
        values.put(MyOpenHelper.description, description);
        values.put(MyOpenHelper.pubDate, pubDate);
        values.put(MyOpenHelper.url, url);
        values.put(MyOpenHelper.imageUrl, imageUrl);
        values.put(MyOpenHelper.thumbUrl, thumbUrl);
        recordId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.TABLE_NAME, values);
        db.close();
        return recordId;
    }


    public void setId(long l) {
        id = l;
    }
    public long getId() {
        return id;
    }

}
