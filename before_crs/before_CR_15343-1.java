/*Replaced /sdcard with Environment.getExternalStorageDirectory()

Change-Id:I92bd31d67e0b68ec8ccce7153b58464cc45d154d*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 2dbdfab..e2cdce3 100644

//Synthetic comment -- @@ -79,6 +79,7 @@
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
//Synthetic comment -- @@ -1382,7 +1383,7 @@
// Depending on the location, there may be an
// extension already on the name or not
String fileName = new String(location);
                String dir = "/sdcard/download/";
String extension;
int index;
if ((index = fileName.indexOf(".")) == -1) {








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageUtils.java b/src/com/android/mms/ui/MessageUtils.java
//Synthetic comment -- index 421f6a8..2248148 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
import android.graphics.Bitmap.CompressFormat;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Sms;
//Synthetic comment -- @@ -833,7 +834,8 @@
* Debugging
*/
public static void writeHprofDataToFile(){
        String filename = "/sdcard/mms_oom_hprof_data";
try {
android.os.Debug.dumpHprofData(filename);
Log.i(TAG, "##### written hprof data to " + filename);







