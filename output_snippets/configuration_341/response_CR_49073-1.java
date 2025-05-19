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
import java.io.FileReader;

File xmlFile = new File(xml.toString());
if (xmlFile.exists()) {
    StringBuilder xmlContent = new StringBuilder();
    try (FileReader fileReader = new FileReader(xmlFile)) {
        int ch;
        while ((ch = fileReader.read()) != -1) {
            xmlContent.append((char) ch);
        }
        ContextPullParser parser = new ContextPullParser(this, xml);
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        parser.setInput(new StringReader(xmlContent.toString()));
        return parser;
    } catch (XmlPullParserException e) {
        AdtPlugin.log(e, "Error parsing XML: " + xmlFile.getAbsolutePath());
    } catch (FileNotFoundException e) {
        AdtPlugin.log(e, "File not found: " + xmlFile.getAbsolutePath());
    } catch (IOException e) {
        AdtPlugin.log(e, "IO error while reading file: " + xmlFile.getAbsolutePath());
    }
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
    StringBuilder layoutContent = new StringBuilder();
    try (FileReader fileReader = new FileReader(layoutFile)) {
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
        AdtPlugin.log(e, "IO error while reading layout file: " + layoutFile.getAbsolutePath());
    } catch (XmlPullParserException e) {
        AdtPlugin.log(e, "Error parsing layout XML: " + layoutFile.getAbsolutePath());
    }
}

//<End of snippet n. 1>