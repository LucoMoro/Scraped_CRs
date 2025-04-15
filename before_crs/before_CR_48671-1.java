/*MediaScanner: delete abnormal elements in prescan procedure.

There is a race in MediaProvider's mUnmountReciver.
when it doing external storage's database clean up at power down procedure,
if the datebase is big, system may shutdown after db.update, but db.delete
not done, there will be many elements with no filename in the database.
That may cause duplicated files and folders shown in MTP.

This patch clean up these abnormal elements in MediaScanner's prescan procedure.

Change-Id:I0a3dacffd12892b1a2a892b2749a02ab3dea109bAuthor: Liang Wang <liangx.wang@intel.com>
Signed-off-by: Liang Wang <liangx.wang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 58969*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 0f01aae..64083a6 100644

//Synthetic comment -- @@ -1116,6 +1116,16 @@
long lastModified = c.getLong(FILES_PRESCAN_DATE_MODIFIED_COLUMN_INDEX);
lastId = rowId;

// Only consider entries with absolute path names.
// This allows storing URIs in the database without the
// media scanner removing them.







