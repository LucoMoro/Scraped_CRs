/*Avoid empty network name

With this fix we will avoid to have an empty network name
in the expanded status bar. This occurs when the SIM does
not work or does not have any connection available.
The solution used is to set another text label when this
happens, “lockscreen_carrier_default”.

Change-Id:Ic7fc14add6207d19e74b720eb71564f932cd3c92*/
//Synthetic comment -- diff --git a/services/java/com/android/server/status/StatusBarService.java b/services/java/com/android/server/status/StatusBarService.java
//Synthetic comment -- index b9a57d6..9bc7d9c 100644

//Synthetic comment -- @@ -1772,6 +1772,7 @@
mPlmnLabel.setText(plmn);
} else {
mPlmnLabel.setText(R.string.lockscreen_carrier_default);
}
} else {
mPlmnLabel.setText("");








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index d539f6f1..9408e4a 100644

//Synthetic comment -- @@ -706,8 +706,11 @@
String opNames[] = (String[])ar.result;

if (opNames != null && opNames.length >= 3) {
                        newSS.setOperatorName (
                                opNames[0], opNames[1], opNames[2]);
}
break;








