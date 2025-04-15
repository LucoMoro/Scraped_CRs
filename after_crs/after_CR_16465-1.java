/*Removing @Override annotations to fix the CTS build, which is still 1.5-based.

Change-Id:Ifd045166d88c2f606def025f6f88a49ca8caef9d*/




//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/CtsVerifierActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/CtsVerifierActivity.java
//Synthetic comment -- index 9c0566e..c73e3f9 100644

//Synthetic comment -- @@ -23,9 +23,8 @@

/** {@link Activity} that displays an introduction to the verifier. */
public class CtsVerifierActivity extends Activity {
    
/** Called when the activity is first created. */
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.main);








//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/TestListActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/TestListActivity.java
//Synthetic comment -- index 143f44d..1300ddd 100644

//Synthetic comment -- @@ -39,7 +39,6 @@
/** Activities implementing {@link Intent#ACTION_MAIN} and this will appear in the list. */
static final String CATEGORY_MANUAL_TEST = "android.cts.intent.category.MANUAL_TEST";

protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setListAdapter(new TestListAdapter(this));
//Synthetic comment -- @@ -47,7 +46,6 @@


/** Launch the activity when its {@link ListView} item is clicked. */
protected void onListItemClick(ListView listView, View view, int position, long id) {
super.onListItemClick(listView, view, position, id);
Intent intent = getIntent(position);








//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/features/FeatureSummaryActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/features/FeatureSummaryActivity.java
//Synthetic comment -- index d448616..5f96217 100644

//Synthetic comment -- @@ -103,7 +103,6 @@
new Feature(PackageManager.FEATURE_WIFI, false),
};

public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.fs_main);








//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/sensors/AccelerometerTestActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/sensors/AccelerometerTestActivity.java
//Synthetic comment -- index 4de1465..df01329 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.cts.verifier.sensors;

import android.app.Activity;
import android.hardware.Sensor;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

//Synthetic comment -- @@ -28,21 +29,18 @@
public class AccelerometerTestActivity extends Activity {
private GLSurfaceView mGLSurfaceView;

protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setRenderer(new SensorTestRenderer(this, Sensor.TYPE_ACCELEROMETER));
setContentView(mGLSurfaceView);
}

protected void onPause() {
super.onPause();
mGLSurfaceView.onPause();
}

protected void onResume() {
super.onResume();
mGLSurfaceView.onResume();








//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/suid/SuidBinariesActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/suid/SuidBinariesActivity.java
//Synthetic comment -- index 9f973bc..5f94990 100644

//Synthetic comment -- @@ -21,7 +21,6 @@

public class SuidBinariesActivity extends Activity {

protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
}








//Synthetic comment -- diff --git a/apps/CtsVerifier/tests/src/com/android/cts/verifier/CtsVerifierActivityTest.java b/apps/CtsVerifier/tests/src/com/android/cts/verifier/CtsVerifierActivityTest.java
//Synthetic comment -- index c3e01b2..e564e19 100644

//Synthetic comment -- @@ -39,7 +39,6 @@
super(CtsVerifierActivity.class);
}

protected void setUp() throws Exception {
super.setUp();
mActivity = getActivity();








//Synthetic comment -- diff --git a/apps/CtsVerifier/tests/src/com/android/cts/verifier/TestListActivityTest.java b/apps/CtsVerifier/tests/src/com/android/cts/verifier/TestListActivityTest.java
//Synthetic comment -- index 5175f33..d13f643 100644

//Synthetic comment -- @@ -36,7 +36,6 @@
super(TestListActivity.class);
}

protected void setUp() throws Exception {
super.setUp();
mActivity = getActivity();







