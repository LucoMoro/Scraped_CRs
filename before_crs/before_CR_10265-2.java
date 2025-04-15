/*Resolving issue with IMAGE_MEDIA: Bug 2861

Adding blank line to provide consistency to the code*/
//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaProvider.java b/src/com/android/providers/media/MediaProvider.java
//Synthetic comment -- index 1a2f5a6..bded092 100644

//Synthetic comment -- @@ -1387,6 +1387,10 @@
GetTableAndWhereOutParameter out) {
String where = null;
switch (match) {
case IMAGES_MEDIA_ID:
out.table = "images";
where = "_id = " + uri.getPathSegments().get(3);







