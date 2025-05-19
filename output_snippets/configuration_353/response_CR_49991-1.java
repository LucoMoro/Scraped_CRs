//<Beginning of snippet n. 0>
} else {
    to = decode(mailToString.substring(length, index));
}

String trimmedTo = to != null ? to.trim() : "";

if (trimmedTo.isEmpty() || trimmedTo.matches("^,*$")) {
    return; // Early exit if `to` is null, empty or only contains commas
}

String[] addresses = trimmedTo.split("\\s*,\\s*");
List<String> validAddresses = new ArrayList<>();
for (String address : addresses) {
    String trimmedAddress = address.trim();
    if (!trimmedAddress.isEmpty() && isValidEmail(trimmedAddress)) {
        validAddresses.add(trimmedAddress);
    }
}

if (!validAddresses.isEmpty()) {
    addAddresses(mToView, validAddresses.toArray(new String[0]));
}
} catch (UnsupportedEncodingException e) {
    Log.e(Logging.LOG_TAG, e.getMessage() + " while decoding '" + mailToString + "'");
}
//<End of snippet n. 0>