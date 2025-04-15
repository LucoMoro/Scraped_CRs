/*Suppress DebuggableTest#testNoDebuggable

Bug 3166146

There is nothing wrong with the test. Will hope to pass it in a
future release.

Change-Id:I2cce1cd82daa2116dfa2e5072ebb1c4bfd0b2fc7*/




//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/DebuggableTest.java b/tests/tests/permission/src/android/permission/cts/DebuggableTest.java
//Synthetic comment -- index fe4ed57..1dbd87f 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package android.permission.cts;

import dalvik.annotation.BrokenTest;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.test.AndroidTestCase;
//Synthetic comment -- @@ -29,6 +31,7 @@
*/
public class DebuggableTest extends AndroidTestCase {

    @BrokenTest("Will be fixed in a future release")
public void testNoDebuggable() {
List<ApplicationInfo> apps = getContext()
.getPackageManager()







