/*Fc when rotating
In my phone, when rotating, the workspace is null in onInterceptHoverEvent.

Change-Id:I335e3b7eb7e170e84f5dccbc42d3adccd4fd5376*/




//Synthetic comment -- diff --git a/src/com/android/launcher2/DragLayer.java b/src/com/android/launcher2/DragLayer.java
//Synthetic comment -- index 4be1914..2d40ab1 100644

//Synthetic comment -- @@ -169,7 +169,14 @@

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







