/*Fix isUsbMassStorageConnected() will always return true

After UMS mounted, isUsbMassStorageConnected() will always return true even if USB is disconnected.
It's because mUmsEnabling will always be ture.

Change-Id:Ib24b2359ea2684eb0a9faeb880f383e87630e6e1*/
//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
//Synthetic comment -- index 6ceeb95..cb55808 100644

//Synthetic comment -- @@ -969,7 +969,7 @@

private void setUmsEnabling(boolean enable) {
synchronized (mListeners) {
            mUmsEnabling = true;
}
}








