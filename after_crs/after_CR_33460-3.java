/*Using the same directory as loaded png file path.

Change-Id:I7bbba4491e6ebb7379b141dc85bdbdf3fc71d1e6*/




//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index 9b086f4..45c765e 100644

//Synthetic comment -- @@ -358,7 +358,7 @@
if (is9Patch) {
return new File(name);
} else {
            JFileChooser chooser = new JFileChooser(name.substring(0, name.lastIndexOf(File.separatorChar)));
chooser.setFileFilter(new PngFileFilter());
int choice = chooser.showSaveDialog(this);
if (choice == JFileChooser.APPROVE_OPTION) {







