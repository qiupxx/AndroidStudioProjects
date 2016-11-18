package com.example.peixuan.earthquakefeed;


import android.app.ListFragment;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * A simple {@link ListFragment} subclass.
 */
public class EarthquakeListFragment extends ListFragment {
    private ArrayAdapter<Quake> aa;
    private ArrayList<Quake> earthquakes = new ArrayList<>();

    /**
     * 重写该方法，展开自己的view hierarchy
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //onCreateView()后调用，做最后的初始化
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //创建ArrayAdapter
        aa = new ArrayAdapter<Quake>(getActivity(), android.R.layout.simple_list_item_1, earthquakes);

        //绑定ArrayAdapter与ListView
        setListAdapter(aa);

        //创建线程刷新数据
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                refreshEarthquakes();
            }
        });
        t.start();
    }

    //获取与解析服务器数据
    private static final String TAG = "EARTHQUAKE";
    private Handler handler = new Handler();

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

                //清除ArrayList
                earthquakes.clear();

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
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                addNewQuake(quake);
                            }
                        });
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
        }

    }

    //将获取的地震数据构造Quake对象，添加到earthquakes list内
    private void addNewQuake(Quake _quake) {
        EarthquakeActivity earthquakeActivity = (EarthquakeActivity) getActivity();
        if (_quake.getMagnitude() > earthquakeActivity.minimumMagnitude)
            earthquakes.add(_quake);

        //通知ArrayAdapter数据发生改变
        aa.notifyDataSetChanged();
    }

}
