/*logcat: fix potential NPE

Fix NPE that could happen if the device goes offline even before
logcat command is issued on it,

Change-Id:I334caae5e42d25eff396ce0b7b7a5e44c3ed4f61*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatReceiver.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatReceiver.java
//Synthetic comment -- index 2674e92..da3e86f 100644

//Synthetic comment -- @@ -19,8 +19,8 @@
import com.android.ddmlib.IDevice;
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.Log;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.MultiLineReceiver;

import org.eclipse.jface.preference.IPreferenceStore;

//Synthetic comment -- @@ -97,7 +97,7 @@
@Override
public void run() {
/* wait while the device comes online */
                while (mCurrentDevice != null && !mCurrentDevice.isOnline()) {
try {
Thread.sleep(DEVICE_POLL_INTERVAL_MSEC);
} catch (InterruptedException e) {
//Synthetic comment -- @@ -106,8 +106,10 @@
}

try {
                    if (mCurrentDevice != null) {
                        mCurrentDevice.executeShellCommand(LOGCAT_COMMAND,
mCurrentLogCatOutputReceiver, 0);
                    }
} catch (Exception e) {
/* There are 4 possible exceptions: TimeoutException,
* AdbCommandRejectedException, ShellCommandUnresponsiveException and







