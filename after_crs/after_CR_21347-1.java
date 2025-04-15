/*Fixing self-assignment in copying code.

Change-Id:I0799f83dc9d02b9bc31c76e747e6ba1ff58000b0*/




//Synthetic comment -- diff --git a/core/java/android/view/MotionEvent.java b/core/java/android/view/MotionEvent.java
//Synthetic comment -- index c2fec96..952face 100644

//Synthetic comment -- @@ -620,7 +620,7 @@
ev.mEdgeFlags = o.mEdgeFlags;
ev.mDownTimeNano = o.mDownTimeNano;
ev.mAction = o.mAction;
        ev.mFlags = o.mFlags;
ev.mMetaState = o.mMetaState;
ev.mXOffset = o.mXOffset;
ev.mYOffset = o.mYOffset;







