/*Launcher2: Fixed the app shortcuts disappearing issue in the homescreen

This issue happens consistently after placing MarketUpdater shortcut
into a newly created folder in the homescreen workspace, then download
& finish installation of some new apps from Market, go back to
homescreen, if drag any existing app shortcuts or create a new
shortcut for any app, they cannot stay in the homescreen.

Change-Id:Ic55558465ab2893703360804ce2563c3ffe1a1ba*/




//Synthetic comment -- diff --git a/src/com/android/launcher2/Launcher.java b/src/com/android/launcher2/Launcher.java
//Synthetic comment -- index e611303..20064c1 100644

//Synthetic comment -- @@ -2126,30 +2126,6 @@
}

/**
* Implementation of the method from LauncherModel.Callbacks.
*/
public int getCurrentWorkspaceScreen() {
//Synthetic comment -- @@ -2193,8 +2169,6 @@
*/
public void bindItems(ArrayList<ItemInfo> shortcuts, int start, int end) {

final Workspace workspace = mWorkspace;

for (int i=start; i<end; i++) {
//Synthetic comment -- @@ -2232,7 +2206,7 @@
* Implementation of the method from LauncherModel.Callbacks.
*/
public void bindFolders(HashMap<Long, FolderInfo> folders) {

sFolders.clear();
sFolders.putAll(folders);
}
//Synthetic comment -- @@ -2243,7 +2217,6 @@
* Implementation of the method from LauncherModel.Callbacks.
*/
public void bindAppWidget(LauncherAppWidgetInfo item) {

final long start = DEBUG_WIDGETS ? SystemClock.uptimeMillis() : 0;
if (DEBUG_WIDGETS) {
//Synthetic comment -- @@ -2281,7 +2254,6 @@
* Implementation of the method from LauncherModel.Callbacks.
*/
public void finishBindingItems() {

if (mSavedState != null) {
if (!mWorkspace.hasFocus()) {
//Synthetic comment -- @@ -2328,7 +2300,7 @@
* Implementation of the method from LauncherModel.Callbacks.
*/
public void bindAppsAdded(ArrayList<ApplicationInfo> apps) {

removeDialog(DIALOG_CREATE_SHORTCUT);
mAllAppsGrid.addApps(apps);
}
//Synthetic comment -- @@ -2339,7 +2311,7 @@
* Implementation of the method from LauncherModel.Callbacks.
*/
public void bindAppsUpdated(ArrayList<ApplicationInfo> apps) {

removeDialog(DIALOG_CREATE_SHORTCUT);
mWorkspace.updateShortcuts(apps);
mAllAppsGrid.updateApps(apps);








//Synthetic comment -- diff --git a/src/com/android/launcher2/LauncherModel.java b/src/com/android/launcher2/LauncherModel.java
//Synthetic comment -- index b819510..566315a 100644

//Synthetic comment -- @@ -96,7 +96,7 @@
private Bitmap mDefaultIcon;

public interface Callbacks {

public int getCurrentWorkspaceScreen();
public void startBinding();
public void bindItems(ArrayList<ItemInfo> shortcuts, int start, int end);
//Synthetic comment -- @@ -349,20 +349,13 @@
// First, schedule to add these apps back in.
String[] packages = intent.getStringArrayExtra(Intent.EXTRA_CHANGED_PACKAGE_LIST);
enqueuePackageUpdated(new PackageUpdatedTask(PackageUpdatedTask.OP_ADD, packages));

            if(packages == null || packages.length ==0)
               return;
            synchronized (this) {
               mAllAppsLoaded = mWorkspaceLoaded = false;
}
            startLoader(context, false);

} else if (Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE.equals(action)) {
String[] packages = intent.getStringArrayExtra(Intent.EXTRA_CHANGED_PACKAGE_LIST);







