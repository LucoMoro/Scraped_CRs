/*Return false on RemoteException

Change-Id:I0e237089f796505a7bc88dfd86355ead7ad7e996*/




//Synthetic comment -- diff --git a/core/java/android/app/ApplicationContext.java b/core/java/android/app/ApplicationContext.java
//Synthetic comment -- index 032a74b..a5047c1 100644

//Synthetic comment -- @@ -2496,8 +2496,9 @@
try {
return mPM.isPackageInstalled(packageName);
} catch (RemoteException e) {
                // Should never happen!
}
            return false;
}

private final ApplicationContext mContext;







