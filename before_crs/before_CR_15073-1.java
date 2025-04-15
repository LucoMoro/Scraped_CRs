/*Fix that InputDeviceReaderThread that got killed during startup

InputDeviceReaderThread could be killed if a key or touch event
was received before initiation made by PolicyThread was made. In
that case we get an exception that we now handle and continue to
execute rather than falling out of messageloop and exit thread.

Change-Id:Ifa7de7ccfadd66ecc2b14c6273e9be32b8e0cb4a*/
//Synthetic comment -- diff --git a/services/java/com/android/server/KeyInputQueue.java b/services/java/com/android/server/KeyInputQueue.java
//Synthetic comment -- index 1bb897b..07a165f 100644

//Synthetic comment -- @@ -564,8 +564,13 @@
}

// first crack at it
                        send = preprocessEvent(di, ev);

if (ev.type == RawInputEvent.EV_KEY) {
di.mMetaKeysState = makeMetaState(ev.keycode,
ev.value != 0, di.mMetaKeysState);







