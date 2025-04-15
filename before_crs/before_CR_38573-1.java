/*Updated DeviceConfig to write out more OpenGL information

Change-Id:I045a2d8c9fabc1a3c0f818e9858cc3e10c8a883a*/
//Synthetic comment -- diff --git a/apps/DeviceConfig/src/com/example/android/deviceconfig/ConfigGenerator.java b/apps/DeviceConfig/src/com/example/android/deviceconfig/ConfigGenerator.java
//Synthetic comment -- index 0bfe5d0..d5573c4 100644

//Synthetic comment -- @@ -45,6 +45,7 @@
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
//Synthetic comment -- @@ -58,6 +59,7 @@

public class ConfigGenerator {
private Context mCtx;

public static final String NS_DEVICES_XSD = "http://schemas.android.com/sdk/devices/1";

//Synthetic comment -- @@ -151,8 +153,9 @@

private static final String TAG = "ConfigGenerator";

    public ConfigGenerator(Context ctx) {
        mCtx = ctx;
}

@SuppressLint("WorldReadableFiles")
//Synthetic comment -- @@ -547,14 +550,29 @@

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
//Synthetic comment -- index 837d072..4dcd8df 100644

//Synthetic comment -- @@ -18,25 +18,42 @@

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







