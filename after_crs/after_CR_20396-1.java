/*New layoutlib API: getViewParent and getViewIndex

Change-Id:I201e42a9a7e31fcef8b7a0c5b91a6abd5d5407bd*/




//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderSession.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderSession.java
//Synthetic comment -- index 12a28de..4db766d 100644

//Synthetic comment -- @@ -149,6 +149,29 @@
}

/**
     * Returns the View parent.
     *
     * @param viewObject the object for which to return the parent.
     *
     * @return a {@link Result} indicating the status of the action, and if success, the parent
     *      object in {@link Result#getData()}
     */
    public Result getViewParent(Object viewObject) {
        return NOT_IMPLEMENTED.createResult();
    }

    /**
     * Returns the index of a given view it its parent.
     * @param viewObject the object for which to return the index.
     *
     * @return a {@link Result} indicating the status of the action, and if success, the index in
     *      the parent in {@link Result#getData()}
     */
    public Result getViewIndex(Object viewObject) {
        return NOT_IMPLEMENTED.createResult();
    }

    /**
* Inserts a new child in a ViewGroup object, and renders the result.
* <p/>
* The child is first inflated and then added to its new parent, at the given <var>index<var>







