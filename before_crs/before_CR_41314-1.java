/*Fix JAVACRASH happens when press down key by keypad in music app

Need to handle new added playlist specially, as it is a special playlist(can not be moved).

Change-Id:I6fd959e7f8f242932d2edc542658741c180cf7f7Author: b359 <b359@borqs.com>
Signed-off-by: Hongyu Zhang <hongyu.zhang@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 36628*/
//Synthetic comment -- diff --git a/src/com/android/music/TrackBrowserActivity.java b/src/com/android/music/TrackBrowserActivity.java
//Synthetic comment -- index 9f77e50..fc9b0f9 100644

//Synthetic comment -- @@ -773,8 +773,9 @@
// (onKeyDown never sees these events, since they are handled by the list)
@Override
public boolean dispatchKeyEvent(KeyEvent event) {
        if (mPlaylist != null && event.getMetaState() != 0 &&
                event.getAction() == KeyEvent.ACTION_DOWN) {
switch (event.getKeyCode()) {
case KeyEvent.KEYCODE_DPAD_UP:
moveItem(true);







