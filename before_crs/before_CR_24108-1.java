/*Add a basic properties dialog.

Shows some information about the trace that can be of interest,
such as which timing mode (thread time or wall time) was used.

Change-Id:I54d136c8fe9e7ba28090547a5dad1ff8916e86db*/
//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/DmTraceReader.java b/traceview/src/com/android/traceview/DmTraceReader.java
//Synthetic comment -- index 260a865..dcf17a2 100644

//Synthetic comment -- @@ -45,6 +45,7 @@
private MethodData mTopLevel;
private ArrayList<Call> mCallList;
private ArrayList<Call> mSwitchList;
private HashMap<Integer, MethodData> mMethodMap;
private HashMap<Integer, ThreadData> mThreadMap;
private ThreadData[] mSortedThreads;
//Synthetic comment -- @@ -60,6 +61,7 @@
public DmTraceReader(String traceFileName, boolean regression) {
mTraceFileName = traceFileName;
mRegression = regression;
mMethodMap = new HashMap<Integer, MethodData>();
mThreadMap = new HashMap<Integer, ThreadData>();

//Synthetic comment -- @@ -366,11 +368,21 @@
parseMethod(line);
break;
case PARSE_OPTIONS:
break;
}
}
}

void parseThread(String line) {
String idStr = null;
String name = null;
//Synthetic comment -- @@ -599,4 +611,9 @@
public long getEndTime() {
return mGlobalEndTime;
}
}








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/MainWindow.java b/traceview/src/com/android/traceview/MainWindow.java
//Synthetic comment -- index b78b4f7..cf949a6 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import com.android.sdkstats.SdkStatsService;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
//Synthetic comment -- @@ -54,6 +56,8 @@
super(null);
mReader = reader;
mTraceName = traceName;
}

public void run() {
//Synthetic comment -- @@ -106,6 +110,30 @@
return sashForm1;
}

/**
* Convert the old two-file format into the current concatenated one.
*








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/PropertiesDialog.java b/traceview/src/com/android/traceview/PropertiesDialog.java
new file mode 100644
//Synthetic comment -- index 0000000..cbae0a8

//Synthetic comment -- @@ -0,0 +1,102 @@








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/TraceReader.java b/traceview/src/com/android/traceview/TraceReader.java
//Synthetic comment -- index ae75876..e936629 100644

//Synthetic comment -- @@ -49,6 +49,10 @@
return 0;
}

public ProfileProvider getProfileProvider() {
return null;
}







