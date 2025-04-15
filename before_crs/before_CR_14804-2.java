/*Removed unused Imports, Added Type Arguments, removed some Warnings by using unused Methods and Variables

Change-Id:I39a4eb2b17f3411b17fcc49cbbbe396e89fab24a*/
//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/Animation.java b/samples/ApiDemos/src/com/example/android/apis/app/Animation.java
//Synthetic comment -- index 90831f5..5ba41c4 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
import com.example.android.apis.view.Controls1;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/ContactsFilter.java b/samples/ApiDemos/src/com/example/android/apis/app/ContactsFilter.java
//Synthetic comment -- index bb843e5..d5b3deb 100644

//Synthetic comment -- @@ -20,8 +20,6 @@
// class is in a sub-package.
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
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

import java.util.Map;

/**
* This is an example implementation of the {@link android.app.Instrumentation}
* class, allowing you to run tests against application code.  The








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/DefaultValues.java b/samples/ApiDemos/src/com/example/android/apis/app/DefaultValues.java
//Synthetic comment -- index 35bdf13..494a2ea 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import com.example.android.apis.ApiDemosApplication;
import com.example.android.apis.R;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/ForegroundService.java b/samples/ApiDemos/src/com/example/android/apis/app/ForegroundService.java
//Synthetic comment -- index 27708f4..6e71207 100644

//Synthetic comment -- @@ -43,9 +43,9 @@
static final String ACTION_FOREGROUND = "com.example.android.apis.FOREGROUND";
static final String ACTION_BACKGROUND = "com.example.android.apis.BACKGROUND";

    private static final Class[] mStartForegroundSignature = new Class[] {
int.class, Notification.class};
    private static final Class[] mStopForegroundSignature = new Class[] {
boolean.class};

private NotificationManager mNM;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/ForegroundServiceController.java b/samples/ApiDemos/src/com/example/android/apis/app/ForegroundServiceController.java
//Synthetic comment -- index a6a6ef3..4849a6b 100644

//Synthetic comment -- @@ -21,14 +21,12 @@
import com.example.android.apis.R;

import android.app.Activity;
import android.content.ComponentName;
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
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

/**








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/LocalSample.java b/samples/ApiDemos/src/com/example/android/apis/app/LocalSample.java
//Synthetic comment -- index 1bb26e9..0e4b66e 100644

//Synthetic comment -- @@ -20,8 +20,6 @@
// class is in a sub-package.
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
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

import java.util.Map;

/**
* This is an example implementation of the {@link android.app.Instrumentation}
* class demonstrating instrumentation against one of this application's sample








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/LocalService.java b/samples/ApiDemos/src/com/example/android/apis/app/LocalService.java
//Synthetic comment -- index 79324a4..815fa6b 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.util.Log;
import android.widget.Toast;

//Synthetic comment -- @@ -45,6 +44,10 @@
public class LocalService extends Service {
private NotificationManager mNM;

/**
* Class for clients to access.  Because we know this service always
* runs in the same process as its clients, we don't need to deal with
//Synthetic comment -- @@ -75,7 +78,7 @@
@Override
public void onDestroy() {
// Cancel the persistent notification.
        mNM.cancel(R.string.local_service_started);

// Tell the user we stopped.
Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
//Synthetic comment -- @@ -110,8 +113,7 @@
text, contentIntent);

// Send the notification.
        // We use a layout id because it is a unique number.  We use it later to cancel.
        mNM.notify(R.string.local_service_started, notification);
}
}









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/LocalServiceBinding.java b/samples/ApiDemos/src/com/example/android/apis/app/LocalServiceBinding.java
//Synthetic comment -- index ddcfad5..6f72d76 100644

//Synthetic comment -- @@ -30,7 +30,6 @@
import android.widget.Button;
import android.widget.Toast;


/**
* <p>Example of binding and unbinding to the {@link LocalService}.
* This demonstrates the implementation of a service which the client will
//Synthetic comment -- @@ -61,7 +60,7 @@
// service that we know is running in our own process, we can
// cast its IBinder to a concrete class and directly access it.
mBoundService = ((LocalService.LocalBinder)service).getService();
            
// Tell the user about this for our demo.
Toast.makeText(LocalServiceBinding.this, R.string.local_service_connected,
Toast.LENGTH_SHORT).show();








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/LocalServiceController.java b/samples/ApiDemos/src/com/example/android/apis/app/LocalServiceController.java
//Synthetic comment -- index 6041249..b663340 100644

//Synthetic comment -- @@ -21,14 +21,12 @@
import com.example.android.apis.R;

import android.app.Activity;
import android.content.ComponentName;
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
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/NotificationDisplay.java b/samples/ApiDemos/src/com/example/android/apis/app/NotificationDisplay.java
//Synthetic comment -- index a6c20ea..25b5d56 100644

//Synthetic comment -- @@ -24,14 +24,11 @@
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


/**
* Activity used by StatusBarNotification to show the notification to the user.
*/








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/NotifyingController.java b/samples/ApiDemos/src/com/example/android/apis/app/NotifyingController.java
//Synthetic comment -- index a0de699..b8b4e3f 100644

//Synthetic comment -- @@ -21,14 +21,12 @@
import com.example.android.apis.R;

import android.app.Activity;
import android.content.ComponentName;
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
import android.widget.RemoteViews;

/**
* This is an example of service that will update its status bar balloon 








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/PreferencesFromXml.java b/samples/ApiDemos/src/com/example/android/apis/app/PreferencesFromXml.java
//Synthetic comment -- index 23461c6..63bbac2 100644

//Synthetic comment -- @@ -18,9 +18,7 @@

import com.example.android.apis.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class PreferencesFromXml extends PreferenceActivity {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/QuickContactsDemo.java b/samples/ApiDemos/src/com/example/android/apis/app/QuickContactsDemo.java
//Synthetic comment -- index 1ee5742..004e568 100644

//Synthetic comment -- @@ -75,8 +75,6 @@
@Override
public void bindView(View view, Context context, Cursor cursor) {
final ContactListItemCache cache = (ContactListItemCache) view.getTag();
            TextView nameView = cache.nameView;
            QuickContactBadge photoView = cache.photoView;
// Set the name
cursor.copyStringToBuffer(SUMMARY_NAME_COLUMN_INDEX, cache.nameBuffer);
int size = cache.nameBuffer.sizeCopied;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/ReceiveResult.java b/samples/ApiDemos/src/com/example/android/apis/app/ReceiveResult.java
//Synthetic comment -- index 4e248b9..22ae7f4 100644

//Synthetic comment -- @@ -18,8 +18,6 @@

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import java.util.Map;

import com.example.android.apis.R;

import android.app.Activity;
//Synthetic comment -- @@ -31,8 +29,6 @@
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

/**
* Shows how an activity can send data to its launching activity when done.y.
* <p>This can be used, for example, to implement a dialog alowing the user to








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/RedirectGetter.java b/samples/ApiDemos/src/com/example/android/apis/app/RedirectGetter.java
//Synthetic comment -- index 982317c..531a8ed 100644

//Synthetic comment -- @@ -32,8 +32,11 @@
*/
public class RedirectGetter extends Activity
{
@Override
	protected void onCreate(Bundle savedInstanceState)
{
super.onCreate(savedInstanceState);

//Synthetic comment -- @@ -45,9 +48,12 @@

// The text being set.
mText = (TextView)findViewById(R.id.text);
}

    private final boolean loadPrefs()
{
// Retrieve the current redirect values.
// NOTE: because this preference is shared between multiple
//Synthetic comment -- @@ -58,10 +64,10 @@
mTextPref = preferences.getString("text", null);
if (mTextPref != null) {
mText.setText(mTextPref);
            return true;
}

        return false;
}

private OnClickListener mApplyListener = new OnClickListener()
//Synthetic comment -- @@ -79,8 +85,4 @@
finish();
}
};

    private String mTextPref;
    TextView mText;
}









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/RemoteService.java b/samples/ApiDemos/src/com/example/android/apis/app/RemoteService.java
//Synthetic comment -- index c0debbc..726d6d7 100644

//Synthetic comment -- @@ -29,8 +29,6 @@
import android.os.RemoteCallbackList;
import android.widget.Toast;

import java.util.HashMap;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import com.example.android.apis.R;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/ServiceStartArguments.java b/samples/ApiDemos/src/com/example/android/apis/app/ServiceStartArguments.java
//Synthetic comment -- index f4f9af1..be5487e 100644

//Synthetic comment -- @@ -49,7 +49,6 @@
*/
public class ServiceStartArguments extends Service {
private NotificationManager mNM;
    private Intent mInvokeIntent;
private volatile Looper mServiceLooper;
private volatile ServiceHandler mServiceHandler;

//Synthetic comment -- @@ -102,10 +101,6 @@

Toast.makeText(this, R.string.service_created,
Toast.LENGTH_SHORT).show();
        
        // This is who should be launched if the user selects our persistent
        // notification.
        mInvokeIntent = new Intent(this, ServiceStartArgumentsController.class);

// Start up the thread running the service.  Note that we create a
// separate thread because the service normally runs in the process's








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/SetWallpaperActivity.java b/samples/ApiDemos/src/com/example/android/apis/app/SetWallpaperActivity.java
//Synthetic comment -- index 8630b1c..f3d4ffb 100644

//Synthetic comment -- @@ -29,7 +29,6 @@
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/WallpaperActivity.java b/samples/ApiDemos/src/com/example/android/apis/app/WallpaperActivity.java
//Synthetic comment -- index 8d7f5a1..7b5eea2 100644

//Synthetic comment -- @@ -22,7 +22,6 @@

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

/**
* <h3>Wallpaper Activity</h3>







