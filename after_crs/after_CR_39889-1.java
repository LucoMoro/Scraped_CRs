/*Add support for icon generators in templates

This CL adds an <icon> element to the template xml file which
allows the template to request the icon generator to be
chained to the wizard to generate icons instead of using
hardcoded ones. The variable ${copyIcons} will be set in the
template context if for some reason the icons were not
generated (for example, because the surrounding template
infrastructure does not support icon generation.)

Example:
     <globals file="globals.xml.ftl" />
     <execute file="recipe.xml.ftl" />
+    <icons
+        type="notification"
+        name="${activityToLayout(viewClass)}"
+        background="#ff00ff"
+        foreground="#ffff00"
+        shape="square"
+        trim="true"
+        padding="5"
+    />

This CL also removes some obsolete code from the
graphic generators and wizards, and changes the default
icon used by new projects from the white circle with a
blue shape to the default packaging icon (which you can
then further customize.)

Change-Id:Ia039bf511b9939d01e16265449c1ad6c930279c2*/




//Synthetic comment -- diff --git a/assetstudio/src/com/android/assetstudiolib/GraphicGenerator.java b/assetstudio/src/com/android/assetstudiolib/GraphicGenerator.java
//Synthetic comment -- index 5d18d4d..a88618c 100644

//Synthetic comment -- @@ -77,11 +77,7 @@
/** Foreground effects styles */
public static enum Style {
/** No effects */
        SIMPLE("fore1");

/** Id, used in filenames to identify associated stencils */
public final String id;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 2e58310..47f63f6 100644

//Synthetic comment -- @@ -1059,4 +1059,30 @@
folder.create(false, false, null);
}
}

    /**
     * Creates all the directories required for the given path.
     *
     * @param wsPath the path to create all the parent directories for
     * @return true if all the parent directories were created
     */
    public static boolean createWsParentDirectory(IContainer wsPath) {
        if (wsPath.getType() == IResource.FOLDER) {
            if (wsPath.exists()) {
                return true;
            }

            IFolder folder = (IFolder) wsPath;
            try {
                if (createWsParentDirectory(wsPath.getParent())) {
                    folder.create(true /* force */, true /* local */, null /* monitor */);
                    return true;
                }
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java
//Synthetic comment -- index 132943b..457f093 100644

//Synthetic comment -- @@ -16,12 +16,14 @@

package com.android.ide.eclipse.adt.internal.assetstudio;

import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.DEFAULT_LAUNCHER_ICON;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.assetstudiolib.ActionBarIconGenerator;
import com.android.assetstudiolib.GraphicGenerator;
import com.android.assetstudiolib.GraphicGenerator.Shape;
import com.android.assetstudiolib.LauncherIconGenerator;
import com.android.assetstudiolib.MenuIconGenerator;
import com.android.assetstudiolib.NotificationIconGenerator;
//Synthetic comment -- @@ -29,6 +31,7 @@
import com.android.assetstudiolib.TextRenderUtil;
import com.android.assetstudiolib.Util;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState.SourceType;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageControl;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageUtils;
//Synthetic comment -- @@ -36,12 +39,16 @@
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.util.Pair;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
//Synthetic comment -- @@ -76,10 +83,12 @@

import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
//Synthetic comment -- @@ -93,18 +102,11 @@
* gets to configure the parameters of the asset, and see a preview.
*/
public class ConfigureAssetSetPage extends WizardPage implements SelectionListener,
        ModifyListener {
private final CreateAssetSetWizardState mValues;

private static final int PREVIEW_AREA_WIDTH = 120;

private boolean mShown;

private Composite mConfigurationArea;
//Synthetic comment -- @@ -122,9 +124,6 @@
private Button mCircleButton;
private Button mBgButton;
private Button mFgButton;
private Composite mPreviewArea;
private Button mFontButton;
private Composite mForegroundArea;
//Synthetic comment -- @@ -134,7 +133,6 @@
private Text mImagePathText;

private boolean mTimerPending;
private RGB mBgColor;
private RGB mFgColor;
private Text mText;
//Synthetic comment -- @@ -153,10 +151,9 @@
private Composite mShapeComposite;
private Label mBgColorLabel;
private Label mFgColorLabel;

private boolean mIgnore;
    private SourceType mShowingType;

/**
* Create the wizard.
//Synthetic comment -- @@ -388,32 +385,6 @@
mFgButton.setAlignment(SWT.CENTER);
mFgButton.addSelectionListener(this);

configurationScrollArea.setContent(mConfigurationArea);
configurationScrollArea.setMinSize(mConfigurationArea.computeSize(SWT.DEFAULT,
SWT.DEFAULT));
//Synthetic comment -- @@ -453,18 +424,19 @@
}

void configureAssetType(AssetType type) {
        if (mValues.sourceType != mShowingType) {
            mShowingType = mValues.sourceType;
            showGroup(type.needsForegroundScaling(), mScalingLabel, mScalingComposite);
            showGroup(type.needsShape(), mShapeLabel, mShapeComposite);
            showGroup(type.needsTheme(), mThemeLabel, mThemeComposite);
            showGroup(type.needsColors(), mBgColorLabel, mBgButton);
            showGroup(type.needsColors() && mValues.sourceType != SourceType.IMAGE,
                    mFgColorLabel, mFgButton);

            Composite parent = mScalingLabel.getParent();
            parent.pack();
            parent.layout();
        }
}

private static void showGroup(boolean show, Control control1, Control control2) {
//Synthetic comment -- @@ -601,6 +573,8 @@
String path = mValues.imagePath != null ? mValues.imagePath.getPath() : null;
if (path == null || path.length() == 0) {
error = "Select an image";
            } else if (path.equals(DEFAULT_LAUNCHER_ICON)) {
                // Silent
} else if (!(new File(path).exists())) {
error = String.format("%1$s does not exist", path);
} else {
//Synthetic comment -- @@ -676,19 +650,28 @@
if (source == mImageRadio) {
mValues.sourceType = CreateAssetSetWizardState.SourceType.IMAGE;
chooseForegroundTab((Button) source, mImageForm);
            configureAssetType(mValues.type);
} else if (source == mClipartRadio) {
mValues.sourceType = CreateAssetSetWizardState.SourceType.CLIPART;
chooseForegroundTab((Button) source, mClipartForm);
            configureAssetType(mValues.type);
} else if (source == mTextRadio) {
mValues.sourceType = CreateAssetSetWizardState.SourceType.TEXT;
updateFontLabel();
chooseForegroundTab((Button) source, mTextForm);
            configureAssetType(mValues.type);
mText.setFocus();
}

// Choose image file
if (source == mPickImageButton) {
FileDialog dialog = new FileDialog(mPickImageButton.getShell(), SWT.OPEN);

            String curLocation = mImagePathText.getText().trim();
            if (!curLocation.isEmpty()) {
                dialog.setFilterPath(curLocation);
            }

String file = dialog.open();
if (file != null) {
mValues.imagePath = new File(file);
//Synthetic comment -- @@ -717,22 +700,6 @@
setShape(mValues.shape);
}

if (source == mTrimCheckBox) {
mValues.trim = mTrimCheckBox.getSelection();
}
//Synthetic comment -- @@ -740,9 +707,11 @@
if (source == mHoloDarkRadio) {
mHoloDarkRadio.setSelection(true);
mHoloLightRadio.setSelection(false);
            mValues.holoDark = true;
} else if (source == mHoloLightRadio) {
mHoloLightRadio.setSelection(true);
mHoloDarkRadio.setSelection(false);
            mValues.holoDark = false;
}

if (source == mChooseClipart) {
//Synthetic comment -- @@ -881,6 +850,7 @@
requestUpdatePreview(updateQuickly);
}

    @SuppressWarnings("unused") // SWT constructors have side effects and are not unused
private void updateClipartPreview() {
for (Control c : mClipartPreviewPanel.getChildren()) {
c.dispose();
//Synthetic comment -- @@ -981,7 +951,8 @@
return;
}

        Map<String, Map<String, BufferedImage>> map = generateImages(mValues,
                true /*previewOnly*/, this);
for (Entry<String, Map<String, BufferedImage>> categoryEntry : map.entrySet()) {
String category = categoryEntry.getKey();
if (category.length() > 0) {
//Synthetic comment -- @@ -1007,7 +978,18 @@
mPreviewArea.layout(true);
}

    /**
     * Generate images using the given wizard state
     *
     * @param mValues the state to use
     * @param previewOnly whether we are only generating previews
     * @param page if non null, a wizard page to write error messages to
     * @return a map of image objects
     */
    public static Map<String, Map<String, BufferedImage>> generateImages(
            @NonNull CreateAssetSetWizardState mValues,
            boolean previewOnly,
            @Nullable WizardPage page) {
// Map of ids to images: Preserve insertion order (the densities)
Map<String, Map<String, BufferedImage>> categoryMap =
new LinkedHashMap<String, Map<String, BufferedImage>>();
//Synthetic comment -- @@ -1022,23 +1004,37 @@
// TODO: Only do this when the source image type is image
String path = mValues.imagePath != null ? mValues.imagePath.getPath() : "";
if (path.length() == 0) {
                    if (page != null) {
                        page.setErrorMessage("Enter a filename");
                    }
return Collections.emptyMap();
}
                if (!path.equals(DEFAULT_LAUNCHER_ICON)) {
                    File file = new File(path);
                    if (!file.isFile()) {
                        if (page != null) {
                            page.setErrorMessage(String.format("%1$s does not exist", file.getPath()));
                        }
                        return Collections.emptyMap();
                    }
}

                if (page != null) {
                    page.setErrorMessage(null);
                }
                try {
                    sourceImage = mValues.getCachedImage(path, false);
                    if (sourceImage != null) {
                        if (trim) {
                            sourceImage = ImageUtils.cropBlank(sourceImage, null, TYPE_INT_ARGB);
                        }
                        if (mValues.padding != 0) {
                            sourceImage = Util.paddedImage(sourceImage, mValues.padding);
                        }
}
                } catch (IOException ioe) {
                    if (page != null) {
                        page.setErrorMessage(ioe.getLocalizedMessage());
}
}
break;
//Synthetic comment -- @@ -1052,8 +1048,8 @@
}

if (type.needsColors()) {
                        RGB fg = mValues.foreground;
                        int color = 0xFF000000 | (fg.red << 16) | (fg.green << 8) | fg.blue;
Paint paint = new java.awt.Color(color);
sourceImage = Util.filledImage(sourceImage, paint);
}
//Synthetic comment -- @@ -1074,8 +1070,8 @@
options.font = mValues.getTextFont();
int color;
if (type.needsColors()) {
                    RGB fg = mValues.foreground;
                    color = 0xFF000000 | (fg.red << 16) | (fg.green << 8) | fg.blue;
} else {
color = 0xFFFFFFFF;
}
//Synthetic comment -- @@ -1103,16 +1099,10 @@
new LauncherIconGenerator.LauncherOptions();
launcherOptions.shape = mValues.shape;
launcherOptions.crop = mValues.crop;
                launcherOptions.style = GraphicGenerator.Style.SIMPLE;

                RGB bg = mValues.background;
                int color = (bg.red << 16) | (bg.green << 8) | bg.blue;
launcherOptions.backgroundColor = color;
// Flag which tells the generator iterator to include a web graphic
launcherOptions.isWebGraphic = !previewOnly;
//Synthetic comment -- @@ -1128,7 +1118,7 @@
generator = new ActionBarIconGenerator();
ActionBarIconGenerator.ActionBarOptions actionBarOptions =
new ActionBarIconGenerator.ActionBarOptions();
                actionBarOptions.theme = mValues.holoDark
? ActionBarIconGenerator.Theme.HOLO_DARK
: ActionBarIconGenerator.Theme.HOLO_LIGHT;

//Synthetic comment -- @@ -1163,11 +1153,68 @@
}

String baseName = mValues.outputName;
        generator.generate(null, categoryMap, mValues, options, baseName);

return categoryMap;
}

    /**
     * Generate custom icons into the project based on the asset studio wizard
     * state
     *
     * @param newProject the project to write into
     * @param values the wizard state to read configuration settings from
     * @param previewOnly whether we are only generating a preview. For example,
     *            the launcher icons won't generate a huge 512x512 web graphic
     *            in preview mode
     * @param page a wizard page to write error messages to, or null
     */
    public static void generateIcons(final IProject newProject,
            @NonNull CreateAssetSetWizardState values,
            boolean previewOnly,
            @Nullable WizardPage page) {
        // Generate the custom icons
        Map<String, Map<String, BufferedImage>> categories = generateImages(values,
                false /*previewOnly*/, page);
        for (Map<String, BufferedImage> previews : categories.values()) {
            for (Map.Entry<String, BufferedImage> entry : previews.entrySet()) {
                String relativePath = entry.getKey();
                IPath dest = new Path(relativePath);
                IFile file = newProject.getFile(dest);

                // In case template already created icons (should remove that)
                // remove them first
                if (file.exists()) {
                    try {
                        file.delete(true, new NullProgressMonitor());
                    } catch (CoreException e) {
                        AdtPlugin.log(e, null);
                    }
                }
                AdtUtils.createWsParentDirectory(file.getParent());
                BufferedImage image = entry.getValue();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                try {
                    ImageIO.write(image, "PNG", stream); //$NON-NLS-1$
                    byte[] bytes = stream.toByteArray();
                    InputStream is = new ByteArrayInputStream(bytes);
                    file.create(is, true /*force*/, null /*progress*/);
                } catch (IOException e) {
                    AdtPlugin.log(e, null);
                } catch (CoreException e) {
                    AdtPlugin.log(e, null);
                }

                try {
                    file.getParent().refreshLocal(1, new NullProgressMonitor());
                } catch (CoreException e) {
                    AdtPlugin.log(e, null);
                }
            }
        }
    }

private void updateColor(Display display, RGB color, boolean isBackground) {
// Button.setBackgroundColor does not work (at least not on OSX) so
// we instead have to use Button.setImage with an image of the given
//Synthetic comment -- @@ -1183,78 +1230,4 @@
mFgButton.setImage(image);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/CreateAssetSetWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/CreateAssetSetWizard.java
//Synthetic comment -- index 0b5fc8e..1c9f059 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.util.Pair;

import org.eclipse.core.resources.IContainer;
//Synthetic comment -- @@ -92,7 +91,7 @@
@Override
public boolean performFinish() {
Map<String, Map<String, BufferedImage>> categories =
                ConfigureAssetSetPage.generateImages(mValues, false, null);

IProject project = mValues.project;

//Synthetic comment -- @@ -142,7 +141,7 @@
}
}

                AdtUtils.createWsParentDirectory(file.getParent());
BufferedImage image = entry.getValue();

ByteArrayOutputStream stream = new ByteArrayOutputStream();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/CreateAssetSetWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/CreateAssetSetWizardState.java
//Synthetic comment -- index 571c6f2..94bc4d3 100644

//Synthetic comment -- @@ -15,22 +15,34 @@
*/
package com.android.ide.eclipse.adt.internal.assetstudio;

import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.DEFAULT_LAUNCHER_ICON;

import com.android.annotations.NonNull;
import com.android.assetstudiolib.GraphicGenerator;
import com.android.assetstudiolib.GraphicGenerator.Shape;
import com.android.assetstudiolib.GraphicGeneratorContext;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.wizards.templates.TemplateManager;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.RGB;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
* Value object for the AssetStudio wizard. These values are both set by the
* wizard as well as read by the wizard initially, so passing in a configured
* {@link CreateAssetSetWizardState} to the icon generator is possible.
*/
public class CreateAssetSetWizardState implements GraphicGeneratorContext {
/**
* The type of asset being created. This field is static such that when you
* bring up the wizard repeatedly (for example to create multiple
//Synthetic comment -- @@ -75,6 +87,9 @@
/** Whether the image should be cropped */
public boolean crop;

    /** Whether to use Holo Dark for action bar icons */
    public boolean holoDark;

/** The background color to use for the shape (unless the shape is {@link Shape#NONE} */
public RGB background = new RGB(0xff, 0x00, 0x00);

//Synthetic comment -- @@ -84,6 +99,8 @@
/** If {@link #sourceType} is a {@link SourceType#TEXT}, the font of the text to render */
private Font mTextFont;

    private Map<String, BufferedImage> mImageCache = null;

/**
* Gets the text font to be used for text rendering if the
* {@link #sourceType} is a {@link SourceType#TEXT}
//Synthetic comment -- @@ -139,4 +156,103 @@
/** Generate the icon using the text in {@link #text} */
TEXT
}

    // ---- Implements GraphicGeneratorContext ----

    @Override
    public BufferedImage loadImageResource(String relativeName) {
        try {
            return getCachedImage(relativeName, true);
        } catch (IOException e) {
            AdtPlugin.log(e, null);
            return null;
        }
    }

    BufferedImage getCachedImage(String path, boolean isPluginRelative)
            throws IOException {
        BufferedImage image = mImageCache != null ? mImageCache.get(path) : null;
        if (image == null) {
            image = getImage(path, isPluginRelative);
            if (mImageCache == null) {
                mImageCache = new HashMap<String, BufferedImage>();
            }
            mImageCache.put(path, image);
        }

        return image;
    }

    @NonNull
    static BufferedImage getImage(@NonNull String path, boolean isPluginRelative)
            throws IOException {
        BufferedImage image = null;
        if (isPluginRelative) {
            image = GraphicGenerator.getStencilImage(path);
        } else {
            if (path.equals(DEFAULT_LAUNCHER_ICON)) {
                File file = TemplateManager.getTemplateLocation(
                  "projects/NewAndroidApplication/root/res/drawable-xhdpi/ic_launcher.png"); //$NON-NLS-1$
                if (file != null) {
                    path = file.getPath();
                } else {
                    image = GraphicGenerator.getStencilImage("user.png");
                }
            }

            File file = new File(path);

            // Requires Batik
            //if (file.getName().endsWith(DOT_SVG)) {
            //    image = loadSvgImage(file);
            //}

            if (image == null) {
                image = ImageIO.read(file);
            }
        }

        if (image == null) {
            image = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
        }

        return image;
    }

    // This requires Batik for SVG rendering
    //
    //public static BufferedImage loadSvgImage(File file) {
    //    BufferedImageTranscoder transcoder = new BufferedImageTranscoder();
    //
    //    String svgURI = file.toURI().toString();
    //    TranscoderInput input = new TranscoderInput(svgURI);
    //
    //    try {
    //        transcoder.transcode(input, null);
    //    } catch (TranscoderException e) {
    //        e.printStackTrace();
    //        return null;
    //    }
    //
    //    return transcoder.decodedImage;
    //}
    //
    ///**
    // * A dummy implementation of an {@link ImageTranscoder} that simply stores the {@link
    // * BufferedImage} generated by the SVG library.
    // */
    //private static class BufferedImageTranscoder extends ImageTranscoder {
    //    public BufferedImage decodedImage;
    //
    //    @Override
    //    public BufferedImage createImage(int w, int h) {
    //        return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    //    }
    //
    //    @Override
    //    public void writeImage(BufferedImage image, TranscoderOutput output)
    //            throws TranscoderException {
    //        this.decodedImage = image;
    //    }
    //}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java
//Synthetic comment -- index 04d149a..91ecaf6 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlFormatPreferences;
//Synthetic comment -- @@ -36,9 +37,7 @@
import com.android.resources.ResourceFolderType;
import com.android.util.Pair;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
//Synthetic comment -- @@ -201,7 +200,7 @@
}
need_delete = true;
} else {
            AdtUtils.createWsParentDirectory(file.getParent());
}

StringBuilder sb = new StringBuilder(XML_HEADER_LINE);
//Synthetic comment -- @@ -320,32 +319,6 @@
}

/**
* Returns an image descriptor for the wizard logo.
*/
private void setImageDescriptor() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewActivityWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewActivityWizard.java
//Synthetic comment -- index 85afe77..101027a 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ui.IWorkbench;

//Synthetic comment -- @@ -53,6 +54,11 @@
}

@Override
    protected boolean shouldAddIconPage() {
        return mActivityValues.getIconState() != null;
    }

    @Override
public void init(IWorkbench workbench, IStructuredSelection selection) {
super.init(workbench, selection);

//Synthetic comment -- @@ -89,7 +95,12 @@
addPage(mTemplatePage);
}
return mTemplatePage;
        } else if (page == mTemplatePage && shouldAddIconPage()) {
            WizardPage iconPage = getIconPage(mActivityValues.getIconState());
            mActivityValues.updateIconState(mTemplatePage.getEvaluator());
            return iconPage;
        } else if (page == mTemplatePage
                || shouldAddIconPage() && page == getIconPage(mActivityValues.getIconState())) {
TemplateMetadata template = mActivityValues.getTemplateHandler().getTemplate();
if (template != null) {
if (InstallDependencyPage.isInstalled(template.getDependencies())) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java
//Synthetic comment -- index 824c542..54432bb 100644

//Synthetic comment -- @@ -27,45 +27,35 @@
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectCreator;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectCreator.ProjectPopulator;
import com.android.sdklib.SdkConstants;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IWorkbench;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* Wizard for creating new projects
*/
public class NewProjectWizard extends TemplateWizard {
    static final String ATTR_COPY_ICONS = "copyIcons";             //$NON-NLS-1$
static final String ATTR_TARGET_API = "targetApi";             //$NON-NLS-1$
static final String ATTR_MIN_API = "minApi";                   //$NON-NLS-1$
static final String ATTR_MIN_BUILD_API = "minBuildApi";        //$NON-NLS-1$
//Synthetic comment -- @@ -77,11 +67,16 @@
static final String CATEGORY_PROJECTS = "projects";            //$NON-NLS-1$
static final String CATEGORY_ACTIVITIES = "activities";        //$NON-NLS-1$
static final String CATEGORY_OTHER = "other";                  //$NON-NLS-1$
    /**
     * Reserved file name for the launcher icon, resolves to the xhdpi version
     *
     * @see CreateAssetSetWizardState#getImage
     */
    public static final String DEFAULT_LAUNCHER_ICON = "launcher_icon";   //$NON-NLS-1$

private NewProjectPage mMainPage;
private ActivityPage mActivityPage;
private NewTemplatePage mTemplatePage;
private NewProjectWizardState mValues;
/** The project being created */
private IProject mProject;
//Synthetic comment -- @@ -108,23 +103,29 @@
public IWizardPage getNextPage(IWizardPage page) {
if (page == mMainPage) {
if (mValues.createIcon) {
                // Bundle asset studio wizard to create the launcher icon
                CreateAssetSetWizardState iconState = mValues.iconState;
                iconState.type = AssetType.LAUNCHER;
                iconState.outputName = "ic_launcher"; //$NON-NLS-1$
                iconState.background = new RGB(0xff, 0xff, 0xff);
                iconState.foreground = new RGB(0x33, 0xb6, 0xea);
                iconState.trim = true;

                // ADT 20: White icon with blue shape
                //iconState.shape = GraphicGenerator.Shape.CIRCLE;
                //iconState.sourceType = CreateAssetSetWizardState.SourceType.CLIPART;
                //iconState.clipartName = "user.png"; //$NON-NLS-1$
                //iconState.padding = 10;

                // ADT 21: Use the platform packaging icon, but allow user to customize it
                iconState.sourceType = CreateAssetSetWizardState.SourceType.IMAGE;
                iconState.imagePath = new File(DEFAULT_LAUNCHER_ICON);
                iconState.shape = GraphicGenerator.Shape.NONE;
                iconState.padding = 0;

                WizardPage p = getIconPage(mValues.iconState);
                p.setTitle("Configure Launcher Icon");
                return p;
} else {
return mActivityPage;
}
//Synthetic comment -- @@ -334,45 +335,7 @@
private void generateIcons(final IProject newProject) {
// Generate the custom icons
assert mValues.createIcon;
        ConfigureAssetSetPage.generateIcons(newProject, mValues.iconState, false, mIconPage);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java
//Synthetic comment -- index 498d42a..2d5e095 100644

//Synthetic comment -- @@ -27,7 +27,6 @@
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageControl;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectChooserHelper;
//Synthetic comment -- @@ -84,7 +83,6 @@
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
//Synthetic comment -- @@ -114,7 +112,6 @@
private ImageControl mPreview;
private Image mPreviewImage;
private ProjectCombo mProjectButton;
private StringEvaluator mEvaluator;

private TemplateMetadata mShowingTemplate;
//Synthetic comment -- @@ -230,7 +227,6 @@
}

List<Parameter> parameters = template.getParameters();
for (Parameter parameter : parameters) {
Parameter.Type type = parameter.type;

//Synthetic comment -- @@ -242,7 +238,6 @@

String id = parameter.id;
assert id != null && !id.isEmpty() : ATTR_ID;
String value = defaults.get(id);
if (value == null) {
value = parameter.initial;
//Synthetic comment -- @@ -353,7 +348,7 @@
combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
2, 1));

                        List<Element> options = parameter.getOptions();
assert options.size() > 0;
int selected = 0;
List<String> ids = Lists.newArrayList();
//Synthetic comment -- @@ -552,6 +547,16 @@
return (Parameter) control.getData();
}

    /**
     * Returns the current string evaluator, if any
     *
     * @return the evaluator or null
     */
    @Nullable
    public StringEvaluator getEvaluator() {
        return mEvaluator;
    }

// ---- Validation ----

private void validatePage() {
//Synthetic comment -- @@ -566,7 +571,10 @@
}
}

        for (Parameter parameter : mShowingTemplate.getParameters()) {
            if (parameter.type == Parameter.Type.SEPARATOR) {
                continue;
            }
IInputValidator validator = parameter.getValidator(mValues.project);
if (validator != null) {
ControlDecoration decoration = mDecorations.get(parameter.id);
//Synthetic comment -- @@ -807,8 +815,10 @@
mValues.parameters.put(id, value);

// Update dependent variables, if any
            List<Parameter> parameters = mShowingTemplate.getParameters();
            for (Parameter p : parameters) {
                if (p == parameter || p.suggest == null || p.edited ||
                        p.type == Parameter.Type.SEPARATOR) {
continue;
}
p.suggest.indexOf(id);
//Synthetic comment -- @@ -820,7 +830,7 @@
if (mEvaluator == null) {
mEvaluator = new StringEvaluator();
}
                    String updated = mEvaluator.evaluate(p.suggest, parameters);
if (updated != null && !updated.equals(p.value)) {
setValue(p, updated);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplateWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplateWizard.java
//Synthetic comment -- index 8c0a809..098beea 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
//Synthetic comment -- @@ -76,6 +77,11 @@
mMainPage = new NewTemplatePage(mValues, true);
}

    @Override
    protected boolean shouldAddIconPage() {
        return mValues.getIconState() != null;
    }

/**
* Hide those parameters that the template requires but that we don't want
* to ask the users about, since we can derive it from the target project
//Synthetic comment -- @@ -99,7 +105,13 @@
@Override
public IWizardPage getNextPage(IWizardPage page) {
TemplateMetadata template = mValues.getTemplateHandler().getTemplate();

        if (page == mMainPage && shouldAddIconPage()) {
            WizardPage iconPage = getIconPage(mValues.getIconState());
            mValues.updateIconState(mMainPage.getEvaluator());
            return iconPage;
        } else if (page == mMainPage
                || shouldAddIconPage() && page == getIconPage(mValues.getIconState())) {
if (template != null) {
if (InstallDependencyPage.isInstalled(template.getDependencies())) {
return getPreviewPage(mValues);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplateWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplateWizardState.java
//Synthetic comment -- index 9986810..5deae30 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.wizards.templates;

import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.ATTR_BUILD_API;
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.ATTR_COPY_ICONS;
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.ATTR_MIN_API;
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.ATTR_MIN_API_LEVEL;
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.ATTR_PACKAGE_NAME;
//Synthetic comment -- @@ -23,10 +25,16 @@
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewTemplateWizard.BLANK_ACTIVITY;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.internal.assetstudio.ConfigureAssetSetPage;
import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.NullChange;

import java.io.File;
import java.util.Collections;
//Synthetic comment -- @@ -63,14 +71,16 @@
/** The minimum API level to use for this template */
public int minSdkLevel;

/** Location of the template being created */
private File mTemplateLocation;

/**
     * State for the asset studio wizard, used to create custom icons provided
     * the icon requests it with an {@code <icons>} element
     */
    private CreateAssetSetWizardState mIconState;

    /**
* Create a new state object for use by the {@link NewTemplatePage}
*/
public NewTemplateWizardState() {
//Synthetic comment -- @@ -135,8 +145,56 @@
parameters.put(ATTR_MIN_API, manifest.getMinSdkVersion());
parameters.put(ATTR_MIN_API_LEVEL, manifest.getMinSdkName());
parameters.put(ATTR_TARGET_API, manifest.getTargetSdkVersion());
        parameters.put(ATTR_BUILD_API, getBuildApi());
        parameters.put(ATTR_COPY_ICONS, mIconState == null);

        List<Change> changes = getTemplateHandler().render(project, parameters);

        if (mIconState != null) {
            String title = String.format("Generate icons (res/drawable-<density>/%1$s.png)",
                    mIconState.outputName);
            changes.add(new NullChange(title) {
                @Override
                public Change perform(IProgressMonitor pm) throws CoreException {
                    ConfigureAssetSetPage.generateIcons(mIconState.project,
                            mIconState, false, null);

                    // Not undoable: just return null instead of an undo-change.
                    return null;
                }
            });

        }

        return changes;
    }

    @NonNull
    CreateAssetSetWizardState getIconState() {
        if (mIconState == null) {
            TemplateHandler handler = getTemplateHandler();
            if (handler != null) {
                TemplateMetadata template = handler.getTemplate();
                if (template != null) {
                    mIconState = template.getIconState(project);
                }
            }
        }

        return mIconState;
    }

    /**
     * Updates the icon state, such as the output name, based on other parameter settings
     * @param evaluator the string evaluator, or null if none exists
     */
    public void updateIconState(@Nullable StringEvaluator evaluator) {
        TemplateMetadata template = getTemplateHandler().getTemplate();
        if (template != null) {
            if (evaluator == null) {
                evaluator = new StringEvaluator();
            }
            template.updateIconName(template.getParameters(), evaluator);
        }
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/Parameter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/Parameter.java
//Synthetic comment -- index e7429dd..9c31033 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.resources.ResourceNameValidator;
import com.android.ide.eclipse.adt.internal.wizards.newproject.ApplicationInfoPage;
import com.android.resources.ResourceFolderType;
//Synthetic comment -- @@ -38,7 +39,9 @@
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.Element;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

/**
//Synthetic comment -- @@ -237,6 +240,26 @@
value = initial;
}

    Parameter(@NonNull Type type, @NonNull String id, @NonNull String initialValue) {
        this.type = type;
        this.id = id;
        this.value = initialValue;
        element = null;
        initial = null;
        suggest = null;
        name = id;
        help = null;
        constraints = EnumSet.noneOf(Constraint.class);
    }

    List<Element> getOptions() {
        if (element != null) {
            return DomUtilities.getChildren(element);
        } else {
            return Collections.emptyList();
        }
    }

@Nullable
public IInputValidator getValidator(@Nullable IProject project) {
if (mNoValidator) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java
//Synthetic comment -- index bdbe50d..1c3b862 100644

//Synthetic comment -- @@ -137,6 +137,7 @@
static final String TAG_THUMB = "thumb";             //$NON-NLS-1$
static final String TAG_THUMBS = "thumbs";           //$NON-NLS-1$
static final String TAG_DEPENDENCY = "dependency";   //$NON-NLS-1$
    static final String TAG_ICONS = "icons";             //$NON-NLS-1$
static final String ATTR_FORMAT = "format";          //$NON-NLS-1$
static final String ATTR_REVISION = "revision";      //$NON-NLS-1$
static final String ATTR_VALUE = "value";            //$NON-NLS-1$
//Synthetic comment -- @@ -151,6 +152,14 @@
static final String ATTR_TO = "to";                  //$NON-NLS-1$
static final String ATTR_FROM = "from";              //$NON-NLS-1$
static final String ATTR_CONSTRAINTS = "constraints";//$NON-NLS-1$
    static final String ATTR_BACKGROUND = "background";  //$NON-NLS-1$
    static final String ATTR_FOREGROUND = "foreground";  //$NON-NLS-1$
    static final String ATTR_SHAPE = "shape";            //$NON-NLS-1$
    static final String ATTR_TRIM = "trim";              //$NON-NLS-1$
    static final String ATTR_PADDING = "padding";        //$NON-NLS-1$
    static final String ATTR_SOURCE_TYPE = "source";     //$NON-NLS-1$
    static final String ATTR_CLIPART_NAME = "clipartName";//$NON-NLS-1$
    static final String ATTR_TEXT = "text";              //$NON-NLS-1$

static final String CATEGORY_ACTIVITIES = "activities";//$NON-NLS-1$
static final String CATEGORY_PROJECTS = "projects";    //$NON-NLS-1$
//Synthetic comment -- @@ -423,7 +432,7 @@
}
} else if (!name.equals("template") && !name.equals("category")
&& !name.equals("option") && !name.equals(TAG_THUMBS) &&
                            !name.equals(TAG_THUMB) && !name.equals(TAG_ICONS)) {
System.err.println("WARNING: Unknown template directive " + name);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateManager.java
//Synthetic comment -- index 4785835..c183ce3 100644

//Synthetic comment -- @@ -40,10 +40,11 @@
import java.util.Map;

/** Handles locating templates and providing template metadata */
public class TemplateManager {
TemplateManager() {
}

    /** @return the root folder containing templates */
@Nullable
public static File getTemplateRootFolder() {
String location = AdtPrefs.getPrefs().getOsSdkFolder();
//Synthetic comment -- @@ -57,6 +58,7 @@
return null;
}

    /** @return the root folder containing extra templates */
@Nullable
public static File getExtraTemplateRootFolder() {
String location = AdtPrefs.getPrefs().getOsSdkFolder();
//Synthetic comment -- @@ -70,6 +72,13 @@
return null;
}

    /**
     * Returns a template file under the given root, if it exists
     *
     * @param root the root folder
     * @param relativePath the relative path
     * @return a template file under the given root, if it exists
     */
@Nullable
public static File getTemplateLocation(@NonNull File root, @NonNull String relativePath) {
File templateRoot = getTemplateRootFolder();
//Synthetic comment -- @@ -86,6 +95,12 @@
return null;
}

    /**
     * Returns a template file under one of the available roots, if it exists
     *
     * @param relativePath the relative path
     * @return a template file under one of the available roots, if it exists
     */
@Nullable
public static File getTemplateLocation(@NonNull String relativePath) {
File templateRoot = getTemplateRootFolder();
//Synthetic comment -- @@ -188,7 +203,7 @@
private Map<File, TemplateMetadata> mTemplateMap;

@Nullable
    TemplateMetadata getTemplate(File templateDir) {
if (mTemplateMap != null) {
TemplateMetadata metadata = mTemplateMap.get(templateDir);
if (metadata != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateMetadata.java
//Synthetic comment -- index 2e78093..699f7cc 100644

//Synthetic comment -- @@ -18,17 +18,36 @@
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.ATTR_MIN_API;
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.ATTR_MIN_BUILD_API;
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.ATTR_REVISION;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_BACKGROUND;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_CLIPART_NAME;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_DESCRIPTION;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_FOREGROUND;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_FORMAT;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_NAME;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_PADDING;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_SHAPE;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_SOURCE_TYPE;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_TEXT;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_TRIM;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_TYPE;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.CURRENT_FORMAT;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.TAG_DEPENDENCY;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.TAG_ICONS;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.TAG_PARAMETER;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.TAG_THUMB;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.assetstudiolib.GraphicGenerator;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.assetstudio.AssetType;
import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState;
import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState.SourceType;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageUtils;
import com.android.util.Pair;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.RGB;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
//Synthetic comment -- @@ -40,6 +59,7 @@
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lombok.ast.libs.org.parboiled.google.collect.Lists;
//Synthetic comment -- @@ -53,6 +73,8 @@
private Integer mMinApi;
private Integer mMinBuildApi;
private Integer mRevision;
    private boolean mNoIcons;
    private CreateAssetSetWizardState mIconState;

TemplateMetadata(@NonNull Document document) {
mDocument = document;
//Synthetic comment -- @@ -75,7 +97,7 @@
if (versionString != null && !versionString.isEmpty()) {
try {
int version = Integer.parseInt(versionString);
                return version <= CURRENT_FORMAT;
} catch (NumberFormatException nufe) {
return false;
}
//Synthetic comment -- @@ -158,6 +180,140 @@
return mRevision.intValue();
}

    /**
     * Returns a suitable icon wizard state instance if this wizard requests
     * icons to be created, and null otherwise
     *
     * @return icon wizard state or null
     */
    @Nullable
    public CreateAssetSetWizardState getIconState(IProject project) {
        if (mIconState == null && !mNoIcons) {
            NodeList icons = mDocument.getElementsByTagName(TAG_ICONS);
            if (icons.getLength() < 1) {
                mNoIcons = true;
                return null;
            }
            Element icon = (Element) icons.item(0);

            mIconState = new CreateAssetSetWizardState();
            mIconState.project = project;

            String typeString = getAttributeOrNull(icon, ATTR_TYPE);
            if (typeString != null) {
                typeString = typeString.toUpperCase(Locale.US);
                boolean found = false;
                for (AssetType type : AssetType.values()) {
                    if (typeString.equals(type.name())) {
                        mIconState.type = type;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    AdtPlugin.log(null, "Unknown asset type %1$s", typeString);
                }
            }

            mIconState.outputName = getAttributeOrNull(icon, ATTR_NAME);
            if (mIconState.outputName != null) {
                // Register parameter such that if it is referencing other values, it gets
                // updated when other values are edited
                Parameter outputParameter = new Parameter(
                        Parameter.Type.STRING, "_iconname", mIconState.outputName); //$NON-NLS-1$
                getParameters().add(outputParameter);
            }

            RGB background = getRgb(icon, ATTR_BACKGROUND);
            if (background != null) {
                mIconState.background = background;
            }
            RGB foreground = getRgb(icon, ATTR_FOREGROUND);
            if (foreground != null) {
                mIconState.foreground = foreground;
            }
            String shapeString = getAttributeOrNull(icon, ATTR_SHAPE);
            if (shapeString != null) {
                shapeString = shapeString.toUpperCase(Locale.US);
                boolean found = false;
                for (GraphicGenerator.Shape shape : GraphicGenerator.Shape.values()) {
                    if (shapeString.equals(shape.name())) {
                        mIconState.shape = shape;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    AdtPlugin.log(null, "Unknown shape %1$s", shapeString);
                }
            }
            String trimString = getAttributeOrNull(icon, ATTR_TRIM);
            if (trimString != null) {
                mIconState.trim = Boolean.valueOf(trimString);
            }
            String paddingString = getAttributeOrNull(icon, ATTR_PADDING);
            if (paddingString != null) {
                mIconState.padding = Integer.parseInt(paddingString);
            }
            String sourceTypeString = getAttributeOrNull(icon, ATTR_SOURCE_TYPE);
            if (sourceTypeString != null) {
                sourceTypeString = sourceTypeString.toUpperCase(Locale.US);
                boolean found = false;
                for (SourceType type : SourceType.values()) {
                    if (sourceTypeString.equals(type.name())) {
                        mIconState.sourceType = type;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    AdtPlugin.log(null, "Unknown source type %1$s", sourceTypeString);
                }
            }
            mIconState.clipartName = getAttributeOrNull(icon, ATTR_CLIPART_NAME);

            String textString = getAttributeOrNull(icon, ATTR_TEXT);
            if (textString != null) {
                mIconState.text = textString;
            }
        }

        return mIconState;
    }

    void updateIconName(List<Parameter> parameters, StringEvaluator evaluator) {
        if (mIconState != null) {
            NodeList icons = mDocument.getElementsByTagName(TAG_ICONS);
            if (icons.getLength() < 1) {
                return;
            }
            Element icon = (Element) icons.item(0);
            String name = getAttributeOrNull(icon, ATTR_NAME);
            if (name != null) {
                mIconState.outputName = evaluator.evaluate(name, parameters);
            }
        }
    }

    private static RGB getRgb(@NonNull Element element, @NonNull String name) {
        String colorString = getAttributeOrNull(element, name);
        if (colorString != null) {
            int rgb = ImageUtils.getColor(colorString.trim());
            return ImageUtils.intToRgb(rgb);
        }

        return null;
    }

    @Nullable
    private static String getAttributeOrNull(@NonNull Element element, @NonNull String name) {
        String value = element.getAttribute(name);
        if (value != null && value.isEmpty()) {
            return null;
        }
        return value;
    }

@Nullable
String getThumbnailPath() {
// Apply selector logic. Pick the thumb first thumb that satisfies the largest number
//Synthetic comment -- @@ -223,7 +379,7 @@
*/
List<Pair<String, Integer>> getDependencies() {
if (mDependencies == null) {
            NodeList elements = mDocument.getElementsByTagName(TAG_DEPENDENCY);
if (elements.getLength() == 0) {
return Collections.emptyList();
}
//Synthetic comment -- @@ -231,9 +387,9 @@
List<Pair<String, Integer>> dependencies = Lists.newArrayList();
for (int i = 0, n = elements.getLength(); i < n; i++) {
Element element = (Element) elements.item(i);
                String name = element.getAttribute(ATTR_NAME);
int revision = -1;
                String revisionString = element.getAttribute(ATTR_REVISION);
if (!revisionString.isEmpty()) {
revision = Integer.parseInt(revisionString);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateWizard.java
//Synthetic comment -- index a062665..ca41a6f 100644

//Synthetic comment -- @@ -19,6 +19,8 @@

import com.android.annotations.NonNull;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.assetstudio.ConfigureAssetSetPage;
import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;

import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -46,10 +48,16 @@
private UpdateToolsPage mUpdatePage;
private InstallDependencyPage mDependencyPage;
private TemplatePreviewPage mPreviewPage;
    protected ConfigureAssetSetPage mIconPage;

protected TemplateWizard() {
}

    /** Should this wizard add an icon page? */
    protected boolean shouldAddIconPage() {
        return false;
    }

@Override
public void init(IWorkbench workbench, IStructuredSelection selection) {
mWorkbench = workbench;
//Synthetic comment -- @@ -91,6 +99,16 @@
return mPreviewPage;
}

    protected WizardPage getIconPage(CreateAssetSetWizardState iconState) {
        if (mIconPage == null) {
            mIconPage = new ConfigureAssetSetPage(iconState);
            mIconPage.setTitle("Configure Icon");
            addPage(mIconPage);
        }

        return mIconPage;
    }

protected WizardPage getDependencyPage(TemplateMetadata template, boolean create) {
if (!create) {
return mDependencyPage;







