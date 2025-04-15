/*Fix: Filename isn't displayed on titlebar when the file not exist.

Change-Id:I5c19b481d6b78b7dc302353f816f0c9f5fd2028c*/
//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/MainFrame.java b/draw9patch/src/com/android/draw9patch/ui/MainFrame.java
//Synthetic comment -- index 57f6cd9..a272a28 100644

//Synthetic comment -- @@ -45,10 +45,6 @@
public MainFrame(String path) throws HeadlessException {
super("Draw 9-patch");

        if (path != null) {
            setTitle(String.format(TITLE_FORMAT, path));
        }

buildActions();
buildMenuBar();
buildContent();
//Synthetic comment -- @@ -60,6 +56,8 @@
File file = new File(path);
BufferedImage img = GraphicsUtilities.loadCompatibleImage(file.toURI().toURL());
showImageEditor(img, file.getAbsolutePath());
} catch (Exception ex) {
showOpenFilePanel();
}







