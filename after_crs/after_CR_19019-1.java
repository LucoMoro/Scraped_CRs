/*ADT: support for editing embedded layouts.

This is a very first rough changeset. The context in which the layout
is edited is hardcoded until we have the UI for it.

The basics are the following:
- if there's a context, don't pass the parser on top of the model to
  the layout lib.
- instead create a KXml based parser that will return the model-based
parser when queried for a custom parser (if the layout name matches the
file being edited).

Change-Id:I2d106def9c64e82ab50c8fae96cb050338422a85*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java
new file mode 100644
//Synthetic comment -- index 0000000..3f06968

//Synthetic comment -- @@ -0,0 +1,54 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.editors.layout;

import com.android.layoutlib.api.IXmlPullParser;

import org.kxml2.io.KXmlParser;

/**
 * Modified {@link KXmlParser} that adds the methods of {@link IXmlPullParser}.
 * <p/>
 * It will return a given parser when queried for one through
 * {@link IXmlPullParser#getParser(String)} for a given name.
 *
 */
public class ContextPullParser extends KXmlParser implements IXmlPullParser {

    private final String mName;
    private final IXmlPullParser mEmbeddedParser;

    public ContextPullParser(String name, IXmlPullParser embeddedParser) {
        super();
        mName = name;
        mEmbeddedParser = embeddedParser;
    }

    // --- Layout lib API methods

    public IXmlPullParser getParser(String layoutName) {
        if (mName.equals(layoutName)) {
            return mEmbeddedParser;
        }

        return null;
    }

    public Object getViewKey() {
        return null; // never any key to return
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index 1e04c6b..1cad70a 100644

//Synthetic comment -- @@ -23,8 +23,8 @@
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.layoutlib.api.IXmlPullParser;
import com.android.layoutlib.api.ViewInfo;
import com.android.layoutlib.api.IDensityBasedResourceValue.Density;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -46,7 +46,7 @@
* <p/>
* It's designed to work on layout files, and will most likely not work on other resource files.
* <p/>
 * This pull parser generates {@link ViewInfo}s which key is a {@link UiElementNode}.
*/
public final class UiElementPullParser extends BasePullParser {
private final static String ATTR_PADDING = "padding"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index f04fcc1..d7d71da 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.ContextPullParser;
import com.android.ide.eclipse.adt.internal.editors.layout.ExplodedRenderingHelper;
import com.android.ide.eclipse.adt.internal.editors.layout.IGraphicalLayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
//Synthetic comment -- @@ -37,6 +38,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
//Synthetic comment -- @@ -48,6 +50,7 @@
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.layoutlib.api.ILayoutLog;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IXmlPullParser;
import com.android.layoutlib.api.LayoutBridge;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneParams;
//Synthetic comment -- @@ -101,9 +104,13 @@
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.w3c.dom.Node;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
//Synthetic comment -- @@ -1320,8 +1327,40 @@
float ydpi = mConfigComposite.getYDpi();
boolean isProjectTheme = mConfigComposite.isProjectTheme();

        IXmlPullParser modelParser = new UiElementPullParser(model,
mUseExplodeMode, explodeNodes, density, xdpi, iProject);
        IXmlPullParser topParser = modelParser;

        // Code to support editing included layout
        // FIXME: refactor this somewhere else, and deal with edit workflow
        if (false) {
            // name of the top layout.
            String contextLayoutName = "includes";

            // find the layout file.
            Map<String, IResourceValue> layouts = configuredProjectRes.get(
                    ResourceType.LAYOUT.getName());

            IResourceValue contextLayout = layouts.get(contextLayoutName);
            File layoutFile = new File(contextLayout.getValue());
            if (layoutFile.isFile()) {
                try {
                    // get the name of the layout actually being edited, without the extension
                    // as it's what IXmlPullParser.getParser(String) will receive.
                    String queryLayoutName = mEditedFile.getName().substring(
                            0, mEditedFile.getName().indexOf('.'));

                    topParser = new ContextPullParser(queryLayoutName, modelParser);
                    topParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
                    topParser.setInput(new FileReader(layoutFile));
                } catch (XmlPullParserException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    // this will not happen since we check above.
                }
            }
        }

RenderingMode renderingMode = RenderingMode.NORMAL;
if (mClippingButton.getSelection() == false) {
//Synthetic comment -- @@ -1336,7 +1375,7 @@
}

SceneParams params = new SceneParams(
                topParser,
iProject /* projectKey */,
width, height,
renderingMode,







