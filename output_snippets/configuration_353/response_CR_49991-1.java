//<Beginning of snippet n. 0>
} else {
    to = decode(mailToString.substring(length, index));
}

if (to == null || to.trim().isEmpty() || to.trim().equals(",")) {
    return; // Early exit if `to` is null, empty or only contains commas
}

String[] addresses = to.split("\\s*,\\s*");
List<String> validAddresses = new ArrayList<>();
for (String address : addresses) {
    if (!address.trim().isEmpty() && isValidEmail(address)) {
        validAddresses.add(address);
    }
}

if (!validAddresses.isEmpty()) {
    addAddresses(mToView, validAddresses.toArray(new String[0]));
}
} catch (UnsupportedEncodingException e) {
    Log.e(Logging.LOG_TAG, e.getMessage() + " while decoding '" + mailToString + "'");
}
//<End of snippet n. 0>