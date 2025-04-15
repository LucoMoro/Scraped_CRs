/*New zoom controls in the layout editor.

New 100% and "real size" buttons. The real size
computes a scale value based on the dpi of the monitor
and the dpi of the layout device info.

Also cleaned-up the CustomToggle class to be a
CustomButton (toggle optional) as the 100% and
zoom in/out buttons are not toggles.

Migrated the clipping button to be set up through
a CustomButton.

Added groups of CustomButton, which are closer to
each others than normal.

Change-Id:Id4b6ed54b7f275f848333b04aeb42ef80e89ea9b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index f5ad935..3d69100 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.ide.eclipse.adt.internal.editors.layout.configuration;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.configurations.DockModeQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
//Synthetic comment -- @@ -38,7 +37,6 @@
import com.android.ide.eclipse.adt.internal.sdk.LayoutDeviceManager;
import com.android.ide.eclipse.adt.internal.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData.LayoutBridge;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice.DeviceConfig;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IStyleResourceValue;
//Synthetic comment -- @@ -107,7 +105,6 @@
private final static int LOCALE_LANG = 0;
private final static int LOCALE_REGION = 1;

    private Button mClippingButton;
private Label mCurrentLayoutLabel;

private Combo mDeviceCombo;
//Synthetic comment -- @@ -127,12 +124,6 @@
private final ArrayList<ResourceQualifier[] > mLocaleList =
new ArrayList<ResourceQualifier[]>();

    /**
     * clipping value. If true, the rendering is limited to the screensize. This is the default
     * value
     */
    private boolean mClipping = true;

private final ConfigState mState = new ConfigState();

private boolean mSdkChanged = false;
//Synthetic comment -- @@ -164,7 +155,6 @@
void onConfigurationChange();
void onThemeChange();
void onCreate();
        void onClippingChange();

ProjectResources getProjectResources();
ProjectResources getFrameworkResources();
//Synthetic comment -- @@ -285,11 +275,11 @@
}

/**
     * Interface implemented by the part which owns a {@link ConfigurationComposite}
     * to define and handle custom toggle buttons in the button bar. Each toggle is
* implemented using a button, with a callback when the button is selected.
*/
    public static abstract class CustomToggle {

/** The UI label of the toggle. Can be null if the image exists. */
private final String mUiLabel;
//Synthetic comment -- @@ -300,66 +290,140 @@
/** The tooltip for the toggle. Can be null. */
private final String mUiTooltip;

/**
         * Initializes a new {@link CustomToggle}. The values set here will be used
         * later to create the actual toggle.
         *
         * @param uiLabel   The UI label of the toggle. Can be null if the image exists.
         * @param image     The image to use for this toggle. Can be null if the label exists.
         * @param uiTooltip The tooltip for the toggle. Can be null.
*/
        public CustomToggle(
String uiLabel,
Image image,
                String uiTooltip) {
mUiLabel = uiLabel;
mImage = image;
mUiTooltip = uiTooltip;
}

        /** Called by the {@link ConfigurationComposite} when the button is selected. */
public abstract void onSelected(boolean newState);

        private void createToggle(Composite parent) {
            final Button b = new Button(parent, SWT.TOGGLE | SWT.FLAT);

if (mUiTooltip != null) {
                b.setToolTipText(mUiTooltip);
}
if (mImage != null) {
                b.setImage(mImage);
}
if (mUiLabel != null) {
                b.setText(mUiLabel);
}

            b.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                    onSelected(b.getSelection());
}
});
}
}

/**
* Creates a new {@link ConfigurationComposite} and adds it to the parent.
*
* @param listener An {@link IConfigListener} that gets and sets configuration properties.
*          Mandatory, cannot be null.
     * @param customToggles An array of {@link CustomToggle} to define extra toggles button
     *          to display at the top of the composite. Can be empty or null.
* @param parent The parent composite.
* @param style The style of this composite.
*/
public ConfigurationComposite(IConfigListener listener,
            CustomToggle[] customToggles,
Composite parent, int style) {
super(parent, style);
mListener = listener;

        if (customToggles == null) {
            customToggles = new CustomToggle[0];
}

GridLayout gl;
//Synthetic comment -- @@ -368,7 +432,7 @@

// ---- First line: custom buttons, clipping button, editing config display.
Composite labelParent = new Composite(this, SWT.NONE);
        labelParent.setLayout(gl = new GridLayout(3 + customToggles.length, false));
gl.marginWidth = gl.marginHeight = 0;
labelParent.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
gd.horizontalSpan = cols;
//Synthetic comment -- @@ -378,20 +442,20 @@
mCurrentLayoutLabel.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
gd.widthHint = 50;

        for (CustomToggle toggle : customToggles) {
            toggle.createToggle(labelParent);
        }

        mClippingButton = new Button(labelParent, SWT.TOGGLE | SWT.FLAT);
        mClippingButton.setSelection(mClipping);
        mClippingButton.setToolTipText("Toggles screen clipping on/off");
        mClippingButton.setImage(IconFactory.getInstance().getIcon("clipping")); //$NON-NLS-1$
        mClippingButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onClippingChange();
}
        });

// ---- 2nd line: device/config/locale/theme Combos, create button.

//Synthetic comment -- @@ -624,7 +688,9 @@
* @see #storeState()
* @see #onSdkLoaded(IAndroidTarget)
*/
    public void onXmlModelLoaded() {
// only attempt to do anything if the SDK and targets are loaded.
LoadStatus sdkStatus = AdtPlugin.getDefault().getSdkLoadStatus();
if (sdkStatus == LoadStatus.LOADED) {
//Synthetic comment -- @@ -657,12 +723,7 @@
mEditedConfig = resFolder.getConfiguration();
}

                // update the clipping state
                AndroidTargetData targetData = Sdk.getCurrent().getTargetData(mTarget);
                if (targetData != null) {
                    LayoutBridge bridge = targetData.getLayoutBridge();
                    setClippingSupport(bridge.apiLevel >= 4);
                }

// get the file stored state
boolean loadedConfigData = false;
//Synthetic comment -- @@ -710,6 +771,8 @@
mDisableUpdates--;
mFirstXmlModelChange  = false;
}
}

/**
//Synthetic comment -- @@ -1261,21 +1324,6 @@
return mThemeCombo.getSelectionIndex() >= mPlatformThemeCount;
}

    public boolean getClipping() {
        return mClipping;
    }

    private void setClippingSupport(boolean b) {
        mClippingButton.setEnabled(b);
        if (b) {
            mClippingButton.setToolTipText("Toggles screen clipping on/off");
        } else {
            mClipping = true;
            mClippingButton.setSelection(true);
            mClippingButton.setToolTipText("Non clipped rendering is not supported");
        }
    }

/**
* Loads the list of {@link LayoutDevice} and inits the UI with it.
*/
//Synthetic comment -- @@ -1635,13 +1683,6 @@
}
}

    private void onClippingChange() {
        mClipping = mClippingButton.getSelection();
        if (mListener != null) {
            mListener.onClippingChange();
        }
    }

/**
* Returns whether the given <var>style</var> is a theme.
* This is done by making sure the parent is a theme.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle1/GraphicalLayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle1/GraphicalLayoutEditor.java
//Synthetic comment -- index 1309273..6dcf4bd 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ILayoutReloadListener;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.CustomToggle;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.parts.ElementCreateCommand;
//Synthetic comment -- @@ -238,32 +238,38 @@

// create the top part for the configuration control

        CustomToggle[] toggles = new CustomToggle[] {
                new CustomToggle(
                        null, //text
                        IconFactory.getInstance().getIcon("explode"),
                        "Displays extra margins in the layout."
                        ) {
                    @Override
                    public void onSelected(boolean newState) {
                        mUseExplodeMode = newState;
                        recomputeLayout();
                    }
                },
                new CustomToggle(
                        null, //text
                        IconFactory.getInstance().getIcon("outline"),
                        "Shows the outline of all views in the layout."
                        ) {
                    @Override
                    public void onSelected(boolean newState) {
                        mUseOutlineMode = newState;
                        recomputeLayout();
}
}
};

        mConfigComposite = new ConfigurationComposite(this, toggles, parent, SWT.NONE);

// create a new composite that will contain the standard editor controls.
Composite editorParent = new Composite(parent, SWT.NONE);
//Synthetic comment -- @@ -873,11 +879,6 @@
recomputeLayout();
}

    public void onClippingChange() {
        recomputeLayout();
    }


public void onCreate() {
LayoutCreatorDialog dialog = new LayoutCreatorDialog(mParent.getShell(),
mEditedFile.getName(), mConfigComposite.getCurrentConfig());
//Synthetic comment -- @@ -1041,7 +1042,7 @@

ILayoutResult result = computeLayout(bridge, parser,
iProject /* projectKey */,
                                    width, height, !mConfigComposite.getClipping(),
density, xdpi, ydpi,
theme, isProjectTheme,
configuredProjectRes, frameworkResources, mProjectCallback,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index f36d9d5..27b7c20 100755

//Synthetic comment -- @@ -28,7 +28,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ILayoutReloadListener;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.CustomToggle;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.parts.ElementCreateCommand;
//Synthetic comment -- @@ -193,6 +193,12 @@

private boolean mUseExplodeMode;


public GraphicalEditorPart(LayoutEditor layoutEditor) {
mLayoutEditor = layoutEditor;
//Synthetic comment -- @@ -236,53 +242,97 @@
gl.marginHeight = gl.marginWidth = 0;

// create the top part for the configuration control

        CustomToggle[] toggles = new CustomToggle[] {
                new CustomToggle(
                        "-",
                        null, //image
                        "Canvas zoom out."
                        ) {
                    @Override
                    public void onSelected(boolean newState) {
                        rescale(-1);
                    }
},
                new CustomToggle(
                        "+",
                        null, //image
                        "Canvas zoom in."
                        ) {
                    @Override
                    public void onSelected(boolean newState) {
                        rescale(+1);
                    }
                },
                new CustomToggle(
                        null, //text
                        IconFactory.getInstance().getIcon("explode"),
                        "Displays extra margins in the layout."
                        ) {
                    @Override
                    public void onSelected(boolean newState) {
                        mUseExplodeMode = newState;
                        recomputeLayout();
                    }
                },
                new CustomToggle(
                        null, //text
                        IconFactory.getInstance().getIcon("outline"),
                        "Shows the of all views in the layout."
                        ) {
                    @Override
                    public void onSelected(boolean newState) {
                        mCanvasViewer.getCanvas().setShowOutline(newState);
}
}
};

mConfigListener = new ConfigListener();
        mConfigComposite = new ConfigurationComposite(mConfigListener, toggles, parent, SWT.BORDER);

mSashPalette = new SashForm(parent, SWT.HORIZONTAL);
mSashPalette.setLayoutData(new GridData(GridData.FILL_BOTH));
//Synthetic comment -- @@ -337,10 +387,36 @@
s = s / 2;
}

        mCanvasViewer.getCanvas().setScale(s);

}


@Override
public void dispose() {
//Synthetic comment -- @@ -449,10 +525,6 @@
recomputeLayout();
}

        public void onClippingChange() {
            recomputeLayout();
        }

public void onCreate() {
LayoutCreatorDialog dialog = new LayoutCreatorDialog(mConfigComposite.getShell(),
mEditedFile.getName(), mConfigComposite.getCurrentConfig());
//Synthetic comment -- @@ -812,10 +884,25 @@
}

public void onTargetChange() {
        mConfigComposite.onXmlModelLoaded();
mConfigListener.onConfigurationChange();
}

public void onSdkChange() {
Sdk currentSdk = Sdk.getCurrent();
if (currentSdk != null) {
//Synthetic comment -- @@ -980,6 +1067,11 @@
LayoutBridge bridge = data.getLayoutBridge();

if (bridge.bridge != null) { // bridge can never be null.
renderWithBridge(iProject, model, bridge);
} else {
// SDK is loaded but not the layout library!
//Synthetic comment -- @@ -1098,7 +1190,7 @@

ILayoutResult result = computeLayout(bridge, parser,
iProject /* projectKey */,
                width, height, !mConfigComposite.getClipping(),
density, xdpi, ydpi,
theme, isProjectTheme,
configuredProjectRes, frameworkResources, mProjectCallback,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index c143c35..5a1bb6f 100755

//Synthetic comment -- @@ -526,10 +526,12 @@
return mHScale.getScale();
}

    /* package */ void setScale(double scale) {
mHScale.setScale(scale);
mVScale.setScale(scale);
        redraw();
}

/**







