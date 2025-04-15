/*Launcher2:Fixed the app shortcuts disappearing issue

This issue happens consistently after placing MarketUpdater
shortcut into a newly created folder in the homescreen
workspace, then download & finish installation of some new
apps from Market, go back to homescreen, if drag any
existing app shortcuts or create a new shortcut for any
app, they cannot stay in the homescreen.

Change-Id:Ic55558465ab2893703360804ce2563c3ffe1a1ba*/
//Synthetic comment -- diff --git a/src/com/android/launcher2/Launcher.java b/src/com/android/launcher2/Launcher.java
//Synthetic comment -- index e611303..20064c1 100644

//Synthetic comment -- @@ -2126,30 +2126,6 @@
}

/**
     * If the activity is currently paused, signal that we need to re-run the loader
     * in onResume.
     *
     * This needs to be called from incoming places where resources might have been loaded
     * while we are paused.  That is becaues the Configuration might be wrong
     * when we're not running, and if it comes back to what it was when we
     * were paused, we are not restarted.
     *
     * Implementation of the method from LauncherModel.Callbacks.
     *
     * @return true if we are currently paused.  The caller might be able to
     * skip some work in that case since we will come back again.
     */
    public boolean setLoadOnResume() {
        if (mPaused) {
            Log.i(TAG, "setLoadOnResume");
            mOnResumeNeedsLoad = true;
            return true;
        } else {
            return false;
        }
    }

    /**
* Implementation of the method from LauncherModel.Callbacks.
*/
public int getCurrentWorkspaceScreen() {
//Synthetic comment -- @@ -2193,8 +2169,6 @@
*/
public void bindItems(ArrayList<ItemInfo> shortcuts, int start, int end) {

        setLoadOnResume();

final Workspace workspace = mWorkspace;

for (int i=start; i<end; i++) {
//Synthetic comment -- @@ -2232,7 +2206,7 @@
* Implementation of the method from LauncherModel.Callbacks.
*/
public void bindFolders(HashMap<Long, FolderInfo> folders) {
        setLoadOnResume();
sFolders.clear();
sFolders.putAll(folders);
}
//Synthetic comment -- @@ -2243,7 +2217,6 @@
* Implementation of the method from LauncherModel.Callbacks.
*/
public void bindAppWidget(LauncherAppWidgetInfo item) {
        setLoadOnResume();

final long start = DEBUG_WIDGETS ? SystemClock.uptimeMillis() : 0;
if (DEBUG_WIDGETS) {
//Synthetic comment -- @@ -2281,7 +2254,6 @@
* Implementation of the method from LauncherModel.Callbacks.
*/
public void finishBindingItems() {
        setLoadOnResume();

if (mSavedState != null) {
if (!mWorkspace.hasFocus()) {
//Synthetic comment -- @@ -2328,7 +2300,7 @@
* Implementation of the method from LauncherModel.Callbacks.
*/
public void bindAppsAdded(ArrayList<ApplicationInfo> apps) {
        setLoadOnResume();
removeDialog(DIALOG_CREATE_SHORTCUT);
mAllAppsGrid.addApps(apps);
}
//Synthetic comment -- @@ -2339,7 +2311,7 @@
* Implementation of the method from LauncherModel.Callbacks.
*/
public void bindAppsUpdated(ArrayList<ApplicationInfo> apps) {
        setLoadOnResume();
removeDialog(DIALOG_CREATE_SHORTCUT);
mWorkspace.updateShortcuts(apps);
mAllAppsGrid.updateApps(apps);








//Synthetic comment -- diff --git a/src/com/android/launcher2/LauncherModel.java b/src/com/android/launcher2/LauncherModel.java
//Synthetic comment -- index b819510..566315a 100644

//Synthetic comment -- @@ -96,7 +96,7 @@
private Bitmap mDefaultIcon;

public interface Callbacks {
        public boolean setLoadOnResume();
public int getCurrentWorkspaceScreen();
public void startBinding();
public void bindItems(ArrayList<ItemInfo> shortcuts, int start, int end);
//Synthetic comment -- @@ -349,20 +349,13 @@
// First, schedule to add these apps back in.
String[] packages = intent.getStringArrayExtra(Intent.EXTRA_CHANGED_PACKAGE_LIST);
enqueuePackageUpdated(new PackageUpdatedTask(PackageUpdatedTask.OP_ADD, packages));
            // Then, rebind everything.
            boolean runLoader = true;
            if (mCallbacks != null) {
                Callbacks callbacks = mCallbacks.get();
                if (callbacks != null) {
                    // If they're paused, we can skip loading, because they'll do it again anyway
                    if (callbacks.setLoadOnResume()) {
                        runLoader = false;
                    }
                }
}
            if (runLoader) {
                startLoader(mApp, false);
            }

} else if (Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE.equals(action)) {
String[] packages = intent.getStringArrayExtra(Intent.EXTRA_CHANGED_PACKAGE_LIST);







