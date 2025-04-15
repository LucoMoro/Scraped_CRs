/*Displaying a filename to the titlebar when draw9-patch tool is invoked with specifying the filename.

Change-Id:Icbf8c7a61d6c184df5ba46f39ca978cd55e67e93*/




//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/MainFrame.java b/draw9patch/src/com/android/draw9patch/ui/MainFrame.java
//Synthetic comment -- index bf8ff77..57f6cd9 100644

//Synthetic comment -- @@ -40,9 +40,15 @@
private JMenuItem saveMenuItem;
private ImageEditorPanel imageEditor;

    private static final String TITLE_FORMAT = "Draw 9-patch: %s";

public MainFrame(String path) throws HeadlessException {
super("Draw 9-patch");

        if (path != null) {
            setTitle(String.format(TITLE_FORMAT, path));
        }

buildActions();
buildMenuBar();
buildContent();
//Synthetic comment -- @@ -164,7 +170,7 @@
protected void done() {
try {
showImageEditor(get(), file.getAbsolutePath());
                setTitle(String.format(TITLE_FORMAT, file.getAbsolutePath()));
} catch (InterruptedException e) {
e.printStackTrace();
} catch (ExecutionException e) {







