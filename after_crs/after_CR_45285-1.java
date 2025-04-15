/*Add null pointer check before do addStorage()

The MtpStorage instance is not created before /mnt/sdcard
primary storage mounted. This causes AndroidRuntime
crash when calling the storage's method with PTP enabled on boot.
So we must add a sanity check here, do not add storage for
null one, until the storage is mounted, added by volume listener.

E/AndroidRuntime( 3006): FATAL EXCEPTION: main
E/AndroidRuntime( 3006): java.lang.RuntimeException: Unable to start service com.android.providers.media.MtpServic
e@41e8f5a0 with Intent { cmp=com.android.providers.media/.MtpService (has extras) }: java.lang.NullPointerExceptio
n
E/AndroidRuntime( 3006):        at android.app.ActivityThread.handleServiceArgs(ActivityThread.java:2507)
E/AndroidRuntime( 3006):        at android.app.ActivityThread.access$1900(ActivityThread.java:130)
E/AndroidRuntime( 3006):        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1292)
E/AndroidRuntime( 3006):        at android.os.Handler.dispatchMessage(Handler.java:99)
E/AndroidRuntime( 3006):        at android.os.Looper.loop(Looper.java:137)
E/AndroidRuntime( 3006):        at android.app.ActivityThread.main(ActivityThread.java:4745)
E/AndroidRuntime( 3006):        at java.lang.reflect.Method.invokeNative(Native Method)
E/AndroidRuntime( 3006):        at java.lang.reflect.Method.invoke(Method.java:511)
E/AndroidRuntime( 3006):        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:786)
E/AndroidRuntime( 3006):        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:553)
E/AndroidRuntime( 3006):        at dalvik.system.NativeStart.main(Native Method)
E/AndroidRuntime( 3006): Caused by: java.lang.NullPointerException
E/AndroidRuntime( 3006):        at android.mtp.MtpStorage.<init>(MtpStorage.java:39)
E/AndroidRuntime( 3006):        at com.android.providers.media.MtpService.addStorageLocked(MtpService.java:206)
E/AndroidRuntime( 3006):        at com.android.providers.media.MtpService.addStorageDevicesLocked(MtpService.java:
53)
E/AndroidRuntime( 3006):        at com.android.providers.media.MtpService.onStartCommand(MtpService.java:148)
E/AndroidRuntime( 3006):        at android.app.ActivityThread.handleServiceArgs(ActivityThread.java:2490)
E/AndroidRuntime( 3006):        ... 10 more
D/WifiService( 2629): New client listening to asynchronous messages

Signed-off-by: Xinyu Chen <xinyu.chen@freescale.com>*/




//Synthetic comment -- diff --git a/src/com/android/providers/media/MtpService.java b/src/com/android/providers/media/MtpService.java
//Synthetic comment -- index fce8360..382e7ff 100644

//Synthetic comment -- @@ -50,7 +50,9 @@
private void addStorageDevicesLocked() {
if (mPtpMode) {
// In PTP mode we support only primary storage
            if (mVolumeMap.get(mVolumes[0].getPath()) != null) {
                addStorageLocked(mVolumeMap.get(mVolumes[0].getPath()));
            }
} else {
for (StorageVolume volume : mVolumeMap.values()) {
addStorageLocked(volume);







