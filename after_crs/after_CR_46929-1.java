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
     * The name of the .android folder returned by {@link #getFolder()}.
     */
    public static final String FOLDER_DOT_ANDROID = ".android";

/**
* Virtual Device folder inside the path returned by {@link #getFolder()}
*/
//Synthetic comment -- @@ -57,7 +63,7 @@
"Unable to get the Android SDK home directory.\n" +
"Make sure the environment variable ANDROID_SDK_HOME is set up.");
} else {
                sPrefsLocation = home + File.separator + FOLDER_DOT_ANDROID + File.separator;
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 5f35661..b8a3d73 100644

//Synthetic comment -- @@ -17,8 +17,11 @@
package com.android.sdklib.internal.avd;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.io.FileWrapper;
import com.android.io.IAbstractFile;
import com.android.io.StreamException;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -33,7 +36,9 @@
import com.android.sdklib.util.GrabProcessOutput.Wait;
import com.android.utils.ILogger;
import com.android.utils.Pair;
import com.google.common.io.Closeables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
//Synthetic comment -- @@ -41,7 +46,9 @@
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
//Synthetic comment -- @@ -67,9 +74,30 @@
}
}

    private final static Pattern INI_LINE_PATTERN =
        Pattern.compile("^([a-zA-Z0-9._-]+)\\s*=\\s*(.*)\\s*$");

public static final String AVD_FOLDER_EXTENSION = ".avd";  //$NON-NLS-1$

    /** Chartset encoding used by the avd.ini/config.ini. */
    public static final String AVD_INI_ENCODING = "avd.ini.encoding"; //$NON-NLS-1$

    /**
     * The *absolute* path to the AVD folder (which contains the #CONFIG_INI file).
     */
    public static final String AVD_INFO_ABS_PATH = "path";          //$NON-NLS-1$

    /**
     * The path to the AVD folder (which contains the #CONFIG_INI file) relative to
     * the {@link AndroidLocation#FOLDER_DOT_ANDROID}. This information is written
     * in the avd ini <b>only</b> if the AVD folder is located under the .android path
     * (that is the relative that has no backward {@code ..} references).
     */
    public static final String AVD_INFO_REL_PATH = "path.rel";      //$NON-NLS-1$

    /**
     * The {@link IAndroidTarget#hashString()} of the AVD.
     */
public static final String AVD_INFO_TARGET = "target";     //$NON-NLS-1$

/**
//Synthetic comment -- @@ -1087,8 +1115,20 @@
}
}

        String absPath = avdFolder.getAbsolutePath();
        String relPath = null;
        String androidPath = AndroidLocation.getFolder();
        if (absPath.startsWith(androidPath)) {
            // Compute the AVD path relative to the android path.
            assert androidPath.endsWith(File.separator);
            relPath = absPath.substring(androidPath.length());
        }

HashMap<String, String> values = new HashMap<String, String>();
        if (relPath != null) {
            values.put(AVD_INFO_REL_PATH, relPath);
        }
        values.put(AVD_INFO_ABS_PATH, absPath);
values.put(AVD_INFO_TARGET, target.hashString());
writeIniFile(iniFile, values);

//Synthetic comment -- @@ -1339,13 +1379,27 @@
*         valid or not.
*/
private AvdInfo parseAvdInfo(File iniPath, ILogger log) {
        Map<String, String> map = parseIniFile(
new FileWrapper(iniPath),
log);

        String avdPath = map.get(AVD_INFO_ABS_PATH);
String targetHash = map.get(AVD_INFO_TARGET);

        if (! new File(avdPath).isDirectory()) {
            // Try to fallback on the relative path, if present.
            String relPath = map.get(AVD_INFO_REL_PATH);
            if (relPath != null) {
                try {
                    String androidPath = AndroidLocation.getFolder();
                    File f = new File(androidPath, relPath);
                    if (f.isDirectory()) {
                        avdPath = f.getAbsolutePath();
                    }
                } catch (AndroidLocationException ignore) {}
            }
        }

IAndroidTarget target = null;
FileWrapper configIniFile = null;
Map<String, String> properties = null;
//Synthetic comment -- @@ -1363,7 +1417,7 @@
if (!configIniFile.isFile()) {
log.warning("Missing file '%1$s'.",  configIniFile.getPath());
} else {
                properties = parseIniFile(configIniFile, log);
}
}

//Synthetic comment -- @@ -1455,6 +1509,7 @@

/**
* Writes a .ini file from a set of properties, using UTF-8 encoding.
     * The file should be read back later by {@link #parseIniFile(IAbstractFile, ILogger)}.
*
* @param iniFile The file to generate.
* @param values THe properties to place in the ini file.
//Synthetic comment -- @@ -1462,8 +1517,14 @@
*/
private static void writeIniFile(File iniFile, Map<String, String> values)
throws IOException {

        Charset charset = Charset.isSupported("ISO-8859-1")     //$NON-NLS-1$
                            ? Charset.forName("ISO-8859-1")     //$NON-NLS-1$
                            : Charset.defaultCharset();
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(iniFile), charset);

        // Write down the charset used in case we want to use it later.
        writer.write(String.format("%1$s=%2$s\n", AVD_INI_ENCODING, charset.name()));

for (Entry<String, String> entry : values.entrySet()) {
writer.write(String.format("%1$s=%2$s\n", entry.getKey(), entry.getValue()));
//Synthetic comment -- @@ -1472,6 +1533,110 @@
}

/**
     * Parses a property file and returns a map of the content.
     * <p/>
     * If the file is not present, null is returned with no error messages sent to the log.
     * <p/>
     * Charset encoding will be either the system's default or the one specified by the
     * {@link #AVD_INI_ENCODING} key if present.
     *
     * @param propFile the property file to parse
     * @param log the ILogger object receiving warning/error from the parsing.
     * @return the map of (key,value) pairs, or null if the parsing failed.
     */
    private static Map<String, String> parseIniFile(
            @NonNull IAbstractFile propFile,
            @Nullable ILogger log) {
        return parseIniFileImpl(propFile, log, null /*charset*/);
    }

    /**
     * Implementation helper for the {@link #parseIniFile(IAbstractFile, ILogger)} method.
     * Don't call this one directly.
     *
     * @param propFile the property file to parse
     * @param log the ILogger object receiving warning/error from the parsing.
     * @param charset When a specific charset is specified, this will be used as-is.
     *   When null, the default charset will first be used and if the key
     *   {@link #AVD_INI_ENCODING} is found the parsing will restart using that specific
     *   charset.
     * @return the map of (key,value) pairs, or null if the parsing failed.
     */
    private static Map<String, String> parseIniFileImpl(
            @NonNull IAbstractFile propFile,
            @Nullable ILogger log,
            @NonNull Charset charset) {

        BufferedReader reader = null;
        try {
            boolean canChangeCharset = false;
            if (charset == null) {
                canChangeCharset = false;
                charset = Charset.isSupported("ISO-8859-1")     //$NON-NLS-1$
                            ? Charset.forName("ISO-8859-1")     //$NON-NLS-1$
                            : Charset.defaultCharset();
            }
            reader = new BufferedReader(new InputStreamReader(propFile.getContents(), charset));

            String line = null;
            Map<String, String> map = new HashMap<String, String>();
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0 && line.charAt(0) != '#') {

                    Matcher m = INI_LINE_PATTERN.matcher(line);
                    if (m.matches()) {
                        // Note: we do NOT escape values.
                        String key = m.group(1);
                        String value = m.group(2);

                        // If we find the charset encoding and it's not the same one and
                        // it's a valid one, re-read the file using that charset.
                        if (canChangeCharset &&
                                AVD_INI_ENCODING.equals(key) &&
                                !charset.name().equals(value) &&
                                Charset.isSupported(value)) {
                            charset = Charset.forName(value);
                            return parseIniFileImpl(propFile, log, charset);
                        }

                        map.put(key, value);
                    } else {
                        if (log != null) {
                            log.warning("Error parsing '%1$s': \"%2$s\" is not a valid syntax",
                                    propFile.getOsLocation(),
                                    line);
                        }
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
            if (log != null) {
                log.warning("Error parsing '%1$s': %2$s.",
                        propFile.getOsLocation(),
                        e.getMessage());
            }
        } catch (StreamException e) {
            if (log != null) {
                log.warning("Error parsing '%1$s': %2$s.",
                        propFile.getOsLocation(),
                        e.getMessage());
            }
        } finally {
            Closeables.closeQuietly(reader);
        }

        return null;
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
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Synthetic comment -- @@ -425,7 +426,19 @@

/**
* Parses a property file (using UTF-8 encoding) and returns a map of the content.
     * <p/>
     * If the file is not present, null is returned with no error messages sent to the log.
     * <p/>
     * IMPORTANT: This method is now unfortunately used in multiple places to parse random
     * property files. This is NOT a safe practice since there is no corresponding method
     * to write property files unless you use {@link ProjectPropertiesWorkingCopy#save()}.
     * Code that writes INI or properties without at least using {@link #escape(String)} will
     * certainly not load back correct data. <br/>
     * Unless there's a strong legacy need to support existing files, new callers should
     * probably just use Java's {@link Properties} which has well defined semantics.
     * It's also a mistake to write/read property files using this code and expect it to
     * work with Java's {@link Properties} or external tools (e.g. ant) since there can be
     * differences in escaping and in character encoding.
*
* @param propFile the property file to parse
* @param log the ILogger object receiving warning/error from the parsing.







