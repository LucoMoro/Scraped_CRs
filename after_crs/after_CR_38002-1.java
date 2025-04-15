/*gltrace: kill app before launching with tracing enabled

When killing an application, search by application package
name and not the full activity name.

Change-Id:Iea5be6d76b6e78ea68a05b893aef993099363555*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/CollectTraceAction.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/CollectTraceAction.java
//Synthetic comment -- index 1085f3f..df3794d 100644

//Synthetic comment -- @@ -171,14 +171,14 @@
}
}

    private void startActivity(IDevice device, String activity)
throws TimeoutException, AdbCommandRejectedException,
ShellCommandUnresponsiveException, IOException, InterruptedException {
        killApp(device, getAppName(activity)); // kill app if it is already running

String startAppCmd = String.format(
"am start --opengl-trace %s -a android.intent.action.MAIN -c android.intent.category.LAUNCHER", //$NON-NLS-1$
                activity);

Semaphore launchCompletionSempahore = new Semaphore(0);
StartActivityOutputReceiver receiver = new StartActivityOutputReceiver(
//Synthetic comment -- @@ -195,6 +195,12 @@
}
}

    /** Returns the application package given the fully qualified activity name of the form
     * "com.foo.package/.ActivityName" */
    private String getAppName(String activity) {
        return activity.split("/")[0];      //$NON-NLS-1$
    }

private void killApp(IDevice device, String appName) {
Client client = device.getClient(appName);
if (client != null) {







