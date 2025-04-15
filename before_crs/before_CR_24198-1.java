/*Fix issue 18054http://code.google.com/p/android/issues/list?thanks=18054Change-Id:Id216f67f004740dc687d2552ee7e4cc364778ab0*/
//Synthetic comment -- diff --git a/src/com/android/phone/InCallScreen.java b/src/com/android/phone/InCallScreen.java
//Synthetic comment -- index 9e8ff40..213e565 100755

//Synthetic comment -- @@ -3476,18 +3476,8 @@
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
                    Toast.makeText(this, R.string.incall_status_dialed_mmi, Toast.LENGTH_SHORT)
                        .show();
                }
break;

case CALL_FAILED:







