/*Show previews of resources in the resource & reference choosers

This is a first cut of previews in the Resource Chooser and the
Reference Chooser. When the selected resource is a drawable or a
color, then a tray is shown on the right hand side of the dialog with
a preview of the given drawable or color. If the color is a state
list, then the fallback color is shown.

Limitations:

* The preview area is of fixed size (100x100) which can cause some
  drawables with other aspect ratios to appear stretched.
* The previews only work for resource/reference choosers associated
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
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageControl.java
//Synthetic comment -- index 7af89f8..d04e3d6 100644

//Synthetic comment -- @@ -71,6 +71,13 @@
});
}

public void setScale(float scale) {
mScale = scale;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java
//Synthetic comment -- index b574eae..249b135 100644

//Synthetic comment -- @@ -500,4 +500,21 @@

return scaled;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index b2ebbf1..ae41f84 100644

//Synthetic comment -- @@ -16,13 +16,11 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.FQCN_DATE_PICKER;
import static com.android.ide.common.layout.LayoutConstants.FQCN_EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_TIME_PICKER;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_PNG;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
//Synthetic comment -- @@ -41,6 +39,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gre.ViewMetadataRepository.RenderMode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.sdklib.IAndroidTarget;
import com.android.util.Pair;
//Synthetic comment -- @@ -49,13 +48,10 @@
import org.eclipse.core.runtime.IStatus;
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
//Synthetic comment -- @@ -70,16 +66,12 @@
import java.util.Properties;

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
//Synthetic comment -- @@ -438,9 +430,11 @@
*/
private RGB renderDrawableResource(String themeItemName) {
GraphicalEditorPart editor = mPalette.getEditor();
BufferedImage image = RenderService.create(editor)
.setSize(100, 100)
            .renderThemeItem(themeItemName);
if (image != null) {
// Use the middle pixel as the color since that works better for gradients;
// solid colors work too.
//Synthetic comment -- @@ -453,97 +447,7 @@

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
                    return ImageUtils.intToRgb(rgba);
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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
//Synthetic comment -- index bee1289..36cea84 100644

//Synthetic comment -- @@ -387,16 +387,19 @@
}

/**
     * Renders the given resource which should refer to a drawable and returns it
* as an image
*
     * @param itemName the theme item to be looked up and rendered
* @return the image, or null if something went wrong
*/
    public BufferedImage renderThemeItem(String itemName) {
finishConfiguration();

        ResourceValue drawableResourceValue = mResourceResolver.findItemInTheme(itemName);
DrawableParams params = new DrawableParams(drawableResourceValue, mProject, mWidth, mHeight,
mDensity, mXdpi, mYdpi, mResourceResolver, mProjectCallback, mMinSdkVersion,
mTargetSdkVersion, mLogger);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java
//Synthetic comment -- index 3d872c7..2f3c1db 100644

//Synthetic comment -- @@ -250,6 +250,19 @@
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
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutCanvas;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.RenderService;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SelectionManager;
//Synthetic comment -- @@ -40,6 +41,7 @@
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.ui.MarginChooser;
import com.android.ide.eclipse.adt.internal.ui.ReferenceChooserDialog;
import com.android.ide.eclipse.adt.internal.ui.ResourceChooser;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -180,7 +182,8 @@
}

public String displayReferenceInput(String currentValue) {
        AndroidXmlEditor editor = mRulesEngine.getEditor().getLayoutEditor();
IProject project = editor.getProject();
if (project != null) {
// get the resource repository for this project and the system resources.
//Synthetic comment -- @@ -194,6 +197,7 @@
project,
projectRepository,
shell);

dlg.setCurrentResource(currentValue);

//Synthetic comment -- @@ -211,7 +215,8 @@

private String displayResourceInput(String resourceTypeName, String currentValue,
IInputValidator validator) {
        AndroidXmlEditor editor = mRulesEngine.getEditor().getLayoutEditor();
IProject project = editor.getProject();
ResourceType type = ResourceType.getEnum(resourceTypeName);
if (project != null) {
//Synthetic comment -- @@ -229,6 +234,7 @@
// open a resource chooser dialog for specified resource type.
ResourceChooser dlg = new ResourceChooser(project, type, projectRepository,
systemRepository, shell);

if (validator != null) {
// Ensure wide enough to accommodate validator error message








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java
//Synthetic comment -- index 467ae49..107ac4e 100644

//Synthetic comment -- @@ -17,15 +17,19 @@
package com.android.ide.eclipse.adt.internal.resources;

import static com.android.AndroidConstants.FD_RES_VALUES;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_STYLE;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_STYLE;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;
import static com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors.NAME_ATTR;
import static com.android.sdklib.SdkConstants.FD_RESOURCES;

import com.android.ide.common.resources.ResourceDeltaKind;
import com.android.ide.common.resources.configuration.CountryCodeQualifier;
import com.android.ide.common.resources.configuration.DockModeQualifier;
import com.android.ide.common.resources.configuration.FolderConfiguration;
//Synthetic comment -- @@ -48,6 +52,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.refactoring.VisualRefactoring;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors;
import com.android.ide.eclipse.adt.internal.editors.xml.Hyperlinks;
//Synthetic comment -- @@ -67,19 +72,27 @@
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
//Synthetic comment -- @@ -87,12 +100,18 @@
import java.util.Map;
import java.util.Set;

/**
* Helper class to deal with SWT specifics for the resources.
*/
@SuppressWarnings("restriction") // XML model
public class ResourceHelper {

private final static Map<Class<?>, Image> sIconMap = new HashMap<Class<?>, Image>(
FolderConfiguration.getQualifierCount());

//Synthetic comment -- @@ -443,4 +462,106 @@
}
return layoutName;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ReferenceChooserDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ReferenceChooserDialog.java
//Synthetic comment -- index b436471..caf05aa 100644

//Synthetic comment -- @@ -72,6 +72,7 @@
private Button mNewResButton;
private final IProject mProject;
private TreeViewer mTreeViewer;

/**
* @param project
//Synthetic comment -- @@ -92,6 +93,10 @@
setDialogBoundsSettings(sDialogSettings, getDialogBoundsStrategy());
}

public void setCurrentResource(String resource) {
mCurrentResource = resource;
}
//Synthetic comment -- @@ -183,6 +188,21 @@
protected void handleSelection() {
validateCurrentSelection();
updateNewResButton();
}

protected void handleDoubleClick() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 5388b4e..fc026e4 100644

//Synthetic comment -- @@ -80,6 +80,7 @@
private String mCurrentResource;
private final IProject mProject;
private IInputValidator mInputValidator;

/**
* Creates a Resource Chooser dialog.
//Synthetic comment -- @@ -111,6 +112,10 @@
mResourceType.getDisplayName().toLowerCase()));
}

@Override
protected void createButtonsForButtonBar(Composite parent) {
createButton(parent, CLEAR_BUTTON_ID, "Clear", false /*defaultButton*/);
//Synthetic comment -- @@ -255,6 +260,11 @@
updateStatus(status);
}
}
}

private String createNewValue(ResourceType type) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourcePreviewHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourcePreviewHelper.java
new file mode 100644
//Synthetic comment -- index 0000000..00636cc

//Synthetic comment -- @@ -0,0 +1,148 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtilsTest.java
//Synthetic comment -- index 8f08fe9..4bd0bba 100644

//Synthetic comment -- @@ -17,6 +17,7 @@

import com.android.ide.common.api.Rect;

import org.eclipse.swt.graphics.Rectangle;

import java.awt.Color;
//Synthetic comment -- @@ -345,4 +346,14 @@
assertEquals(0xFFFF0000, scaled.getRGB(100, 100));
assertEquals(0xFF00FF00, scaled.getRGB(199, 199));
}
}







