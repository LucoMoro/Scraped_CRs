/*Fix "Home, then sleep" behavior.

Calling stopAppSwitches() in goHome() was preventing this option from
working entirely.*/
//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/PhoneWindowManager.java b/phone/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index 8140b3d..a1df926 100644

//Synthetic comment -- @@ -1821,7 +1821,6 @@
// This code brings home to the front or, if it is already
// at the front, puts the device to sleep.
try {
                ActivityManagerNative.getDefault().stopAppSwitches();
sendCloseSystemWindows();
int result = ActivityManagerNative.getDefault()
.startActivity(null, mHomeIntent,







