/*uiautomator: Display root cause of exception if possible.

Also increased timeout since it seems to take a while longer
on the first attempt.

Change-Id:Ib005408f428dd083cc058a8594d4f6d55a0eead6*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java
//Synthetic comment -- index ae9ab91..5f7d0dd 100644

//Synthetic comment -- @@ -550,8 +550,12 @@
}
});
} catch (Exception e) {
Status s = new Status(IStatus.ERROR, DdmsPlugin.PLUGIN_ID,
                                            "Error obtaining UI hierarchy", e);
ErrorDialog.openError(shell, "UI Automator",
"Unexpected error while obtaining UI hierarchy", s);
}








//Synthetic comment -- diff --git a/uiautomatorviewer/src/com/android/uiautomator/UiAutomatorHelper.java b/uiautomatorviewer/src/com/android/uiautomator/UiAutomatorHelper.java
//Synthetic comment -- index 6e51ad8..6011fed 100644

//Synthetic comment -- @@ -80,7 +80,7 @@
try {
device.executeShellCommand(command,
new CollectingOutputReceiver(commandCompleteLatch));
            commandCompleteLatch.await(20, TimeUnit.SECONDS);

monitor.subTask("Pull UI XML snapshot from device...");
device.getSyncService().pullFile(UIDUMP_DEVICE_PATH,







