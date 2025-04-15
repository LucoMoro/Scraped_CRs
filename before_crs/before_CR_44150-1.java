/*Make locale safe paths

generateTempPhotoFileName was using SimpleDateFormat
with the default locale to create paths. In some locales
this resulted in characters that are not safe to use
on all file systems. Explicitly use Locale.US to
be locale safe.

Change-Id:I23fdc90042fcf2c323ef58e25fd34220e21648ca*/
//Synthetic comment -- diff --git a/src/com/android/contacts/util/ContactPhotoUtils.java b/src/com/android/contacts/util/ContactPhotoUtils.java
//Synthetic comment -- index 49be19d..b14b36c 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* Utilities related to loading/saving contact photos.
//Synthetic comment -- @@ -69,7 +70,7 @@

public static String generateTempPhotoFileName() {
Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(PHOTO_DATE_FORMAT);
return "ContactPhoto-" + dateFormat.format(date) + ".jpg";
}








