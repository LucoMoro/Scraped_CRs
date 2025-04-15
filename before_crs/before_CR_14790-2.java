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
//Synthetic comment -- index cd7bca4..2f512b0 100644

//Synthetic comment -- @@ -20,9 +20,13 @@
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.IAbstractFolder;
import com.android.sdklib.io.StreamException;
import com.android.sdklib.xml.ManifestData.Activity;
import com.android.sdklib.xml.ManifestData.Instrumentation;
import com.android.sdklib.xml.ManifestData.SupportsScreens;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
//Synthetic comment -- @@ -43,17 +47,23 @@
public class AndroidManifestParser {

private final static int LEVEL_MANIFEST = 0;
private final static int LEVEL_APPLICATION = 1;
private final static int LEVEL_ACTIVITY = 2;
private final static int LEVEL_INTENT_FILTER = 3;
private final static int LEVEL_CATEGORY = 4;

private final static String ACTION_MAIN = "android.intent.action.MAIN"; //$NON-NLS-1$
private final static String CATEGORY_LAUNCHER = "android.intent.category.LAUNCHER"; //$NON-NLS-1$

public interface ManifestErrorHandler extends ErrorHandler {
/**
* Handles a parsing error and an optional line number.
* @param exception
* @param lineNumber
*/
//Synthetic comment -- @@ -63,11 +73,13 @@
* Checks that a class is valid and can be used in the Android Manifest.
* <p/>
* Errors are put as {@link IMarker} on the manifest file.
* @param locator
* @param className the fully qualified name of the class to test.
         * @param superClassName the fully qualified name of the class it is supposed to extend.
         * @param testVisibility if <code>true</code>, the method will check the visibility of
         * the class or of its constructors.
*/
void checkClass(Locator locator, String className, String superClassName,
boolean testVisibility);
//Synthetic comment -- @@ -76,27 +88,34 @@
/**
* XML error & data handler used when parsing the AndroidManifest.xml file.
* <p/>
     * This serves both as an {@link XmlErrorHandler} to report errors and as a data repository
     * to collect data from the manifest.
*/
private static class ManifestHandler extends DefaultHandler {

        //--- temporary data/flags used during parsing
private final ManifestData mManifestData;
private final ManifestErrorHandler mErrorHandler;
private int mCurrentLevel = 0;
private int mValidLevel = 0;
private Activity mCurrentActivity = null;
private Locator mLocator;

/**
         * Creates a new {@link ManifestHandler}, which is also an {@link XmlErrorHandler}.
*
* @param manifestFile The manifest file being parsed. Can be null.
* @param errorListener An optional error listener.
* @param gatherData True if data should be gathered.
         * @param javaProject The java project holding the manifest file. Can be null.
         * @param markErrors True if errors should be marked as Eclipse Markers on the resource.
*/
ManifestHandler(IAbstractFile manifestFile, ManifestData manifestData,
ManifestErrorHandler errorHandler) {
//Synthetic comment -- @@ -105,8 +124,11 @@
mErrorHandler = errorHandler;
}

        /* (non-Javadoc)
         * @see org.xml.sax.helpers.DefaultHandler#setDocumentLocator(org.xml.sax.Locator)
*/
@Override
public void setDocumentLocator(Locator locator) {
//Synthetic comment -- @@ -114,9 +136,11 @@
super.setDocumentLocator(locator);
}

        /* (non-Javadoc)
         * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String,
         * java.lang.String, org.xml.sax.Attributes)
*/
@Override
public void startElement(String uri, String localName, String name, Attributes attributes)
//Synthetic comment -- @@ -134,8 +158,7 @@
if (AndroidManifest.NODE_MANIFEST.equals(localName)) {
// lets get the package name.
mManifestData.mPackage = getAttributeValue(attributes,
                                        AndroidManifest.ATTRIBUTE_PACKAGE,
                                        false /* hasNamespace */);

// and the versionCode
String tmp = getAttributeValue(attributes,
//Synthetic comment -- @@ -153,15 +176,13 @@
case LEVEL_APPLICATION:
if (AndroidManifest.NODE_APPLICATION.equals(localName)) {
value = getAttributeValue(attributes,
                                        AndroidManifest.ATTRIBUTE_PROCESS,
                                        true /* hasNamespace */);
if (value != null) {
mManifestData.addProcessName(value);
}

value = getAttributeValue(attributes,
                                        AndroidManifest.ATTRIBUTE_DEBUGGABLE,
                                        true /* hasNamespace*/);
if (value != null) {
mManifestData.mDebuggable = Boolean.parseBoolean(value);
}
//Synthetic comment -- @@ -169,12 +190,16 @@
mValidLevel++;
} else if (AndroidManifest.NODE_USES_SDK.equals(localName)) {
mManifestData.mApiLevelRequirement = getAttributeValue(attributes,
                                        AndroidManifest.ATTRIBUTE_MIN_SDK_VERSION,
                                        true /* hasNamespace */);
} else if (AndroidManifest.NODE_INSTRUMENTATION.equals(localName)) {
processInstrumentationNode(attributes);
} else if (AndroidManifest.NODE_SUPPORTS_SCREENS.equals(localName)) {
processSupportsScreensNode(attributes);
}
break;
case LEVEL_ACTIVITY:
//Synthetic comment -- @@ -192,8 +217,7 @@
mValidLevel++;
} else if (AndroidManifest.NODE_USES_LIBRARY.equals(localName)) {
value = getAttributeValue(attributes,
                                        AndroidManifest.ATTRIBUTE_NAME,
                                        true /* hasNamespace */);
if (value != null) {
mManifestData.mLibraries.add(value);
}
//Synthetic comment -- @@ -201,8 +225,8 @@
break;
case LEVEL_INTENT_FILTER:
// only process this level if we are in an activity
                            if (mCurrentActivity != null &&
                                    AndroidManifest.NODE_INTENT.equals(localName)) {
mCurrentActivity.resetIntentFilter();
mValidLevel++;
}
//Synthetic comment -- @@ -212,23 +236,22 @@
if (AndroidManifest.NODE_ACTION.equals(localName)) {
// get the name attribute
String action = getAttributeValue(attributes,
                                            AndroidManifest.ATTRIBUTE_NAME,
                                            true /* hasNamespace */);
if (action != null) {
mCurrentActivity.setHasAction(true);
                                        mCurrentActivity.setHasMainAction(
                                                ACTION_MAIN.equals(action));
}
} else if (AndroidManifest.NODE_CATEGORY.equals(localName)) {
String category = getAttributeValue(attributes,
                                            AndroidManifest.ATTRIBUTE_NAME,
                                            true /* hasNamespace */);
if (CATEGORY_LAUNCHER.equals(category)) {
mCurrentActivity.setHasLauncherCategory(true);
}
}

                                // no need to increase mValidLevel as we don't process anything
// below this level.
}
break;
//Synthetic comment -- @@ -241,9 +264,10 @@
}
}

        /* (non-Javadoc)
         * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String,
         * java.lang.String)
*/
@Override
public void endElement(String uri, String localName, String name) throws SAXException {
//Synthetic comment -- @@ -266,12 +290,12 @@
mCurrentActivity = null;
break;
case LEVEL_INTENT_FILTER:
                            // if we found both a main action and a launcher category, this is our
// launcher activity!
                            if (mManifestData.mLauncherActivity == null &&
                                    mCurrentActivity != null &&
                                    mCurrentActivity.isHomeActivity() &&
                                    mCurrentActivity.isExported()) {
mManifestData.mLauncherActivity = mCurrentActivity;
}
break;
//Synthetic comment -- @@ -285,8 +309,11 @@
}
}

        /* (non-Javadoc)
         * @see org.xml.sax.helpers.DefaultHandler#error(org.xml.sax.SAXParseException)
*/
@Override
public void error(SAXParseException e) {
//Synthetic comment -- @@ -295,8 +322,10 @@
}
}

        /* (non-Javadoc)
         * @see org.xml.sax.helpers.DefaultHandler#fatalError(org.xml.sax.SAXParseException)
*/
@Override
public void fatalError(SAXParseException e) {
//Synthetic comment -- @@ -305,8 +334,11 @@
}
}

        /* (non-Javadoc)
         * @see org.xml.sax.helpers.DefaultHandler#warning(org.xml.sax.SAXParseException)
*/
@Override
public void warning(SAXParseException e) throws SAXException {
//Synthetic comment -- @@ -317,6 +349,7 @@

/**
* Processes the activity node.
* @param attributes the attributes for the activity node.
*/
private void processActivityNode(Attributes attributes) {
//Synthetic comment -- @@ -330,8 +363,7 @@
// get the exported flag.
String exportedStr = getAttributeValue(attributes,
AndroidManifest.ATTRIBUTE_EXPORTED, true);
                boolean exported = exportedStr == null ||
                        exportedStr.toLowerCase().equals("true"); // $NON-NLS-1$
mCurrentActivity = new Activity(activityName, exported);
mManifestData.mActivities.add(mCurrentActivity);

//Synthetic comment -- @@ -354,21 +386,21 @@

/**
* Processes the service/receiver/provider nodes.
* @param attributes the attributes for the activity node.
         * @param superClassName the fully qualified name of the super class that this
         * node is representing
*/
private void processNode(Attributes attributes, String superClassName) {
// lets get the class name, and check it if required.
            String serviceName = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_NAME,
                    true /* hasNamespace */);
if (serviceName != null) {
serviceName = AndroidManifest.combinePackageAndClassName(mManifestData.mPackage,
serviceName);

if (mErrorHandler != null) {
                    mErrorHandler.checkClass(mLocator, serviceName, superClassName,
                            false /* testVisibility */);
}
}

//Synthetic comment -- @@ -381,21 +413,20 @@

/**
* Processes the instrumentation node.
* @param attributes the attributes for the instrumentation node.
*/
private void processInstrumentationNode(Attributes attributes) {
// lets get the class name, and check it if required.
String instrumentationName = getAttributeValue(attributes,
                    AndroidManifest.ATTRIBUTE_NAME,
                    true /* hasNamespace */);
if (instrumentationName != null) {
String instrClassName = AndroidManifest.combinePackageAndClassName(
mManifestData.mPackage, instrumentationName);
String targetPackage = getAttributeValue(attributes,
                        AndroidManifest.ATTRIBUTE_TARGET_PACKAGE,
                        true /* hasNamespace */);
                mManifestData.mInstrumentations.add(
                        new Instrumentation(instrClassName, targetPackage));
if (mErrorHandler != null) {
mErrorHandler.checkClass(mLocator, instrClassName,
SdkConstants.CLASS_INSTRUMENTATION, true /* testVisibility */);
//Synthetic comment -- @@ -405,44 +436,66 @@

/**
* Processes the supports-screens node.
* @param attributes the attributes for the supports-screens node.
*/
private void processSupportsScreensNode(Attributes attributes) {
mManifestData.mSupportsScreens = new SupportsScreens();

            mManifestData.mSupportsScreens.mResizeable = Boolean.valueOf(
                    getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_RESIZABLE, true /*hasNamespace*/));
            mManifestData.mSupportsScreens.mAnyDensity = Boolean.valueOf(
                    getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_ANYDENSITY, true /*hasNamespace*/));
            mManifestData.mSupportsScreens.mSmallScreens = Boolean.valueOf(
                    getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_SMALLSCREENS, true /*hasNamespace*/));
            mManifestData.mSupportsScreens.mNormalScreens = Boolean.valueOf(
                    getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_NORMALSCREENS, true /*hasNamespace*/));
            mManifestData.mSupportsScreens.mLargeScreens = Boolean.valueOf(
                    getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_LARGESCREENS, true /*hasNamespace*/));
}

/**
         * Searches through the attributes list for a particular one and returns its value.
* @param attributes the attribute list to search through
* @param attributeName the name of the attribute to look for.
         * @param hasNamespace Indicates whether the attribute has an android namespace.
         * @return a String with the value or null if the attribute was not found.
* @see SdkConstants#NS_RESOURCES
*/
private String getAttributeValue(Attributes attributes, String attributeName,
boolean hasNamespace) {
int count = attributes.getLength();
            for (int i = 0 ; i < count ; i++) {
                if (attributeName.equals(attributes.getLocalName(i)) &&
                        ((hasNamespace &&
                                SdkConstants.NS_RESOURCES.equals(attributes.getURI(i))) ||
                                (hasNamespace == false && attributes.getURI(i).length() == 0))) {
return attributes.getValue(i);
}
}
//Synthetic comment -- @@ -460,12 +513,13 @@
}

/**
     * Parses the Android Manifest, and returns a {@link ManifestData} object containing the
     * result of the parsing.
*
     * @param manifestFile the {@link IAbstractFile} representing the manifest file.
     * @param gatherData indicates whether the parsing will extract data from the manifest. If false
     * the method will always return null.
* @param errorHandler an optional errorHandler.
* @return
* @throws StreamException
//Synthetic comment -- @@ -473,11 +527,9 @@
* @throws SAXException
* @throws ParserConfigurationException
*/
    public static ManifestData parse(
            IAbstractFile manifestFile,
            boolean gatherData,
            ManifestErrorHandler errorHandler)
            throws SAXException, IOException, StreamException, ParserConfigurationException {
if (manifestFile != null) {
SAXParser parser = sParserFactory.newSAXParser();

//Synthetic comment -- @@ -486,8 +538,7 @@
data = new ManifestData();
}

            ManifestHandler manifestHandler = new ManifestHandler(manifestFile,
                    data, errorHandler);
parser.parse(new InputSource(manifestFile.getContents()), manifestHandler);

return data;
//Synthetic comment -- @@ -497,10 +548,14 @@
}

/**
     * Parses the Android Manifest, and returns an object containing the result of the parsing.
     *
* <p/>
     * This is the equivalent of calling <pre>parse(manifestFile, true, null)</pre>
*
* @param manifestFile the manifest file to parse.
* @throws ParserConfigurationException
//Synthetic comment -- @@ -508,13 +563,13 @@
* @throws IOException
* @throws SAXException
*/
    public static ManifestData parse(IAbstractFile manifestFile)
            throws SAXException, IOException, StreamException, ParserConfigurationException {
return parse(manifestFile, true, null);
}

    public static ManifestData parse(IAbstractFolder projectFolder)
            throws SAXException, IOException, StreamException, ParserConfigurationException {
IAbstractFile manifestFile = getManifest(projectFolder);
if (manifestFile == null) {
throw new FileNotFoundException();
//Synthetic comment -- @@ -524,18 +579,19 @@
}

/**
     * Parses the Android Manifest from an {@link InputStream}, and returns a {@link ManifestData}
     * object containing the result of the parsing.
*
     * @param manifestFileStream the {@link InputStream} representing the manifest file.
* @return
* @throws StreamException
* @throws IOException
* @throws SAXException
* @throws ParserConfigurationException
*/
    public static ManifestData parse(InputStream manifestFileStream)
            throws SAXException, IOException, StreamException, ParserConfigurationException {
if (manifestFileStream != null) {
SAXParser parser = sParserFactory.newSAXParser();

//Synthetic comment -- @@ -551,11 +607,12 @@
}

/**
     * Returns an {@link IAbstractFile} object representing the manifest for the given project.
*
* @param project The project containing the manifest file.
     * @return An IAbstractFile object pointing to the manifest or null if the manifest
     *         is missing.
*/
public static IAbstractFile getManifest(IAbstractFolder projectFolder) {
IAbstractFile file = projectFolder.getFile(SdkConstants.FN_ANDROID_MANIFEST_XML);








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







