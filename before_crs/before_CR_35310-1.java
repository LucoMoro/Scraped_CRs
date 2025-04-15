/*Prevent to deleting image file at d&d from package explorer pane to draw9patch tool.

Eclipse send d&d event as MOVE action now. However if MOVE action is successful, the file is deleted.

Change-Id:I844880528d98c5c83e90edf64d2dffb413a92ec1*/
//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageTransferHandler.java b/draw9patch/src/com/android/draw9patch/ui/ImageTransferHandler.java
//Synthetic comment -- index f14cd77..dc7640e 100644

//Synthetic comment -- @@ -75,10 +75,11 @@
}

@Override
    public boolean canImport(JComponent component, DataFlavor[] dataFlavors) {
        for (DataFlavor flavor : dataFlavors) {
if (flavor.isFlavorJavaFileListType() || flavor.isFlavorTextType()) {
                return true;
}
}
return false;







