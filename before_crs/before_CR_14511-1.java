/*Fixed disappearing menu options when switching from landscape to portrait.

The email client had menu options that appeared to disappear when one menu
item was pressed while the phone was rotated between landscape and portrait
mode.  Menu options and text were actually already there, but they were
using white text.

The issue is that the alert dialog used by the email client was using recycled
views when the phone was rotated.  One TextView had the "pressed" flag set
when it went into the recycle bin.  That TextView was then pulled out of the
recycle bin as a different menu option, so the TextView that was "Open" in
landscape became "Save" in portrait.  Then, the text option that was previously
set then had its flag cleared.  Since that option was not the one with the pressed
flag set, because of the recycle change, the option that was not selected continued
to print its text as if its pressed flag was set.  This made option that was using
white text change between portrait and landscape mode.

The fix was to clear the focus, selected and pressed flags when a view is pulled
from the recycle bin to be reused.  The focus, selected, and pressed flags are
all properties that are manipulated by the ListView and its parents (AbsListView,
AdapterView, and ViewGroup).  The RecycleBin class is an inner class in AbsListView
and is only used by its children.

Change-Id:Ib966df28f976e0cf3a77d3865044e7457026c456*/
//Synthetic comment -- diff --git a/core/java/android/widget/AbsListView.java b/core/java/android/widget/AbsListView.java
//Synthetic comment -- index 271989a..9951b43 100644

//Synthetic comment -- @@ -3575,6 +3575,13 @@
if (mRecyclerListener != null) {
mRecyclerListener.onMovedToScrapHeap(scrap);
}
}

/**
//Synthetic comment -- @@ -3609,6 +3616,13 @@
mRecyclerListener.onMovedToScrapHeap(victim);
}

if (ViewDebug.TRACE_RECYCLER) {
ViewDebug.trace(victim,
ViewDebug.RecyclerTraceType.MOVE_FROM_ACTIVE_TO_SCRAP_HEAP,







