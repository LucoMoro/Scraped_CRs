/*Wait for this window to be shown before turning on the screen

Align the code as the comment "Keyguard may be in the process
of being shown, but not yet updated with the window manager...
give it a chance to do so"

Change-Id:Ief634792df798d705f9cf87a774278f08b9c79b5Signed-off-by: guoyin.chen <guoyin.chen@freescale.com>*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/KeyguardViewManager.java b/policy/src/com/android/internal/policy/impl/KeyguardViewManager.java
//Synthetic comment -- index fb6ff24..9bbe6c5d 100644

//Synthetic comment -- @@ -229,7 +229,7 @@

// Caller should wait for this window to be shown before turning
// on the screen.
            if (mKeyguardHost.getVisibility() != View.VISIBLE) {
// Keyguard may be in the process of being shown, but not yet
// updated with the window manager...  give it a chance to do so.
mKeyguardHost.post(new Runnable() {
//Synthetic comment -- @@ -242,7 +242,7 @@
}
});
} else {
                showListener.onShown(mKeyguardHost.getWindowToken());
}
} else {
showListener.onShown(null);







