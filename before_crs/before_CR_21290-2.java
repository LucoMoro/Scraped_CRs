/*SDK manager fix for "force create AVD"

In the SDK Manager, creating an AVD would fail if there
was some file or folder in the way (in the .android/avd folder)
yet no AVD was loaded properly from it. This detects it,
properly notifies the user and makes "force create AVD"
work in this case.

Change-Id:Ie0abc383fef568c1a7e98c14eb7d48fbc5d66616*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 1fe6d97..5fb1132 100644

//Synthetic comment -- @@ -923,8 +923,7 @@
if (paramFolderPath != null) {
avdFolder = new File(paramFolderPath);
} else {
                avdFolder = new File(AndroidLocation.getFolder() + AndroidLocation.FOLDER_AVD,
                        avdName + AvdManager.AVD_FOLDER_EXTENSION);
}

// Validate skin is either default (empty) or NNNxMMM or a valid skin name.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 9bca42b..2471500 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import com.android.sdklib.internal.avd.AvdManager.AvdInfo.AvdStatus;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.io.FileWrapper;

import java.io.BufferedReader;
import java.io.File;
//Synthetic comment -- @@ -157,7 +158,26 @@

public final static String HARDWARE_INI = "hardware.ini"; //$NON-NLS-1$

    /** An immutable structure describing an Android Virtual Device. */
public static final class AvdInfo implements Comparable<AvdInfo> {

/**
//Synthetic comment -- @@ -255,12 +275,27 @@
}

/**
* Helper method that returns the .ini {@link File} for a given AVD name.
* @throws AndroidLocationException if there's a problem getting android root directory.
*/
public static File getIniFile(String name) throws AndroidLocationException {
            String avdRoot;
            avdRoot = getBaseAvdFolder();
return new File(avdRoot, name + INI_EXTENSION);
}

//Synthetic comment -- @@ -485,6 +520,53 @@
}

/**
* Reloads the AVD list.
* @param log the log object to receive action logs. Cannot be null.
* @throws AndroidLocationException if there was an error finding the location of the
//Synthetic comment -- @@ -564,7 +646,7 @@
}

// actually write the ini file
            iniFile = createAvdIniFile(name, avdFolder, target);

// writes the userdata.img in it.
String imagePath = target.getPath(IAndroidTarget.IMAGES);
//Synthetic comment -- @@ -958,13 +1040,23 @@
* @param name of the AVD.
* @param avdFolder path for the data folder of the AVD.
* @param target of the AVD.
* @throws AndroidLocationException if there's a problem getting android root directory.
* @throws IOException if {@link File#getAbsolutePath()} fails.
*/
    private File createAvdIniFile(String name, File avdFolder, IAndroidTarget target)
throws AndroidLocationException, IOException {
HashMap<String, String> values = new HashMap<String, String>();
File iniFile = AvdInfo.getIniFile(name);
values.put(AVD_INFO_PATH, avdFolder.getAbsolutePath());
values.put(AVD_INFO_TARGET, target.hashString());
writeIniFile(iniFile, values);
//Synthetic comment -- @@ -980,7 +1072,10 @@
* @throws IOException if {@link File#getAbsolutePath()} fails.
*/
private File createAvdIniFile(AvdInfo info) throws AndroidLocationException, IOException {
        return createAvdIniFile(info.getName(), new File(info.getPath()), info.getTarget());
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 7197ecd..53a6c76 100644

//Synthetic comment -- @@ -16,20 +16,21 @@

package com.android.sdkuilib.internal.widgets;

import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.io.FileWrapper;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
//Synthetic comment -- @@ -73,8 +74,8 @@
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;

/**
//Synthetic comment -- @@ -153,8 +154,8 @@
public void modifyText(ModifyEvent e) {
String name = mAvdName.getText().trim();
if (mEditAvdInfo == null || !name.equals(mEditAvdInfo.getName())) {
                AvdInfo avdMatch = mAvdManager.getAvd(name, false /*validAvdOnly*/);
                if (avdMatch != null) {
// If we're changing the state from disabled to enabled, make sure
// to uncheck the button, to force the user to voluntarily re-enforce it.
// This happens when editing an existing AVD and changing the name from
//Synthetic comment -- @@ -953,24 +954,47 @@
String height = mSkinSizeHeight.getText(); // rejects non digit.

if (width.length() == 0 || height.length() == 0) {
                    error = "Skin size is incorrect.\nBoth dimensions must be > 0";
}
}
}

// Check for duplicate AVD name
        if (isCreate && hasAvdName && error == null) {
            AvdInfo avdMatch = mAvdManager.getAvd(avdName, false /*validAvdOnly*/);
            if (avdMatch != null && !mForceCreation.getSelection()) {
error = String.format(
"The AVD name '%s' is already used.\n" +
"Check \"Override the existing AVD\" to delete the existing one.",
avdName);
}
}

if (error == null && mEditAvdInfo != null && isCreate) {
            warning = String.format("The AVD '%1$s' will be duplicated into '%2$s'",
mEditAvdInfo.getName(),
avdName);
}
//Synthetic comment -- @@ -1153,9 +1177,7 @@

File avdFolder = null;
try {
            avdFolder = new File(
                    AndroidLocation.getFolder() + AndroidLocation.FOLDER_AVD,
                    avdName + AvdManager.AVD_FOLDER_EXTENSION);
} catch (AndroidLocationException e) {
return false;
}







