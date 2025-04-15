/*Mms: Searching result is not displayed when searching " "

When querying a double quote , there will be a SqliteException, so
the cursor wil be null.
When cursor is null, we have to set the title.

Change-Id:I6fc91ad5562ca770412968b19cbc45aa1a667bd4Author: b533 <b533@borqs.com>
Signed-off-by: Kun Jiang <kunx.jiang@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 25854*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/SearchActivity.java b/src/com/android/mms/ui/SearchActivity.java
//Synthetic comment -- index 92e746b..e7fe0dd 100644

//Synthetic comment -- @@ -286,6 +286,11 @@
mQueryHandler = new AsyncQueryHandler(cr) {
protected void onQueryComplete(int token, Object cookie, Cursor c) {
if (c == null) {
                    setTitle(getResources().getQuantityString(
                        R.plurals.search_results_title,
                        0,
                        0,
                        searchString));
return;
}
final int threadIdPos = c.getColumnIndex("thread_id");







