/*Play default ringtone if the current is unavailable

Make sure that the default ringtone is played if the
current ringtone is unavailable (f.ex. if sdcard is
removed). If the current tone becomes available again, it's
still kept as the current ringtone.
This also applies to notification and alarm.

Change-Id:Id9efd512766e3035bd4230b5f0d028b76fd58016*/
//Synthetic comment -- diff --git a/media/java/android/media/RingtoneManager.java b/media/java/android/media/RingtoneManager.java
//Synthetic comment -- index 8481410..2d5d873 100644

//Synthetic comment -- @@ -27,12 +27,16 @@
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DrmStore;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Settings.System;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

//Synthetic comment -- @@ -607,6 +611,40 @@

return null;
}

/**
* Gets the current default sound's {@link Uri}. This will give the actual
//Synthetic comment -- @@ -624,6 +662,29 @@
String setting = getSettingForType(type);
if (setting == null) return null;
final String uriString = Settings.System.getString(context.getContentResolver(), setting);
return uriString != null ? Uri.parse(uriString) : null;
}








