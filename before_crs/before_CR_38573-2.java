/*Updated DeviceConfig to write out more OpenGL information

Change-Id:I045a2d8c9fabc1a3c0f818e9858cc3e10c8a883a*/
//Synthetic comment -- diff --git a/apps/DeviceConfig/src/com/example/android/deviceconfig/ConfigGenerator.java b/apps/DeviceConfig/src/com/example/android/deviceconfig/ConfigGenerator.java
//Synthetic comment -- index 0bfe5d0..e8d6333 100644

//Synthetic comment -- @@ -16,6 +16,27 @@

package com.example.android.deviceconfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
//Synthetic comment -- @@ -38,26 +59,9 @@
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

public class ConfigGenerator {
private Context mCtx;

public static final String NS_DEVICES_XSD = "http://schemas.android.com/sdk/devices/1";

//Synthetic comment -- @@ -151,8 +155,9 @@

private static final String TAG = "ConfigGenerator";

    public ConfigGenerator(Context ctx) {
        mCtx = ctx;
}

@SuppressLint("WorldReadableFiles")
//Synthetic comment -- @@ -547,14 +552,29 @@

Element glVersion = doc.createElement(PREFIX + NODE_GL_VERSION);
software.appendChild(glVersion);
            glVersion.appendChild(doc.createTextNode(" "));

Element glExtensions = doc.createElement(PREFIX + NODE_GL_EXTENSIONS);
software.appendChild(glExtensions);
            glExtensions.appendChild(doc.createTextNode(" "));

Transformer tf = TransformerFactory.newInstance().newTransformer();
tf.setOutputProperty(OutputKeys.INDENT, "yes");
DOMSource source = new DOMSource(doc);
String filename = String.format("devices_%1$tm_%1$td_%1$ty.xml", Calendar.getInstance()
.getTime());








//Synthetic comment -- diff --git a/apps/DeviceConfig/src/com/example/android/deviceconfig/MyActivity.java b/apps/DeviceConfig/src/com/example/android/deviceconfig/MyActivity.java
//Synthetic comment -- index 837d072..38a78cd 100644

//Synthetic comment -- @@ -16,27 +16,44 @@

package com.example.android.deviceconfig;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.example.android.deviceconfig.R;

public class MyActivity extends Activity implements OnClickListener {

/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.main);

Button btn = (Button) findViewById(R.id.generateConfigButton);
btn.setOnClickListener(this);
//Synthetic comment -- @@ -114,7 +131,7 @@
}

public void onClick(View v) {
        ConfigGenerator configGen = new ConfigGenerator(this);
final String filename = configGen.generateConfig();
if (filename != null) {
Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//Synthetic comment -- @@ -130,4 +147,40 @@
}
}

}







