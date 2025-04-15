/*Delete Broken AudioManagerTest#testMuteSolo

Bug 3188260

There is not a way using the public API to determine what streams can
be muted, so remove the broken test.

Change-Id:I5ed6c5eb65ca988fbc467d3586a434a2e3924846*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioManagerTest.java b/tests/tests/media/src/android/media/cts/AudioManagerTest.java
//Synthetic comment -- index cfa9293..b4053cf 100644

//Synthetic comment -- @@ -38,7 +38,6 @@

import com.android.cts.stub.R;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -47,7 +46,6 @@

import android.app.cts.CTSResult;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.Settings;
//Synthetic comment -- @@ -87,32 +85,6 @@
@TestTargets({
@TestTargetNew(
level = TestLevel.COMPLETE,
method = "setMicrophoneMute",
args = {boolean.class}
),







