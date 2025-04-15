/*Framework/base: Added synchronization block to avoid race condition

While removing the top activities of crashed process from the
history stack sometimes it was leading to race condition and
decrementing history stack index twice. Because of this index
out of bound issue was coming

Change-Id:I14bb5834e778c15b674248e46fe93b0ce9f37967*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityStack.java b/services/java/com/android/server/am/ActivityStack.java
//Synthetic comment -- index 4546dc3..676722f 100644

//Synthetic comment -- @@ -3623,7 +3623,9 @@
}

if (activityRemoved) {
            synchronized (mService) {
                resumeTopActivityLocked(null);
            }
}

return res;







