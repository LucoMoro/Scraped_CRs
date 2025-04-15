/*Remove AdtPlugin dependency from AttrsXmlParser.

The AdtPluin was used just for logging. Instead the
AttrsXmlParser takes an ISdkLog and a convenience
class AdtPluginLog is provided.

For unit tests we use the new StdSdkLog convenience
class that prints to stdout/stderr (formerly MockStdLogger
from the Sdk Manager was doing that.)

Change-Id:I658af61d04efb19ad6e3bf9c0bf471452372885a*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java
//Synthetic comment -- index 85b3c76..a17c6d5 100644

//Synthetic comment -- @@ -18,11 +18,10 @@

import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.resources.platform.ViewClassInfo.LayoutParamsInfo;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;

import org.eclipse.core.runtime.IStatus;
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

    /** Map of all (constant, value) pairs for attributes of format enum or flag.
* E.g. for attribute name=gravity, this tells us there's an enum/flag called "center"
* with value 0x11.
*/
private Map<String, Map<String, Integer>> mEnumFlagValues;


/**
* Creates a new {@link AttrsXmlParser}, set to load things from the given
* XML file. Nothing has been parsed yet. Callers should call {@link #preload()}
* next.
*/
    public AttrsXmlParser(String osAttrsXmlPath) {
        this(osAttrsXmlPath, null /* inheritableAttributes */);
}

/**
* Creates a new {@link AttrsXmlParser} set to load things from the given
     * XML file. If inheritableAttributes is non-null, it must point to a preloaded
* {@link AttrsXmlParser} which attributes will be used for this one. Since
* already defined attributes are not modifiable, they are thus "inherited".
*/
    public AttrsXmlParser(String osAttrsXmlPath, AttrsXmlParser inheritableAttributes) {
mOsAttrsXmlPath = osAttrsXmlPath;

if (inheritableAttributes == null) {
mAttributeMap = new HashMap<String, AttributeInfo>();
//Synthetic comment -- @@ -91,7 +117,7 @@
}

/**
     * @return The OS path of the attrs.xml file parsed
*/
public String getOsAttrsXmlPath() {
return mOsAttrsXmlPath;
//Synthetic comment -- @@ -106,7 +132,7 @@
Document doc = getDocument();

if (doc == null) {
            AdtPlugin.log(IStatus.WARNING, "Failed to find %1$s", //$NON-NLS-1$
mOsAttrsXmlPath);
return this;
}
//Synthetic comment -- @@ -119,7 +145,7 @@
}

if (res == null) {
            AdtPlugin.log(IStatus.WARNING, "Failed to find a <resources> node in %1$s", //$NON-NLS-1$
mOsAttrsXmlPath);
return this;
}
//Synthetic comment -- @@ -161,7 +187,7 @@
}

/**
     * Returns a list of all decleare-styleable found in the xml file.
*/
public Map<String, DeclareStyleableInfo> getDeclareStyleableList() {
return Collections.unmodifiableMap(mStyleMap);
//Synthetic comment -- @@ -189,13 +215,13 @@
DocumentBuilder builder = factory.newDocumentBuilder();
mDocument = builder.parse(new File(mOsAttrsXmlPath));
} catch (ParserConfigurationException e) {
                AdtPlugin.log(e, "Failed to create XML document builder for %1$s", //$NON-NLS-1$
mOsAttrsXmlPath);
} catch (SAXException e) {
                AdtPlugin.log(e, "Failed to parse XML document %1$s", //$NON-NLS-1$
mOsAttrsXmlPath);
} catch (IOException e) {
                AdtPlugin.log(e, "Failed to read XML document %1$s", //$NON-NLS-1$
mOsAttrsXmlPath);
}
}
//Synthetic comment -- @@ -203,7 +229,8 @@
}

/**
     * Finds all the <declare-styleable> and <attr> nodes in the top <resources> node.
*/
private void parseResources(Node res) {

//Synthetic comment -- @@ -438,8 +465,9 @@
formats.add(format);
}
} catch (IllegalArgumentException e) {
                    AdtPlugin.log(e, "Unknown format name '%s' in <attr name=\"%s\">, file '%s'.", //$NON-NLS-1$
                            f, name, getOsAttrsXmlPath());
}
}
}
//Synthetic comment -- @@ -487,7 +515,7 @@
if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals(filter)) {
Node nameNode = child.getAttributes().getNamedItem("name");  //$NON-NLS-1$
if (nameNode == null) {
                    AdtPlugin.log(IStatus.WARNING,
"Missing name attribute in <attr name=\"%s\"><%s></attr>", //$NON-NLS-1$
attrName, filter);
} else {
//Synthetic comment -- @@ -499,9 +527,9 @@

Node valueNode = child.getAttributes().getNamedItem("value");  //$NON-NLS-1$
if (valueNode == null) {
                        AdtPlugin.log(IStatus.WARNING,
                                "Missing value attribute in <attr name=\"%s\"><%s name=\"%s\"></attr>", //$NON-NLS-1$
                                attrName, filter, name);
} else {
String value = valueNode.getNodeValue();
try {
//Synthetic comment -- @@ -517,7 +545,7 @@
map.put(name, Integer.valueOf(i));

} catch(NumberFormatException e) {
                            AdtPlugin.log(e,
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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPluginLog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPluginLog.java
new file mode 100755
//Synthetic comment -- index 0000000..e510782

//Synthetic comment -- @@ -0,0 +1,81 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java
//Synthetic comment -- index 56c047f..2f7dab6 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.common.resources.platform.DeclareStyleableInfo;
import com.android.ide.common.resources.platform.ViewClassInfo;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;
import com.android.ide.eclipse.adt.internal.editors.menu.descriptors.MenuDescriptors;
//Synthetic comment -- @@ -134,16 +135,19 @@
}

// gather the attribute definition
progress.subTask("Attributes definitions");
AttrsXmlParser attrsXmlParser = new AttrsXmlParser(
                    mAndroidTarget.getPath(IAndroidTarget.ATTRIBUTES));
attrsXmlParser.preload();
progress.worked(1);

progress.subTask("Manifest definitions");
AttrsXmlParser attrsManifestXmlParser = new AttrsXmlParser(
mAndroidTarget.getPath(IAndroidTarget.MANIFEST_ATTRIBUTES),
                    attrsXmlParser);
attrsManifestXmlParser.preload();
progress.worked(1);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserManifestTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserManifestTest.java
//Synthetic comment -- index 3033275..eed2c92 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.common.resources.platform;

import com.android.ide.eclipse.tests.AdtTestData;

import java.util.Arrays;
import java.util.Map;
//Synthetic comment -- @@ -35,7 +36,7 @@
@Override
public void setUp() throws Exception {
mFilePath = AdtTestData.getInstance().getTestFilePath(MOCK_DATA_PATH); //$NON-NLS-1$
        mParser = new AttrsXmlParser(mFilePath);
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserTest.java
//Synthetic comment -- index 39ed330..f146e7d 100644

//Synthetic comment -- @@ -19,6 +19,7 @@

import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.eclipse.tests.AdtTestData;

import java.util.Map;

//Synthetic comment -- @@ -35,7 +36,7 @@
@Override
public void setUp() throws Exception {
mFilePath = AdtTestData.getInstance().getTestFilePath(MOCK_DATA_PATH); //$NON-NLS-1$
        mParser = new AttrsXmlParser(mFilePath);
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParserTest.java
//Synthetic comment -- index d3ebc57..26fb4c7 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.ide.eclipse.adt.internal.sdk.AndroidJarLoader.ClassWrapper;
import com.android.ide.eclipse.adt.internal.sdk.IAndroidClassLoader.IClassDescriptor;
import com.android.ide.eclipse.tests.AdtTestData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
//Synthetic comment -- @@ -60,7 +61,8 @@
public MockLayoutParamsParser() {
super(new MockFrameworkClassLoader(),
new AttrsXmlParser(
                          AdtTestData.getInstance().getTestFilePath(MOCK_DATA_PATH)).preload());

mTopViewClass = new ClassWrapper(mock_android.view.View.class);
mTopGroupClass = new ClassWrapper(mock_android.view.ViewGroup.class);








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/CommandLineProcessorTest.java b/sdkmanager/app/tests/com/android/sdkmanager/CommandLineProcessorTest.java
//Synthetic comment -- index a213652..688ce52 100644

//Synthetic comment -- @@ -17,13 +17,14 @@
package com.android.sdkmanager;

import com.android.sdklib.ISdkLog;

import junit.framework.TestCase;


public class CommandLineProcessorTest extends TestCase {

    private MockStdLogger mLog;

/**
* A mock version of the {@link CommandLineProcessor} class that does not
//Synthetic comment -- @@ -92,7 +93,7 @@

@Override
protected void setUp() throws Exception {
        mLog = new MockStdLogger();
super.setUp();
}









//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/SdkCommandLineTest.java b/sdkmanager/app/tests/com/android/sdkmanager/SdkCommandLineTest.java
//Synthetic comment -- index 07a32e0..8206e2a 100644

//Synthetic comment -- @@ -17,12 +17,13 @@
package com.android.sdkmanager;

import com.android.sdklib.ISdkLog;

import junit.framework.TestCase;

public class SdkCommandLineTest extends TestCase {

    private MockStdLogger mLog;

/**
* A mock version of the {@link SdkCommandLine} class that does not
//Synthetic comment -- @@ -69,7 +70,7 @@

@Override
protected void setUp() throws Exception {
        mLog = new MockStdLogger();
super.setUp();
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/ISdkLog.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/ISdkLog.java
//Synthetic comment -- index 163f7a9..a8d674b 100644

//Synthetic comment -- @@ -20,9 +20,25 @@

/**
* Interface used to display warnings/errors while parsing the SDK content.
*/
public interface ISdkLog {
    
/**
* Prints a warning message on stdout.
* <p/>
//Synthetic comment -- @@ -30,13 +46,13 @@
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
//Synthetic comment -- @@ -44,7 +60,7 @@
* need to put such a prefix in the format string.
* <p/>
* Implementation should always display errors, independent of verbose mode.
     * 
* @param t is an optional {@link Throwable} or {@link Exception}. If non-null, it's
*          message will be printed out.
* @param errorFormat is an optional error format. If non-null, it will be printed
//Synthetic comment -- @@ -52,13 +68,13 @@
* @param args provides the arguments for errorFormat.
*/
void error(Throwable t, String errorFormat, Object... args);
    
/**
* Prints a message as-is on stdout.
* <p/>
     * Implementation should always display errors, independent of verbose mode.
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

package com.android.sdkmanager;

import com.android.sdklib.ISdkLog;

/**
 * 
*/
public class MockStdLogger implements ISdkLog {

public void error(Throwable t, String errorFormat, Object... args) {
if (errorFormat != null) {







