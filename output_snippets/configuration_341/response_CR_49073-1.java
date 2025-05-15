//<Beginning of snippet n. 0>

import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.resources.ResourceType;
import com.android.util.Pair;

import org.eclipse.core.resources.IProject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

ContextPullParser parser = new ContextPullParser(this, xml);
try (Reader reader = new FileReader(xml)) {
    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
    parser.setInput(reader);
    return parser;
} catch (XmlPullParserException e) {
    AdtPlugin.log(e, null);
} catch (FileNotFoundException e) {
    // Shouldn't happen since we check isFile() above
} catch (IOException e) {
    AdtPlugin.log(e, null);
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
import com.google.common.io.Files;

import org.eclipse.core.resources.IProject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

File layoutFile = new File(contextLayout.getValue());
if (layoutFile.isFile()) {
    try (Reader reader = new FileReader(layoutFile)) {
        // Get the name of the layout actually being edited, without the extension
        // as it's what IXmlPullParser.getParser(String) will receive.
        String queryLayoutName = mEditor.getLayoutResourceName();
        mProjectCallback.setLayoutParser(queryLayoutName, modelParser);
        topParser = new ContextPullParser(mProjectCallback, layoutFile);
        topParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        topParser.setInput(reader);
    } catch (IOException e) {
        AdtPlugin.log(e, null);
    } catch (XmlPullParserException e) {
        AdtPlugin.log(e, null);
    }
}

//<End of snippet n. 1>