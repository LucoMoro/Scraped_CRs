/*HTMLViewer could not open UTF-8 encoded file path.

HTMLViewer got encoded file path in onCreate but did not decode
the path when opening it. This caused a problem when the file path
contained non-ASCII codes like Chinese, Japanese or Korean.

Change-Id:I50dd611fc6da9b854c19ff5752785ae710f3b33d*/
//Synthetic comment -- diff --git a/src/com/android/htmlviewer/FileContentProvider.java b/src/com/android/htmlviewer/FileContentProvider.java
//Synthetic comment -- index 2583b70..2f417a1 100644

//Synthetic comment -- @@ -38,7 +38,6 @@

public static final String BASE_URI = 
"content://com.android.htmlfileprovider";
    public static final int BASE_URI_LEN = BASE_URI.length();

@Override
public String getType(Uri uri) {
//Synthetic comment -- @@ -57,7 +56,7 @@
if (!"r".equals(mode)) {
throw new FileNotFoundException("Bad mode for " + uri + ": " + mode);
}
        String filename = uri.toString().substring(BASE_URI_LEN);
return ParcelFileDescriptor.open(new File(filename),
ParcelFileDescriptor.MODE_READ_ONLY);
}







