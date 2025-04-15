/*Removed Calls to deprecated APIs and unused Imports

Change-Id:Ib26783ca1d6c345cc91aa3ab5b9654f5316c78a0*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/app/ExternalMediaFormatActivity.java b/core/java/com/android/internal/app/ExternalMediaFormatActivity.java
//Synthetic comment -- index 000f6c4..9ed506c 100644

//Synthetic comment -- @@ -23,13 +23,10 @@
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IMountService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.Environment;
import android.util.Log;

/**
//Synthetic comment -- @@ -38,7 +35,7 @@
*/
public class ExternalMediaFormatActivity extends AlertActivity implements DialogInterface.OnClickListener {

    private static final int POSITIVE_BUTTON = AlertDialog.BUTTON_POSITIVE;

/** Used to detect when the media state changes, in case we need to call finish() */
private BroadcastReceiver mStorageReceiver = new BroadcastReceiver() {








//Synthetic comment -- diff --git a/core/java/com/android/internal/app/RingtonePickerActivity.java b/core/java/com/android/internal/app/RingtonePickerActivity.java
//Synthetic comment -- index 5a0fea3..5ffb136 100644

//Synthetic comment -- @@ -223,7 +223,7 @@
* On click of Ok/Cancel buttons
*/
public void onClick(DialogInterface dialog, int which) {
        boolean positiveResult = which == DialogInterface.BUTTON_POSITIVE;

// Stop playing the previous ringtone
mRingtoneManager.stopPreviousRingtone();








//Synthetic comment -- diff --git a/core/java/com/android/internal/app/UsbStorageActivity.java b/core/java/com/android/internal/app/UsbStorageActivity.java
//Synthetic comment -- index b8a2136..78786b3 100644

//Synthetic comment -- @@ -23,9 +23,7 @@
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IMountService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.widget.Toast;
//Synthetic comment -- @@ -37,7 +35,7 @@
*/
public class UsbStorageActivity extends AlertActivity implements DialogInterface.OnClickListener {

    private static final int POSITIVE_BUTTON = AlertDialog.BUTTON_POSITIVE;

/** Used to detect when the USB cable is unplugged, so we can call finish() */
private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {








//Synthetic comment -- diff --git a/core/java/com/android/internal/app/UsbStorageStopActivity.java b/core/java/com/android/internal/app/UsbStorageStopActivity.java
//Synthetic comment -- index 557a523..de701ce 100644

//Synthetic comment -- @@ -23,9 +23,7 @@
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IMountService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.widget.Toast;
//Synthetic comment -- @@ -36,7 +34,7 @@
*/
public class UsbStorageStopActivity extends AlertActivity implements DialogInterface.OnClickListener {

    private static final int POSITIVE_BUTTON = AlertDialog.BUTTON_POSITIVE;

/** Used to detect when the USB cable is unplugged, so we can call finish() */
private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {







