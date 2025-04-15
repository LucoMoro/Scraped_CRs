/*Changes to CallCard: To fetch contact image instead of default image while dialing/alerting.

Change-Id:I7da5a5fa8de265439f05eed99fb8703f5fa950af*/
//Synthetic comment -- diff --git a/src/com/android/phone/CallCard.java b/src/com/android/phone/CallCard.java
//Synthetic comment -- index db5fa01..0b8c94e 100755

//Synthetic comment -- @@ -1224,7 +1224,7 @@

case DIALING:
case ALERTING:
                photoImageResource = R.drawable.picture_dialing;
break;

default:







