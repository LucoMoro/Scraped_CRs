/*Fixed bug in creating already existing layout configs

Change-Id:If3017c1c77d21cf99a4d77e62b46fb079997596e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index fcb39fb..7948652 100644

//Synthetic comment -- @@ -863,15 +863,13 @@
String path = res.getLocation().toOSString();

File newLayoutFolder = new File(path + File.separator + folderName);
                        if (newLayoutFolder.isFile()) {
// this should not happen since aapt would have complained
// before, but if one disable the automatic build, this could
// happen.
                            String message = String.format("File 'res/%1$s' is in the way!",
folderName);

                            AdtPlugin.displayError("Layout Creation", message);

return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID, message);
} else if (newLayoutFolder.exists() == false) {
// create it.







