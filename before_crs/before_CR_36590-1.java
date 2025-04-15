/*Send ACK messages to emulator on FB update events.

This is device-side improvement to FB update protocol. This change allows the emulator
not to send new FB update until device has fully processed the previous one.

Change-Id:Ib1aa66359e3c8b52e3ed00b037ced4adf59534dc*/
//Synthetic comment -- diff --git a/apps/SdkController/src/com/android/tools/sdkcontroller/activities/MultiTouchActivity.java b/apps/SdkController/src/com/android/tools/sdkcontroller/activities/MultiTouchActivity.java
//Synthetic comment -- index 06b29f1..c0079d9 100755

//Synthetic comment -- @@ -246,6 +246,7 @@
break;
case MultiTouchChannel.EVENT_FRAME_BUFFER:
onFrameBuffer(((ByteBuffer) msg.obj).array());
break;
}
return true; // we consumed this message








//Synthetic comment -- diff --git a/apps/SdkController/src/com/android/tools/sdkcontroller/handlers/MultiTouchChannel.java b/apps/SdkController/src/com/android/tools/sdkcontroller/handlers/MultiTouchChannel.java
//Synthetic comment -- index fc70ae1..522a06e 100755

//Synthetic comment -- @@ -109,6 +109,7 @@
Message msg = Message.obtain();
msg.what = EVENT_FRAME_BUFFER;
msg.obj = msg_data;
notifyUiHandlers(msg);
break;









//Synthetic comment -- diff --git a/apps/SdkController/src/com/android/tools/sdkcontroller/lib/ProtocolConstants.java b/apps/SdkController/src/com/android/tools/sdkcontroller/lib/ProtocolConstants.java
//Synthetic comment -- index b4c6593..32abf2b 100644

//Synthetic comment -- @@ -120,6 +120,10 @@
public static final int MT_POINTER_UP = 5;
/** Sends framebuffer update. */
public static final int MT_FB_UPDATE = 6;
/** Size of an event entry in the touch event message to the emulator. */
public static final int MT_EVENT_ENTRY_SIZE = 16;








