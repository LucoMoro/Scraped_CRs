/*Misc improvements to the multi-configuration editing

* Fix bug in switching to preview configurations
* Don't draw drop shadows for thumbnails in Dialog themes
* Move the preview title labels to sit above each preview
  thumbnail
* Make error thumbnails include rendering error messages
  (and wrap if necessary), plus tweak appearance
* Make switch animation show rectangles animating in
  both directions

Change-Id:I0995617fa277b48419a88c5203abf5b1d49af711*/




//Synthetic comment -- diff --git a/common/src/com/android/utils/SdkUtils.java b/common/src/com/android/utils/SdkUtils.java
//Synthetic comment -- index 18d3ccd..160f95d 100644

//Synthetic comment -- @@ -17,7 +17,9 @@
package com.android.utils;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

/** Miscellaneous utilities used by the Android SDK tools */
public class SdkUtils {
/**
* Returns true if the given string ends with the given suffix, using a
//Synthetic comment -- @@ -111,7 +113,12 @@
return sb.toString();
}

    /**
     * Returns true if the given string has an upper case character.
     *
     * @param s the string to check
     * @return true if it contains uppercase characters
     */
public static boolean hasUpperCaseCharacter(String s) {
for (int i = 0; i < s.length(); i++) {
if (Character.isUpperCase(s.charAt(i))) {
//Synthetic comment -- @@ -144,4 +151,66 @@

return sLineSeparator;
}

    /**
     * Wraps the given text at the given line width, with an optional hanging
     * indent.
     *
     * @param text the text to be wrapped
     * @param lineWidth the number of characters to wrap the text to
     * @param hangingIndent the hanging indent (to be used for the second and
     *            subsequent lines in each paragraph, or null if not known
     * @return the string, wrapped
     */
    @NonNull
    public static String wrap(
            @NonNull String text,
            int lineWidth,
            @Nullable String hangingIndent) {
        if (hangingIndent == null) {
            hangingIndent = "";
        }
        int explanationLength = text.length();
        StringBuilder sb = new StringBuilder(explanationLength * 2);
        int index = 0;

        while (index < explanationLength) {
            int lineEnd = text.indexOf('\n', index);
            int next;

            if (lineEnd != -1 && (lineEnd - index) < lineWidth) {
                next = lineEnd + 1;
            } else {
                // Line is longer than available width; grab as much as we can
                lineEnd = Math.min(index + lineWidth, explanationLength);
                if (lineEnd - index < lineWidth) {
                    next = explanationLength;
                } else {
                    // then back up to the last space
                    int lastSpace = text.lastIndexOf(' ', lineEnd);
                    if (lastSpace > index) {
                        lineEnd = lastSpace;
                        next = lastSpace + 1;
                    } else {
                        // No space anywhere on the line: it contains something wider than
                        // can fit (like a long URL) so just hard break it
                        next = lineEnd + 1;
                    }
                }
            }

            if (sb.length() > 0) {
                sb.append(hangingIndent);
            } else {
                lineWidth -= hangingIndent.length();
            }

            sb.append(text.substring(index, lineEnd));
            sb.append('\n');
            index = next;
        }

        return sb.toString();
    }

}








//Synthetic comment -- diff --git a/common/tests/src/com/android/utils/SdkUtilsTest.java b/common/tests/src/com/android/utils/SdkUtilsTest.java
//Synthetic comment -- index 29a4d51..030e1b7 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class SdkUtilsTest extends TestCase {
public void testEndsWithIgnoreCase() {
assertTrue(SdkUtils.endsWithIgnoreCase("foo", "foo"));
//Synthetic comment -- @@ -81,4 +82,52 @@
assertEquals("foobar", SdkUtils.stripWhitespace("foo bar"));
assertEquals("foobar", SdkUtils.stripWhitespace("  foo bar  \n\t"));
}

    public void testWrap() {
        String s =
            "Hardcoding text attributes directly in layout files is bad for several reasons:\n" +
            "\n" +
            "* When creating configuration variations (for example for landscape or portrait)" +
            "you have to repeat the actual text (and keep it up to date when making changes)\n" +
            "\n" +
            "* The application cannot be translated to other languages by just adding new " +
            "translations for existing string resources.";
        String wrapped = SdkUtils.wrap(s, 70, "");
        assertEquals(
            "Hardcoding text attributes directly in layout files is bad for several\n" +
            "reasons:\n" +
            "\n" +
            "* When creating configuration variations (for example for landscape or\n" +
            "portrait)you have to repeat the actual text (and keep it up to date\n" +
            "when making changes)\n" +
            "\n" +
            "* The application cannot be translated to other languages by just\n" +
            "adding new translations for existing string resources.\n",
            wrapped);
    }

    public void testWrapPrefix() {
        String s =
            "Hardcoding text attributes directly in layout files is bad for several reasons:\n" +
            "\n" +
            "* When creating configuration variations (for example for landscape or portrait)" +
            "you have to repeat the actual text (and keep it up to date when making changes)\n" +
            "\n" +
            "* The application cannot be translated to other languages by just adding new " +
            "translations for existing string resources.";
        String wrapped = SdkUtils.wrap(s, 70, "    ");
        assertEquals(
            "Hardcoding text attributes directly in layout files is bad for several\n" +
            "    reasons:\n" +
            "    \n" +
            "    * When creating configuration variations (for example for\n" +
            "    landscape or portrait)you have to repeat the actual text (and keep\n" +
            "    it up to date when making changes)\n" +
            "    \n" +
            "    * The application cannot be translated to other languages by just\n" +
            "    adding new translations for existing string resources.\n",
            wrapped);
    }


}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ComplementingConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ComplementingConfiguration.java
//Synthetic comment -- index cce5bc9..66a29c8 100644

//Synthetic comment -- @@ -87,6 +87,30 @@
}

/**
     * Creates a new {@linkplain ComplementingConfiguration} that has the same overriding
     * attributes as the given other {@linkplain ComplementingConfiguration}.
     *
     * @param other the configuration to copy overrides from
     * @param parent the parent to tie the configuration to for inheriting values
     * @return a new configuration
     */
    @NonNull
    public static ComplementingConfiguration create(
            @NonNull ComplementingConfiguration other,
            @NonNull Configuration parent) {
        ComplementingConfiguration configuration =
                new ComplementingConfiguration(other.mConfigChooser, parent);
        configuration.mOverrideLocale = other.mOverrideLocale;
        configuration.mOverrideTarget = other.mOverrideTarget;
        configuration.mOverrideDevice = other.mOverrideDevice;
        configuration.mOverrideDeviceState = other.mOverrideDeviceState;
        configuration.mOverrideNightMode = other.mOverrideNightMode;
        configuration.mOverrideUiMode = other.mOverrideUiMode;

        return configuration;
    }

    /**
* Sets the variation version for this
* {@linkplain ComplementingConfiguration}. There might be multiple
* {@linkplain ComplementingConfiguration} instances inheriting from a








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java
//Synthetic comment -- index b9c7745..1615722 100644

//Synthetic comment -- @@ -50,6 +50,7 @@
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.State;
import com.android.utils.Pair;
import com.google.common.base.Objects;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -1063,6 +1064,9 @@

@Override
public String toString() {
        return Objects.toStringHelper(this.getClass())
                .add("display", getDisplayName())                 //$NON-NLS-1$
                .add("persistent", toPersistentString())          //$NON-NLS-1$
                .toString();
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java
//Synthetic comment -- index ce78800..592fa32 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import static com.android.SdkConstants.ANDROID_NS_NAME_PREFIX;
import static com.android.SdkConstants.ANDROID_STYLE_RESOURCE_PREFIX;
import static com.android.SdkConstants.ATTR_CONTEXT;
import static com.android.SdkConstants.PREFIX_RESOURCE_REF;
import static com.android.SdkConstants.RES_QUALIFIER_SEP;
import static com.android.SdkConstants.STYLE_RESOURCE_PREFIX;
//Synthetic comment -- @@ -1091,6 +1090,11 @@
}

private void selectConfiguration(FolderConfiguration fileConfig) {
        /* For now, don't show any text in the configuration combo, use just an
           icon. This has the advantage that the configuration contents don't
           shift around, so you can for example click back and forth between
           portrait and landscape without the icon moving under the mouse.
           If this works well, remove this whole method post ADT 21.
assert isUiThread();
try {
String current = mEditedFile.getParent().getName();
//Synthetic comment -- @@ -1107,6 +1111,7 @@
} finally {
mDisableUpdates--;
}
         */
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/NestedConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/NestedConfiguration.java
//Synthetic comment -- index 73d08f8..432abdb 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.State;
import com.google.common.base.Objects;

/**
* An {@linkplain NestedConfiguration} is a {@link Configuration} which inherits
//Synthetic comment -- @@ -37,7 +38,7 @@
* be "en", but otherwise inherit everything else.
*/
public class NestedConfiguration extends Configuration {
    protected Configuration mParent;
protected boolean mOverrideLocale;
protected boolean mOverrideTarget;
protected boolean mOverrideDevice;
//Synthetic comment -- @@ -66,6 +67,61 @@
}

/**
     * Creates a new {@linkplain NestedConfiguration} that has the same overriding
     * attributes as the given other {@linkplain NestedConfiguration}, and gets
     * its values from the given {@linkplain Configuration}.
     *
     * @param other the configuration to copy overrides from
     * @param values the configuration to copy values from
     * @param parent the parent to tie the configuration to for inheriting values
     * @return a new configuration
     */
    @NonNull
    public static NestedConfiguration create(
            @NonNull NestedConfiguration other,
            @NonNull Configuration values,
            @NonNull Configuration parent) {
        NestedConfiguration configuration =
                new NestedConfiguration(other.mConfigChooser, parent);
        configuration.mOverrideLocale = other.mOverrideLocale;
        if (configuration.mOverrideLocale) {
            configuration.setLocale(values.getLocale(), true);
        }
        configuration.mOverrideTarget = other.mOverrideTarget;
        if (configuration.mOverrideTarget) {
            configuration.setTarget(values.getTarget(), true);
        }
        configuration.mOverrideDevice = other.mOverrideDevice;
        configuration.mOverrideDeviceState = other.mOverrideDeviceState;
        if (configuration.mOverrideDevice) {
            configuration.setDevice(values.getDevice(), true);
        }
        if (configuration.mOverrideDeviceState) {
            configuration.setDeviceState(values.getDeviceState(), true);
        }

        configuration.mOverrideNightMode = other.mOverrideNightMode;
        if (configuration.mOverrideNightMode) {
            configuration.setNightMode(values.getNightMode(), true);
        }
        configuration.mOverrideUiMode = other.mOverrideUiMode;
        if (configuration.mOverrideUiMode) {
            configuration.setUiMode(values.getUiMode(), true);
        }

        return configuration;
    }

    /**
     * Sets the parent configuration that this configuration is inheriting from.
     *
     * @param parent the parent configuration
     */
    public void setParent(@NonNull Configuration parent) {
        mParent = parent;
    }

    /**
* Creates a new {@linkplain Configuration} which inherits values from the
* given parent {@linkplain Configuration}, possibly overriding some as
* well.
//Synthetic comment -- @@ -302,4 +358,28 @@
public Configuration getParent() {
return mParent;
}

    @Override
    @Nullable
    public String getActivity() {
        return mParent.getActivity();
    }

    @Override
    public void setActivity(String activity) {
        super.setActivity(activity);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this.getClass())
                .add("parent", mParent.getDisplayName())          //$NON-NLS-1$
                .add("display", getDisplayName())                 //$NON-NLS-1$
                .add("overrideLocale", mOverrideLocale)           //$NON-NLS-1$
                .add("overrideTarget", mOverrideTarget)           //$NON-NLS-1$
                .add("overrideDevice", mOverrideDevice)           //$NON-NLS-1$
                .add("overrideDeviceState", mOverrideDeviceState) //$NON-NLS-1$
                .add("persistent", toPersistentString())          //$NON-NLS-1$
                .toString();
    }
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 1813dfb..93fdb5e 100644

//Synthetic comment -- @@ -657,8 +657,6 @@
return true;
}

// Before doing the normal process, test for the following case.
// - the editor is being opened (or reset for a new input)
// - the file being opened is not the best match for any possible configuration
//Synthetic comment -- @@ -753,6 +751,8 @@

reloadPalette();

        getCanvasControl().getPreviewManager().configurationChanged(flags);

return true;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java
//Synthetic comment -- index e3a68b8..4811235 100644

//Synthetic comment -- @@ -64,6 +64,7 @@
import com.android.resources.ResourceType;
import com.android.resources.ScreenOrientation;
import com.android.sdklib.IAndroidTarget;
import com.android.utils.SdkUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
//Synthetic comment -- @@ -139,7 +140,7 @@
}

/** The configuration being previewed */
    private @NonNull Configuration mConfiguration;

/** The associated manager */
private final @NonNull RenderPreviewManager mManager;
//Synthetic comment -- @@ -154,6 +155,7 @@
private int mHeight;
private int mX;
private int mY;
    private int mTitleHeight;
private double mScale = 1.0;
private double mAspectRatio;

//Synthetic comment -- @@ -166,9 +168,13 @@
/** Whether the mouse is actively hovering over this preview */
private boolean mActive;

    /**
     * Whether this preview cannot be rendered because of a model error - such
     * as an invalid configuration, a missing resource, an error in the XML
     * markup, etc. If non null, contains the error message (or a blank string
     * if not known), and null if the render was successful.
     */
    private String mError;

/**
* Whether this preview presents a file that has been "forked" (separate,
//Synthetic comment -- @@ -211,6 +217,15 @@
}

/**
     * Sets the configuration to use for this preview
     *
     * @param configuration the new configuration
     */
    public void setConfiguration(@NonNull Configuration configuration) {
        mConfiguration = configuration;
    }

    /**
* Gets the scale being applied to the thumbnail
*
* @return the scale being applied to the thumbnail
//Synthetic comment -- @@ -545,7 +560,7 @@

Document document = DomUtilities.getDocument(mInput);
if (document == null) {
                mError = "No document";
createErrorThumbnail();
return;
}
//Synthetic comment -- @@ -574,7 +589,14 @@
}
}

        if (render.isSuccess()) {
            mError = null;
        } else {
            mError = render.getErrorMessage();
            if (mError == null) {
                mError = "";
            }
        }

if (render.getStatus() == Status.ERROR_TIMEOUT) {
// TODO: Special handling? schedule update again later
//Synthetic comment -- @@ -587,7 +609,7 @@
}
}

        if (mError != null) {
createErrorThumbnail();
}
}
//Synthetic comment -- @@ -663,18 +685,26 @@

double scale = getWidth() / (double) image.getWidth();
if (scale < 1.0) {
            ImageOverlay imageOverlay = mCanvas.getImageOverlay();
            boolean drawShadows = imageOverlay == null || imageOverlay.getShowDropShadow();
if (LARGE_SHADOWS) {
                int shadowSize = drawShadows ? SHADOW_SIZE : 0;
image = ImageUtils.scale(image, scale, scale,
                        shadowSize, shadowSize);
                if (drawShadows) {
                    ImageUtils.drawRectangleShadow(image, 0, 0,
                            image.getWidth() - shadowSize,
                            image.getHeight() - shadowSize);
                }
} else {
                int shadowSize = drawShadows ? SMALL_SHADOW_SIZE : 0;
image = ImageUtils.scale(image, scale, scale,
                        shadowSize, shadowSize);
                if (drawShadows) {
                    ImageUtils.drawSmallRectangleShadow(image, 0, 0,
                            image.getWidth() - shadowSize,
                            image.getHeight() - shadowSize);
                }
}
}

//Synthetic comment -- @@ -700,19 +730,23 @@
BufferedImage.TYPE_INT_ARGB);

Graphics2D g = image.createGraphics();
        g.setColor(new java.awt.Color(0xfffbfcc6));
g.fillRect(0, 0, width, height);

g.dispose();

        ImageOverlay imageOverlay = mCanvas.getImageOverlay();
        boolean drawShadows = imageOverlay == null || imageOverlay.getShowDropShadow();
        if (drawShadows) {
            if (LARGE_SHADOWS) {
                ImageUtils.drawRectangleShadow(image, 0, 0,
                        image.getWidth() - SHADOW_SIZE,
                        image.getHeight() - SHADOW_SIZE);
            } else {
                ImageUtils.drawSmallRectangleShadow(image, 0, 0,
                        image.getWidth() - SMALL_SHADOW_SIZE,
                        image.getHeight() - SMALL_SHADOW_SIZE);
            }
}

mThumbnail = SwtUtils.convertToSwt(mCanvas.getDisplay(), image,
//Synthetic comment -- @@ -761,7 +795,7 @@
* @return true if this preview handled (and therefore consumed) the click
*/
public boolean click(int x, int y) {
        if (y >= mTitleHeight && y < mTitleHeight + HEADER_HEIGHT) {
int left = 0;
left += CLOSE_ICON_WIDTH;
if (x <= left) {
//Synthetic comment -- @@ -829,9 +863,13 @@
* @param y the y coordinate to paint the preview at
*/
void paint(GC gc, int x, int y) {
        mTitleHeight = paintTitle(gc, x, y, true /*showFile*/);
        y += mTitleHeight;
        y += 2;

int width = getWidth();
int height = getHeight();
        if (mThumbnail != null && mError == null) {
gc.drawImage(mThumbnail, x, y);

if (mActive) {
//Synthetic comment -- @@ -841,7 +879,7 @@
gc.drawRectangle(x - 1, y - 1, width + 2, height + 2);
gc.setLineWidth(oldWidth);
}
        } else if (mError != null) {
if (mThumbnail != null) {
gc.drawImage(mThumbnail, x, y);
} else {
//Synthetic comment -- @@ -849,25 +887,29 @@
gc.drawRectangle(x, y, width, height);
}

            gc.setClipping(x, y, width, height);
Image icon = IconFactory.getInstance().getIcon("renderError"); //$NON-NLS-1$
ImageData data = icon.getImageData();
int prevAlpha = gc.getAlpha();
            int alpha = 96;
if (mThumbnail != null) {
                alpha -= 32;
}
gc.setAlpha(alpha);
gc.drawImage(icon, x + (width - data.width) / 2, y + (height - data.height) / 2);

            String msg = mError;
Density density = mConfiguration.getDensity();
if (density == Density.TV || density == Density.LOW) {
                msg = "Broken rendering library; unsupported DPI. Try using the SDK manager " +
                        "to get updated layout libraries.";
}
            int charWidth = gc.getFontMetrics().getAverageCharWidth();
            int charsPerLine = (width - 10) / charWidth;
            msg = SdkUtils.wrap(msg, charsPerLine, null);
            gc.setAlpha(255);
            gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
            gc.drawText(msg, x + 5, y + HEADER_HEIGHT, true);
gc.setAlpha(prevAlpha);
gc.setClipping((Region) null);
} else {
//Synthetic comment -- @@ -877,7 +919,7 @@
Image icon = IconFactory.getInstance().getIcon("refreshPreview"); //$NON-NLS-1$
ImageData data = icon.getImageData();
int prevAlpha = gc.getAlpha();
            gc.setAlpha(96);
gc.drawImage(icon, x + (width - data.width) / 2,
y + (height - data.height) / 2);
gc.setAlpha(prevAlpha);
//Synthetic comment -- @@ -886,12 +928,14 @@
if (mActive) {
int left = x ;
int prevAlpha = gc.getAlpha();
            gc.setAlpha(208);
Color bg = mCanvas.getDisplay().getSystemColor(SWT.COLOR_WHITE);
gc.setBackground(bg);
gc.fillRectangle(left, y, x + width - left, HEADER_HEIGHT);
gc.setAlpha(prevAlpha);

            y += 2;

// Paint icons
gc.drawImage(CLOSE_ICON, left, y);
left += CLOSE_ICON_WIDTH;
//Synthetic comment -- @@ -905,40 +949,40 @@
gc.drawImage(EDIT_ICON, left, y);
left += EDIT_ICON_WIDTH;
}
}

/**
     * Paints the preview title at the given position (and returns the required
     * height)
*
* @param gc the graphics context to paint into
* @param x the left edge of the preview rectangle
* @param y the top edge of the preview rectangle
*/
    int paintTitle(GC gc, int x, int y, boolean showFile) {
        int titleHeight = 0;

String displayName = getDisplayName();
if (displayName != null && displayName.length() > 0) {
gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));

int width = getWidth();
Point extent = gc.textExtent(displayName);
int labelLeft = Math.max(x, x + (width - extent.x) / 2);
            int labelTop = y + 1;
            Image icon = null;
Locale locale = mConfiguration.getLocale();
if (locale != null && (locale.hasLanguage() || locale.hasRegion())
&& (!(mConfiguration instanceof NestedConfiguration)
|| ((NestedConfiguration) mConfiguration).isOverridingLocale())) {
                icon = locale.getFlagImage();
}

            gc.setClipping(x, labelTop, width, 100);
            if (icon != null) {
                int flagWidth = icon.getImageData().width;
                int flagHeight = icon.getImageData().height;
                gc.drawImage(icon, labelLeft - flagWidth / 2 - 1, labelTop);
labelLeft += flagWidth / 2 + 1;
gc.drawText(displayName, labelLeft,
labelTop - (extent.y - flagHeight) / 2, true);
//Synthetic comment -- @@ -946,27 +990,38 @@
gc.drawText(displayName, labelLeft, labelTop, true);
}

            // Use font height rather than extent height since we want two adjacent
            // previews (which may have different display names and therefore end
            // up with slightly different extent heights) to have identical title
            // heights such that they are aligned identically
            titleHeight = gc.getFontMetrics().getHeight();

if (mForked && mInput != null && showFile) {
// Draw file flag, and parent folder name
labelTop += extent.y;
String fileName = mInput.getParent().getName() + File.separator + mInput.getName();
extent = gc.textExtent(fileName);
                icon = IconFactory.getInstance().getIcon("android_file"); //$NON-NLS-1$
                int flagWidth = icon.getImageData().width;
                int flagHeight = icon.getImageData().height;

labelLeft = Math.max(x, x + (width - extent.x - flagWidth - 1) / 2);

                gc.drawImage(icon, labelLeft, labelTop);

gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_GRAY));
labelLeft += flagWidth + 1;
labelTop -= (extent.y - flagHeight) / 2;
gc.drawText(fileName, labelLeft, labelTop, true);

                titleHeight += Math.max(titleHeight, icon.getImageData().height);

}

gc.setClipping((Region) null);
}

        return titleHeight;
}

/**
//Synthetic comment -- @@ -992,7 +1047,7 @@
FolderConfiguration folderConfig = mConfiguration.getFullConfig();
ScreenOrientationQualifier qualifier = folderConfig.getScreenOrientationQualifier();
ScreenOrientation orientation = qualifier == null
                ? ScreenOrientation.PORTRAIT : qualifier.getValue();
if (orientation == ScreenOrientation.LANDSCAPE
|| orientation == ScreenOrientation.SQUARE) {
orientation = ScreenOrientation.PORTRAIT;
//Synthetic comment -- @@ -1150,6 +1205,11 @@
return mDescription;
}

    @Override
    public String toString() {
        return getDisplayName() + ':' + mConfiguration;
    }

/** Sorts render previews into increasing aspect ratio order */
static Comparator<RenderPreview> INCREASING_ASPECT_RATIO = new Comparator<RenderPreview>() {
@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java
//Synthetic comment -- index c731266..80391cd 100644

//Synthetic comment -- @@ -571,7 +571,7 @@
int destHeight = vi.getScaledImgSize();

int x = destX + destWidth / 2 - preview.getWidth() / 2;
                int y = destY + destHeight;
preview.paintTitle(gc, x, y, false /*showFile*/);
}

//Synthetic comment -- @@ -1145,38 +1145,69 @@
GraphicalEditorPart editor = mCanvas.getEditorDelegate().getGraphicalEditor();
ConfigurationChooser chooser = editor.getConfigurationChooser();

        Configuration originalConfiguration = chooser.getConfiguration();

        // The new configuration is the configuration which will become the configuration
        // in the layout editor's chooser
        Configuration previewConfiguration = preview.getConfiguration();
        Configuration newConfiguration = previewConfiguration;
if (newConfiguration instanceof NestedConfiguration) {
// Should never use a complementing configuration for the main
// rendering's configuration; instead, create a new configuration
// with a snapshot of the configuration's current values
            newConfiguration = Configuration.copy(previewConfiguration);

            // Remap all the previews to be parented to this new copy instead
            // of the old one (which is no longer controlled by the chooser)
            for (RenderPreview p : mPreviews) {
                Configuration configuration = p.getConfiguration();
                if (configuration instanceof NestedConfiguration) {
                    NestedConfiguration nested = (NestedConfiguration) configuration;
                    nested.setParent(newConfiguration);
                }
            }
        }

        // Make a preview for the configuration which *was* showing in the
        // chooser up until this point:
        RenderPreview newPreview = mCanvas.getPreview();
        if (newPreview == null) {
            newPreview = RenderPreview.create(this, originalConfiguration);
        }

        // Update its configuration such that it is complementing or inheriting
        // from the new chosen configuration
        if (previewConfiguration instanceof ComplementingConfiguration) {
            originalConfiguration = ComplementingConfiguration.create(
                    (ComplementingConfiguration) previewConfiguration,
                    newConfiguration);
            newPreview.setConfiguration(originalConfiguration);
        } else if (previewConfiguration instanceof NestedConfiguration) {
            originalConfiguration = NestedConfiguration.create(
                    (NestedConfiguration) previewConfiguration,
                    originalConfiguration,
                    newConfiguration);
            newPreview.setConfiguration(originalConfiguration);
        }

        // Replace clicked preview with preview of the formerly edited main configuration
        // This doesn't work yet because the image overlay has had its image
        // replaced by the configuration previews! I should make a list of them
        //newPreview.setFullImage(mImageOverlay.getAwtImage());
        for (int i = 0, n = mPreviews.size(); i < n; i++) {
            if (preview == mPreviews.get(i)) {
                mPreviews.set(i, newPreview);
                break;
            }
        }

        // Stash the corresponding preview (not active) on the canvas so we can
        // retrieve it if clicking to some other preview later
        mCanvas.setPreview(preview);

        // Switch to the configuration from the clicked preview (though it's
        // most likely a copy, see above)
        chooser.setConfiguration(newConfiguration);
editor.recomputeLayout();

// Scroll to the top again, if necessary
//Synthetic comment -- @@ -1184,7 +1215,7 @@

mNeedLayout = mNeedZoom = true;
mCanvas.redraw();
        mAnimation = new SwapAnimation(preview, newPreview);
}

/**
//Synthetic comment -- @@ -1430,14 +1461,24 @@
private long begin;
private long end;
private static final long DURATION = 400; // ms
        private Rect initialRect1;
        private Rect targetRect1;
        private Rect initialRect2;
        private Rect targetRect2;
        private RenderPreview preview;

        SwapAnimation(RenderPreview preview1, RenderPreview preview2) {
begin = System.currentTimeMillis();
end = begin + DURATION;

            initialRect1 = new Rect(preview1.getX(), preview1.getY(),
                    preview1.getWidth(), preview1.getHeight());

            CanvasTransform hi = mCanvas.getHorizontalTransform();
            CanvasTransform vi = mCanvas.getVerticalTransform();
            initialRect2 = new Rect(hi.translate(0), vi.translate(0),
                    hi.getScaledImgSize(), vi.getScaledImgSize());
            preview = preview2;
}

void tick(GC gc) {
//Synthetic comment -- @@ -1447,21 +1488,33 @@
return;
}

CanvasTransform hi = mCanvas.getHorizontalTransform();
CanvasTransform vi = mCanvas.getVerticalTransform();
            if (targetRect1 == null) {
                targetRect1 = new Rect(hi.translate(0), vi.translate(0),
hi.getScaledImgSize(), vi.getScaledImgSize());
            }
double portion = (now - begin) / (double) DURATION;
            Rect rect1 = new Rect(
                    (int) (portion * (targetRect1.x - initialRect1.x) + initialRect1.x),
                    (int) (portion * (targetRect1.y - initialRect1.y) + initialRect1.y),
                    (int) (portion * (targetRect1.w - initialRect1.w) + initialRect1.w),
                    (int) (portion * (targetRect1.h - initialRect1.h) + initialRect1.h));

            if (targetRect2 == null) {
                targetRect2 = new Rect(preview.getX(), preview.getY(),
                        preview.getWidth(), preview.getHeight());
            }
            portion = (now - begin) / (double) DURATION;
            Rect rect2 = new Rect(
                (int) (portion * (targetRect2.x - initialRect2.x) + initialRect2.x),
                (int) (portion * (targetRect2.y - initialRect2.y) + initialRect2.y),
                (int) (portion * (targetRect2.w - initialRect2.w) + initialRect2.w),
                (int) (portion * (targetRect2.h - initialRect2.h) + initialRect2.h));

gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_GRAY));
            gc.drawRectangle(rect1.x, rect1.y, rect1.w, rect1.h);
            gc.drawRectangle(rect2.x, rect2.y, rect2.w, rect2.h);

mCanvas.getDisplay().timerExec(5, this);
}








//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/Main.java b/lint/cli/src/com/android/tools/lint/Main.java
//Synthetic comment -- index a33890d..ad086c7 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.tools.lint;

import static com.android.SdkConstants.DOT_XML;
import static com.android.tools.lint.client.api.IssueRegistry.LINT_ERROR;
import static com.android.tools.lint.client.api.IssueRegistry.PARSER_ERROR;
import static com.android.tools.lint.detector.api.LintUtils.endsWith;

import com.android.annotations.NonNull;
//Synthetic comment -- @@ -40,6 +40,7 @@
import com.android.tools.lint.detector.api.Position;
import com.android.tools.lint.detector.api.Project;
import com.android.tools.lint.detector.api.Severity;
import com.android.utils.SdkUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
//Synthetic comment -- @@ -873,47 +874,7 @@
}

static String wrap(String explanation, int lineWidth, String hangingIndent) {
        return SdkUtils.wrap(explanation, lineWidth, hangingIndent);
}

private static void printUsage(PrintStream out) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/MainTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/MainTest.java
//Synthetic comment -- index 0ea384e..4e7a029 100644

//Synthetic comment -- @@ -28,52 +28,6 @@

@SuppressWarnings("javadoc")
public class MainTest extends AbstractCheckTest {
protected String checkLint(String[] args, List<File> files) throws Exception {
PrintStream previousOut = System.out;
try {







