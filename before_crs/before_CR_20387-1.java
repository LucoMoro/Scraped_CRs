/*Make it possible to override the SDK path using an env var

The value will only be used if it matches an existing directory.
Useful for testing from Eclipse.

Note: this is a hack that does not change the preferences, so if the user
looks at Window > Preferences > Android, the path will be the preferences
one and not the overriden one.

Change-Id:I70fb1a7117f56d4c6e5cab253c3323480605413f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AdtPrefs.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AdtPrefs.java
//Synthetic comment -- index de5847b..19f91de 100644

//Synthetic comment -- @@ -117,6 +117,17 @@
if (property == null || PREFS_SDK_DIR.equals(property)) {
mOsSdkLocation = mStore.getString(PREFS_SDK_DIR);

// make sure it ends with a separator. Normally this is done when the preference
// is set. But to make sure older version still work, we fix it here as well.
if (mOsSdkLocation.length() > 0 && mOsSdkLocation.endsWith(File.separator) == false) {







