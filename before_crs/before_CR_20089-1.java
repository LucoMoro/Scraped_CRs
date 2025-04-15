/*Adding support for Bluetooth FTP profile

See the following android-contrib Google groups post:http://groups.google.com/group/android-contrib/msg/f48d9d4d28b6dc59In order to make support for BT FTP server we need to do changes in
Bluetooth framework files.

* Expand SDP UUID list with FTP UUID,
* Add FTP service record registration on start up.

Change-Id:I5da99548cca6d7f0b8c14e2e0ef4d35f18325680Signed-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/core/java/android/bluetooth/BluetoothUuid.java b/core/java/android/bluetooth/BluetoothUuid.java
//Synthetic comment -- index 4164a3d..fb0b09d 100644

//Synthetic comment -- @@ -49,10 +49,12 @@
ParcelUuid.fromString("0000110C-0000-1000-8000-00805F9B34FB");
public static final ParcelUuid ObexObjectPush =
ParcelUuid.fromString("00001105-0000-1000-8000-00805f9b34fb");

public static final ParcelUuid[] RESERVED_UUIDS = {
AudioSink, AudioSource, AdvAudioDist, HSP, Handsfree, AvrcpController, AvrcpTarget,
        ObexObjectPush};

public static boolean isAudioSource(ParcelUuid uuid) {
return uuid.equals(AudioSource);








//Synthetic comment -- diff --git a/core/java/android/server/BluetoothService.java b/core/java/android/server/BluetoothService.java
//Synthetic comment -- index 4d4d309..c8cae06 100644

//Synthetic comment -- @@ -121,7 +121,9 @@
private static final ParcelUuid[] RFCOMM_UUIDS = {
BluetoothUuid.Handsfree,
BluetoothUuid.HSP,
            BluetoothUuid.ObexObjectPush };

// TODO(): Optimize all these string handling
private final Map<String, String> mAdapterProperties;
//Synthetic comment -- @@ -488,6 +490,12 @@
case 4:
Log.d(TAG, "Registering pbap record");
SystemService.start("pbap");
break;
}
break;







