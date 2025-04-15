/*Clean Up VideoViewTest

Remove testOnKeyDown since it fails in batch mode. Remove empty
tests that were cluttering up the file.

Change-Id:I11b1b2e518194d0732732c013acfadeae10a8f07*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/VideoViewTest.java b/tests/tests/widget/src/android/widget/cts/VideoViewTest.java
//Synthetic comment -- index 6b9aa84..44d48a5 100644

//Synthetic comment -- @@ -31,7 +31,6 @@
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View.MeasureSpec;
import android.view.animation.cts.DelayedCheck;
import android.widget.MediaController;
//Synthetic comment -- @@ -310,89 +309,6 @@
}

@TestTargetNew(
level = TestLevel.COMPLETE,
method = "getDuration",
args = {}







