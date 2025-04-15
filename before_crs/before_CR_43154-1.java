/*Add support for android.uid.diag for system apps

This allows apps authored & signed by device manufacturers to be
installed on secure builds for advanced diagnostics, without
compromising the security of the device.

android.uid.log was inappropriate for this use-case, because any apps
claiming READ_LOGS, and the adb shell user, are able to modify the
private storage of the app.  This is the case due to the intersection
of the following:
- An app with android.uid.log has AID_LOG as its primary GID.
- Apps with READ_LOGS, and the adb shell user, have the AID_LOG GID
  as a supplementary GID.
- Some of folders/files in the private storage of Android apps allows
  r/w access to the group.

Change-Id:I1ec099e69fc5bf5267da651b32ae508ff7044a18*/
//Synthetic comment -- diff --git a/core/java/android/os/Process.java b/core/java/android/os/Process.java
//Synthetic comment -- index 6ab4dc1..e74acba 100644

//Synthetic comment -- @@ -80,6 +80,12 @@
public static final int LOG_UID = 1007;

/**
* Defines the UID/GID for the WIFI supplicant process.
* @hide
*/








//Synthetic comment -- diff --git a/services/java/com/android/server/pm/PackageManagerService.java b/services/java/com/android/server/pm/PackageManagerService.java
//Synthetic comment -- index 7390cbe..7ab2a1f 100644

//Synthetic comment -- @@ -174,6 +174,7 @@

private static final int RADIO_UID = Process.PHONE_UID;
private static final int LOG_UID = Process.LOG_UID;
private static final int NFC_UID = Process.NFC_UID;

private static final boolean GET_CERTIFICATES = true;
//Synthetic comment -- @@ -879,6 +880,7 @@
Process.SYSTEM_UID, ApplicationInfo.FLAG_SYSTEM);
mSettings.addSharedUserLPw("android.uid.phone", RADIO_UID, ApplicationInfo.FLAG_SYSTEM);
mSettings.addSharedUserLPw("android.uid.log", LOG_UID, ApplicationInfo.FLAG_SYSTEM);
mSettings.addSharedUserLPw("android.uid.nfc", NFC_UID, ApplicationInfo.FLAG_SYSTEM);

String separateProcesses = SystemProperties.get("debug.separate_processes");







