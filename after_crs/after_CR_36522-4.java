/*Function uri.getAuthority is called twice. Minor doc corrections.

Function uri.getAuthority was called twice in methods acquireProvider
and acquireExistingProvider was called twice although a parameter
representing the value had existed. The second call to the function is
changed to the parameter. The parameter's modifier changed to final.
Minor corrections in function descriptions in the file.

Signed-off-by: Yury Zhauniarovich <y.zhalnerovich@gmail.com>
Change-Id:Id003aa38c17d644357873c41a8f5ec455e46a4b7*/




//Synthetic comment -- diff --git a/core/java/android/content/ContentResolver.java b/core/java/android/content/ContentResolver.java
//Synthetic comment -- index 9e406d4..bde4d2b 100644

//Synthetic comment -- @@ -518,7 +518,7 @@
* ContentProvider.openFile}.
* @return Returns a new ParcelFileDescriptor pointing to the file.  You
* own this descriptor and are responsible for closing it when done.
     * @throws FileNotFoundException Throws FileNotFoundException if no
* file exists under the URI or the mode is invalid.
* @see #openAssetFileDescriptor(Uri, String)
*/
//Synthetic comment -- @@ -1049,9 +1049,9 @@
if (!SCHEME_CONTENT.equals(uri.getScheme())) {
return null;
}
        final String auth = uri.getAuthority();
if (auth != null) {
            return acquireProvider(mContext, auth);
}
return null;
}
//Synthetic comment -- @@ -1068,9 +1068,9 @@
if (!SCHEME_CONTENT.equals(uri.getScheme())) {
return null;
}
        final String auth = uri.getAuthority();
if (auth != null) {
            return acquireExistingProvider(mContext, auth);
}
return null;
}







