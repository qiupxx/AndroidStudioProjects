package com.example.peixuan.earthquakefeed;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class EarthquakeUpdateService extends Service {

    public static String TAG = "EARTHQUAKE_UPDATE_SERVICE";
    private Timer updateTimer;

    @Override
    public void onCreate() {
        super.onCreate();
        updateTimer = new Timer("earthquakeUpdates");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        int updateFreq = Integer.parseInt(prefs.getString(PreferencesActivity.PREF_UPDATE_FREQ, "60"));
        boolean autoUpdateChecked = prefs.getBoolean(PreferencesActivity.PREF_AUTO_UPDATE, false);

        updateTimer.cancel();
        if (autoUpdateChecked) {
            updateTimer = new Timer("earthquakeUpdates");
            updateTimer.scheduleAtFixedRate(doRefresh, 0, updateFreq * 60 * 1000);
        } else {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    refreshEarthquakes();
                }
            });
            t.start();
        }
        return Service.START_STICKY;
    }

    private TimerTask doRefresh = new TimerTask() {
        @Override
        public void run() {
            refreshEarthquakes();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //将获取的地震数据构造Quake对象，添加到earthquakes list内
    private void addNewQuake(Quake _quake) {
        ContentResolver cr = getContentResolver();

        String where = EarthquakeProvider.KEY_DATE + "=" + _quake.getDate().getTime();

        Cursor query = cr.query(EarthquakeProvider.CONTENT_URI, null, where, null, null);
        if (query.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(EarthquakeProvider.KEY_DATE, _quake.getDate().getTime());
            values.put(EarthquakeProvider.KEY_DETAILS, _quake.getDetails());
            values.put(EarthquakeProvider.KEY_SUMMARY, _quake.toString());

            double latitude = _quake.getLocation().getLatitude();
            double longitude = _quake.getLocation().getLongitude();
            values.put(EarthquakeProvider.KEY_LOCATION_LAT, latitude);
            values.put(EarthquakeProvider.KEY_LOCATION_LNG, longitude);
            values.put(EarthquakeProvider.KEY_LINK, _quake.getLink());
            values.put(EarthquakeProvider.KEY_MAGNITUDE, _quake.getMagnitude());

            cr.insert(EarthquakeProvider.CONTENT_URI, values);
        }
        query.close();
    }

    public void refreshEarthquakes() {
        URL url;
        try {
            String quakeFeed = getString(R.string.quake_feed_url);
            url = new URL(quakeFeed);

            URLConnection connection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            int responseCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "resonseCode : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "成功获取服务器响应");

                InputStream in = httpURLConnection.getInputStream();

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                Document dom = db.parse(in);
                Element docEle = dom.getDocumentElement();

                NodeList nl = docEle.getElementsByTagName("entry");
                if (nl != null && nl.getLength() > 0) {
                    Log.d(TAG, "entry nodes number : " + (nl.getLength() + 1));
                    for (int i = 0; i < nl.getLength(); i++) {
                        Element entry = (Element) nl.item(i);
                        Element title = (Element) entry.getElementsByTagName("title").item(0);
                        Element g = (Element) entry.getElementsByTagName("georss:point").item(0);
                        Element when = (Element) entry.getElementsByTagName("updated").item(0);
                        Element link = (Element) entry.getElementsByTagName("link").item(0);

                        String details = title.getFirstChild().getNodeValue();
                        String hostname = "http://earthquake.usgs.gov";
                        String linkString = hostname + link.getAttribute("href");

                        String point = g.getFirstChild().getNodeValue();
                        String dt = when.getFirstChild().getNodeValue();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
                        Date qdate = new GregorianCalendar(0, 0, 0).getTime();
                        try {
                            qdate = sdf.parse(dt);
                        } catch (ParseException e) {
                            Log.d(TAG, "解析日期失败" + e.getMessage());
                        }

                        Log.d(TAG, "构造Location");

                        String[] location = point.split(" ");
                        Location l = new Location("dummyGPS");
                        l.setLatitude(Double.parseDouble(location[0]));
                        l.setLongitude(Double.parseDouble(location[1]));

                        Log.d(TAG, "构造magnitude");
                        String magnitudeString = details.split(" ")[1];
                        double magnitude = Double.parseDouble(magnitudeString);


                        final Quake quake = new Quake(qdate, details, l, magnitude, linkString);
                        Log.d(TAG, "解析html文件完成");

                        //处理新创建的Quake
                        addNewQuake(quake);
                    }
                }

            }
        } catch (MalformedURLException e) {
            Log.d(TAG, "构造URL失败");
        } catch (IOException e) {
            Log.d(TAG, "打开url连接失败");
        } catch (ParserConfigurationException e) {
            Log.d(TAG, "解析DocumentBuilder失败");
        } catch (SAXException e) {
            Log.d(TAG, "解析DOM失败");
        } finally {
        }
    }
}
