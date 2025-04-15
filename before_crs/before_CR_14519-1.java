/*Enable contact image in dial/alerting state.

Change-Id:I7425d77a79f18a636fce1990c38844c4fbfdb1f2*/
//Synthetic comment -- diff --git a/src/com/android/phone/CallCard.java b/src/com/android/phone/CallCard.java
//Synthetic comment -- index 26c04f1..146f465 100755

//Synthetic comment -- @@ -809,7 +809,7 @@

case DIALING:
case ALERTING:
                retVal = context.getString(R.string.card_title_dialing);
break;

case INCOMING:







