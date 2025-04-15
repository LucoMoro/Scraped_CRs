/*Remove AdtPlugin dependency from AttrsXmlParser.

The AdtPluin was used just for logging. Instead the
AttrsXmlParser takes an ILogger (AdtPlug implements
ILogger and can be used directly in unit tests too).

For unit tests there is a new StdSdkLog convenience
class that prints to stdout/stderr (formerly MockStdLogger
from the Sdk Manager was doing that.)

Change-Id:I658af61d04efb19ad6e3bf9c0bf471452372885a*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java
//Synthetic comment -- index 85b3c76..deacd7a 100644

//Synthetic comment -- @@ -17,12 +17,11 @@
package com.android.ide.common.resources.platform;

import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.log.ILogger;
import com.android.ide.common.resources.platform.ViewClassInfo.LayoutParamsInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
//Synthetic comment -- @@ -47,6 +46,7 @@

private Document mDocument;
private String mOsAttrsXmlPath;

// all attributes that have the same name are supposed to have the same
// parameters so we'll keep a cache of them to avoid processing them twice.
private HashMap<String, AttributeInfo> mAttributeMap;
//Synthetic comment -- @@ -55,30 +55,56 @@
private final Map<String, DeclareStyleableInfo> mStyleMap =
new HashMap<String, DeclareStyleableInfo>();

    /**
     * Map of all (constant, value) pairs for attributes of format enum or flag.
* E.g. for attribute name=gravity, this tells us there's an enum/flag called "center"
* with value 0x11.
*/
private Map<String, Map<String, Integer>> mEnumFlagValues;

    /**
     * A logger object. Must not be null.
     */
    private final ILogger mLog;


/**
* Creates a new {@link AttrsXmlParser}, set to load things from the given
* XML file. Nothing has been parsed yet. Callers should call {@link #preload()}
* next.
     *
     * @param osAttrsXmlPath The path of the <code>attrs.xml</code> file to parse.
     *              Must not be null. Should point to an existing valid XML document.
     * @param log A logger object. Must not be null.
*/
    public AttrsXmlParser(String osAttrsXmlPath, ILogger log) {
        this(osAttrsXmlPath, null /* inheritableAttributes */, log);
}

/**
* Creates a new {@link AttrsXmlParser} set to load things from the given
     * XML file.
     * <p/>
     * If inheritableAttributes is non-null, it must point to a preloaded
* {@link AttrsXmlParser} which attributes will be used for this one. Since
* already defined attributes are not modifiable, they are thus "inherited".
     *
     * @param osAttrsXmlPath The path of the <code>attrs.xml</code> file to parse.
     *              Must not be null. Should point to an existing valid XML document.
     * @param inheritableAttributes An optional parser with attributes to inherit. Can be null.
     *              If not null, the parser must have had its {@link #preload()} method
     *              invoked prior to being used here.
     * @param log A logger object. Must not be null.
*/
    public AttrsXmlParser(
            String osAttrsXmlPath,
            AttrsXmlParser inheritableAttributes,
            ILogger log) {
mOsAttrsXmlPath = osAttrsXmlPath;
        mLog = log;

        assert osAttrsXmlPath != null;
        assert log != null;

if (inheritableAttributes == null) {
mAttributeMap = new HashMap<String, AttributeInfo>();
//Synthetic comment -- @@ -91,7 +117,7 @@
}

/**
     * Returns the OS path of the attrs.xml file parsed.
*/
public String getOsAttrsXmlPath() {
return mOsAttrsXmlPath;
//Synthetic comment -- @@ -106,7 +132,7 @@
Document doc = getDocument();

if (doc == null) {
            mLog.warning("Failed to find %1$s", //$NON-NLS-1$
mOsAttrsXmlPath);
return this;
}
//Synthetic comment -- @@ -119,7 +145,7 @@
}

if (res == null) {
            mLog.warning("Failed to find a <resources> node in %1$s", //$NON-NLS-1$
mOsAttrsXmlPath);
return this;
}
//Synthetic comment -- @@ -161,7 +187,7 @@
}

/**
     * Returns a list of all <code>decleare-styleable</code> found in the XML file.
*/
public Map<String, DeclareStyleableInfo> getDeclareStyleableList() {
return Collections.unmodifiableMap(mStyleMap);
//Synthetic comment -- @@ -189,13 +215,13 @@
DocumentBuilder builder = factory.newDocumentBuilder();
mDocument = builder.parse(new File(mOsAttrsXmlPath));
} catch (ParserConfigurationException e) {
                mLog.error(e, "Failed to create XML document builder for %1$s", //$NON-NLS-1$
mOsAttrsXmlPath);
} catch (SAXException e) {
                mLog.error(e, "Failed to parse XML document %1$s", //$NON-NLS-1$
mOsAttrsXmlPath);
} catch (IOException e) {
                mLog.error(e, "Failed to read XML document %1$s", //$NON-NLS-1$
mOsAttrsXmlPath);
}
}
//Synthetic comment -- @@ -203,7 +229,8 @@
}

/**
     * Finds all the &lt;declare-styleable&gt; and &lt;attr&gt; nodes
     * in the top &lt;resources&gt; node.
*/
private void parseResources(Node res) {

//Synthetic comment -- @@ -438,8 +465,9 @@
formats.add(format);
}
} catch (IllegalArgumentException e) {
                    mLog.error(e,
                        "Unknown format name '%s' in <attr name=\"%s\">, file '%s'.", //$NON-NLS-1$
                        f, name, getOsAttrsXmlPath());
}
}
}
//Synthetic comment -- @@ -487,7 +515,7 @@
if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals(filter)) {
Node nameNode = child.getAttributes().getNamedItem("name");  //$NON-NLS-1$
if (nameNode == null) {
                    mLog.warning(
"Missing name attribute in <attr name=\"%s\"><%s></attr>", //$NON-NLS-1$
attrName, filter);
} else {
//Synthetic comment -- @@ -499,9 +527,9 @@

Node valueNode = child.getAttributes().getNamedItem("value");  //$NON-NLS-1$
if (valueNode == null) {
                        mLog.warning(
                            "Missing value attribute in <attr name=\"%s\"><%s name=\"%s\"></attr>", //$NON-NLS-1$
                            attrName, filter, name);
} else {
String value = valueNode.getNodeValue();
try {
//Synthetic comment -- @@ -517,7 +545,7 @@
map.put(name, Integer.valueOf(i));

} catch(NumberFormatException e) {
                            mLog.error(e,
"Value in <attr name=\"%s\"><%s name=\"%s\" value=\"%s\"></attr> is not a valid decimal or hexadecimal", //$NON-NLS-1$
attrName, filter, name, value);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/DeclareStyleableInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/DeclareStyleableInfo.java
//Synthetic comment -- index 123e75e..8719aa9 100644

//Synthetic comment -- @@ -17,9 +17,6 @@
package com.android.ide.common.resources.platform;


/**
* Information needed to represent a View or ViewGroup (aka Layout) item
* in the layout hierarchy, as extracted from the main android.jar and the








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/ViewClassInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/ViewClassInfo.java
//Synthetic comment -- index 82a3c34..214eb9c 100644

//Synthetic comment -- @@ -17,8 +17,6 @@
package com.android.ide.common.resources.platform;


/**
* Information needed to represent a View or ViewGroup (aka Layout) item
* in the layout hierarchy, as extracted from the main android.jar and the








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java
//Synthetic comment -- index 56c047f..0d5ad23 100644

//Synthetic comment -- @@ -136,14 +136,16 @@
// gather the attribute definition
progress.subTask("Attributes definitions");
AttrsXmlParser attrsXmlParser = new AttrsXmlParser(
                    mAndroidTarget.getPath(IAndroidTarget.ATTRIBUTES),
                    AdtPlugin.getDefault());
attrsXmlParser.preload();
progress.worked(1);

progress.subTask("Manifest definitions");
AttrsXmlParser attrsManifestXmlParser = new AttrsXmlParser(
mAndroidTarget.getPath(IAndroidTarget.MANIFEST_ATTRIBUTES),
                    attrsXmlParser,
                    AdtPlugin.getDefault());
attrsManifestXmlParser.preload();
progress.worked(1);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserManifestTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserManifestTest.java
//Synthetic comment -- index 3033275..b10f68d 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.common.resources.platform;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.tests.AdtTestData;

import java.util.Arrays;
//Synthetic comment -- @@ -35,7 +36,7 @@
@Override
public void setUp() throws Exception {
mFilePath = AdtTestData.getInstance().getTestFilePath(MOCK_DATA_PATH); //$NON-NLS-1$
        mParser = new AttrsXmlParser(mFilePath, AdtPlugin.getDefault());
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserTest.java
//Synthetic comment -- index 39ed330..3e47c35 100644

//Synthetic comment -- @@ -18,6 +18,7 @@


import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.tests.AdtTestData;

import java.util.Map;
//Synthetic comment -- @@ -35,7 +36,7 @@
@Override
public void setUp() throws Exception {
mFilePath = AdtTestData.getInstance().getTestFilePath(MOCK_DATA_PATH); //$NON-NLS-1$
        mParser = new AttrsXmlParser(mFilePath, AdtPlugin.getDefault());
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParserTest.java
//Synthetic comment -- index d3ebc57..42f2455 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ide.common.resources.platform.AttrsXmlParser;
import com.android.ide.common.resources.platform.ViewClassInfo;
import com.android.ide.common.resources.platform.ViewClassInfo.LayoutParamsInfo;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.sdk.AndroidJarLoader.ClassWrapper;
import com.android.ide.eclipse.adt.internal.sdk.IAndroidClassLoader.IClassDescriptor;
import com.android.ide.eclipse.tests.AdtTestData;
//Synthetic comment -- @@ -60,7 +61,8 @@
public MockLayoutParamsParser() {
super(new MockFrameworkClassLoader(),
new AttrsXmlParser(
                          AdtTestData.getInstance().getTestFilePath(MOCK_DATA_PATH),
                          AdtPlugin.getDefault()).preload());

mTopViewClass = new ClassWrapper(mock_android.view.View.class);
mTopGroupClass = new ClassWrapper(mock_android.view.ViewGroup.class);








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/CommandLineProcessorTest.java b/sdkmanager/app/tests/com/android/sdkmanager/CommandLineProcessorTest.java
//Synthetic comment -- index a213652..688ce52 100644

//Synthetic comment -- @@ -17,13 +17,14 @@
package com.android.sdkmanager;

import com.android.sdklib.ISdkLog;
import com.android.sdklib.StdSdkLog;

import junit.framework.TestCase;


public class CommandLineProcessorTest extends TestCase {

    private StdSdkLog mLog;

/**
* A mock version of the {@link CommandLineProcessor} class that does not
//Synthetic comment -- @@ -92,7 +93,7 @@

@Override
protected void setUp() throws Exception {
        mLog = new StdSdkLog();
super.setUp();
}









//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/SdkCommandLineTest.java b/sdkmanager/app/tests/com/android/sdkmanager/SdkCommandLineTest.java
//Synthetic comment -- index 07a32e0..8206e2a 100644

//Synthetic comment -- @@ -17,12 +17,13 @@
package com.android.sdkmanager;

import com.android.sdklib.ISdkLog;
import com.android.sdklib.StdSdkLog;

import junit.framework.TestCase;

public class SdkCommandLineTest extends TestCase {

    private StdSdkLog mLog;

/**
* A mock version of the {@link SdkCommandLine} class that does not
//Synthetic comment -- @@ -69,7 +70,7 @@

@Override
protected void setUp() throws Exception {
        mLog = new StdSdkLog();
super.setUp();
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/ISdkLog.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/ISdkLog.java
//Synthetic comment -- index 163f7a9..ec22177 100644

//Synthetic comment -- @@ -20,9 +20,26 @@

/**
* Interface used to display warnings/errors while parsing the SDK content.
 * <p/>
 * There are a few default implementations available:
 * <ul>
 * <li> {@link NullSdkLog} is an implementation that does <em>nothing</em> with the log.
 *  Useful for limited cases where you need to call a class that requires a non-null logging
 *  yet the calling code does not have any mean of reporting logs itself. It can be
 *  acceptable for use a temporary implementation but most of the time that means the caller
 *  code needs to be reworked to take a logger object from its own caller.
 * </li>
 * <li> {@link StdSdkLog} is an implementation that dumps the log to {@link System#out} or
 *  {@link System#err}. This is useful for unit tests or code that does not have any GUI.
 *  Apps based on Eclipse or SWT should not use it and should provide a better way to report
 *  to the user.
 * </li>
 * <li> ADT has a <code>AdtPlugin</code> which implements a similar interface called
 *      <code>ILogger</code>, useful in case we don't want to pull the whole SdkLib.
 * </ul>
*/
public interface ISdkLog {

/**
* Prints a warning message on stdout.
* <p/>
//Synthetic comment -- @@ -30,13 +47,13 @@
* need to put such a prefix in the format string.
* <p/>
* Implementations should only display warnings in verbose mode.
     *
* @param warningFormat is an optional error format. If non-null, it will be printed
*          using a {@link Formatter} with the provided arguments.
* @param args provides the arguments for warningFormat.
*/
void warning(String warningFormat, Object... args);

/**
* Prints an error message on stderr.
* <p/>
//Synthetic comment -- @@ -44,7 +61,7 @@
* need to put such a prefix in the format string.
* <p/>
* Implementation should always display errors, independent of verbose mode.
     *
* @param t is an optional {@link Throwable} or {@link Exception}. If non-null, it's
*          message will be printed out.
* @param errorFormat is an optional error format. If non-null, it will be printed
//Synthetic comment -- @@ -52,13 +69,13 @@
* @param args provides the arguments for errorFormat.
*/
void error(Throwable t, String errorFormat, Object... args);

/**
* Prints a message as-is on stdout.
* <p/>
     * Implementation can omit printing such messages when not in verbose mode.
* No prefix is used, the message is printed as-is after formatting.
     *
* @param msgFormat is an optional error format. If non-null, it will be printed
*          using a {@link Formatter} with the provided arguments.
* @param args provides the arguments for msgFormat.








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/MockStdLogger.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/StdSdkLog.java
similarity index 77%
rename from sdkmanager/app/tests/com/android/sdkmanager/MockStdLogger.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/StdSdkLog.java
//Synthetic comment -- index 961e88d..1683808 100644

//Synthetic comment -- @@ -4,7 +4,7 @@
* Licensed under the Eclipse Public License, Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
 *
*      http://www.eclipse.org/org/documents/epl-v10.php
*
* Unless required by applicable law or agreed to in writing, software
//Synthetic comment -- @@ -14,14 +14,17 @@
* limitations under the License.
*/

package com.android.sdklib;


/**
 * An implementation of {@link ISdkLog} that prints to {@link System#out} and {@link System#err}.
 * <p/>
 * This is mostly useful for unit tests. It should not be used by GUI-based tools (e.g.
 * Eclipse plugin or SWT-based apps) which should have a better way to expose their logging
 * error and warnings.
*/
public class StdSdkLog implements ISdkLog {

public void error(Throwable t, String errorFormat, Object... args) {
if (errorFormat != null) {







