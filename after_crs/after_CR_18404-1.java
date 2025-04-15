/*Connect the resolution chooser to the zoom control in GLE

When using the Real-Life zoom control the first time, it
will display the Resolution chooser (from the AVD start dialog)
to let the user figure out their monitor dpi.

Next Step: add the actual density to a prefs panel so that it
can be manually tweaked.

Change-Id:If2322dd077b657386ab0452f9c507cb3c0730c32*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigManagerDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigManagerDialog.java
//Synthetic comment -- index 6c47150..20aacba 100644

//Synthetic comment -- @@ -458,7 +458,6 @@
* Returns a {@link DeviceSelection} object representing the selected path in the
* {@link TreeViewer}
*/
private DeviceSelection getSelection() {
// get the selection paths
TreeSelection selection = (TreeSelection)mTreeViewer.getSelection();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 2a78deb..4b3a3f3 100755

//Synthetic comment -- @@ -33,6 +33,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
//Synthetic comment -- @@ -52,6 +53,7 @@
import com.android.layoutlib.api.IXmlPullParser;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.widgets.ResolutionChooserDialog;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -76,6 +78,7 @@
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
//Synthetic comment -- @@ -251,10 +254,13 @@
) {
@Override
public void onSelected(boolean newState) {
                            if (rescaleToReal(newState)) {
                                mZoomOutButton.setEnabled(!newState);
                                mZoomResetButton.setEnabled(!newState);
                                mZoomInButton.setEnabled(!newState);
                            } else {
                                mZoomRealSizeButton.setSelection(!newState);
                            }
}
},
mZoomOutButton = new CustomButton(
//Synthetic comment -- @@ -396,23 +402,35 @@
mCanvasViewer.getCanvas().setScale(1, true /*redraw*/);
}

    private boolean rescaleToReal(boolean real) {
if (real) {
            return computeAndSetRealScale(true /*redraw*/);
} else {
// reset the scale to 100%
mCanvasViewer.getCanvas().setScale(1, true /*redraw*/);
            return true;
}
}

    private boolean computeAndSetRealScale(boolean redraw) {
// compute average dpi of X and Y
float dpi = (mConfigComposite.getXDpi() + mConfigComposite.getYDpi()) / 2.f;

// get the monitor dpi
        float monitor = AdtPrefs.getPrefs().getMonitorDensity();
        if (monitor == 0.f) {
            ResolutionChooserDialog dialog = new ResolutionChooserDialog(
                    mConfigComposite.getShell());
            if (dialog.open() == Window.OK) {
                monitor = dialog.getDensity();
                AdtPrefs.getPrefs().setMonitorDensity(monitor);
            } else {
                return false;
            }
        }

mCanvasViewer.getCanvas().setScale(monitor / dpi, redraw);
        return true;
}










//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AdtPrefs.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AdtPrefs.java
//Synthetic comment -- index 8f2393b..7d72fa7 100644

//Synthetic comment -- @@ -44,6 +44,8 @@

public final static String PREFS_EMU_OPTIONS = AdtPlugin.PLUGIN_ID + ".emuOptions"; //$NON-NLS-1$

    public final static String PREFS_MONITOR_DENSITY = AdtPlugin.PLUGIN_ID + ".monitorDensity"; //$NON-NLS-1$

/** singleton instance */
private final static AdtPrefs sThis = new AdtPrefs();

//Synthetic comment -- @@ -58,6 +60,7 @@

private boolean mBuildForceResResfresh = false;
private boolean mBuildForceErrorOnNativeLibInJar = true;
    private float mMonitorDensity = 0.f;

public static enum BuildVerbosity {
/** Build verbosity "Always". Those messages are always displayed, even in silent mode */
//Synthetic comment -- @@ -132,6 +135,10 @@
if (property == null || PREFS_BUILD_FORCE_ERROR_ON_NATIVELIB_IN_JAR.equals(property)) {
mBuildForceErrorOnNativeLibInJar = mStore.getBoolean(PREFS_BUILD_RES_AUTO_REFRESH);
}

        if (property == null || PREFS_MONITOR_DENSITY.equals(property)) {
            mMonitorDensity = mStore.getFloat(PREFS_MONITOR_DENSITY);
        }
}

/**
//Synthetic comment -- @@ -154,6 +161,14 @@
return mBuildForceErrorOnNativeLibInJar;
}

    public float getMonitorDensity() {
        return mMonitorDensity;
    }

    public void setMonitorDensity(float density) {
        mMonitorDensity = density;
    }

@Override
public void initializeDefaultPreferences() {
IPreferenceStore store = AdtPlugin.getDefault().getPreferenceStore();
//Synthetic comment -- @@ -165,6 +180,8 @@

store.setDefault(PREFS_HOME_PACKAGE, "android.process.acore"); //$NON-NLS-1$

        store.setDefault(PREFS_MONITOR_DENSITY, 0.f);

try {
store.setDefault(PREFS_DEFAULT_DEBUG_KEYSTORE,
DebugKeyProvider.getDefaultKeyStoreOsPath());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/ResolutionChooserDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/ResolutionChooserDialog.java
//Synthetic comment -- index 60888e6..7454437 100644

//Synthetic comment -- @@ -39,7 +39,7 @@
* After the dialog as returned, one can query {@link #getDensity()} to get the chosen monitor
* pixel density.
*/
public class ResolutionChooserDialog extends GridDialog {
public final static float[] MONITOR_SIZES = new float[] {
13.3f, 14, 15.4f, 15.6f, 17, 19, 20, 21, 24, 30,
};
//Synthetic comment -- @@ -52,14 +52,14 @@
private int mScreenSizeIndex = -1;
private int mMonitorIndex = 0;

    public ResolutionChooserDialog(Shell parentShell) {
super(parentShell, 2, false);
}

/**
* Returns the pixel density of the user-chosen monitor.
*/
    public int getDensity() {
float size = MONITOR_SIZES[mScreenSizeIndex];
Rectangle rect = mMonitors[mMonitorIndex].getBounds();








