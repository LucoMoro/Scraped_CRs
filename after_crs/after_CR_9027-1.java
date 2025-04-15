/*Optimize call to queryIntentActivities()

Replace a call to queryIntentActivities() with a calls to
resolveActivity(). This is done since the only purpose of the call
is to check if the returned list is empty or non-empty. It's
inefficient to move an entire list across the process boundary,
only to discard it.*/




//Synthetic comment -- diff --git a/src/com/android/providers/downloads/Helpers.java b/src/com/android/providers/downloads/Helpers.java
//Synthetic comment -- index 0226eba..866cddc 100644

//Synthetic comment -- @@ -38,7 +38,6 @@
import java.io.File; 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
//Synthetic comment -- @@ -115,11 +114,10 @@

PackageManager pm = context.getPackageManager();
intent.setDataAndType(Uri.fromParts("file", "", null), mimeType);
                ResolveInfo ri = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
//Log.i(Constants.TAG, "*** FILENAME QUERY " + intent + ": " + list);

                if (ri == null) {
if (Config.LOGD) {
Log.d(Constants.TAG, "no handler found for type " + mimeType);
}







