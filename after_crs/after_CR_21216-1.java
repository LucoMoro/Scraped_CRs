/*gsm: add report GPRS type in CREG's return

3GPP spec (7.2 Network registration +CREG)
wrote modem can report network type(Act) in CREG,
add this in the singnal report code for some modem.

Change-Id:I1b7238ee642b34287deed2e6cee0b5ef94900f49Signed-off-by: Zhang Jiejing <kzjeef.code@gmail.com>*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 6ddb312..2833adf 100644

//Synthetic comment -- @@ -668,6 +668,10 @@
cid = Integer.parseInt(states[2], 16);
}
}
			    if (states.lenght >= 4) {
				newNetworkType = Integer.parseInt(states[3], 16);
				newSS.setRadioTechnology(newNetworkType);
			    }
if (states.length > 14) {
if (states[14] != null && states[14].length() > 0) {
psc = Integer.parseInt(states[14], 16);







