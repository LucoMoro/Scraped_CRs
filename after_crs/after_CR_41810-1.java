/*Show a Toast on a successful application uninstall

Instead of waiting for the user to tap OK, just show a Toast
and get out of the way. Does not change anything for
unsuccessful uninstall attempts

Change-Id:I32c2097405b4c4f514224b7561b83175a1c882fb*/




//Synthetic comment -- diff --git a/src/com/android/packageinstaller/UninstallAppProgress.java b/src/com/android/packageinstaller/UninstallAppProgress.java
//Synthetic comment -- index 3dfa80f..a81d23d 100755

//Synthetic comment -- @@ -31,6 +31,8 @@
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

/**
* This activity corresponds to a download progress screen that is displayed 
//Synthetic comment -- @@ -74,7 +76,11 @@
switch (msg.arg1) {
case PackageManager.DELETE_SUCCEEDED:
statusText = R.string.uninstall_done;
                            // Show a Toast and finish the activity
                            Context ctx = getBaseContext();
                            Toast.makeText(ctx, statusText, Toast.LENGTH_LONG).show();
                            setResultAndFinish(mResultCode);
                            return;
case PackageManager.DELETE_FAILED_DEVICE_POLICY_MANAGER:
Log.d(TAG, "Uninstall failed because " + packageName
+ " is a device admin");







