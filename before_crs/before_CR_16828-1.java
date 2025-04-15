/*Adding pan behavior on preference dialog.

Changed the preference dialog with text input to pan if
the display area is limited. This helps the user to see
the input better.

Change-Id:I12341546f6f82601ac5a2746153255a9b2d49a1c*/
//Synthetic comment -- diff --git a/core/java/android/preference/DialogPreference.java b/core/java/android/preference/DialogPreference.java
//Synthetic comment -- index bbad2b6..9a27891 100644

//Synthetic comment -- @@ -321,8 +321,9 @@
*/
private void requestInputMethod(Dialog dialog) {
Window window = dialog.getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
}

/**







