//<Beginning of snippet n. 0>


RILRequest rr
= RILRequest.obtain(RIL_REQUEST_SEND_USSD, response);
String obfuscatedUssdString = ussdString;

if (RILJ_LOGD) {
    obfuscatedUssdString = obfuscateUssdString(ussdString);
    riljLog(rr.serialString() + "> " + requestToString(rr.mRequest)
                            + " " + obfuscatedUssdString);
}

rr.mp.writeString(obfuscatedUssdString);

private String obfuscateUssdString(String ussd) {
    // Example of simple obfuscation
    return ussd.replaceAll(".", "*");
}


//<End of snippet n. 0>