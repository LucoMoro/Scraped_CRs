//<Beginning of snippet n. 0>
@Override
protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (mLauncher != null) {
        outState.putParcelable("launcher_state", mLauncher.saveState());
        outState.putString("current_folder", mLauncher.getWorkspace().getOpenFolder() != null ? mLauncher.getWorkspace().getOpenFolder().getId() : null);
    }
}

@Override
protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    if (mLauncher != null) {
        mLauncher.restoreState(savedInstanceState.getParcelable("launcher_state"));
        String currentFolderId = savedInstanceState.getString("current_folder");
        if (currentFolderId != null) {
            mLauncher.getWorkspace().openFolder(currentFolderId);
        }
    }
}

@Override
public boolean onInterceptHoverEvent(MotionEvent ev) {
    if (mLauncher == null) {
        Log.w("HoverEvent", "mLauncher is null");
        return false;
    }
    Workspace workspace = mLauncher.getWorkspace();
    if (workspace == null) {
        Log.w("HoverEvent", "Workspace is null");
        return false;
    }
    Folder currentFolder = workspace.getOpenFolder();
    if (currentFolder == null) {
        Log.w("HoverEvent", "Current folder is null");
        return false;
    } else {
        // Your existing logic for handling event with currentFolder
    }
}
//<End of snippet n. 0>