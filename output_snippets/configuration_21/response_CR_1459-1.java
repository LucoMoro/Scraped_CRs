//<Beginning of snippet n. 0>

package com.android.camera;

import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.widget.TextView;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.util.Log;
import android.util.Xml;

public class CameraActivity extends Activity
{
    private static final String TAG = "CameraActivity";
    
    private static final String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";
    private static final boolean mDevServer = false;
    
    private ArrayList<String> mCategoriesShort = new ArrayList<String>();
    private ArrayList<String> mCategoriesLong  = new ArrayList<String>();

    @Override 
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        TextView tv = new TextView(this);
        tv.setText("");        
        setContentView(tv);
    }
    
    @Override 
    public void onResume() {
        super.onResume();
        
        final ProgressDialog pd = ProgressDialog.show(this, "please wait", "");
        
        final ImageManager.IImageList all = ImageManager.instance().allImages(
                CameraActivity.this, 
                getContentResolver(), 
                ImageManager.DataLocation.ALL, 
                ImageManager.INCLUDE_VIDEOS, 
                ImageManager.SORT_ASCENDING);

        android.net.Uri uri = getIntent().getData();
        if (uri == null) {
            uri = (Uri) getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
        }
        if (uri != null) {
            final ImageManager.VideoObject vid = (ImageManager.VideoObject) all.getImageForUri(uri);
            if (vid != null) {
                new Thread(new Runnable() {
                    public void run() {
                        getCategories();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                pd.cancel();
                                // Removed YouTubeUpload references
                                finish();
                            }
                        });
                    }
                }).start();
            }
        }
    }
    
    protected String getYouTubeBaseUrl() {
        if (mDevServer) {
            return "http://dev.gdata.youtube.com";
        } else {
            return "http://gdata.youtube.com";
        }
    }

    public void getCategories() {
        String uri = getYouTubeBaseUrl() + "/schemas/2007/categories.cat";
        AndroidHttpClient mClient = AndroidHttpClient.newInstance("Android-Camera/0.1");

        try {
            org.apache.http.HttpResponse r = mClient.execute(new org.apache.http.client.methods.HttpGet(uri));
            processReturnedData(r.getEntity().getContent());
        } catch (Exception ex) {
            Log.e(TAG, "Error getting categories", ex);
        }
    }
    
    public void processReturnedData(InputStream s) throws IOException, SAXException, XmlPullParserException {
        try {
            Xml.parse(s, Xml.findEncodingByName(null), new DefaultHandler() {
                    @Override
                    public void startElement(String uri, String localName, String qName,
                            Attributes attributes) throws SAXException {
                        if (ATOM_NAMESPACE.equals(uri)) {
                            if ("category".equals(localName)) {
                                String catShortName = attributes.getValue("", "term");
                                String catLongName = attributes.getValue("", "label");
                                mCategoriesLong .add(catLongName);
                                mCategoriesShort.add(catShortName);
                                return;
                            }
                        }
                    }
                });
        } catch (SAXException e) {
            Log.e(TAG, "Error processing returned data");
        }
    }
}

//<End of snippet n. 0>