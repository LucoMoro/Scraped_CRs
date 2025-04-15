/*The phone is crashing when unknown mime content is transferred.

The content resolver does not take care of the IllegalStateException
that is thrown in getType and that needs to be fixed.

Change-Id:I3e66f1aa259ab91fb9233e1ba07faa1ab6c3f2dd*/
//Synthetic comment -- diff --git a/core/java/android/content/ContentResolver.java b/core/java/android/content/ContentResolver.java
//Synthetic comment -- index 81ff414..f261630 100644

//Synthetic comment -- @@ -216,6 +216,8 @@
return type;
} catch (RemoteException e) {
return null;
}
}








