/*Don't display option menu when previewing movies

This is an Android defect. The display option doesn't
take into effects when the user is previewing movies.
In this patch, the opition menu is not displayed
when previewing movies, which improves the user experiences.

Change-Id:Ie024a60469880ac3a84cbe0c34d38d872bc30f31Author: Wei Feng <wei.feng@intel.com>
Signed-off-by: Wei Feng <wei.feng@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 37176*/




//Synthetic comment -- diff --git a/src/com/android/videoeditor/VideoEditorActivity.java b/src/com/android/videoeditor/VideoEditorActivity.java
//Synthetic comment -- index ca0b770..3eb5d9f 100755

//Synthetic comment -- @@ -511,9 +511,6 @@
menu.findItem(R.id.menu_item_import_image).setVisible(haveProject);
menu.findItem(R.id.menu_item_import_audio).setVisible(haveProject &&
mProject.getAudioTracks().size() == 0 && haveMediaItems);

// Check if there is an operation pending or preview is on.
boolean enableMenu = haveProject;
//Synthetic comment -- @@ -524,6 +521,9 @@
enableMenu = !ApiService.isProjectBeingEdited(mProjectPath);
}
}
        menu.findItem(R.id.menu_item_change_aspect_ratio).setVisible(enableMenu &&
                mProject.hasMultipleAspectRatios());
        menu.findItem(R.id.menu_item_edit_project_name).setVisible(enableMenu);

menu.findItem(R.id.menu_item_export_movie).setVisible(enableMenu && haveMediaItems);
menu.findItem(R.id.menu_item_delete_project).setVisible(enableMenu);
//Synthetic comment -- @@ -1976,6 +1976,7 @@
mAudioTrackLayout.setPlaybackInProgress(true);

mPreviewState = PREVIEW_STATE_STARTING;
            invalidateOptionsMenu();

// Keep the screen on during the preview.
VideoEditorActivity.this.getWindow().addFlags(
//Synthetic comment -- @@ -2095,7 +2096,7 @@
mMediaLayout.setPlaybackInProgress(false);
mAudioTrackLayout.setPlaybackInProgress(false);
mOverlayLayout.setPlaybackInProgress(false);
            invalidateOptionsMenu();
// Do not keep the screen on if there is no preview in progress.
VideoEditorActivity.this.getWindow().clearFlags(
WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);







