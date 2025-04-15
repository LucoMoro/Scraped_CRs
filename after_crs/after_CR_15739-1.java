/*CAT: Use new Cat package from frameworks

As all stk related classes were moved to new Cat package this application
needs to use new package instead of old one.

Change-Id:I537c61318eff3677951af9639e9991425c418241*/




//Synthetic comment -- diff --git a/src/com/android/stk/StkApp.java b/src/com/android/stk/StkApp.java
//Synthetic comment -- index ebd52b1..0b1f208 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import android.app.Application;

import com.android.internal.telephony.cat.Duration;

/**
* Top-level Application class for STK app.








//Synthetic comment -- diff --git a/src/com/android/stk/StkAppInstaller.java b/src/com/android/stk/StkAppInstaller.java
//Synthetic comment -- index 305b0b3..d9e96e9 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.stk;

import com.android.internal.telephony.cat.CatLog;

import android.content.ComponentName;
import android.content.Context;
//Synthetic comment -- @@ -55,7 +55,7 @@
pm.setComponentEnabledSetting(cName, state,
PackageManager.DONT_KILL_APP);
} catch (Exception e) {
            CatLog.d("StkAppInstaller", "Could not change STK app state");
}
}
}








//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index f334f74..ec7fd50 100644

//Synthetic comment -- @@ -37,15 +37,15 @@
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.telephony.cat.AppInterface;
import com.android.internal.telephony.cat.Menu;
import com.android.internal.telephony.cat.Item;
import com.android.internal.telephony.cat.ResultCode;
import com.android.internal.telephony.cat.CatCmdMessage;
import com.android.internal.telephony.cat.CatCmdMessage.BrowserSettings;
import com.android.internal.telephony.cat.CatLog;
import com.android.internal.telephony.cat.CatResponseMessage;
import com.android.internal.telephony.cat.TextMessage;

import java.util.LinkedList;

//Synthetic comment -- @@ -61,8 +61,8 @@
private volatile ServiceHandler mServiceHandler;
private AppInterface mStkService;
private Context mContext = null;
    private CatCmdMessage mMainCmd = null;
    private CatCmdMessage mCurrentCmd = null;
private Menu mCurrentMenu = null;
private String lastSelectedItem = null;
private boolean mMenuIsVisibile = false;
//Synthetic comment -- @@ -123,9 +123,9 @@
private class DelayedCmd {
// members
int id;
        CatCmdMessage msg;

        DelayedCmd(int id, CatCmdMessage msg) {
this.id = id;
this.msg = msg;
}
//Synthetic comment -- @@ -134,7 +134,7 @@
@Override
public void onCreate() {
// Initialize members
        mStkService = com.android.internal.telephony.cat.CatService
.getInstance();

// NOTE mStkService is a singleton and continues to exist even if the GSMPhone is disposed
//Synthetic comment -- @@ -144,7 +144,7 @@
if ((mStkService == null)
&& (TelephonyManager.getDefault().getPhoneType()
!= TelephonyManager.PHONE_TYPE_CDMA)) {
            CatLog.d(this, " Unable to get Service handle");
return;
}

//Synthetic comment -- @@ -259,7 +259,7 @@
launchMenuActivity(null);
break;
case OP_CMD:
                CatCmdMessage cmdMsg = (CatCmdMessage) msg.obj;
// There are two types of commands:
// 1. Interactive - user's response is required.
// 2. Informative - display a message, no interaction with the user.
//Synthetic comment -- @@ -273,10 +273,10 @@
} else {
if (!mCmdInProgress) {
mCmdInProgress = true;
                        handleCmd((CatCmdMessage) msg.obj);
} else {
mCmdsQ.addLast(new DelayedCmd(OP_CMD,
                                (CatCmdMessage) msg.obj));
}
}
break;
//Synthetic comment -- @@ -302,7 +302,7 @@
}
break;
case OP_BOOT_COMPLETED:
                CatLog.d(this, "OP_BOOT_COMPLETED");
if (mMainCmd == null) {
StkAppInstaller.unInstall(mContext);
}
//Synthetic comment -- @@ -314,7 +314,7 @@
}
}

    private boolean isCmdInteractive(CatCmdMessage cmd) {
switch (cmd.getCmdType()) {
case SEND_DTMF:
case SEND_SMS:
//Synthetic comment -- @@ -371,7 +371,7 @@
}
}

    private void handleCmd(CatCmdMessage cmdMsg) {
if (cmdMsg == null) {
return;
}
//Synthetic comment -- @@ -379,7 +379,7 @@
mCurrentCmd = cmdMsg;
boolean waitForUsersResponse = true;

        CatLog.d(this, cmdMsg.getCmdType().name());
switch (cmdMsg.getCmdType()) {
case DISPLAY_TEXT:
TextMessage msg = cmdMsg.geTextMessage();
//Synthetic comment -- @@ -402,11 +402,11 @@
mMainCmd = mCurrentCmd;
mCurrentMenu = cmdMsg.getMenu();
if (removeMenu()) {
                CatLog.d(this, "Uninstall App");
mCurrentMenu = null;
StkAppInstaller.unInstall(mContext);
} else {
                CatLog.d(this, "Install App");
StkAppInstaller.install(mContext);
}
if (mMenuIsVisibile) {
//Synthetic comment -- @@ -452,14 +452,14 @@
if (mCurrentCmd == null) {
return;
}
        CatResponseMessage resMsg = new CatResponseMessage(mCurrentCmd);

// set result code
boolean helpRequired = args.getBoolean(HELP, false);

switch(args.getInt(RES_ID)) {
case RES_ID_MENU_SELECTION:
            CatLog.d(this, "RES_ID_MENU_SELECTION");
int menuSelection = args.getInt(MENU_SELECTION);
switch(mCurrentCmd.getCmdType()) {
case SET_UP_MENU:
//Synthetic comment -- @@ -475,7 +475,7 @@
}
break;
case RES_ID_INPUT:
            CatLog.d(this, "RES_ID_INPUT");
String input = args.getString(INPUT);
if (mCurrentCmd.geInput().yesNo) {
boolean yesNoSelection = input
//Synthetic comment -- @@ -491,7 +491,7 @@
}
break;
case RES_ID_CONFIRM:
            CatLog.d(this, "RES_ID_CONFIRM");
boolean confirmed = args.getBoolean(CONFIRMATION);
switch (mCurrentCmd.getCmdType()) {
case DISPLAY_TEXT:
//Synthetic comment -- @@ -519,15 +519,15 @@
resMsg.setResultCode(ResultCode.OK);
break;
case RES_ID_BACKWARD:
            CatLog.d(this, "RES_ID_BACKWARD");
resMsg.setResultCode(ResultCode.BACKWARD_MOVE_BY_USER);
break;
case RES_ID_END_SESSION:
            CatLog.d(this, "RES_ID_END_SESSION");
resMsg.setResultCode(ResultCode.UICC_SESSION_TERM_BY_USER);
break;
case RES_ID_TIMEOUT:
            CatLog.d(this, "RES_ID_TIMEOUT");
// GCF test-case 27.22.4.1.1 Expected Sequence 1.5 (DISPLAY TEXT,
// Clear message after delay, successful) expects result code OK.
// If the command qualifier specifies no user response is required
//Synthetic comment -- @@ -541,7 +541,7 @@
}
break;
default:
            CatLog.d(this, "Unknown result id");
return;
}
mStkService.onCmdResponse(resMsg);
//Synthetic comment -- @@ -760,7 +760,7 @@
return true;
}
} catch (NullPointerException e) {
            CatLog.d(this, "Unable to get Menu's items size");
return true;
}
return false;








//Synthetic comment -- diff --git a/src/com/android/stk/StkCmdReceiver.java b/src/com/android/stk/StkCmdReceiver.java
//Synthetic comment -- index 916ec26..f276611 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.stk;

import com.android.internal.telephony.cat.AppInterface;

import android.content.BroadcastReceiver;
import android.content.Context;
//Synthetic comment -- @@ -33,9 +33,9 @@
public void onReceive(Context context, Intent intent) {
String action = intent.getAction();

        if (action.equals(AppInterface.CAT_CMD_ACTION)) {
handleCommandMessage(context, intent);
        } else if (action.equals(AppInterface.CAT_SESSION_END_ACTION)) {
handleSessionEnd(context, intent);
}
}








//Synthetic comment -- diff --git a/src/com/android/stk/StkDialogActivity.java b/src/com/android/stk/StkDialogActivity.java
//Synthetic comment -- index 18972cc..3fd3ef7 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.stk;

import com.android.internal.telephony.cat.TextMessage;

import android.app.Activity;
import android.content.Intent;
//Synthetic comment -- @@ -126,7 +126,6 @@
@Override
public void onResume() {
super.onResume();
startTimeOut();
}









//Synthetic comment -- diff --git a/src/com/android/stk/StkInputActivity.java b/src/com/android/stk/StkInputActivity.java
//Synthetic comment -- index 65a4ad5..b6228fb 100644

//Synthetic comment -- @@ -36,9 +36,8 @@
import android.widget.EditText;
import android.widget.TextView.BufferType;

import com.android.internal.telephony.cat.FontSize;
import com.android.internal.telephony.cat.Input;

/**
* Display a request for a text input a long with a text edit form.








//Synthetic comment -- diff --git a/src/com/android/stk/StkMenuActivity.java b/src/com/android/stk/StkMenuActivity.java
//Synthetic comment -- index 4aeb58d..aac1a12 100644

//Synthetic comment -- @@ -31,9 +31,9 @@
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.internal.telephony.cat.Item;
import com.android.internal.telephony.cat.Menu;
import com.android.internal.telephony.cat.CatLog;

/**
* ListActivity used for displaying STK menus. These can be SET UP MENU and
//Synthetic comment -- @@ -76,7 +76,7 @@
public void onCreate(Bundle icicle) {
super.onCreate(icicle);

        CatLog.d(this, "onCreate");
// Remove the default title, customized one is used.
requestWindowFeature(Window.FEATURE_NO_TITLE);
// Set the layout for this activity.
//Synthetic comment -- @@ -95,7 +95,7 @@
protected void onNewIntent(Intent intent) {
super.onNewIntent(intent);

        CatLog.d(this, "onNewIntent");
initFromIntent(intent);
mAcceptUsersInput = true;
}
//Synthetic comment -- @@ -177,7 +177,7 @@
public void onDestroy() {
super.onDestroy();

        CatLog.d(this, "onDestroy");
}

@Override
//Synthetic comment -- @@ -306,11 +306,11 @@
item = mStkMenu.items.get(position);
} catch (IndexOutOfBoundsException e) {
if (StkApp.DBG) {
                    CatLog.d(this, "Invalid menu");
}
} catch (NullPointerException e) {
if (StkApp.DBG) {
                    CatLog.d(this, "Invalid menu");
}
}
}








//Synthetic comment -- diff --git a/src/com/android/stk/StkMenuAdapter.java b/src/com/android/stk/StkMenuAdapter.java
//Synthetic comment -- index 253b39a..c53b3ac 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.stk;

import com.android.internal.telephony.cat.Item;

import android.content.Context;
import android.view.LayoutInflater;








//Synthetic comment -- diff --git a/src/com/android/stk/ToneDialog.java b/src/com/android/stk/ToneDialog.java
//Synthetic comment -- index 9d55663..ba4a957 100644

//Synthetic comment -- @@ -27,8 +27,8 @@
import android.widget.ImageView;
import android.widget.TextView;

import com.android.internal.telephony.cat.TextMessage;
import com.android.internal.telephony.cat.ToneSettings;

/**
* Activity used for PLAY TONE command.








//Synthetic comment -- diff --git a/src/com/android/stk/TonePlayer.java b/src/com/android/stk/TonePlayer.java
//Synthetic comment -- index 913c5ad..e628c14 100644

//Synthetic comment -- @@ -20,7 +20,7 @@

import android.media.AudioManager;
import android.media.ToneGenerator;
import com.android.internal.telephony.cat.Tone;

/**
* Class that implements a tones player for the SIM toolkit application.







