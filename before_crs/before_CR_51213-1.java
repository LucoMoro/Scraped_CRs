/*telephony: Compare address when comparing cdma connections

When comparing cdma connections compare the address
even if it is a MO call.
If modem replaced the MO call with a different
call this can be identified by comparing
the address.

Change-Id:I87f232a572436e95fad766cc9766f13a836aede2CRs-Fixed: 344745*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaConnection.java b/src/java/com/android/internal/telephony/cdma/CdmaConnection.java
//Synthetic comment -- index 354806e..6ab0205 100755

//Synthetic comment -- @@ -205,17 +205,26 @@

/*package*/ boolean
compareTo(DriverCall c) {
        // On mobile originated (MO) calls, the phone number may have changed
        // due to a SIM Toolkit call control modification.
        //
        // We assume we know when MO calls are created (since we created them)
        // and therefore don't need to compare the phone number anyway.
        if (! (isIncoming || c.isMT)) return true;

        // ... but we can compare phone numbers on MT calls, and we have
        // no control over when they begin, so we might as well

String cAddress = PhoneNumberUtils.stringFromStringAndTOA(c.number, c.TOA);
return isIncoming == c.isMT && equalsHandlesNulls(address, cAddress);
}








