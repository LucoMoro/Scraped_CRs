/*Prevent system crash when OOM in Binder thread.

When handling large images during an update of a widget,
we can run out of memory in the Binder thread. This will
cause an OutOfMemoryError to be thrown. When an Error is
thrown in the Binder thread, the entire system will crash.
This was fixed by catching this OutOfMemoryError and instead
throw a RuntimeException to keep the system alive.

Change-Id:If27199676c6f8aef23fb249be1197ca5dfe0fe99*/




//Synthetic comment -- diff --git a/core/java/android/os/Binder.java b/core/java/android/os/Binder.java
//Synthetic comment -- index c9df567..1db8f68 100644

//Synthetic comment -- @@ -292,6 +292,10 @@
} catch (RuntimeException e) {
reply.writeException(e);
res = true;
        } catch (OutOfMemoryError e) {
            RuntimeException re = new RuntimeException("Out of memory", e);
            reply.writeException(re);
            res = true;
}
reply.recycle();
data.recycle();







