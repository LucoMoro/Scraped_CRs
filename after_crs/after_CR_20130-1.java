/*Support broken addons in SDK Manager UI.

Change-Id:Idec7365e440e865a8225ed6f0c9c156206141d73*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 32c5838..d3e1cc1 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.sdklib.AndroidVersion.AndroidVersionException;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.io.FileWrapper;
import com.android.sdklib.util.Pair;

import java.io.File;
import java.io.FileInputStream;
//Synthetic comment -- @@ -43,27 +44,27 @@
*/
public final class SdkManager {

    public final static String PROP_VERSION_SDK = "ro.build.version.sdk";              //$NON-NLS-1$
    public final static String PROP_VERSION_CODENAME = "ro.build.version.codename";    //$NON-NLS-1$
    public final static String PROP_VERSION_RELEASE = "ro.build.version.release";      //$NON-NLS-1$

    private final static String ADDON_NAME = "name";                                   //$NON-NLS-1$
    private final static String ADDON_VENDOR = "vendor";                               //$NON-NLS-1$
    private final static String ADDON_API = "api";                                     //$NON-NLS-1$
    private final static String ADDON_DESCRIPTION = "description";                     //$NON-NLS-1$
    private final static String ADDON_LIBRARIES = "libraries";                         //$NON-NLS-1$
    private final static String ADDON_DEFAULT_SKIN = "skin";                           //$NON-NLS-1$
    private final static String ADDON_USB_VENDOR = "usb-vendor";                       //$NON-NLS-1$
    private final static String ADDON_REVISION = "revision";                           //$NON-NLS-1$
    private final static String ADDON_REVISION_OLD = "version";                        //$NON-NLS-1$


private final static Pattern PATTERN_LIB_DATA = Pattern.compile(
            "^([a-zA-Z0-9._-]+\\.jar);(.*)$", Pattern.CASE_INSENSITIVE);               //$NON-NLS-1$

// usb ids are 16-bit hexadecimal values.
private final static Pattern PATTERN_USB_IDS = Pattern.compile(
            "^0x[a-f0-9]{4}$", Pattern.CASE_INSENSITIVE);                              //$NON-NLS-1$

/** List of items in the platform to check when parsing it. These paths are relative to the
* platform root folder. */
//Synthetic comment -- @@ -73,39 +74,40 @@
};

/** Preference file containing the usb ids for adb */
    private final static String ADB_INI_FILE = "adb_usb.ini";                          //$NON-NLS-1$
//0--------90--------90--------90--------90--------90--------90--------90--------9
private final static String ADB_INI_HEADER =
        "# ANDROID 3RD PARTY USB VENDOR ID LIST -- DO NOT EDIT.\n" +                   //$NON-NLS-1$
        "# USE 'android update adb' TO GENERATE.\n" +                                  //$NON-NLS-1$
        "# 1 USB VENDOR ID PER LINE.\n";                                               //$NON-NLS-1$

    /** The location of the SDK as an OS path */
    private final String mOsSdkPath;
    /** Valid targets that have been loaded. */
private IAndroidTarget[] mTargets;

/**
* Create a new {@link SdkManager} instance.
* External users should use {@link #createManager(String, ISdkLog)}.
*
     * @param osSdkPath the location of the SDK.
*/
    private SdkManager(String osSdkPath) {
        mOsSdkPath = osSdkPath;
}

/**
* Creates an {@link SdkManager} for a given sdk location.
     * @param osSdkPath the location of the SDK.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
* @return the created {@link SdkManager} or null if the location is not valid.
*/
    public static SdkManager createManager(String osSdkPath, ISdkLog log) {
try {
            SdkManager manager = new SdkManager(osSdkPath);
ArrayList<IAndroidTarget> list = new ArrayList<IAndroidTarget>();
            loadPlatforms(osSdkPath, list, log);
            loadAddOns(osSdkPath, list, log);

// sort the targets/add-ons
Collections.sort(list);
//Synthetic comment -- @@ -127,7 +129,7 @@
* Returns the location of the SDK.
*/
public String getLocation() {
        return mOsSdkPath;
}

/**
//Synthetic comment -- @@ -193,7 +195,7 @@

// now write the Id in a text file, one per line.
for (Integer i : set) {
                writer.write(String.format("0x%04x\n", i));                            //$NON-NLS-1$
}
} finally {
if (writer != null) {
//Synthetic comment -- @@ -210,8 +212,8 @@
public void reloadSdk(ISdkLog log) {
// get the current target list.
ArrayList<IAndroidTarget> list = new ArrayList<IAndroidTarget>();
        loadPlatforms(mOsSdkPath, list, log);
        loadAddOns(mOsSdkPath, list, log);

// For now replace the old list with the new one.
// In the future we may want to keep the current objects, so that ADT doesn't have to deal
//Synthetic comment -- @@ -326,7 +328,7 @@
sourcePropFile, log);
if (sourceProp != null) {
try {
                        revision = Integer.parseInt(sourceProp.get("Pkg.Revision"));   //$NON-NLS-1$
} catch (NumberFormatException e) {
// do nothing, we'll keep the default value of 1.
}
//Synthetic comment -- @@ -376,19 +378,21 @@

/**
* Loads the Add-on from the SDK.
     * @param osSdkPath Location of the SDK
* @param list the list to fill with the add-ons.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
*/
    private static void loadAddOns(String osSdkPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
        File addonFolder = new File(osSdkPath, SdkConstants.FD_ADDONS);
if (addonFolder.isDirectory()) {
File[] addons  = addonFolder.listFiles();

            IAndroidTarget[] targetList = list.toArray(new IAndroidTarget[list.size()]);

for (File addon : addons) {
// Add-ons have to be folders. Ignore files and no need to warn about them.
if (addon.isDirectory()) {
                    AddOnTarget target = loadAddon(addon, targetList, log);
if (target != null) {
list.add(target);
}
//Synthetic comment -- @@ -411,151 +415,222 @@

/**
* Loads a specific Add-on at a given location.
     * @param addonDir the location of the addon direction.
* @param targetList The list of Android target that were already loaded from the SDK.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
*/
    private static AddOnTarget loadAddon(File addonDir,
            IAndroidTarget[] targetList,
ISdkLog log) {

        // Parse the addon properties to ensure we can load it.
        Pair<Map<String, String>, String> infos = parseAddonProperties(addonDir, targetList, log);

        Map<String, String> propertyMap = infos.getFirst();
        String error = infos.getSecond();

        if (error != null) {
            log.warning("Ignoring add-on '%1$s': %2$s", addonDir.getName(), error);
            return null;
        }

        // Since error==null we're not supposed to encounter any issues loading this add-on.
        try {
            assert propertyMap != null;

            String api = propertyMap.get(ADDON_API);
            String name = propertyMap.get(ADDON_NAME);
            String vendor = propertyMap.get(ADDON_VENDOR);

            assert api != null;
            assert name != null;
            assert vendor != null;

            PlatformTarget baseTarget = null;

            // Look for a platform that has a matching api level or codename.
            for (IAndroidTarget target : targetList) {
                if (target.isPlatform() && target.getVersion().equals(api)) {
                    baseTarget = (PlatformTarget)target;
                    break;
}
            }

            assert baseTarget != null;

            // get the optional description
            String description = propertyMap.get(ADDON_DESCRIPTION);

            // get the add-on revision
            int revisionValue = 1;
            String revision = propertyMap.get(ADDON_REVISION);
            if (revision == null) {
                revision = propertyMap.get(ADDON_REVISION_OLD);
            }
            if (revision != null) {
                revisionValue = Integer.parseInt(revision);
            }

            // get the optional libraries
            String librariesValue = propertyMap.get(ADDON_LIBRARIES);
            Map<String, String[]> libMap = null;

            if (librariesValue != null) {
                librariesValue = librariesValue.trim();
                if (librariesValue.length() > 0) {
                    // split in the string into the libraries name
                    String[] libraries = librariesValue.split(";");                    //$NON-NLS-1$
                    if (libraries.length > 0) {
                        libMap = new HashMap<String, String[]>();
                        for (String libName : libraries) {
                            libName = libName.trim();

                            // get the library data from the properties
                            String libData = propertyMap.get(libName);

                            if (libData != null) {
                                // split the jar file from the description
                                Matcher m = PATTERN_LIB_DATA.matcher(libData);
                                if (m.matches()) {
                                    libMap.put(libName, new String[] {
                                            m.group(1), m.group(2) });
} else {
log.warning(
                                            "Ignoring library '%1$s', property value has wrong format\n\t%2$s",
libName, libData);
}
                            } else {
                                log.warning(
                                        "Ignoring library '%1$s', missing property value",
                                        libName, libData);
}
}
}
}
}

            AddOnTarget target = new AddOnTarget(addonDir.getAbsolutePath(), name, vendor,
                    revisionValue, description, libMap, baseTarget);

            // need to parse the skins.
            String[] skins = parseSkinFolder(target.getPath(IAndroidTarget.SKINS));

            // get the default skin, or take it from the base platform if needed.
            String defaultSkin = propertyMap.get(ADDON_DEFAULT_SKIN);
            if (defaultSkin == null) {
                if (skins.length == 1) {
                    defaultSkin = skins[0];
                } else {
                    defaultSkin = baseTarget.getDefaultSkin();
                }
            }

            // get the USB ID (if available)
            int usbVendorId = convertId(propertyMap.get(ADDON_USB_VENDOR));
            if (usbVendorId != IAndroidTarget.NO_USB_ID) {
                target.setUsbVendorId(usbVendorId);
            }

            target.setSkins(skins, defaultSkin);

            return target;
        }
        catch (Exception e) {
            log.warning("Ignoring add-on '%1$s': error %2$s.",
                    addonDir.getName(), e.toString());
}

return null;
}

/**
     * Parses the add-on properties and decodes any error that occurs when loading an addon.
     *
     * @param addonDir the location of the addon directory.
     * @param targetList The list of Android target that were already loaded from the SDK.
     * @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
     * @return A pair with the property map and an error string. Both can be null but not at the
     *  same time. If a non-null error is present then the property map must be ignored. The error
     *  should be translatable as it might show up in the SdkManager UI.
     */
    public static Pair<Map<String, String>, String> parseAddonProperties(
            File addonDir,
            IAndroidTarget[] targetList,
            ISdkLog log) {
        Map<String, String> propertyMap = null;
        String error = null;

        FileWrapper addOnManifest = new FileWrapper(addonDir, SdkConstants.FN_MANIFEST_INI);

        do {
            if (!addOnManifest.isFile()) {
                error = String.format("File not found: %1$s", SdkConstants.FN_MANIFEST_INI);
                break;
            }

            propertyMap = ProjectProperties.parsePropertyFile(addOnManifest, log);
            if (propertyMap == null) {
                error = String.format("Failed to parse properties from %1$s",
                        SdkConstants.FN_MANIFEST_INI);
                break;
            }

            // look for some specific values in the map.
            // we require name, vendor, and api
            String name = propertyMap.get(ADDON_NAME);
            if (name == null) {
                error = addonManifestWarning(ADDON_NAME);
                break;
            }

            String vendor = propertyMap.get(ADDON_VENDOR);
            if (vendor == null) {
                error = addonManifestWarning(ADDON_VENDOR);
                break;
            }

            String api = propertyMap.get(ADDON_API);
            PlatformTarget baseTarget = null;
            if (api == null) {
                error = addonManifestWarning(ADDON_API);
                break;
            }

            // Look for a platform that has a matching api level or codename.
            for (IAndroidTarget target : targetList) {
                if (target.isPlatform() && target.getVersion().equals(api)) {
                    baseTarget = (PlatformTarget)target;
                    break;
                }
            }

            if (baseTarget == null) {
                error = String.format("Unable to find base platform with API level '%1$s'", api);
                break;
            }

            // get the add-on revision
            String revision = propertyMap.get(ADDON_REVISION);
            if (revision == null) {
                revision = propertyMap.get(ADDON_REVISION_OLD);
            }
            if (revision != null) {
                try {
                    Integer.parseInt(revision);
                } catch (NumberFormatException e) {
                    // looks like apiNumber does not parse to a number.
                    error = String.format("%1$s is not a valid number in %2$s.",
                                ADDON_REVISION, SdkConstants.FN_BUILD_PROP);
                    break;
                }
            }

        } while(false);

        return Pair.of(propertyMap, error);
    }

    /**
* Converts a string representation of an hexadecimal ID into an int.
* @param value the string to convert.
* @return the int value, or {@link IAndroidTarget#NO_USB_ID} if the convertion failed.
//Synthetic comment -- @@ -577,16 +652,14 @@
}

/**
     * Prepares a warning about the addon being ignored due to a missing manifest value.
     * This string will show up in the SdkManager UI.
*
* @param valueName The missing manifest value, for display.
*/
    private static String addonManifestWarning(String valueName) {
        return String.format("'%1$s' is missing from %2$s.",
                valueName, SdkConstants.FN_MANIFEST_INI);
}

/**
//Synthetic comment -- @@ -603,7 +676,7 @@
File f = new File(platform, relativePath);
if (!f.exists()) {
log.warning(
                        "Ignoring platform '%1$s': %2$s is missing.",                  //$NON-NLS-1$
platform.getName(), relativePath);
return false;
}
//Synthetic comment -- @@ -650,7 +723,7 @@
* @param log Logger. Cannot be null.
*/
private void loadSamples(ISdkLog log) {
        File sampleFolder = new File(mOsSdkPath, SdkConstants.FD_SAMPLES);
if (sampleFolder.isDirectory()) {
File[] platforms  = sampleFolder.listFiles();

//Synthetic comment -- @@ -688,13 +761,13 @@

return new AndroidVersion(p);
} catch (FileNotFoundException e) {
            log.warning("Ignoring sample '%1$s': does not contain %2$s.",              //$NON-NLS-1$
folder.getName(), SdkConstants.FN_SOURCE_PROP);
} catch (IOException e) {
            log.warning("Ignoring sample '%1$s': failed reading %2$s.",                //$NON-NLS-1$
folder.getName(), SdkConstants.FN_SOURCE_PROP);
} catch (AndroidVersionException e) {
            log.warning("Ignoring sample '%1$s': no android version found in %2$s.",   //$NON-NLS-1$
folder.getName(), SdkConstants.FN_SOURCE_PROP);
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/BrokenPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/BrokenPackage.java
new file mode 100755
//Synthetic comment -- index 0000000..ba3c495

//Synthetic comment -- @@ -0,0 +1,400 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.internal.repository;

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.AndroidVersion.AndroidVersionException;
import com.android.sdklib.internal.repository.Archive.Arch;
import com.android.sdklib.internal.repository.Archive.Os;
import com.android.sdklib.repository.SdkRepoConstants;

import org.w3c.dom.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Properties;

/**
 * Represents an SDK repository package that is incomplete.
 * It has a distinct icon and a specific error that is supposed to help the user on how to fix it.
 */
public class BrokenPackage extends Package implements IMinApiLevelDependency {

    /**
     * The minimal API level required by this extra package, if > 0,
     * or {@link #MIN_API_LEVEL_NOT_SPECIFIED} if there is no such requirement.
     */
    private final int mMinApiLevel;

    /** The error describing why this package failed to load. */
    private final String mError;

    /**
     * Creates a new "broken" package that represents a package that we failed to load,
     * for whatever error indicated in <code>error</code>.
     * There is also an <em>optional</em> API level dependency that can be specified.
     * <p/>
     * By design, this creates a package with one and only one archive.
     */
    BrokenPackage(Properties props,
            String description,
            String error,
            int apiLevel,
            String archiveOsPath) {
        super(  null,                                   //source
                props,                                  //properties
                0,                                      //revision will be taken from props
                null,                                   //license
                description,                            //description
                null,                                   //descUrl
                Os.ANY,                                 //archiveOs
                Arch.ANY,                               //archiveArch
                archiveOsPath                           //archiveOsPath
                );
        mError = error;
        mMinApiLevel = apiLevel;
    }

    /**
     * Save the properties of the current packages in the given {@link Properties} object.
     * These properties will later be given to a constructor that takes a {@link Properties} object.
     * <p/>
     * Base implementation override: We don't actually save properties for a broken package.
     */
    @Override
    void saveProperties(Properties props) {
        // Nop. We don't actually save properties for a broken package.
    }

    /**
     * Returns the minimal API level required by this extra package, if > 0,
     * or {@link #MIN_API_LEVEL_NOT_SPECIFIED} if there is no such requirement.
     */
    public int getMinApiLevel() {
        return mMinApiLevel;
    }

    /** Returns a short description for an {@link IDescription}. */
    @Override
    public String getShortDescription() {
        String s = String.format("Samples for SDK API %1$s%2$s, revision %3$d%4$s",
                mVersion.getApiString(),
                mVersion.isPreview() ? " Preview" : "",
                getRevision(),
                isObsolete() ? " (Obsolete)" : "");
        return s;
    }

    /**
     * Returns a long description for an {@link IDescription}.
     *
     * The long description is whatever the XML contains for the &lt;description&gt; field,
     * or the short description if the former is empty.
     */
    @Override
    public String getLongDescription() {
        String s = getDescription();
        if (s == null || s.length() == 0) {
            s = getShortDescription();
        }

        if (s.indexOf("revision") == -1) {
            s += String.format("\nRevision %1$d%2$s",
                    getRevision(),
                    isObsolete() ? " (Obsolete)" : "");
        }

        return s;
    }

    /**
     * Computes a potential installation folder if an archive of this package were
     * to be installed right away in the given SDK root.
     * <p/>
     * A sample package is typically installed in SDK/samples/android-"version".
     * However if we can find a different directory that already has this sample
     * version installed, we'll use that one.
     *
     * @param osSdkRoot The OS path of the SDK root folder.
     * @param sdkManager An existing SDK manager to list current platforms and addons.
     * @return A new {@link File} corresponding to the directory to use to install this package.
     */
    @Override
    public File getInstallFolder(String osSdkRoot, SdkManager sdkManager) {

        // The /samples dir at the root of the SDK
        File samplesRoot = new File(osSdkRoot, SdkConstants.FD_SAMPLES);

        // First find if this platform is already installed. If so, reuse the same directory.
        for (IAndroidTarget target : sdkManager.getTargets()) {
            if (target.isPlatform() &&
                    target.getVersion().equals(mVersion)) {
                String p = target.getPath(IAndroidTarget.SAMPLES);
                File f = new File(p);
                if (f.isDirectory()) {
                    // We *only* use this directory if it's using the "new" location
                    // under SDK/samples. We explicitly do not reuse the "old" location
                    // under SDK/platform/android-N/samples.
                    if (f.getParentFile().equals(samplesRoot)) {
                        return f;
                    }
                }
            }
        }

        // Otherwise, get a suitable default
        File folder = new File(samplesRoot,
                String.format("android-%s", getVersion().getApiString())); //$NON-NLS-1$

        for (int n = 1; folder.exists(); n++) {
            // Keep trying till we find an unused directory.
            folder = new File(samplesRoot,
                    String.format("android-%s_%d", getVersion().getApiString(), n)); //$NON-NLS-1$
        }

        return folder;
    }

    @Override
    public boolean sameItemAs(Package pkg) {
        if (pkg instanceof BrokenPackage) {
            BrokenPackage newPkg = (BrokenPackage)pkg;

            // check they are the same platform.
            return newPkg.getVersion().equals(this.getVersion());
        }

        return false;
    }

    /**
     * Makes sure the base /samples folder exists before installing.
     *
     * {@inheritDoc}
     */
    @Override
    public boolean preInstallHook(Archive archive,
            ITaskMonitor monitor,
            String osSdkRoot,
            File installFolder) {
        File samplesRoot = new File(osSdkRoot, SdkConstants.FD_SAMPLES);
        if (!samplesRoot.isDirectory()) {
            samplesRoot.mkdir();
        }

        if (installFolder != null && installFolder.isDirectory()) {
            // Get the hash computed during the last installation
            String storedHash = readContentHash(installFolder);
            if (storedHash != null && storedHash.length() > 0) {

                // Get the hash of the folder now
                String currentHash = computeContentHash(installFolder);

                if (!storedHash.equals(currentHash)) {
                    // The hashes differ. The content was modified.
                    // Ask the user if we should still wipe the old samples.

                    String pkgName = archive.getParentPackage().getShortDescription();

                    String msg = String.format(
                            "-= Warning ! =-\n" +
                            "You are about to replace the content of the folder:\n " +
                            "  %1$s\n" +
                            "by the new package:\n" +
                            "  %2$s.\n" +
                            "\n" +
                            "However it seems that the content of the existing samples " +
                            "has been modified since it was last installed. Are you sure " +
                            "you want to DELETE the existing samples? This cannot be undone.\n" +
                            "Please select YES to delete the existing sample and replace them " +
                            "by the new ones.\n" +
                            "Please select NO to skip this package. You can always install it later.",
                            installFolder.getAbsolutePath(),
                            pkgName);

                    // Returns true if we can wipe & replace.
                    return monitor.displayPrompt("SDK Manager: overwrite samples?", msg);
                }
            }
        }

        // The default is to allow installation
        return super.preInstallHook(archive, monitor, osSdkRoot, installFolder);
    }

    /**
     * Computes a hash of the installed content (in case of successful install.)
     *
     * {@inheritDoc}
     */
    @Override
    public void postInstallHook(Archive archive, ITaskMonitor monitor, File installFolder) {
        super.postInstallHook(archive, monitor, installFolder);

        if (installFolder != null) {
            String h = computeContentHash(installFolder);
            saveContentHash(installFolder, h);
        }
    }

    /**
     * Reads the hash from the properties file, if it exists.
     * Returns null if something goes wrong, e.g. there's no property file or
     * it doesn't contain our hash. Returns an empty string if the hash wasn't
     * correctly computed last time by {@link #saveContentHash(File, String)}.
     */
    private String readContentHash(File folder) {
        Properties props = new Properties();

        FileInputStream fis = null;
        try {
            File f = new File(folder, SdkConstants.FN_CONTENT_HASH_PROP);
            if (f.isFile()) {
                fis = new FileInputStream(f);
                props.load(fis);
                return props.getProperty("content-hash", null);  //$NON-NLS-1$
            }
        } catch (Exception e) {
            // ignore
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }

    /**
     * Saves the hash using a properties file
     */
    private void saveContentHash(File folder, String hash) {
        Properties props = new Properties();

        props.setProperty("content-hash", hash == null ? "" : hash);  //$NON-NLS-1$ //$NON-NLS-2$

        FileOutputStream fos = null;
        try {
            File f = new File(folder, SdkConstants.FN_CONTENT_HASH_PROP);
            fos = new FileOutputStream(f);
            props.store( fos, "## Android - hash of this archive.");  //$NON-NLS-1$
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Computes a hash of the files names and sizes installed in the folder
     * using the SHA-1 digest.
     * Returns null if the digest algorithm is not available.
     */
    private String computeContentHash(File installFolder) {
        MessageDigest md = null;
        try {
            // SHA-1 is a standard algorithm.
            // http://java.sun.com/j2se/1.4.2/docs/guide/security/CryptoSpec.html#AppB
            md = MessageDigest.getInstance("SHA-1");    //$NON-NLS-1$
        } catch (NoSuchAlgorithmException e) {
            // We're unlikely to get there unless this JVM is not spec conforming
            // in which case there won't be any hash available.
        }

        if (md != null) {
            hashDirectoryContent(installFolder, md);
            return getDigestHexString(md);
        }

        return null;
    }

    /**
     * Computes a hash of the *content* of this directory. The hash only uses
     * the files names and the file sizes.
     */
    private void hashDirectoryContent(File folder, MessageDigest md) {
        if (folder == null || md == null || !folder.isDirectory()) {
            return;
        }

        for (File f : folder.listFiles()) {
            if (f.isDirectory()) {
                hashDirectoryContent(f, md);

            } else {
                String name = f.getName();

                // Skip the file we use to store the content hash
                if (name == null || SdkConstants.FN_CONTENT_HASH_PROP.equals(name)) {
                    continue;
                }

                try {
                    md.update(name.getBytes("UTF-8"));   //$NON-NLS-1$
                } catch (UnsupportedEncodingException e) {
                    // There is no valid reason for UTF-8 to be unsupported. Ignore.
                }
                try {
                    long len = f.length();
                    md.update((byte) (len & 0x0FF));
                    md.update((byte) ((len >> 8) & 0x0FF));
                    md.update((byte) ((len >> 16) & 0x0FF));
                    md.update((byte) ((len >> 24) & 0x0FF));

                } catch (SecurityException e) {
                    // Might happen if file is not readable. Ignore.
                }
            }
        }
    }

    /**
     * Returns a digest as an hex string.
     */
    private String getDigestHexString(MessageDigest digester) {
        // Create an hex string from the digest
        byte[] digest = digester.digest();
        int n = digest.length;
        String hex = "0123456789abcdef";                     //$NON-NLS-1$
        char[] hexDigest = new char[n * 2];
        for (int i = 0; i < n; i++) {
            int b = digest[i] & 0x0FF;
            hexDigest[i*2 + 0] = hex.charAt(b >>> 4);
            hexDigest[i*2 + 1] = hex.charAt(b & 0x0f);
        }

        return new String(hexDigest);
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LocalSdkParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LocalSdkParser.java
//Synthetic comment -- index 915a8e6..8655294 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.Archive.Arch;
import com.android.sdklib.internal.repository.Archive.Os;
import com.android.sdklib.util.Pair;

import java.io.File;
import java.io.FileInputStream;
//Synthetic comment -- @@ -29,6 +30,7 @@
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

//Synthetic comment -- @@ -102,7 +104,6 @@

// for platforms, add-ons and samples, rely on the SdkManager parser
for(IAndroidTarget target : sdkManager.getTargets()) {
Properties props = parseProperties(new File(target.getLocation(),
SdkConstants.FN_SOURCE_PROP));

//Synthetic comment -- @@ -138,6 +139,7 @@
}
}

        scanMissingAddons(sdkManager, visited, packages, log);
scanMissingSamples(osSdkRoot, visited, packages, log);
scanExtras(osSdkRoot, visited, packages, log);

//Synthetic comment -- @@ -229,6 +231,41 @@
}

/**
     * The sdk manager only lists valid addons. However here we also want to find "broken"
     * addons, i.e. addons that failed to load for some reason.
     * <p/>
     * Find any other sub-directories under the /add-ons root that hasn't been visited yet
     * and assume they contain broken addons.
     */
    private void scanMissingAddons(SdkManager sdkManager,
            HashSet<File> visited,
            ArrayList<Package> packages,
            ISdkLog log) {
        File addons = new File( new File(sdkManager.getLocation()), SdkConstants.FD_ADDONS);

        if (!addons.isDirectory()) {
            // It makes listFiles() return null so let's avoid it.
            return;
        }

        for (File dir : addons.listFiles()) {
            if (dir.isDirectory() && !visited.contains(dir)) {
                Pair<Map<String, String>, String> infos = 
                    SdkManager.parseAddonProperties(dir, sdkManager.getTargets(), log);

                String error = infos.getSecond();
                try {
                    SamplePackage pkg = new SamplePackage(dir.getAbsolutePath(), props);
                    packages.add(pkg);
                    visited.add(dir);
                } catch (Exception e) {
                    log.error(e, null);
                }
            }
        }
    }

    /**
* Try to find a tools package at the given location.
* Returns null if not found.
*/







