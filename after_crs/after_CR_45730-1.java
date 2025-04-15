/*Possible NPE in ChooseAccountTypeActivity

Change-Id:I8d36717cc9b74f62ac90eae22225424f57fb9d05*/




//Synthetic comment -- diff --git a/core/java/android/accounts/ChooseAccountTypeActivity.java b/core/java/android/accounts/ChooseAccountTypeActivity.java
//Synthetic comment -- index acc8549..5b3b553 100644

//Synthetic comment -- @@ -134,7 +134,6 @@
if (sequence != null) {
name = sequence.toString();
}
} catch (PackageManager.NameNotFoundException e) {
// Nothing we can do much here, just log
if (Log.isLoggable(TAG, Log.WARN)) {







