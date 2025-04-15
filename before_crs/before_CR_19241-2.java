/*Preserve flags field of event passed to injectKeyEvent()

This patch allows users of WindowManagerService.injectKeyEvent() to
set flags on the key event being injected.

In particular this allows long presses (FLAG_LONG_PRESS) to be
injected into the window manager.

Change-Id:I3079552f23ddb5f9cb6d9abdb350278bb4faf010*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index dd4aeab..6e1b63a 100644

//Synthetic comment -- @@ -5488,12 +5488,13 @@
int metaState = ev.getMetaState();
int deviceId = ev.getDeviceId();
int scancode = ev.getScanCode();

if (eventTime == 0) eventTime = SystemClock.uptimeMillis();
if (downTime == 0) downTime = eventTime;

KeyEvent newEvent = new KeyEvent(downTime, eventTime, action, code, repeatCount, metaState,
                deviceId, scancode, KeyEvent.FLAG_FROM_SYSTEM);

final int pid = Binder.getCallingPid();
final int uid = Binder.getCallingUid();







