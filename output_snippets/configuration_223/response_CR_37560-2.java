//<Beginning of snippet n. 0>
@Override
public Map<String, Object> instrument(String packageName, Map<String, Object> args) {
    List<String> shellCmd = Lists.newArrayList("am", "instrument", "-w", "-r", packageName);

    if (args != null) {
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.equals("class") && value instanceof String) {
                shellCmd.add("-c");
                shellCmd.add((String) value);
            } else if (key.equals("timeout") && value instanceof Integer) {
                shellCmd.add("-t");
                shellCmd.add(String.valueOf(value));
            } else if (key.equals("log-level") && value instanceof String) {
                shellCmd.add("-l");
                shellCmd.add((String) value);
            } else {
                // Log unrecognized key instead of throwing an exception
                // Assuming a logger instance is available
                logger.warn("Unrecognized key: " + key);
            }
        }
    }

    String result = shell(shellCmd.toArray(new String[0]));
    return convertInstrumentResult(result);
}
//<End of snippet n. 0>