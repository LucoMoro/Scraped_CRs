/*Changed Sample Sensor Implementation, Cleaned up Android Samples

Change-Id:I17d64997d2f4e3c338f0c8558c8a23fd4e91701a*/




//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/ApiDemos.java b/samples/ApiDemos/src/com/example/android/apis/ApiDemos.java
//Synthetic comment -- index 78b1fd7..1c1a294 100644

//Synthetic comment -- @@ -52,8 +52,8 @@
getListView().setTextFilterEnabled(true);
}

    protected List<Map<String, ?>> getData(String prefix) {
        List<Map<String, ?>> myData = new ArrayList<Map<String, ?>>();

Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
mainIntent.addCategory(Intent.CATEGORY_SAMPLE_CODE);
//Synthetic comment -- @@ -107,10 +107,11 @@
return myData;
}

    private final static Comparator<Map<String, ?>> sDisplayNameComparator =
        new Comparator<Map<String, ?>>() {
private final Collator   collator = Collator.getInstance();

        public int compare(Map<String, ?> map1, Map<String, ?> map2) {
return collator.compare(map1.get("title"), map2.get("title"));
}
};
//Synthetic comment -- @@ -128,7 +129,7 @@
return result;
}

    protected void addItem(List<Map<String, ?>> data, String name, Intent intent) {
Map<String, Object> temp = new HashMap<String, Object>();
temp.put("title", name);
temp.put("intent", intent);
//Synthetic comment -- @@ -136,8 +137,9 @@
}

@Override
    @SuppressWarnings("unchecked")
protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, ?> map = (Map<String, ?>) l.getItemAtPosition(position);

Intent intent = (Intent) map.get("intent");
startActivity(intent);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/animation/Transition3d.java b/samples/ApiDemos/src/com/example/android/apis/animation/Transition3d.java
//Synthetic comment -- index 38e69d0..a52b01b 100644

//Synthetic comment -- @@ -101,7 +101,7 @@
mContainer.startAnimation(rotation);
}

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
// Pre-load the image then start the animation
mImageView.setImageResource(PHOTOS_RESOURCES[position]);
applyRotation(position, 0, 90);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/Animation.java b/samples/ApiDemos/src/com/example/android/apis/app/Animation.java
//Synthetic comment -- index 90831f5..5ba41c4 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
import com.example.android.apis.view.Controls1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/ContactsFilter.java b/samples/ApiDemos/src/com/example/android/apis/app/ContactsFilter.java
//Synthetic comment -- index bb843e5..d5b3deb 100644

//Synthetic comment -- @@ -20,8 +20,6 @@
// class is in a sub-package.
import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
//Synthetic comment -- @@ -29,7 +27,6 @@

import com.example.android.apis.R;

/**
* Front-end for launching {@link ContactsFilterInstrumentation} example
* instrumentation class.








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/ContactsFilterInstrumentation.java b/samples/ApiDemos/src/com/example/android/apis/app/ContactsFilterInstrumentation.java
//Synthetic comment -- index 04bb671..6895363 100644

//Synthetic comment -- @@ -23,8 +23,6 @@
import android.os.Bundle;
import android.util.Log;

/**
* This is an example implementation of the {@link android.app.Instrumentation}
* class, allowing you to run tests against application code.  The








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/ContactsSelectInstrumentation.java b/samples/ApiDemos/src/com/example/android/apis/app/ContactsSelectInstrumentation.java
//Synthetic comment -- index dcb8b83..cdc490d 100644

//Synthetic comment -- @@ -20,14 +20,11 @@
import android.app.Instrumentation;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.provider.Contacts;
import android.os.Bundle;
import android.util.Log;

/**
* This is an example implementation of the {@link android.app.Instrumentation}
* class, allowing you to run tests against application code.  The








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/DefaultValues.java b/samples/ApiDemos/src/com/example/android/apis/app/DefaultValues.java
//Synthetic comment -- index 35bdf13..494a2ea 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import com.example.android.apis.ApiDemosApplication;
import com.example.android.apis.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/ForegroundService.java b/samples/ApiDemos/src/com/example/android/apis/app/ForegroundService.java
//Synthetic comment -- index 27708f4..bf61c26 100644

//Synthetic comment -- @@ -43,9 +43,9 @@
static final String ACTION_FOREGROUND = "com.example.android.apis.FOREGROUND";
static final String ACTION_BACKGROUND = "com.example.android.apis.BACKGROUND";

    private static final Class<?>[] mStartForegroundSignature = new Class[] {
int.class, Notification.class};
    private static final Class<?>[] mStopForegroundSignature = new Class[] {
boolean.class};

private NotificationManager mNM;
//Synthetic comment -- @@ -112,6 +112,7 @@
* This is a wrapper around the new startForeground method, using the older
* APIs if it is not available.
*/
    @SuppressWarnings("deprecation")
void startForegroundCompat(int id, Notification notification) {
// If we have the new startForeground API, then use it.
if (mStartForeground != null) {
//Synthetic comment -- @@ -138,6 +139,7 @@
* This is a wrapper around the new stopForeground method, using the older
* APIs if it is not available.
*/
    @SuppressWarnings("deprecation")
void stopForegroundCompat(int id) {
// If we have the new stopForeground API, then use it.
if (mStopForeground != null) {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/ForegroundServiceController.java b/samples/ApiDemos/src/com/example/android/apis/app/ForegroundServiceController.java
//Synthetic comment -- index a6a6ef3..4849a6b 100644

//Synthetic comment -- @@ -21,14 +21,12 @@
import com.example.android.apis.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
* <p>Example of explicitly starting and stopping the {@link ForegroundService}.
*/








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/LaunchingPreferences.java b/samples/ApiDemos/src/com/example/android/apis/app/LaunchingPreferences.java
//Synthetic comment -- index aa151fe..32d1280 100644

//Synthetic comment -- @@ -28,7 +28,6 @@
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/LocalSample.java b/samples/ApiDemos/src/com/example/android/apis/app/LocalSample.java
//Synthetic comment -- index 1bb26e9..0e4b66e 100644

//Synthetic comment -- @@ -20,8 +20,6 @@
// class is in a sub-package.
import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
//Synthetic comment -- @@ -29,7 +27,6 @@

import com.example.android.apis.R;

/**
* Front-end for launching {@link LocalSampleInstrumentation} example
* instrumentation class.








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/LocalSampleInstrumentation.java b/samples/ApiDemos/src/com/example/android/apis/app/LocalSampleInstrumentation.java
//Synthetic comment -- index e0f6163..aabfec3 100644

//Synthetic comment -- @@ -23,8 +23,6 @@
import android.os.Bundle;
import android.util.Log;

/**
* This is an example implementation of the {@link android.app.Instrumentation}
* class demonstrating instrumentation against one of this application's sample








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/LocalService.java b/samples/ApiDemos/src/com/example/android/apis/app/LocalService.java
//Synthetic comment -- index 79324a4..5ce0c92 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/LocalServiceController.java b/samples/ApiDemos/src/com/example/android/apis/app/LocalServiceController.java
//Synthetic comment -- index 6041249..b663340 100644

//Synthetic comment -- @@ -21,14 +21,12 @@
import com.example.android.apis.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
* <p>Example of explicitly starting and stopping the {@link LocalService}.
* This demonstrates the implementation of a service that runs in the same








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/MenuInflateFromXml.java b/samples/ApiDemos/src/com/example/android/apis/app/MenuInflateFromXml.java
//Synthetic comment -- index f51b3b8..42599ab 100644

//Synthetic comment -- @@ -19,14 +19,11 @@
import com.example.android.apis.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/MyPreference.java b/samples/ApiDemos/src/com/example/android/apis/app/MyPreference.java
//Synthetic comment -- index 967c181..96cee4e 100644

//Synthetic comment -- @@ -156,6 +156,7 @@
super(superState);
}

        @SuppressWarnings("unused")
public static final Parcelable.Creator<SavedState> CREATOR =
new Parcelable.Creator<SavedState>() {
public SavedState createFromParcel(Parcel in) {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/NotificationDisplay.java b/samples/ApiDemos/src/com/example/android/apis/app/NotificationDisplay.java
//Synthetic comment -- index a6c20ea..25b5d56 100644

//Synthetic comment -- @@ -24,14 +24,11 @@
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

/**
* Activity used by StatusBarNotification to show the notification to the user.
*/








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/NotifyingController.java b/samples/ApiDemos/src/com/example/android/apis/app/NotifyingController.java
//Synthetic comment -- index a0de699..b8b4e3f 100644

//Synthetic comment -- @@ -21,14 +21,12 @@
import com.example.android.apis.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
* Controller to start and stop a service. The serivce will update a status bar
* notification every 5 seconds for a minute.








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/NotifyingService.java b/samples/ApiDemos/src/com/example/android/apis/app/NotifyingService.java
//Synthetic comment -- index e580978..3b8139f 100644

//Synthetic comment -- @@ -30,7 +30,6 @@
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/**
* This is an example of service that will update its status bar balloon 








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/PreferencesFromXml.java b/samples/ApiDemos/src/com/example/android/apis/app/PreferencesFromXml.java
//Synthetic comment -- index 23461c6..63bbac2 100644

//Synthetic comment -- @@ -18,9 +18,7 @@

import com.example.android.apis.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferencesFromXml extends PreferenceActivity {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/QuickContactsDemo.java b/samples/ApiDemos/src/com/example/android/apis/app/QuickContactsDemo.java
//Synthetic comment -- index 1ee5742..004e568 100644

//Synthetic comment -- @@ -75,8 +75,6 @@
@Override
public void bindView(View view, Context context, Cursor cursor) {
final ContactListItemCache cache = (ContactListItemCache) view.getTag();
// Set the name
cursor.copyStringToBuffer(SUMMARY_NAME_COLUMN_INDEX, cache.nameBuffer);
int size = cache.nameBuffer.sizeCopied;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/ReceiveResult.java b/samples/ApiDemos/src/com/example/android/apis/app/ReceiveResult.java
//Synthetic comment -- index 4e248b9..22ae7f4 100644

//Synthetic comment -- @@ -18,8 +18,6 @@

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import com.example.android.apis.R;

import android.app.Activity;
//Synthetic comment -- @@ -31,8 +29,6 @@
import android.widget.Button;
import android.widget.TextView;

/**
* Shows how an activity can send data to its launching activity when done.y.
* <p>This can be used, for example, to implement a dialog alowing the user to








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/RemoteService.java b/samples/ApiDemos/src/com/example/android/apis/app/RemoteService.java
//Synthetic comment -- index c0debbc..726d6d7 100644

//Synthetic comment -- @@ -29,8 +29,6 @@
import android.os.RemoteCallbackList;
import android.widget.Toast;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import com.example.android.apis.R;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/SetWallpaperActivity.java b/samples/ApiDemos/src/com/example/android/apis/app/SetWallpaperActivity.java
//Synthetic comment -- index 8630b1c..f3d4ffb 100644

//Synthetic comment -- @@ -29,7 +29,6 @@
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/StatusBarNotifications.java b/samples/ApiDemos/src/com/example/android/apis/app/StatusBarNotifications.java
//Synthetic comment -- index 97f6199..9b911cc 100644

//Synthetic comment -- @@ -174,7 +174,7 @@

// Send the notification.
// We use a layout id because it is a unique number.  We use it later to cancel.
        mNotificationManager.notify(MOOD_NOTIFICATIONS, notification);
}

private void setMoodView(int moodId, int textId) {
//Synthetic comment -- @@ -202,7 +202,7 @@

// we use a string id because is a unique number.  we use it later to cancel the
// notification
        mNotificationManager.notify(MOOD_NOTIFICATIONS, notif);
}

private void setDefault(int defaults) {
//Synthetic comment -- @@ -231,8 +231,8 @@
notification.defaults = defaults;

mNotificationManager.notify(
                MOOD_NOTIFICATIONS, // we use a string id because it is a unique
                                    // number.  we use it later to cancel the notification
                notification);
}    
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/WallpaperActivity.java b/samples/ApiDemos/src/com/example/android/apis/app/WallpaperActivity.java
//Synthetic comment -- index 8d7f5a1..7b5eea2 100644

//Synthetic comment -- @@ -22,7 +22,6 @@

import android.app.Activity;
import android.os.Bundle;

/**
* <h3>Wallpaper Activity</h3>








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/appwidget/ExampleAppWidgetConfigure.java b/samples/ApiDemos/src/com/example/android/apis/appwidget/ExampleAppWidgetConfigure.java
//Synthetic comment -- index 5f8874c..e2cd1ca 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/AlphaBitmap.java b/samples/ApiDemos/src/com/example/android/apis/graphics/AlphaBitmap.java
//Synthetic comment -- index 8fff231..8b216cf 100644

//Synthetic comment -- @@ -18,15 +18,12 @@

import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

import java.io.InputStream;

public class AlphaBitmap extends GraphicsActivity {









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/AnimateDrawables.java b/samples/ApiDemos/src/com/example/android/apis/graphics/AnimateDrawables.java
//Synthetic comment -- index 7c9473d..6f58ff5 100644

//Synthetic comment -- @@ -18,13 +18,11 @@

import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.animation.*;
import android.os.Bundle;
import android.view.View;

public class AnimateDrawables extends GraphicsActivity {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Arcs.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Arcs.java
//Synthetic comment -- index ff8b38b..2efe851 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
// class is in a sub-package.
//import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/BitmapDecode.java b/samples/ApiDemos/src/com/example/android/apis/graphics/BitmapDecode.java
//Synthetic comment -- index 88f0c1d..3e3bb53 100644

//Synthetic comment -- @@ -18,15 +18,12 @@

import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.Bundle;
import android.view.*;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/BitmapPixels.java b/samples/ApiDemos/src/com/example/android/apis/graphics/BitmapPixels.java
//Synthetic comment -- index 88717bc..432efe4 100644

//Synthetic comment -- @@ -16,14 +16,9 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

import java.nio.IntBuffer;
//Synthetic comment -- @@ -41,7 +36,6 @@
private Bitmap mBitmap1;
private Bitmap mBitmap2;
private Bitmap mBitmap3;

// access the red component from a premultiplied color
private static int getR32(int c) { return (c >>  0) & 0xFF; }








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Clipping.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Clipping.java
//Synthetic comment -- index cf83597..6898e88 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/ColorFilters.java b/samples/ApiDemos/src/com/example/android/apis/graphics/ColorFilters.java
//Synthetic comment -- index 92d18ba..039cdfe 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.Bundle;
import android.view.*;

public class ColorFilters extends GraphicsActivity {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/ColorMatrixSample.java b/samples/ApiDemos/src/com/example/android/apis/graphics/ColorMatrixSample.java
//Synthetic comment -- index 19a0f7f..1a79e40 100644

//Synthetic comment -- @@ -18,11 +18,9 @@

import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.View;

public class ColorMatrixSample extends GraphicsActivity {
//Synthetic comment -- @@ -35,9 +33,7 @@

private static class SampleView extends View {
private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
private Bitmap mBitmap;
private float mAngle;

public SampleView(Context context) {
//Synthetic comment -- @@ -47,15 +43,6 @@
R.drawable.balloons);
}

private static void setContrast(ColorMatrix cm, float contrast) {
float scale = contrast + 1.f;
float translate = (-.5f * scale + .5f) * 255.f;
//Synthetic comment -- @@ -78,7 +65,6 @@

private static void setContrastScaleOnly(ColorMatrix cm, float contrast) {
float scale = contrast + 1.f;
cm.set(new float[] {
scale, 0, 0, 0, 0,
0, scale, 0, 0, 0,








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Compass.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Compass.java
//Synthetic comment -- index d2a9907..75317ef 100644

//Synthetic comment -- @@ -16,15 +16,13 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.view.View;
//Synthetic comment -- @@ -34,22 +32,21 @@
private static final String TAG = "Compass";

	private SensorManager mSensorManager;
	private Sensor mSensor;
private SampleView mView;
private float[] mValues;

    private final SensorEventListener mListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            if (Config.LOGD) Log.d(TAG,
                    "sensorChanged (" + event.values[0] + ", " + event.values[1] + ", " + event.values[2] + ")");
            mValues = event.values;
if (mView != null) {
mView.invalidate();
}
}

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
}
};

//Synthetic comment -- @@ -57,6 +54,7 @@
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);
mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
mView = new SampleView(this);
setContentView(mView);
}
//Synthetic comment -- @@ -66,9 +64,9 @@
{
if (Config.LOGD) Log.d(TAG, "onResume");
super.onResume();

        mSensorManager.registerListener(mListener, mSensor,
                SensorManager.SENSOR_DELAY_GAME);
}

@Override








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/CreateBitmap.java b/samples/ApiDemos/src/com/example/android/apis/graphics/CreateBitmap.java
//Synthetic comment -- index e3e5d9a..d6188d9 100644

//Synthetic comment -- @@ -16,13 +16,9 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

import java.io.ByteArrayOutputStream;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Cube.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Cube.java
//Synthetic comment -- index bb154eb..715e175 100644

//Synthetic comment -- @@ -88,10 +88,10 @@

public void draw(GL10 gl)
{
        gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
}

private IntBuffer   mVertexBuffer;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/CubeRenderer.java b/samples/ApiDemos/src/com/example/android/apis/graphics/CubeRenderer.java
//Synthetic comment -- index 0f15f91..ac0ae27 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/DensityActivity.java b/samples/ApiDemos/src/com/example/android/apis/graphics/DensityActivity.java
//Synthetic comment -- index 10c42a7..58c3b37 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import com.example.android.apis.R;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
//Synthetic comment -- @@ -34,8 +33,6 @@
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/DrawPoints.java b/samples/ApiDemos/src/com/example/android/apis/graphics/DrawPoints.java
//Synthetic comment -- index cbe6373..c5cf77a 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/FingerPaint.java b/samples/ApiDemos/src/com/example/android/apis/graphics/FingerPaint.java
//Synthetic comment -- index 867da4c..27786b5 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -57,10 +56,6 @@
}

public class MyView extends View {
private Bitmap  mBitmap;
private Canvas  mCanvas;
private Path    mPath;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/GradientDrawable1.java b/samples/ApiDemos/src/com/example/android/apis/graphics/GradientDrawable1.java
//Synthetic comment -- index eb6d47d..fa7a6b7 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
// class is in a sub-package.
import com.example.android.apis.R;

import android.os.Bundle;

public class GradientDrawable1 extends GraphicsActivity {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/GraphicsActivity.java b/samples/ApiDemos/src/com/example/android/apis/graphics/GraphicsActivity.java
//Synthetic comment -- index 023c0d7..03c7198 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

class GraphicsActivity extends Activity {
@Override








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Layers.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Layers.java
//Synthetic comment -- index d9f5db0..1ea23ae 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/MeasureText.java b/samples/ApiDemos/src/com/example/android/apis/graphics/MeasureText.java
//Synthetic comment -- index e159efe..2b83737 100644

//Synthetic comment -- @@ -16,13 +16,9 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

public class MeasureText extends GraphicsActivity {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/PathEffects.java b/samples/ApiDemos/src/com/example/android/apis/graphics/PathEffects.java
//Synthetic comment -- index 80ddf38..73ee111 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/PathFillTypes.java b/samples/ApiDemos/src/com/example/android/apis/graphics/PathFillTypes.java
//Synthetic comment -- index 78dba26..5decd3e 100644

//Synthetic comment -- @@ -20,11 +20,9 @@
// class is in a sub-package.
//import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.View;

public class PathFillTypes extends GraphicsActivity {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Patterns.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Patterns.java
//Synthetic comment -- index d2a51ff..5d37192 100644

//Synthetic comment -- @@ -16,11 +16,9 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.*;









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Pictures.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Pictures.java
//Synthetic comment -- index 1bd0a8c..d8e2df3 100644

//Synthetic comment -- @@ -16,13 +16,11 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.View;

import java.io.*;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/PolyToPoly.java b/samples/ApiDemos/src/com/example/android/apis/graphics/PolyToPoly.java
//Synthetic comment -- index 15d92de..d9062e0 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
// class is in a sub-package.
//import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -74,8 +73,6 @@
}

@Override protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);

canvas.save();








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Regions.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Regions.java
//Synthetic comment -- index 833274b..7267675 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/RoundRects.java b/samples/ApiDemos/src/com/example/android/apis/graphics/RoundRects.java
//Synthetic comment -- index b0ff0359..008f299 100644

//Synthetic comment -- @@ -16,14 +16,10 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.Bundle;
import android.view.*;

public class RoundRects extends GraphicsActivity {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/ScaleToFit.java b/samples/ApiDemos/src/com/example/android/apis/graphics/ScaleToFit.java
//Synthetic comment -- index f55e55b..21998ad 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -93,8 +92,6 @@
}

@Override protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);

canvas.translate(10, 10);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/SensorTest.java b/samples/ApiDemos/src/com/example/android/apis/graphics/SensorTest.java
//Synthetic comment -- index ed5b5ae..87e0461 100644

//Synthetic comment -- @@ -16,22 +16,21 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Config;
import android.view.View;

public class SensorTest extends GraphicsActivity {
    private final String TAG = "SensorTest";

    private SensorManager mSensorManager;
    private Sensor mSensor;
private SampleView mView;
private float[] mValues;

//Synthetic comment -- @@ -76,29 +75,31 @@
}
};

    private final SensorEventListener mListener = new SensorEventListener() {

private final float[] mScale = new float[] { 2, 2.5f, 0.5f };   // accel
private float[] mPrev = new float[3];
        private long mLastGestureTime;

        public void onSensorChanged(SensorEvent event) {
boolean show = false;
float[] diff = new float[3];

for (int i = 0; i < 3; i++) {
                diff[i] = Math.round(mScale[i] * (event.values[i] - mPrev[i]) * 0.45f);
if (Math.abs(diff[i]) > 0) {
show = true;
}
                mPrev[i] = event.values[i];
}

if (show) {
// only shows if we think the delta is big enough, in an attempt
// to detect "serious" moves left/right or up/down
                android.util.Log.e(TAG, "sensorChanged " + event.sensor.getName() +
                        " (" + event.values[0] + ", " + event.values[1] + ", " +
                        event.values[2] + ")" + " diff(" + diff[0] +
                        " " + diff[1] + " " + diff[2] + ")");
}

long now = android.os.SystemClock.uptimeMillis();
//Synthetic comment -- @@ -128,12 +129,8 @@
}
}
}

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
}
};

//Synthetic comment -- @@ -141,28 +138,24 @@
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);
mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
mView = new SampleView(this);
setContentView(mView);
        if (Config.LOGD) android.util.Log.d(TAG, "create " + mSensorManager);
}

@Override
protected void onResume() {
super.onResume();
        mSensorManager.registerListener(mListener, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        if (Config.LOGD) android.util.Log.d(TAG, "resume " + mSensorManager);
}

@Override
protected void onStop() {
mSensorManager.unregisterListener(mListener);
super.onStop();
        if (Config.LOGD) android.util.Log.d(TAG, "stop " + mSensorManager);
}

private class SampleView extends View {
//Synthetic comment -- @@ -182,7 +175,8 @@
mPath.close();
}

        @Override
        protected void onDraw(Canvas canvas) {
Paint paint = mPaint;

canvas.drawColor(Color.WHITE);
//Synthetic comment -- @@ -216,4 +210,3 @@
}
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/ShapeDrawable1.java b/samples/ApiDemos/src/com/example/android/apis/graphics/ShapeDrawable1.java
//Synthetic comment -- index 6d450bb..acde814 100644

//Synthetic comment -- @@ -16,16 +16,12 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.*;
import android.os.Bundle;
import android.view.*;

public class ShapeDrawable1 extends GraphicsActivity {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Sweep.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Sweep.java
//Synthetic comment -- index dc127fd..bf45e5a 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
// class is in a sub-package.
//import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/TextAlign.java b/samples/ApiDemos/src/com/example/android/apis/graphics/TextAlign.java
//Synthetic comment -- index 0576a7c..2cc72b1 100644

//Synthetic comment -- @@ -16,13 +16,9 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

public class TextAlign extends GraphicsActivity {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/TouchPaint.java b/samples/ApiDemos/src/com/example/android/apis/graphics/TouchPaint.java
//Synthetic comment -- index 0942852..75a189c 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/TouchRotateActivity.java b/samples/ApiDemos/src/com/example/android/apis/graphics/TouchRotateActivity.java
//Synthetic comment -- index c0f32a7..4133c04 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/TriangleRenderer.java b/samples/ApiDemos/src/com/example/android/apis/graphics/TriangleRenderer.java
//Synthetic comment -- index e5299b3..0433af1 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Typefaces.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Typefaces.java
//Synthetic comment -- index aefc311..e36162c 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/UnicodeChart.java b/samples/ApiDemos/src/com/example/android/apis/graphics/UnicodeChart.java
//Synthetic comment -- index 7ee99d0..9a9a624 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Vertices.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Vertices.java
//Synthetic comment -- index 1e61906..79d4006 100644

//Synthetic comment -- @@ -18,17 +18,11 @@

import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

public class Vertices extends GraphicsActivity {

@Override








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Xfermodes.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Xfermodes.java
//Synthetic comment -- index b9f8424..791013a 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/spritetext/SpriteTextRenderer.java b/samples/ApiDemos/src/com/example/android/apis/graphics/spritetext/SpriteTextRenderer.java
//Synthetic comment -- index 223300a..183a8ec 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/media/MediaPlayerDemo_Audio.java b/samples/ApiDemos/src/com/example/android/apis/media/MediaPlayerDemo_Audio.java
//Synthetic comment -- index b209ce9..90cc3ac 100644

//Synthetic comment -- @@ -92,7 +92,6 @@
@Override
protected void onDestroy() {
super.onDestroy();
if (mMediaPlayer != null) {
mMediaPlayer.release();
mMediaPlayer = null;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/os/MorseCode.java b/samples/ApiDemos/src/com/example/android/apis/os/MorseCode.java
//Synthetic comment -- index dafa192..5f91bac 100644

//Synthetic comment -- @@ -49,9 +49,6 @@
*/
public class MorseCode extends Activity
{
/** Our text view */
private TextView mTextView;









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/os/Sensors.java b/samples/ApiDemos/src/com/example/android/apis/os/Sensors.java
//Synthetic comment -- index 910961d..9863222 100644

//Synthetic comment -- @@ -20,9 +20,10 @@
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
//Synthetic comment -- @@ -47,13 +48,10 @@
* </table> 
*/
public class Sensors extends Activity {
private SensorManager mSensorManager;
private GraphView mGraphView;

    private class GraphView extends View implements SensorEventListener
{
private Bitmap  mBitmap;
private Paint   mPaint = new Paint();
//Synthetic comment -- @@ -172,29 +170,29 @@
}
}

        public void onSensorChanged(SensorEvent event) {
//Log.d(TAG, "sensor: " + sensor + ", x: " + values[0] + ", y: " + values[1] + ", z: " + values[2]);
synchronized (this) {
if (mBitmap != null) {
final Canvas canvas = mCanvas;
final Paint paint = mPaint;
                    if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
for (int i=0 ; i<3 ; i++) {
                            mOrientationValues[i] = event.values[i];
}
} else {
float deltaX = mSpeed;
float newX = mLastX + deltaX;

                        int j = (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) ? 1 : 0;
for (int i=0 ; i<3 ; i++) {
int k = i+j*3;
                            final float v = mYOffset + event.values[i] * mScale[j];
paint.setColor(mColors[k]);
canvas.drawLine(mLastX, mLastValues[k], newX, v, paint);
mLastValues[k] = v;
}
                        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
mLastX += mSpeed;
}
invalidate();
//Synthetic comment -- @@ -202,9 +200,7 @@
}
}

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
}
}

//Synthetic comment -- @@ -226,10 +222,14 @@
@Override
protected void onResume() {
super.onResume();
        mSensorManager.registerListener(mGraphView,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(mGraphView,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);
mSensorManager.registerListener(mGraphView, 
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
SensorManager.SENSOR_DELAY_FASTEST);
}









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/text/LogTextBox.java b/samples/ApiDemos/src/com/example/android/apis/text/LogTextBox.java
//Synthetic comment -- index c78b54b..09957c5 100644

//Synthetic comment -- @@ -20,13 +20,9 @@
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.text.method.MovementMethod;
import android.text.Editable;
import android.util.AttributeSet;

/**
* This is a TextView that is Editable and by default scrollable,
* like EditText without a cursor.








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/Animation2.java b/samples/ApiDemos/src/com/example/android/apis/view/Animation2.java
//Synthetic comment -- index b2236aa..041794e 100644

//Synthetic comment -- @@ -49,7 +49,7 @@
s.setOnItemSelectedListener(this);
}

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
switch (position) {

case 0:
//Synthetic comment -- @@ -79,7 +79,7 @@
}
}

    public void onNothingSelected(AdapterView<?> parent) {
}

private String[] mStrings = {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/Animation3.java b/samples/ApiDemos/src/com/example/android/apis/view/Animation3.java
//Synthetic comment -- index 11fc9ed..2cd7605 100644

//Synthetic comment -- @@ -50,7 +50,7 @@
s.setOnItemSelectedListener(this);
}

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
final View target = findViewById(R.id.target);
final View targetParent = (View) target.getParent();

//Synthetic comment -- @@ -96,6 +96,6 @@
target.startAnimation(a);
}

    public void onNothingSelected(AdapterView<?> parent) {
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete1.java b/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete1.java
//Synthetic comment -- index f4274e5..bec4a5d 100644

//Synthetic comment -- @@ -19,12 +19,9 @@
import com.example.android.apis.R;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.os.Bundle;

public class AutoComplete1 extends Activity {









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete6.java b/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete6.java
//Synthetic comment -- index 3573bfb..2c28d65 100644

//Synthetic comment -- @@ -19,12 +19,9 @@
import com.example.android.apis.R;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.os.Bundle;

public class AutoComplete6 extends Activity {









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/Buttons1.java b/samples/ApiDemos/src/com/example/android/apis/view/Buttons1.java
//Synthetic comment -- index e2f8cc8..a88ee30 100644

//Synthetic comment -- @@ -22,9 +22,6 @@

import android.app.Activity;
import android.os.Bundle;

/**
* A gallery of the different styles of buttons.








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/ExpandableList1.java b/samples/ApiDemos/src/com/example/android/apis/view/ExpandableList1.java
//Synthetic comment -- index 944db64..94d6c06 100644

//Synthetic comment -- @@ -24,7 +24,6 @@
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/Focus1.java b/samples/ApiDemos/src/com/example/android/apis/view/Focus1.java
//Synthetic comment -- index 86f6ee7..c816b31 100644

//Synthetic comment -- @@ -20,12 +20,10 @@

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ArrayAdapter;

/**
* Demonstrates the use of non-focusable views.
*/








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/Gallery1.java b/samples/ApiDemos/src/com/example/android/apis/view/Gallery1.java
//Synthetic comment -- index a539a5b..7aaaaef 100644

//Synthetic comment -- @@ -49,7 +49,7 @@

// Set a item click listener, and just Toast the clicked position
g.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
Toast.makeText(Gallery1.this, "" + position, Toast.LENGTH_SHORT).show();
}
});








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/Gallery2.java b/samples/ApiDemos/src/com/example/android/apis/view/Gallery2.java
//Synthetic comment -- index 2eea1ff..efce3cb 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.example.android.apis.view;

import android.app.Activity;
import android.database.Cursor;
import android.provider.Contacts.People;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/ImageSwitcher1.java b/samples/ApiDemos/src/com/example/android/apis/view/ImageSwitcher1.java
//Synthetic comment -- index f72b623..0e74ea5 100644

//Synthetic comment -- @@ -56,11 +56,11 @@
g.setOnItemSelectedListener(this);
}

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
mSwitcher.setImageResource(mImageIds[position]);
}

    public void onNothingSelected(AdapterView<?> parent) {
}

public View makeView() {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/RatingBar1.java b/samples/ApiDemos/src/com/example/android/apis/view/RatingBar1.java
//Synthetic comment -- index 97416d4..5fbf6dd 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.apis.R;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/ScrollView2.java b/samples/ApiDemos/src/com/example/android/apis/view/ScrollView2.java
//Synthetic comment -- index 89e4003..696937c 100644

//Synthetic comment -- @@ -20,12 +20,10 @@

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

/**
* Demonstrates wrapping a layout in a ScrollView.
*








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/TableLayout10.java b/samples/ApiDemos/src/com/example/android/apis/view/TableLayout10.java
//Synthetic comment -- index f1f8f24..f904f84 100644

//Synthetic comment -- @@ -19,15 +19,9 @@
import com.example.android.apis.R;

import android.app.Activity;
import android.os.Bundle;

public class TableLayout10 extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/TableLayout11.java b/samples/ApiDemos/src/com/example/android/apis/view/TableLayout11.java
//Synthetic comment -- index 770238f..09b19a2 100644

//Synthetic comment -- @@ -25,8 +25,6 @@
* <p>This example shows how to use horizontal gravity in a table layout.</p>
*/
public class TableLayout11 extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/TableLayout12.java b/samples/ApiDemos/src/com/example/android/apis/view/TableLayout12.java
//Synthetic comment -- index 14cbd0d..f3fe850 100644

//Synthetic comment -- @@ -25,8 +25,6 @@
* <p>This example shows how to use cell spanning in a table layout.</p>
*/
public class TableLayout12 extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/Tabs1.java b/samples/ApiDemos/src/com/example/android/apis/view/Tabs1.java
//Synthetic comment -- index 455969e..39f7e9b 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.view.LayoutInflater;

import com.example.android.apis.R;








