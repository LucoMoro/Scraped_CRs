/*Fix bug with redraw and long press to caps lock

If you long-press on the shift key, the keyboard will go straight into
caps lock mode. However, it doesn't mark the keyboard as dirty and queue
a redraw. This ends up only painting the uppercase characters for keys
that you actually press and not all the characters.

This patch adds a call to setShifted(currentCapsLock) just before the
call path to LatinKeyboard's setShiftLocked() so that the KeyboardView
can mark the entire keyboard view as dirty.*/
//Synthetic comment -- diff --git a/src/com/android/inputmethod/latin/LatinIME.java b/src/com/android/inputmethod/latin/LatinIME.java
//Synthetic comment -- index a36ff57..868edf2 100644

//Synthetic comment -- @@ -704,7 +704,7 @@
private void toggleCapsLock() {
mCapsLock = !mCapsLock;
if (mKeyboardSwitcher.isAlphabetMode()) {
            ((LatinKeyboard) mInputView.getKeyboard()).setShiftLocked(mCapsLock);
}
}









//Synthetic comment -- diff --git a/src/com/android/inputmethod/latin/LatinKeyboardView.java b/src/com/android/inputmethod/latin/LatinKeyboardView.java
//Synthetic comment -- index 363dcd0..6c988b5 100644

//Synthetic comment -- @@ -66,6 +66,11 @@
}
}


/****************************  INSTRUMENTATION  *******************************/








