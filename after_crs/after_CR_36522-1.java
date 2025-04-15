/*Function uri.getAuthority is called twice. Minor doc corrections.

Function uri.getAuthority was called twice in methods acquireProvider and acquireExistingProvider was called twice although a parameter representing the value had existed. The second call to the function is changed to the parameter. The parameter's modifier changed to final. Minor corrections in function descriptions in the file.
Signed-off-by: Yury Zhauniarovich <y.zhalnerovich@gmail.com>

Change-Id:Id003aa38c17d644357873c41a8f5ec455e46a4b7*/




//Synthetic comment -- diff --git a/core/java/android/content/ContentResolver.java b/core/java/android/content/ContentResolver.java
//Synthetic comment -- index 780ec11..75f620b 100644

//Synthetic comment -- @@ -440,7 +440,7 @@
* ContentProvider.openFile}.
* @return Returns a new ParcelFileDescriptor pointing to the file.  You
* own this descriptor and are responsible for closing it when done.
     * @throws FileNotFoundException Throws FileNotFoundException if no
* file exists under the URI or the mode is invalid.
* @see #openAssetFileDescriptor(Uri, String)
*/
//Synthetic comment -- @@ -556,7 +556,7 @@
fd.getDeclaredLength());
} catch (RemoteException e) {
// Somewhat pointless, as Activity Manager will kill this
                    // process shortly anyway if the dependent ContentProvider dies.
throw new FileNotFoundException("Dead content provider: " + uri);
} catch (FileNotFoundException e) {
throw e;
//Synthetic comment -- @@ -913,9 +913,9 @@
if (!SCHEME_CONTENT.equals(uri.getScheme())) {
return null;
}
        final String auth = uri.getAuthority();
if (auth != null) {
            return acquireProvider(mContext, auth);
}
return null;
}
//Synthetic comment -- @@ -932,9 +932,9 @@
if (!SCHEME_CONTENT.equals(uri.getScheme())) {
return null;
}
        final String auth = uri.getAuthority();
if (auth != null) {
            return acquireExistingProvider(mContext, auth);
}
return null;
}







