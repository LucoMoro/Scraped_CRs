/*SettingsProvider: be less restrictive when proxying tones

Make the SettingsProvider less restictive when proxing default ringtones.
The implementation restricted proxying exclusively to the media- and drmstore
which prevented third parties from implementing their own toneprovider.

Change-Id:I983e1a9bf1c66357d048b9c7e8f1ea46244cfaceSigned-off-by: Simon Brakhane <simon.brakhane@googlemail.com>*/
//Synthetic comment -- diff --git a/packages/SettingsProvider/src/com/android/providers/settings/SettingsProvider.java b/packages/SettingsProvider/src/com/android/providers/settings/SettingsProvider.java
//Synthetic comment -- index 95fd62d..4196011 100644

//Synthetic comment -- @@ -39,7 +39,6 @@
import android.os.ParcelFileDescriptor;
import android.os.SystemProperties;
import android.provider.DrmStore;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
//Synthetic comment -- @@ -622,7 +621,7 @@
/*
* When a client attempts to openFile the default ringtone or
* notification setting Uri, we will proxy the call to the current
         * default ringtone's Uri (if it is in the DRM or media provider).
*/
int ringtoneType = RingtoneManager.getDefaultType(uri);
// Above call returns -1 if the Uri doesn't match a default type
//Synthetic comment -- @@ -633,24 +632,20 @@
Uri soundUri = RingtoneManager.getActualDefaultRingtoneUri(context, ringtoneType);

if (soundUri != null) {
                // Only proxy the openFile call to drm or media providers
String authority = soundUri.getAuthority();
boolean isDrmAuthority = authority.equals(DrmStore.AUTHORITY);
                if (isDrmAuthority || authority.equals(MediaStore.AUTHORITY)) {

                    if (isDrmAuthority) {
                        try {
                            // Check DRM access permission here, since once we
                            // do the below call the DRM will be checking our
                            // permission, not our caller's permission
                            DrmStore.enforceAccessDrmPermission(context);
                        } catch (SecurityException e) {
                            throw new FileNotFoundException(e.getMessage());
                        }
}

                    return context.getContentResolver().openFileDescriptor(soundUri, mode);
}
}
}

//Synthetic comment -- @@ -663,7 +658,7 @@
/*
* When a client attempts to openFile the default ringtone or
* notification setting Uri, we will proxy the call to the current
         * default ringtone's Uri (if it is in the DRM or media provider).
*/
int ringtoneType = RingtoneManager.getDefaultType(uri);
// Above call returns -1 if the Uri doesn't match a default type
//Synthetic comment -- @@ -674,31 +669,27 @@
Uri soundUri = RingtoneManager.getActualDefaultRingtoneUri(context, ringtoneType);

if (soundUri != null) {
                // Only proxy the openFile call to drm or media providers
String authority = soundUri.getAuthority();
boolean isDrmAuthority = authority.equals(DrmStore.AUTHORITY);
                if (isDrmAuthority || authority.equals(MediaStore.AUTHORITY)) {

                    if (isDrmAuthority) {
                        try {
                            // Check DRM access permission here, since once we
                            // do the below call the DRM will be checking our
                            // permission, not our caller's permission
                            DrmStore.enforceAccessDrmPermission(context);
                        } catch (SecurityException e) {
                            throw new FileNotFoundException(e.getMessage());
                        }
                    }

                    ParcelFileDescriptor pfd = null;
try {
                        pfd = context.getContentResolver().openFileDescriptor(soundUri, mode);
                        return new AssetFileDescriptor(pfd, 0, -1);
                    } catch (FileNotFoundException ex) {
                        // fall through and open the fallback ringtone below
}
}

try {
return super.openAssetFile(soundUri, mode);
} catch (FileNotFoundException ex) {







