/*Keyguard: Don't keep the screen on while some hard key is pressed

If some hard keys were pressed,
KeyguardViewBase.dispatchKeyEvent() was calling
KeyguardViewMediator's pokeWakeLock() method that was acquiring
a full wake lock for a certain amount of time.
When a hard key is pressed for a long time, system calls
dispatchKeyEvent() repeatedly all the time that key is down.
Hence, a screen could be on for hours if a specific hard key
was unintentionally in a pressed state (like if a phone was
squeezed in a pocket). This was leading to fast battery draining
and overheating of a phone.

Change-Id:I417df488499979cbfabebfe9788e8932c2151f19*/
//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/KeyguardViewBase.java b/policy/src/com/android/internal/policy/impl/KeyguardViewBase.java
//Synthetic comment -- index 29a5573..f91a245 100644

//Synthetic comment -- @@ -153,7 +153,7 @@
}

private boolean shouldEventKeepScreenOnWhileKeyguardShowing(KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_DOWN) {
return false;
}
switch (event.getKeyCode()) {







