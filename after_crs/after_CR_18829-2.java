/*Whitelist for DebuggableTest#testNoDebuggable

Bug 3166146

Add a whitelist to let exceptions for Froyo through. In a
future release, there will be no whitelist.

Change-Id:I2cce1cd82daa2116dfa2e5072ebb1c4bfd0b2fc7*/




//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/DebuggableTest.java b/tests/tests/permission/src/android/permission/cts/DebuggableTest.java
//Synthetic comment -- index fe4ed57..58751a5 100644

//Synthetic comment -- @@ -20,7 +20,10 @@
import android.content.pm.PackageManager;
import android.test.AndroidTestCase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* Verify that pre-installed packages don't have the debuggable
//Synthetic comment -- @@ -29,14 +32,21 @@
*/
public class DebuggableTest extends AndroidTestCase {

    // Remove whitelist in future release.
    private static final Set<String> WHITELISTED_APPS = new HashSet<String>(Arrays.asList(
            "com.google.android.apps.uploader"
            ));

public void testNoDebuggable() {
List<ApplicationInfo> apps = getContext()
.getPackageManager()
.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
for (ApplicationInfo app : apps) {
String appName = app.packageName;
            if (!WHITELISTED_APPS.contains(appName)) {
                assertTrue("Package " + appName + " is marked as debuggable.",
                        (app.flags & ApplicationInfo.FLAG_DEBUGGABLE) == 0);
            }
}
}
}







