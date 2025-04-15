/*Replaced deprecated SensorListener with new SensorEventListener
Removed unused imports

Change-Id:Ic222b758439e916e50a508b8e36e93fbe56c5294*/




//Synthetic comment -- diff --git a/src/com/android/browser/BrowserActivity.java b/src/com/android/browser/BrowserActivity.java
//Synthetic comment -- index 60dffac..68fd8b8 100644

//Synthetic comment -- @@ -38,7 +38,6 @@
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
//Synthetic comment -- @@ -47,20 +46,17 @@
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.WebAddress;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.AsyncTask;
//Synthetic comment -- @@ -99,12 +95,6 @@
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
//Synthetic comment -- @@ -115,7 +105,6 @@
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebIconDatabase;
import android.webkit.WebStorage;
//Synthetic comment -- @@ -127,13 +116,8 @@
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
//Synthetic comment -- @@ -141,14 +125,10 @@
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BrowserActivity extends Activity
implements View.OnCreateContextMenuListener,
//Synthetic comment -- @@ -754,19 +734,19 @@
*              * *
*               *
*/
    private final SensorEventListener mSensorEventListener = new SensorEventListener() {
private long mLastGestureTime;
private float[] mPrev = new float[3];
private float[] mPrevDiff = new float[3];
private float[] mDiff = new float[3];
private float[] mRevertDiff = new float[3];

        public void onSensorChanged(SensorEvent event) {
boolean show = false;
float[] diff = new float[3];

for (int i = 0; i < 3; i++) {
                diff[i] = event.values[i] - mPrev[i];
if (Math.abs(diff[i]) > 1) {
show = true;
}
//Synthetic comment -- @@ -781,14 +761,14 @@
}
mDiff[i] += diff[i];
mPrevDiff[i] = diff[i];
                mPrev[i] = event.values[i];
}

if (false) {
// only shows if we think the delta is big enough, in an attempt
// to detect "serious" moves left/right or up/down
                Log.d("BrowserSensorHack", "sensorChanged " + event.sensor.getName() + " ("
                        + event.values[0] + ", " + event.values[1] + ", " + event.values[2] + ")"
+ " diff(" + diff[0] + " " + diff[1] + " " + diff[2]
+ ")");
Log.d("BrowserSensorHack", "      mDiff(" + mDiff[0] + " "
//Synthetic comment -- @@ -831,7 +811,7 @@
}
}

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
// TODO Auto-generated method stub

}
//Synthetic comment -- @@ -873,8 +853,8 @@
mSensorManager = (SensorManager) getSystemService(
Context.SENSOR_SERVICE);
}
            mSensorManager.registerListener(mSensorEventListener,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
SensorManager.SENSOR_DELAY_FASTEST);
} else {
mSensorManager = null;
//Synthetic comment -- @@ -1137,7 +1117,7 @@
WebView.disablePlatformNotifications();

if (mSensorManager != null) {
            mSensorManager.unregisterListener(mSensorEventListener);
}
}

//Synthetic comment -- @@ -4254,7 +4234,7 @@
Intent intent = new Intent(this,
BrowserDownloadPage.class);
intent.setData(downloadRecord);
        startActivityForResult(intent, BrowserActivity.DOWNLOAD_PAGE);

}








