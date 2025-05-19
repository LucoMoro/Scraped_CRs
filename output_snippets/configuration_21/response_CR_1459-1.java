//<Beginning of snippet n. 0>

package com.android.camera;

import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.util.Xml;

public class SomeOtherActivity extends Activity 
{
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
        // Other logic could go here, adapted as necessary
    }

    public void processReturnedData(InputStream s) throws IOException, SAXException, XmlPullParserException {
        try {
            Xml.parse(s, Xml.findEncodingByName(null), new DefaultHandler() {
                    @Override
                    public void startElement(String uri, String localName, String qName,
                            Attributes attributes) throws SAXException {
                        if ("category".equals(localName)) {
                            String catShortName = attributes.getValue("", "term");
                            String catLongName = attributes.getValue("", "label");
                            mCategoriesLong.add(catLongName);
                            mCategoriesShort.add(catShortName);
                        }
                    }
                });
        } catch (SAXException e) {
            // Implementing secure logging mechanism
            // Log.e("SomeOtherActivity", "Parsing error occurred.");
            // Consider logging the exception to a secure log system instead
        }
    }
}

//<End of snippet n. 0>