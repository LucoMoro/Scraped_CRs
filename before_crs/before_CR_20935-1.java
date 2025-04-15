/*RingerVolumePreference.onActivityStop only stops playback

Enter the VolumeDialog in Settings, switch to another app
using long-home and then switch back to the VolmeDialog.
At this point the volume sliders no longer works. The
reason is that dialog has received onActivityStop and
unregistered all of its listeners, but the dialog can not
get any notification of that it is visible.

So, in RingerVolumePreference.onActivityStop, the cleanup
has been replaced with only stopping ringtone playback
for the seekbars. This will avoid problems when switching
between applications that previously lead to inconsistencies
in the ring volume dialog.

Change-Id:Ie44643f3a8e6bb18a8cb8bd92ac5757a3b7e15f7*/
//Synthetic comment -- diff --git a/src/com/android/settings/RingerVolumePreference.java b/src/com/android/settings/RingerVolumePreference.java
//Synthetic comment -- index 3ecd819..59ae428 100644

//Synthetic comment -- @@ -100,7 +100,11 @@
@Override
public void onActivityStop() {
super.onActivityStop();
        cleanup();
}

public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {








//Synthetic comment -- diff --git a/tests/src/com/android/settings/RingerVolumeDialogTests.java b/tests/src/com/android/settings/RingerVolumeDialogTests.java
new file mode 100644
//Synthetic comment -- index 0000000..e18d25e

//Synthetic comment -- @@ -0,0 +1,170 @@







