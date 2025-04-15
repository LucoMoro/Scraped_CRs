/*Adding blank line to provide consistency to the code*/




//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaProvider.java b/src/com/android/providers/media/MediaProvider.java
//Synthetic comment -- index 28c7176..bded092 100644

//Synthetic comment -- @@ -1390,6 +1390,7 @@
case IMAGES_MEDIA:
out.table = "images";
break;

case IMAGES_MEDIA_ID:
out.table = "images";
where = "_id = " + uri.getPathSegments().get(3);







