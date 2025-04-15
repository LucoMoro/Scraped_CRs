/*When switch input mode, also hide the input bar.

Can function when soft keyboard switch.*/




//Synthetic comment -- diff --git a/src/com/android/inputmethod/pinyin/PinyinIME.java b/src/com/android/inputmethod/pinyin/PinyinIME.java
//Synthetic comment -- index 9ac2c2d..0288a44 100644

//Synthetic comment -- @@ -1070,6 +1070,7 @@
} catch (Exception e) {
Log.e(TAG, "Fail to show the PopupWindow.");
}
        setCandidatesViewShown(false);

if (null != mSkbContainer && mSkbContainer.isShown()) {
mSkbContainer.toggleCandidateMode(false);







