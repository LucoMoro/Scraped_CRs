/*Fix potential NPE when closing Eclipse/ADT

Seems like it was trying to finish a build and closing
the plugin ADT at the same time. How's that even possible?
In any case I've never seen that before and I was in
debug mode, so I bet it's a rare race condition.

SDK Bug 3222178

Change-Id:I28a9bdfcbe56e2a6b73eafb5cadea611c5177f7f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 5047e11..088c9ef 100644

//Synthetic comment -- @@ -213,8 +213,13 @@
throws ApkCreationException, KeytoolException, AndroidLocationException,
NativeLibInJarException, DuplicateFileException, CoreException {

// get the debug keystore to use.
        IPreferenceStore store = AdtPlugin.getDefault().getPreferenceStore();
String keystoreOsPath = store.getString(AdtPrefs.PREFS_CUSTOM_DEBUG_KEYSTORE);
if (keystoreOsPath == null || new File(keystoreOsPath).isFile() == false) {
keystoreOsPath = DebugKeyProvider.getDefaultKeyStoreOsPath();







