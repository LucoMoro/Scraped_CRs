/*Add Baseline utility method to layoutlib's bridge.

Change-Id:I580dad3b15de028b299a0c75e206bb31b5d6d062*/




//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java
//Synthetic comment -- index f04b266..c044353 100644

//Synthetic comment -- @@ -134,4 +134,17 @@
public Result getViewIndex(Object viewObject) {
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
        return -1;
    }
}







