//<Beginning of snippet n. 0>
@Override
public Map<String, Object> instrument(String packageName, Map<String, Object> args) {
    List<String> shellCmd = Lists.newArrayList("am", "instrument", "-w", "-r", packageName);

    if (args != null) {
        if (args.containsKey("class")) {
            shellCmd.add("-c");
            shellCmd.add((String) args.get("class"));
        }
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            if (!entry.getKey().equals("class")) {
                shellCmd.add("-e");
                shellCmd.add(entry.getKey());
                shellCmd.add(String.valueOf(entry.getValue()));
            }
        }
    }

    String result = shell(shellCmd.toArray(ZERO_LENGTH_STRING_ARRAY));
    return convertInstrumentResult(result);
}
//<End of snippet n. 0>