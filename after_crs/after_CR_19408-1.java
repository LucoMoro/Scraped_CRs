/*Silence console during tests

Change-Id:I44b1b87989fae6b27adf3778599ee52155da6e34*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkRepoSourceTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/SdkRepoSourceTest.java
//Synthetic comment -- index c92acde..a1a7cfc 100755

//Synthetic comment -- @@ -21,9 +21,11 @@
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

//Synthetic comment -- @@ -90,14 +92,31 @@
// Support an empty input
String str = "";
ByteArrayInputStream input = new ByteArrayInputStream(str.getBytes());
        ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        PrintStream myErr = new PrintStream(myOut);
        PrintStream oldErr = System.err;
        try {
            myOut.reset();
            System.setErr(myErr);
            result = mSource._findAlternateToolsXml(input);
        } finally {
            System.setErr(oldErr);
        }
assertNull(result);
        assertEquals("[Fatal Error] :1:1: Premature end of file.\n", myOut.toString());

// Support a random string as input
str = "Some random string, not even HTML nor XML";
input = new ByteArrayInputStream(str.getBytes());
        try {
            myOut.reset();
            System.setErr(myErr);
            result = mSource._findAlternateToolsXml(input);
        } finally {
            System.setErr(oldErr);
        }
assertNull(result);
        assertEquals("[Fatal Error] :1:1: Content is not allowed in prolog.\n", myOut.toString());

// Support an HTML input, e.g. a typical 404 document as returned by DL
str = "<html><head> " +
//Synthetic comment -- @@ -116,8 +135,16 @@
"<table width=100% cellpadding=0 cellspacing=0><tr><td bgcolor=\"#3366cc\"><img alt=\"\" width=1 height=4></td></tr></table> " +
"</body></html> ";
input = new ByteArrayInputStream(str.getBytes());
        try {
            myOut.reset();
            System.setErr(myErr);
            result = mSource._findAlternateToolsXml(input);
        } finally {
            System.setErr(oldErr);
        }
assertNull(result);
        assertEquals("[Fatal Error] :1:266: The element type \"meta\" must be terminated by the"
                + " matching end-tag \"</meta>\".\n", myOut.toString());

// Support some random XML document, totally unrelated to our sdk-repository schema
str = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
//Synthetic comment -- @@ -126,8 +153,15 @@
"    <application android:label=\"@string/app_name\" android:icon=\"@drawable/icon\"/>" +
"</manifest>";
input = new ByteArrayInputStream(str.getBytes());
        try {
            myOut.reset();
            System.setErr(myErr);
            result = mSource._findAlternateToolsXml(input);
        } finally {
            System.setErr(oldErr);
        }
assertNull(result);
        assertEquals("", myOut.toString());
}

/**







