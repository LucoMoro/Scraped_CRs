/*When switch input mode, also hide the input bar.*/




//Synthetic comment -- diff --git a/PinyinIME/src/com/android/inputmethod/pinyin/PinyinIME.java b/PinyinIME/src/com/android/inputmethod/pinyin/PinyinIME.java
//Synthetic comment -- index 8d7dcce..a2355fc 100644

//Synthetic comment -- @@ -267,6 +267,8 @@
updateIcon(mInputModeSwitcher.switchLanguageWithHkb());
resetToIdleState(true);

            requestHideSelf(0);

int allMetaState = KeyEvent.META_ALT_ON | KeyEvent.META_ALT_LEFT_ON
| KeyEvent.META_ALT_RIGHT_ON | KeyEvent.META_SHIFT_ON
| KeyEvent.META_SHIFT_LEFT_ON







