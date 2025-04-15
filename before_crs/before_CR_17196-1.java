/*The Browser crashes if there are null titles.

Added support for handling of bookmarks with
a title equal to null, as these kind of bookmarks
should be able to be added according to cts testcases
inside "android.provider.cts.BrowserTest".

Similar problems has also been found in the wild
through third party applications.

Change-Id:I4b35be63370de0b7df5206717574c52804010c94*/
//Synthetic comment -- diff --git a/src/com/android/browser/Bookmarks.java b/src/com/android/browser/Bookmarks.java
//Synthetic comment -- index 5ada9dc..8087f51 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Browser;
import android.util.Log;
import android.webkit.WebIconDatabase;
import android.widget.Toast;
//Synthetic comment -- @@ -88,8 +89,8 @@
// One or more bookmarks already exist for this site.
// Check the names of each
cursor.moveToPosition(i);
                    if (cursor.getString(Browser.HISTORY_PROJECTION_TITLE_INDEX)
                            .equals(name)) {
// The old bookmark has the same name.
// Update its creation time.
map.put(Browser.BookmarkColumns.CREATED,
//Synthetic comment -- @@ -158,8 +159,8 @@
cursor = cr.query(
Browser.BOOKMARKS_URI,
Browser.HISTORY_PROJECTION,
                    "url = ? AND title = ?",
                    new String[] { url, title },
null);
boolean first = cursor.moveToFirst();
// Should be in the database no matter what








//Synthetic comment -- diff --git a/src/com/android/browser/BrowserBackupAgent.java b/src/com/android/browser/BrowserBackupAgent.java
//Synthetic comment -- index c968ce5..d590cd8 100644

//Synthetic comment -- @@ -236,6 +236,11 @@
long created = cursor.getLong(3);
String title = cursor.getString(4);

// construct the flattened record in a byte array
bufstream.reset();
bout.writeUTF(url);








//Synthetic comment -- diff --git a/src/com/android/browser/BrowserBookmarksPage.java b/src/com/android/browser/BrowserBookmarksPage.java
//Synthetic comment -- index 7560c78..5ab717d 100644

//Synthetic comment -- @@ -448,6 +448,9 @@
private Intent createShortcutIntent(int position) {
String url = getUrl(position);
String title = getBookmarkTitle(position);
Bitmap touchIcon = getTouchIcon(position);

final Intent i = new Intent();
//Synthetic comment -- @@ -660,11 +663,15 @@
// Put up a dialog asking if the user really wants to
// delete the bookmark
final int deletePos = position;
new AlertDialog.Builder(this)
.setTitle(R.string.delete_bookmark)
.setIcon(android.R.drawable.ic_dialog_alert)
.setMessage(getText(R.string.delete_bookmark_warning).toString().replace(
                        "%s", getBookmarkTitle(deletePos)))
.setPositiveButton(R.string.ok,
new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int whichButton) {








//Synthetic comment -- diff --git a/tests/src/com/android/browser/BrowserBookmarkTest.java b/tests/src/com/android/browser/BrowserBookmarkTest.java
new file mode 100644
//Synthetic comment -- index 0000000..f583683

//Synthetic comment -- @@ -0,0 +1,203 @@








//Synthetic comment -- diff --git a/tests/src/com/android/browser/BrowserTestHelper.java b/tests/src/com/android/browser/BrowserTestHelper.java
new file mode 100644
//Synthetic comment -- index 0000000..d2cb7d9

//Synthetic comment -- @@ -0,0 +1,122 @@








//Synthetic comment -- diff --git a/tests/src/com/android/browser/TestItem.java b/tests/src/com/android/browser/TestItem.java
new file mode 100644
//Synthetic comment -- index 0000000..04c7f04

//Synthetic comment -- @@ -0,0 +1,165 @@







