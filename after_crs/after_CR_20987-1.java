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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* The accordion control allows a series of labels with associated content that can be
//Synthetic comment -- @@ -191,9 +193,10 @@
* @param greedy if true, grow vertically as much as possible
* @param wrapChildren if true, configure the child area to be horizontally laid out
*            with wrapping
     * @param expand Set of headers to expand initially
*/
public AccordionControl(Composite parent, int style, List<?> headers,
            boolean greedy, boolean wrapChildren, Set<String> expand) {
super(parent, style);
mWrap = wrapChildren;

//Synthetic comment -- @@ -208,7 +211,7 @@

mOpen = IconFactory.getInstance().getIcon("open-folder");     //$NON-NLS-1$
mClosed = IconFactory.getInstance().getIcon("closed-folder"); //$NON-NLS-1$
        List<CLabel> expandLabels = new ArrayList<CLabel>();

for (Object header : headers) {
final CLabel label = new CLabel(this, SWT.SHADOW_OUT);
//Synthetic comment -- @@ -279,14 +282,17 @@
}

updateIcon(label);
            if (expand != null && expand.contains(label.getText())) {
                // Comparing "label.getText()" rather than "header" because we make some
                // tweaks to the label (replacing & with && etc) and in the getExpandedCategories
                // method we return the label texts
                expandLabels.add(label);
}
}

        // Expand the requested categories
        for (CLabel label : expandLabels) {
            toggle(label, false, false);
}
}

//Synthetic comment -- @@ -365,4 +371,23 @@
protected void checkSubclass() {
// Disable the check that prevents subclassing of SWT components
}

    /**
     * Returns the set of expanded categories in the palette. Note: Header labels will have
     * escaped ampersand characters with double ampersands.
     *
     * @return the set of expanded categories in the palette - never null
     */
    public Set<String> getExpandedCategories() {
        Set<String> expanded = new HashSet<String>();
        for (Control c : getChildren()) {
            if (c instanceof CLabel) {
                if (isOpen(c)) {
                    expanded.add(((CLabel) c).getText());
                }
            }
        }

        return expanded;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index bfbbf31..7bcbdcb 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.StaticRenderSession;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.DrawableParams;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
//Synthetic comment -- @@ -130,6 +131,7 @@
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//Synthetic comment -- @@ -1258,15 +1260,15 @@
* @param height the height to use for the layout, or -1 to use the height of the screen
*            associated with this editor
* @param explodeNodes a set of nodes to explode, or null for none
     * @param overrideBgColor If non-null, use the given color as a background to render over
     *        rather than the normal background requested by the theme
     * @param noDecor If true, don't draw window decorations like the system bar
* @param logger a logger where rendering errors are reported
* @param renderingMode the {@link RenderingMode} to use for rendering
* @return the resulting rendered image wrapped in an {@link RenderSession}
*/
public RenderSession render(UiDocumentNode model, int width, int height,
            Set<UiElementNode> explodeNodes, Integer overrideBgColor, boolean noDecor,
LayoutLog logger, RenderingMode renderingMode) {
if (!ensureFileValid()) {
return null;
//Synthetic comment -- @@ -1278,7 +1280,7 @@

IProject iProject = mEditedFile.getProject();
return renderWithBridge(iProject, model, layoutLib, width, height, explodeNodes,
                overrideBgColor, noDecor, logger, null /* includeWithin */, renderingMode);
}

/**
//Synthetic comment -- @@ -1513,7 +1515,7 @@
}

RenderSession session = renderWithBridge(iProject, model, layoutLib, width, height,
                explodeNodes, null /*custom bg*/, false, logger, mIncludedWithin, renderingMode);

canvas.setSession(session, explodeNodes);

//Synthetic comment -- @@ -1555,7 +1557,7 @@

private RenderSession renderWithBridge(IProject iProject, UiDocumentNode model,
LayoutLibrary layoutLib, int width, int height, Set<UiElementNode> explodeNodes,
            Integer overrideBgColor, boolean noDecor, LayoutLog logger, Reference includeWithin,
RenderingMode renderingMode) {
ResourceManager resManager = ResourceManager.getInstance();

//Synthetic comment -- @@ -1671,6 +1673,9 @@
mMinSdkVersion,
mTargetSdkVersion,
logger);
        if (noDecor) {
            params.setForceNoDecor();
        }

// FIXME make persistent and only reload when the manifest (or at least resources) chanage.
IFolderWrapper projectFolder = new IFolderWrapper(getProject());
//Synthetic comment -- @@ -1693,11 +1698,8 @@
params.setConfigScreenSize(ssq.getValue());
}

        if (overrideBgColor != null) {
            params.setOverrideBgColor(overrideBgColor.intValue());
}

// set the Image Overlay as the image factory.
//Synthetic comment -- @@ -1715,6 +1717,44 @@
}
}

    /**
     * Renders the given resource which should refer to a drawable and returns it
     * as an image
     *
     * @param resourceName the resource to be looked up and rendered
     * @param width the width of the drawable to be rendered
     * @param height the height of the drawable to be rendered
     * @return the image, or null if something went wrong
     */
    BufferedImage renderDrawableResource(String resourceName, int width, int height) {
        ResourceResolver resources = createResolver();
        LayoutLibrary layoutLibrary = getLayoutLibrary();
        IProject project = getProject();
        ResourceValue drawableResourceValue = resources.findItemInTheme(resourceName);
        Density density = mConfigComposite.getDensity();
        float xdpi = mConfigComposite.getXDpi();
        float ydpi = mConfigComposite.getYDpi();
        ResourceManager resManager = ResourceManager.getInstance();
        ProjectResources projectRes = resManager.getProjectResources(project);
        ProjectCallback projectCallback = new ProjectCallback(
                layoutLibrary.getClassLoader(), projectRes, project);
        LayoutLog silentLogger = new LayoutLog();
        DrawableParams params = new DrawableParams(drawableResourceValue, project,
                width, height,
                density, xdpi, ydpi, resources, projectCallback,
                mMinSdkVersion, mTargetSdkVersion, silentLogger);
        params.setForceNoDecor();
        Result result = layoutLibrary.renderDrawable(params);
        if (result != null && result.isSuccess()) {
            Object data = result.getData();
            if (data instanceof BufferedImage) {
                return (BufferedImage) data;
            }
        }

        return null;
    }

ResourceResolver createResolver() {
String theme = mConfigComposite.getTheme();
boolean isProjectTheme = mConfigComposite.isProjectTheme();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java
//Synthetic comment -- index 6432aa4..b574eae 100644

//Synthetic comment -- @@ -17,6 +17,7 @@

import com.android.ide.common.api.Rect;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

import java.awt.AlphaComposite;
//Synthetic comment -- @@ -61,6 +62,48 @@
return false;
}

    /**
     * Returns the perceived brightness of the given RGB integer on a scale from 0 to 255
     *
     * @param rgb the RGB triplet, 8 bits each
     * @return the perceived brightness, with 0 maximally dark and 255 maximally bright
     */
    public static int getBrightness(int rgb) {
        if ((rgb & 0xFFFFFF) != 0) {
            int r = (rgb & 0xFF0000) >> 16;
            int g = (rgb & 0x00FF00) >> 8;
            int b = (rgb & 0x0000FF);
            // See the containsDarkPixels implementation for details
            return (int) ((299L*r + 587*g + 114*b) / 1000);
        }

        return 0;
    }

    /**
     * Converts an alpha-red-green-blue integer color into an {@link RGB} color.
     * <p>
     * <b>NOTE</b> - this will drop the alpha value since {@link RGB} objects do not
     * contain transparency information.
     *
     * @param rgb the RGB integer to convert to a color description
     * @return the color description corresponding to the integer
     */
    public static RGB intToRgb(int rgb) {
        return new RGB((rgb & 0xFF0000) >>> 16, (rgb & 0xFF00) >>> 8, rgb & 0xFF);
    }

    /**
     * Converts an {@link RGB} color into a alpha-red-green-blue integer
     *
     * @param rgb the RGB color descriptor to convert
     * @param alpha the amount of alpha to add into the color integer (since the
     *            {@link RGB} objects do not contain an alpha channel)
     * @return an integer corresponding to the {@link RGB} color
     */
    public static int rgbToInt(RGB rgb, int alpha) {
        return alpha << 24 | (rgb.red << 16) | (rgb.green << 8) | rgb.blue;
    }

/**
* Crops blank pixels from the edges of the image and returns the cropped result. We








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index b73bff8..fb20d7b 100755

//Synthetic comment -- @@ -95,6 +95,7 @@
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//Synthetic comment -- @@ -199,7 +200,7 @@
private static final String VALUE_NO_AUTOCLOSE = "noauto";      //$NON-NLS-1$

private final PreviewIconFactory mPreviewIconFactory = new PreviewIconFactory(this);
    private PaletteMode mPaletteMode = null;
/** Use alphabetical sorting instead of natural order? */
private boolean mAlphabetical;
/** Use categories instead of a single large list of views? */
//Synthetic comment -- @@ -210,6 +211,7 @@
private String mCurrentTheme;
private String mCurrentDevice;
private IAndroidTarget mCurrentTarget;
    private AndroidTargetData mCurrentTargetData;

/**
* Create the composite.
//Synthetic comment -- @@ -220,7 +222,6 @@
super(parent, SWT.NONE);

mEditor = editor;
}

/** Reads UI mode from persistent store to preserve palette mode across IDE sessions */
//Synthetic comment -- @@ -236,6 +237,8 @@
mAlphabetical = paletteModes.contains(VALUE_ALPHABETICAL);
mCategories = !paletteModes.contains(VALUE_NO_CATEGORIES);
mAutoClose = !paletteModes.contains(VALUE_NO_AUTOCLOSE);
        } else {
            mPaletteMode = PaletteMode.SMALL_PREVIEW;
}
}

//Synthetic comment -- @@ -267,6 +270,7 @@
private void refreshPalette() {
IAndroidTarget oldTarget = mCurrentTarget;
mCurrentTarget = null;
        mCurrentTargetData = null;
mCurrentTheme = null;
mCurrentDevice = null;
reloadPalette(oldTarget);
//Synthetic comment -- @@ -328,20 +332,43 @@
ConfigurationComposite configuration = mEditor.getConfigurationComposite();
String theme = configuration.getTheme();
String device = configuration.getDevice();
        AndroidTargetData targetData =
            target != null ? Sdk.getCurrent().getTargetData(target) : null;
        if (target == mCurrentTarget && targetData == mCurrentTargetData
                && mCurrentTheme != null && mCurrentTheme.equals(theme)
                && mCurrentDevice != null && mCurrentDevice.equals(device)) {
return;
}
mCurrentTheme = theme;
mCurrentTarget = target;
        mCurrentTargetData = targetData;
mCurrentDevice = device;
mPreviewIconFactory.reset();

        if (targetData == null) {
            return;
        }

        Set<String> expandedCategories = null;
        if (mAccordion != null) {
            expandedCategories = mAccordion.getExpandedCategories();
            // We auto-expand all categories when showing icons-only. When returning to some
            // other mode we don't want to retain all categories open.
            if (expandedCategories.size() > 3) {
                expandedCategories = null;
            }
        }

// Erase old content and recreate new
for (Control c : getChildren()) {
c.dispose();
}

        if (mPaletteMode == null) {
            loadPaletteMode();
            assert mPaletteMode != null;
        }

if (mPaletteMode.isPreview()) {
if (mForeground != null) {
mForeground.dispose();
//Synthetic comment -- @@ -352,31 +379,32 @@
mBackground = null;
}
RGB background = mPreviewIconFactory.getBackgroundColor();
            if (background != null) {
                mBackground = new Color(getDisplay(), background);
            }
RGB foreground = mPreviewIconFactory.getForegroundColor();
if (foreground != null) {
mForeground = new Color(getDisplay(), foreground);
}
}

        List<String> headers = Collections.emptyList();
final Map<String, List<ViewElementDescriptor>> categoryToItems;
        categoryToItems = new HashMap<String, List<ViewElementDescriptor>>();
        headers = new ArrayList<String>();
        List<Pair<String,List<ViewElementDescriptor>>> paletteEntries =
            ViewMetadataRepository.get().getPaletteEntries(targetData,
                    mAlphabetical, mCategories);
        for (Pair<String,List<ViewElementDescriptor>> pair : paletteEntries) {
            String category = pair.getFirst();
            List<ViewElementDescriptor> categoryItems = pair.getSecond();
            headers.add(category);
            categoryToItems.put(category, categoryItems);
        }

        if (expandedCategories == null && headers.size() > 0) {
            // Expand the first category if we don't have a previous selection (e.g. refresh)
            expandedCategories = Collections.singleton(headers.get(0));
}

boolean wrap = mPaletteMode.getWrap();
//Synthetic comment -- @@ -384,11 +412,12 @@
// Pack icon-only view vertically; others stretch to fill palette region
boolean fillVertical = mPaletteMode != PaletteMode.ICON_ONLY;

        mAccordion = new AccordionControl(this, SWT.NONE, headers, fillVertical, wrap,
                expandedCategories) {
@Override
protected Composite createChildContainer(Composite parent) {
Composite composite = super.createChildContainer(parent);
                if (mPaletteMode.isPreview() && mBackground != null) {
composite.setBackground(mBackground);
}
addMenu(composite);
//Synthetic comment -- @@ -454,7 +483,9 @@
imageControl.setScale(scale);
}
imageControl.setHoverColor(getDisplay().getSystemColor(SWT.COLOR_WHITE));
                    if (mBackground != null) {
                        imageControl.setBackground(mBackground);
                    }
String toolTip = desc.getUiName();
// It appears pretty much none of the descriptors have tooltips
//String descToolTip = desc.getTooltip();
//Synthetic comment -- @@ -756,10 +787,14 @@
null, childNode, InsertType.CREATE);
}

            Integer overrideBgColor = null;
boolean hasTransparency = false;
LayoutLibrary layoutLibrary = editor.getLayoutLibrary();
            if (layoutLibrary != null && layoutLibrary.supports(Capability.TRANSPARENCY)) {
                // It doesn't matter what the background color is as long as the alpha
                // is 0 (fully transparent). We're using red to make it more obvious if
                // for some reason the background is painted when it shouldn't be.
                overrideBgColor = new Integer(0x00FF0000);
}

RenderSession session = null;
//Synthetic comment -- @@ -773,7 +808,8 @@
int renderHeight = Math.min(screenBounds.height, MAX_RENDER_HEIGHT);
LayoutLog silentLogger = new LayoutLog();
session = editor.render(model, renderWidth, renderHeight,
                    null /* explodeNodes */, overrideBgColor, true /*no decorations*/,
                    silentLogger, RenderingMode.NORMAL);
} catch (Throwable t) {
// Previews can fail for a variety of reasons -- let's not bug
// the user with it
//Synthetic comment -- @@ -916,11 +952,14 @@
final static int TOGGLE_CATEGORY = 1;
final static int TOGGLE_ALPHABETICAL = 2;
final static int TOGGLE_AUTO_CLOSE = 3;
        final static int REFRESH = 4;

ToggleViewOptionAction(String title, int action, boolean checked) {
            super(title, action == REFRESH ? IAction.AS_PUSH_BUTTON : IAction.AS_CHECK_BOX);
mAction = action;
            if (checked) {
                setChecked(checked);
            }
}

@Override
//Synthetic comment -- @@ -938,6 +977,10 @@
mAutoClose = !mAutoClose;
mAccordion.setAutoClose(mAutoClose);
break;
                case REFRESH:
                    mPreviewIconFactory.refresh();
                    refreshPalette();
                    break;
}
savePaletteMode();
}
//Synthetic comment -- @@ -955,6 +998,12 @@
for (PaletteMode mode : PaletteMode.values()) {
manager.add(new PaletteModeAction(mode));
}
                if (mPaletteMode.isPreview()) {
                    manager.add(new Separator());
                    manager.add(new ToggleViewOptionAction("Refresh Previews",
                            ToggleViewOptionAction.REFRESH,
                            false));
                }
manager.add(new Separator());
manager.add(new ToggleViewOptionAction("Show Categories",
ToggleViewOptionAction.TOGGLE_CATEGORY,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index d6057fc..89ad822 100644

//Synthetic comment -- @@ -20,6 +20,8 @@
import static com.android.ide.eclipse.adt.AndroidConstants.DOT_PNG;
import static com.android.ide.eclipse.adt.AndroidConstants.DOT_XML;

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
//Synthetic comment -- @@ -96,6 +98,21 @@
}

/**
     * Deletes all the persistent state for the current settings such that it will be regenerated
     */
    public void refresh() {
        File imageDir = getImageDir(false);
        if (imageDir != null && imageDir.exists()) {
            File[] files = imageDir.listFiles();
            for (File file : files) {
                file.delete();
            }
            imageDir.delete();
            reset();
        }
    }

    /**
* Returns an image descriptor for the given element descriptor, or null if no image
* could be computed. The rendering parameters (SDK, theme etc) correspond to those
* stored in the associated palette.
//Synthetic comment -- @@ -163,7 +180,22 @@
* disk
*/
private boolean render() {
        File imageDir = getImageDir(true);

        GraphicalEditorPart editor = mPalette.getEditor();
        LayoutEditor layoutEditor = editor.getLayoutEditor();
        LayoutLibrary layoutLibrary = editor.getLayoutLibrary();
        Integer overrideBgColor = null;
        if (layoutLibrary != null) {
            if (layoutLibrary.supports(Capability.TRANSPARENCY)) {
                Pair<RGB, RGB> themeColors = getColorsFromTheme();
                RGB bg = themeColors.getFirst();
                RGB fg = themeColors.getSecond();
                storeBackground(imageDir, bg, fg);

                overrideBgColor = Integer.valueOf(ImageUtils.rgbToInt(bg, 0xFF));
            }
        }

ViewMetadataRepository repository = ViewMetadataRepository.get();
Document document = repository.getRenderingConfigDoc();
//Synthetic comment -- @@ -182,7 +214,6 @@
}
UiDocumentNode model = (UiDocumentNode) documentDescriptor.createUiNode();
model.setEditor(layoutEditor);
model.setUnknownDescriptorProvider(editor.getModel().getUnknownDescriptorProvider());

Element documentElement = document.getDocumentElement();
//Synthetic comment -- @@ -207,8 +238,9 @@
int height = 2000;
Set<UiElementNode> expandNodes = Collections.<UiElementNode>emptySet();
RenderingMode renderingMode = RenderingMode.FULL_EXPAND;

session = editor.render(model, width, height, expandNodes,
                        overrideBgColor, true /*no decorations*/, logger,
renderingMode);

} catch (Throwable t) {
//Synthetic comment -- @@ -221,25 +253,29 @@
if (session.getResult().isSuccess()) {
BufferedImage image = session.getImage();
if (image != null && image.getWidth() > 0 && image.getHeight() > 0) {

                        // Fallback for older platforms where we couldn't do background rendering
                        // at the beginning of this method
if (mBackground == null) {
Pair<RGB, RGB> themeColors = getColorsFromTheme();
RGB bg = themeColors.getFirst();
RGB fg = themeColors.getSecond();

if (bg == null) {
                                // Just use a pixel from the rendering instead.
int p = image.getRGB(image.getWidth() - 1, image.getHeight() - 1);
                                // However, in this case we don't trust the foreground color
                                // even if one was found in the themes; pick one that is guaranteed
                                // to contrast with the background
                                bg = ImageUtils.intToRgb(p);
                                if (ImageUtils.getBrightness(ImageUtils.rgbToInt(bg, 255)) < 128) {
                                    fg = new RGB(255, 255, 255);
                                } else {
                                    fg = new RGB(0, 0, 0);
                                }
}
storeBackground(imageDir, bg, fg);
                            assert mBackground != null;
}

List<ViewInfo> viewInfoList = session.getRootViews();
//Synthetic comment -- @@ -297,14 +333,64 @@
if (theme != null) {
background = resolveThemeColor(resources, "windowBackground"); //$NON-NLS-1$
if (background == null) {
                background = renderDrawableResource("windowBackground"); //$NON-NLS-1$
                // This causes some harm with some themes: We'll find a color, say black,
                // that isn't actually rendered in the theme. Better to use null here,
                // which will cause the caller to pick a pixel from the observed background
                // instead.
                //if (background == null) {
                //    background = resolveThemeColor(resources, "colorBackground"); //$NON-NLS-1$
                //}
}
foreground = resolveThemeColor(resources, "textColorPrimary"); //$NON-NLS-1$
}

        // Ensure that the foreground color is suitably distinct from the background color
        if (background != null) {
            int bgRgb = ImageUtils.rgbToInt(background, 0xFF);
            int backgroundBrightness = ImageUtils.getBrightness(bgRgb);
            if (foreground == null) {
                if (backgroundBrightness < 128) {
                    foreground = new RGB(255, 255, 255);
                } else {
                    foreground = new RGB(0, 0, 0);
                }
            } else {
                int fgRgb = ImageUtils.rgbToInt(foreground, 0xFF);
                int foregroundBrightness = ImageUtils.getBrightness(fgRgb);
                if (Math.abs(backgroundBrightness - foregroundBrightness) < 64) {
                    if (backgroundBrightness < 128) {
                        foreground = new RGB(255, 255, 255);
                    } else {
                        foreground = new RGB(0, 0, 0);
                    }
                }
            }
        }

return Pair.of(background, foreground);
}

    /**
     * Renders the given resource which should refer to a drawable and returns a
     * representative color value for the drawable (such as the color in the center)
     *
     * @param resourceName the resource to be looked up and rendered
     * @return a color representing a typical color in the drawable
     */
    private RGB renderDrawableResource(String resourceName) {
        GraphicalEditorPart editor = mPalette.getEditor();
        BufferedImage image = editor.renderDrawableResource(resourceName, 100, 100);
        if (image != null) {
            // Use the middle pixel as the color since that works better for gradients;
            // solid colors work too.
            int rgb = image.getRGB(image.getWidth() / 2, image.getHeight() / 2);
            return ImageUtils.intToRgb(rgb);
        }

        return null;
    }

private static RGB resolveThemeColor(ResourceResolver resources, String resourceName) {
ResourceValue textColor = resources.findItemInTheme(resourceName);
textColor = resources.resolveResValue(textColor);
//Synthetic comment -- @@ -318,7 +404,7 @@
try {
int rgba = ImageUtils.getColor(value);
// Drop alpha channel
                    return ImageUtils.intToRgb(rgba);
} catch (NumberFormatException nfe) {
;
}
//Synthetic comment -- @@ -452,7 +538,7 @@
if (themeName.startsWith(themeNamePrefix)) {
themeName = themeName.substring(themeNamePrefix.length());
}
            String dirName = String.format("palette-preview-r10-%s-%s-%s", cleanup(targetName),
cleanup(themeName), cleanup(mPalette.getCurrentDevice()));
IPath dirPath = pluginState.append(dirName);

//Synthetic comment -- @@ -481,7 +567,7 @@
mForeground = fg;
File file = new File(imageDir, PREVIEW_INFO_FILE);
String colors = String.format(
                "background=#%02x%02x%02x\nforeground=#%02x%02x%02x\n", //$NON-NLS-1$
bg.red, bg.green, bg.blue,
fg.red, fg.green, fg.blue);
AdtPlugin.writeFile(file, colors);
//Synthetic comment -- @@ -544,12 +630,12 @@
String colorString = (String) properties.get("background"); //$NON-NLS-1$
if (colorString != null) {
int rgb = ImageUtils.getColor(colorString.trim());
                    mBackground = ImageUtils.intToRgb(rgb);
}
colorString = (String) properties.get("foreground"); //$NON-NLS-1$
if (colorString != null) {
int rgb = ImageUtils.getColor(colorString.trim());
                    mForeground = ImageUtils.intToRgb(rgb);
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtilsTest.java
//Synthetic comment -- index f903e74..8f08fe9 100644

//Synthetic comment -- @@ -304,6 +304,23 @@
assertEquals(0xABCDEF91, ImageUtils.getColor("#ABCDEF91"));
}

    public void testGetBrightness() throws Exception {
        assertEquals(96, ImageUtils.getBrightness(0x456789));
        assertEquals(198, ImageUtils.getBrightness(0xABCDEF));

        assertEquals(0, ImageUtils.getBrightness(0x0));
        assertEquals(255, ImageUtils.getBrightness(0xFFFFFF));
        assertEquals(299*255/1000, ImageUtils.getBrightness(0xFF0000));
        assertEquals(587*255/1000, ImageUtils.getBrightness(0x00FF00));
        assertEquals(114*255/1000, ImageUtils.getBrightness(0x0000FF));
    }

    public void testColorConversion() throws Exception {
        assertEquals(0, ImageUtils.rgbToInt(ImageUtils.intToRgb(0), 0));
        assertEquals(0xFFFFFFFF, ImageUtils.rgbToInt(ImageUtils.intToRgb(0xFFFFFF), 0xFF));
        assertEquals(0x12345678, ImageUtils.rgbToInt(ImageUtils.intToRgb(0x345678), 0x12));
    }

public void testScaleImage() throws Exception {
BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB_PRE);
Graphics g = image.getGraphics();







