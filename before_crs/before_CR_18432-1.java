/*Missing platform toosl doesn't mean the SDK is invalid.

Change-Id:I623edf5df8bcc9b0b1f806c49e863879fcd4fda4*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 801b165..082f46f 100644

//Synthetic comment -- @@ -791,7 +791,7 @@
// check that we have both the tools component and the platform-tools component.
String platformTools = osSdkLocation + SdkConstants.OS_SDK_PLATFORM_TOOLS_FOLDER;
if (checkFolder(platformTools) == false) {
            return errorHandler.handleError("SDK Platform Tools component is missing!\n" +
"Please use the SDK Manager to install it.");
}








