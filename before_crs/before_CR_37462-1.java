/*ADT: NPE when capturing emulator output.

This can happen when starting an emulator from
the AVD selector (or the AVD Manager window.)

Change-Id:I6f5098bddc0fa54d89c164e98c51cd80509bbaf7*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 72488a3..3a76b2a 100644

//Synthetic comment -- @@ -1091,7 +1091,7 @@
final ProgressTask progress = new ProgressTask(mTable.getShell(),
"Starting Android Emulator");
progress.start(new ITask() {
                ITaskMonitor mMonitor = null;

@Override
public void run(final ITaskMonitor monitor) {
//Synthetic comment -- @@ -1115,16 +1115,12 @@
new IProcessOutput() {
@Override
public void out(@Nullable String line) {
                                        if (line != null) {
                                            filterStdOut(line);
                                        }
}

@Override
public void err(@Nullable String line) {
                                        if (line != null) {
                                            filterStdErr(line);
                                        }
}
});

//Synthetic comment -- @@ -1150,6 +1146,11 @@
}

private void filterStdOut(String line) {
// Skip some non-useful messages.
if (line.indexOf("NSQuickDrawView") != -1) { //$NON-NLS-1$
// Discard the MacOS warning:
//Synthetic comment -- @@ -1162,22 +1163,27 @@
if (line.toLowerCase().indexOf("error") != -1 ||                //$NON-NLS-1$
line.indexOf("qemu: fatal") != -1) {                    //$NON-NLS-1$
// Sometimes the emulator seems to output errors on stdout. Catch these.
                        mMonitor.logError("%1$s", line);                            //$NON-NLS-1$
return;
}

                    mMonitor.log("%1$s", line);                                     //$NON-NLS-1$
}

private void filterStdErr(String line) {
                    if (line.indexOf("emulator: device") != -1 ||                   //$NON-NLS-1$
                            line.indexOf("HAX is working") != -1) {                 //$NON-NLS-1$
                        // These are not errors. Output them as regular stdout messages.
                        mMonitor.log("%1$s", line);                                 //$NON-NLS-1$
return;
}

                    mMonitor.logError("%1$s", line);                                //$NON-NLS-1$
}
});
}







