/*Removed unused imports, added Generic Type Arguments, get rid of depracated API calls

Change-Id:Ie0194287178860a6c74052ab9249ab7610ae9cd0*/
//Synthetic comment -- diff --git a/core/java/android/app/ActivityManagerNative.java b/core/java/android/app/ActivityManagerNative.java
//Synthetic comment -- index 3b8aee9..49d1a12 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package android.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
//Synthetic comment -- @@ -380,7 +381,7 @@
IThumbnailReceiver receiver = receiverBinder != null
? IThumbnailReceiver.Stub.asInterface(receiverBinder)
: null;
            List list = getTasks(maxNum, fl, receiver);
reply.writeNoException();
int N = list != null ? list.size() : -1;
reply.writeInt(N);
//Synthetic comment -- @@ -408,7 +409,7 @@
data.enforceInterface(IActivityManager.descriptor);
int maxNum = data.readInt();
int fl = data.readInt();
            List list = getServices(maxNum, fl);
reply.writeNoException();
int N = list != null ? list.size() : -1;
reply.writeInt(N);
//Synthetic comment -- @@ -1486,7 +1487,7 @@
reply.recycle();
return res;
}
    public List getTasks(int maxNum, int flags,
IThumbnailReceiver receiver) throws RemoteException {
Parcel data = Parcel.obtain();
Parcel reply = Parcel.obtain();
//Synthetic comment -- @@ -1496,10 +1497,10 @@
data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
mRemote.transact(GET_TASKS_TRANSACTION, data, reply, 0);
reply.readException();
        ArrayList list = null;
int N = reply.readInt();
if (N >= 0) {
            list = new ArrayList();
while (N > 0) {
ActivityManager.RunningTaskInfo info =
ActivityManager.RunningTaskInfo.CREATOR
//Synthetic comment -- @@ -1527,7 +1528,7 @@
reply.recycle();
return list;
}
    public List getServices(int maxNum, int flags) throws RemoteException {
Parcel data = Parcel.obtain();
Parcel reply = Parcel.obtain();
data.writeInterfaceToken(IActivityManager.descriptor);
//Synthetic comment -- @@ -1535,10 +1536,10 @@
data.writeInt(flags);
mRemote.transact(GET_SERVICES_TRANSACTION, data, reply, 0);
reply.readException();
        ArrayList list = null;
int N = reply.readInt();
if (N >= 0) {
            list = new ArrayList();
while (N > 0) {
ActivityManager.RunningServiceInfo info =
ActivityManager.RunningServiceInfo.CREATOR








//Synthetic comment -- diff --git a/core/java/android/app/ActivityThread.java b/core/java/android/app/ActivityThread.java
//Synthetic comment -- index 909620d..29aef7e 100644

//Synthetic comment -- @@ -2059,7 +2059,7 @@
= new ArrayList<Application>();
// set of instantiated backup agents, keyed by package name
final HashMap<String, BackupAgent> mBackupAgents = new HashMap<String, BackupAgent>();
    static final ThreadLocal sThreadLocal = new ThreadLocal();
Instrumentation mInstrumentation;
String mInstrumentationAppDir = null;
String mInstrumentationAppPackage = null;








//Synthetic comment -- diff --git a/core/java/android/app/AlarmManager.java b/core/java/android/app/AlarmManager.java
//Synthetic comment -- index 53c7935..1d02415 100644

//Synthetic comment -- @@ -16,10 +16,8 @@

package android.app;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.os.ServiceManager;

/**
* This class provides access to the system alarm services.  These allow you








//Synthetic comment -- diff --git a/core/java/android/app/ApplicationLoaders.java b/core/java/android/app/ApplicationLoaders.java
//Synthetic comment -- index 2e301c9..f1a95d3 100644

//Synthetic comment -- @@ -65,7 +65,7 @@
}
}

    private final HashMap mLoaders = new HashMap();

private static final ApplicationLoaders gApplicationLoaders
= new ApplicationLoaders();








//Synthetic comment -- diff --git a/core/java/android/app/DatePickerDialog.java b/core/java/android/app/DatePickerDialog.java
//Synthetic comment -- index 78bbb4f4..49a57b3 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
//Synthetic comment -- @@ -112,8 +111,8 @@
mCalendar = Calendar.getInstance();
updateTitle(mInitialYear, mInitialMonth, mInitialDay);

        setButton(context.getText(R.string.date_time_set), this);
        setButton2(context.getText(R.string.cancel), (OnClickListener) null);
setIcon(R.drawable.ic_dialog_time);

LayoutInflater inflater = 








//Synthetic comment -- diff --git a/core/java/android/app/ExpandableListActivity.java b/core/java/android/app/ExpandableListActivity.java
//Synthetic comment -- index a2e048f..b30cb75 100644

//Synthetic comment -- @@ -27,7 +27,6 @@
import android.widget.ExpandableListView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

import java.util.Map;









//Synthetic comment -- diff --git a/core/java/android/app/IActivityManager.java b/core/java/android/app/IActivityManager.java
//Synthetic comment -- index 9f505ac..88efd66 100644

//Synthetic comment -- @@ -118,11 +118,11 @@
public void activityDestroyed(IBinder token) throws RemoteException;
public String getCallingPackage(IBinder token) throws RemoteException;
public ComponentName getCallingActivity(IBinder token) throws RemoteException;
    public List getTasks(int maxNum, int flags,
IThumbnailReceiver receiver) throws RemoteException;
public List<ActivityManager.RecentTaskInfo> getRecentTasks(int maxNum,
int flags) throws RemoteException;
    public List getServices(int maxNum, int flags) throws RemoteException;
public List<ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState()
throws RemoteException;
public void moveTaskToFront(int task) throws RemoteException;








//Synthetic comment -- diff --git a/core/java/android/app/Instrumentation.java b/core/java/android/app/Instrumentation.java
//Synthetic comment -- index b8c3aa3..028214f 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PerformanceCollector;
import android.os.RemoteException;
import android.os.Debug;
//Synthetic comment -- @@ -33,7 +34,6 @@
import android.os.SystemClock;
import android.os.ServiceManager;
import android.util.AndroidRuntimeException;
import android.util.Config;
import android.util.Log;
import android.view.IWindowManager;
import android.view.KeyCharacterMap;
//Synthetic comment -- @@ -41,7 +41,6 @@
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.util.ArrayList;
//Synthetic comment -- @@ -384,7 +383,7 @@
final ActivityWaiter aw = new ActivityWaiter(intent);

if (mWaitingActivities == null) {
                mWaitingActivities = new ArrayList();
}
mWaitingActivities.add(aw);

//Synthetic comment -- @@ -594,7 +593,7 @@
public void addMonitor(ActivityMonitor monitor) {
synchronized (mSync) {
if (mActivityMonitors == null) {
                mActivityMonitors = new ArrayList();
}
mActivityMonitors.add(monitor);
}
//Synthetic comment -- @@ -1385,7 +1384,8 @@
Context instrContext, Context appContext, ComponentName component, 
IInstrumentationWatcher watcher) {
mThread = thread;
        mMessageQueue = mThread.getLooper().myQueue();
mInstrContext = instrContext;
mAppContext = appContext;
mComponent = component;








//Synthetic comment -- diff --git a/core/java/android/app/ListActivity.java b/core/java/android/app/ListActivity.java
//Synthetic comment -- index 19b99c8..7444c7f 100644

//Synthetic comment -- @@ -18,9 +18,7 @@

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;








//Synthetic comment -- diff --git a/core/java/android/app/Notification.java b/core/java/android/app/Notification.java
//Synthetic comment -- index be5a7d3..718a131 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;








//Synthetic comment -- diff --git a/core/java/android/app/ResultInfo.java b/core/java/android/app/ResultInfo.java
//Synthetic comment -- index 48a0fc2..5e0867c 100644

//Synthetic comment -- @@ -17,12 +17,8 @@
package android.app;

import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Bundle;

import java.util.Map;

/**
* {@hide}








//Synthetic comment -- diff --git a/core/java/android/app/SearchDialog.java b/core/java/android/app/SearchDialog.java
//Synthetic comment -- index e5a769b..be4fa0e 100644

//Synthetic comment -- @@ -1336,7 +1336,7 @@
} else {
// If the intent was created from a suggestion, it will always have an explicit
// component here.
                Log.i(LOG_TAG, "Starting (as ourselves) " + intent.toURI());
getContext().startActivity(intent);
// If the search switches to a different activity,
// SearchDialogWrapper#performActivityResuming
//Synthetic comment -- @@ -1418,7 +1418,7 @@
String resultWho = null;
int requestCode = -1;
boolean onlyIfNeeded = false;
            Log.i(LOG_TAG, "Starting (uid " + uid + ", " + packageName + ") " + intent.toURI());
int result = ActivityManagerNative.getDefault().startActivityInPackage(
uid, intent, resolvedType, resultTo, resultWho, requestCode, onlyIfNeeded);
checkStartActivityResult(result, intent);








//Synthetic comment -- diff --git a/core/java/android/app/TimePickerDialog.java b/core/java/android/app/TimePickerDialog.java
//Synthetic comment -- index 002b01f..230ec8a 100644

//Synthetic comment -- @@ -99,8 +99,8 @@
mCalendar = Calendar.getInstance();
updateTitle(mInitialHourOfDay, mInitialMinute);

        setButton(context.getText(R.string.date_time_set), this);
        setButton2(context.getText(R.string.cancel), (OnClickListener) null);
setIcon(R.drawable.ic_dialog_time);

LayoutInflater inflater = 







