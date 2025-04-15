/*Fix no voice readout when clicking a group view

When Accessibility was on, there is no voice readout for group view.
For example:
1. Launch Browser app, press menu key, choose Bookmarks.
   - Voice readout "Bookmarks".
2.  Press "History" label, tap on "Today".
   - Issue: there is no voice readout when press the button.

To fix it, add sendAccessibilityEvent() when handling a group view
click.

Change-Id:I3a20f7d5b9d7c8acf0133933438cd8d9e646275a*/
//Synthetic comment -- diff --git a/core/java/android/widget/ExpandableListView.java b/core/java/android/widget/ExpandableListView.java
//Synthetic comment -- index a746370..b8937b2 100644

//Synthetic comment -- @@ -539,6 +539,9 @@
if (posMetadata.position.type == ExpandableListPosition.GROUP) {
/* It's a group, so handle collapsing/expanding */

/* It's a group click, so pass on event */
if (mOnGroupClickListener != null) {
if (mOnGroupClickListener.onGroupClick(this, v,







