/*Removed Calls to deprecated APIs and unused imports

Change-Id:Ibaaf3418ec2c97a99495eca7ea546af415370c3e*/




//Synthetic comment -- diff --git a/src/com/android/settings/ApplicationSettings.java b/src/com/android/settings/ApplicationSettings.java
//Synthetic comment -- index 6a8aa81..1df85dc 100644

//Synthetic comment -- @@ -75,7 +75,7 @@
}

public void onClick(DialogInterface dialog, int which) {
        if (dialog == mWarnInstallApps && which == DialogInterface.BUTTON_POSITIVE) {
setNonMarketAppsAllowed(true);
mToggleAppInstallation.setChecked(true);
}








//Synthetic comment -- diff --git a/src/com/android/settings/quicklaunch/QuickLaunchSettings.java b/src/com/android/settings/quicklaunch/QuickLaunchSettings.java
//Synthetic comment -- index fb9fbcd..ca5d3ab 100644

//Synthetic comment -- @@ -178,7 +178,7 @@
}

public void onClick(DialogInterface dialog, int which) {
        if (mClearDialogShortcut > 0 && which == AlertDialog.BUTTON_POSITIVE) {
// Clear the shortcut
clearShortcut(mClearDialogShortcut);
}








//Synthetic comment -- diff --git a/src/com/android/settings/vpn/VpnSettings.java b/src/com/android/settings/vpn/VpnSettings.java
//Synthetic comment -- index d380375..5f6a207 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.settings.vpn;

import com.android.settings.R;

import android.app.AlertDialog;
import android.app.Dialog;
//Synthetic comment -- @@ -38,13 +37,10 @@
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.os.Parcelable;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;
import android.security.Credentials;
//Synthetic comment -- @@ -53,7 +49,6 @@
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
//Synthetic comment -- @@ -67,12 +62,9 @@
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* The preference activity for configuring VPN settings.
//Synthetic comment -- @@ -101,8 +93,8 @@
private static final int CONTEXT_MENU_EDIT_ID = ContextMenu.FIRST + 2;
private static final int CONTEXT_MENU_DELETE_ID = ContextMenu.FIRST + 3;

    private static final int CONNECT_BUTTON = DialogInterface.BUTTON_POSITIVE;
    private static final int OK_BUTTON = DialogInterface.BUTTON_POSITIVE;

private static final int DIALOG_CONNECT = VpnManager.VPN_ERROR_LARGEST + 1;
private static final int DIALOG_SECRET_NOT_SET = DIALOG_CONNECT + 1;







