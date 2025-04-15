/*gltrace: Add a special keyword to not launch any activities.

This is a hack to allow tracing of processes like surfaceflinger.

Change-Id:I4b542ac95487ed2488796944c98d2d317c3ee428*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/CollectTraceAction.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/CollectTraceAction.java
//Synthetic comment -- index 2b07680..93365ae 100644

//Synthetic comment -- @@ -49,6 +49,9 @@
/** Local port that is forwarded to the device's {@link #GLTRACE_UDS} socket. */
private static final int LOCAL_FORWARDED_PORT = 6039;

@Override
public void run(IAction action) {
connectToDevice();
//Synthetic comment -- @@ -84,7 +87,9 @@
}

try {
            startActivity(device, traceOptions.activityToTrace);
} catch (Exception e) {
MessageDialog.openError(shell, "Setup GL Trace",
"Error while launching application: " + e.getMessage());







