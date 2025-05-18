//<Beginning of snippet n. 0>

package com.android.camera;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<String> mCategoriesShort = new ArrayList<String>();
    private ArrayList<String> mCategoriesLong = new ArrayList<String>();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }

    @Override
    public void onResume() {
        super.onResume();

        final ProgressDialog pd = ProgressDialog.show(this, "please wait", "");

        final ImageManager.IImageList all = ImageManager.instance().allImages(
                MainActivity.this,
                getContentResolver(),
                ImageManager.DataLocation.ALL,
                ImageManager.INCLUDE_VIDEOS,
                ImageManager.SORT_ASCENDING
        );

        Uri uri = getIntent().getData();
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
                            }
                        });
                    }
                }).start();
            }
        }
    }

    public void getCategories() {
        String uri = getYouTubeBaseUrl() + "/schemas/2007/categories.cat";
        AndroidHttpClient mClient = AndroidHttpClient.newInstance("Android-Camera/0.1");

        try {
            org.apache.http.HttpResponse r = mClient.execute(new org.apache.http.client.methods.HttpGet(uri));
            processReturnedData(r.getEntity().getContent());
        } catch (Exception ex) {
            // Handle exception without exposing details
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
                            mCategoriesLong.add(catLongName);
                            mCategoriesShort.add(catShortName);
                        }
                    }
                }
            });
        } catch (SAXException e) {
            // Exception caught with no logging
        }
    }
}

//<End of snippet n. 0>