/*Add getViewBaseline to the LayoutLibrary class.

Change-Id:I29caadc8cb31c4a3aab0863a4fec16534b9bc900*/




//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index 3294cbe..e1256fe 100644

//Synthetic comment -- @@ -354,6 +354,23 @@
return NOT_IMPLEMENTED.createResult();
}

    /**
     * Utility method returning the baseline value for a given view object. This basically returns
     * View.getBaseline().
     *
     * @param viewObject the object for which to return the index.
     *
     * @return the baseline value or -1 if not applicable to the view object or if this layout
     *     library does not implement this method.
     */
    public int getViewBaseline(Object viewObject) {
        if (mBridge != null) {
            return mBridge.getViewBaseline(viewObject);
        }

        return -1;
    }


// ------ Implementation








