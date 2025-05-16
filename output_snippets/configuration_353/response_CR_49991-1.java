//<Beginning of snippet n. 0>
} else {
    to = decode(mailToString.substring(length, index));
    if (to != null && !to.trim().isEmpty()) {
        String[] addresses = to.trim().split(",");
        for (String address : addresses) {
            String trimmedAddress = address.trim();
            if (!trimmedAddress.isEmpty()) {
                mToView.add(trimmedAddress);
            }
        }
    }
} catch (UnsupportedEncodingException e) {
    Log.e(Logging.LOG_TAG, e.getMessage() + " while decoding '" + mailToString + "'");
}
//<End of snippet n. 0>