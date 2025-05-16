//<Beginning of snippet n. 0>


RILRequest rr
= RILRequest.obtain(RIL_REQUEST_SEND_USSD, response);

if (RILJ_LOGV) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest) + " USSD request sent");

rr.mp.writeString(obfuscateUssdString(ussdString));

//<End of snippet n. 0>

private String obfuscateUssdString(String ussd) {
    return ussd.replaceAll(".", "*");
}