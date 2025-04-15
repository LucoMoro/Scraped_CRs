/*Minor layoutlib api change for the data binding project callback.

Change-Id:Ib1b39938a459627a6ecf70556bdd6a99342ddf93*/
//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java b/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java
//Synthetic comment -- index 99a1267..7368e50 100644

//Synthetic comment -- @@ -99,13 +99,14 @@
* @param adapterCookie the view cookie for this particular view.
* @param itemRef the {@link ResourceReference} for the layout used by the adapter item.
* @param fullPosition the position of the item in the full list.
     * @param typePosition the position of the item if only items of the same type are considered.
     *     If there is only one type of items, this is the same as <var>fullPosition</var>.
     * @param fullChildPosition the position of the item in the full children list. This is only
*     valid if the adapter view is an ExpandableListView.
     * @param typeChildPosition the position of the child item if only items of the same type are
     *     considered. This is only valid if the adapter view is an ExpandableListView.
     *     If there is only one type of items, this is the same as <var>fullChildPosition</var>.
* @param viewRef The {@link ResourceReference} for the view we're trying to fill.
* @param ViewAttribute the attribute being queried.
* @param defaultValue the default value for this attribute. The object class matches the
//Synthetic comment -- @@ -116,8 +117,8 @@
*/
Object getAdapterItemValue(ResourceReference adapterView, Object adapterCookie,
ResourceReference itemRef,
            int fullPosition, int typePosition,
            int fullChildPosition, int typeChildPosition,
ResourceReference viewRef, ViewAttribute viewAttribute, Object defaultValue);

/**







