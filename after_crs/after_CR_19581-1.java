/*CDMA Authentication Key support

Allow applications to validate and store the CDMA Authentication Key
from the vendor Radio-Interface-Layer (RIL) layer. There is already
mostly support for this, we just need to expose it.

The motivation for this change is to provide needed support for some
operator network applications.

Change-Id:Idfae2a545464bec5ae51ef2510de3afe6a012990*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CommandsInterface.java b/telephony/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index 5de0426..a2855c1 100644

//Synthetic comment -- @@ -1401,4 +1401,14 @@
*          Callback message containing {@link IccCardStatus} structure for the card.
*/
public void getIccCardStatus(Message result);

    /**
     * Request to validate and store the Authentication Key
     *
     * @param akey
     *            String containing authentication key with the checksum
     * @param response
     *            Callback message to report success or failure
     */
    public void validateAndStoreAuthenticationKey(String akey, Message response);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index af483db..1060e26 100644

//Synthetic comment -- @@ -3499,4 +3499,17 @@

send(rr);
}

    /**
     * {@inheritDoc}
     */
    public void validateAndStoreAuthenticationKey(String akey, Message response) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_CDMA_VALIDATE_AND_WRITE_AKEY, response);

        rr.mp.writeString(akey);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index f31bf24..1ca988b 100755

//Synthetic comment -- @@ -1460,4 +1460,17 @@
}
return false;
}

    /**
     * Request to validate and store the Authentication Key
     *
     * @param akey
     *            String containing authentication key with the checksum
     * @param response
     *            Callback message to report success or failure
     */
    public void
    validateAndStoreAuthenticationKey(String akey, Message response) {
        mCM.validateAndStoreAuthenticationKey(akey, response);
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/sip/SipCommandInterface.java b/telephony/java/com/android/internal/telephony/sip/SipCommandInterface.java
//Synthetic comment -- index ed578c8..0707a75 100644

//Synthetic comment -- @@ -369,4 +369,7 @@

public void exitEmergencyCallbackMode(Message response) {
}

    public void validateAndStoreAuthenticationKey(String akey, Message response) {
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java b/telephony/java/com/android/internal/telephony/test/SimulatedCommands.java
//Synthetic comment -- index 9c72e5a..cd1fa1d 100644

//Synthetic comment -- @@ -1473,4 +1473,8 @@
public void getGsmBroadcastConfig(Message response) {
unimplemented(response);
}

    public void validateAndStoreAuthenticationKey(String akey, Message response) {
        unimplemented(response);
    }
}







