/*Code cleanup in sdklib2 and manifmerger

Change-Id:I9f1ac118a24ada28daa9c07a30ac98da24938c2b*/




//Synthetic comment -- diff --git a/manifmerger/src/main/java/com/android/manifmerger/IMergerLog.java b/manifmerger/src/main/java/com/android/manifmerger/IMergerLog.java
//Synthetic comment -- index fb231a4..2066a83 100755

//Synthetic comment -- @@ -78,7 +78,7 @@
Object...msgParams);

/**
     * Information about the file and line number where an error occurred.
*/
public static class FileAndLine {
private final String mFilePath;








//Synthetic comment -- diff --git a/manifmerger/src/main/java/com/android/manifmerger/ManifestMerger.java b/manifmerger/src/main/java/com/android/manifmerger/ManifestMerger.java
//Synthetic comment -- index 616c2aa..c8e25e4 100755

//Synthetic comment -- @@ -738,7 +738,7 @@
/**
* Merge elements as identified by their key name attribute.
* The element must have an option boolean "required" attribute which can be either "true" or
     * "false". Default is true if the attribute is missing. When merging, a "false" is superseded
* by a "true" (explicit or implicit).
* <p/>
* When merging, this does NOT merge any other attributes than {@code keyAttr} and








//Synthetic comment -- diff --git a/manifmerger/src/main/java/com/android/manifmerger/MergerLog.java b/manifmerger/src/main/java/com/android/manifmerger/MergerLog.java
//Synthetic comment -- index 36ade15..043ffde 100755

//Synthetic comment -- @@ -93,7 +93,7 @@
String.format(message, msgParams));
break;
}
            }
};
}

//Synthetic comment -- @@ -144,7 +144,7 @@
}

parentLog.conflict(severity, location1, location2, message, msgParams);
            }
};
}









//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/SdkManager.java b/sdklib2/src/main/java/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 6839317..0a5e06b 100644

//Synthetic comment -- @@ -1077,7 +1077,7 @@
/**
* Converts a string representation of an hexadecimal ID into an int.
* @param value the string to convert.
     * @return the int value, or {@link IAndroidTarget#NO_USB_ID} if the conversion failed.
*/
private static int convertId(String value) {
if (value != null && value.length() > 0) {
//Synthetic comment -- @@ -1266,7 +1266,7 @@
@NonNull
private final File mDir;
private final long mDirModifiedTS;
        private final long mPropsModifiedTS;
private final long mPropsChecksum;

/**
//Synthetic comment -- @@ -1279,22 +1279,22 @@
mDirModifiedTS = dir.lastModified();

// Capture some info about the source.properties file if it exists.
            // We use propsModifiedTS == 0 to mean there is no props file.
long propsChecksum = 0;
            long propsModifiedTS = 0;
File props = new File(dir, SdkConstants.FN_SOURCE_PROP);
if (props.isFile()) {
                propsModifiedTS = props.lastModified();
propsChecksum = getFileChecksum(props);
}
            mPropsModifiedTS = propsModifiedTS;
mPropsChecksum = propsChecksum;
}

/**
* Checks whether the directory/source.properties attributes have changed.
*
         * @return True if the directory modified timestamp or
*  its source.property files have changed.
*/
public boolean hasChanged() {
//Synthetic comment -- @@ -1310,8 +1310,8 @@
File props = new File(mDir, SdkConstants.FN_SOURCE_PROP);

// The directory did not have a props file if target was null or
            // if mPropsModifiedTS is 0.
            boolean hadProps = mPropsModifiedTS != 0;

// Was there a props file and it vanished, or there wasn't and there's one now?
if (hadProps != props.isFile()) {
//Synthetic comment -- @@ -1319,8 +1319,8 @@
}

if (hadProps) {
                // Has source.props file modified-timestamp changed?
                if (mPropsModifiedTS != props.lastModified()) {
return true;
}
// Had the content of source.props changed?
//Synthetic comment -- @@ -1353,7 +1353,7 @@
if (fis != null) {
fis.close();
}
                } catch(Exception ignore) {}
}
return 0;
}
//Synthetic comment -- @@ -1362,9 +1362,9 @@
@Override
public String toString() {
String s = String.format("<DirInfo %1$s TS=%2$d", mDir, mDirModifiedTS);  //$NON-NLS-1$
            if (mPropsModifiedTS != 0) {
s += String.format(" | Props TS=%1$d, Chksum=%2$s",                   //$NON-NLS-1$
                        mPropsModifiedTS, mPropsChecksum);
}
return s + ">";                                                           //$NON-NLS-1$
}








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/build/ApkBuilder.java b/sdklib2/src/main/java/com/android/sdklib/build/ApkBuilder.java
//Synthetic comment -- index ae2856e..685cbc5 100644

//Synthetic comment -- @@ -42,7 +42,7 @@
* Class making the final apk packaging.
* The inputs are:
* - packaged resources (output of aapt)
 * - code file (output of dx)
* - Java resources coming from the project, its libraries, and its jar files
* - Native libraries from the project or its library.
*








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/build/ApkBuilderMain.java b/sdklib2/src/main/java/com/android/sdklib/build/ApkBuilderMain.java
//Synthetic comment -- index cbdf7d5..e6f6329 100644

//Synthetic comment -- @@ -193,7 +193,7 @@
System.err.println("    -v      Verbose.");
System.err.println("    -d      Debug Mode: Includes debug files in the APK file.");
System.err.println("    -u      Creates an unsigned package.");
        System.err.println("    -storetype Forces the KeyStore type. If omitted the default is used.");
System.err.println("");
System.err.println("    -z      Followed by the path to a zip archive.");
System.err.println("            Adds the content of the application package.");








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/build/JarListSanitizer.java b/sdklib2/src/main/java/com/android/sdklib/build/JarListSanitizer.java
//Synthetic comment -- index 4d1dcdb..b91c015 100644

//Synthetic comment -- @@ -218,7 +218,7 @@

Map<String, List<JarEntity>> nameMap = new HashMap<String, List<JarEntity>>();

        // update the current jar list if needed, while building a secondary map based on
// filename only.
for (File file : files) {
String path = file.getAbsolutePath();
//Synthetic comment -- @@ -242,7 +242,7 @@
}

try {
            // now look for duplicates. Each name list can have more than one file but they must
// have the same size/sha1
for (Entry<String, List<JarEntity>> entry : nameMap.entrySet()) {
List<JarEntity> list = entry.getValue();
//Synthetic comment -- @@ -369,7 +369,7 @@
writer = new OutputStreamWriter(
new FileOutputStream(cacheFile), "UTF-8");

            writer.write("# cache for current jar dependency. DO NOT EDIT.\n");
writer.write("# format is <lastModified> <length> <SHA-1> <path>\n");
writer.write("# Encoding is UTF-8\n");









//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/devices/DeviceManager.java b/sdklib2/src/main/java/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index c17a4df..ce7602c 100644

//Synthetic comment -- @@ -90,7 +90,7 @@
/**
* There is no device with the given name and manufacturer
*/
        MISSING
}

/**








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/devices/DeviceWriter.java b/sdklib2/src/main/java/com/android/sdklib/devices/DeviceWriter.java
//Synthetic comment -- index 11b1c6d..867430a 100644

//Synthetic comment -- @@ -177,7 +177,7 @@
addElement(doc, hardware, DeviceSchema.NODE_KEYBOARD, hw.getKeyboard().getResourceValue());
addElement(doc, hardware, DeviceSchema.NODE_NAV, hw.getNav().getResourceValue());

        Storage.Unit unit = hw.getRam().getAppropriateUnits();
Element ram = addElement(doc, hardware, DeviceSchema.NODE_RAM,
Long.toString(hw.getRam().getSizeAsUnit(unit)));
ram.setAttribute(DeviceSchema.ATTR_UNIT, unit.toString());
//Synthetic comment -- @@ -193,7 +193,7 @@

StringBuilder sb = new StringBuilder();
for (UiMode u : hw.getSupportedUiModes()) {
            sb.append('\n').append(u.getResourceValue());
}
addElement(doc, hardware, DeviceSchema.NODE_DOCK, sb.toString());

//Synthetic comment -- @@ -261,10 +261,10 @@
}

private static Element addElement(Document doc, Element parent, String tag,
            Collection<?> content) {
StringBuilder sb = new StringBuilder();
for (Object o : content) {
            sb.append('\n').append(o.toString());
}
return addElement(doc, parent,  tag, sb.toString());
}
//Synthetic comment -- @@ -279,14 +279,14 @@
// Get the lowest common unit (so if one piece of storage is 128KiB and another is 1MiB,
// use KiB for units)
for(Storage storage : content) {
            if(storage.getAppropriateUnits().getNumberOfBytes() < unit.getNumberOfBytes()) {
                unit = storage.getAppropriateUnits();
}
}

StringBuilder sb = new StringBuilder();
for(Storage storage : content) {
            sb.append('\n').append(storage.getSizeAsUnit(unit));
}
Element storage = addElement(doc, parent, tag, sb.toString());
storage.setAttribute(DeviceSchema.ATTR_UNIT, unit.toString());








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/devices/Storage.java b/sdklib2/src/main/java/com/android/sdklib/devices/Storage.java
//Synthetic comment -- index b30fe6e..72148eb 100644

//Synthetic comment -- @@ -109,7 +109,7 @@
* with no loss of accuracy.
* @return The most appropriate {@link Unit}.
*/
    public Unit getAppropriateUnits() {
Unit optimalUnit = Unit.B;
for(Unit unit : Unit.values()) {
if(mNoBytes % unit.getNumberOfBytes() == 0) {








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/avd/AvdInfo.java b/sdklib2/src/main/java/com/android/sdklib/internal/avd/AvdInfo.java
//Synthetic comment -- index 83aa2ef..c3b6329 100755

//Synthetic comment -- @@ -51,7 +51,7 @@
/** The {@link Device} this AVD is based on has changed from its original configuration*/
ERROR_DEVICE_CHANGED,
/** The {@link Device} this AVD is based on is no longer available */
        ERROR_DEVICE_MISSING
}

private final String mName;








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/project/ProjectCreator.java b/sdklib2/src/main/java/com/android/sdklib/internal/project/ProjectCreator.java
//Synthetic comment -- index 4bec0a9..6b0235a 100644

//Synthetic comment -- @@ -112,7 +112,7 @@
* error but not warnings. */
NORMAL,
/** Verbose mode. Project creation will display what's being done, errors and warnings. */
        VERBOSE
}

/**








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/project/ProjectPropertiesWorkingCopy.java b/sdklib2/src/main/java/com/android/sdklib/internal/project/ProjectPropertiesWorkingCopy.java
//Synthetic comment -- index 7bbd48d..4fd1eeb 100644

//Synthetic comment -- @@ -37,7 +37,7 @@
import java.util.regex.Matcher;

/**
 * A modifiable and savable copy of a {@link ProjectProperties}.
* <p/>This copy gives access to modification method such as {@link #setProperty(String, String)}
* and {@link #removeProperty(String)}.
*








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/AddonsListFetcher.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/AddonsListFetcher.java
//Synthetic comment -- index ac7b5d0..ac4309e 100755

//Synthetic comment -- @@ -511,7 +511,7 @@
}

/**
     * Parse all sites defined in the Addons list XML and returns an array of sites.
*
* @param doc The XML DOM to parse.
* @param nsUri The addons-list schema URI of the document.








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/ITaskMonitor.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/ITaskMonitor.java
//Synthetic comment -- index 74dd14a..4d953e6 100755

//Synthetic comment -- @@ -86,7 +86,7 @@
public void setProgressMax(int max);

/**
     * Returns the max value of the progress bar, as last set by {@link #setProgressMax(int)}.
* Returns 0 if the max has never been set yet.
*/
public int getProgressMax();








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/SdkStats.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/SdkStats.java
//Synthetic comment -- index 0301b5e..19b0cb1 100755

//Synthetic comment -- @@ -502,7 +502,7 @@
/**
* Parses all valid platforms found in the XML.
* Changes the stats array returned by {@link #getStats()}
     * (also returns the value directly, useful for unit tests.)
*/
@VisibleForTesting(visibility=Visibility.PRIVATE)
protected SparseArray<PlatformStat> parseStatsDocument(








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/archives/Archive.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/archives/Archive.java
//Synthetic comment -- index 508911f..7ddba1a 100755

//Synthetic comment -- @@ -53,7 +53,7 @@
private final String mAlgorithmName;

/**
         * Constructs a {@link ChecksumType} with the algorithm name
* suitable for {@link MessageDigest#getInstance(String)}.
* <p/>
* These names are officially documented at








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/packages/Package.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/packages/Package.java
//Synthetic comment -- index feab109..34f5e8a 100755

//Synthetic comment -- @@ -89,7 +89,7 @@
*  TODO: this name is confusing. We need to dig deeper. */
NOT_UPDATE,
/** Means that the 2 packages are the same thing, and one is the upgrade of the other */
        UPDATE
}

/**
//Synthetic comment -- @@ -480,7 +480,7 @@
* Should not be empty. Must never be null.
* <p/>
* Note that this is the "base" name for the package
     * with no specific revision nor API mentioned.
* In contrast, {@link #getShortDescription()} should be used if you want more details
* such as the package revision number or the API, if applicable.
*/








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/packages/PlatformToolPackage.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/packages/PlatformToolPackage.java
//Synthetic comment -- index c46e940..516717f 100755

//Synthetic comment -- @@ -239,7 +239,7 @@
return true;
} else {
// however previews can only match previews by default, unless we ignore that check.
                return pkg.getRevision().isPreview() ==
getRevision().isPreview();
}
}








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/packages/ToolPackage.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/packages/ToolPackage.java
//Synthetic comment -- index 8084c6b..a2250c8 100755

//Synthetic comment -- @@ -254,8 +254,7 @@
return true;
} else {
// however previews can only match previews by default, unless we ignore that check.
                return pkg.getRevision().isPreview() == getRevision().isPreview();
}
}
return false;








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/sources/SdkSource.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/sources/SdkSource.java
//Synthetic comment -- index 2558e71..56ff708 100755

//Synthetic comment -- @@ -483,7 +483,7 @@

// If we haven't already tried the alternate URL, let's do it now.
// We don't capture any fetch exception that happen during the second
                    // fetch in order to avoid hiding any previous fetch errors.
if (!url.endsWith(firstDefaultName)) {
if (!url.endsWith("/")) {       //$NON-NLS-1$
url += "/";                 //$NON-NLS-1$








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/sources/SdkSourceCategory.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/sources/SdkSourceCategory.java
//Synthetic comment -- index 5272cd5..fa6f232 100755

//Synthetic comment -- @@ -61,7 +61,7 @@
}

/**
     * Returns the UI-visible name of the category. Displayed in the available package tree.
* Cannot be null nor empty.
*/
public String getUiName() {








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/internal/repository/sources/SdkSourceProperties.java b/sdklib2/src/main/java/com/android/sdklib/internal/repository/sources/SdkSourceProperties.java
//Synthetic comment -- index cdd428f..23e8614 100755

//Synthetic comment -- @@ -169,7 +169,7 @@
// Nothing was loaded. Initialize the storage with a version
// identified. This isn't currently checked back, but we might
// want it later if we decide to change the way this works.
            // The version key is chosen on purpose to not match any valid URL.
sSourcesProperties.setProperty(KEY_VERSION, "1"); //$NON-NLS-1$ //$NON-NLS-2$
}
}








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/io/FileOp.java b/sdklib2/src/main/java/com/android/sdklib/io/FileOp.java
//Synthetic comment -- index c0b061f..aeb45e1 100755

//Synthetic comment -- @@ -57,9 +57,9 @@
boolean.class, boolean.class);

} catch (SecurityException e) {
            // do nothing we'll use chmod instead
} catch (NoSuchMethodException e) {
            // do nothing we'll use chmod instead
}
}

//Synthetic comment -- @@ -178,9 +178,6 @@
});
}

@Override
public void setReadOnly(File file) {
file.setReadOnly();








//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/util/ArrayUtils.java b/sdklib2/src/main/java/com/android/sdklib/util/ArrayUtils.java
//Synthetic comment -- index 20f9c01..787940b 100644

//Synthetic comment -- @@ -28,7 +28,7 @@
*/
/* package */ class ArrayUtils
{
    private static final Object[] EMPTY = new Object[0];
private static final int CACHE_SIZE = 73;
private static Object[] sCache = new Object[CACHE_SIZE];









//Synthetic comment -- diff --git a/sdklib2/src/main/java/com/android/sdklib/util/GrabProcessOutput.java b/sdklib2/src/main/java/com/android/sdklib/util/GrabProcessOutput.java
//Synthetic comment -- index 2935493..3d3734c 100755

//Synthetic comment -- @@ -71,7 +71,7 @@
* Get the stderr/stdout outputs of a process and return when the process is done.
* Both <b>must</b> be read or the process will block on windows.
*
     * @param process The process to get the output from.
* @param output Optional object to capture stdout/stderr.
*      Note that on Windows capturing the output is not optional. If output is null
*      the stdout/stderr will be captured and discarded.







