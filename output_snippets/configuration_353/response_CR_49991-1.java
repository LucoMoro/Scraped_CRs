//<Beginning of snippet n. 0>
} else {
    to = decode(mailToString.substring(length, index));
}
if (to != null && !to.isEmpty()) {
    String[] addresses = to.split("\\s*,\\s*");
    for (String address : addresses) {
        if (isValidEmail(address)) {
            addAddresses(mToView, address);
        } else {
            Log.e(Logging.LOG_TAG, "Invalid email address: " + address);
        }
    }
} else {
    Log.e(Logging.LOG_TAG, "No valid recipients found in to field for: " + mailToString);
} 
} catch (UnsupportedEncodingException e) {
    Log.e(Logging.LOG_TAG, e.getMessage() + " while decoding '" + mailToString + "'");
}
//<End of snippet n. 0>