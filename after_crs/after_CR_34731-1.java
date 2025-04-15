/*Prompt for package uninstall when signature differs

If a package with a different signature is already installed on the
launch device, the current behaviour is to simply abort the launch
process and print an error message to the console. The developer has
to use adb to actually do the uninstall.

This patch prompts the user if he wants to uninstall the current
package directly from within Eclipse.

Fixes #22480.

Change-Id:I8b32d19ebba511cace389696848ad9da411011bd*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index d7d87cb..e39f149 100644

//Synthetic comment -- @@ -1131,7 +1131,17 @@
String.format("Installation failed: Could not copy %1$s to its final location!",
launchInfo.getPackageFile().getName()),
"Please check logcat output for more details.");
        } else if (result.equals("INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES")) { //$NON-NLS-1$
            if (retryMode != InstallRetryMode.NEVER) {
                boolean prompt = AdtPlugin.displayPrompt("Application Install",
                                "Re-installation failed due to different application signatures. You must perform a full uninstall of the application. WARNING: This will remove the application data!\nDo you want to uninstall?");
                if (prompt) {
                    doUninstall(device, launchInfo);
                    String res = doInstall(launchInfo, remotePath, device, false);
                    return checkInstallResult(res, device, launchInfo, remotePath,
                            InstallRetryMode.NEVER);
                }
            }
AdtPlugin.printErrorToConsole(launchInfo.getProject(),
"Re-installation failed due to different application signatures.",
"You must perform a full uninstall of the application. WARNING: This will remove the application data!",
//Synthetic comment -- @@ -1152,7 +1162,6 @@
* @return a {@link String} with an error code, or <code>null</code> if success.
* @throws InstallException if the installation failed.
*/
private String doUninstall(IDevice device, DelayedLaunchInfo launchInfo)
throws InstallException {
try {







