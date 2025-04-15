/*Allow ProjectProperties rewrite by only changing prop values.

Change-Id:I15663980f931e0d15998f0430ee7154446036fca*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 9ce3006..4ffbd0e 100644

//Synthetic comment -- @@ -926,7 +926,7 @@
File skinFolder = avdManager.getSkinPath(skin, target);
File skinHardwareFile = new File(skinFolder, AvdManager.HARDWARE_INI);
if (skinHardwareFile.isFile()) {
                            skinHardwareConfig = ProjectProperties.parsePropertyFile(
skinHardwareFile, mSdkLog);
}
break;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index e70c8fe..36afc5b 100644

//Synthetic comment -- @@ -83,12 +83,18 @@
/** hardware properties definition file */
public final static String FN_HARDWARE_INI = "hardware-properties.ini";

    /** project default property file */
public final static String FN_DEFAULT_PROPERTIES = "default.properties";

    /** project export property file */
public final static String FN_EXPORT_PROPERTIES = "export.properties";

    /** project local property file */
    public final static String FN_LOCAL_PROPERTIES = "local.properties";

    /** project build property file */
    public final static String FN_BUILD_PROPERTIES = "build.properties";

/** Skin layout file */
public final static String FN_SKIN_LAYOUT = "layout";//$NON-NLS-1$









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 71fcee5..4249365 100644

//Synthetic comment -- @@ -19,17 +19,13 @@
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.AndroidVersion.AndroidVersionException;
import com.android.sdklib.internal.project.ProjectProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
//Synthetic comment -- @@ -60,8 +56,6 @@
private final static String ADDON_REVISION = "revision";
private final static String ADDON_REVISION_OLD = "version";


private final static Pattern PATTERN_LIB_DATA = Pattern.compile(
"^([a-zA-Z0-9._-]+\\.jar);(.*)$", Pattern.CASE_INSENSITIVE);
//Synthetic comment -- @@ -280,7 +274,7 @@
File buildProp = new File(platform, SdkConstants.FN_BUILD_PROP);

if (buildProp.isFile()) {
            Map<String, String> map = ProjectProperties.parsePropertyFile(buildProp, log);

if (map != null) {
// look for some specific values in the map.
//Synthetic comment -- @@ -328,7 +322,8 @@
// platform rev number
int revision = 1;
File sourcePropFile = new File(platform, SdkConstants.FN_SOURCE_PROP);
                Map<String, String> sourceProp = ProjectProperties.parsePropertyFile(sourcePropFile,
                        log);
if (sourceProp != null) {
try {
revision = Integer.parseInt(sourceProp.get("Pkg.Revision"));
//Synthetic comment -- @@ -340,7 +335,7 @@

// Ant properties
File sdkPropFile = new File(platform, SdkConstants.FN_SDK_PROP);
                Map<String, String> antProp = ProjectProperties.parsePropertyFile(sdkPropFile, log);
if (antProp != null) {
map.putAll(antProp);
}
//Synthetic comment -- @@ -419,7 +414,8 @@
File addOnManifest = new File(addon, SdkConstants.FN_MANIFEST_INI);

if (addOnManifest.isFile()) {
            Map<String, String> propertyMap = ProjectProperties.parsePropertyFile(addOnManifest,
                    log);

if (propertyMap != null) {
// look for some specific values in the map.
//Synthetic comment -- @@ -609,76 +605,6 @@
}



/**
* Parses the skin folder and builds the skin list.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 35ce0b0..83dd7b0 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo.AvdStatus;
import com.android.sdklib.internal.project.ProjectProperties;

import java.io.BufferedReader;
import java.io.File;
//Synthetic comment -- @@ -690,7 +691,7 @@

File targetHardwareFile = new File(target.getLocation(), AvdManager.HARDWARE_INI);
if (targetHardwareFile.isFile()) {
                Map<String, String> targetHardwareConfig = ProjectProperties.parsePropertyFile(
targetHardwareFile, log);
if (targetHardwareConfig != null) {
finalHardwareValues.putAll(targetHardwareConfig);
//Synthetic comment -- @@ -702,7 +703,7 @@
File skinFolder = getSkinPath(skinName, target);
File skinHardwareFile = new File(skinFolder, AvdManager.HARDWARE_INI);
if (skinHardwareFile.isFile()) {
                Map<String, String> skinHardwareConfig = ProjectProperties.parsePropertyFile(
skinHardwareFile, log);
if (skinHardwareConfig != null) {
finalHardwareValues.putAll(skinHardwareConfig);
//Synthetic comment -- @@ -1140,7 +1141,7 @@
*         valid or not.
*/
private AvdInfo parseAvdInfo(File path, ISdkLog log) {
        Map<String, String> map = ProjectProperties.parsePropertyFile(path, log);

String avdPath = map.get(AVD_INFO_PATH);
String targetHash = map.get(AVD_INFO_TARGET);
//Synthetic comment -- @@ -1162,7 +1163,7 @@
if (!configIniFile.isFile()) {
log.warning("Missing file '%1$s'.",  configIniFile.getPath());
} else {
                properties = ProjectProperties.parsePropertyFile(configIniFile, log);
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java
//Synthetic comment -- index 68fa817..dd465aa 100644

//Synthetic comment -- @@ -16,26 +16,40 @@

package com.android.sdklib.internal.project;

import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.io.FileWrapper;
import com.android.sdklib.io.FolderWrapper;
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.IAbstractFolder;
import com.android.sdklib.io.StreamException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* Class to load and save project properties for both ADT and Ant-based build.
*
*/
public final class ProjectProperties {
    private final static Pattern PATTERN_PROP = Pattern.compile(
    "^([a-zA-Z0-9._-]+)\\s*=\\s*(.*)\\s*$");

/** The property name for the project target */
public final static String PROPERTY_TARGET = "target";

//Synthetic comment -- @@ -56,6 +70,7 @@
public final static String PROPERTY_TESTED_PROJECT = "tested.project.dir";

public final static String PROPERTY_BUILD_SOURCE_DIR = "source.dir";
    public final static String PROPERTY_BUILD_OUT_DIR = "out.dir";

public final static String PROPERTY_PACKAGE = "package";
public final static String PROPERTY_VERSIONCODE = "versionCode";
//Synthetic comment -- @@ -64,22 +79,44 @@
public final static String PROPERTY_KEY_ALIAS = "key.alias";

public static enum PropertyType {
        BUILD(SdkConstants.FN_BUILD_PROPERTIES, BUILD_HEADER, new String[] {
                PROPERTY_BUILD_SOURCE_DIR, PROPERTY_BUILD_OUT_DIR
            }),
        DEFAULT(SdkConstants.FN_DEFAULT_PROPERTIES, DEFAULT_HEADER, new String[] {
                PROPERTY_TARGET, PROPERTY_LIBRARY, PROPERTY_LIB_REF,
                PROPERTY_KEY_STORE, PROPERTY_KEY_ALIAS
            }),
        LOCAL(SdkConstants.FN_LOCAL_PROPERTIES, LOCAL_HEADER, new String[] {
                PROPERTY_SDK
            }),
        EXPORT(SdkConstants.FN_EXPORT_PROPERTIES, EXPORT_HEADER, new String[] {
                PROPERTY_PACKAGE, PROPERTY_VERSIONCODE, PROPERTY_PROJECTS,
                PROPERTY_KEY_STORE, PROPERTY_KEY_ALIAS
            });

private final String mFilename;
private final String mHeader;
        private final Set<String> mValidProps;

        PropertyType(String filename, String header, String[] validProps) {
mFilename = filename;
mHeader = header;
            HashSet<String> s = new HashSet<String>();
            s.addAll(Arrays.asList(validProps));
            mValidProps = Collections.unmodifiableSet(s);
}

public String getFilename() {
return mFilename;
}

        /**
         * Returns an unmodifyable {@link Set} of the known properties for that type of
         * property file.
         */
        public Set<String> getValidProps() {
            return mValidProps;
        }
}

private final static String LOCAL_HEADER =
//Synthetic comment -- @@ -192,7 +229,7 @@
if (projectFolder.exists()) {
IAbstractFile propFile = projectFolder.getFile(type.mFilename);
if (propFile.exists()) {
                Map<String, String> map = parsePropertyFile(propFile, null /* log */);
if (map != null) {
return new ProjectProperties(projectFolder, map, type);
}
//Synthetic comment -- @@ -222,7 +259,7 @@
if (mProjectFolder.exists()) {
IAbstractFile propFile = mProjectFolder.getFile(type.mFilename);
if (propFile.exists()) {
                Map<String, String> map = parsePropertyFile(propFile, null /* log */);
if (map != null) {
for(Entry<String, String> entry : map.entrySet()) {
String key = entry.getKey();
//Synthetic comment -- @@ -310,7 +347,7 @@
if (mProjectFolder.exists()) {
IAbstractFile propFile = mProjectFolder.getFile(mType.mFilename);
if (propFile.exists()) {
                Map<String, String> map = parsePropertyFile(propFile, null /* log */);
if (map != null) {
mProperties.clear();
mProperties.putAll(map);
//Synthetic comment -- @@ -327,30 +364,150 @@
public synchronized void save() throws IOException, StreamException {
IAbstractFile toSave = mProjectFolder.getFile(mType.mFilename);

        // write the whole file in a byte array before dumping it in the file. This
        // This is so that if the file already existing
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(baos, SdkConstants.INI_CHARSET);

        if (toSave.exists()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(toSave.getContents(),
                    SdkConstants.INI_CHARSET));

            String line = null;
            while ((line = reader.readLine()) != null) {
                // check if this is a line containing a property.
                if (line.length() > 0 && line.charAt(0) != '#') {

                    Matcher m = PATTERN_PROP.matcher(line);
                    if (m.matches()) {
                        String key = m.group(1);
                        String value = m.group(2);
                        // check if the property still exists.
                        if (mProperties.containsKey(key)) {
                            // put the new value.
                            value = mProperties.get(key);
                        } else {
                            // property doesn't exist. Check if it's a known property.
                            // if it's a known one, we'll remove it, otherwise, leave it untouched.
                            if (mType.getValidProps().contains(key)) {
                                value = null;
                            }
                        }

                        // if the value is still valid, write it down.
                        if (value != null) {
                            value = value.replaceAll("\\\\", "\\\\\\\\");
                            writer.write(String.format("%s=%s\n", key, value));
                        }
                    } else  {
                        // the line was wrong, let's just ignore it so that it's removed from the
                        // file.
                    }
                } else {
                    // non-property line: just write the line in the output as-is.
                    writer.append(line).append('\n');
                }
}
        } else {
            // new file, just write it all
            // write the header
            writer.write(mType.mHeader);

            // write the properties.
            for (Entry<String, String> entry : mProperties.entrySet()) {
                String comment = COMMENT_MAP.get(entry.getKey());
                if (comment != null) {
                    writer.write(comment);
                }
                String value = entry.getValue();
                if (value != null) {
                    value = value.replaceAll("\\\\", "\\\\\\\\");
                    writer.write(String.format("%s=%s\n", entry.getKey(), value));
                }
}
}

        writer.flush();

        // now put the content in the file.
        OutputStream filestream = toSave.getOutputStream();
        filestream.write(baos.toByteArray());
        filestream.flush();
}

/**
     * Parses a property file (using UTF-8 encoding) and returns a map of the content.
     * <p/>If the file is not present, null is returned with no error messages sent to the log.
     *
     * @param propFile the property file to parse
     * @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
     * @return the map of (key,value) pairs, or null if the parsing failed.
     * @deprecated Use {@link #parsePropertyFile(IAbstractFile, ISdkLog)}
     */
    public static Map<String, String> parsePropertyFile(File propFile, ISdkLog log) {
        IAbstractFile wrapper = new FileWrapper(propFile);
        return parsePropertyFile(wrapper, log);
    }

    /**
     * Parses a property file (using UTF-8 encoding) and returns a map of the content.
     * <p/>If the file is not present, null is returned with no error messages sent to the log.
     *
     * @param propFile the property file to parse
     * @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
     * @return the map of (key,value) pairs, or null if the parsing failed.
     */
    public static Map<String, String> parsePropertyFile(IAbstractFile propFile, ISdkLog log) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(propFile.getContents(),
                    SdkConstants.INI_CHARSET));

            String line = null;
            Map<String, String> map = new HashMap<String, String>();
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0 && line.charAt(0) != '#') {

                    Matcher m = PATTERN_PROP.matcher(line);
                    if (m.matches()) {
                        map.put(m.group(1), m.group(2));
                    } else {
                        log.warning("Error parsing '%1$s': \"%2$s\" is not a valid syntax",
                                propFile.getOsLocation(),
                                line);
                        return null;
                    }
                }
            }

            return map;
        } catch (FileNotFoundException e) {
            // this should not happen since we usually test the file existence before
            // calling the method.
            // Return null below.
        } catch (IOException e) {
            log.warning("Error parsing '%1$s': %2$s.",
                    propFile.getOsLocation(),
                    e.getMessage());
        } catch (StreamException e) {
            log.warning("Error parsing '%1$s': %2$s.",
                    propFile.getOsLocation(),
                    e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // pass
                }
            }
        }

        return null;
    }


    /**
* Private constructor.
* <p/>
* Use {@link #load(String, PropertyType)} or {@link #create(String, PropertyType)}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index e3724be..ff42b0b 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo;
import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.GridDialog;

//Synthetic comment -- @@ -876,7 +877,7 @@
if (target.isPlatform() == false) {
File targetHardwareFile = new File(target.getLocation(), AvdManager.HARDWARE_INI);
if (targetHardwareFile.isFile()) {
                Map<String, String> targetHardwareConfig = ProjectProperties.parsePropertyFile(
targetHardwareFile, null /*log*/);
if (targetHardwareConfig != null) {
hardwareValues.putAll(targetHardwareConfig);
//Synthetic comment -- @@ -887,7 +888,7 @@
// from the skin
File skinHardwareFile = new File(skin, AvdManager.HARDWARE_INI);
if (skinHardwareFile.isFile()) {
            Map<String, String> skinHardwareConfig = ProjectProperties.parsePropertyFile(
skinHardwareFile, null /*log*/);
if (skinHardwareConfig != null) {
hardwareValues.putAll(skinHardwareConfig);







