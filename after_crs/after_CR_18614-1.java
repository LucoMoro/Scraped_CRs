/*Add support for Content-Disposition when save link

Content-Disposition isn't used when downloading an
item by long-click the link. It'll result in different
file name if the item is downloaded by clicking the link
or if it's downloaded by long-click the link and select
Save link if the HTTP response includes the Content-Disposition
header with the filename attribute

Change-Id:I7eacfd1128da261e0674bbdc3064208a82e46ab3*/




//Synthetic comment -- diff --git a/src/com/android/browser/FetchUrlMimeType.java b/src/com/android/browser/FetchUrlMimeType.java
//Synthetic comment -- index 9bd0cf9..62d877e 100644

//Synthetic comment -- @@ -47,7 +47,7 @@
* android.os.webkit.LoadListener rather than handling it here.
*
*/
class FetchUrlMimeType extends AsyncTask<ContentValues, String, ContentValues> {

BrowserActivity mActivity;
ContentValues mValues;
//Synthetic comment -- @@ -57,7 +57,7 @@
}

@Override
    public ContentValues doInBackground(ContentValues... values) {
mValues = values[0];

// Check to make sure we have a URI to download
//Synthetic comment -- @@ -87,7 +87,7 @@
}

HttpResponse response;
        ContentValues result = new ContentValues();
try {
response = client.execute(request);
// We could get a redirect here, but if we do lets let
//Synthetic comment -- @@ -96,11 +96,16 @@
if (response.getStatusLine().getStatusCode() == 200) {
Header header = response.getFirstHeader("Content-Type");
if (header != null) {
                    String mimeType = header.getValue();
final int semicolonIndex = mimeType.indexOf(';');
if (semicolonIndex != -1) {
mimeType = mimeType.substring(0, semicolonIndex);
}
                    result.put("Content-Type", mimeType);
                }
                Header contentDispositionHeader = response.getFirstHeader("Content-Disposition");
                if (contentDispositionHeader != null) {
                    result.put("Content-Disposition", contentDispositionHeader.getValue());
}
}
} catch (IllegalArgumentException ex) {
//Synthetic comment -- @@ -111,11 +116,13 @@
client.close();
}

        return result;
}

@Override
    public void onPostExecute(ContentValues values) {
       final String mimeType = values.getAsString("Content-Type");
       final String contentDisposition = values.getAsString("Content-Disposition");
if (mimeType != null) {
String url = mValues.getAsString(Downloads.Impl.COLUMN_URI);
if (mimeType.equalsIgnoreCase("text/plain") ||
//Synthetic comment -- @@ -128,7 +135,7 @@
}
}
String filename = URLUtil.guessFileName(url,
                   contentDisposition, mimeType);
mValues.put(Downloads.Impl.COLUMN_FILE_NAME_HINT, filename);
}








