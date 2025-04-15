/*frameworks/base: Implement Mutual exclusion in BroadcastReceiver

Invoke forceStopPackageLocked after acquiring a lock on
ActivityManagerService.this.

Change-Id:I75a85da03418a87c26b89360cebd3bccc6a25e46*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 0430c74..a388311 100644

//Synthetic comment -- @@ -5880,10 +5880,12 @@
String[] pkgs = intent.getStringArrayExtra(Intent.EXTRA_PACKAGES);
if (pkgs != null) {
for (String pkg : pkgs) {
                        if (forceStopPackageLocked(pkg, -1, false, false, false)) {
                            setResultCode(Activity.RESULT_OK);
                            return;
                        }
}
}
}







