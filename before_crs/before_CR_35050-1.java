/*Changing of volume control intents scheme.

The new intent was sent inside onProgressChanged() method -
after every volume seek bar moving. Intents processing is
slower then sending of new ones, so some of them could be
still not processed while playback has already started.
So, sometimes user will hear other volume level than he
has actually set.
This patch replaces all intents sent in onProgressChanged()
method during seeking by only one sent after user has
finished volume seek bar tracking.

Change-Id:I383c9fea6bc50b068b04e45adb6ef033bf7c36ffSigned-off-by: Sergii Iegorov <x0155539@ti.com>*/
//Synthetic comment -- diff --git a/src/com/android/videoeditor/widgets/AudioTrackLinearLayout.java b/src/com/android/videoeditor/widgets/AudioTrackLinearLayout.java
//Synthetic comment -- index 2f8cc0c..9b60962 100755

//Synthetic comment -- @@ -83,6 +83,7 @@
SeekBar.OnSeekBarChangeListener {
// Instance variables
private final MovieAudioTrack mAudioTrack;

/**
* Constructor
//Synthetic comment -- @@ -109,7 +110,8 @@
final SeekBar seekBar =
((SeekBar)titleBarView.findViewById(R.id.action_volume));
seekBar.setOnSeekBarChangeListener(this);
            seekBar.setProgress(mAudioTrack.getAppVolume());

return true;
}
//Synthetic comment -- @@ -147,9 +149,8 @@
@Override
public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
if (fromUser) {
                mAudioTrack.setAppVolume(progress);
                ApiService.setAudioTrackVolume(getContext(), mProject.getPath(),
                        mAudioTrack.getId(), progress);
}
}

//Synthetic comment -- @@ -159,6 +160,8 @@

@Override
public void onStopTrackingTouch(SeekBar seekBar) {
}

@Override







