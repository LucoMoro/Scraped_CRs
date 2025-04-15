/*VolumePreference.onActivityStop only stops playback

In VolumePreference.onActivityStop, the cleanup has
been replaced with only stopping ringtone playback
for the seekbar. This will avoid problems when switching
between applications that previously lead to inconsistencies
in the ring volume dialog.

Change-Id:Ia751fe0893610c4f5650eb07705481400642a84e*/
//Synthetic comment -- diff --git a/core/java/android/preference/VolumePreference.java b/core/java/android/preference/VolumePreference.java
//Synthetic comment -- index 970d520..0219fe8 100644

//Synthetic comment -- @@ -109,7 +109,9 @@
}

public void onActivityStop() {
        cleanup();
}

/**







