/*add reply.writeNoException() for quick response

In case of SET_ACTIVITY_CONTROLLER_TRANSACTION, there is no reply transaction.
So makes same to other transactions.

Change-Id:I3b9527682662a12f78cc96b6084904f18178fee7*/
//Synthetic comment -- diff --git a/core/java/android/app/ActivityManagerNative.java b/core/java/android/app/ActivityManagerNative.java
//Synthetic comment -- index 67d3930..bc7ac32 100644

//Synthetic comment -- @@ -1147,6 +1147,7 @@
IActivityController watcher = IActivityController.Stub.asInterface(
data.readStrongBinder());
setActivityController(watcher);
return true;
}








