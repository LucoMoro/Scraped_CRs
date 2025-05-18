//<Beginning of snippet n. 0>
} else {
    to = decode(mailToString.substring(length, index));
    if (to != null) {
        to = to.trim();
        if (!to.isEmpty()) {
            String[] emails = to.split(",");
            List<String> validEmails = Arrays.stream(emails)
                    .map(String::trim)
                    .filter(email -> !email.isEmpty())
                    .collect(Collectors.toList());
            if (!validEmails.isEmpty()) {
                addAddresses(mToView, validEmails.toArray(new String[0]));
            }
        }
    }
} catch (UnsupportedEncodingException e) {
    Log.e(Logging.LOG_TAG, e.getMessage() + " while decoding '" + mailToString + "'");
}
//<End of snippet n. 0>