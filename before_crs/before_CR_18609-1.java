/*Release reference when putting RILRequest back into the pool.

In order to reduce object creation the RILRequest objects are
stored in an array when it is unused (max 4). This avoids GC
of the object. The object in turn has references to other
objects which sometimes hold large memory chunks. This fix
releases these references since they are not used anyway.

This will make it possible to GC the Message (mResult) which
in some cases holds references to a Bitmap which sometimes
leads to OutOfMemoryException. The reference is cleared
anyway in RILRequest.obtain(...)

Change-Id:I3b895bc39b5e2f3ab7cc8297c3583ea78e0ebc77*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index a9a4be2..2f7aa21 100644

//Synthetic comment -- @@ -141,6 +141,7 @@
this.mNext = sPool;
sPool = this;
sPoolSize++;
}
}
}







