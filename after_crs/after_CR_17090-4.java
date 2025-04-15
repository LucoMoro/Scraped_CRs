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
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.configurations.DockModeQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
//Synthetic comment -- @@ -38,7 +37,6 @@
import com.android.ide.eclipse.adt.internal.sdk.LayoutDeviceManager;
import com.android.ide.eclipse.adt.internal.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice.DeviceConfig;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.IStyleResourceValue;
//Synthetic comment -- @@ -107,7 +105,6 @@
private final static int LOCALE_LANG = 0;
private final static int LOCALE_REGION = 1;

private Label mCurrentLayoutLabel;

private Combo mDeviceCombo;
//Synthetic comment -- @@ -127,12 +124,6 @@
private final ArrayList<ResourceQualifier[] > mLocaleList =
new ArrayList<ResourceQualifier[]>();

private final ConfigState mState = new ConfigState();

private boolean mSdkChanged = false;
//Synthetic comment -- @@ -164,7 +155,6 @@
void onConfigurationChange();
void onThemeChange();
void onCreate();

ProjectResources getProjectResources();
ProjectResources getFrameworkResources();
//Synthetic comment -- @@ -285,11 +275,11 @@
}

/**
     * Abstract class implemented by the part which owns a {@link ConfigurationComposite}
     * to define and handle custom buttons in the button bar. Each button is
* implemented using a button, with a callback when the button is selected.
*/
    public static abstract class CustomButton {

/** The UI label of the toggle. Can be null if the image exists. */
private final String mUiLabel;
//Synthetic comment -- @@ -300,66 +290,140 @@
/** The tooltip for the toggle. Can be null. */
private final String mUiTooltip;

        /** Whether the button is a toggle */
        private final boolean mIsToggle;
        /** The default value of the toggle. */
        private final boolean mDefaultValue;

/**
         * the SWT button.
*/
        private Button mButton;


        /**
         * Initializes a new {@link CustomButton}. The values set here will be used
         * later to create the actual button.
         *
         * @param uiLabel      The UI label of the button. Can be null if the image exists.
         * @param image        The image to use for this button. Can be null if the label exists.
         * @param uiTooltip    The tooltip for the button. Can be null.
         * @param isToggle     Whether the button is a toggle button.
         * @param defaultValue The default value of the toggle if <var>isToggle</var> is true.
         */
        public CustomButton(
String uiLabel,
Image image,
                String uiTooltip,
                boolean isToggle,
                boolean defaultValue) {
mUiLabel = uiLabel;
mImage = image;
mUiTooltip = uiTooltip;
            mIsToggle = isToggle;
            mDefaultValue = defaultValue;
}

        /**
         * Initializes a new {@link CustomButton} that is <b>not</b> a toggle.
         * The values set here will be used later to create the actual button.
         *
         * @param uiLabel      The UI label of the button. Can be null if the image exists.
         * @param image        The image to use for this button. Can be null if the label exists.
         * @param uiTooltip    The tooltip for the button. Can be null.
         */
        public CustomButton(
                String uiLabel,
                Image image,
                String uiTooltip) {
            this(uiLabel, image, uiTooltip, false, false);
        }


        /**
         * Called by the {@link ConfigurationComposite} when the button is selected
         * @param newState the state of the button if the button is a toggle.
         */
public abstract void onSelected(boolean newState);

        private void createButton(Composite parent) {
            int style = SWT.FLAT;
            if (mIsToggle) {
                style |= SWT.TOGGLE;
            }
            mButton = new Button(parent, style);

if (mUiTooltip != null) {
                mButton.setToolTipText(mUiTooltip);
}
if (mImage != null) {
                mButton.setImage(mImage);
}
if (mUiLabel != null) {
                mButton.setText(mUiLabel);
}

            if (mIsToggle && mDefaultValue) {
                mButton.setSelection(true);
            }

            mButton.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                    onSelected(mButton.getSelection());
}
});
}

        public void setEnabled(boolean enabledState) {
            if (mButton != null) {
                mButton.setEnabled(enabledState);
            }
        }

        public void setToolTipText(String text) {
            if (mButton != null) {
                mButton.setToolTipText(text);
            }
        }

        public void setSelection(boolean selected) {
            if (mButton != null) {
                mButton.setSelection(selected);
            }
        }

        public boolean getSelection() {
            if (mButton != null) {
                return mButton.getSelection();
            }

            return mDefaultValue;
        }
}

/**
* Creates a new {@link ConfigurationComposite} and adds it to the parent.
*
     * The method also receives custom buttons to set into the configuration composite. The list
     * is organized as an array of arrays. Each array represents a group of buttons thematically
     * grouped together.
     *
* @param listener An {@link IConfigListener} that gets and sets configuration properties.
*          Mandatory, cannot be null.
     * @param customButtons An array of array of {@link CustomButton} to define extra action/toggle
     *          buttons to display at the top of the composite. Can be empty or null.
* @param parent The parent composite.
* @param style The style of this composite.
*/
public ConfigurationComposite(IConfigListener listener,
            CustomButton[][] customButtons,
Composite parent, int style) {
super(parent, style);
mListener = listener;

        if (customButtons == null) {
            customButtons = new CustomButton[0][0];
}

GridLayout gl;
//Synthetic comment -- @@ -368,7 +432,7 @@

// ---- First line: custom buttons, clipping button, editing config display.
Composite labelParent = new Composite(this, SWT.NONE);
        labelParent.setLayout(gl = new GridLayout(2 + customButtons.length, false));
gl.marginWidth = gl.marginHeight = 0;
labelParent.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
gd.horizontalSpan = cols;
//Synthetic comment -- @@ -378,20 +442,20 @@
mCurrentLayoutLabel.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
gd.widthHint = 50;

        for (CustomButton[] buttons : customButtons) {
            if (buttons.length == 1) {
                buttons[0].createButton(labelParent);
            } else if (buttons.length > 1) {
                Composite buttonParent = new Composite(labelParent, SWT.NONE);
                buttonParent.setLayout(gl = new GridLayout(buttons.length, false));
                gl.marginWidth = gl.marginHeight = 0;
                gl.horizontalSpacing = 1;
                for (CustomButton button : buttons) {
                    button.createButton(buttonParent);
                }

}
        }

// ---- 2nd line: device/config/locale/theme Combos, create button.

//Synthetic comment -- @@ -624,7 +688,9 @@
* @see #storeState()
* @see #onSdkLoaded(IAndroidTarget)
*/
    public AndroidTargetData onXmlModelLoaded() {
        AndroidTargetData targetData = null;

// only attempt to do anything if the SDK and targets are loaded.
LoadStatus sdkStatus = AdtPlugin.getDefault().getSdkLoadStatus();
if (sdkStatus == LoadStatus.LOADED) {
//Synthetic comment -- @@ -657,12 +723,7 @@
mEditedConfig = resFolder.getConfiguration();
}

                targetData = Sdk.getCurrent().getTargetData(mTarget);

// get the file stored state
boolean loadedConfigData = false;
//Synthetic comment -- @@ -710,6 +771,8 @@
mDisableUpdates--;
mFirstXmlModelChange  = false;
}

        return targetData;
}

/**
//Synthetic comment -- @@ -1261,21 +1324,6 @@
return mThemeCombo.getSelectionIndex() >= mPlatformThemeCount;
}

/**
* Loads the list of {@link LayoutDevice} and inits the UI with it.
*/
//Synthetic comment -- @@ -1635,13 +1683,6 @@
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
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.CustomButton;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.parts.ElementCreateCommand;
//Synthetic comment -- @@ -238,32 +238,38 @@

// create the top part for the configuration control

        CustomButton[][] customButtons = new CustomButton[][] {
                new CustomButton[] {
                    new CustomButton(
                            null, //text
                            IconFactory.getInstance().getIcon("explode"),
                            "Displays extra margins in the layout.",
                            true /*toggle*/,
                            false /*defaultValue*/
                            ) {
                        @Override
                        public void onSelected(boolean newState) {
                            mUseExplodeMode = newState;
                            recomputeLayout();
                        }
                    },
                    new CustomButton(
                            null, //text
                            IconFactory.getInstance().getIcon("outline"),
                            "Shows the outline of all views in the layout.",
                            true /*toggle*/,
                            false /*defaultValue*/
                            ) {
                        @Override
                        public void onSelected(boolean newState) {
                            mUseOutlineMode = newState;
                            recomputeLayout();
                        }
}
}
};

        mConfigComposite = new ConfigurationComposite(this, customButtons, parent, SWT.NONE);

// create a new composite that will contain the standard editor controls.
Composite editorParent = new Composite(parent, SWT.NONE);
//Synthetic comment -- @@ -873,11 +879,6 @@
recomputeLayout();
}

public void onCreate() {
LayoutCreatorDialog dialog = new LayoutCreatorDialog(mParent.getShell(),
mEditedFile.getName(), mConfigComposite.getCurrentConfig());
//Synthetic comment -- @@ -1041,7 +1042,7 @@

ILayoutResult result = computeLayout(bridge, parser,
iProject /* projectKey */,
                                    width, height, false /*renderFullSize*/,
density, xdpi, ydpi,
theme, isProjectTheme,
configuredProjectRes, frameworkResources, mProjectCallback,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index f36d9d5..27b7c20 100755

//Synthetic comment -- @@ -28,7 +28,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutReloadMonitor.ILayoutReloadListener;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.LayoutCreatorDialog;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.CustomButton;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.parts.ElementCreateCommand;
//Synthetic comment -- @@ -193,6 +193,12 @@

private boolean mUseExplodeMode;

    private CustomButton mZoomRealSizeButton;
    private CustomButton mZoomOutButton;
    private CustomButton mZoomResetButton;
    private CustomButton mZoomInButton;

    private CustomButton mClippingButton;

public GraphicalEditorPart(LayoutEditor layoutEditor) {
mLayoutEditor = layoutEditor;
//Synthetic comment -- @@ -236,53 +242,97 @@
gl.marginHeight = gl.marginWidth = 0;

// create the top part for the configuration control
        CustomButton[][] customButtons = new CustomButton[][] {
                new CustomButton[] {
                    mZoomRealSizeButton = new CustomButton(
                            "*",
                            null, //image
                            "Emulate real size",
                            true /*isToggle*/,
                            false /*defaultValue*/
                            ) {
                        @Override
                        public void onSelected(boolean newState) {
                            mZoomOutButton.setEnabled(!newState);
                            mZoomResetButton.setEnabled(!newState);
                            mZoomInButton.setEnabled(!newState);
                            rescaleToReal(newState);
                        }
                    },
                    mZoomOutButton = new CustomButton(
                            "-",
                            null, //image
                            "Canvas zoom out."
                            ) {
                        @Override
                        public void onSelected(boolean newState) {
                            rescale(-1);
                        }
                    },
                    mZoomResetButton = new CustomButton(
                            "100%",
                            null, //image
                            "Reset Canvas to 100%"
                            ) {
                        @Override
                        public void onSelected(boolean newState) {
                            resetScale();
                        }
                    },
                    mZoomInButton = new CustomButton(
                            "+",
                            null, //image
                            "Canvas zoom in."
                            ) {
                        @Override
                        public void onSelected(boolean newState) {
                            rescale(+1);
                        }
                    },
},
                new CustomButton[] {
                    new CustomButton(
                            null, //text
                            IconFactory.getInstance().getIcon("explode"), //$NON-NLS-1$
                            "Displays extra margins in the layout.",
                            true /*toggle*/,
                            false /*defaultValue*/
                            ) {
                        @Override
                        public void onSelected(boolean newState) {
                            mUseExplodeMode = newState;
                            recomputeLayout();
                        }
                    },
                    new CustomButton(
                            null, //text
                            IconFactory.getInstance().getIcon("outline"), //$NON-NLS-1$
                            "Shows the outline of all views in the layout.",
                            true /*toggle*/,
                            false /*defaultValue*/
                            ) {
                        @Override
                        public void onSelected(boolean newState) {
                            mCanvasViewer.getCanvas().setShowOutline(newState);
                        }
                    },
                    mClippingButton =  new CustomButton(
                            null, //text
                            IconFactory.getInstance().getIcon("clipping"), //$NON-NLS-1$
                            "Toggles screen clipping on/off",
                            true /*toggle*/,
                            true /*defaultValue*/
                            ) {
                        @Override
                        public void onSelected(boolean newState) {
                            recomputeLayout();
                        }
}
}
};

mConfigListener = new ConfigListener();
        mConfigComposite = new ConfigurationComposite(mConfigListener, customButtons, parent, SWT.BORDER);

mSashPalette = new SashForm(parent, SWT.HORIZONTAL);
mSashPalette.setLayoutData(new GridData(GridData.FILL_BOTH));
//Synthetic comment -- @@ -337,10 +387,36 @@
s = s / 2;
}

        mCanvasViewer.getCanvas().setScale(s, true /*redraw*/);

}

    /**
     * Reset the canvas scale to 100%
     */
    private void resetScale() {
        mCanvasViewer.getCanvas().setScale(1, true /*redraw*/);
    }

    private void rescaleToReal(boolean real) {
        if (real) {
            computeAndSetRealScale(true /*redraw*/);
        } else {
            // reset the scale to 100%
            mCanvasViewer.getCanvas().setScale(1, true /*redraw*/);
        }
    }

    private void computeAndSetRealScale(boolean redraw) {
        // compute average dpi of X and Y
        float dpi = (mConfigComposite.getXDpi() + mConfigComposite.getYDpi()) / 2.f;

        // get the monitor dpi
        float monitor = 110f;

        mCanvasViewer.getCanvas().setScale(monitor / dpi, redraw);
    }


@Override
public void dispose() {
//Synthetic comment -- @@ -449,10 +525,6 @@
recomputeLayout();
}

public void onCreate() {
LayoutCreatorDialog dialog = new LayoutCreatorDialog(mConfigComposite.getShell(),
mEditedFile.getName(), mConfigComposite.getCurrentConfig());
//Synthetic comment -- @@ -812,10 +884,25 @@
}

public void onTargetChange() {
        AndroidTargetData targetData = mConfigComposite.onXmlModelLoaded();
        if (targetData != null) {
            LayoutBridge bridge = targetData.getLayoutBridge();
            setClippingSupport(bridge.apiLevel >= 4);
        }

mConfigListener.onConfigurationChange();
}

    private void setClippingSupport(boolean b) {
        mClippingButton.setEnabled(b);
        if (b) {
            mClippingButton.setToolTipText("Toggles screen clipping on/off");
        } else {
            mClippingButton.setSelection(true);
            mClippingButton.setToolTipText("Non clipped rendering is not supported");
        }
    }

public void onSdkChange() {
Sdk currentSdk = Sdk.getCurrent();
if (currentSdk != null) {
//Synthetic comment -- @@ -980,6 +1067,11 @@
LayoutBridge bridge = data.getLayoutBridge();

if (bridge.bridge != null) { // bridge can never be null.
                    // if drawing in real size, (re)set the scaling factor.
                    if (mZoomRealSizeButton.getSelection()) {
                        computeAndSetRealScale(false /*redraw*/);
                    }

renderWithBridge(iProject, model, bridge);
} else {
// SDK is loaded but not the layout library!
//Synthetic comment -- @@ -1098,7 +1190,7 @@

ILayoutResult result = computeLayout(bridge, parser,
iProject /* projectKey */,
                width, height, !mClippingButton.getSelection(),
density, xdpi, ydpi,
theme, isProjectTheme,
configuredProjectRes, frameworkResources, mProjectCallback,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index c143c35..5a1bb6f 100755

//Synthetic comment -- @@ -526,10 +526,12 @@
return mHScale.getScale();
}

    /* package */ void setScale(double scale, boolean redraw) {
mHScale.setScale(scale);
mVScale.setScale(scale);
        if (redraw) {
            redraw();
        }
}

/**







