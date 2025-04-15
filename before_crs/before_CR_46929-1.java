/*SDK: Relative AVD root path.

AVD have a root ini file in .android/avds/name.ini
with a "path" absolute path to the avd config directory.
This does 2 things:
- force the file to use ISO-8859-1 (aka Latin1) encoding.
- add a "path.rel" key with a relative path to the .android
  folder if the avd directory is at its typical location.

The loading logic is:
- try to use the "path" key if it points to an existing
  directory (as an absolute path).
- otherwise use the "path.rel" key as a path relative
  to the .android folder if present.

SDK Bug: 40498

Change-Id:I2364c2ee69f48816f3e6f6f33c9fa43170e05587*/
//Synthetic comment -- diff --git a/common/src/com/android/prefs/AndroidLocation.java b/common/src/com/android/prefs/AndroidLocation.java
//Synthetic comment -- index 66c0248..29e76f6 100644

//Synthetic comment -- @@ -24,6 +24,12 @@
* Manages the location of the android files (including emulator files, ddms config, debug keystore)
*/
public final class AndroidLocation {
/**
* Virtual Device folder inside the path returned by {@link #getFolder()}
*/
//Synthetic comment -- @@ -57,7 +63,7 @@
"Unable to get the Android SDK home directory.\n" +
"Make sure the environment variable ANDROID_SDK_HOME is set up.");
} else {
                sPrefsLocation = home + File.separator + ".android" + File.separator;
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 5f35661..b8a3d73 100644

//Synthetic comment -- @@ -17,8 +17,11 @@
package com.android.sdklib.internal.avd;

import com.android.SdkConstants;
import com.android.annotations.Nullable;
import com.android.io.FileWrapper;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -33,7 +36,9 @@
import com.android.sdklib.util.GrabProcessOutput.Wait;
import com.android.utils.ILogger;
import com.android.utils.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
//Synthetic comment -- @@ -41,7 +46,9 @@
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
//Synthetic comment -- @@ -67,9 +74,30 @@
}
}

public static final String AVD_FOLDER_EXTENSION = ".avd";  //$NON-NLS-1$

    public static final String AVD_INFO_PATH = "path";         //$NON-NLS-1$
public static final String AVD_INFO_TARGET = "target";     //$NON-NLS-1$

/**
//Synthetic comment -- @@ -1087,8 +1115,20 @@
}
}

HashMap<String, String> values = new HashMap<String, String>();
        values.put(AVD_INFO_PATH, avdFolder.getAbsolutePath());
values.put(AVD_INFO_TARGET, target.hashString());
writeIniFile(iniFile, values);

//Synthetic comment -- @@ -1339,13 +1379,27 @@
*         valid or not.
*/
private AvdInfo parseAvdInfo(File iniPath, ILogger log) {
        Map<String, String> map = ProjectProperties.parsePropertyFile(
new FileWrapper(iniPath),
log);

        String avdPath = map.get(AVD_INFO_PATH);
String targetHash = map.get(AVD_INFO_TARGET);

IAndroidTarget target = null;
FileWrapper configIniFile = null;
Map<String, String> properties = null;
//Synthetic comment -- @@ -1363,7 +1417,7 @@
if (!configIniFile.isFile()) {
log.warning("Missing file '%1$s'.",  configIniFile.getPath());
} else {
                properties = ProjectProperties.parsePropertyFile(configIniFile, log);
}
}

//Synthetic comment -- @@ -1455,6 +1509,7 @@

/**
* Writes a .ini file from a set of properties, using UTF-8 encoding.
*
* @param iniFile The file to generate.
* @param values THe properties to place in the ini file.
//Synthetic comment -- @@ -1462,8 +1517,14 @@
*/
private static void writeIniFile(File iniFile, Map<String, String> values)
throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(iniFile),
                SdkConstants.INI_CHARSET);

for (Entry<String, String> entry : values.entrySet()) {
writer.write(String.format("%1$s=%2$s\n", entry.getKey(), entry.getValue()));
//Synthetic comment -- @@ -1472,6 +1533,110 @@
}

/**
* Invokes the tool to create a new SD card image file.
*
* @param toolLocation The path to the mksdcard tool.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java
//Synthetic comment -- index ef931ee..d2a5fdb 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Synthetic comment -- @@ -425,7 +426,19 @@

/**
* Parses a property file (using UTF-8 encoding) and returns a map of the content.
     * <p/>If the file is not present, null is returned with no error messages sent to the log.
*
* @param propFile the property file to parse
* @param log the ILogger object receiving warning/error from the parsing.







