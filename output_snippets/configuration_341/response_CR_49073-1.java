//<Beginning of snippet n. 0>


import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.resources.ResourceType;
import com.android.util.Pair;

import org.eclipse.core.resources.IProject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

ContextPullParser parser = new ContextPullParser(this, xml);
File layoutFile = new File(xml);
if (layoutFile.isFile()) {
    try (FileReader fileReader = new FileReader(layoutFile)) {
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        parser.setInput(fileReader);
        return parser;
    } catch (XmlPullParserException e) {
        AdtPlugin.log(e, null);
        throw new RuntimeException("XML parsing error", e);
    } catch (IOException e) {
        AdtPlugin.log(e, null);
        throw new RuntimeException("IO error", e);
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>


import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

File layoutFile = new File(contextLayout.getValue());
if (layoutFile.isFile()) {
    try (FileReader fileReader = new FileReader(layoutFile)) {
        String queryLayoutName = mEditor.getLayoutResourceName();
        mProjectCallback.setLayoutParser(queryLayoutName, modelParser);
        ContextPullParser topParser = new ContextPullParser(mProjectCallback, layoutFile);
        topParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        topParser.setInput(fileReader);
    } catch (IOException e) {
        AdtPlugin.log(e, null);
        throw new RuntimeException("IO error", e);
    } catch (XmlPullParserException e) {
        AdtPlugin.log(e, null);
        throw new RuntimeException("XML parsing error", e);
    }
}

//<End of snippet n. 1>