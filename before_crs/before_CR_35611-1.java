/*monitor startup: check for null sdk path.

Change-Id:I9da6a189b6b2660bd0f29752feb0e6666a5889ba*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.monitor/src/com/android/ide/eclipse/monitor/MonitorStartup.java b/eclipse/plugins/com.android.ide.eclipse.monitor/src/com/android/ide/eclipse/monitor/MonitorStartup.java
//Synthetic comment -- index 2bbe7f4..462852a 100644

//Synthetic comment -- @@ -38,8 +38,12 @@
@Override
protected IStatus run(IProgressMonitor monitor) {
SdkStatsService stats = new SdkStatsService();
                String toolsPath = new Path(MonitorPlugin.getDefault().getSdkPath())
                                                .append("tools").toString();
ping(stats, toolsPath);
return Status.OK_STATUS;
}







