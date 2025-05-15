
//<Beginning of snippet n. 0>



@Override
public boolean onInterceptHoverEvent(MotionEvent ev) {
        if ( mLauncher == null) {
             return false;
        }
        Workspace workspace = mLauncher.getWorkspace();
        if (workspace == null) {
             return false;
        }
        Folder currentFolder = workspace.getOpenFolder();
if (currentFolder == null) {
return false;
} else {

//<End of snippet n. 0>








