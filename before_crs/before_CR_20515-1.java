/*Go back in history before closing activity.

On back-key go back in history one step at a time, instead of closing
the activity immediately. This is consistent with other viewers and
makes browsing files on sdcard easier. The default back-key handler is
called at the end of the history.

Change-Id:Ic6427e0dd8b9fc2d77da83ce07dfe2a49c6199c2*/
//Synthetic comment -- diff --git a/src/com/android/htmlviewer/HTMLViewerActivity.java b/src/com/android/htmlviewer/HTMLViewerActivity.java
//Synthetic comment -- index b8a59d3..e7532df 100644

//Synthetic comment -- @@ -110,7 +110,16 @@
}
}
}
    
@Override
protected void onResume() {
super.onResume();







