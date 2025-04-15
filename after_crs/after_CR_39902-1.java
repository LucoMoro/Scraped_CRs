/*Fix camera crash during live effects chose

If user choose "Your video" in effects menu Camera app
will crash in background  - because in fact effet is
"active", but mEffectsRecorder is not created.

Change-Id:Ibc84335be324ec7c9c1b4c71b51a36dd7bdd6f9fSigned-off-by: Andriy Chepurnyy <x0155536@ti.com>*/




//Synthetic comment -- diff --git a/src/com/android/camera/VideoCamera.java b/src/com/android/camera/VideoCamera.java
//Synthetic comment -- index dc03217..07e7b2e 100755

//Synthetic comment -- @@ -947,7 +947,7 @@
mCameraScreenNail.releaseSurfaceTexture();
mSurfaceTexture = null;
}
        if (effectsActive() && mEffectsRecorder != null) {
// If the effects are active, make sure we tell the graph that the
// surfacetexture is not valid anymore. Disconnect the graph from the
// display.







