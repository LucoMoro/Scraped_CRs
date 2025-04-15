/*Enable possibility to register for ACTION_HEADSET_PLUG statically

Currently, its not possible to register for ACTION_HEADSET_PLUG in
the manifest of Android applications, and the intent is only available
when the application is already running. This leads to odd use-cases
where applications needs to start on BOOT_COMPLETEd, to register for
the intent, and then stay alive. Instead of forcing applications
to do this, this commit enables them to listen for this intent
in AndroidManifest.xml.

Change-Id:Ibee7ab5d47bd1972d0b8cf98de735fa030e3e7d0*/
//Synthetic comment -- diff --git a/services/java/com/android/server/HeadsetObserver.java b/services/java/com/android/server/HeadsetObserver.java
//Synthetic comment -- index 6f0a91d..b0713dc 100644

//Synthetic comment -- @@ -153,7 +153,6 @@
if ((headsetState & headset) != (prevHeadsetState & headset)) {
//  Pack up the values and broadcast them to everyone
Intent intent = new Intent(Intent.ACTION_HEADSET_PLUG);
            intent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
int state = 0;
int microphone = 0;








