/*Fix issue #18054

Removes useless toast displayed when sending USSD during a call.

Change-Id:Ic6ed1084558756987d9fdeaa43f7cdd76f3ea6e8http://code.google.com/p/android/issues/detail?id=18054*/
//Synthetic comment -- diff --git a/src/com/android/phone/InCallScreen.java b/src/com/android/phone/InCallScreen.java
//Synthetic comment -- index 26733ed..6b83afd 100755

//Synthetic comment -- @@ -2944,18 +2944,8 @@
break;

case DIALED_MMI:
                // Our initial phone number was actually an MMI sequence.
                // There's no real "error" here, but we do bring up the
                // a Toast (as requested of the New UI paradigm).
                //
                // In-call MMIs do not trigger the normal MMI Initiate
                // Notifications, so we should notify the user here.
                // Otherwise, the code in PhoneUtils.java should handle
                // user notifications in the form of Toasts or Dialogs.
                if (mCM.getState() == Phone.State.OFFHOOK) {
                    Toast.makeText(mApp, R.string.incall_status_dialed_mmi, Toast.LENGTH_SHORT)
                            .show();
                }
break;

case CALL_FAILED:







