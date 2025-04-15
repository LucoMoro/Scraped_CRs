/*Fix logic inversion in ListView Javadoc

The Javadoc for isItemChecked, getCheckedItemPosition, and
getCheckedItemPositions stated the return was valid if conditions
were the inverse of what the method checked for.

This was pointed out in Android issue 2366*/




//Synthetic comment -- diff --git a/core/java/android/widget/ListView.java b/core/java/android/widget/ListView.java
//Synthetic comment -- index a786b3f..0a18f20 100644

//Synthetic comment -- @@ -3167,7 +3167,7 @@

/**
* Returns the checked state of the specified position. The result is only
     * valid if the choice mode has been set to {@link #CHOICE_MODE_SINGLE}
* or {@link #CHOICE_MODE_MULTIPLE}.
*
* @param position The item whose checked state to return
//Synthetic comment -- @@ -3185,7 +3185,7 @@

/**
* Returns the currently checked item. The result is only valid if the choice
     * mode has been set to {@link #CHOICE_MODE_SINGLE}.
*
* @return The position of the currently checked item or
*         {@link #INVALID_POSITION} if nothing is selected
//Synthetic comment -- @@ -3202,10 +3202,12 @@

/**
* Returns the set of checked items in the list. The result is only valid if
     * the choice mode has not been set to {@link #CHOICE_MODE_NONE}.
*
* @return  A SparseBooleanArray which will return true for each call to
     *          get(int position) where position is a position in the list,
     *          or <code>null</code> if the choice mode is set to
     *          {@link #CHOICE_MODE_NONE}.
*/
public SparseBooleanArray getCheckedItemPositions() {
if (mChoiceMode != CHOICE_MODE_NONE) {







