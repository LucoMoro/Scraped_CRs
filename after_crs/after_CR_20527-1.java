/*Display AIDL cmd line in ADT Build Verbose mode.

Change-Id:I9d9091baac364671f0698becd33c87fba41edc7d*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index dc3bf9e..11712bf 100644

//Synthetic comment -- @@ -1022,6 +1022,16 @@
private boolean execAidl(String[] command, IFile file) {
// do the exec
try {
            if (AdtPrefs.getPrefs().getBuildVerbosity() == BuildVerbosity.VERBOSE) {
                StringBuilder sb = new StringBuilder();
                for (String c : command) {
                    sb.append(c);
                    sb.append(' ');
                }
                String cmd_line = sb.toString();
                AdtPlugin.printToConsole(getProject(), cmd_line);
            }

Process p = Runtime.getRuntime().exec(command);

// list to store each line of stderr







