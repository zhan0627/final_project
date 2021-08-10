package algonquin.cst2335.finalproject.soccer;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.R;


public class GoalCom
{
    Context context;
    public boolean ready = false;
    public GoalCom(Context context)
    {
        this.context = context;
    }
    public ArrayList<HashMap<String, String>> goalList = null;
    public void loadFeed (String language)
    {
        goalList = new ArrayList<>();
        Executor newThread=Executors.newSingleThreadExecutor();
        newThread.execute(()-> {
            try {
                String stringURL = "https://www.goal.com/" + context.getString(R.string.rsslanguage)  +"/feeds/news?fmt=rss";
                URL url = new URL(stringURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
                parser.setInput(in, "UTF-8");

                String description = null;
                String iconName = null;
                String current = null;
                String min = null;
                String max = null;
                String humidity = null;

                // https://www.tutlane.com/tutorial/android/android-xml-parsing-using-xmlpullparser

                HashMap<String,String> item = new HashMap<>();
                String tag = "";
                String text = "";
                int event = parser.getEventType();
                while ( event != XmlPullParser.END_DOCUMENT) {
                    tag = parser.getName();
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            text = null;
                            if (tag.equals("item")) {
                                if (goalList == null)
                                {
                                    goalList = new ArrayList<>();
                                }
                                item = new HashMap<>();
                            }
                            if (tag.equals("df:author")) {
                                text = parser.getAttributeValue(null, "name");
                            }
                            if (tag.equals("media:content") || tag.equals("media:thumbnail")) {
                                text = parser.getAttributeValue(null, "url");
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            switch (tag){
                                case "title":
                                    item.put("title",text);
                                    Log.i("goal", text);
                                    break;
                                case "description":
                                    item.put("description",text);
                                    break;
                                case "pubDate":
                                    item.put("pubDate",text);
                                    break;
                                case "link":
                                    item.put("link",text);
                                    break;
                                case "category":
                                    // filter here
                                    item.put("category",text);
                                    break;
                                case "media:content":
                                    item.put("image",text);
                                    break;
                                case "media:thumbnail":
                                    item.put("thumbnail",text);
                                    break;
                                case "df:language":
                                    item.put("language",text);
                                    break;
                                case "df:author":
                                    item.put("author",text);
                                    if (text != null)
                                        Log.i("goal-author", text);
                                    break;
                                case "dc:creator":
                                    item.put("creator",text);
//                                    Log.i("goal", text);
                                    break;
                                case "item":
                                    if(goalList != null)
                                        goalList.add(item);
                                    break;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            text = parser.getText();
                            break;
                    }
                    event = parser.next();
                }

//                 String text1 = (new BufferedReader(
//                         new InputStreamReader(in, StandardCharsets.UTF_8)))
//                         .lines()
//                         .collect(Collectors.joining("\n"));

                Log.d("goal", String.valueOf(goalList.size()));
                ready = true;

            } catch(IOException | XmlPullParserException ioe) {
                Log.e("Connection error:", ioe.getMessage());
                ready = true;
            };
        });
        return;
    };
}
