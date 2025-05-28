
//<Beginning of snippet n. 0>


import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.resources.ResourceType;
import com.android.util.Pair;

import org.eclipse.core.resources.IProject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
ContextPullParser parser = new ContextPullParser(this, xml);
try {
parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
                parser.setInput(new FileInputStream(xml), "UTF-8"); //$NON-NLS-1$
return parser;
} catch (XmlPullParserException e) {
AdtPlugin.log(e, null);
} catch (FileNotFoundException e) {
// Shouldn't happen since we check isFile() above
}
}


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
import com.google.common.io.Files;

import org.eclipse.core.resources.IProject;
import org.xmlpull.v1.XmlPullParserException;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
File layoutFile = new File(contextLayout.getValue());
if (layoutFile.isFile()) {
try {
                        byte[] bytes = Files.toByteArray(layoutFile);

// Get the name of the layout actually being edited, without the extension
// as it's what IXmlPullParser.getParser(String) will receive.
String queryLayoutName = mEditor.getLayoutResourceName();
mProjectCallback.setLayoutParser(queryLayoutName, modelParser);
topParser = new ContextPullParser(mProjectCallback, layoutFile);
topParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
                        InputStream inputStream = new ByteArrayInputStream(bytes);
                        topParser.setInput(inputStream, "UTF-8"); //$NON-NLS-1$
} catch (IOException e) {
AdtPlugin.log(e, null);
} catch (XmlPullParserException e) {

//<End of snippet n. 1>








