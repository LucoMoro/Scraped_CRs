/*isUsbMassStorageConnected() will always return true after UMS mounted even if USB disconnected, it's because mUmsEnabling will always be ture.

Change-Id:I16d3f903b911872f55896bdc7c81a08f6844e927*/
//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
//Synthetic comment -- index 6ceeb95..cb55808 100644

//Synthetic comment -- @@ -969,7 +969,7 @@

private void setUmsEnabling(boolean enable) {
synchronized (mListeners) {
            mUmsEnabling = true;
}
}








