/*dependent change for "parse instrumentation result bundles" in sdk

Change-Id:Ifc07399b633d267ff7e71e1b31c5ad614ac4bb53*/




//Synthetic comment -- diff --git a/tests/appsecurity-tests/src/com/android/cts/appsecurity/AppSecurityTests.java b/tests/appsecurity-tests/src/com/android/cts/appsecurity/AppSecurityTests.java
//Synthetic comment -- index a4f85a1..1928a09 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import java.io.File;
import java.io.IOException;
import java.util.Map;

import junit.framework.Test;

//Synthetic comment -- @@ -276,7 +277,7 @@
mAllTestsPassed = false;
}

        public void testRunEnded(long elapsedTime, Map<String, String> resultBundle) {
// ignore
}








