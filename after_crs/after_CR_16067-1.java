/*"create alternate layout" default to no qualifier.

Previously the dialog opened with all qualifiers set up
which would make most users default to making a very restrictive
layout.

New version is simpler (can't edit the qualifier value), and
let the user simply adding the ones they want.

Change-Id:I684390f58f14ab74678ac03980e6643442361ab3*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigEditDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigEditDialog.java
//Synthetic comment -- index f337cdf..1dc5c27 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector.ConfigurationState;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector.IQualifierFilter;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector.SelectorMode;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.jface.dialogs.Dialog;
//Synthetic comment -- @@ -236,7 +237,7 @@
}
});

        mConfigSelector = new ConfigurationSelector(configGroup, SelectorMode.DEVICE_ONLY);
// configure the selector to be in "device mode" and not accept language/region/version
// since those are selected from a different combo
// FIXME: add version combo.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/LayoutCreatorDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/LayoutCreatorDialog.java
//Synthetic comment -- index de31ca4..978f114 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector.ConfigurationState;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector.SelectorMode;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.jface.dialogs.Dialog;
//Synthetic comment -- @@ -65,9 +66,13 @@
new Label(parent, SWT.NONE).setText(
String.format("Configuration for the alternate version of %1$s", mFileName));

        mSelector = new ConfigurationSelector(parent, SelectorMode.CONFIG_ONLY);
mSelector.setConfiguration(mConfig);

        // because the ConfigSelector is running in CONFIG_ONLY mode, the current config
        // displayed by it is not mConfig anymore, so get the current config.
        mSelector.getConfiguration(mConfig);

// parent's layout is a GridLayout as specified in the javadoc.
GridData gd = new GridData();
gd.widthHint = ConfigurationSelector.WIDTH_HINT;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringInputPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringInputPage.java
//Synthetic comment -- index 954e58c..cbcd581 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector.SelectorMode;
import com.android.sdklib.SdkConstants;

import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -208,7 +209,7 @@
label = new Label(group, SWT.NONE);
label.setText("&Configuration:");

        mConfigSelector = new ConfigurationSelector(group, SelectorMode.DEFAULT);
GridData gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
gd.horizontalSpan = 2;
gd.widthHint = ConfigurationSelector.WIDTH_HINT;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java
//Synthetic comment -- index 21703d0..1839d0b 100644

//Synthetic comment -- @@ -116,7 +116,7 @@

private final HashMap<Class<? extends ResourceQualifier>, QualifierEditBase> mUiMap =
new HashMap<Class<? extends ResourceQualifier>, QualifierEditBase>();
    private final SelectorMode mMode;
private Composite mQualifierEditParent;
private IQualifierFilter mQualifierFilter;

//Synthetic comment -- @@ -194,6 +194,18 @@
OK, INVALID_CONFIG, REGION_WITHOUT_LANGUAGE;
}

    public enum SelectorMode {
        /** the default mode */
        DEFAULT,
        /** mode forcing the qualifier values to be valid on a device.
         * For instance {@link Density#NODPI} is a valid qualifier for a resource configuration but
         * this is not valid on a device */
        DEVICE_ONLY,
        /** mode where only the specific config can be edited. The user can only select
         * which non-empty qualifier to select. */
        CONFIG_ONLY;
    }

/**
* A filter for {@link ResourceQualifier}.
* @see ConfigurationSelector#setQualifierFilter(IQualifierFilter)
//Synthetic comment -- @@ -211,16 +223,13 @@
* If the device mode is <code>true</code> then the configuration selector only
* allows to create configuration that are valid on a device (as opposed to resource
* configuration).
     * .
* @param parent the composite parent.
* @param deviceMode the device mode.
*/
    public ConfigurationSelector(Composite parent, SelectorMode mode) {
super(parent, SWT.NONE);
        mMode  = mode;

GridLayout gl = new GridLayout(4, false);
gl.marginWidth = gl.marginHeight = 0;
//Synthetic comment -- @@ -247,8 +256,10 @@

mFullTableViewer = new TableViewer(fullTable);
mFullTableViewer.setContentProvider(new QualifierContentProvider());
        // the label provider must return the value of the label in this case if the mode is
        // CONFIG_ONLY
mFullTableViewer.setLabelProvider(new QualifierLabelProvider(
                mMode == SelectorMode.CONFIG_ONLY));
mFullTableViewer.setInput(mBaseConfiguration);
mFullTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
public void selectionChanged(SelectionChangedEvent event) {
//Synthetic comment -- @@ -364,20 +375,24 @@
if (first instanceof ResourceQualifier) {
mRemoveButton.setEnabled(true);

                            if (mMode != SelectorMode.CONFIG_ONLY) {
                                QualifierEditBase composite = mUiMap.get(first.getClass());

                                if (composite != null) {
                                    composite.setQualifier((ResourceQualifier)first);
                                }

                                mStackLayout.topControl = composite;
                                mQualifierEditParent.layout();
}

return;
}
} else {
                        if (mMode != SelectorMode.CONFIG_ONLY) {
                            mStackLayout.topControl = null;
                            mQualifierEditParent.layout();
                        }
}
}

//Synthetic comment -- @@ -385,29 +400,34 @@
}
});

        if (mMode != SelectorMode.CONFIG_ONLY) {
            // 4th column is the detail of the selected qualifier
            mQualifierEditParent = new Composite(this, SWT.NONE);
            mQualifierEditParent.setLayout(mStackLayout = new StackLayout());
            mQualifierEditParent.setLayoutData(new GridData(GridData.FILL_VERTICAL));

            // create the UI for all the qualifiers, and associate them to the
            // ResourceQualifer class.
            mUiMap.put(CountryCodeQualifier.class, new MCCEdit(mQualifierEditParent));
            mUiMap.put(NetworkCodeQualifier.class, new MNCEdit(mQualifierEditParent));
            mUiMap.put(LanguageQualifier.class, new LanguageEdit(mQualifierEditParent));
            mUiMap.put(RegionQualifier.class, new RegionEdit(mQualifierEditParent));
            mUiMap.put(ScreenSizeQualifier.class, new ScreenSizeEdit(mQualifierEditParent));
            mUiMap.put(ScreenRatioQualifier.class, new ScreenRatioEdit(mQualifierEditParent));
            mUiMap.put(ScreenOrientationQualifier.class, new OrientationEdit(mQualifierEditParent));
            mUiMap.put(DockModeQualifier.class, new DockModeEdit(mQualifierEditParent));
            mUiMap.put(NightModeQualifier.class, new NightModeEdit(mQualifierEditParent));
            mUiMap.put(PixelDensityQualifier.class, new PixelDensityEdit(mQualifierEditParent));
            mUiMap.put(TouchScreenQualifier.class, new TouchEdit(mQualifierEditParent));
            mUiMap.put(KeyboardStateQualifier.class, new KeyboardEdit(mQualifierEditParent));
            mUiMap.put(TextInputMethodQualifier.class, new TextInputEdit(mQualifierEditParent));
            mUiMap.put(NavigationStateQualifier.class,
                    new NavigationStateEdit(mQualifierEditParent));
            mUiMap.put(NavigationMethodQualifier.class, new NavigationEdit(mQualifierEditParent));
            mUiMap.put(ScreenDimensionQualifier.class,
                    new ScreenDimensionEdit(mQualifierEditParent));
            mUiMap.put(VersionQualifier.class, new VersionEdit(mQualifierEditParent));
        }
}

/**
//Synthetic comment -- @@ -434,12 +454,20 @@
* @param config The configuration.
*/
public void setConfiguration(FolderConfiguration config) {

        if (mMode != SelectorMode.CONFIG_ONLY) {
            mSelectedConfiguration.set(config, true /*nonFakeValuesOnly*/);

            // create the base config, which is the default config minus the qualifiers
            // in SelectedConfiguration
            mBaseConfiguration.createDefault();
            mBaseConfiguration.substract(mSelectedConfiguration);
        } else {
            // set the base config to the edited config.
            mBaseConfiguration.set(config, true /*nonFakeValuesOnly*/);
        }

        mSelectionTableViewer.refresh();
mFullTableViewer.refresh();
}

//Synthetic comment -- @@ -537,9 +565,9 @@
private void fillCombo(Combo combo, ResourceEnum[] resEnums) {
for (ResourceEnum resEnum : resEnums) {
// only add the enum if:
            // not in device mode OR (device mode is true and) it's a valid device value.
// Also, always ignore fake values.
            if ((mMode == SelectorMode.DEFAULT || resEnum.isValidValueForDevice()) &&
resEnum.isFakeValue() == false) {
combo.add(resEnum.getShortDisplayValue());
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java
//Synthetic comment -- index 7837d66..9ed1d79 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk.TargetChangeListener;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector.ConfigurationState;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector.SelectorMode;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -618,7 +619,7 @@

// configuration selector
emptyCell(parent);
        mConfigSelector = new ConfigurationSelector(parent, SelectorMode.DEFAULT);
GridData gd = newGridData(2, GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
gd.widthHint = ConfigurationSelector.WIDTH_HINT;
gd.heightHint = ConfigurationSelector.HEIGHT_HINT;







