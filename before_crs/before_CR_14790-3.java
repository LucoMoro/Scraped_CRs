/*Add parsing of <uses-configuration> in the manifest.

Change-Id:Id6f4b58c5a811b0b7e32b40162c8bd6680f2fc67*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/resources/configurations/TextInputMethodQualifierTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/resources/configurations/TextInputMethodQualifierTest.java
//Synthetic comment -- index 04eb8da..e51c12d 100644

//Synthetic comment -- @@ -51,7 +51,7 @@
public void test12Key() {
assertEquals(true, timq.checkAndSet("12key", config)); //$NON-NLS-1$
assertTrue(config.getTextInputMethodQualifier() != null);
        assertEquals(Keyboard.TWELVEKEYS, config.getTextInputMethodQualifier().getValue());
assertEquals("12key", config.getTextInputMethodQualifier().toString()); //$NON-NLS-1$
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/resources/Keyboard.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/resources/Keyboard.java
//Synthetic comment -- index 1b910d4..914b205 100644

//Synthetic comment -- @@ -21,15 +21,16 @@
* <p/>This is used in the manifest in the uses-configuration node and in the resource folder names.
*/
public enum Keyboard {
    NOKEY("nokeys", "No Keys"), //$NON-NLS-1$
    QWERTY("qwerty", "Qwerty"), //$NON-NLS-1$
    TWELVEKEYS("12key", "12 Key"); //$NON-NLS-1$

    private String mValue;
private String mDisplayValue;

    private Keyboard(String value, String displayValue) {
mValue = value;
mDisplayValue = displayValue;
}

//Synthetic comment -- @@ -39,9 +40,10 @@
* @return the enum for the qualifier value or null if no matching was found.
*/
public static Keyboard getEnum(String value) {
        for (Keyboard orient : values()) {
            if (orient.mValue.equals(value)) {
                return orient;
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java
//Synthetic comment -- index 47340eb..13b4880 100644

//Synthetic comment -- @@ -46,7 +46,8 @@
public final static String NODE_USES_SDK = "uses-sdk";
public final static String NODE_INSTRUMENTATION = "instrumentation";
public final static String NODE_USES_LIBRARY = "uses-library";
    public static final String NODE_SUPPORTS_SCREENS = "supports-screens";

public final static String ATTRIBUTE_PACKAGE = "package";
public final static String ATTRIBUTE_VERSIONCODE = "versionCode";
//Synthetic comment -- @@ -61,6 +62,11 @@
public final static String ATTRIBUTE_SMALLSCREENS = "smallScreens";
public final static String ATTRIBUTE_NORMALSCREENS = "normalScreens";
public final static String ATTRIBUTE_LARGESCREENS = "largeScreens";

/**
* Returns the package for a given project.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java
//Synthetic comment -- index cd7bca4..afc8901 100644

//Synthetic comment -- @@ -20,9 +20,13 @@
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.IAbstractFolder;
import com.android.sdklib.io.StreamException;
import com.android.sdklib.xml.ManifestData.Activity;
import com.android.sdklib.xml.ManifestData.Instrumentation;
import com.android.sdklib.xml.ManifestData.SupportsScreens;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
//Synthetic comment -- @@ -76,8 +80,8 @@
/**
* XML error & data handler used when parsing the AndroidManifest.xml file.
* <p/>
     * This serves both as an {@link XmlErrorHandler} to report errors and as a data repository
     * to collect data from the manifest.
*/
private static class ManifestHandler extends DefaultHandler {

//Synthetic comment -- @@ -90,7 +94,7 @@
private Locator mLocator;

/**
         * Creates a new {@link ManifestHandler}, which is also an {@link XmlErrorHandler}.
*
* @param manifestFile The manifest file being parsed. Can be null.
* @param errorListener An optional error listener.
//Synthetic comment -- @@ -173,8 +177,13 @@
true /* hasNamespace */);
} else if (AndroidManifest.NODE_INSTRUMENTATION.equals(localName)) {
processInstrumentationNode(attributes);
} else if (AndroidManifest.NODE_SUPPORTS_SCREENS.equals(localName)) {
processSupportsScreensNode(attributes);
}
break;
case LEVEL_ACTIVITY:
//Synthetic comment -- @@ -428,6 +437,30 @@
}

/**
* Searches through the attributes list for a particular one and returns its value.
* @param attributes the attribute list to search through
* @param attributeName the name of the attribute to look for.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java
//Synthetic comment -- index 5101736..8abcd2a 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package com.android.sdklib.xml;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
//Synthetic comment -- @@ -45,6 +49,7 @@
final ArrayList<String> mLibraries = new ArrayList<String>();

SupportsScreens mSupportsScreens;

/**
* Instrumentation info obtained from manifest
//Synthetic comment -- @@ -158,6 +163,29 @@
public final static class UsesConfiguration {
Boolean mReqFiveWayNav;
Boolean mReqHardKeyboard;
}

/**
//Synthetic comment -- @@ -239,6 +267,10 @@
return mSupportsScreens;
}

void addProcessName(String processName) {
if (mProcesses == null) {
mProcesses = new TreeSet<String>();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/AndroidManifestParserTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/AndroidManifestParserTest.java
//Synthetic comment -- index 647aa74..80e04c0 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package com.android.sdklib.xml;

import java.io.InputStream;

import junit.framework.TestCase;
//Synthetic comment -- @@ -102,6 +106,16 @@
assertEquals(Boolean.TRUE, supportsScreens.getLargeScreens());
}

private void assertEquals(ManifestData.Activity lhs, ManifestData.Activity rhs) {
assertTrue(lhs == rhs || (lhs != null && rhs != null));
if (lhs != null && rhs != null) {







