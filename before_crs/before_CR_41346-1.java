/*Don't display option menu when previewing movies

This is an Android defect. The display option doesn't
take into effects when the user is previewing movies.
In this patch, the opition menu is not displayed
when previewing movies, which improves the user experiences.

Change-Id:Iceec9cdee12e54c9967d096f1ff6e6502e2da7deAuthor: Wei Feng <wei.feng@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 37176*/
//Synthetic comment -- diff --git a/src/com/android/videoeditor/VideoEditorActivity.java b/src/com/android/videoeditor/VideoEditorActivity.java
//Synthetic comment -- index 193bae3..0d5e79b 100755

//Synthetic comment -- @@ -510,9 +510,6 @@
menu.findItem(R.id.menu_item_import_image).setVisible(haveProject);
menu.findItem(R.id.menu_item_import_audio).setVisible(haveProject &&
mProject.getAudioTracks().size() == 0 && haveMediaItems);
        menu.findItem(R.id.menu_item_change_aspect_ratio).setVisible(haveProject &&
                mProject.hasMultipleAspectRatios());
        menu.findItem(R.id.menu_item_edit_project_name).setVisible(haveProject);

// Check if there is an operation pending or preview is on.
boolean enableMenu = haveProject;
//Synthetic comment -- @@ -523,6 +520,9 @@
enableMenu = !ApiService.isProjectBeingEdited(mProjectPath);
}
}

menu.findItem(R.id.menu_item_export_movie).setVisible(enableMenu && haveMediaItems);
menu.findItem(R.id.menu_item_delete_project).setVisible(enableMenu);
//Synthetic comment -- @@ -1975,6 +1975,7 @@
mAudioTrackLayout.setPlaybackInProgress(true);

mPreviewState = PREVIEW_STATE_STARTING;

// Keep the screen on during the preview.
VideoEditorActivity.this.getWindow().addFlags(
//Synthetic comment -- @@ -2094,7 +2095,7 @@
mMediaLayout.setPlaybackInProgress(false);
mAudioTrackLayout.setPlaybackInProgress(false);
mOverlayLayout.setPlaybackInProgress(false);

// Do not keep the screen on if there is no preview in progress.
VideoEditorActivity.this.getWindow().clearFlags(
WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);







