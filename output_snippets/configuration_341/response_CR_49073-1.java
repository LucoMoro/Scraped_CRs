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
import java.io.BufferedReader;
import java.io.StringWriter;

ContextPullParser parser = new ContextPullParser(this, xml);
File layoutFile = new File(xml);
if (layoutFile.isFile()) {
    StringBuilder xmlContent = new StringBuilder();
    try (FileReader fileReader = new FileReader(layoutFile);
         BufferedReader bufferedReader = new BufferedReader(fileReader)) {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            xmlContent.append(line).append("\n");
        }
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        parser.setInput(new StringReader(xmlContent.toString()));
        return parser;
    } catch (XmlPullParserException e) {
        AdtPlugin.log(e, null);
        throw new XmlPullParserException("XML parsing error", e);
    } catch (IOException e) {
        AdtPlugin.log(e, null);
        throw new IOException("IO error", e);
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
import java.io.BufferedReader;
import java.io.StringWriter;

File layoutFile = new File(contextLayout.getValue());
if (layoutFile.isFile()) {
    StringBuilder xmlContent = new StringBuilder();
    try (FileReader fileReader = new FileReader(layoutFile);
         BufferedReader bufferedReader = new BufferedReader(fileReader)) {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            xmlContent.append(line).append("\n");
        }
        String queryLayoutName = mEditor.getLayoutResourceName();
        mProjectCallback.setLayoutParser(queryLayoutName, modelParser);
        ContextPullParser topParser = new ContextPullParser(mProjectCallback, layoutFile);
        topParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        topParser.setInput(new StringReader(xmlContent.toString()));
    } catch (IOException e) {
        AdtPlugin.log(e, null);
        throw new IOException("IO error", e);
    } catch (XmlPullParserException e) {
        AdtPlugin.log(e, null);
        throw new XmlPullParserException("XML parsing error", e);
    }
}

//<End of snippet n. 1>