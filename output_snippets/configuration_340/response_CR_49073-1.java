//<Beginning of snippet n. 0>


import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.resources.ResourceType;
import com.android.util.Pair;

import org.eclipse.core.resources.IProject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

ContextPullParser parser = new ContextPullParser(this, xml);
try (FileReader fileReader = new FileReader(xml)) {
    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
    parser.setInput(fileReader);
    return parser;
} catch (XmlPullParserException e) {
    AdtPlugin.log(e, null);
} catch (IOException e) {
    // Shouldn't happen since we check isFile() above
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
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

File layoutFile = new File(contextLayout.getValue());
if (layoutFile.isFile()) {
    try {
        byte[] bytes = Files.toByteArray(layoutFile);
        String layoutContent = new String(bytes, "UTF-8");

        String queryLayoutName = mEditor.getLayoutResourceName();
        mProjectCallback.setLayoutParser(queryLayoutName, modelParser);
        topParser = new ContextPullParser(mProjectCallback, layoutFile);
        topParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        try (StringReader stringReader = new StringReader(layoutContent)) {
            topParser.setInput(stringReader);
        }
    } catch (IOException e) {
        AdtPlugin.log(e, null);
    } catch (XmlPullParserException e) {
        AdtPlugin.log(e, null);
    }
}

//<End of snippet n. 1>