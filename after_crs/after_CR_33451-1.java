/*Real cancelable Toast

Currently Toast#cancel() just hides itself. When a lot of Toast
are created, we don't have the way of removing them.
So a user has to wait until all Toast are gone.

Change-Id:Ide14e655d71b0c4c52c84a090cb84cdaff835951*/




//Synthetic comment -- diff --git a/core/java/android/widget/Toast.java b/core/java/android/widget/Toast.java
//Synthetic comment -- index 88d7e05..d752898 100644

//Synthetic comment -- @@ -120,7 +120,17 @@
*/
public void cancel() {
mTN.hide();

        INotificationManager service = getService();
        String pkg = mContext.getPackageName();
        TN tn = mTN;

        try {
            service.cancelToast(pkg, tn);
        } catch (RemoteException e) {
            // just dump
            e.printStackTrace();
        }
}

/**







