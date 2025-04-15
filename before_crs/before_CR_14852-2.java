/*Replaced deprecated Inten.toURI method

Change-Id:Iff3200294edbc1cac188502219bfe481133a5d8e*/
//Synthetic comment -- diff --git a/core/java/android/app/ApplicationContext.java b/core/java/android/app/ApplicationContext.java
//Synthetic comment -- index f48f150..4c8e8ff 100644

//Synthetic comment -- @@ -1972,7 +1972,7 @@
return info.activityInfo.loadIcon(this);
}

            throw new NameNotFoundException(intent.toURI());
}

@Override public Drawable getDefaultActivityIcon() {








//Synthetic comment -- diff --git a/core/java/android/app/SearchDialog.java b/core/java/android/app/SearchDialog.java
//Synthetic comment -- index e5a769b..be4fa0e 100644

//Synthetic comment -- @@ -1336,7 +1336,7 @@
} else {
// If the intent was created from a suggestion, it will always have an explicit
// component here.
                Log.i(LOG_TAG, "Starting (as ourselves) " + intent.toURI());
getContext().startActivity(intent);
// If the search switches to a different activity,
// SearchDialogWrapper#performActivityResuming
//Synthetic comment -- @@ -1418,7 +1418,7 @@
String resultWho = null;
int requestCode = -1;
boolean onlyIfNeeded = false;
            Log.i(LOG_TAG, "Starting (uid " + uid + ", " + packageName + ") " + intent.toURI());
int result = ActivityManagerNative.getDefault().startActivityInPackage(
uid, intent, resolvedType, resultTo, resultWho, requestCode, onlyIfNeeded);
checkStartActivityResult(result, intent);








//Synthetic comment -- diff --git a/core/java/android/provider/Settings.java b/core/java/android/provider/Settings.java
//Synthetic comment -- index 69d3b77..6fbb3e9 100644

//Synthetic comment -- @@ -3837,7 +3837,7 @@
ContentValues values = new ContentValues();
if (title != null) values.put(TITLE, title);
if (folder != null) values.put(FOLDER, folder);
            values.put(INTENT, intent.toURI());
if (shortcut != 0) values.put(SHORTCUT, (int) shortcut);
values.put(ORDERING, ordering);
return cr.insert(CONTENT_URI, values);








//Synthetic comment -- diff --git a/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java b/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java
//Synthetic comment -- index 1a64e20..f8f695c 100644

//Synthetic comment -- @@ -637,7 +637,7 @@
info = packageManager.getActivityInfo(cn, 0);
intent.setComponent(cn);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    values.put(Settings.Bookmarks.INTENT, intent.toURI());
values.put(Settings.Bookmarks.TITLE,
info.loadLabel(packageManager).toString());
values.put(Settings.Bookmarks.SHORTCUT, shortcutValue);







