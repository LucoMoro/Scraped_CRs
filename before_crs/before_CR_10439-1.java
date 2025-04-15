/*Fix for Android.com issue 1421:
	"The insert method doesn't get a theradID if the thread ID is missing."http://code.google.com/p/android/issues/detail?id=1421*/
//Synthetic comment -- diff --git a/src/com/android/providers/telephony/MmsProvider.java b/src/com/android/providers/telephony/MmsProvider.java
//Synthetic comment -- index 37d1787..22c55d7 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.BaseColumns;
import android.provider.Telephony.Mms;
import android.provider.Telephony.MmsSms;
import android.provider.Telephony.Mms.Addr;
//Synthetic comment -- @@ -42,6 +43,7 @@
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
* The class to provide base facility to access MMS related content,
//Synthetic comment -- @@ -339,6 +341,14 @@
finalValues.put(Mms.READ, 1);
}

if ((rowId = db.insert(table, null, finalValues)) <= 0) {
Log.e(TAG, "MmsProvider.insert: failed! " + finalValues);
return null;







