/*Improve the choice of default configuration

When you open a new layout, the layout editor needs to pick an initial
configuration for the layout.  Currently, the choice of layout is
based on the project API level (such that it picks tablet screen and
landscape orientation for API 11, and phone and portrait for lower
APIs).

This changeset adds another factor: the currently used configuration.
If you have another layout visible and you open a new layout, then the
configuration for the current layout will be used for the new layout
(if it is a compatible match).

Change-Id:I66302e7ffb13c9b66e6fd0f7f347ebf4df142f77*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index d304303..dcc94a9 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import com.android.ide.common.resources.configuration.VersionQualifier;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
//Synthetic comment -- @@ -74,6 +75,11 @@
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import java.util.ArrayList;
import java.util.Collections;
//Synthetic comment -- @@ -863,6 +869,8 @@
// add the night mode to the bundle combinations.
addNightModeToBundles(configBundles);

        addRenderTargetToBundles(configBundles);

for (LayoutDevice device : mDeviceList) {
for (DeviceConfig config : device.getConfigs()) {

//Synthetic comment -- @@ -1045,10 +1053,47 @@
Collections.sort(matches, new PhoneConfigComparator());
}

        // Look at the currently active editor to see if it's a layout editor, and if so,
        // look up its configuration and if the configuration is in our match list,
        // use it. This means we "preserve" the current configuration when you open
        // new layouts.
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
        IEditorPart activeEditor = page.getActiveEditor();
        if (activeEditor instanceof LayoutEditor
                && mEditedFile != null
                // (Only do this when the two files are in the same project)
                && ((LayoutEditor) activeEditor).getProject() == mEditedFile.getProject()) {
            LayoutEditor editor = (LayoutEditor) activeEditor;
            FolderConfiguration configuration = editor.getGraphicalEditor().getConfiguration();
            if (configuration != null) {
                for (ConfigMatch match : matches) {
                    if (configuration.equals(match.testConfig)) {
                        return match;
                    }
                }
            }
        }

// the list has been sorted so that the first item is the best config
return matches.get(0);
}

    private void addRenderTargetToBundles(List<ConfigBundle> configBundles) {
        Pair<ResourceQualifier[], IAndroidTarget> state = loadRenderState();
        if (state != null) {
            IAndroidTarget target = state.getSecond();
            if (target != null) {
                int apiLevel = target.getVersion().getApiLevel();
                for (ConfigBundle bundle : configBundles) {
                    bundle.config.setVersionQualifier(
                            new VersionQualifier(apiLevel));
                }
            }
        }
    }

private void addDockModeToBundles(List<ConfigBundle> addConfig) {
ArrayList<ConfigBundle> list = new ArrayList<ConfigBundle>();

//Synthetic comment -- @@ -1282,16 +1327,16 @@
SortedSet<String> regions = projectRes.getRegions(language);
for (String region : regions) {
mLocaleCombo.add(
                                String.format("%1$s / %2$s", language, region));
RegionQualifier regionQual = new RegionQualifier(region);
mLocaleList.add(new ResourceQualifier[] { langQual, regionQual });
}

// now the entry for the other regions the language alone
if (regions.size() > 0) {
                        mLocaleCombo.add(String.format("%1$s / Other", language));
} else {
                        mLocaleCombo.add(String.format("%1$s / Any", language));
}
// create a region qualifier that will never be matched by qualified resources.
mLocaleList.add(new ResourceQualifier[] {







