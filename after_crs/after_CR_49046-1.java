/*Ensure streams are closed after parsing. DO NOT MERGE

Just read in full contents of XML file up front
such that the stream can be closed. Also switch
from InputStream to Reader since that's the native
format KXml wants anyway (inside setInput() it creates
one from the input stream if it doesn't already have
one)

Change-Id:I669acc428f05f0ac2572389f847afc4ec28f2007*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index b261a5f..74c033c 100644

//Synthetic comment -- @@ -51,14 +51,17 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.resources.ResourceType;
import com.android.util.Pair;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.eclipse.core.resources.IProject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
//Synthetic comment -- @@ -462,12 +465,15 @@
ContextPullParser parser = new ContextPullParser(this, xml);
try {
parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
                String xmlText = Files.toString(xml, Charsets.UTF_8);
                parser.setInput(new StringReader(xmlText));
return parser;
} catch (XmlPullParserException e) {
AdtPlugin.log(e, null);
} catch (FileNotFoundException e) {
// Shouldn't happen since we check isFile() above
            } catch (IOException e) {
                AdtPlugin.log(e, null);
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
//Synthetic comment -- index 0669f9e..fdc5fed 100644

//Synthetic comment -- @@ -54,6 +54,7 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -61,10 +62,9 @@
import org.xmlpull.v1.XmlPullParserException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
//Synthetic comment -- @@ -380,16 +380,14 @@
File layoutFile = new File(contextLayout.getValue());
if (layoutFile.isFile()) {
try {
// Get the name of the layout actually being edited, without the extension
// as it's what IXmlPullParser.getParser(String) will receive.
String queryLayoutName = mEditor.getLayoutResourceName();
mProjectCallback.setLayoutParser(queryLayoutName, modelParser);
topParser = new ContextPullParser(mProjectCallback, layoutFile);
topParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
                        String xmlText = Files.toString(layoutFile, Charsets.UTF_8);
                        topParser.setInput(new StringReader(xmlText));
} catch (IOException e) {
AdtPlugin.log(e, null);
} catch (XmlPullParserException e) {







