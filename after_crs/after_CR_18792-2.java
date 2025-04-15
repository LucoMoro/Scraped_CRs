/*Avoid empty network name

With this fix we will avoid to have an empty network name
in the expanded status bar. This occurs when the SIM does
not work or does not have any connection available.

Change-Id:Ic7fc14add6207d19e74b720eb71564f932cd3c92*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 18ef121..8867898 100644

//Synthetic comment -- @@ -718,8 +718,11 @@
String opNames[] = (String[])ar.result;

if (opNames != null && opNames.length >= 3) {
                        if (opNames[0] != null && opNames[0].length() > 0) {
                            newSS.setOperatorName(opNames[0], opNames[1], opNames[2]);
                        } else {
                            newSS.setOperatorName(null, opNames[1], opNames[2]);
                        }
}
break;








