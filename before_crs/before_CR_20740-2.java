/*Various palette improvements

1. Look up the theme background and foreground colors using theme
   resources, if possible.  These theme resources are then used to set
   the foreground and background colors on the labels used as a
   fallback for non-previewable widgets.  In other words, when you're
   looking at a dark theme, the layouts such as LinearLayout is now
   shown using a white label on a dark background, making the palette
   view more consistent visually.

2. Configure scrollbar increments properly such that scrolling the
   views with scrollwheels or mouse gestures works better.

3. Fix a bug in the way preview images were copied out of the rendered
   image; the root coordinates were not taken into account, which
   matters for themes like Theme.Dialog.

Change-Id:I4832166a0560d33fe4e4dd8079c82a180e07e897*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/AccordionControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/AccordionControl.java
//Synthetic comment -- index 4a99dab..0c9c0fc 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.List;
//Synthetic comment -- @@ -245,6 +246,9 @@

// Turn off border?
final ScrolledComposite scrolledComposite = new ScrolledComposite(this, SWT.V_SCROLL);

// Do we need the scrolled composite or can we just look at the next
// wizard in the hierarchy?
//Synthetic comment -- @@ -267,6 +271,8 @@
if (content != null && r != null) {
Point minSize = content.computeSize(r.width, SWT.DEFAULT);
scrolledComposite.setMinSize(minSize);
}
}
});








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 8e4b12f..397aac9 100644

//Synthetic comment -- @@ -1700,6 +1700,21 @@
}
}

/**
* Returns the resource name of this layout, NOT including the @layout/ prefix
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java
//Synthetic comment -- index 1952462..94419b3 100644

//Synthetic comment -- @@ -373,4 +373,58 @@

return sub;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 1aeda77..fbf7167 100755

//Synthetic comment -- @@ -27,9 +27,9 @@
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.Params.RenderingMode;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -150,6 +150,7 @@
*/
private GraphicalEditorPart mEditor;
private Color mBackground;

/** The palette modes control various ways to visualize and lay out the views */
private static enum PaletteMode {
//Synthetic comment -- @@ -281,6 +282,10 @@
mBackground.dispose();
mBackground = null;
}

super.dispose();
}
//Synthetic comment -- @@ -337,11 +342,20 @@
}

if (mPaletteMode.isPreview()) {
            RGB background = mPreviewIconFactory.getBackgroundColor();
if (mBackground != null) {
mBackground.dispose();
}
mBackground = new Color(getDisplay(), background);
}

AndroidTargetData targetData = Sdk.getCurrent().getTargetData(target);
//Synthetic comment -- @@ -452,6 +466,10 @@
} else {
// Just use an Icon+Text item for these for now
item = new IconTextItem(parent, desc);
}
break;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 38d947f..a93c81e 100644

//Synthetic comment -- @@ -13,14 +13,20 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.eclipse.adt.AndroidConstants.DOT_PNG;

import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.Params.RenderingMode;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -30,14 +36,18 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.RGB;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
//Synthetic comment -- @@ -53,14 +63,19 @@
import java.util.Set;

import javax.imageio.ImageIO;

/**
* Factory which can provide preview icons for android views of a particular SDK and
* editor's configuration chooser
*/
public class PreviewIconFactory {
private PaletteControl mPalette;
private RGB mBackground;
private File mImageDir;

private static final String PREVIEW_INFO_FILE = "preview.properties"; //$NON-NLS-1$
//Synthetic comment -- @@ -77,6 +92,7 @@
public void reset() {
mImageDir = null;
mBackground = null;
}

/**
//Synthetic comment -- @@ -143,8 +159,8 @@
}

/**
     * Renders ALL the widgets and then extracts image data for each view and saves it
     * on disk
*/
private boolean render() {
LayoutEditor layoutEditor = mPalette.getEditor().getLayoutEditor();
//Synthetic comment -- @@ -209,10 +225,21 @@

// TODO - use resource resolution instead?
if (mBackground == null) {
                            int rgb = image.getRGB(image.getWidth()-1, image.getHeight()-1);
                            RGB color = new RGB((rgb & 0xFF0000) >> 16, (rgb & 0xFF00) >> 8,
                                    rgb & 0xFF);
                            storeBackground(imageDir, color);
}

List<ViewInfo> viewInfoList = session.getRootViews();
//Synthetic comment -- @@ -220,6 +247,8 @@
// We don't render previews under a <merge> so there should
// only be one root.
ViewInfo firstRoot = viewInfoList.get(0);
List<ViewInfo> infos = firstRoot.getChildren();
for (ViewInfo info : infos) {
Object cookie = info.getCookie();
//Synthetic comment -- @@ -233,10 +262,10 @@
// On Windows, perhaps we need to rename instead?
file.delete();
}
                                int x1 = info.getLeft();
                                int y1 = info.getTop();
                                int x2 = info.getRight();
                                int y2 = info.getBottom();
if (x1 != x2 && y1 != y2) {
savePreview(file, image, x1, y1, x2, y2);
}
//Synthetic comment -- @@ -252,6 +281,125 @@
return true;
}

private String getFileName(ElementDescriptor descriptor) {
return descriptor.getUiName() + DOT_PNG;
}
//Synthetic comment -- @@ -304,8 +452,8 @@
if (themeName.startsWith(themeNamePrefix)) {
themeName = themeName.substring(themeNamePrefix.length());
}
            String dirName = String.format("palette-%s-%s-%s",
                    cleanup(targetName), cleanup(themeName), cleanup(mPalette.getCurrentDevice()));
IPath dirPath = pluginState.append(dirName);

mImageDir = new File(dirPath.toOSString());
//Synthetic comment -- @@ -328,21 +476,50 @@
}
}

    private void storeBackground(File imageDir, RGB color) {
        mBackground = color;
File file = new File(imageDir, PREVIEW_INFO_FILE);
        String colors = String.format("background=%02x,%02x,%02x",
                color.red, color.green, color.blue);
AdtPlugin.writeFile(file, colors);
}

public RGB getBackgroundColor() {
if (mBackground == null) {
            mBackground = null;

File imageDir = getImageDir(false);
if (!imageDir.exists()) {
render();
}

File file = new File(imageDir, PREVIEW_INFO_FILE);
//Synthetic comment -- @@ -366,22 +543,22 @@

String colorString = (String) properties.get("background"); //$NON-NLS-1$
if (colorString != null) {
                    String[] colors = colorString.split(","); //$NON-NLS-1$
                    if (colors.length == 3) {
                        colorString = colorString.trim();
                        int red = Integer.parseInt(colors[0], 16);
                        int green = Integer.parseInt(colors[1], 16);
                        int blue = Integer.parseInt(colors[2], 16);
                        mBackground = new RGB(red, green, blue);
                    }
}
}

if (mBackground == null) {
                mBackground = new RGB(0,0,0);
}
}

        return mBackground;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtilsTest.java
//Synthetic comment -- index c611bab..73129f4 100644

//Synthetic comment -- @@ -298,5 +298,9 @@
assertEquals(0xFFFF0000, sub.getRGB(9, 9));
}


}







