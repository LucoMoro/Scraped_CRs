//<Beginning of snippet n. 0>

RILRequest rr = RILRequest.obtain(RIL_REQUEST_SEND_USSD, response);

String obfuscatedUssdString = obfuscateUssdString(ussdString);
if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest) + " " + obfuscatedUssdString);

rr.mp.writeString(obfuscatedUssdString);

private String obfuscateUssdString(String ussdString) {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] hashBytes = digest.digest(ussdString.getBytes(StandardCharsets.UTF_8));
    return DatatypeConverter.printHexBinary(hashBytes);
}

//<End of snippet n. 0>