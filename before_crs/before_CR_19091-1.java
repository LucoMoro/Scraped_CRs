/*Change HomeScreen behavior when it is loading. Now user can scroll between
    Home Screens before Home gets fully loaded.

Change-Id:I6d2bec67a71882667d2dcb1d4c5495cb41792f8e*/
//Synthetic comment -- diff --git a/src/com/android/launcher2/DragController.java b/src/com/android/launcher2/DragController.java
//Synthetic comment -- index b4f972b..a94a408 100644

//Synthetic comment -- @@ -118,6 +118,8 @@

private InputMethodManager mInputMethodManager;

/**
* Interface to receive notifications when a drag starts or stops
*/
//Synthetic comment -- @@ -149,6 +151,10 @@
mHandler = new Handler();
}

/**
* Starts a drag.
* 
//Synthetic comment -- @@ -159,6 +165,8 @@
*        {@link #DRAG_ACTION_COPY}
*/
public void startDrag(View v, DragSource source, Object dragInfo, int dragAction) {
mOriginator = v;

Bitmap b = getViewBitmap(v);
//Synthetic comment -- @@ -202,6 +210,9 @@
public void startDrag(Bitmap b, int screenX, int screenY,
int textureLeft, int textureTop, int textureWidth, int textureHeight,
DragSource source, Object dragInfo, int dragAction) {
if (PROFILE_DRAWING_DURING_DRAG) {
android.os.Debug.startMethodTracing("Launcher");
}








//Synthetic comment -- diff --git a/src/com/android/launcher2/Launcher.java b/src/com/android/launcher2/Launcher.java
//Synthetic comment -- index 2742f2f..8437094 100644

//Synthetic comment -- @@ -189,6 +189,7 @@
private SpannableStringBuilder mDefaultKeySsb = null;

private boolean mWorkspaceLoading = true;

private boolean mPaused = true;
private boolean mRestoring;
//Synthetic comment -- @@ -1168,6 +1169,13 @@
return mWorkspaceLoading || mWaitingForResult;
}

private void addItems() {
closeAllApps(true);
showAddDialog(mMenuAddInfo);
//Synthetic comment -- @@ -2103,6 +2111,13 @@
}

/**
* Refreshes the shortcuts shown on the workspace.
*
* Implementation of the method from LauncherModel.Callbacks.
//Synthetic comment -- @@ -2246,6 +2261,8 @@
}

mWorkspaceLoading = false;
}

/**








//Synthetic comment -- diff --git a/src/com/android/launcher2/LauncherModel.java b/src/com/android/launcher2/LauncherModel.java
//Synthetic comment -- index 023264d..14b62e4 100644

//Synthetic comment -- @@ -89,6 +89,7 @@

public interface Callbacks {
public int getCurrentWorkspaceScreen();
public void startBinding();
public void bindItems(ArrayList<ItemInfo> shortcuts, int start, int end);
public void bindFolders(HashMap<Long,FolderInfo> folders);
//Synthetic comment -- @@ -1030,6 +1031,10 @@
// Wait until the queue goes empty.
mHandler.post(new Runnable() {
public void run() {
if (DEBUG_LOADERS) {
Log.d(TAG, "Going to start binding widgets soon.");
}








//Synthetic comment -- diff --git a/src/com/android/launcher2/Workspace.java b/src/com/android/launcher2/Workspace.java
//Synthetic comment -- index cba1a3b..c095a04 100644

//Synthetic comment -- @@ -625,7 +625,7 @@
@Override
public boolean dispatchTouchEvent(MotionEvent ev) {
if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (mLauncher.isWorkspaceLocked() || mLauncher.isAllAppsVisible()) {
return false;
}
}
//Synthetic comment -- @@ -634,9 +634,9 @@

@Override
public boolean onInterceptTouchEvent(MotionEvent ev) {
        final boolean workspaceLocked = mLauncher.isWorkspaceLocked();
final boolean allAppsVisible = mLauncher.isAllAppsVisible();
        if (workspaceLocked || allAppsVisible) {
return false; // We don't want the events.  Let them fall through to the all apps view.
}

//Synthetic comment -- @@ -842,7 +842,7 @@
@Override
public boolean onTouchEvent(MotionEvent ev) {

        if (mLauncher.isWorkspaceLocked()) {
return false; // We don't want the events.  Let them fall through to the all apps view.
}
if (mLauncher.isAllAppsVisible()) {







