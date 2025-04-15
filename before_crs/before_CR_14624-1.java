/*Prevent browser becoming unresponsive when surfing to a URL with many %-escaped characters.

Surfing to a webpage with many %-characters in the URL will hang the browser for several minutes.
To reproduce:
String url =
"http://zh.wikipedia.org/wiki/%E4%B8%AD%E5%8D%8E%E4%BA%BA%E6%B0%91%E5%85%B1%E5%92%8C%E5%9B%BD%E5%8E%BF%E7%BA%A7%E4%BB%A5%E4%B8%8A%E8%A1%8C%E6%94%BF%E5%8C%BA%E5%88%97%E8%A1%A8?%E4%B8%AD%E5%8D%8E
%E4%BA%BA%E6%B0%91%E5%85%B1%E5%92%8C%E5%9B%BD%E5%8E%BF%E7%BA%A7%E4%BB%A5%E4%B8%8A%E8%A1%8C%E6%94
%BF%E5%8C%BA%E5%88%97%E8%A1%A8%E4%B8%AD%E5%8D%8E%E4%BA%BA%E6%B0%91%E5%85%B1%E5%92%8C%E5%9B%BD%E5
%8E%BF%E7%BA%A7%E4%BB%A5%E4%B8%8A%E8%A1%8C%E6%94%BF%E5%8C%BA%E5%88%97%E8%A1%A8%E4%B8%AD%E5%8D%8E
%E4%BA%BA%E6%B0%91%E5%85%B1%E5%92%8C%E5%9B%BD%E5%8E%BF%E7%BA%A7%E4%BB%A5%E4%B8%8A%E8%A1%8C%E6%94
%BF%E5%8C%BA%E5%88%97%E8%A1%A8%E4%B8%AD%E5%8D%8E%E4%BA%BA%E6%B0%91%E5%85%B1%E5%92%8C%E5%9B%BD%E5
%8E%BF%E7%BA%A7%E4%BB%A5%E4%B8%8A%E8%A1%8C%E6%94%BF%E5%8C%BA%E5%88%97%E8%A1%A8%E4%B8%AD%E5%8D%8E
%E4%BA%BA%E6%B0%91%E5%85%B1%E5%92%8C%E5%9B%BD%E5%8E%BF%E7%BA%A7%E4%BB%A5%E4%B8%8A%E8%A1%8C%E6%94
%BF%E5%8C%BA%E5%88%97%E8%A1%A8%E4%B8%AD%E5%8D%8E%E4%BA%BA%E6%B0%91%E5%85%B1%E5%92%8C%E5%9B%BD%E5
%8E%BF%E7%BA%A7%E4%BB%A5%E4%B8%8A%E8%A1%8C%E6%94%BF%E5%8C%BA%E5%88%97%E8%A1%A8";
Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
intent.addCategory(Intent.CATEGORY_BROWSABLE);
startActivity(intent);

The raw URL is used to match previously visited URLs in the history database with the SQL LIKE operator.
The LIKE operator does however treat %-signs as wildcards and an exhaustive and unnecessary search is made.
The fix uses the ESCAPE functionality of LIKE to treat the %-signs in the URL as non-wildcards.
As underscore is treated as also treated as a wildcard, it is escaped as well.

Change-Id:I20d182bc76b339c49ad23acc1c4d735414310b04*/
//Synthetic comment -- diff --git a/src/com/android/browser/BrowserActivity.java b/src/com/android/browser/BrowserActivity.java
//Synthetic comment -- index a754900..0c40d66 100644

//Synthetic comment -- @@ -3292,13 +3292,15 @@
if (url.startsWith("http://www.")) {
url = url.substring(11);
} else if (url.startsWith("http://")) {
                url = url.substring(4);
}
try {
url = "%" + url;
String [] selArgs = new String[] { url };

                String where = Browser.BookmarkColumns.URL + " LIKE ? AND "
+ Browser.BookmarkColumns.BOOKMARK + " = 0";
Cursor c = mResolver.query(Browser.BOOKMARKS_URI,
Browser.HISTORY_PROJECTION, where, selArgs, null);







