/*Fix issue #18054

Removes useless toast displayed when sending USSD during a call.

Change-Id:Ic6ed1084558756987d9fdeaa43f7cdd76f3ea6e8http://code.google.com/p/android/issues/detail?id=18054*/




//Synthetic comment -- diff --git a/src/com/android/phone/InCallScreen.java b/src/com/android/phone/InCallScreen.java
//Synthetic comment -- index 26733ed..6b83afd 100755

//Synthetic comment -- @@ -2944,18 +2944,8 @@
break;

case DIALED_MMI:
                // The code in PhoneUtils.java handles user notification
                // in the form of a Dialog.
break;

case CALL_FAILED:







