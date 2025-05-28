//@<Beginning of snippet n. 0>
@Override
public Map<String, Object> instrument(String packageName, Map<String, Object> args) {
    List<String> shellCmd = new ArrayList<>(Arrays.asList("am", "instrument", "-w", "-r", packageName));
    List<String> unrecognizedKeys = new ArrayList<>();

    if (args != null) {
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.equals("class")) {
                shellCmd.add("-c");
                shellCmd.add((String) value);
            } else if (key.equals("timeout")) {
                if (value instanceof Integer) {
                    shellCmd.add("-t");
                    shellCmd.add(String.valueOf(value));
                } else {
                    unrecognizedKeys.add("Invalid value for key 'timeout'. Expected an Integer.");
                }
            } else if (key.equals("log-level")) {
                if (value instanceof String) {
                    shellCmd.add("-l");
                    shellCmd.add((String) value);
                } else {
                    unrecognizedKeys.add("Invalid value for key 'log-level'. Expected a String.");
                }
            } else {
                unrecognizedKeys.add("Unrecognized key: " + key);
            }
        }
    } else {
        return Collections.emptyMap();
    }

    if (!unrecognizedKeys.isEmpty()) {
        for (String errorMsg : unrecognizedKeys) {
            System.err.println(errorMsg);
        }
    }

    String result = shell(shellCmd.toArray(new String[0]));
    return convertInstrumentResult(result);
}
//<End of snippet n. 0>