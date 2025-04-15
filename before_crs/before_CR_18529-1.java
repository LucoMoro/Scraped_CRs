/*Remove temporary disabling of proguard during ADT export.

Change-Id:Iadff55b838b7317f9dcc518d5246a2889f7a5f39*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java
//Synthetic comment -- index 0cfd84f..cdfe3b3 100644

//Synthetic comment -- @@ -181,7 +181,7 @@
new File(project.getLocation().toFile(), SdkConstants.FD_PROGUARD));

// dx input is proguard's output
                dxInput = new String[] { inputJar/*obfuscatedJar*/.getAbsolutePath() };
} else {
// no proguard, simply get all the compiled code path: project output(s) +
// jar file(s)







