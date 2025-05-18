//<Beginning of snippet n. 0>

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

RILRequest rr = RILRequest.obtain(RIL_REQUEST_SEND_USSD, response);

if (shouldLog()) {
    String obfuscatedUssdString = obfuscateUssdString(ussdString);
    riljLog(rr.serialString() + "> " + requestToString(rr.mRequest) + " " + obfuscatedUssdString);
}

rr.mp.writeString(ussdString);

private String obfuscateUssdString(String ussdString) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(ussdString.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
        return ussdString; // Fallback to original in case of error
    }
}

private boolean shouldLog() {
    // Configuration check to determine whether logging is enabled
    return !isProductionEnvironment();
}

//<End of snippet n. 0>