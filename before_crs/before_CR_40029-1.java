/*Fix manifest merger for non-integer values

The minSdkVersion and targetSdkVersion attributes in a manifest file
are allowed to contain non-numeric strings - they can contain
codenames such as "JellyBean", when you're targeting a particular
preview SDK.

The manifest merger would fail to merge manifests where one or more of
these attributes weren't numeric, which (for example) meant creating a
new project and setting the minSdkVersion to a preview platform would
create a non-merged manifest file.

Change-Id:Idf0299f21beac003abc3bf55be657b9398c1e535*/
//Synthetic comment -- diff --git a/manifmerger/src/com/android/manifmerger/ManifestMerger.java b/manifmerger/src/com/android/manifmerger/ManifestMerger.java
//Synthetic comment -- index 229faa3..d63ecd8 100755

//Synthetic comment -- @@ -36,7 +36,7 @@
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
//Synthetic comment -- @@ -742,8 +742,8 @@
Element destUsesSdk = findFirstElement(mMainDoc, "/manifest/uses-sdk");  //$NON-NLS-1$
Element srcUsesSdk  = findFirstElement(libDoc,   "/manifest/uses-sdk");  //$NON-NLS-1$

        AtomicInteger destValue = new AtomicInteger(1);
        AtomicInteger srcValue  = new AtomicInteger(1);
AtomicBoolean destImplied = new AtomicBoolean(true);
AtomicBoolean srcImplied = new AtomicBoolean(true);

//Synthetic comment -- @@ -756,16 +756,17 @@
destValue, srcValue,
destImplied, srcImplied);

        if (result) {
// Make it an error for an application to use a library with a greater
// minSdkVersion. This means the library code may crash unexpectedly.
// TODO it would be nice to be able to work around this in case the
// user think s/he knows what s/he's doing.
// We could define a simple XML comment flag: <!-- @NoMinSdkVersionMergeError -->

            destMinSdk = destValue.get();

            if (destMinSdk < srcValue.get()) {
mLog.conflict(Severity.ERROR,
xmlFileAndLine(destUsesSdk == null ? mMainDoc : destUsesSdk),
xmlFileAndLine(srcUsesSdk == null ? libDoc : srcUsesSdk),
//Synthetic comment -- @@ -794,13 +795,14 @@
destImplied, srcImplied);

result &= result2;
        if (result2) {
// Make it a warning for an application to use a library with a greater
// targetSdkVersion.

            int destTargetSdk = destImplied.get() ? destMinSdk : destValue.get();

            if (destTargetSdk < srcValue.get()) {
mLog.conflict(Severity.WARNING,
xmlFileAndLine(destUsesSdk == null ? mMainDoc : destUsesSdk),
xmlFileAndLine(srcUsesSdk == null ? libDoc : srcUsesSdk),
//Synthetic comment -- @@ -831,8 +833,8 @@
Element destUsesSdk,
Element srcUsesSdk,
String attr,
            AtomicInteger destValue,
            AtomicInteger srcValue,
AtomicBoolean destImplied,
AtomicBoolean srcImplied) {
String s = destUsesSdk == null ? ""                                      //$NON-NLS-1$
//Synthetic comment -- @@ -846,14 +848,9 @@
destImplied.set(false);
}
} catch (NumberFormatException e) {
            // Note: NumberFormatException.toString() has no interesting information
            // so we don't output it.
            mLog.error(Severity.ERROR,
                    xmlFileAndLine(destUsesSdk == null ? mMainDoc : destUsesSdk),
                    "Failed to parse <uses-sdk %1$sSdkVersion='%2$s'>: must be an integer number.",
                    attr,
                    s);
            return false;
}

s = srcUsesSdk == null ? ""                                      //$NON-NLS-1$
//Synthetic comment -- @@ -866,12 +863,9 @@
srcImplied.set(false);
}
} catch (NumberFormatException e) {
            mLog.error(Severity.ERROR,
                    xmlFileAndLine(srcUsesSdk == null ? libDoc : srcUsesSdk),
                    "Failed to parse <uses-sdk %1$sSdkVersion='%2$s'>: must be an integer number.",
                    attr,
                    s);
            return false;
}

return true;







