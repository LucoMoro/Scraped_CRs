/*Fix testEmptyGeneration so that it contains the right exec time.

Change-Id:Ic1ef7deeaa6873c1d63020c14afeef4726ea2c1e*/




//Synthetic comment -- diff --git a/ddmlib/src/test/java/com/android/ddmlib/testrunner/XmlTestRunListenerTest.java b/ddmlib/src/test/java/com/android/ddmlib/testrunner/XmlTestRunListenerTest.java
//Synthetic comment -- index f5672fa..39ce762 100644

//Synthetic comment -- @@ -19,13 +19,20 @@

import junit.framework.TestCase;

import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Collections;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
* Unit tests for {@link XmlTestRunListener}.
*/
//Synthetic comment -- @@ -97,13 +104,21 @@
*/
public void testEmptyGeneration() {
final String expectedOutput = "<?xml version='1.0' encoding='UTF-8' ?>" +
            "<testsuite name=\"test\" tests=\"0\" failures=\"0\" errors=\"0\" time=\"#TIMEVALUE#\" " +
"timestamp=\"ignore\" hostname=\"localhost\"> " +
"<properties />" +
"</testsuite>";
mResultReporter.testRunStarted("test", 1);
mResultReporter.testRunEnded(1, Collections.<String, String> emptyMap());

        // because the timestamp is impossible to hardcode, look for the actual timestamp and
        // replace it in the expected string.
        String output = getOutput();
        String time = getTime(output);
        assertNotNull(time);

        String expectedTimedOutput = expectedOutput.replaceFirst("#TIMEVALUE#", time);
        assertEquals(expectedTimedOutput, output);
}

/**
//Synthetic comment -- @@ -156,4 +171,24 @@
// replace two ws chars with one
return output.replaceAll("  ", " ");
}

    /**
     * Returns the value if the time attribute from the given XML content
     *
     * Actual XPATH: /testsuite/@time
     *
     * @param xml XML content.
     * @return
     */
    private String getTime(String xml) {
        XPath xpath = XPathFactory.newInstance().newXPath();

        try {
            return xpath.evaluate("/testsuite/@time", new InputSource(new StringReader(xml)));
        } catch (XPathExpressionException e) {
            // won't happen.
        }

        return null;
    }
}







