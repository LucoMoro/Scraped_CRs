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
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
//Synthetic comment -- @@ -742,8 +742,8 @@
Element destUsesSdk = findFirstElement(mMainDoc, "/manifest/uses-sdk");  //$NON-NLS-1$
Element srcUsesSdk  = findFirstElement(libDoc,   "/manifest/uses-sdk");  //$NON-NLS-1$

        AtomicReference<Object> destValue = new AtomicReference<Object>(1); // String or Integer
        AtomicReference<Object> srcValue  = new AtomicReference<Object>(1);
AtomicBoolean destImplied = new AtomicBoolean(true);
AtomicBoolean srcImplied = new AtomicBoolean(true);

//Synthetic comment -- @@ -756,17 +756,16 @@
destValue, srcValue,
destImplied, srcImplied);

        if (result && destValue.get() instanceof Integer && srcValue.get() instanceof Integer) {
// Make it an error for an application to use a library with a greater
// minSdkVersion. This means the library code may crash unexpectedly.
// TODO it would be nice to be able to work around this in case the
// user think s/he knows what s/he's doing.
// We could define a simple XML comment flag: <!-- @NoMinSdkVersionMergeError -->

            destMinSdk = (Integer) destValue.get();

            int srcMinSdk = (Integer) srcValue.get();
            if (destMinSdk < srcMinSdk) {
mLog.conflict(Severity.ERROR,
xmlFileAndLine(destUsesSdk == null ? mMainDoc : destUsesSdk),
xmlFileAndLine(srcUsesSdk == null ? libDoc : srcUsesSdk),
//Synthetic comment -- @@ -795,14 +794,13 @@
destImplied, srcImplied);

result &= result2;
        if (result2 && destValue.get() instanceof Integer && srcValue.get() instanceof Integer) {
// Make it a warning for an application to use a library with a greater
// targetSdkVersion.

            int destTargetSdk = destImplied.get() ? destMinSdk : (Integer) destValue.get();

            int srcMinSdk = (Integer) srcValue.get();
            if (destTargetSdk < srcMinSdk) {
mLog.conflict(Severity.WARNING,
xmlFileAndLine(destUsesSdk == null ? mMainDoc : destUsesSdk),
xmlFileAndLine(srcUsesSdk == null ? libDoc : srcUsesSdk),
//Synthetic comment -- @@ -833,8 +831,8 @@
Element destUsesSdk,
Element srcUsesSdk,
String attr,
            AtomicReference<Object> destValue,
            AtomicReference<Object> srcValue,
AtomicBoolean destImplied,
AtomicBoolean srcImplied) {
String s = destUsesSdk == null ? ""                                      //$NON-NLS-1$
//Synthetic comment -- @@ -848,9 +846,14 @@
destImplied.set(false);
}
} catch (NumberFormatException e) {
            // Versions can contain codenames such as "JellyBean"
            destValue.set(s);
            destImplied.set(false);
}

s = srcUsesSdk == null ? ""                                      //$NON-NLS-1$
//Synthetic comment -- @@ -863,9 +866,12 @@
srcImplied.set(false);
}
} catch (NumberFormatException e) {
            // Versions can contain codenames such as "JellyBean"
            destValue.set(s);
            destImplied.set(false);
}

return true;







