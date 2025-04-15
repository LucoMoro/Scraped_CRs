/*Asset Studio: Add no-background option, and support embedding

This changeset improves the asset studio wizard in two ways:

- First, it adds support for having no background shape (which the web
  version has added)

- Second, it adds a "value holder" class which holds the current (as
  well as initial) state of the various configuration parameters for
  the icon generator. This allows the same code to be used to generate
  icons programatically, and also allows reuse of the asset studio
  wizard pages in different wizards and with custom initial state.
  For example, the New Project wizard can now embed the asset studio
  page and preconfigure it to generate launcher icons.

Change-Id:I9eac396325214af8309447083ff9dcb9e59645ab*/




//Synthetic comment -- diff --git a/assetstudio/src/com/android/assetstudiolib/GraphicGenerator.java b/assetstudio/src/com/android/assetstudiolib/GraphicGenerator.java
//Synthetic comment -- index fa81392..64e02cd 100644

//Synthetic comment -- @@ -60,6 +60,8 @@
/** Shapes that can be used for icon backgrounds */
public static enum Shape {
/** Circular background */
        NONE("none"),
        /** Circular background */
CIRCLE("circle"),
/** Square background */
SQUARE("square");








//Synthetic comment -- diff --git a/assetstudio/src/com/android/assetstudiolib/LauncherIconGenerator.java b/assetstudio/src/com/android/assetstudiolib/LauncherIconGenerator.java
//Synthetic comment -- index 4e0534d..b3e327b 100644

//Synthetic comment -- @@ -42,12 +42,17 @@
density = launcherOptions.density.getResourceValue();
}
String shape = launcherOptions.shape.id;
        BufferedImage mBackImage = null;
        BufferedImage mForeImage = null;
        BufferedImage mMaskImage = null;
        if (launcherOptions.shape != Shape.NONE) {
            mBackImage = context.loadImageResource("/images/launcher_stencil/"
+ shape + "/" + density + "/back.png");
            mForeImage = context.loadImageResource("/images/launcher_stencil/"
+ shape + "/" + density + "/" + launcherOptions.style.id + ".png");
            mMaskImage = context.loadImageResource("/images/launcher_stencil/"
+ shape + "/" + density + "/mask.png");
        }

float scaleFactor = GraphicGenerator.getMdpiScaleFactor(launcherOptions.density);
if (launcherOptions.isWebGraphic) {
//Synthetic comment -- @@ -59,14 +64,18 @@

BufferedImage outImage = Util.newArgbBufferedImage(imageRect.width, imageRect.height);
Graphics2D g = (Graphics2D) outImage.getGraphics();
        if (mBackImage != null) {
            g.drawImage(mBackImage, 0, 0, null);
        }

BufferedImage tempImage = Util.newArgbBufferedImage(imageRect.width, imageRect.height);
Graphics2D g2 = (Graphics2D) tempImage.getGraphics();
        if (mMaskImage != null) {
            g2.drawImage(mMaskImage, 0, 0, null);
            g2.setComposite(AlphaComposite.SrcAtop);
            g2.setPaint(new Color(launcherOptions.backgroundColor));
            g2.fillRect(0, 0, imageRect.width, imageRect.height);
        }

if (launcherOptions.crop) {
Util.drawCenterCrop(g2, launcherOptions.sourceImage, targetRect);
//Synthetic comment -- @@ -75,7 +84,9 @@
}

g.drawImage(tempImage, 0, 0, null);
        if (mForeImage != null) {
            g.drawImage(mForeImage, 0, 0, null);
        }

g.dispose();
g2.dispose();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/AssetType.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/AssetType.java
//Synthetic comment -- index 29cb910..58dd332 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
/**
* The type of asset to create: launcher icon, menu icon, etc.
*/
public enum AssetType {
/** Launcher icon to be shown in the application list */
LAUNCHER("Launcher Icons", "ic_launcher"),                     //$NON-NLS-2$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ChooseAssetTypePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ChooseAssetTypePage.java
//Synthetic comment -- index 15b1f05..a26a22e 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import com.android.ide.eclipse.adt.internal.resources.ResourceNameValidator;
import com.android.resources.ResourceFolderType;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -41,25 +40,19 @@

/** Page for choosing the type of asset to create, as well as the target project */
public class ChooseAssetTypePage extends WizardPage implements SelectionListener, ModifyListener {
    private final CreateAssetSetWizardState mValues;
private ProjectCombo mProjectButton;
private Button mClipboardButton;
private Text mNameText;
private boolean mNameModified;
private Label mResourceName;

/**
* Create the wizard.
*/
    public ChooseAssetTypePage(CreateAssetSetWizardState values) {
super("chooseAssetTypePage");
        mValues = values;
setTitle("Choose Icon Set Type");
setDescription("Select the type of icon set to create:");
}
//Synthetic comment -- @@ -80,7 +73,7 @@
Button button = new Button(container, SWT.RADIO);
button.setData(type);
button.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
            button.setSelection(type == mValues.type);
button.setText(type.getDisplayName());
button.addSelectionListener(this);
}
//Synthetic comment -- @@ -96,7 +89,7 @@

ProjectChooserHelper helper =
new ProjectChooserHelper(getShell(), null /* filter */);
        mProjectButton = new ProjectCombo(helper, container, mValues.project);
mProjectButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
mProjectButton.addSelectionListener(this);

//Synthetic comment -- @@ -135,8 +128,10 @@
if (!mNameModified) {
// Default name suggestion, possibly as a suffix, e.g. "ic_menu_<name>"
String replace = "name";
            String suggestedName = String.format(mValues.type.getDefaultNameFormat(), replace);
mNameText.setText(suggestedName);
            mValues.outputName = suggestedName;

updateResourceLabel();
mNameModified = false;
int start = suggestedName.indexOf(replace);
//Synthetic comment -- @@ -154,24 +149,16 @@
mResourceName.setText("@drawable/" + getOutputName()); //$NON-NLS-1$
}

@Override
public boolean canFlipToNextPage() {
        return mValues.project != null;
}

@Override
public void widgetSelected(SelectionEvent e) {
Object source = e.getSource();
if (source == mProjectButton) {
            mValues.project = mProjectButton.getSelectedProject();
validatePage();
} else if (source == mClipboardButton) {
Clipboard clipboard = new Clipboard(getShell().getDisplay());
//Synthetic comment -- @@ -184,7 +171,8 @@
// User selected a different asset type to be created
Object data = ((Button) source).getData();
if (data instanceof AssetType) {
                mValues.type = (AssetType) data;
                CreateAssetSetWizardState.sLastType = mValues.type;
updateAssetType();
}
}
//Synthetic comment -- @@ -199,24 +187,21 @@
Object source = e.getSource();
if (source == mNameText) {
mNameModified = true;
            mValues.outputName = mNameText.getText().trim();
updateResourceLabel();
}

validatePage();
}

    private String getOutputName() {
return mNameText.getText().trim();
}

private void validatePage() {
String error = null;

        if (mValues.project == null) {
error = "Please select an Android project.";
} else {
String outputName = getOutputName();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java
//Synthetic comment -- index 105892a..7082246 100644

//Synthetic comment -- @@ -92,6 +92,7 @@
*/
public class ConfigureAssetSetPage extends WizardPage implements SelectionListener,
GraphicGeneratorContext, ModifyListener {
    private final CreateAssetSetWizardState mValues;

private static final int PREVIEW_AREA_WIDTH = 120;

//Synthetic comment -- @@ -102,6 +103,8 @@
*/
private static boolean SUPPORT_LAUNCHER_ICON_TYPES = false;

    private boolean mShown;

private Composite mConfigurationArea;
private Button mImageRadio;
private Button mClipartRadio;
//Synthetic comment -- @@ -112,6 +115,7 @@
private Label mPercentLabel;
private Button mCropRadio;
private Button mCenterRadio;
    private Button mNoShapeRadio;
private Button mSquareRadio;
private Button mCircleButton;
private Button mBgButton;
//Synthetic comment -- @@ -132,7 +136,6 @@
private RGB mBgColor;
private RGB mFgColor;
private Text mText;

/** Most recently set image path: preserved across wizard sessions */
private static String sImagePath;
//Synthetic comment -- @@ -151,11 +154,17 @@
private Label mEffectsLabel;
private Composite mEffectsComposite;

    private boolean mIgnore;

/**
* Create the wizard.
     *
     * @param values the wizard state
*/
    public ConfigureAssetSetPage(CreateAssetSetWizardState values) {
super("configureAssetPage");
        mValues = values;

setTitle("Configure Icon Set");
setDescription("Configure the attributes of the icon set");
}
//Synthetic comment -- @@ -261,7 +270,6 @@
textLabel.setText("Text:");

mText = new Text(mTextForm, SWT.BORDER);
mText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
mText.addModifyListener(this);

//Synthetic comment -- @@ -292,12 +300,11 @@
// This doesn't work right -- not sure why. For now just use a plain slider
// and subtract 10 from it to get the real range.
//mPaddingSlider.setValues(0, -10, 50, 0, 1, 10);
        //mPaddingSlider.setSelection(10 + 15);
mPaddingSlider.addSelectionListener(this);

mPercentLabel = new Label(mConfigurationArea, SWT.NONE);
mPercentLabel.setText("  15%"); // Enough available space for -10%
mScalingLabel = new Label(mConfigurationArea, SWT.NONE);
mScalingLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
mScalingLabel.setText("Foreground Scaling:");
//Synthetic comment -- @@ -324,10 +331,14 @@

mShapeComposite = new Composite(mConfigurationArea, SWT.NONE);
mShapeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        GridLayout gl_mShapeComposite = new GridLayout(6, false);
gl_mShapeComposite.horizontalSpacing = 0;
mShapeComposite.setLayout(gl_mShapeComposite);

        mNoShapeRadio = new Button(mShapeComposite, SWT.FLAT | SWT.TOGGLE);
        mNoShapeRadio.setText("None");
        mNoShapeRadio.addSelectionListener(this);

mSquareRadio = new Button(mShapeComposite, SWT.FLAT | SWT.TOGGLE);
mSquareRadio.setSelection(true);
mSquareRadio.setText("Square");
//Synthetic comment -- @@ -427,17 +438,11 @@

// Initial color
Display display = parent.getDisplay();
        updateColor(display, mValues.background, true /*background*/);
        updateColor(display, mValues.foreground, false /*background*/);

        setSourceType(mValues.sourceType);

new Label(mConfigurationArea, SWT.NONE);
new Label(mConfigurationArea, SWT.NONE);
new Label(mConfigurationArea, SWT.NONE);
//Synthetic comment -- @@ -482,6 +487,8 @@
// that method is called when the wizard is created, and we want to wait until the
// user has chosen a project before attempting to look up the right default image to use
if (visible) {
            mShown = true;

// Clear out old previews - important if the user goes back to page one, changes
// asset type and steps into page 2 - at that point we arrive here and we might
// display the old previews for a brief period until the preview delay timer expires.
//Synthetic comment -- @@ -492,15 +499,14 @@

// Update asset type configuration: will show/hide parameter controls depending
// on which asset type is chosen
            AssetType type = mValues.type;
assert type != null;
configureAssetType(type);

// Initial image - use the most recently used image, or the default launcher
// icon created in our default projects, if there
if (sImagePath == null) {
                IProject project = mValues.project;
if (project != null) {
IResource icon = project.findMember("res/drawable-hdpi/icon.png"); //$NON-NLS-1$
if (icon != null) {
//Synthetic comment -- @@ -511,8 +517,42 @@
}
}
if (sImagePath != null) {
                mValues.imagePath = new File(sImagePath);
mImagePathText.setText(sImagePath);
}

            try {
                mIgnore = true;

                mTrimCheckBox.setSelection(mValues.trim);

                // This doesn't work right -- not sure why. For now just use a plain slider
                // and subtract 10 from it to get the real range.
                //mPaddingSlider.setValues(0, -10, 50, 0, 1, 10);
                //mPaddingSlider.setSelection(10 + 15);
                mPaddingSlider.setSelection(mValues.padding + 10);
                mPercentLabel.setText(Integer.toString(mValues.padding) + '%');

                if (mValues.imagePath != null) {
                    mImagePathText.setText(mValues.imagePath.getPath());
                }

                if (mValues.text != null) {
                    mText.setText(mValues.text);
                }

                setSourceType(mValues.sourceType);
                setShape(mValues.shape);

                // Initial color
                Display display = mPreviewArea.getDisplay();
                //updateColor(display, new RGB(0xa4, 0xc6, 0x39), true /*background*/);
                updateColor(display, mValues.background, true /*background*/);
                updateColor(display, mValues.foreground, false /*background*/);
            } finally {
                mIgnore = false;
            }

validatePage();

requestUpdatePreview(true /*quickly*/);
//Synthetic comment -- @@ -523,13 +563,25 @@
}
}

    private void setSourceType(CreateAssetSetWizardState.SourceType sourceType) {
        if (sourceType == CreateAssetSetWizardState.SourceType.IMAGE) {
            chooseForegroundTab(mImageRadio, mImageForm);
        } else if (sourceType == CreateAssetSetWizardState.SourceType.CLIPART) {
            chooseForegroundTab(mClipartRadio, mClipartForm);
        } else if (sourceType == CreateAssetSetWizardState.SourceType.TEXT) {
            updateFontLabel(mFontButton.getFont());
            chooseForegroundTab(mTextRadio, mTextForm);
            mText.setFocus();
        }
    }

private boolean validatePage() {
String error = null;
//String warning = null;

if (mImageRadio.getSelection()) {
            String path = mValues.imagePath != null ? mValues.imagePath.getPath() : null;
            if (path == null || path.length() == 0) {
error = "Select an image";
} else if (!(new File(path).exists())) {
error = String.format("%1$s does not exist", path);
//Synthetic comment -- @@ -538,13 +590,12 @@
sImagePath = path;
}
} else if (mTextRadio.getSelection()) {
            if (mValues.text.length() == 0) {
error = "Enter text";
}
} else {
assert mClipartRadio.getSelection();
            if (mValues.clipartName == null) {
error = "Select clip art";
}
}
//Synthetic comment -- @@ -565,16 +616,22 @@
@Override
public boolean isPageComplete() {
// Force user to reach second page before hitting Finish
        return mShown;
}

// ---- Implements ModifyListener ----

@Override
public void modifyText(ModifyEvent e) {
        if (mIgnore) {
            return;
        }

if (e.getSource() == mImagePathText) {
            mValues.imagePath = new File(mImagePathText.getText().trim());
requestUpdatePreview(false);
} else if (e.getSource() == mText) {
            mValues.text = mText.getText().trim();
requestUpdatePreview(false);
}

//Synthetic comment -- @@ -590,15 +647,22 @@

@Override
public void widgetSelected(SelectionEvent e) {
        if (mIgnore) {
            return;
        }

Object source = e.getSource();
boolean updateQuickly = true;

// Tabs
if (source == mImageRadio) {
            mValues.sourceType = CreateAssetSetWizardState.SourceType.IMAGE;
chooseForegroundTab((Button) source, mImageForm);
} else if (source == mClipartRadio) {
            mValues.sourceType = CreateAssetSetWizardState.SourceType.CLIPART;
chooseForegroundTab((Button) source, mClipartForm);
} else if (source == mTextRadio) {
            mValues.sourceType = CreateAssetSetWizardState.SourceType.TEXT;
updateFontLabel(mFontButton.getFont());
chooseForegroundTab((Button) source, mTextForm);
mText.setFocus();
//Synthetic comment -- @@ -609,6 +673,7 @@
FileDialog dialog = new FileDialog(mPickImageButton.getShell(), SWT.OPEN);
String file = dialog.open();
if (file != null) {
                mValues.imagePath = new File(file);
mImagePathText.setText(file);
}
}
//Synthetic comment -- @@ -622,11 +687,14 @@
mCropRadio.setSelection(false);
}
if (source == mSquareRadio) {
            mValues.shape = GraphicGenerator.Shape.SQUARE;
            setShape(mValues.shape);
} else if (source == mCircleButton) {
            mValues.shape = GraphicGenerator.Shape.CIRCLE;
            setShape(mValues.shape);
        } else if (source == mNoShapeRadio) {
            mValues.shape = GraphicGenerator.Shape.NONE;
            setShape(mValues.shape);
}

if (SUPPORT_LAUNCHER_ICON_TYPES) {
//Synthetic comment -- @@ -645,6 +713,10 @@
}
}

        if (source == mTrimCheckBox) {
            mValues.trim = mTrimCheckBox.getSelection();
        }

if (source == mHoloDarkRadio) {
mHoloDarkRadio.setSelection(true);
mHoloLightRadio.setSelection(false);
//Synthetic comment -- @@ -683,7 +755,7 @@
// Clicked on some of the sample art
if (event.widget instanceof ImageControl) {
ImageControl image = (ImageControl) event.widget;
                                mValues.clipartName = (String) image.getData();
close();

for (Control c : mClipartPreviewPanel.getChildren()) {
//Synthetic comment -- @@ -692,7 +764,7 @@
if (mClipartPreviewPanel.getChildren().length == 0) {
try {
BufferedImage icon =
                                                GraphicGenerator.getClipartIcon(mValues.clipartName);
if (icon != null) {
Display display = mClipartForm.getDisplay();
Image swtImage = SwtUtils.convertToSwt(display, icon,
//Synthetic comment -- @@ -746,6 +818,7 @@
if (rgb != null) {
// Dispose the old color, create the
// new one, and set into the label
                mValues.background = rgb;
updateColor(mBgButton.getDisplay(), rgb, true /*background*/);
}
} else if (source == mFgButton) {
//Synthetic comment -- @@ -756,6 +829,7 @@
if (rgb != null) {
// Dispose the old color, create the
// new one, and set into the label
                mValues.foreground = rgb;
updateColor(mFgButton.getDisplay(), rgb, false /*background*/);
}
}
//Synthetic comment -- @@ -782,6 +856,7 @@
}

if (source == mPaddingSlider) {
            mValues.padding = getPadding();
mPercentLabel.setText(Integer.toString(getPadding()) + '%');

// When dragging the slider, only do periodic updates
//Synthetic comment -- @@ -791,6 +866,24 @@
requestUpdatePreview(updateQuickly);
}

    private void setShape(GraphicGenerator.Shape shape) {
        if (shape == GraphicGenerator.Shape.SQUARE) {
            mSquareRadio.setSelection(true);
            mCircleButton.setSelection(false);
            mNoShapeRadio.setSelection(false);
        } else if (shape == GraphicGenerator.Shape.CIRCLE) {
            mCircleButton.setSelection(true);
            mSquareRadio.setSelection(false);
            mNoShapeRadio.setSelection(false);
        } else if (shape == GraphicGenerator.Shape.NONE) {
            mNoShapeRadio.setSelection(true);
            mCircleButton.setSelection(false);
            mSquareRadio.setSelection(false);
        } else {
            assert false : shape;
        }
    }

private void updateFontLabel(Font f) {
FontData[] fd = f.getFontData();
FontData primary = fd[0];
//Synthetic comment -- @@ -909,86 +1002,89 @@
mPreviewArea.layout(true);
}

    public Map<String, Map<String, BufferedImage>> generateImages(boolean previewOnly) {
// Map of ids to images: Preserve insertion order (the densities)
Map<String, Map<String, BufferedImage>> categoryMap =
new LinkedHashMap<String, Map<String, BufferedImage>>();

        AssetType type = mValues.type;
        boolean trim = mValues.trim;

BufferedImage sourceImage = null;
        switch (mValues.sourceType) {
            case IMAGE: {
                // Load the image
                // TODO: Only do this when the source image type is image
                String path = mValues.imagePath != null ? mValues.imagePath.getPath() : "";
                if (path.length() == 0) {
                    setErrorMessage("Enter a filename");
                    return Collections.emptyMap();
}
                File file = new File(path);
                if (!file.exists()) {
                    setErrorMessage(String.format("%1$s does not exist", file.getPath()));
                    return Collections.emptyMap();
}

                setErrorMessage(null);
                sourceImage = getImage(path, false);
                if (sourceImage != null) {
                    if (trim) {
                        sourceImage = ImageUtils.cropBlank(sourceImage, null, TYPE_INT_ARGB);
                    }
                    if (mValues.padding != 0) {
                        sourceImage = Util.paddedImage(sourceImage, mValues.padding);
                    }
                }
                break;
            }
            case CLIPART: {
                try {
                    sourceImage = GraphicGenerator.getClipartImage(mValues.clipartName);

                    if (trim) {
                        sourceImage = ImageUtils.cropBlank(sourceImage, null, TYPE_INT_ARGB);
                    }

                    if (type.needsColors()) {
                        int color = 0xFF000000 | (mFgColor.red << 16) | (mFgColor.green << 8)
                                | mFgColor.blue;
                        Paint paint = new java.awt.Color(color);
                        sourceImage = Util.filledImage(sourceImage, paint);
                    }

                    int padding = mValues.padding;
                    if (padding != 0) {
                        sourceImage = Util.paddedImage(sourceImage, padding);
                    }
                } catch (IOException e) {
                    AdtPlugin.log(e, null);
                    return categoryMap;
                }
                break;
            }
            case TEXT: {
                String text = mValues.text;
                TextRenderUtil.Options options = new TextRenderUtil.Options();
                options.font = getSelectedFont();
                int color;
if (type.needsColors()) {
                    color = 0xFF000000 | (mFgColor.red << 16) | (mFgColor.green << 8) | mFgColor.blue;
                } else {
                    color = 0xFFFFFFFF;
                }
                options.foregroundColor = color;
                sourceImage = TextRenderUtil.renderTextImage(text, mValues.padding, options);

                if (trim) {
                    sourceImage = ImageUtils.cropBlank(sourceImage, null, TYPE_INT_ARGB);
}

                int padding = mValues.padding;
if (padding != 0) {
sourceImage = Util.paddedImage(sourceImage, padding);
}
                break;
}
}

//Synthetic comment -- @@ -999,8 +1095,14 @@
generator = new LauncherIconGenerator();
LauncherIconGenerator.LauncherOptions launcherOptions =
new LauncherIconGenerator.LauncherOptions();
                if (mCircleButton.getSelection()) {
                    launcherOptions.shape = GraphicGenerator.Shape.CIRCLE;
                } else if (mSquareRadio.getSelection()) {
                    launcherOptions.shape = GraphicGenerator.Shape.SQUARE;
                } else {
                    assert mNoShapeRadio.getSelection();
                    launcherOptions.shape = GraphicGenerator.Shape.NONE;
                }
launcherOptions.crop = mCropRadio.getSelection();

if (SUPPORT_LAUNCHER_ICON_TYPES) {
//Synthetic comment -- @@ -1038,8 +1140,7 @@
generator = new NotificationIconGenerator();
NotificationIconGenerator.NotificationOptions notificationOptions =
new NotificationIconGenerator.NotificationOptions();
                notificationOptions.shape = mValues.shape;
options = notificationOptions;
break;
}
//Synthetic comment -- @@ -1054,11 +1155,15 @@

options.sourceImage = sourceImage;

        IProject project = mValues.project;
        if (mValues.minSdk != -1) {
            options.minSdk = mValues.minSdk;
        } else {
            Pair<Integer, Integer> v = ManifestInfo.computeSdkVersions(project);
            options.minSdk = v.getFirst();
        }

        String baseName = mValues.outputName;
generator.generate(null, categoryMap, this, options, baseName);

return categoryMap;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/CreateAssetSetWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/CreateAssetSetWizard.java
//Synthetic comment -- index f42960c..0b5fc8e 100644

//Synthetic comment -- @@ -71,6 +71,7 @@
private ConfigureAssetSetPage mConfigureAssetPage;
private IProject mInitialProject;
private List<IResource> mCreatedFiles;
    private CreateAssetSetWizardState mValues = new CreateAssetSetWizardState();

/** Creates a new asset set wizard */
public CreateAssetSetWizard() {
//Synthetic comment -- @@ -79,24 +80,21 @@

@Override
public void addPages() {
        mValues.project = mInitialProject;

        mChooseAssetPage = new ChooseAssetTypePage(mValues);
        mConfigureAssetPage = new ConfigureAssetSetPage(mValues);

addPage(mChooseAssetPage);
addPage(mConfigureAssetPage);
}

@Override
public boolean performFinish() {
Map<String, Map<String, BufferedImage>> categories =
mConfigureAssetPage.generateImages(false);

        IProject project = mValues.project;

// Write out the images into the project
boolean yesToAll = false;
//Synthetic comment -- @@ -236,28 +234,10 @@
}
}

/** Sets the initial project to be used by the wizard */
void setProject(IProject project) {
mInitialProject = project;
        mValues.project = project;
}

@Override
//Synthetic comment -- @@ -265,9 +245,7 @@
setHelpAvailable(false);

mInitialProject = guessProject(selection);
        mValues.project = mInitialProject;
}

private IProject guessProject(IStructuredSelection selection) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/CreateAssetSetWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/CreateAssetSetWizardState.java
new file mode 100644
//Synthetic comment -- index 0000000..d14b60a

//Synthetic comment -- @@ -0,0 +1,89 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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
package com.android.ide.eclipse.adt.internal.assetstudio;

import com.android.assetstudiolib.GraphicGenerator.Shape;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.RGB;

import java.io.File;

/**
 * Value object for the AssetStudio wizard. These values are both set by the
 * wizard as well as read by the wizard initially, so passing in a configured
 * {@link CreateAssetSetWizardState} to the icon generator is possible.
 */
public class CreateAssetSetWizardState {
    /**
     * The type of asset being created. This field is static such that when you
     * bring up the wizard repeatedly (for example to create multiple
     * notification icons) you don't have to keep selecting the same type over
     * and over.
     */
    public static AssetType sLastType = AssetType.LAUNCHER;

    /** The type of asset to be created */
    public AssetType type = sLastType;

    /** The base name to use for the created icons */
    public String outputName;

    /** The minimum SDK being targeted */
    public int minSdk = -1;

    /** The project to create the icons into */
    public IProject project;

    /** Whether empty space around the source image should be trimmed */
    public boolean trim;

    /** The type of source the icon is being created from */
    public SourceType sourceType = SourceType.TEXT;

    /** If {@link #sourceType} is a {@link SourceType#CLIPART}, the name of the clipart image */
    public String clipartName;

    /** If {@link #sourceType} is a {@link SourceType#IMAGE}, the path to the input image */
    public File imagePath;

    /** If {@link #sourceType} is a {@link SourceType#TEXT}, the text to render */
    public String text = "aA";

    /** The amount of padding to add around the source image */
    public int padding = 15;

    /** The background shape */
    public Shape shape = Shape.SQUARE;

    /** The background color to use for the shape (unless the shape is {@link Shape#NONE} */
    public RGB background = new RGB(0xff, 0x00, 0x00);

    /** The background color to use for the text or clipart (unless shape is {@link Shape#NONE} */
    public RGB foreground = new RGB(0x00, 0x00, 0x00);

    /** Types of sources that the asset studio can use to generate icons from */
    public enum SourceType {
        /** Generate the icon using the image pointed to by {@link #imagePath} */
        IMAGE,

        /** Generate the icon using the clipart named by {@link #clipartName} */
        CLIPART,

        /** Generate the icon using the text in {@link #text} */
        TEXT
    };
}







