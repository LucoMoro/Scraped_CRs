/*Make ADT check for emulator-arm instead of emulator.

Change-Id:I2316f4f652b95338b4124a2ce506c259516ff573*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 6018d17..1bfe7d7 100644

//Synthetic comment -- @@ -1165,7 +1165,8 @@
// not meant to be exhaustive.
String[] filesToCheck = new String[] {
osSdkLocation + getOsRelativeAdb(),
                osSdkLocation + getOsRelativeEmulator() + SdkConstants.FN_EMULATOR_EXTENSION
};
for (String file : filesToCheck) {
if (checkFile(file) == false) {







