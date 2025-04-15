/*Phone: Handle NullPointerException for Call Waiting functionality.

Display the processing dialog as soon as user enters "Additional
Call Settings" screen.This will avoid the user from clicking
on the checkbox, which is resulting in null pointer exception.

Change-Id:I9186ca2561e7b5e2dfb803cb8897a51ec797f5fb*/
//Synthetic comment -- diff --git a/src/com/android/phone/TimeConsumingPreferenceActivity.java b/src/com/android/phone/TimeConsumingPreferenceActivity.java
//Synthetic comment -- index 94568f2..5c2ae1b 100644

//Synthetic comment -- @@ -6,6 +6,7 @@
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
//Synthetic comment -- @@ -106,6 +107,12 @@
}

@Override
public void onResume() {
super.onResume();
mIsForeground = true;







