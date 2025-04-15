/*Enable catact image display while dialing/alerting.

Change-Id:Ieb9847bfe788878e5884a0c765cd29b675fcaab3*/
//Synthetic comment -- diff --git a/src/com/android/phone/CallCard.java b/src/com/android/phone/CallCard.java
//Synthetic comment -- index 26c04f1..146f465 100755

//Synthetic comment -- @@ -809,7 +809,7 @@

case DIALING:
case ALERTING:
                retVal = context.getString(R.string.card_title_dialing);
break;

case INCOMING:







