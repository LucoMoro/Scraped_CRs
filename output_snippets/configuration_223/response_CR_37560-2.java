//<Beginning of snippet n. 0>
@Override
public Map<String, Object> instrument(String packageName, Map<String, Object> args) {
    List<String> shellCmd = new ArrayList<>(Arrays.asList("am", "instrument", "-w", "-r", packageName));

    // Validate and incorporate recognized arguments
    if (args != null) {
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            switch (key) {
                case "class":
                    shellCmd.add("-e");
                    shellCmd.add("class");
                    shellCmd.add(value.toString());
                    break;
                // Add more recognized arguments here
                default:
                    // Log warning for unrecognized keys
                    System.err.println("Warning: Unrecognized argument '" + key + "' ignored.");
                    break;
            }
        }
    }

    String result = shell(shellCmd.toArray(new String[0]));
    return convertInstrumentResult(result);
}
//<End of snippet n. 0>