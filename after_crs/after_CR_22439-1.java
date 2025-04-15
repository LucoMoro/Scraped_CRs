/*Fixes disappearing lines

This fixes where divider lines under the phone icon dissappear
in the Favorite tab of Contacts when Menu and Back are pressed.

Change-Id:I43de245340a3d47a887c3f4fa1e3dd9802d940e4Signed-off-by: madan ankapura <mankapur@sta.samsung.com>*/




//Synthetic comment -- diff --git a/src/com/android/contacts/ContactListItemView.java b/src/com/android/contacts/ContactListItemView.java
//Synthetic comment -- index 89e4265..f899dfd 100644

//Synthetic comment -- @@ -257,7 +257,7 @@
ensureHorizontalDivider();
mHorizontalDividerDrawable.setBounds(
0,
                    height,
width,
height);
}








//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index ac6a3a8..3662906 100644

//Synthetic comment -- @@ -863,9 +863,6 @@
mHighlightingAnimation =
new NameHighlightingAnimation(list, TEXT_HIGHLIGHTING_ANIMATION_DURATION);

list.setOnCreateContextMenuListener(this);

mAdapter = new ContactItemListAdapter(this);







