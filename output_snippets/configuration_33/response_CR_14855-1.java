//<Beginning of snippet n. 0>
else if ("Intent".equalsIgnoreCase(commands[0])) {
    for (int i = 1; i < commands.length; i++) {
        try {
            Intent intent = Intent.parseUri(commands[i], 0);
            if (intent.getStringExtra(SENTENCE_EXTRA) == null) {
                intent.putExtra(SENTENCE_EXTRA, literal);
            }
        } catch (URISyntaxException e) {
            // Handle the exception appropriately (log, notify user, etc.)
        }
    }
}
//<End of snippet n. 0>