/*Adding function: copying 9-patch pixels from other 9-patched file.

Change-Id:Id786eb42c9443f03c8ee8e0af6960f16d88a250e*/




//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index e0aa026..c01c643 100644

//Synthetic comment -- @@ -349,6 +349,43 @@
name = name.substring(0, name.lastIndexOf('.')) + ".9.png";
}

    void copy9patchInfo(BufferedImage fromImage) {
        int toWidth = image.getWidth();
        int toHeight = image.getHeight();
        int fromWidth = fromImage.getWidth();
        int fromHeight = fromImage.getHeight();

        int width = Math.min(toWidth, fromWidth);
        int height = Math.min(toHeight, fromHeight);

        for (int i = 0; i < width; i++) {
            int pixel = fromImage.getRGB(i, 0);
            if (pixel == 0 || pixel == 0xFF000000) {
                image.setRGB(i, 0, pixel);
            }
            pixel = fromImage.getRGB(i, fromHeight - 1);
            if (pixel == 0 || pixel == 0xFF000000) {
                image.setRGB(i, toHeight - 1, pixel);
            }
        }
        for (int i = 0; i < height; i++) {
            int pixel = fromImage.getRGB(0, i);
            if (pixel == 0 || pixel == 0xFF000000) {
                image.setRGB(0, i, pixel);
            }
            pixel = fromImage.getRGB(fromWidth - 1, i);
            if (pixel == 0 || pixel == 0xFF000000) {
                image.setRGB(toWidth - 1, i, pixel);
            }
        }

        viewer.findPatches();
        viewer.validate();
        viewer.repaint();

        stretchesViewer.computePatches();
    }

File chooseSaveFile() {
if (is9Patch) {
return new File(name);








//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/MainFrame.java b/draw9patch/src/com/android/draw9patch/ui/MainFrame.java
//Synthetic comment -- index 57f6cd9..69778c6 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.draw9patch.ui.action.ExitAction;
import com.android.draw9patch.ui.action.OpenAction;
import com.android.draw9patch.ui.action.CopyFromAction;
import com.android.draw9patch.ui.action.SaveAction;
import com.android.draw9patch.graphics.GraphicsUtilities;

//Synthetic comment -- @@ -37,6 +38,7 @@

public class MainFrame extends JFrame {
private ActionMap actionsMap;
    private JMenuItem copyFromMenuItem;
private JMenuItem saveMenuItem;
private ImageEditorPanel imageEditor;

//Synthetic comment -- @@ -72,6 +74,7 @@
private void buildActions() {
actionsMap = new ActionMap();
actionsMap.put(OpenAction.ACTION_NAME, new OpenAction(this));
        actionsMap.put(CopyFromAction.ACTION_NAME, new CopyFromAction(this));
actionsMap.put(SaveAction.ACTION_NAME, new SaveAction(this));
actionsMap.put(ExitAction.ACTION_NAME, new ExitAction(this));
}
//Synthetic comment -- @@ -79,12 +82,17 @@
private void buildMenuBar() {
JMenu fileMenu = new JMenu("File");
JMenuItem openMenuItem = new JMenuItem();
        copyFromMenuItem = new JMenuItem();
saveMenuItem = new JMenuItem();
JMenuItem exitMenuItem = new JMenuItem();

openMenuItem.setAction(actionsMap.get(OpenAction.ACTION_NAME));
fileMenu.add(openMenuItem);

        copyFromMenuItem.setAction(actionsMap.get(CopyFromAction.ACTION_NAME));
        copyFromMenuItem.setEnabled(false);
        fileMenu.add(copyFromMenuItem);

saveMenuItem.setAction(actionsMap.get(SaveAction.ACTION_NAME));
saveMenuItem.setEnabled(false);
fileMenu.add(saveMenuItem);
//Synthetic comment -- @@ -120,6 +128,21 @@
}
}

    public SwingWorker<?, ?> copy(File file) {
        if (file == null) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new NinePatchFileFilter());
            int choice = chooser.showOpenDialog(this);
            if (choice == JFileChooser.APPROVE_OPTION) {
                return new CopyTask(chooser.getSelectedFile());
            } else {
                return null;
            }
        } else {
            return new OpenTask(file);
        }
    }

void showImageEditor(BufferedImage image, String name) {
getContentPane().removeAll();
imageEditor = new ImageEditorPanel(this, image, name);
//Synthetic comment -- @@ -129,6 +152,10 @@
repaint();
}

    void copy9patchInfo(BufferedImage image, String name) {
        imageEditor.copy9patchInfo(image);
    }

public SwingWorker<?, ?> save() {
if (imageEditor == null) {
return null;
//Synthetic comment -- @@ -170,6 +197,7 @@
protected void done() {
try {
showImageEditor(get(), file.getAbsolutePath());
                copyFromMenuItem.setEnabled(true);
setTitle(String.format(TITLE_FORMAT, file.getAbsolutePath()));
} catch (InterruptedException e) {
e.printStackTrace();
//Synthetic comment -- @@ -178,4 +206,27 @@
}
}
}

    private class CopyTask extends SwingWorker<BufferedImage, Void> {
        private final File file;

        CopyTask(File file) {
            this.file = file;
        }

        protected BufferedImage doInBackground() throws Exception {
            return GraphicsUtilities.loadCompatibleImage(file.toURI().toURL());
        }

        @Override
        protected void done() {
            try {
                copy9patchInfo(get(), file.getAbsolutePath());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}








//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/action/CopyFromAction.java b/draw9patch/src/com/android/draw9patch/ui/action/CopyFromAction.java
new file mode 100644
//Synthetic comment -- index 0000000..2f5dbc1

//Synthetic comment -- @@ -0,0 +1,43 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.draw9patch.ui.action;

import com.android.draw9patch.ui.MainFrame;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class CopyFromAction extends BackgroundAction {
    public static final String ACTION_NAME = "copy";
    private MainFrame frame;

    public CopyFromAction(MainFrame frame) {
        this.frame = frame;
        putValue(NAME, "Copy 9-patch from...");
        putValue(SHORT_DESCRIPTION, "Copy from...");
        putValue(LONG_DESCRIPTION, "9-patch copy from...");
        putValue(MNEMONIC_KEY, KeyEvent.VK_C);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    public void actionPerformed(ActionEvent e) {
        executeBackgroundTask(frame.copy(null));
    }
}







