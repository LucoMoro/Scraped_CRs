//@<Beginning of snippet n. 0>
@Override
public Map<String, Object> instrument(String packageName, Map<String, Object> args) {
    List<String> shellCmd = new ArrayList<>(Arrays.asList("am", "instrument", "-w", "-r", packageName));
    List<String> unrecognizedKeys = new ArrayList<>();
    Map<String, Object> resultMap = new HashMap<>();

    if (args == null) {
        resultMap.put("error", "Arguments map is null. Please provide valid arguments.");
        return resultMap;
    }

    for (Map.Entry<String, Object> entry : args.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();
        switch (key) {
            case "class":
                shellCmd.add("-c");
                shellCmd.add((String) value);
                break;
            case "timeout":
                if (value instanceof Integer) {
                    shellCmd.add("-t");
                    shellCmd.add(String.valueOf(value));
                } else {
                    unrecognizedKeys.add("Invalid value for key 'timeout'. Expected an Integer.");
                }
                break;
            case "log-level":
                if (value instanceof String) {
                    shellCmd.add("-l");
                    shellCmd.add((String) value);
                } else {
                    unrecognizedKeys.add("Invalid value for key 'log-level'. Expected a String.");
                }
                break;
            default:
                unrecognizedKeys.add("Unrecognized key: " + key);
                break;
        }
    }

    if (!unrecognizedKeys.isEmpty()) {
        resultMap.put("unrecognizedKeys", unrecognizedKeys);
    }

    String result = shell(shellCmd.toArray(new String[0]));
    resultMap.put("result", convertInstrumentResult(result));
    return resultMap;
}
//<End of snippet n. 0>