/*Show previews of resources in the resource & reference choosers

This is a first cut of previews in the Resource Chooser and the
Reference Chooser. When the selected resource is a drawable or a
color, then a tray is shown on the right hand side of the dialog with
a preview of the given drawable or color. If the color is a state
list, then the fallback color is shown.

The previews only work for resource/reference choosers associated
with a layout editor (because it looks up the associated rendering
configuration from that editor). This should be made more generic
such that it can work for example when bringing up the resource
chooser from a manifest form editor.

Change-Id:I9f2896cb7ce1468076a3ca83f820be7e9affcda6*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageViewRule.java
//Synthetic comment -- index a33323e..f7d8d5d 100644

//Synthetic comment -- @@ -40,6 +40,9 @@
new PropertySettingNodeHandler(ANDROID_URI, ATTR_SRC,
src.length() > 0 ? src : null));
return;
            } else {
                // Remove the view; the insertion was canceled
                parent.removeChild(node);
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtConstants.java
//Synthetic comment -- index e7c5157..d89acb5 100644

//Synthetic comment -- @@ -108,6 +108,14 @@
public final static String DOT_RES = DOT + EXT_RES;
/** Dot-Extension for PNG files, i.e. ".png" */
public static final String DOT_PNG = ".png"; //$NON-NLS-1$
    /** Dot-Extension for 9-patch files, i.e. ".9.png" */
    public static final String DOT_9PNG = ".9.png"; //$NON-NLS-1$
    /** Dot-Extension for GIF files, i.e. ".gif" */
    public static final String DOT_GIF = ".gif"; //$NON-NLS-1$
    /** Dot-Extension for JPEG files, i.e. ".jpg" */
    public static final String DOT_JPG = ".jpg"; //$NON-NLS-1$
    /** Dot-Extension for BMP files, i.e. ".bmp" */
    public static final String DOT_BMP = ".bmp"; //$NON-NLS-1$

/** Name of the android sources directory */
public static final String FD_ANDROID_SOURCES = "sources"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
new file mode 100644
//Synthetic comment -- index 0000000..bd40d1b

//Synthetic comment -- @@ -0,0 +1,26 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.android.ide.eclipse.adt;


/** Utility methods for ADT */
public class AdtUtils {
    public static boolean endsWithIgnoreCase(String string, String suffix) {
        return string.regionMatches(true /* ignoreCase */, string.length() - suffix.length(),
                suffix, 0, suffix.length());
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageControl.java
//Synthetic comment -- index 7af89f8..d04e3d6 100644

//Synthetic comment -- @@ -71,6 +71,13 @@
});
}

    public void setImage(Image image) {
        if (mDisposeImage) {
            mImage.dispose();
        }
        mImage = image;
    }

public void setScale(float scale) {
mScale = scale;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java
//Synthetic comment -- index b574eae..fbeecb9 100644

//Synthetic comment -- @@ -15,6 +15,13 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_9PNG;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_BMP;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_GIF;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_JPG;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_PNG;
import static com.android.ide.eclipse.adt.AdtUtils.endsWithIgnoreCase;

import com.android.ide.common.api.Rect;

import org.eclipse.swt.graphics.RGB;
//Synthetic comment -- @@ -484,8 +491,8 @@
public static BufferedImage scale(BufferedImage source, double xScale, double yScale) {
int sourceWidth = source.getWidth();
int sourceHeight = source.getHeight();
        int destWidth = Math.max(1, (int) (xScale * sourceWidth));
        int destHeight = Math.max(1, (int) (yScale * sourceHeight));
BufferedImage scaled = new BufferedImage(destWidth, destHeight, source.getType());
Graphics2D g2 = scaled.createGraphics();
g2.setComposite(AlphaComposite.Src);
//Synthetic comment -- @@ -500,4 +507,37 @@

return scaled;
}

    /**
     * Returns true if the given file path points to an image file recognized by
     * Android. See http://developer.android.com/guide/appendix/media-formats.html
     * for details.
     *
     * @param path the filename to be tested
     * @return true if the file represents an image file
     */
    public static boolean hasImageExtension(String path) {
        return endsWithIgnoreCase(path, DOT_PNG)
            || endsWithIgnoreCase(path, DOT_9PNG)
            || endsWithIgnoreCase(path, DOT_GIF)
            || endsWithIgnoreCase(path, DOT_JPG)
            || endsWithIgnoreCase(path, DOT_BMP);
    }

    /**
     * Creates a new image of the given size filled with the given color
     *
     * @param width the width of the image
     * @param height the height of the image
     * @param color the color of the image
     * @return a new image of the given size filled with the given color
     */
    public static BufferedImage createColoredImage(int width, int height, RGB color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(new Color(color.red, color.green, color.blue));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.dispose();
        return image;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index b2ebbf1..ae41f84 100644

//Synthetic comment -- @@ -16,13 +16,11 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.FQCN_DATE_PICKER;
import static com.android.ide.common.layout.LayoutConstants.FQCN_EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_TIME_PICKER;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_PNG;

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
//Synthetic comment -- @@ -41,6 +39,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gre.ViewMetadataRepository.RenderMode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.sdklib.IAndroidTarget;
import com.android.util.Pair;
//Synthetic comment -- @@ -49,13 +48,10 @@
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.RGB;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
//Synthetic comment -- @@ -70,16 +66,12 @@
import java.util.Properties;

import javax.imageio.ImageIO;

/**
* Factory which can provide preview icons for android views of a particular SDK and
* editor's configuration chooser
*/
public class PreviewIconFactory {
private PaletteControl mPalette;
private RGB mBackground;
private RGB mForeground;
//Synthetic comment -- @@ -438,9 +430,11 @@
*/
private RGB renderDrawableResource(String themeItemName) {
GraphicalEditorPart editor = mPalette.getEditor();
        ResourceResolver resources = editor.getResourceResolver();
        ResourceValue resourceValue = resources.findItemInTheme(themeItemName);
BufferedImage image = RenderService.create(editor)
.setSize(100, 100)
            .renderDrawable(resourceValue);
if (image != null) {
// Use the middle pixel as the color since that works better for gradients;
// solid colors work too.
//Synthetic comment -- @@ -453,97 +447,7 @@

private static RGB resolveThemeColor(ResourceResolver resources, String resourceName) {
ResourceValue textColor = resources.findItemInTheme(resourceName);
        return ResourceHelper.resolveColor(resources, textColor);
}

private String getFileName(ElementDescriptor descriptor) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
//Synthetic comment -- index bee1289..36cea84 100644

//Synthetic comment -- @@ -387,16 +387,19 @@
}

/**
     * Renders the given resource value (which should refer to a drawable) and returns it
* as an image
*
     * @param drawableResourceValue the drawable resource value to be rendered, or null
* @return the image, or null if something went wrong
*/
    public BufferedImage renderDrawable(ResourceValue drawableResourceValue) {
        if (drawableResourceValue == null) {
            return null;
        }

finishConfiguration();

DrawableParams params = new DrawableParams(drawableResourceValue, mProject, mWidth, mHeight,
mDensity, mXdpi, mYdpi, mResourceResolver, mProjectCallback, mMinSdkVersion,
mTargetSdkVersion, mLogger);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java
//Synthetic comment -- index 3d872c7..22ebf10 100644

//Synthetic comment -- @@ -17,13 +17,17 @@

import com.android.ide.common.api.Rect;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.util.List;
//Synthetic comment -- @@ -61,6 +65,44 @@
}

/**
     * Returns true if the given type of {@link BufferedImage} is supported for
     * conversion. For unsupported formats, use
     * {@link #convertToCompatibleFormat(BufferedImage)} first.
     *
     * @param imageType the {@link BufferedImage#getType()}
     * @return true if we can convert the given buffered image format
     */
    private static boolean isSupportedPaletteType(int imageType) {
        switch (imageType) {
            case BufferedImage.TYPE_INT_RGB:
            case BufferedImage.TYPE_INT_ARGB:
            case BufferedImage.TYPE_INT_ARGB_PRE:
            case BufferedImage.TYPE_3BYTE_BGR:
            case BufferedImage.TYPE_4BYTE_ABGR:
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
                return true;
            default:
                return false;
        }
    }

    /** Converts the given arbitrary {@link BufferedImage} to another {@link BufferedImage}
     * in a format that is supported (see {@link #isSupportedPaletteType(int)})
     *
     * @param image the image to be converted
     * @return a new image that is in a guaranteed compatible format
     */
    private static BufferedImage convertToCompatibleFormat(BufferedImage image) {
        BufferedImage converted = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = converted.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        return converted;
    }

    /**
* Converts an AWT {@link BufferedImage} into an equivalent SWT {@link Image}. Whether
* the transparency data is transferred is optional, and this method can also apply an
* alpha adjustment during the conversion.
//Synthetic comment -- @@ -79,16 +121,31 @@
*/
public static Image convertToSwt(Display display, BufferedImage awtImage,
boolean transferAlpha, int globalAlpha) {
        if (!isSupportedPaletteType(awtImage.getType())) {
            awtImage = convertToCompatibleFormat(awtImage);
        }

int width = awtImage.getWidth();
int height = awtImage.getHeight();

WritableRaster raster = awtImage.getRaster();
        DataBuffer dataBuffer = raster.getDataBuffer();
ImageData imageData =
new ImageData(width, height, 32, getAwtPaletteData(awtImage.getType()));

        if (dataBuffer instanceof DataBufferInt) {
            int[] imageDataBuffer = ((DataBufferInt) dataBuffer).getData();
            imageData.setPixels(0, 0, imageDataBuffer.length, imageDataBuffer, 0);
        } else if (dataBuffer instanceof DataBufferByte) {
            byte[] imageDataBuffer = ((DataBufferByte) dataBuffer).getData();
            try {
                imageData.setPixels(0, 0, imageDataBuffer.length, imageDataBuffer, 0);
            } catch (SWTException se) {
                // Unsupported depth
                return convertToSwt(display, convertToCompatibleFormat(awtImage),
                        transferAlpha, globalAlpha);
            }
        }

if (transferAlpha) {
byte[] alphaData = new byte[height * width];
//Synthetic comment -- @@ -250,6 +307,19 @@
}

/**
     * Creates a new empty/blank image of the given size
     *
     * @param display the display to associate the image with
     * @param width the width of the image
     * @param height the height of the image
     * @return a new blank image of the given size
     */
    public static Image createEmptyImage(Display display, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        return SwtUtils.convertToSwt(display, image, false, 0);
    }

    /**
* Converts the given SWT {@link Rectangle} into an ADT {@link Rect}
*
* @param swtRect the SWT {@link Rectangle}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java
//Synthetic comment -- index e775420..5f60911 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import com.android.ide.eclipse.adt.internal.actions.AddCompatibilityJarAction;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutCanvas;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.RenderService;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SelectionManager;
//Synthetic comment -- @@ -40,6 +41,7 @@
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.ui.MarginChooser;
import com.android.ide.eclipse.adt.internal.ui.ResourcePreviewHelper;
import com.android.ide.eclipse.adt.internal.ui.ReferenceChooserDialog;
import com.android.ide.eclipse.adt.internal.ui.ResourceChooser;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -180,7 +182,8 @@
}

public String displayReferenceInput(String currentValue) {
        GraphicalEditorPart graphicalEditor = mRulesEngine.getEditor();
        AndroidXmlEditor editor = graphicalEditor.getLayoutEditor();
IProject project = editor.getProject();
if (project != null) {
// get the resource repository for this project and the system resources.
//Synthetic comment -- @@ -194,6 +197,7 @@
project,
projectRepository,
shell);
            dlg.setPreviewHelper(new ResourcePreviewHelper(dlg, graphicalEditor));

dlg.setCurrentResource(currentValue);

//Synthetic comment -- @@ -211,7 +215,8 @@

private String displayResourceInput(String resourceTypeName, String currentValue,
IInputValidator validator) {
        GraphicalEditorPart graphicalEditor = mRulesEngine.getEditor();
        AndroidXmlEditor editor = graphicalEditor.getLayoutEditor();
IProject project = editor.getProject();
ResourceType type = ResourceType.getEnum(resourceTypeName);
if (project != null) {
//Synthetic comment -- @@ -229,6 +234,7 @@
// open a resource chooser dialog for specified resource type.
ResourceChooser dlg = new ResourceChooser(project, type, projectRepository,
systemRepository, shell);
            dlg.setPreviewHelper(new ResourcePreviewHelper(dlg, graphicalEditor));

if (validator != null) {
// Ensure wide enough to accommodate validator error message








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java
//Synthetic comment -- index 467ae49..107ac4e 100644

//Synthetic comment -- @@ -17,15 +17,19 @@
package com.android.ide.eclipse.adt.internal.resources;

import static com.android.AndroidConstants.FD_RES_VALUES;
import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_STYLE;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_STYLE;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;
import static com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors.NAME_ATTR;
import static com.android.sdklib.SdkConstants.FD_RESOURCES;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ResourceDeltaKind;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.CountryCodeQualifier;
import com.android.ide.common.resources.configuration.DockModeQualifier;
import com.android.ide.common.resources.configuration.FolderConfiguration;
//Synthetic comment -- @@ -48,6 +52,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.refactoring.VisualRefactoring;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors;
import com.android.ide.eclipse.adt.internal.editors.xml.Hyperlinks;
//Synthetic comment -- @@ -67,19 +72,27 @@
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
//Synthetic comment -- @@ -87,12 +100,18 @@
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
* Helper class to deal with SWT specifics for the resources.
*/
@SuppressWarnings("restriction") // XML model
public class ResourceHelper {

    private static final String TAG_ITEM = "item"; //$NON-NLS-1$
    private static final String ATTR_COLOR = "color";  //$NON-NLS-1$

private final static Map<Class<?>, Image> sIconMap = new HashMap<Class<?>, Image>(
FolderConfiguration.getQualifierCount());

//Synthetic comment -- @@ -443,4 +462,106 @@
}
return layoutName;
}

    /**
     * Tries to resolve the given resource value to an actual RGB color. For state lists
     * it will pick the simplest/fallback color.
     *
     * @param resources the resource resolver to use to follow color references
     * @param color the color to resolve
     * @return the corresponding {@link RGB} color, or null
     */
    public static RGB resolveColor(ResourceResolver resources, ResourceValue color) {
        color = resources.resolveResValue(color);
        if (color == null) {
            return null;
        }
        String value = color.getValue();

        while (value != null) {
            if (value.startsWith("#")) { //$NON-NLS-1$
                try {
                    int rgba = ImageUtils.getColor(value);
                    // Drop alpha channel
                    return ImageUtils.intToRgb(rgba);
                } catch (NumberFormatException nfe) {
                    ;
                }
                return null;
            }
            if (value.startsWith("@")) { //$NON-NLS-1$
                boolean isFramework = color.isFramework();
                color = resources.findResValue(value, isFramework);
                if (color != null) {
                    value = color.getValue();
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
     * Searches a color XML file for the color definition element that does not
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
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ReferenceChooserDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ReferenceChooserDialog.java
//Synthetic comment -- index b436471..caf05aa 100644

//Synthetic comment -- @@ -72,6 +72,7 @@
private Button mNewResButton;
private final IProject mProject;
private TreeViewer mTreeViewer;
    private ResourcePreviewHelper mPreviewHelper;

/**
* @param project
//Synthetic comment -- @@ -92,6 +93,10 @@
setDialogBoundsSettings(sDialogSettings, getDialogBoundsStrategy());
}

    public void setPreviewHelper(ResourcePreviewHelper previewHelper) {
        mPreviewHelper = previewHelper;
    }

public void setCurrentResource(String resource) {
mCurrentResource = resource;
}
//Synthetic comment -- @@ -183,6 +188,21 @@
protected void handleSelection() {
validateCurrentSelection();
updateNewResButton();

        if (mPreviewHelper != null) {
            TreePath treeSelection = getSelection();
            ResourceType type = null;
            if (treeSelection != null && treeSelection.getSegmentCount() == 2) {
                Object segment = treeSelection.getSegment(0);
                if (segment instanceof ResourceType) {
                    type = (ResourceType) segment;
                    // Ensure that mCurrentResource is valid
                    computeResult();
                }
            }

            mPreviewHelper.updatePreview(type, mCurrentResource);
        }
}

protected void handleDoubleClick() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 5388b4e..fc026e4 100644

//Synthetic comment -- @@ -80,6 +80,7 @@
private String mCurrentResource;
private final IProject mProject;
private IInputValidator mInputValidator;
    private ResourcePreviewHelper mPreviewHelper;

/**
* Creates a Resource Chooser dialog.
//Synthetic comment -- @@ -111,6 +112,10 @@
mResourceType.getDisplayName().toLowerCase()));
}

    public void setPreviewHelper(ResourcePreviewHelper previewHelper) {
        mPreviewHelper = previewHelper;
    }

@Override
protected void createButtonsForButtonBar(Composite parent) {
createButton(parent, CLEAR_BUTTON_ID, "Clear", false /*defaultButton*/);
//Synthetic comment -- @@ -255,6 +260,11 @@
updateStatus(status);
}
}

        if (mPreviewHelper != null) {
            computeResult();
            mPreviewHelper.updatePreview(mResourceType, mCurrentResource);
        }
}

private String createNewValue(ResourceType type) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourcePreviewHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourcePreviewHelper.java
new file mode 100644
//Synthetic comment -- index 0000000..d26cbae

//Synthetic comment -- @@ -0,0 +1,184 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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
package com.android.ide.eclipse.adt.internal.ui;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_9PNG;
import static com.android.ide.eclipse.adt.AdtUtils.endsWithIgnoreCase;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageControl;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.RenderService;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SwtUtils;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.resources.ResourceType;

import org.eclipse.jface.dialogs.DialogTray;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The {@link ResourcePreviewHelper} provides help to {@link TrayDialog} resource choosers
 * where some resources (such as drawables and colors) are previewed in the tray area.
 */
public class ResourcePreviewHelper {
    /**
     * The width of the preview rendering
     * <p>
     * TODO: Make the preview rendering resize if the tray area is resized
     */
    private static final int WIDTH = 100;
    /** The height of the preview rendering */
    private static final int HEIGHT = 100;

    private final GraphicalEditorPart mEditor;
    private final TrayDialog mTrayDialog;

    private boolean mShowingPreview;
    private DialogTray mPreviewTray;
    private ImageControl mPreviewImageControl;

    /**
     * Constructs a new {@link ResourcePreviewHelper}.
     * <p>
     * TODO: Add support for performing previews without an associated graphical editor,
     * such as previewing icons from the manifest form editor; just pick default
     * configuration settings in that case.
     *
     * @param trayDialog the associated tray-capable dialog
     * @param editor a graphical editor. This is currently needed in order to provide
     *            configuration data for the rendering.
     */
    public ResourcePreviewHelper(TrayDialog trayDialog, GraphicalEditorPart editor) {
        this.mTrayDialog = trayDialog;
        this.mEditor = editor;
    }

    /**
     * Updates the preview based on the current selection and resource type, possibly
     * hiding or opening the tray in the process.
     *
     * @param type the resource type for the selected resource
     * @param resource the full resource url
     */
    public void updatePreview(ResourceType type, String resource) {
        boolean showPreview = type == ResourceType.DRAWABLE || type == ResourceType.COLOR;
        if (showPreview) {
            if (mPreviewTray == null) {
                mPreviewTray = new DialogTray() {
                    @Override
                    protected Control createContents(Composite parent) {
                        // This creates a centered image control
                        Composite panel = new Composite(parent, SWT.NONE);
                        panel.setLayout(new GridLayout(3, false));
                        Label dummy1 = new Label(panel, SWT.NONE);
                        dummy1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
                        mPreviewImageControl = new ImageControl(panel, SWT.NONE, SwtUtils
                                .createEmptyImage(parent.getDisplay(), WIDTH, HEIGHT));
                        GridData gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
                        gd.widthHint = WIDTH;
                        gd.heightHint = HEIGHT;
                        mPreviewImageControl.setLayoutData(gd);
                        Label dummy2 = new Label(panel, SWT.NONE);
                        dummy2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));

                        return panel;
                    }

                };
            }

            if (!mShowingPreview) {
                mTrayDialog.openTray(mPreviewTray);
            }

            BufferedImage image = null;
            if (type == ResourceType.COLOR) {
                ResourceResolver resources = mEditor.getResourceResolver();
                ResourceValue value = resources.findResValue(resource, false);
                if (value != null) {
                    RGB color = ResourceHelper.resolveColor(resources, value);
                    if (color != null) {
                        image = ImageUtils.createColoredImage(WIDTH, HEIGHT, color);
                    }
                }
            } else {
                assert type == ResourceType.DRAWABLE;

                ResourceResolver resources = mEditor.getResourceResolver();
                ResourceValue drawable = resources.findResValue(resource, false);
                if (drawable != null) {
                    String path = drawable.getValue();

                    // Special-case image files (other than 9-patch files) and render these
                    // directly, in order to provide proper aspect ratio handling and
                    // to handle scaling to show the full contents:
                    if (ImageUtils.hasImageExtension(path)
                            && !endsWithIgnoreCase(path, DOT_9PNG)) {
                        File file = new File(path);
                        if (file.exists()) {
                            try {
                                image = ImageIO.read(file);
                                int width = image.getWidth();
                                int height = image.getHeight();
                                if (width > WIDTH || height > HEIGHT) {
                                    double xScale = WIDTH / (double) width;
                                    double yScale = HEIGHT / (double) height;
                                    double scale = Math.min(xScale, yScale);
                                    image = ImageUtils.scale(image, scale, scale);
                                }
                            } catch (IOException e) {
                                AdtPlugin.log(e, "Can't read preview image %1$s", path);
                            }
                        }
                    }

                    if (image == null) {
                        RenderService renderService = RenderService.create(mEditor);
                        renderService.setSize(WIDTH, HEIGHT);
                        image = renderService.renderDrawable(drawable);
                    }
                }
            }
            Display display = mEditor.getSite().getShell().getDisplay();
            if (image != null) {
                mPreviewImageControl.setImage(SwtUtils.convertToSwt(display, image, true, -1));
            } else {
                mPreviewImageControl.setImage(SwtUtils.createEmptyImage(display, WIDTH, HEIGHT));
            }
            mPreviewImageControl.redraw();
        } else if (mPreviewTray != null && mShowingPreview) {
            mTrayDialog.closeTray();
        }
        mShowingPreview = showPreview;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/AdtUtilsTest.java
new file mode 100644
//Synthetic comment -- index 0000000..01fbe1b

//Synthetic comment -- @@ -0,0 +1,32 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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
package com.android.ide.eclipse.adt;

import junit.framework.TestCase;

public class AdtUtilsTest extends TestCase {
    public void testEndsWithIgnoreCase() {
        assertTrue(AdtUtils.endsWithIgnoreCase("foo", "foo"));
        assertTrue(AdtUtils.endsWithIgnoreCase("foo", "Foo"));
        assertTrue(AdtUtils.endsWithIgnoreCase("foo", "foo"));
        assertTrue(AdtUtils.endsWithIgnoreCase("Barfoo", "foo"));
        assertTrue(AdtUtils.endsWithIgnoreCase("BarFoo", "foo"));
        assertTrue(AdtUtils.endsWithIgnoreCase("BarFoo", "foO"));

        assertFalse(AdtUtils.endsWithIgnoreCase("foob", "foo"));
        assertFalse(AdtUtils.endsWithIgnoreCase("foo", "fo"));
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtilsTest.java
//Synthetic comment -- index 8f08fe9..4bd0bba 100644

//Synthetic comment -- @@ -17,6 +17,7 @@

import com.android.ide.common.api.Rect;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

import java.awt.Color;
//Synthetic comment -- @@ -345,4 +346,14 @@
assertEquals(0xFFFF0000, scaled.getRGB(100, 100));
assertEquals(0xFF00FF00, scaled.getRGB(199, 199));
}

    public void testCreateColoredImage() throws Exception {
        BufferedImage image = ImageUtils.createColoredImage(120, 110, new RGB(0xFE, 0xFD, 0xFC));
        assertEquals(120, image.getWidth());
        assertEquals(110, image.getHeight());
        assertEquals(0xFFFEFDFC, image.getRGB(0, 0));
        assertEquals(0xFFFEFDFC, image.getRGB(50, 50));
        assertEquals(0xFFFEFDFC, image.getRGB(119, 109));
    }

}







