/*Fix NullpointerException in MediaMetadataRetriever

According to the API documentation and the ICS implementation
the setDataSource(String) method throws an
IllegalArgumentException if path is null. In JB a
NullpointerException is thrown instead. This fix restores
the earlier behaviour.

Change-Id:Ic47baadf91076acc227d92d84f6b8d1d6ecd0c03*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaMetadataRetriever.java b/media/java/android/media/MediaMetadataRetriever.java
//Synthetic comment -- index cc59d02..0066c35 100644

//Synthetic comment -- @@ -59,6 +59,10 @@
* @throws IllegalArgumentException If the path is invalid.
*/
public void setDataSource(String path) throws IllegalArgumentException {
FileInputStream is = null;
try {
is = new FileInputStream(path);







