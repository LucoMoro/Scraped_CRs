/*Fix issue 12251: double click handling

Surprisingly, SWT will deliver a double click event even if you click
button 1 then 3 in rapid succession. This changeset ensures that only
a double click on button 1 warps to XML.

Change-Id:I2b470e2dea5bdb4c9240c9304650ca1c7403db84*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java
//Synthetic comment -- index 9da3599..585f354 100644

//Synthetic comment -- @@ -332,7 +332,12 @@
}

public void mouseDoubleClick(MouseEvent e) {
            mCanvas.showXml(e);
}

// --- KeyListener ---







