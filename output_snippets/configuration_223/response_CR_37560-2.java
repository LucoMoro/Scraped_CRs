//<Beginning of snippet n. 0>
@Override
public Map<String, Object> instrument(String packageName, Map<String, Object> args) {
    List<String> shellCmd = Lists.newArrayList("am", "instrument", "-w", "-r", packageName);
    StringBuilder feedback = new StringBuilder();
    boolean unsupportedArgsPresent = false;
    
    for (Map.Entry<String, Object> entry : args.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();
        
        if (key.equals("class")) {
            shellCmd.add("-c");
            shellCmd.add(value.toString());
            feedback.append("Class argument used: ").append(value).append("\n");
        } else {
            unsupportedArgsPresent = true;
            feedback.append("Unsupported argument '").append(key).append("', ignored.\n");
        }
    }

    String result = shell(shellCmd.toArray(ZERO_LENGTH_STRING_ARRAY));
    feedback.append("Shell command executed, result obtained.");
    Map<String, Object> response = convertInstrumentResult(result);
    response.put("feedback", feedback.toString());
    response.put("status", unsupportedArgsPresent ? "partial_success" : "success");
    return response;
}
//<End of snippet n. 0>