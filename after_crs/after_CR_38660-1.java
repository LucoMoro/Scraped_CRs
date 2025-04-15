/*Phone: The waiting dialog always show when choose hide number in caller ID

It should set mIsForeground true when start to init.

Change-Id:I88d7c04cc8ccc56a996e9e9a89769dc0e4d6568bSigned-off-by: Charles Li<liyuchenlp@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/phone/GsmUmtsAdditionalCallOptions.java b/src/com/android/phone/GsmUmtsAdditionalCallOptions.java
//Synthetic comment -- index 69a025c..7965d06 100644

//Synthetic comment -- @@ -39,6 +39,7 @@

if (icicle == null) {
if (DBG) Log.d(LOG_TAG, "start to init ");
            mIsForeground = true;
mCLIRButton.init(this, false);
} else {
if (DBG) Log.d(LOG_TAG, "restore stored states");







