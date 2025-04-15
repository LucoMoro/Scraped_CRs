/*Make locale safe paths

generateMultiplePath was using SimpleDateFormat with
the default locale to create paths. In some locales
this resulted in characters that are not safe to use
on all file systems. Explicitly use Locale.US to
be locale safe.

Change-Id:Ic1cc0b83d04a81ab4791b90e34365896c9ca27fc*/




//Synthetic comment -- diff --git a/src/com/android/nfc/handover/HandoverManager.java b/src/com/android/nfc/handover/HandoverManager.java
//Synthetic comment -- index 9836cdc..c751168 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

//Synthetic comment -- @@ -571,7 +572,7 @@
synchronized File generateMultiplePath(String beamRoot) {
// Generate a unique directory with the date
String format = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
String newPath = beamRoot + "beam-" + sdf.format(new Date());
File newFile = new File(newPath);
int count = 0;







