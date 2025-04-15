/*Revert "Enable catact image display while dialing/alerting."

This reverts commit 88c3ddafabc224dfd60999d3ebba6835b1f541d8.*/
//Synthetic comment -- diff --git a/src/com/android/phone/CallCard.java b/src/com/android/phone/CallCard.java
//Synthetic comment -- index 146f465..26c04f1 100755

//Synthetic comment -- @@ -809,7 +809,7 @@

case DIALING:
case ALERTING:
                //retVal = context.getString(R.string.card_title_dialing);
break;

case INCOMING:







