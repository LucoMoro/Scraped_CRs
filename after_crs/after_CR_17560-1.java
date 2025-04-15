/*Fix: Text is cut in Edit word edit box when adding word to dictionary

When a word is added to dictionary its display is cut in edit box of
Edit word dialog on some screens.

Change-Id:I8c8dde9df6b2ccad21bb2113a240d24a99d51012*/




//Synthetic comment -- diff --git a/src/com/android/settings/UserDictionarySettings.java b/src/com/android/settings/UserDictionarySettings.java
//Synthetic comment -- index 6ffcb3d..49363a9 100644

//Synthetic comment -- @@ -224,7 +224,7 @@
if (mAutoReturn) finish();                        
}})
.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
return dialog;
}







