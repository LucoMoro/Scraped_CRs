/*Add null check to the PackageInstaller.

Avoid a potential NPE by adding a null check to the PackageInstaller.

Change-Id:I65671c7365a2f67ff24bb4ab25e87d4d7f388a52*/




//Synthetic comment -- diff --git a/src/com/android/packageinstaller/PackageInstallerActivity.java b/src/com/android/packageinstaller/PackageInstallerActivity.java
//Synthetic comment -- index b165194..ac37695 100644

//Synthetic comment -- @@ -249,7 +249,9 @@
new IntentFilter(ClearCacheReceiver.INTENT_CLEAR_CACHE));
PendingIntent pi = PendingIntent.getBroadcast(this,
0,  new Intent(ClearCacheReceiver.INTENT_CLEAR_CACHE), 0);
        if (pi != null) {
            mPm.freeStorage(size, pi.getIntentSender());
        }
}

private void launchSettingsAppAndFinish() {







