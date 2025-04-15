/*Releasing lock even if exception is thrown.

However, the whole method needs to be reviewed. It still has several
locking/unlocking places not tied together by try-finally construct,
but they cannot be fixed without deeper understanding.

Change-Id:Iaaf87568d585327654be409377b6d57a1da135eb*/
//Synthetic comment -- diff --git a/core/java/android/view/ViewRoot.java b/core/java/android/view/ViewRoot.java
//Synthetic comment -- index c58207e..ccaef40 100644

//Synthetic comment -- @@ -1065,10 +1065,11 @@
}
}
mSurfaceHolder.mSurfaceLock.lock();
                    // Make surface invalid.
                    //mSurfaceHolder.mSurface.copyFrom(mSurface);
                    mSurfaceHolder.mSurface = new Surface();
                    mSurfaceHolder.mSurfaceLock.unlock();
}
}








