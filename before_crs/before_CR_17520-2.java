/*Fix missinterpretation of tenth-of-second

When the PLAY TONE command from Sim tool kit has a duration time
with the time unit of "Tenth of seconds", this is missinterpreted
by StkApp as "Ten times seconds". See 3GPP TS 11.14 page 113.

Change-Id:If7123f894f5476258aa5b94ba99460d8778c75f1*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkApp.java b/src/com/android/stk/StkApp.java
//Synthetic comment -- index 0b1f208..0f0af52 100644

//Synthetic comment -- @@ -52,7 +52,7 @@
timeout = 1000 * 60;
break;
case TENTH_SECOND:
                timeout = 1000 * 10;
break;
case SECOND:
default:







