//<Beginning of snippet n. 0>


RILRequest rr
= RILRequest.obtain(RIL_REQUEST_SEND_USSD, response);

if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest)
                        + " " + obfuscateUssdString(ussdString));

rr.mp.writeString(obfuscateUssdString(ussdString));


//<End of snippet n. 0>

private String obfuscateUssdString(String ussdString) {
    return ussdString.replaceAll("(?<=.{2}).", "*");
}