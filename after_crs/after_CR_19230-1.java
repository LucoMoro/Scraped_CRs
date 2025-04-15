/*Fix android.net.cts.ProxyTest

Remove the code that was trying to write to secure settings
and take off the BrokenTest annotation.

Change-Id:I0759db38225a9822b25c987e66a7590c555b5e9e*/




//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/ProxyTest.java b/tests/tests/net/src/android/net/cts/ProxyTest.java
//Synthetic comment -- index 357935a..184a07c 100644

//Synthetic comment -- @@ -16,29 +16,17 @@

package android.net.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.net.Proxy;
import android.test.AndroidTestCase;

@TestTargetClass(Proxy.class)
public class ProxyTest extends AndroidTestCase {

@TestTargetNew(
level = TestLevel.COMPLETE,
method = "Proxy",
//Synthetic comment -- @@ -59,18 +47,7 @@
method = "getDefaultHost",
args = {}
),
})
public void testAccessProperties() {
final int minValidPort = 0;
final int maxValidPort = 65535;
//Synthetic comment -- @@ -80,12 +57,5 @@
} else {
assertTrue(defaultPort >= minValidPort && defaultPort <= maxValidPort);
}
}
}







