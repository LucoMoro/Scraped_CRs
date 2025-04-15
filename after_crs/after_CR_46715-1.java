/*VideoModule: check for videosnapshot support in onSingleTapUp

onSingleTapUp is now triggered through PreviewGestures instead of
ActivityBase, so the check in initializeVideoSnapshot() is no
longer enough to prevent triggering video snapshot on unsupported
devices.

Change-Id:Iab0b4b3d0cd9b0ea7278eceee3ae58419f5df100*/




//Synthetic comment -- diff --git a/src/com/android/camera/VideoModule.java b/src/com/android/camera/VideoModule.java
//Synthetic comment -- index ec8a69d..0a1e4c5 100644

//Synthetic comment -- @@ -2486,7 +2486,8 @@
return;
}

        if (!mPaused || mSnapshotInProgress || effectsActive()
                || !Util.isVideoSnapshotSupported(mParameters)) {
return;
}








