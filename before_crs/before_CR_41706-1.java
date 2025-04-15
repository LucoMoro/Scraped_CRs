/*Disable soft keyboard in SIM unlock screen

Simunlock screen has its own numeric keypad, so softinput is not
needed on the screen. Since needsInput was returning true the
softkeypad shows up on top of this screen. Changing return value to false.

Change-Id:I32fd1feba6ae158ee2b78e2d846de0e71a43b59a*/
//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/SimUnlockScreen.java b/policy/src/com/android/internal/policy/impl/SimUnlockScreen.java
//Synthetic comment -- index 80407f5..890baf8 100644

//Synthetic comment -- @@ -107,7 +107,7 @@

/** {@inheritDoc} */
public boolean needsInput() {
        return true;
}

/** {@inheritDoc} */







