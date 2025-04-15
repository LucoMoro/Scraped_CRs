/*ADT/GLE2: Add callback before the rendering target changes.

Change-Id:If02fd18c39b06042615735b55190e9f03fef5b19*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/IGraphicalLayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/IGraphicalLayoutEditor.java
//Synthetic comment -- index 8ce67ea..3a22b64 100755

//Synthetic comment -- @@ -58,11 +58,6 @@
abstract void onTargetChange();

/**
     * Responds to an SDK reload/change.
     */
    abstract void onSdkChange();

    /**
* Callback for XML model changed. Only update/recompute the layout if the editor is visible
*/
abstract void onXmlModelChanged();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 3284d28..386fd5f 100644

//Synthetic comment -- @@ -114,7 +114,7 @@
private Combo mDockCombo;
private Combo mNightCombo;
private Combo mThemeCombo;
    private Combo mApiCombo;

private int mPlatformThemeCount = 0;
/** updates are disabled if > 0 */
//Synthetic comment -- @@ -142,7 +142,9 @@
/** The {@link ProjectResources} for the edited file's project */
private ProjectResources mResources;
/** The target of the project of the file being edited. */
    private IAndroidTarget mTarget;
/** The {@link FolderConfiguration} being edited. */
private FolderConfiguration mEditedConfig;

//Synthetic comment -- @@ -154,10 +156,29 @@
* be displayed.
*/
public interface IConfigListener {
void onConfigurationChange();
void onThemeChange();
void onCreate();

ProjectResources getProjectResources();
ProjectResources getFrameworkResources();
ProjectResources getFrameworkResources(IAndroidTarget target);
//Synthetic comment -- @@ -559,13 +580,13 @@
GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL));
gd.heightHint = 0;

        mApiCombo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        mApiCombo.setLayoutData(new GridData(
GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        mApiCombo.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                onApiLevelChange();
}
});
}
//Synthetic comment -- @@ -621,7 +642,8 @@
// only attempt to do anything if the SDK and targets are loaded.
LoadStatus sdkStatus = AdtPlugin.getDefault().getSdkLoadStatus();
if (sdkStatus == LoadStatus.LOADED) {
                LoadStatus targetStatus = Sdk.getCurrent().checkAndLoadTargetData(mTarget, null);

if (targetStatus == LoadStatus.LOADED) {

//Synthetic comment -- @@ -630,7 +652,7 @@
adaptConfigSelection(true /*needBestMatch*/);

// compute the final current config
                    computeCurrentConfig(true /*force*/);

// update the string showing the config value
updateConfigDisplay(mEditedConfig);
//Synthetic comment -- @@ -671,7 +693,7 @@
mSdkChanged = true;

// store the new target.
        mTarget = target;

mDisableUpdates++; // we do not want to trigger onXXXChange when setting
// new values in the widgets.
//Synthetic comment -- @@ -718,12 +740,12 @@

Sdk currentSdk = Sdk.getCurrent();
if (currentSdk != null) {
                    mTarget = currentSdk.getTarget(iProject);
}

LoadStatus targetStatus = LoadStatus.FAILED;
                if (mTarget != null) {
                    targetStatus = Sdk.getCurrent().checkAndLoadTargetData(mTarget, null);
initTargets();
}

//Synthetic comment -- @@ -737,7 +759,7 @@
mEditedConfig = resFolder.getConfiguration();
}

                    targetData = Sdk.getCurrent().getTargetData(mTarget);

// get the file stored state
boolean loadedConfigData = false;
//Synthetic comment -- @@ -779,7 +801,7 @@
updateConfigDisplay(mEditedConfig);

// compute the final current config
                    computeCurrentConfig(true /*force*/);
}
} finally {
mDisableUpdates--;
//Synthetic comment -- @@ -864,7 +886,7 @@
mLocaleCombo.select(anyLocaleIndex);

// TODO: display a better warning!
                computeCurrentConfig(false /*force*/);
AdtPlugin.printErrorToConsole(mEditedFile.getProject(),
String.format(
"'%1$s' is not a best match for any device/locale combination.",
//Synthetic comment -- @@ -989,7 +1011,7 @@
mCurrentLayoutLabel.setToolTipText(layoutLabel);
}

    private void saveState(boolean force) {
if (mDisableUpdates == 0) {
int index = mDeviceConfigCombo.getSelectionIndex();
if (index != -1) {
//Synthetic comment -- @@ -1209,20 +1231,27 @@
}

// try to reselect the previous theme.
if (mState.theme != null) {
final int count = mThemeCombo.getItemCount();
for (int i = 0 ; i < count ; i++) {
if (mState.theme.equals(mThemeCombo.getItem(i))) {
mThemeCombo.select(i);
break;
}
}
                mThemeCombo.setEnabled(true);
            } else if (mThemeCombo.getItemCount() > 0) {
                mThemeCombo.select(0);
                mThemeCombo.setEnabled(true);
            } else {
                mThemeCombo.setEnabled(false);
}

mThemeCombo.getParent().layout();
//Synthetic comment -- @@ -1347,7 +1376,7 @@
}

public IAndroidTarget getRenderingTarget() {
        int index = mApiCombo.getSelectionIndex();
if (index >= 0) {
return mTargetList.get(index);
}
//Synthetic comment -- @@ -1359,10 +1388,7 @@
* Loads the list of {@link IAndroidTarget} and inits the UI with it.
*/
private void initTargets() {
        // do we have a selection already?
        IAndroidTarget renderingTarget = getRenderingTarget();

        mApiCombo.removeAll();
mTargetList.clear();

Sdk currentSdk = Sdk.getCurrent();
//Synthetic comment -- @@ -1372,24 +1398,32 @@
for (int i = 0 ; i < targets.length; i++) {
// FIXME: support add-on rendering and check based on project minSdkVersion
if (targets[i].isPlatform()) {
                    mApiCombo.add(targets[i].getFullName());
mTargetList.add(targets[i]);

                    if (renderingTarget != null) {
                        if (renderingTarget == targets[i]) {
match = mTargetList.indexOf(targets[i]);
}
                    } else if (mTarget == targets[i]) {
match = mTargetList.indexOf(targets[i]);
}
}
}

            mApiCombo.setEnabled(mTargetList.size() > 1);
if (match == -1) {
                mApiCombo.deselectAll();
} else {
                mApiCombo.select(match);
}
}
}
//Synthetic comment -- @@ -1508,7 +1542,7 @@

fillConfigCombo(newConfigName);

        computeCurrentConfig(false /*force*/);

if (recomputeLayout) {
onDeviceConfigChange();
//Synthetic comment -- @@ -1553,7 +1587,7 @@
}

// recompute the current config
        computeCurrentConfig(false /*force*/);

// force a redraw
onDeviceChange(true /*recomputeLayout*/);
//Synthetic comment -- @@ -1655,7 +1689,7 @@
return;
}

        if (computeCurrentConfig(false /*force*/) && mListener != null) {
mListener.onConfigurationChange();
}
}
//Synthetic comment -- @@ -1670,19 +1704,19 @@
return;
}

        if (computeCurrentConfig(false /*force*/) &&  mListener != null) {
mListener.onConfigurationChange();
}
}

private void onDockChange() {
        if (computeCurrentConfig(false /*force*/) &&  mListener != null) {
mListener.onConfigurationChange();
}
}

private void onDayChange() {
        if (computeCurrentConfig(false /*force*/) &&  mListener != null) {
mListener.onConfigurationChange();
}
}
//Synthetic comment -- @@ -1690,20 +1724,33 @@
/**
* Call back for api level combo selection
*/
    private void onApiLevelChange() {
// because mApiCombo triggers onApiLevelChange at each modification, the filling
// of the combo with data will trigger notifications, and we don't want that.
if (mDisableUpdates > 0) {
return;
}

        boolean computeOk = computeCurrentConfig(false /*force*/);

// force a theme update to reflect the new rendering target.
// This must be done after computeCurrentConfig since it'll depend on the currentConfig
// to figure out the theme list.
updateThemes();

if (computeOk &&  mListener != null) {
mListener.onConfigurationChange();
}
//Synthetic comment -- @@ -1711,12 +1758,11 @@

/**
* Saves the current state and the current configuration
     * @param force forces saving the states even if updates are disabled
*
     * @see #saveState(boolean)
*/
    private boolean computeCurrentConfig(boolean force) {
        saveState(force);

if (mState.device != null) {
// get the device config from the device/config combos.
//Synthetic comment -- @@ -1752,9 +1798,9 @@
new NightModeQualifier(NightMode.getByIndex(index)));

// replace the API level by the selection of the combo
            index = mApiCombo.getSelectionIndex();
if (index == -1) {
                index = mTargetList.indexOf(mTarget);
}
if (index != -1) {
IAndroidTarget target = mTargetList.get(index);
//Synthetic comment -- @@ -1775,7 +1821,7 @@
}

private void onThemeChange() {
        saveState(false /*force*/);

int themeIndex = mThemeCombo.getSelectionIndex();
if (themeIndex != -1) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 1c5868c..7f54b3d 100755

//Synthetic comment -- @@ -48,6 +48,7 @@
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.layoutlib.api.ILayoutLog;
import com.android.layoutlib.api.IResourceValue;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneParams;
import com.android.layoutlib.api.SceneResult;
//Synthetic comment -- @@ -128,7 +129,7 @@
*
* TODO List:
* - display error icon
 * - completly rethink the property panel
*/
public class GraphicalEditorPart extends EditorPart
implements IGraphicalLayoutEditor, ISelectionListener, INullSelectionListener {
//Synthetic comment -- @@ -564,6 +565,10 @@
}
}

public Map<String, Map<String, IResourceValue>> getConfiguredFrameworkResources() {
if (mConfiguredFrameworkRes == null && mConfigComposite != null) {
ProjectResources frameworkRes = getFrameworkResources();
//Synthetic comment -- @@ -770,10 +775,18 @@
}

public void onSdkLoaded() {
            IAndroidTarget target = getRenderingTarget();
            if (target != null) {
                mConfigComposite.onSdkLoaded(target);
                mConfigListener.onConfigurationChange();
}
}

//Synthetic comment -- @@ -933,14 +946,6 @@
}
}

    public void onSdkChange() {
        IAndroidTarget target = getRenderingTarget();
        if (target != null) {
            mConfigComposite.onSdkLoaded(target);
            mConfigListener.onConfigurationChange();
        }
    }

public LayoutEditor getLayoutEditor() {
return mLayoutEditor;
}
//Synthetic comment -- @@ -1064,6 +1069,7 @@
return true;
}

/**
* Returns a {@link LayoutLibrary} that is ready for rendering, or null if the bridge
* is not available or not ready yet (due to SDK loading still being in progress etc).
//Synthetic comment -- @@ -1337,6 +1343,29 @@
return scene;
}

public Rectangle getBounds() {
return mConfigComposite.getScreenBounds();
}







