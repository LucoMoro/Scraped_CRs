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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index 1e04c6b..1cad70a 100644

//Synthetic comment -- @@ -23,8 +23,8 @@
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.layoutlib.api.IXmlPullParser;
import com.android.layoutlib.api.IDensityBasedResourceValue.Density;
import com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -46,7 +46,7 @@
* <p/>
* It's designed to work on layout files, and will most likely not work on other resource files.
* <p/>
 * This pull parser generates {@link ILayoutViewInfo}s which key is a {@link UiElementNode}.
*/
public final class UiElementPullParser extends BasePullParser {
private final static String ATTR_PADDING = "padding"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index f04fcc1..d7d71da 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.ExplodedRenderingHelper;
import com.android.ide.eclipse.adt.internal.editors.layout.IGraphicalLayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
//Synthetic comment -- @@ -37,6 +38,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
//Synthetic comment -- @@ -48,6 +50,7 @@
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.layoutlib.api.ILayoutLog;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.LayoutBridge;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneParams;
//Synthetic comment -- @@ -101,9 +104,13 @@
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
//Synthetic comment -- @@ -1320,8 +1327,40 @@
float ydpi = mConfigComposite.getYDpi();
boolean isProjectTheme = mConfigComposite.isProjectTheme();

        UiElementPullParser parser = new UiElementPullParser(model,
mUseExplodeMode, explodeNodes, density, xdpi, iProject);

RenderingMode renderingMode = RenderingMode.NORMAL;
if (mClippingButton.getSelection() == false) {
//Synthetic comment -- @@ -1336,7 +1375,7 @@
}

SceneParams params = new SceneParams(
                parser,
iProject /* projectKey */,
width, height,
renderingMode,







