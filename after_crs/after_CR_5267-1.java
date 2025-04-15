/*Support power off/on with POWER button*/




//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/PhoneWindowManager.java b/phone/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index 1731206..05ea393 100644

//Synthetic comment -- @@ -1179,6 +1179,27 @@
mBroadcastWakeLock.acquire();
mHandler.post(new PassHeadsetKey(keyEvent));
}
            } else if (code == KeyEvent.KEYCODE_POWER) {
                if (down) {
                    if (!screenIsOn) {
                        mShouldTurnOffOnKeyUp = false;
                    } else {
                        mShouldTurnOffOnKeyUp = true;
                        mHandler.postDelayed(mEndCallLongPress,
                                ViewConfiguration.getGlobalActionKeyTimeout());
                        result &= ~ACTION_PASS_TO_USER;
                    }
                } else {
                    mHandler.removeCallbacks(mEndCallLongPress);
                    if (mShouldTurnOffOnKeyUp) {
                        mShouldTurnOffOnKeyUp = false;
                        if (screenIsOn) {
                            result &= ~ACTION_POKE_USER_ACTIVITY;
                            result |= ACTION_GO_TO_SLEEP;
                        }
                        result &= ~ACTION_PASS_TO_USER;
                    }
                }
}
}








