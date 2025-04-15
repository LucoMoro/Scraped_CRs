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
import org.eclipse.swt.widgets.ScrollBar;

import java.util.ArrayList;
import java.util.List;
//Synthetic comment -- @@ -245,6 +246,9 @@

// Turn off border?
final ScrolledComposite scrolledComposite = new ScrolledComposite(this, SWT.V_SCROLL);
            ScrollBar verticalBar = scrolledComposite.getVerticalBar();
            verticalBar.setIncrement(20);
            verticalBar.setPageIncrement(100);

// Do we need the scrolled composite or can we just look at the next
// wizard in the hierarchy?
//Synthetic comment -- @@ -267,6 +271,8 @@
if (content != null && r != null) {
Point minSize = content.computeSize(r.width, SWT.DEFAULT);
scrolledComposite.setMinSize(minSize);
                            ScrollBar vBar = scrolledComposite.getVerticalBar();
                            vBar.setPageIncrement(r.height);
}
}
});








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 902d3d0..ad681d4 100644

//Synthetic comment -- @@ -1694,6 +1694,21 @@
}
}

    ResourceResolver createResolver() {
        String theme = mConfigComposite.getTheme();
        boolean isProjectTheme = mConfigComposite.isProjectTheme();
        Map<String, Map<String, ResourceValue>> configuredProjectRes =
            mConfigListener.getConfiguredProjectResources();

        // Get the framework resources
        Map<String, Map<String, ResourceValue>> frameworkResources =
            mConfigListener.getConfiguredFrameworkResources();

        return ResourceResolver.create(
                configuredProjectRes, frameworkResources,
                theme, isProjectTheme);
    }

/**
* Returns the resource name of this layout, NOT including the @layout/ prefix
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java
//Synthetic comment -- index 1952462..94419b3 100644

//Synthetic comment -- @@ -373,4 +373,58 @@

return sub;
}

    /**
     * Returns the color value represented by the given string value
     * @param value the color value
     * @return the color as an int
     * @throw NumberFormatException if the conversion failed.
     */
    public static int getColor(String value) {
        // Copied from ResourceHelper in layoutlib
        if (value != null) {
            if (value.startsWith("#") == false) { //$NON-NLS-1$
                throw new NumberFormatException(
                        String.format("Color value '%s' must start with #", value));
            }

            value = value.substring(1);

            // make sure it's not longer than 32bit
            if (value.length() > 8) {
                throw new NumberFormatException(String.format(
                        "Color value '%s' is too long. Format is either" +
                        "#AARRGGBB, #RRGGBB, #RGB, or #ARGB",
                        value));
            }

            if (value.length() == 3) { // RGB format
                char[] color = new char[8];
                color[0] = color[1] = 'F';
                color[2] = color[3] = value.charAt(0);
                color[4] = color[5] = value.charAt(1);
                color[6] = color[7] = value.charAt(2);
                value = new String(color);
            } else if (value.length() == 4) { // ARGB format
                char[] color = new char[8];
                color[0] = color[1] = value.charAt(0);
                color[2] = color[3] = value.charAt(1);
                color[4] = color[5] = value.charAt(2);
                color[6] = color[7] = value.charAt(3);
                value = new String(color);
            } else if (value.length() == 6) {
                value = "FF" + value; //$NON-NLS-1$
            }

            // this is a RRGGBB or AARRGGBB value

            // Integer.parseInt will fail to parse strings like "ff191919", so we use
            // a Long, but cast the result back into an int, since we know that we're only
            // dealing with 32 bit values.
            return (int)Long.parseLong(value, 16);
        }

        throw new NumberFormatException();
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 1aeda77..fbf7167 100755

//Synthetic comment -- @@ -27,9 +27,9 @@
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.Params.RenderingMode;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -150,6 +150,7 @@
*/
private GraphicalEditorPart mEditor;
private Color mBackground;
    private Color mForeground;

/** The palette modes control various ways to visualize and lay out the views */
private static enum PaletteMode {
//Synthetic comment -- @@ -281,6 +282,10 @@
mBackground.dispose();
mBackground = null;
}
        if (mForeground != null) {
            mForeground.dispose();
            mForeground = null;
        }

super.dispose();
}
//Synthetic comment -- @@ -337,11 +342,20 @@
}

if (mPaletteMode.isPreview()) {
            if (mForeground != null) {
                mForeground.dispose();
                mForeground = null;
            }
if (mBackground != null) {
mBackground.dispose();
                mBackground = null;
}
            RGB background = mPreviewIconFactory.getBackgroundColor();
mBackground = new Color(getDisplay(), background);
            RGB foreground = mPreviewIconFactory.getForegroundColor();
            if (foreground != null) {
                mForeground = new Color(getDisplay(), foreground);
            }
}

AndroidTargetData targetData = Sdk.getCurrent().getTargetData(target);
//Synthetic comment -- @@ -452,6 +466,10 @@
} else {
// Just use an Icon+Text item for these for now
item = new IconTextItem(parent, desc);
                    if (mForeground != null) {
                        item.setForeground(mForeground);
                        item.setBackground(mBackground);
                    }
}
break;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 998b28a..9599039 100644

//Synthetic comment -- @@ -13,14 +13,20 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.eclipse.adt.AndroidConstants.DOT_PNG;
import static com.android.ide.eclipse.adt.AndroidConstants.DOT_XML;

import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.StyleResourceValue;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.Params.RenderingMode;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -30,18 +36,24 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.sdklib.util.Pair;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.RGB;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
//Synthetic comment -- @@ -53,14 +65,19 @@
import java.util.Set;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
* Factory which can provide preview icons for android views of a particular SDK and
* editor's configuration chooser
*/
public class PreviewIconFactory {
    private static final String TAG_ITEM = "item"; //$NON-NLS-1$
    private static final String ATTR_COLOR = "color";  //$NON-NLS-1$
private PaletteControl mPalette;
private RGB mBackground;
    private RGB mForeground;
private File mImageDir;

private static final String PREVIEW_INFO_FILE = "preview.properties"; //$NON-NLS-1$
//Synthetic comment -- @@ -77,6 +94,7 @@
public void reset() {
mImageDir = null;
mBackground = null;
        mForeground = null;
}

/**
//Synthetic comment -- @@ -143,8 +161,8 @@
}

/**
     * Renders ALL the widgets and then extracts image data for each view and saves it on
     * disk
*/
private boolean render() {
LayoutEditor layoutEditor = mPalette.getEditor().getLayoutEditor();
//Synthetic comment -- @@ -209,10 +227,21 @@

// TODO - use resource resolution instead?
if (mBackground == null) {
                            Pair<RGB, RGB> themeColors = getColorsFromTheme();

                            RGB bg = themeColors.getFirst();
                            RGB fg = themeColors.getSecond();

                            if (bg == null) {
                                int p = image.getRGB(image.getWidth() - 1, image.getHeight() - 1);
                                bg = new RGB((p & 0xFF0000) >> 16, (p & 0xFF00) >> 8, p & 0xFF);
                                // This isn't reliable - for example, for some themes the
                                // background is a 9 patch image - so in this case don't
                                // set the foreground color
                                fg = null;
                            }

                            storeBackground(imageDir, bg, fg);
}

List<ViewInfo> viewInfoList = session.getRootViews();
//Synthetic comment -- @@ -220,6 +249,8 @@
// We don't render previews under a <merge> so there should
// only be one root.
ViewInfo firstRoot = viewInfoList.get(0);
                            int parentX = firstRoot.getLeft();
                            int parentY = firstRoot.getTop();
List<ViewInfo> infos = firstRoot.getChildren();
for (ViewInfo info : infos) {
Object cookie = info.getCookie();
//Synthetic comment -- @@ -233,10 +264,10 @@
// On Windows, perhaps we need to rename instead?
file.delete();
}
                                int x1 = parentX + info.getLeft();
                                int y1 = parentY + info.getTop();
                                int x2 = parentX + info.getRight();
                                int y2 = parentY + info.getBottom();
if (x1 != x2 && y1 != y2) {
savePreview(file, image, x1, y1, x2, y2);
}
//Synthetic comment -- @@ -252,6 +283,125 @@
return true;
}

    /**
     * Look up the background and foreground colors from the theme. May not find either
     * the background or foreground or both, but will always return a pair of possibly
     * null colors.
     *
     * @return a pair of possibly null color descriptions
     */
    private Pair<RGB, RGB> getColorsFromTheme() {
        RGB background = null;
        RGB foreground = null;

        ResourceResolver resources = mPalette.getEditor().createResolver();
        StyleResourceValue theme = resources.getTheme();
        if (theme != null) {
            background = resolveThemeColor(resources, "windowBackground"); //$NON-NLS-1$
            if (background == null) {
                background = resolveThemeColor(resources, "colorBackground"); //$NON-NLS-1$
            }
            foreground = resolveThemeColor(resources, "textColorPrimary"); //$NON-NLS-1$
        }

        return Pair.of(background, foreground);
    }

    private static RGB resolveThemeColor(ResourceResolver resources, String resourceName) {
        ResourceValue textColor = resources.findItemInTheme(resourceName);
        textColor = resources.resolveResValue(textColor);
        if (textColor == null) {
            return null;
        }
        String value = textColor.getValue();

        while (value != null) {
            if (value.startsWith("#")) { //$NON-NLS-1$
                try {
                    int rgba = ImageUtils.getColor(value);
                    // Drop alpha channel
                    return new RGB((rgba & 0xFF0000) >> 16, (rgba & 0xFF00) >> 8, rgba & 0xFF);
                } catch (NumberFormatException nfe) {
                    ;
                }
                return null;
            }
            if (value.startsWith("@")) { //$NON-NLS-1$
                boolean isFramework = textColor.isFramework();
                textColor = resources.findResValue(value, isFramework);
                if (textColor != null) {
                    value = textColor.getValue();
                } else {
                    break;
                }
            } else {
                File file = new File(value);
                if (file.exists() && file.getName().endsWith(DOT_XML)) {
                    // Parse
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    BufferedInputStream bis = null;
                    try {
                        bis = new BufferedInputStream(new FileInputStream(file));
                        InputSource is = new InputSource(bis);
                        factory.setNamespaceAware(true);
                        factory.setValidating(false);
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document document = builder.parse(is);
                        NodeList items = document.getElementsByTagName(TAG_ITEM);

                        value = findColorValue(items);
                        continue;
                    } catch (Exception e) {
                        AdtPlugin.log(e, "Failed parsing color file %1$s", file.getName());
                    } finally {
                        if (bis != null) {
                            try {
                                bis.close();
                            } catch (IOException e) {
                                // Nothing useful can be done here
                            }
                        }
                    }
                }

                return null;
            }
        }

        return null;
    }

    /**
     *  Searches a color XML file for the color definition element that does not
     * have an associated state and returns its color
     */
    private static String findColorValue(NodeList items) {
        for (int i = 0, n = items.getLength(); i < n; i++) {
            // Find non-state color definition
            Node item = items.item(i);
            boolean hasState = false;
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) item;
                if (element.hasAttributeNS(ANDROID_URI, ATTR_COLOR)) {
                    NamedNodeMap attributes = element.getAttributes();
                    for (int j = 0, m = attributes.getLength(); j < m; j++) {
                        Attr attribute = (Attr) attributes.item(j);
                        if (attribute.getLocalName().startsWith("state_")) { //$NON-NLS-1$
                            hasState = true;
                            break;
                        }
                    }

                    if (!hasState) {
                        return element.getAttributeNS(ANDROID_URI, ATTR_COLOR);
                    }
                }
            }
        }

        return null;
    }

private String getFileName(ElementDescriptor descriptor) {
return descriptor.getUiName() + DOT_PNG;
}
//Synthetic comment -- @@ -304,8 +454,8 @@
if (themeName.startsWith(themeNamePrefix)) {
themeName = themeName.substring(themeNamePrefix.length());
}
            String dirName = String.format("palette-preview-%s-%s-%s", cleanup(targetName),
                    cleanup(themeName), cleanup(mPalette.getCurrentDevice()));
IPath dirPath = pluginState.append(dirName);

mImageDir = new File(dirPath.toOSString());
//Synthetic comment -- @@ -328,21 +478,50 @@
}
}

    private void storeBackground(File imageDir, RGB bg, RGB fg) {
        mBackground = bg;
        mForeground = fg;
File file = new File(imageDir, PREVIEW_INFO_FILE);
        String colors = String.format(
                "background=#%02x%02x%02x\nforeground=#%02x%02x%02x\\n", //$NON-NLS-1$
                bg.red, bg.green, bg.blue,
                fg.red, fg.green, fg.blue);
AdtPlugin.writeFile(file, colors);
}

public RGB getBackgroundColor() {
if (mBackground == null) {
            initColors();
        }

        return mBackground;
    }

    public RGB getForegroundColor() {
        if (mForeground == null) {
            initColors();
        }

        return mForeground;
    }

    public void initColors() {
        try {
            // Already initialized? Foreground can be null which would call
            // initColors again and again, but background is never null after
            // initialization so we use it as the have-initialized flag.
            if (mBackground != null) {
                return;
            }

File imageDir = getImageDir(false);
if (!imageDir.exists()) {
render();

                // Initialized as part of the render
                if (mBackground != null) {
                    return;
                }
}

File file = new File(imageDir, PREVIEW_INFO_FILE);
//Synthetic comment -- @@ -366,22 +545,22 @@

String colorString = (String) properties.get("background"); //$NON-NLS-1$
if (colorString != null) {
                    int rgb = ImageUtils.getColor(colorString.trim());
                    mBackground = new RGB((rgb & 0xFF0000) >> 16, (rgb & 0xFF00) >> 8, rgb & 0xFF);
                }
                colorString = (String) properties.get("foreground"); //$NON-NLS-1$
                if (colorString != null) {
                    int rgb = ImageUtils.getColor(colorString.trim());
                    mForeground = new RGB((rgb & 0xFF0000) >> 16, (rgb & 0xFF00) >> 8, rgb & 0xFF);
}
}

if (mBackground == null) {
                mBackground = new RGB(0, 0, 0);
}
            // mForeground is allowed to be null.
        } catch (Throwable t) {
            AdtPlugin.log(t, "Cannot initialize preview color settings");
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtilsTest.java
//Synthetic comment -- index c611bab..73129f4 100644

//Synthetic comment -- @@ -298,5 +298,9 @@
assertEquals(0xFFFF0000, sub.getRGB(9, 9));
}

    public void testGetColor() throws Exception {
        assertEquals(0xFF000000, ImageUtils.getColor("#000"));
        assertEquals(0xFF000000, ImageUtils.getColor("#000000"));
        assertEquals(0xABCDEF91, ImageUtils.getColor("#ABCDEF91"));
    }
}







