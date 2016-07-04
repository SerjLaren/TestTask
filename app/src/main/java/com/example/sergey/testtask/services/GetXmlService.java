package com.example.sergey.testtask.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;

import com.example.sergey.testtask.activities.ServiceActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetXmlService extends Service {

    private Intent intentFilt;
    private StringBuilder stringBuilder;
    private InputStream inStream;
    private MyTask myAsync;
    private final static int WAITING_TIME = 20000;
    private final static String XML_DATA = "xml_data";
    private final static String DATE_TAG = "date", TEXT_TAG = "text", XML_LOADED = "xml_loaded",
            XML_URL = "http://storage.space-o.ru/testXmlFeed.xml", METHOD_GET = "GET";

    public int onStartCommand(Intent intent, int flags, int startId) {
        myAsync = new MyTask();
        myAsync.execute();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        myAsync.cancel(true);
        stopSelf();
    }

    private void getXmlFromUrl() {
        try {
            URL urlXML = new URL(XML_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) urlXML.openConnection();
            urlConnection.setRequestMethod(METHOD_GET);
            urlConnection.setConnectTimeout(WAITING_TIME);
            urlConnection.setReadTimeout(WAITING_TIME);
            try {
                inStream = urlConnection.getInputStream();
                XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = xmlFactory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inStream, null);
                stringBuilder = new StringBuilder();
                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    switch (parser.getEventType()) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (String.valueOf(parser.getName()).equals(DATE_TAG)) { // если текущий тэг содержит дату
                                SystemClock.sleep(100); // задержка специально для наглядности загрузки данных в список
                                parser.next();
                                stringBuilder.append(parser.getText().toString()).append("\n");
                                intentFilt = new Intent(ServiceActivity.CONNECTION_TO_XMLSERVICE);
                                intentFilt.putExtra(XML_DATA, stringBuilder.toString());
                                LocalBroadcastManager.getInstance(GetXmlService.this).sendBroadcast(intentFilt);
                            }
                            if (String.valueOf(parser.getName()).equals(TEXT_TAG)) { // если текущий тэг содержит текст сообщения
                                SystemClock.sleep(100); // задержка специально для наглядности загрузки данных в список
                                parser.next();
                                stringBuilder.append(parser.getText().toString()).append("\n" + "\n" + "\n");
                                intentFilt = new Intent(ServiceActivity.CONNECTION_TO_XMLSERVICE);
                                intentFilt.putExtra(XML_DATA, stringBuilder.toString());
                                LocalBroadcastManager.getInstance(GetXmlService.this).sendBroadcast(intentFilt);
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            break;

                        default:
                            break;
                    }
                    parser.next();
                }
                inStream.close();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
        }
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getXmlFromUrl();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            intentFilt = new Intent(ServiceActivity.CONNECTION_TO_XMLSERVICE);
            intentFilt.putExtra(XML_LOADED, true);
            LocalBroadcastManager.getInstance(GetXmlService.this).sendBroadcast(intentFilt);
            intentFilt = new Intent(ServiceActivity.CONNECTION_TO_XMLSERVICE);
            intentFilt.putExtra(XML_DATA, stringBuilder.toString());
            LocalBroadcastManager.getInstance(GetXmlService.this).sendBroadcast(intentFilt);
            super.onPostExecute(result);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
