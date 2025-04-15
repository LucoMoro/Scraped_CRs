/*Volume Overlay Bug When swapping streams

Merged inhttp://review.cyanogenmod.com/#change,14109Fixing a bug that when an app is dismissed or loaded that changes
the current active audio stream, if the volume overlay is visible during the swap,
if you adjust the volume (with hard keys) after the stream has changed while
the panel is still visible, the wrong panel (view) is still visible on-top of the
one that is being adjusted.

A good way to replicate this is to
   Open the Phone APP
   Go to the Keypad Screen
   Adjust the volume (not the icon on the overlay)
   While the volume is visible Hit the HOME hard key
   Before the volume dissapears, adjust the volume with hard keys

You will hear and feel the volume adjusting, but the overlay will not update.

Change-Id:Ied50ed83b153234cff82c282e3fd76ed671b420b*/




//Synthetic comment -- diff --git a/core/java/android/view/VolumePanel.java b/core/java/android/view/VolumePanel.java
//Synthetic comment -- index 48fe0df..1072953 100644

//Synthetic comment -- @@ -400,7 +400,10 @@
if (LOGD) Log.d(TAG, "onVolumeChanged(streamType: " + streamType + ", flags: " + flags + ")");

if ((flags & AudioManager.FLAG_SHOW_UI) != 0) {
                // If the activePanel is none - or - the one we are updating is not the current active panel
                // then it is likely that the audio stream being updated has been swapped by an app
                // we need to reorder the sliders to bring the new active one to the front
            if (mActiveStreamType == -1 || streamType != mActiveStreamType) {
reorderSliders(streamType);
}
onShowVolumeChanged(streamType, flags);







