/*Displaying a file name to window title, when a image is loaded in draw9patch tool.

Change-Id:Iad52e227696403752c6deea590b183c74624c12b*/
//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/MainFrame.java b/draw9patch/src/com/android/draw9patch/ui/MainFrame.java
//Synthetic comment -- index d5b6409..bf8ff77 100644

//Synthetic comment -- @@ -164,6 +164,7 @@
protected void done() {
try {
showImageEditor(get(), file.getAbsolutePath());
} catch (InterruptedException e) {
e.printStackTrace();
} catch (ExecutionException e) {







