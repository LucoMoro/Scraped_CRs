/*Remove Battery config option if has_battery boolean is not set.

Change-Id:Id91a457a9fc0962b8b7e4af306d085eacce993ea*/
//Synthetic comment -- diff --git a/src/com/android/settings/Settings.java b/src/com/android/settings/Settings.java
//Synthetic comment -- index ebbec5dd..028dc7f 100644

//Synthetic comment -- @@ -379,7 +379,10 @@
Header header = target.get(i);
// Ids are integers, so downcasting
int id = (int) header.id;
            if (id == R.id.dock_settings) {
if (!needsDockSettings())
target.remove(header);
} else if (id == R.id.operator_settings || id == R.id.manufacturer_settings) {
//Synthetic comment -- @@ -499,6 +502,10 @@
return getResources().getBoolean(R.bool.has_dock_settings);
}

private void getMetaData() {
try {
ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(),







