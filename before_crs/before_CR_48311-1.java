/*Mms: Process has stopped after dragging progress

testcase 1> create a slideshow mms and play it. Tap progress bar
                continuously until play finished.
         2> create a slideshow mms and play it. Tap progress bar
                continuously when rotate the screen

We have to set the seek bar change listener to null,otherwise if
the activity stop during tap progress bar continuously, window
will leak.

Change-Id:I206d35b91e04de06c2073c3fb0d9176da6adc81cAuthor: Frederic Predon <frederic.predon@intel.com>
Signed-off-by: b533 <b533@borqs.com>
Signed-off-by: Frederic Predon <frederic.predon@intel.com>
Signed-off-by: Bin Yang <bin.y.yang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 28634*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideshowActivity.java b/src/com/android/mms/ui/SlideshowActivity.java
//Synthetic comment -- index c76b178..62d46a2 100644

//Synthetic comment -- @@ -42,6 +42,7 @@
import android.view.Window;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;

import com.android.mms.R;
import com.android.mms.dom.AttrImpl;
//Synthetic comment -- @@ -284,6 +285,11 @@
mSmilPlayer.stopWhenReload();
}
if (mMediaController != null) {
// Must do this so we don't leak a window.
mMediaController.hide();
}
//Synthetic comment -- @@ -291,6 +297,14 @@
}

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
switch (keyCode) {
case KeyEvent.KEYCODE_VOLUME_DOWN:







