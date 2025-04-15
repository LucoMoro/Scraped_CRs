/*Play default ringtone if the current is unavailable

Make sure that the default ringtone is played if the
current ringtone is unavailable (f.ex. if sdcard is
removed). If the current tone becomes available again, it's
still kept as the current ringtone.
This also applies to notification and alarm.

Change-Id:Id9efd512766e3035bd4230b5f0d028b76fd58016*/




//Synthetic comment -- diff --git a/media/java/android/media/RingtoneManager.java b/media/java/android/media/RingtoneManager.java
//Synthetic comment -- index 8481410..436c9d0 100644

//Synthetic comment -- @@ -27,12 +27,16 @@
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.SystemProperties;
import android.provider.DrmStore;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Settings.System;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Synthetic comment -- @@ -607,6 +611,41 @@

return null;
}

    /**
     * Gets the ringtone Uri by looking up the customized default
     * ringtone from SystemProperties.
     *
     * @param context A context used for querying.
     * @param setting The ringtone setting to query for. One of Settings.System.RINGTONE,
     *                Settings.System.NOTIFICATION_SOUND, or Settings.System.ALARM_ALERT.
     * @return A Uri pointing to the customized default sound for the sound type.
     */
    private static Uri getCustomizedDefaultRingtoneUri(Context context, String setting) {
        final String name = SystemProperties.get("ro.config." + setting);
        if (name != null && !name.equals("")) {
            Cursor c = null;
            try {
                c = context.getContentResolver().query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DISPLAY_NAME},
                        MediaStore.Audio.Media.DISPLAY_NAME + " = ?", new String[] {name},
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

                if (c != null && c.moveToFirst()) {
                    final int idIndex = c.getColumnIndex(MediaStore.Audio.Media._ID);
                    final int id = c.getInt(idIndex);
                    return Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                            String.valueOf(id));
                }
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }
        return null;
    }

/**
* Gets the current default sound's {@link Uri}. This will give the actual
//Synthetic comment -- @@ -624,6 +663,29 @@
String setting = getSettingForType(type);
if (setting == null) return null;
final String uriString = Settings.System.getString(context.getContentResolver(), setting);

        // if uriString can't be found, get the "original" customized default
        // from system properties.
        if (uriString != null) {
            ParcelFileDescriptor pfd = null;
            try {
                pfd = context.getContentResolver().openFileDescriptor(Uri.parse(uriString), "r");
            } catch (FileNotFoundException ex) {
                final Uri customizedUri = getCustomizedDefaultRingtoneUri(context, setting);
                if (customizedUri != null) {
                    return customizedUri;
                }
            } finally {
                if (pfd != null) {
                    try {
                        pfd.close();
                    } catch (IOException ioe) {
                        // Ignore
                    }
                }
            }
        }

return uriString != null ? Uri.parse(uriString) : null;
}








