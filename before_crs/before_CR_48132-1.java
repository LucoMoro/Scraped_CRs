/*ddmlib: Add controls for OpenGL tracing via jdwp

WIP

Change-Id:I99afd04eed00925a044e778e1172d5873a7e1ba4*/
//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/UIThread.java b/ddms/app/src/com/android/ddms/UIThread.java
//Synthetic comment -- index 8aaa806..9e99772 100644

//Synthetic comment -- @@ -186,6 +186,7 @@
private ToolItem mTBCauseGc;
private ToolItem mTBDumpHprof;
private ToolItem mTBProfiling;

private final class FilterStorage implements ILogFilterStorageManager {

//Synthetic comment -- @@ -1159,6 +1160,18 @@
}
});

new ToolItem(toolBar, SWT.SEPARATOR);

// add "kill VM" button; need to make this visually distinct from
//Synthetic comment -- @@ -1722,6 +1735,14 @@
mTBProfiling.setImage(mTracingStartImage);
mTBProfiling.setToolTipText("Start Method Profiling (not supported by this VM)");
}
} else {
// list is empty, disable these
mTBShowThreadUpdates.setSelection(false);








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/Client.java b/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/Client.java
//Synthetic comment -- index 5b03462..d6e623e 100644

//Synthetic comment -- @@ -271,6 +271,21 @@
}
}

/**
* Sends a request to the VM to send the enable status of the method profiling.
* This is asynchronous.








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/ClientData.java b/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/ClientData.java
//Synthetic comment -- index ff83c37..f490c1a 100644

//Synthetic comment -- @@ -130,6 +130,12 @@
public final static String FEATURE_PROFILING_STREAMING = "method-trace-profiling-streaming"; //$NON-NLS-1$

/**
* String for feature allowing to dump hprof files
* @see #hasFeature(String)
*/








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/HandleOpenGlTracing.java b/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/HandleOpenGlTracing.java
new file mode 100644
//Synthetic comment -- index 0000000..d69d438

//Synthetic comment -- @@ -0,0 +1,41 @@








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/DevicePanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/DevicePanel.java
//Synthetic comment -- index a24b8a0..ddb1b02 100644

//Synthetic comment -- @@ -452,6 +452,12 @@
}
}

public void setEnabledHeapOnSelectedClient(boolean enable) {
if (mCurrentClient != null) {
mCurrentClient.setHeapUpdateEnabled(enable);







