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
                avdFolder = AvdManager.AvdInfo.getAvdFolder(avdName);
}

// Validate skin is either default (empty) or NNNxMMM or a valid skin name.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 9bca42b..2471500 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import com.android.sdklib.internal.avd.AvdManager.AvdInfo.AvdStatus;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.io.FileWrapper;
import com.android.util.Pair;

import java.io.BufferedReader;
import java.io.File;
//Synthetic comment -- @@ -157,7 +158,26 @@

public final static String HARDWARE_INI = "hardware.ini"; //$NON-NLS-1$

    /**
     * Status returned by {@link AvdManager#isAvdNameConflicting(String)}.
     */
    public static enum AvdConflict {
        /** There is no known conflict for the given AVD name. */
        NO_CONFLICT,
        /** The AVD name conflicts with an existing valid AVD. */
        CONFLICT_EXISTING_AVD,
        /** The AVD name conflicts with an existing invalid AVD. */
        CONFLICT_INVALID_AVD,
        /**
         * The AVD name does not conflict with any known AVD however there are
         * files or directory that would cause a conflict if this were to be created.
         */
        CONFLICT_EXISTING_PATH,
    }

    /**
     * An immutable structure describing an Android Virtual Device.
     */
public static final class AvdInfo implements Comparable<AvdInfo> {

/**
//Synthetic comment -- @@ -255,12 +275,27 @@
}

/**
         * Helper method that returns the default AVD folder that would be used for a given
         * AVD name <em>if and only if</em> the AVD was created with the default choice.
         * <p/>
         * Callers must NOT use this to "guess" the actual folder from an actual AVD since
         * the purpose of the AVD .ini file is to be able to change this folder.
         * <p/>
         * For an actual existing AVD, callers must use {@link #getPath()} instead.
         *
         * @throws AndroidLocationException if there's a problem getting android root directory.
         */
        public static File getAvdFolder(String name) throws AndroidLocationException {
            return new File(AndroidLocation.getFolder() + AndroidLocation.FOLDER_AVD,
                            name + AvdManager.AVD_FOLDER_EXTENSION);
        }

        /**
* Helper method that returns the .ini {@link File} for a given AVD name.
* @throws AndroidLocationException if there's a problem getting android root directory.
*/
public static File getIniFile(String name) throws AndroidLocationException {
            String avdRoot = getBaseAvdFolder();
return new File(avdRoot, name + INI_EXTENSION);
}

//Synthetic comment -- @@ -485,6 +520,53 @@
}

/**
     * Returns whether this AVD name would generate a conflict.
     *
     * @param name the name of the AVD to return
     * @return A pair of {@link AvdConflict} and the path or AVD name that conflicts.
     */
    public Pair<AvdConflict, String> isAvdNameConflicting(String name) {

        boolean ignoreCase = SdkConstants.currentPlatform() == SdkConstants.PLATFORM_WINDOWS;

        // Check whether we have a conflict with an existing or invalid AVD
        // known to the manager.
        synchronized (mAllAvdList) {
            for (AvdInfo info : mAllAvdList) {
                String name2 = info.getName();
                if (name2.equals(name) || (ignoreCase && name2.equalsIgnoreCase(name))) {
                    if (info.getStatus() == AvdStatus.OK) {
                        return Pair.of(AvdConflict.CONFLICT_EXISTING_AVD, name2);
                    } else {
                        return Pair.of(AvdConflict.CONFLICT_INVALID_AVD, name2);
                    }
                }
            }
        }

        // No conflict with known AVDs.
        // Are some existing files/folders in the way of creating this AVD?

        try {
            File file = AvdInfo.getIniFile(name);
            if (file.exists()) {
                return Pair.of(AvdConflict.CONFLICT_EXISTING_PATH, file.getPath());
            }

            file = AvdInfo.getAvdFolder(name);
            if (file.exists()) {
                return Pair.of(AvdConflict.CONFLICT_EXISTING_PATH, file.getPath());
            }

        } catch (AndroidLocationException e) {
            // ignore
        }


        return Pair.of(AvdConflict.NO_CONFLICT, null);
    }

    /**
* Reloads the AVD list.
* @param log the log object to receive action logs. Cannot be null.
* @throws AndroidLocationException if there was an error finding the location of the
//Synthetic comment -- @@ -564,7 +646,7 @@
}

// actually write the ini file
            iniFile = createAvdIniFile(name, avdFolder, target, removePrevious);

// writes the userdata.img in it.
String imagePath = target.getPath(IAndroidTarget.IMAGES);
//Synthetic comment -- @@ -958,13 +1040,23 @@
* @param name of the AVD.
* @param avdFolder path for the data folder of the AVD.
* @param target of the AVD.
     * @param removePrevious True if an existing ini file should be removed.
* @throws AndroidLocationException if there's a problem getting android root directory.
* @throws IOException if {@link File#getAbsolutePath()} fails.
*/
    private File createAvdIniFile(String name,
            File avdFolder,
            IAndroidTarget target,
            boolean removePrevious)
throws AndroidLocationException, IOException {
HashMap<String, String> values = new HashMap<String, String>();
File iniFile = AvdInfo.getIniFile(name);
        if (iniFile.isFile()) {
            iniFile.delete();
        } else if (iniFile.isDirectory()) {
            deleteContentOf(iniFile);
            iniFile.delete();
        }
values.put(AVD_INFO_PATH, avdFolder.getAbsolutePath());
values.put(AVD_INFO_TARGET, target.hashString());
writeIniFile(iniFile, values);
//Synthetic comment -- @@ -980,7 +1072,10 @@
* @throws IOException if {@link File#getAbsolutePath()} fails.
*/
private File createAvdIniFile(AvdInfo info) throws AndroidLocationException, IOException {
        return createAvdIniFile(info.getName(),
                new File(info.getPath()),
                info.getTarget(),
                false /*removePrevious*/);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 7197ecd..53a6c76 100644

//Synthetic comment -- @@ -16,20 +16,21 @@

package com.android.sdkuilib.internal.widgets;

import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.internal.avd.AvdManager.AvdConflict;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo;
import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.io.FileWrapper;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.GridDialog;
import com.android.util.Pair;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
//Synthetic comment -- @@ -73,8 +74,8 @@
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;

/**
//Synthetic comment -- @@ -153,8 +154,8 @@
public void modifyText(ModifyEvent e) {
String name = mAvdName.getText().trim();
if (mEditAvdInfo == null || !name.equals(mEditAvdInfo.getName())) {
                Pair<AvdConflict, String> conflict = mAvdManager.isAvdNameConflicting(name);
                if (conflict.getFirst() != AvdManager.AvdConflict.NO_CONFLICT) {
// If we're changing the state from disabled to enabled, make sure
// to uncheck the button, to force the user to voluntarily re-enforce it.
// This happens when editing an existing AVD and changing the name from
//Synthetic comment -- @@ -953,24 +954,47 @@
String height = mSkinSizeHeight.getText(); // rejects non digit.

if (width.length() == 0 || height.length() == 0) {
                    error = "Skin size is incorrect.\nBoth dimensions must be > 0.";
}
}
}

// Check for duplicate AVD name
        if (isCreate && hasAvdName && error == null && !mForceCreation.getSelection()) {
            Pair<AvdConflict, String> conflict = mAvdManager.isAvdNameConflicting(avdName);
            assert conflict != null;
            switch(conflict.getFirst()) {
            case NO_CONFLICT:
                break;
            case CONFLICT_EXISTING_AVD:
            case CONFLICT_INVALID_AVD:
error = String.format(
"The AVD name '%s' is already used.\n" +
"Check \"Override the existing AVD\" to delete the existing one.",
avdName);
                break;
            case CONFLICT_EXISTING_PATH:
                error = String.format(
                        "Conflict with %s\n" +
                        "Check \"Override the existing AVD\" to delete the existing one.",
                        conflict.getSecond());
                break;
            default:
                // Hmm not supposed to happen... probably someone expanded the
                // enum without adding something here. In this case just do an
                // assert and use a generic error message.
                error = String.format(
                        "Conflict %s with %s.\n" +
                        "Check \"Override the existing AVD\" to delete the existing one.",
                        conflict.getFirst().toString(),
                        conflict.getSecond());
                assert false;
                break;
}
}

if (error == null && mEditAvdInfo != null && isCreate) {
            warning = String.format("The AVD '%1$s' will be duplicated into '%2$s'.",
mEditAvdInfo.getName(),
avdName);
}
//Synthetic comment -- @@ -1153,9 +1177,7 @@

File avdFolder = null;
try {
            avdFolder = AvdManager.AvdInfo.getAvdFolder(avdName);
} catch (AndroidLocationException e) {
return false;
}







