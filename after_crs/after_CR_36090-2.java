/*Fix issue #18054: USSD in-call toast

Removes useless toast displayed when sending USSD during a call.http://code.google.com/p/android/issues/detail?id=18054Change-Id:Ic6ed1084558756987d9fdeaa43f7cdd76f3ea6e8*/




//Synthetic comment -- diff --git a/src/com/android/phone/InCallScreen.java b/src/com/android/phone/InCallScreen.java
//Synthetic comment -- index 2cc22a0..da9362a 100755

//Synthetic comment -- @@ -3093,18 +3093,8 @@
break;

case DIALED_MMI:
                // The code in PhoneUtils.java handles user notification
                // in the form of a Dialog.
break;

case CALL_FAILED:







