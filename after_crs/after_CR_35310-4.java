/*Prevent to deleting original image file at d&d from Eclipse to
draw9patch tool on Mac OS X.

Issue:http://code.google.com/p/android/issues/detail?id=28978Change-Id:I844880528d98c5c83e90edf64d2dffb413a92ec1*/




//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageTransferHandler.java b/draw9patch/src/com/android/draw9patch/ui/ImageTransferHandler.java
//Synthetic comment -- index f14cd77..bb1cdb3 100644

//Synthetic comment -- @@ -75,9 +75,15 @@
}

@Override
    public boolean canImport(TransferSupport support) {
        boolean isCopySupported
                = (COPY & support.getSourceDropActions()) == COPY;
        if (!isCopySupported) {
            return false;
        }
        for (DataFlavor flavor : support.getDataFlavors()) {
if (flavor.isFlavorJavaFileListType() || flavor.isFlavorTextType()) {
                support.setDropAction(COPY);
return true;
}
}







