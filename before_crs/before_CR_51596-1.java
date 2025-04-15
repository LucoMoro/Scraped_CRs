/*Fix testEmptyGeneration so that it contains the right exec time.

Change-Id:Ic1ef7deeaa6873c1d63020c14afeef4726ea2c1e*/
//Synthetic comment -- diff --git a/ddmlib/src/test/java/com/android/ddmlib/testrunner/XmlTestRunListenerTest.java b/ddmlib/src/test/java/com/android/ddmlib/testrunner/XmlTestRunListenerTest.java
//Synthetic comment -- index f5672fa..39ce762 100644

//Synthetic comment -- @@ -19,13 +19,20 @@

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

/**
* Unit tests for {@link XmlTestRunListener}.
*/
//Synthetic comment -- @@ -97,13 +104,21 @@
*/
public void testEmptyGeneration() {
final String expectedOutput = "<?xml version='1.0' encoding='UTF-8' ?>" +
            "<testsuite name=\"test\" tests=\"0\" failures=\"0\" errors=\"0\" time=\"1\" " +
"timestamp=\"ignore\" hostname=\"localhost\"> " +
"<properties />" +
"</testsuite>";
mResultReporter.testRunStarted("test", 1);
mResultReporter.testRunEnded(1, Collections.<String, String> emptyMap());
        assertEquals(expectedOutput, getOutput());
}

/**
//Synthetic comment -- @@ -156,4 +171,24 @@
// replace two ws chars with one
return output.replaceAll("  ", " ");
}
}







