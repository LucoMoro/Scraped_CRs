/*MediaStore.Audio.Media.getContentUriForPath() returns unexpected content

MediaStore.Audio.getContentUriForPath() returns an uri to
internal storage if anything but /mnt/sdcard/ is sent in.
This fix checks if there is an additional sdcard
(normally called ext_card) or usb mass storage attached to
the device and then returns an uri to the external db.
The extra sdcard name and the usb mass storage name info is read from
the system environment variable SECONDARY_STORAGE so if a customer
chooses to change the name this will work as expected.

Change-Id:Ib78bca929fe382d4770df895149a0132f0e56994*/
//Synthetic comment -- diff --git a/core/java/android/provider/MediaStore.java b/core/java/android/provider/MediaStore.java
//Synthetic comment -- index 79d0144..3f412fc 100644

//Synthetic comment -- @@ -1255,6 +1255,18 @@
}

public static final class Media implements AudioColumns {
/**
* Get the content:// style URI for the audio media table on the
* given volume.
//Synthetic comment -- @@ -1268,6 +1280,12 @@
}

public static Uri getContentUriForPath(String path) {
return (path.startsWith(Environment.getExternalStorageDirectory().getPath()) ?
EXTERNAL_CONTENT_URI : INTERNAL_CONTENT_URI);
}







