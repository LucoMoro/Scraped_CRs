/*Adding function: copying 9-patch pixels from other 9-patched file.

Change-Id:Id786eb42c9443f03c8ee8e0af6960f16d88a250e*/
//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index e0aa026..366cded 100644

//Synthetic comment -- @@ -349,6 +349,43 @@
name = name.substring(0, name.lastIndexOf('.')) + ".9.png";
}

File chooseSaveFile() {
if (is9Patch) {
return new File(name);








//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/MainFrame.java b/draw9patch/src/com/android/draw9patch/ui/MainFrame.java
//Synthetic comment -- index 57f6cd9..69778c6 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.draw9patch.ui.action.ExitAction;
import com.android.draw9patch.ui.action.OpenAction;
import com.android.draw9patch.ui.action.SaveAction;
import com.android.draw9patch.graphics.GraphicsUtilities;

//Synthetic comment -- @@ -37,6 +38,7 @@

public class MainFrame extends JFrame {
private ActionMap actionsMap;
private JMenuItem saveMenuItem;
private ImageEditorPanel imageEditor;

//Synthetic comment -- @@ -72,6 +74,7 @@
private void buildActions() {
actionsMap = new ActionMap();
actionsMap.put(OpenAction.ACTION_NAME, new OpenAction(this));
actionsMap.put(SaveAction.ACTION_NAME, new SaveAction(this));
actionsMap.put(ExitAction.ACTION_NAME, new ExitAction(this));
}
//Synthetic comment -- @@ -79,12 +82,17 @@
private void buildMenuBar() {
JMenu fileMenu = new JMenu("File");
JMenuItem openMenuItem = new JMenuItem();
saveMenuItem = new JMenuItem();
JMenuItem exitMenuItem = new JMenuItem();

openMenuItem.setAction(actionsMap.get(OpenAction.ACTION_NAME));
fileMenu.add(openMenuItem);

saveMenuItem.setAction(actionsMap.get(SaveAction.ACTION_NAME));
saveMenuItem.setEnabled(false);
fileMenu.add(saveMenuItem);
//Synthetic comment -- @@ -120,6 +128,21 @@
}
}

void showImageEditor(BufferedImage image, String name) {
getContentPane().removeAll();
imageEditor = new ImageEditorPanel(this, image, name);
//Synthetic comment -- @@ -129,6 +152,10 @@
repaint();
}

public SwingWorker<?, ?> save() {
if (imageEditor == null) {
return null;
//Synthetic comment -- @@ -170,6 +197,7 @@
protected void done() {
try {
showImageEditor(get(), file.getAbsolutePath());
setTitle(String.format(TITLE_FORMAT, file.getAbsolutePath()));
} catch (InterruptedException e) {
e.printStackTrace();
//Synthetic comment -- @@ -178,4 +206,27 @@
}
}
}
}








//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/action/CopyFromAction.java b/draw9patch/src/com/android/draw9patch/ui/action/CopyFromAction.java
new file mode 100644
//Synthetic comment -- index 0000000..2f5dbc1

//Synthetic comment -- @@ -0,0 +1,43 @@







