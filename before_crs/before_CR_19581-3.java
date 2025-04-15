/*CDMA Authentication Key support

Allow applications to validate and store the CDMA Authentication Key
from the vendor Radio-Interface-Layer (RIL) layer. There is already
mostly support for this, we just need to expose it.

The motivation for this change is to provide needed support for some
operator network applications.

Change-Id:Idfae2a545464bec5ae51ef2510de3afe6a012990*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CommandsInterface.java b/telephony/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index d6e6ae0..89af1d2 100644

//Synthetic comment -- @@ -1641,4 +1641,14 @@
* @param response a callback message with the String response in the obj field
*/
public void requestIsimAuthentication(String nonce, Message response);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index f2e7f45..499768b 100644

//Synthetic comment -- @@ -3747,4 +3747,17 @@

send(rr);
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 58e3e5f..4fe5571 100755

//Synthetic comment -- @@ -1450,4 +1450,17 @@
if (DBG)
Log.d(LOG_TAG, "[CDMAPhone] " + s);
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/sip/SipCommandInterface.java b/telephony/java/com/android/internal/telephony/sip/SipCommandInterface.java
//Synthetic comment -- index ab01012..7426653 100644

//Synthetic comment -- @@ -412,4 +412,7 @@

public void requestIsimAuthentication(String nonce, Message response) {
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java b/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index a0c7d5d..abc6763 100644

//Synthetic comment -- @@ -1517,4 +1517,8 @@
public void requestIsimAuthentication(String nonce, Message response) {
unimplemented(response);
}
}








//Synthetic comment -- diff --git a/telephony/tests/telephonytests/src/com/android/internal/telephony/gsm/UsimDataDownloadCommands.java b/telephony/tests/telephonytests/src/com/android/internal/telephony/gsm/UsimDataDownloadCommands.java
//Synthetic comment -- index 7e0d3c4..3a0a57a 100644

//Synthetic comment -- @@ -608,4 +608,8 @@
@Override
public void requestIsimAuthentication(String nonce, Message response) {
}
}







