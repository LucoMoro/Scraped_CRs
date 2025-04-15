/*dependent change for "parse instrumentation result bundles" in sdk

Change-Id:Ie3708d30620a703909829c32f5599816cc52b3f6*/




//Synthetic comment -- diff --git a/core/tests/hosttests/src/android/content/pm/PackageManagerHostTestUtils.java b/core/tests/hosttests/src/android/content/pm/PackageManagerHostTestUtils.java
//Synthetic comment -- index 91cbe2f..4cb50ce 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import java.io.StringReader;
import java.lang.Runtime;
import java.lang.Process;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -468,7 +469,7 @@
mAllTestsPassed = false;
}

        public void testRunEnded(long elapsedTime, Map<String, String> resultBundle) {
// ignore
}








