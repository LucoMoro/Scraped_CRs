/*replaced deprecated getIntent with parseURI

Change-Id:Iabf82ff0f9be2a76dece9aafe8603bf83ac7d049*/




//Synthetic comment -- diff --git a/src/com/android/settings/quicklaunch/QuickLaunchSettings.java b/src/com/android/settings/quicklaunch/QuickLaunchSettings.java
//Synthetic comment -- index ca5d3ab..2d2b01c 100644

//Synthetic comment -- @@ -204,7 +204,7 @@
return true;
}

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

// Open the clear shortcut dialog
Preference pref = (Preference) getPreferenceScreen().getRootAdapter().getItem(position);
//Synthetic comment -- @@ -314,7 +314,7 @@
String intentUri = c.getString(intentColumn);
PackageManager packageManager = getPackageManager();
try {
                Intent intent = Intent.parseUri(intentUri, 0);
ResolveInfo info = packageManager.resolveActivity(intent, 0);
if (info != null) {
title = info.loadLabel(packageManager);







