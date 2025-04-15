/*Fix a runtime exception caused by ArrayList to String conversion.

Change-Id:I08a8afe647ade3ffd79fde7652bb9de658e8a924*/
//Synthetic comment -- diff --git a/src/com/android/settings/TetherSettings.java b/src/com/android/settings/TetherSettings.java
//Synthetic comment -- index b924571..609a16a 100644

//Synthetic comment -- @@ -157,8 +157,9 @@
ConnectivityManager.EXTRA_ACTIVE_TETHER);
ArrayList<String> errored = intent.getStringArrayListExtra(
ConnectivityManager.EXTRA_ERRORED_TETHER);
                updateState((String[]) available.toArray(), (String[]) active.toArray(),
                        (String[]) errored.toArray());
} else if (intent.getAction().equals(Intent.ACTION_MEDIA_SHARED) ||
intent.getAction().equals(Intent.ACTION_MEDIA_UNSHARED)) {
updateState();







