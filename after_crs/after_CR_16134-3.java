/*SdkManager: don't use deprecated methods.

If we're going to deprecate the parsePropertyFile(File), we might
as well stop using it ourselves.

Also removes the obsolete parsePropertyFile(File) method.

Change-Id:I811590ca583f2a89d5b7d606d70a4bc14eb6230b*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 4ffbd0e..1bdd2fa 100644

//Synthetic comment -- @@ -31,12 +31,13 @@
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectCreator.OutputLevel;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.io.FileWrapper;
import com.android.sdklib.repository.SdkRepository;
import com.android.sdklib.xml.AndroidXPathFactory;
import com.android.sdkmanager.internal.repository.AboutPage;
import com.android.sdkmanager.internal.repository.SettingsPage;
import com.android.sdkuilib.internal.repository.LocalPackagesPage;
import com.android.sdkuilib.internal.repository.UpdateNoWindow;
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.UpdaterWindow;

//Synthetic comment -- @@ -927,7 +928,8 @@
File skinHardwareFile = new File(skinFolder, AvdManager.HARDWARE_INI);
if (skinHardwareFile.isFile()) {
skinHardwareConfig = ProjectProperties.parsePropertyFile(
                                    new FileWrapper(skinHardwareFile),
                                    mSdkLog);
}
break;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 4249365..9c90817 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.AndroidVersion.AndroidVersionException;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.io.FileWrapper;

import java.io.File;
import java.io.FileInputStream;
//Synthetic comment -- @@ -274,7 +275,9 @@
File buildProp = new File(platform, SdkConstants.FN_BUILD_PROP);

if (buildProp.isFile()) {
            Map<String, String> map = ProjectProperties.parsePropertyFile(
                    new FileWrapper(buildProp),
                    log);

if (map != null) {
// look for some specific values in the map.
//Synthetic comment -- @@ -322,7 +325,8 @@
// platform rev number
int revision = 1;
File sourcePropFile = new File(platform, SdkConstants.FN_SOURCE_PROP);
                Map<String, String> sourceProp = ProjectProperties.parsePropertyFile(
                        new FileWrapper(sourcePropFile),
log);
if (sourceProp != null) {
try {
//Synthetic comment -- @@ -335,7 +339,9 @@

// Ant properties
File sdkPropFile = new File(platform, SdkConstants.FN_SDK_PROP);
                Map<String, String> antProp = ProjectProperties.parsePropertyFile(
                        new FileWrapper(sdkPropFile),
                        log);
if (antProp != null) {
map.putAll(antProp);
}
//Synthetic comment -- @@ -414,7 +420,8 @@
File addOnManifest = new File(addon, SdkConstants.FN_MANIFEST_INI);

if (addOnManifest.isFile()) {
            Map<String, String> propertyMap = ProjectProperties.parsePropertyFile(
                    new FileWrapper(addOnManifest),
log);

if (propertyMap != null) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 83dd7b0..273eab3 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo.AvdStatus;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.io.FileWrapper;

import java.io.BufferedReader;
import java.io.File;
//Synthetic comment -- @@ -692,7 +693,8 @@
File targetHardwareFile = new File(target.getLocation(), AvdManager.HARDWARE_INI);
if (targetHardwareFile.isFile()) {
Map<String, String> targetHardwareConfig = ProjectProperties.parsePropertyFile(
                        new FileWrapper(targetHardwareFile),
                        log);
if (targetHardwareConfig != null) {
finalHardwareValues.putAll(targetHardwareConfig);
values.putAll(targetHardwareConfig);
//Synthetic comment -- @@ -704,7 +706,8 @@
File skinHardwareFile = new File(skinFolder, AvdManager.HARDWARE_INI);
if (skinHardwareFile.isFile()) {
Map<String, String> skinHardwareConfig = ProjectProperties.parsePropertyFile(
                        new FileWrapper(skinHardwareFile),
                        log);
if (skinHardwareConfig != null) {
finalHardwareValues.putAll(skinHardwareConfig);
values.putAll(skinHardwareConfig);
//Synthetic comment -- @@ -1141,7 +1144,9 @@
*         valid or not.
*/
private AvdInfo parseAvdInfo(File path, ISdkLog log) {
        Map<String, String> map = ProjectProperties.parsePropertyFile(
                new FileWrapper(path),
                log);

String avdPath = map.get(AVD_INFO_PATH);
String targetHash = map.get(AVD_INFO_TARGET);
//Synthetic comment -- @@ -1163,7 +1168,9 @@
if (!configIniFile.isFile()) {
log.warning("Missing file '%1$s'.",  configIniFile.getPath());
} else {
                properties = ProjectProperties.parsePropertyFile(
                        new FileWrapper(configIniFile),
                        log);
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java
//Synthetic comment -- index 4aea001..1d36835 100644

//Synthetic comment -- @@ -18,14 +18,12 @@

import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.io.FolderWrapper;
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.IAbstractFolder;
import com.android.sdklib.io.StreamException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
//Synthetic comment -- @@ -316,20 +314,6 @@
* @param propFile the property file to parse
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
* @return the map of (key,value) pairs, or null if the parsing failed.
*/
public static Map<String, String> parsePropertyFile(IAbstractFile propFile, ISdkLog log) {
BufferedReader reader = null;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/io/FileWrapper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/FileWrapper.java
//Synthetic comment -- index 9a0a4a6..afc19b2 100644

//Synthetic comment -- @@ -33,6 +33,14 @@
private static final long serialVersionUID = 1L;

/**
     * Creates a new File instance matching a given {@link File} object.
     * @param file the file to match
     */
    public FileWrapper(File file) {
        super(file.getAbsolutePath());
    }

    /**
* Creates a new File instance from a parent abstract pathname and a child pathname string.
* @param parent the parent pathname
* @param child the child name
//Synthetic comment -- @@ -46,12 +54,12 @@
/**
* Creates a new File instance by converting the given pathname string into an abstract
* pathname.
     * @param osPathname the OS pathname
*
* @see File#File(String)
*/
    public FileWrapper(String osPathname) {
        super(osPathname);
}

/**
//Synthetic comment -- @@ -77,14 +85,6 @@
super(uri);
}

public InputStream getContents() throws StreamException {
try {
return new FileInputStream(this);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index ff42b0b..cdb8054 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import com.android.sdklib.internal.avd.AvdManager.AvdInfo;
import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.io.FileWrapper;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.GridDialog;

//Synthetic comment -- @@ -878,7 +879,8 @@
File targetHardwareFile = new File(target.getLocation(), AvdManager.HARDWARE_INI);
if (targetHardwareFile.isFile()) {
Map<String, String> targetHardwareConfig = ProjectProperties.parsePropertyFile(
                        new FileWrapper(targetHardwareFile),
                        null /*log*/);
if (targetHardwareConfig != null) {
hardwareValues.putAll(targetHardwareConfig);
}
//Synthetic comment -- @@ -889,7 +891,8 @@
File skinHardwareFile = new File(skin, AvdManager.HARDWARE_INI);
if (skinHardwareFile.isFile()) {
Map<String, String> skinHardwareConfig = ProjectProperties.parsePropertyFile(
                    new FileWrapper(skinHardwareFile),
                    null /*log*/);
if (skinHardwareConfig != null) {
hardwareValues.putAll(skinHardwareConfig);
}







