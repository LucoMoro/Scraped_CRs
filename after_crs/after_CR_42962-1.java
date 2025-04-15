/*Revert "Fix manifest merger for non-integer values"

This reverts commit 0ecd33255e76b00a45c79216c744cb624ba61756.
The fix breaks unit tests and is in the way of the changes
I want to do.*/




//Synthetic comment -- diff --git a/manifmerger/src/com/android/manifmerger/ManifestMerger.java b/manifmerger/src/com/android/manifmerger/ManifestMerger.java
//Synthetic comment -- index c5153e9..8a61e67 100755

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

//Synthetic comment -- @@ -756,17 +756,16 @@
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
//Synthetic comment -- @@ -795,14 +794,13 @@
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
//Synthetic comment -- @@ -833,8 +831,8 @@
Element destUsesSdk,
Element srcUsesSdk,
String attr,
            AtomicInteger destValue,
            AtomicInteger srcValue,
AtomicBoolean destImplied,
AtomicBoolean srcImplied) {
String s = destUsesSdk == null ? ""                                      //$NON-NLS-1$
//Synthetic comment -- @@ -848,9 +846,14 @@
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
//Synthetic comment -- @@ -863,9 +866,12 @@
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







