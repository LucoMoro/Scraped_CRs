/*Removal of non functional star in context menu of HistoryItem

In contrary to the contextmenus created by longpressing an element
under the "Bookmarks tab" or the "Most visited tab" in the browser,
the context menu created from an element under the "History tab"
has a star present in the header. Removed this star to get a more
unified look and feel. This star also crashed the browser when it
was pressed.

Change-Id:Icddd6ba7d813773445d4dedea589f70be06e5881*/




//Synthetic comment -- diff --git a/src/com/android/browser/BrowserHistoryPage.java b/src/com/android/browser/BrowserHistoryPage.java
//Synthetic comment -- index 23080f8..6d7b7ed 100644

//Synthetic comment -- @@ -52,7 +52,7 @@
public class BrowserHistoryPage extends ExpandableListActivity {
private HistoryAdapter          mAdapter;
private boolean                 mDisableNewWindow;
    private BookmarkItem            mContextHeader;

private final static String LOGTAG = "browser";

//Synthetic comment -- @@ -207,8 +207,9 @@
HistoryItem historyItem = (HistoryItem) i.targetView;

// Setup the header
        // Use a BookmarkItem to get the same look and feel as in the other context menus.
if (mContextHeader == null) {
            mContextHeader = new BookmarkItem(this);
} else if (mContextHeader.getParent() != null) {
((ViewGroup) mContextHeader.getParent()).removeView(mContextHeader);
}







