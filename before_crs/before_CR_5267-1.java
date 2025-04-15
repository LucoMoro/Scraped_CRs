/*Support power off/on with POWER button*/
//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/PhoneWindowManager.java b/phone/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index 1731206..05ea393 100644

//Synthetic comment -- @@ -1179,6 +1179,27 @@
mBroadcastWakeLock.acquire();
mHandler.post(new PassHeadsetKey(keyEvent));
}
}
}








