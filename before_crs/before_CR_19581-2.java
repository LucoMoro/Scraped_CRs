/*CDMA Authentication Key support

Allow applications to validate and store the CDMA Authentication Key
from the vendor Radio-Interface-Layer (RIL) layer. There is already
mostly support for this, we just need to expose it.

The motivation for this change is to provide needed support for some
operator network applications.

Change-Id:Idfae2a545464bec5ae51ef2510de3afe6a012990*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CommandsInterface.java b/telephony/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index 56c641b..64c8425 100644

//Synthetic comment -- @@ -1408,4 +1408,14 @@
*          Callback message containing {@link IccCardStatus} structure for the card.
*/
public void getIccCardStatus(Message result);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index f5490016..5f227e0 100644

//Synthetic comment -- @@ -3560,4 +3560,17 @@

send(rr);
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 0865d6f..d81af85 100755

//Synthetic comment -- @@ -1446,4 +1446,17 @@
}
return false;
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/sip/SipCommandInterface.java b/telephony/java/com/android/internal/telephony/sip/SipCommandInterface.java
//Synthetic comment -- index ed578c8..0707a75 100644

//Synthetic comment -- @@ -369,4 +369,7 @@

public void exitEmergencyCallbackMode(Message response) {
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java b/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index beec177..e598bde 100644

//Synthetic comment -- @@ -1478,4 +1478,8 @@
public void getGsmBroadcastConfig(Message response) {
unimplemented(response);
}
}







