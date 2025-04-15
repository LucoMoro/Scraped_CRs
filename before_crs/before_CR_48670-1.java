/*[MTP] fix an ArrayIndexOutOfBoundsException

The object's file path may include more than 255 characters that is
out of MTP string limitation and this will casue a exception
"ArrayIndexOutOfBoundsException". This patch add checking for the
string length of path.

Change-Id:I74ca12e2bccd5f4644aadc79904fa0f3a3415a2aAuthor: Changbin Du <changbinx.du@intel.com>
Signed-off-by: Changbin Du <changbinx.du@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 56769*/
//Synthetic comment -- diff --git a/media/java/android/mtp/MtpDatabase.java b/media/java/android/mtp/MtpDatabase.java
//Synthetic comment -- index 487585e..23ea90e 100644

//Synthetic comment -- @@ -882,7 +882,13 @@
ID_WHERE, new String[] {  Integer.toString(handle) }, null, null);
if (c != null && c.moveToNext()) {
String path = c.getString(1);
                path.getChars(0, path.length(), outFilePath, 0);
outFilePath[path.length()] = 0;
// File transfers from device to host will likely fail if the size is incorrect.
// So to be safe, use the actual file size here.







