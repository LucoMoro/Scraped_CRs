/*When the device has no gps datetime, we can use original datetime as datetaken in the media database.

Change-Id:I855c94987a31e08abd75d42339e0dabe8a5c53c7*/




//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 3333268..db9584f 100644

//Synthetic comment -- @@ -742,6 +742,11 @@
long time = exif.getGpsDateTime();
if (time != -1) {
values.put(Images.Media.DATE_TAKEN, time);
                    } else {
                        time = exif.getDateTime();
                        if (time != -1) {
                            values.put(Images.Media.DATE_TAKEN, time);
                        }
}

int orientation = exif.getAttributeInt(







