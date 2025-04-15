/*replaced deprecated setButton Methods
replaced deprecated String Constructor

Change-Id:I7031b8ddc80b9847af9933b05fe4ca96405f7605*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 9e9552a..d1c5f7c 100644

//Synthetic comment -- @@ -48,6 +48,7 @@
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IIntentReceiver;
//Synthetic comment -- @@ -1145,7 +1146,7 @@
d.setCancelable(false);
d.setTitle("System UIDs Inconsistent");
d.setMessage("UIDs on the system are inconsistent, you need to wipe your data partition or your device will be unstable.");
                d.setButton(DialogInterface.BUTTON_POSITIVE, "I'm Feeling Lucky",
mHandler.obtainMessage(IM_FEELING_LUCKY_MSG));
mUidAlert = d;
d.show();
//Synthetic comment -- @@ -4627,7 +4628,7 @@
byte[] inp = new byte[8192];
int size = fs.read(inp);
fs.close();
            return new String(inp, 0, size);
} catch (java.io.IOException e) {
}
return "";








//Synthetic comment -- diff --git a/services/java/com/android/server/am/AppWaitingForDebuggerDialog.java b/services/java/com/android/server/am/AppWaitingForDebuggerDialog.java
//Synthetic comment -- index 0992d4d..3e60c55 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.server.am;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

//Synthetic comment -- @@ -49,7 +50,7 @@
text.append(" is waiting for the debugger to attach.");

setMessage(text.toString());
        setButton(DialogInterface.BUTTON_POSITIVE, "Force Close", mHandler.obtainMessage(1, app));
setTitle("Waiting For Debugger");
getWindow().setTitle("Waiting For Debugger: " + app.info.processName);
}








//Synthetic comment -- diff --git a/services/java/com/android/server/am/FactoryErrorDialog.java b/services/java/com/android/server/am/FactoryErrorDialog.java
//Synthetic comment -- index 2e25474..b19bb5ca 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.server.am;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

//Synthetic comment -- @@ -26,7 +27,8 @@
setCancelable(false);
setTitle(context.getText(com.android.internal.R.string.factorytest_failed));
setMessage(msg);
        setButton(DialogInterface.BUTTON_POSITIVE,
                context.getText(com.android.internal.R.string.factorytest_reboot),
mHandler.obtainMessage(0));
getWindow().setTitle("Factory Error");
}







