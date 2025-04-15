/*Use the new download manager APIs introduced in change 7400*/
//Synthetic comment -- diff --git a/src/com/android/browser/BrowserActivity.java b/src/com/android/browser/BrowserActivity.java
//Synthetic comment -- index 170083d..789babe 100644

//Synthetic comment -- @@ -3427,19 +3427,19 @@
String cookies = CookieManager.getInstance().getCookie(url);

ContentValues values = new ContentValues();
        values.put(Downloads.URI, url);
        values.put(Downloads.COOKIE_DATA, cookies);
        values.put(Downloads.USER_AGENT, userAgent);
        values.put(Downloads.NOTIFICATION_PACKAGE,
getPackageName());
        values.put(Downloads.NOTIFICATION_CLASS,
BrowserDownloadPage.class.getCanonicalName());
        values.put(Downloads.VISIBILITY, Downloads.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        values.put(Downloads.MIMETYPE, mimetype);
        values.put(Downloads.FILENAME_HINT, filename);
        values.put(Downloads.DESCRIPTION, Uri.parse(url).getHost());
if (contentLength > 0) {
            values.put(Downloads.TOTAL_BYTES, contentLength);
}
if (mimetype == null) {
// We must have long pressed on a link or image to download it. We








//Synthetic comment -- diff --git a/src/com/android/browser/BrowserDownloadAdapter.java b/src/com/android/browser/BrowserDownloadAdapter.java
//Synthetic comment -- index 0b509ef..68d3a83 100644

//Synthetic comment -- @@ -60,14 +60,14 @@
public BrowserDownloadAdapter(Context context, int layout, Cursor c) {
super(context, layout, c);
mFilenameColumnId = c.getColumnIndexOrThrow(Downloads._DATA);
        mTitleColumnId = c.getColumnIndexOrThrow(Downloads.TITLE);
        mDescColumnId = c.getColumnIndexOrThrow(Downloads.DESCRIPTION);
        mStatusColumnId = c.getColumnIndexOrThrow(Downloads.STATUS);
        mTotalBytesColumnId = c.getColumnIndexOrThrow(Downloads.TOTAL_BYTES);
mCurrentBytesColumnId = 
            c.getColumnIndexOrThrow(Downloads.CURRENT_BYTES);
        mMimetypeColumnId = c.getColumnIndexOrThrow(Downloads.MIMETYPE);
        mDateColumnId = c.getColumnIndexOrThrow(Downloads.LAST_MODIFICATION);
}

@Override
//Synthetic comment -- @@ -106,7 +106,7 @@
// We have a filename, so we can build a title from that
title = new File(fullFilename).getName();
ContentValues values = new ContentValues();
                values.put(Downloads.TITLE, title);
// assume "_id" is the first column for the cursor 
context.getContentResolver().update(
ContentUris.withAppendedId(Downloads.CONTENT_URI,








//Synthetic comment -- diff --git a/src/com/android/browser/BrowserDownloadPage.java b/src/com/android/browser/BrowserDownloadPage.java
//Synthetic comment -- index 4397337..9776834 100644

//Synthetic comment -- @@ -73,22 +73,22 @@
mListView.setEmptyView(v);

mDownloadCursor = managedQuery(Downloads.CONTENT_URI, 
                new String [] {"_id", Downloads.TITLE, Downloads.STATUS,
                Downloads.TOTAL_BYTES, Downloads.CURRENT_BYTES, 
                Downloads._DATA, Downloads.DESCRIPTION, 
                Downloads.MIMETYPE, Downloads.LAST_MODIFICATION,
                Downloads.VISIBILITY}, 
null, null);

// only attach everything to the listbox if we can access
// the download database. Otherwise, just show it empty
if (mDownloadCursor != null) {
mStatusColumnId = 
                    mDownloadCursor.getColumnIndexOrThrow(Downloads.STATUS);
mIdColumnId =
mDownloadCursor.getColumnIndexOrThrow(Downloads._ID);
mTitleColumnId = 
                    mDownloadCursor.getColumnIndexOrThrow(Downloads.TITLE);

// Create a list "controller" for the data
mDownloadAdapter = new BrowserDownloadAdapter(this, 
//Synthetic comment -- @@ -403,7 +403,7 @@
mDownloadCursor.getColumnIndexOrThrow(Downloads._DATA);
String filename = mDownloadCursor.getString(filenameColumnId);
int mimetypeColumnId =
                mDownloadCursor.getColumnIndexOrThrow(Downloads.MIMETYPE);
String mimetype = mDownloadCursor.getString(mimetypeColumnId);
Uri path = Uri.parse(filename);
// If there is no scheme, then it must be a file
//Synthetic comment -- @@ -453,13 +453,13 @@
private void hideCompletedDownload() {
int status = mDownloadCursor.getInt(mStatusColumnId);

        int visibilityColumn = mDownloadCursor.getColumnIndexOrThrow(Downloads.VISIBILITY);
int visibility = mDownloadCursor.getInt(visibilityColumn);

if (Downloads.isStatusCompleted(status) &&
visibility == Downloads.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) {
ContentValues values = new ContentValues();
            values.put(Downloads.VISIBILITY, Downloads.VISIBILITY_VISIBLE);
getContentResolver().update(
ContentUris.withAppendedId(Downloads.CONTENT_URI,
mDownloadCursor.getLong(mIdColumnId)), values, null, null);








//Synthetic comment -- diff --git a/src/com/android/browser/FetchUrlMimeType.java b/src/com/android/browser/FetchUrlMimeType.java
//Synthetic comment -- index 8578643..c585dbb 100644

//Synthetic comment -- @@ -58,7 +58,7 @@
mValues = values[0];

// Check to make sure we have a URI to download
        String uri = mValues.getAsString(Downloads.URI);
if (uri == null || uri.length() == 0) {
return null;
}
//Synthetic comment -- @@ -66,15 +66,15 @@
// User agent is likely to be null, though the AndroidHttpClient
// seems ok with that.
AndroidHttpClient client = AndroidHttpClient.newInstance(
                mValues.getAsString(Downloads.USER_AGENT));
HttpHead request = new HttpHead(uri);

        String cookie = mValues.getAsString(Downloads.COOKIE_DATA);
if (cookie != null && cookie.length() > 0) {
request.addHeader("Cookie", cookie);
}

        String referer = mValues.getAsString(Downloads.REFERER);
if (referer != null && referer.length() > 0) {
request.addHeader("Referer", referer);
}
//Synthetic comment -- @@ -111,19 +111,19 @@
@Override
public void onPostExecute(String mimeType) {
if (mimeType != null) {
           String url = mValues.getAsString(Downloads.URI);
if (mimeType.equalsIgnoreCase("text/plain") ||
mimeType.equalsIgnoreCase("application/octet-stream")) {
String newMimeType =
MimeTypeMap.getSingleton().getMimeTypeFromExtension(
MimeTypeMap.getFileExtensionFromUrl(url));
if (newMimeType != null) {
                   mValues.put(Downloads.MIMETYPE, newMimeType);
}
}
String filename = URLUtil.guessFileName(url,
null, mimeType);
           mValues.put(Downloads.FILENAME_HINT, filename);
}

// Start the download







