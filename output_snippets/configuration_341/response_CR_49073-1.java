//<Beginning of snippet n. 0>


import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.resources.ResourceType;
import com.android.util.Pair;

import org.eclipse.core.resources.IProject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

ContextPullParser parser = new ContextPullParser(this, xml);
StringBuilder xmlContent = new StringBuilder();
try (FileReader fileReader = new FileReader(xml)) {
    int ch;
    while ((ch = fileReader.read()) != -1) {
        xmlContent.append((char) ch);
    }
    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
    parser.setInput(new StringReader(xmlContent.toString()));
    return parser;
} catch (XmlPullParserException e) {
    AdtPlugin.log(e, null);
} catch (FileNotFoundException e) {
    AdtPlugin.log(e, null);
} catch (IOException e) {
    AdtPlugin.log(e, null);
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>


import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;

import org.eclipse.core.resources.IProject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

File layoutFile = new File(contextLayout.getValue());
if (layoutFile.isFile()) {
    try (FileReader fileReader = new FileReader(layoutFile)) {
        StringBuilder layoutContent = new StringBuilder();
        int ch;
        while ((ch = fileReader.read()) != -1) {
            layoutContent.append((char) ch);
        }
        
        String queryLayoutName = mEditor.getLayoutResourceName();
        mProjectCallback.setLayoutParser(queryLayoutName, modelParser);
        ContextPullParser topParser = new ContextPullParser(mProjectCallback, layoutFile);
        topParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        topParser.setInput(new StringReader(layoutContent.toString()));
    } catch (IOException e) {
        AdtPlugin.log(e, null);
    } catch (XmlPullParserException e) {
        AdtPlugin.log(e, null);
    }
}

//<End of snippet n. 1>