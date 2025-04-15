/*Synchronize to 60Hz tick at begining of render frame

Currently, test case is measuring the time from start of frame rendering till
SwapBuffer ends, during this time measurement, it is including the wait time
for the 60Hz tick, which is not appropriate.
If the test case intention is to limt 60fps, then it would be more appropriate
to synchronize to 60Hz tick at the beginning of renderframe instead of doing
in the middle.

Fix tested on tag : android-cts-4.0.3_r3*/




//Synthetic comment -- diff --git a/tests/tests/textureview/src/android/textureview/cts/GLTextureUploadRenderer.java b/tests/tests/textureview/src/android/textureview/cts/GLTextureUploadRenderer.java
//Synthetic comment -- index 6d86540..20ce133 100644

//Synthetic comment -- @@ -291,7 +291,6 @@

mScrollOffset += 16;
mFramesRendered++;
}

void GLCHK() {







