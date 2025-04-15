/*Fix typo in precompiler builder.

Bug: 3102564

Change-Id:I44d60fc89e91eadf55c4f5451493f33ec5e13c73*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index d5fdea8..3f4d0de 100644

//Synthetic comment -- @@ -386,7 +386,7 @@
if (codename != null) {
// integer minSdk when the target is a preview => fatal error
String msg = String.format(
                                "Platform %1$s is a preview and requires appication manifest to set %2$s to '%1$s'",
codename, AndroidManifest.ATTRIBUTE_MIN_SDK_VERSION);
AdtPlugin.printErrorToConsole(project, msg);
BaseProjectHelper.markResource(manifestFile, AndroidConstants.MARKER_ADT,
//Synthetic comment -- @@ -440,7 +440,7 @@
// Display an error
String codename = projectTarget.getVersion().getCodename();
String msg = String.format(
                        "Platform %1$s is a preview and requires appication manifests to set %2$s to '%1$s'",
codename, AndroidManifest.ATTRIBUTE_MIN_SDK_VERSION);
AdtPlugin.printErrorToConsole(project, msg);
BaseProjectHelper.markResource(manifestFile, AndroidConstants.MARKER_ADT, msg,







