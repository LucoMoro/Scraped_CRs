/*Palette previews with custom themes and other palette fixes

Palette previews are rendered on top of the default theme
background. For certain custom themes, where a gradient or image is
used, this can look terrible, since the preview images will include
portions of the gradient or image.

This changeset improves this situation by taking advantage of
layoutlib 5's ability to override the background. It now uses theme
resolution to find the background color, and paints the previews with
the custom background override to force this color. In addition, if
the theme background is not a color (but a gradient etc), it uses
layoutlib5's new render-drawable support to generate the background,
and it extracts a suitable color from it (currently, the center
pixel.)

This fix also ensures that the foreground color (used to paint labels
for the widgets that cannot be rendered, such as layouts) has a
suitable contrast with the background. After computing the background,
it computes the brightness, and if the foreground and background
differ less than 25% in brightness, then the foreground color is
forced to white or black depending on whether the background is light
or dark.

This changeset also contains a bugfix for a palette initialization
problem where in some cases the palette could come up empty (because
the code which attempts to avoid doing extra work when reloadPalette
is called repeatedly could be tricked by a scenario where the SDK
target does not change but its "have target data" status did.)

The open palette category is now preserved across palette refreshes
(which occur when you change palette modes or category/sorting
options, along with theme and render target changes).

Finally, the changeset also turns off the new window decorations
(system bar, action bar, etc) when generating preview images for both
palette previews and for drag & drop.

Change-Id:I8b8766ad45f2cb6a4a6b79a24c31ed0f08d1e826*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/AccordionControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/AccordionControl.java
//Synthetic comment -- index 0c9c0fc..24c582d 100644

//Synthetic comment -- @@ -41,7 +41,9 @@
import org.eclipse.swt.widgets.ScrollBar;

import java.util.ArrayList;
import java.util.List;

/**
* The accordion control allows a series of labels with associated content that can be
//Synthetic comment -- @@ -191,9 +193,10 @@
* @param greedy if true, grow vertically as much as possible
* @param wrapChildren if true, configure the child area to be horizontally laid out
*            with wrapping
*/
public AccordionControl(Composite parent, int style, List<?> headers,
            boolean greedy, boolean wrapChildren) {
super(parent, style);
mWrap = wrapChildren;

//Synthetic comment -- @@ -208,7 +211,7 @@

mOpen = IconFactory.getInstance().getIcon("open-folder");     //$NON-NLS-1$
mClosed = IconFactory.getInstance().getIcon("closed-folder"); //$NON-NLS-1$
        CLabel first = null;

for (Object header : headers) {
final CLabel label = new CLabel(this, SWT.SHADOW_OUT);
//Synthetic comment -- @@ -279,14 +282,17 @@
}

updateIcon(label);
            if (first == null) {
                first = label;
}
}

        // Initially show the first category
        if (headers.size() > 0) {
            toggle(first, false, false);
}
}

//Synthetic comment -- @@ -365,4 +371,23 @@
protected void checkSubclass() {
// Disable the check that prevents subclassing of SWT components
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index bfbbf31..7bcbdcb 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.StaticRenderSession;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
//Synthetic comment -- @@ -130,6 +131,7 @@
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//Synthetic comment -- @@ -1258,15 +1260,15 @@
* @param height the height to use for the layout, or -1 to use the height of the screen
*            associated with this editor
* @param explodeNodes a set of nodes to explode, or null for none
     * @param transparentBackground If true, the rendering will <b>not</b> paint the
     *            normal background requested by the theme, and it will instead paint the
     *            background using a fully transparent background color
* @param logger a logger where rendering errors are reported
* @param renderingMode the {@link RenderingMode} to use for rendering
* @return the resulting rendered image wrapped in an {@link RenderSession}
*/
public RenderSession render(UiDocumentNode model, int width, int height,
            Set<UiElementNode> explodeNodes, boolean transparentBackground,
LayoutLog logger, RenderingMode renderingMode) {
if (!ensureFileValid()) {
return null;
//Synthetic comment -- @@ -1278,7 +1280,7 @@

IProject iProject = mEditedFile.getProject();
return renderWithBridge(iProject, model, layoutLib, width, height, explodeNodes,
                transparentBackground, logger, null /* includeWithin */, renderingMode);
}

/**
//Synthetic comment -- @@ -1513,7 +1515,7 @@
}

RenderSession session = renderWithBridge(iProject, model, layoutLib, width, height,
                explodeNodes, false, logger, mIncludedWithin, renderingMode);

canvas.setSession(session, explodeNodes);

//Synthetic comment -- @@ -1555,7 +1557,7 @@

private RenderSession renderWithBridge(IProject iProject, UiDocumentNode model,
LayoutLibrary layoutLib, int width, int height, Set<UiElementNode> explodeNodes,
            boolean transparentBackground, LayoutLog logger, Reference includeWithin,
RenderingMode renderingMode) {
ResourceManager resManager = ResourceManager.getInstance();

//Synthetic comment -- @@ -1671,6 +1673,9 @@
mMinSdkVersion,
mTargetSdkVersion,
logger);

// FIXME make persistent and only reload when the manifest (or at least resources) chanage.
IFolderWrapper projectFolder = new IFolderWrapper(getProject());
//Synthetic comment -- @@ -1693,11 +1698,8 @@
params.setConfigScreenSize(ssq.getValue());
}

        if (transparentBackground) {
            // It doesn't matter what the background color is as long as the alpha
            // is 0 (fully transparent). We're using red to make it more obvious if
            // for some reason the background is painted when it shouldn't be.
            params.setOverrideBgColor(0x00FF0000);
}

// set the Image Overlay as the image factory.
//Synthetic comment -- @@ -1715,6 +1717,44 @@
}
}

ResourceResolver createResolver() {
String theme = mConfigComposite.getTheme();
boolean isProjectTheme = mConfigComposite.isProjectTheme();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java
//Synthetic comment -- index 6432aa4..b574eae 100644

//Synthetic comment -- @@ -17,6 +17,7 @@

import com.android.ide.common.api.Rect;

import org.eclipse.swt.graphics.Rectangle;

import java.awt.AlphaComposite;
//Synthetic comment -- @@ -61,6 +62,48 @@
return false;
}


/**
* Crops blank pixels from the edges of the image and returns the cropped result. We








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index b73bff8..fb20d7b 100755

//Synthetic comment -- @@ -95,6 +95,7 @@
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//Synthetic comment -- @@ -199,7 +200,7 @@
private static final String VALUE_NO_AUTOCLOSE = "noauto";      //$NON-NLS-1$

private final PreviewIconFactory mPreviewIconFactory = new PreviewIconFactory(this);
    private PaletteMode mPaletteMode = PaletteMode.SMALL_PREVIEW;
/** Use alphabetical sorting instead of natural order? */
private boolean mAlphabetical;
/** Use categories instead of a single large list of views? */
//Synthetic comment -- @@ -210,6 +211,7 @@
private String mCurrentTheme;
private String mCurrentDevice;
private IAndroidTarget mCurrentTarget;

/**
* Create the composite.
//Synthetic comment -- @@ -220,7 +222,6 @@
super(parent, SWT.NONE);

mEditor = editor;
        loadPaletteMode();
}

/** Reads UI mode from persistent store to preserve palette mode across IDE sessions */
//Synthetic comment -- @@ -236,6 +237,8 @@
mAlphabetical = paletteModes.contains(VALUE_ALPHABETICAL);
mCategories = !paletteModes.contains(VALUE_NO_CATEGORIES);
mAutoClose = !paletteModes.contains(VALUE_NO_AUTOCLOSE);
}
}

//Synthetic comment -- @@ -267,6 +270,7 @@
private void refreshPalette() {
IAndroidTarget oldTarget = mCurrentTarget;
mCurrentTarget = null;
mCurrentTheme = null;
mCurrentDevice = null;
reloadPalette(oldTarget);
//Synthetic comment -- @@ -328,20 +332,43 @@
ConfigurationComposite configuration = mEditor.getConfigurationComposite();
String theme = configuration.getTheme();
String device = configuration.getDevice();
        if (target == mCurrentTarget && mCurrentTheme != null && mCurrentTheme.equals(theme) &&
                mCurrentDevice != null && mCurrentDevice.equals(device)) {
return;
}
mCurrentTheme = theme;
mCurrentTarget = target;
mCurrentDevice = device;
mPreviewIconFactory.reset();

// Erase old content and recreate new
for (Control c : getChildren()) {
c.dispose();
}

if (mPaletteMode.isPreview()) {
if (mForeground != null) {
mForeground.dispose();
//Synthetic comment -- @@ -352,31 +379,32 @@
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

        List<Object> headers = Collections.emptyList();
final Map<String, List<ViewElementDescriptor>> categoryToItems;
        if (targetData != null) {
            categoryToItems = new HashMap<String, List<ViewElementDescriptor>>();
            headers = new ArrayList<Object>();
            List<Pair<String,List<ViewElementDescriptor>>> paletteEntries =
                ViewMetadataRepository.get().getPaletteEntries(targetData,
                        mAlphabetical, mCategories);
            for (Pair<String,List<ViewElementDescriptor>> pair : paletteEntries) {
                String category = pair.getFirst();
                List<ViewElementDescriptor> categoryItems = pair.getSecond();
                headers.add(category);
                categoryToItems.put(category, categoryItems);
            }
        } else {
            categoryToItems = null;
}

boolean wrap = mPaletteMode.getWrap();
//Synthetic comment -- @@ -384,11 +412,12 @@
// Pack icon-only view vertically; others stretch to fill palette region
boolean fillVertical = mPaletteMode != PaletteMode.ICON_ONLY;

        mAccordion = new AccordionControl(this, SWT.NONE, headers, fillVertical, wrap) {
@Override
protected Composite createChildContainer(Composite parent) {
Composite composite = super.createChildContainer(parent);
                if (mPaletteMode.isPreview()) {
composite.setBackground(mBackground);
}
addMenu(composite);
//Synthetic comment -- @@ -454,7 +483,9 @@
imageControl.setScale(scale);
}
imageControl.setHoverColor(getDisplay().getSystemColor(SWT.COLOR_WHITE));
                    imageControl.setBackground(mBackground);
String toolTip = desc.getUiName();
// It appears pretty much none of the descriptors have tooltips
//String descToolTip = desc.getTooltip();
//Synthetic comment -- @@ -756,10 +787,14 @@
null, childNode, InsertType.CREATE);
}

boolean hasTransparency = false;
LayoutLibrary layoutLibrary = editor.getLayoutLibrary();
            if (layoutLibrary != null) {
                hasTransparency = layoutLibrary.supports(Capability.TRANSPARENCY);
}

RenderSession session = null;
//Synthetic comment -- @@ -773,7 +808,8 @@
int renderHeight = Math.min(screenBounds.height, MAX_RENDER_HEIGHT);
LayoutLog silentLogger = new LayoutLog();
session = editor.render(model, renderWidth, renderHeight,
                    null /* explodeNodes */, hasTransparency, silentLogger, RenderingMode.NORMAL);
} catch (Throwable t) {
// Previews can fail for a variety of reasons -- let's not bug
// the user with it
//Synthetic comment -- @@ -916,11 +952,14 @@
final static int TOGGLE_CATEGORY = 1;
final static int TOGGLE_ALPHABETICAL = 2;
final static int TOGGLE_AUTO_CLOSE = 3;

ToggleViewOptionAction(String title, int action, boolean checked) {
            super(title, IAction.AS_CHECK_BOX);
mAction = action;
            setChecked(checked);
}

@Override
//Synthetic comment -- @@ -938,6 +977,10 @@
mAutoClose = !mAutoClose;
mAccordion.setAutoClose(mAutoClose);
break;
}
savePaletteMode();
}
//Synthetic comment -- @@ -955,6 +998,12 @@
for (PaletteMode mode : PaletteMode.values()) {
manager.add(new PaletteModeAction(mode));
}
manager.add(new Separator());
manager.add(new ToggleViewOptionAction("Show Categories",
ToggleViewOptionAction.TOGGLE_CATEGORY,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index d6057fc..89ad822 100644

//Synthetic comment -- @@ -20,6 +20,8 @@
import static com.android.ide.eclipse.adt.AndroidConstants.DOT_PNG;
import static com.android.ide.eclipse.adt.AndroidConstants.DOT_XML;

import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
//Synthetic comment -- @@ -96,6 +98,21 @@
}

/**
* Returns an image descriptor for the given element descriptor, or null if no image
* could be computed. The rendering parameters (SDK, theme etc) correspond to those
* stored in the associated palette.
//Synthetic comment -- @@ -163,7 +180,22 @@
* disk
*/
private boolean render() {
        LayoutEditor layoutEditor = mPalette.getEditor().getLayoutEditor();

ViewMetadataRepository repository = ViewMetadataRepository.get();
Document document = repository.getRenderingConfigDoc();
//Synthetic comment -- @@ -182,7 +214,6 @@
}
UiDocumentNode model = (UiDocumentNode) documentDescriptor.createUiNode();
model.setEditor(layoutEditor);
        GraphicalEditorPart editor = mPalette.getEditor();
model.setUnknownDescriptorProvider(editor.getModel().getUnknownDescriptorProvider());

Element documentElement = document.getDocumentElement();
//Synthetic comment -- @@ -207,8 +238,9 @@
int height = 2000;
Set<UiElementNode> expandNodes = Collections.<UiElementNode>emptySet();
RenderingMode renderingMode = RenderingMode.FULL_EXPAND;
session = editor.render(model, width, height, expandNodes,
                        false /*hasTransparency*/, logger,
renderingMode);

} catch (Throwable t) {
//Synthetic comment -- @@ -221,25 +253,29 @@
if (session.getResult().isSuccess()) {
BufferedImage image = session.getImage();
if (image != null && image.getWidth() > 0 && image.getHeight() > 0) {
                        File imageDir = getImageDir(true);

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
//Synthetic comment -- @@ -297,14 +333,64 @@
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
//Synthetic comment -- @@ -318,7 +404,7 @@
try {
int rgba = ImageUtils.getColor(value);
// Drop alpha channel
                    return new RGB((rgba & 0xFF0000) >> 16, (rgba & 0xFF00) >> 8, rgba & 0xFF);
} catch (NumberFormatException nfe) {
;
}
//Synthetic comment -- @@ -452,7 +538,7 @@
if (themeName.startsWith(themeNamePrefix)) {
themeName = themeName.substring(themeNamePrefix.length());
}
            String dirName = String.format("palette-preview-%s-%s-%s", cleanup(targetName),
cleanup(themeName), cleanup(mPalette.getCurrentDevice()));
IPath dirPath = pluginState.append(dirName);

//Synthetic comment -- @@ -481,7 +567,7 @@
mForeground = fg;
File file = new File(imageDir, PREVIEW_INFO_FILE);
String colors = String.format(
                "background=#%02x%02x%02x\nforeground=#%02x%02x%02x\\n", //$NON-NLS-1$
bg.red, bg.green, bg.blue,
fg.red, fg.green, fg.blue);
AdtPlugin.writeFile(file, colors);
//Synthetic comment -- @@ -544,12 +630,12 @@
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









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtilsTest.java
//Synthetic comment -- index f903e74..8f08fe9 100644

//Synthetic comment -- @@ -304,6 +304,23 @@
assertEquals(0xABCDEF91, ImageUtils.getColor("#ABCDEF91"));
}

public void testScaleImage() throws Exception {
BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB_PRE);
Graphics g = image.getGraphics();







