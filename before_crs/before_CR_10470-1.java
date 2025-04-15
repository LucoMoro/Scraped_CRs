/*Better implement for:
When switch input mode, also hide the input bar.

Can function when soft keyboard switch.*/
//Synthetic comment -- diff --git a/PinyinIME/src/com/android/inputmethod/pinyin/PinyinIME.java b/PinyinIME/src/com/android/inputmethod/pinyin/PinyinIME.java
//Synthetic comment -- index a2355fc..6a9fe68 100644

//Synthetic comment -- @@ -267,8 +267,6 @@
updateIcon(mInputModeSwitcher.switchLanguageWithHkb());
resetToIdleState(true);

            requestHideSelf(0);

int allMetaState = KeyEvent.META_ALT_ON | KeyEvent.META_ALT_LEFT_ON
| KeyEvent.META_ALT_RIGHT_ON | KeyEvent.META_SHIFT_ON
| KeyEvent.META_SHIFT_LEFT_ON
//Synthetic comment -- @@ -1026,6 +1024,7 @@
} catch (Exception e) {
Log.e(TAG, "Fail to show the PopupWindow.");
}

if (null != mSkbContainer && mSkbContainer.isShown()) {
mSkbContainer.toggleCandidateMode(false);







