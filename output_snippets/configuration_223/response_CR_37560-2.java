//<Beginning of snippet n. 0>
@Override
public Map<String, Object> instrument(String packageName, Map<String, Object> args) {
    List<String> shellCmd = Lists.newArrayList("am", "instrument", "-w", "-r", packageName);
    
    for (Map.Entry<String, Object> entry : args.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();
        if (key != null && value != null) {
            shellCmd.add("--" + key);
            shellCmd.add(value.toString());
        } else {
            // Log the error for invalid key or value
            System.err.println("Invalid argument: key or value is null.");
            return Collections.singletonMap("error", "Invalid argument in args map");
        }
    }

    String result = shell(shellCmd.toArray(new String[0]));
    return convertInstrumentResult(result);
}
//<End of snippet n. 0>