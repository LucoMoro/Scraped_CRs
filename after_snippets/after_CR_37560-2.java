
//<Beginning of snippet n. 0>



@Override
public Map<String, Object> instrument(String packageName, Map<String, Object> args) {
        List<String> shellCmd = Lists.newArrayList("am", "instrument", "-w", "-r");
        for (Entry<String, Object> entry: args.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            if (key != null && value != null) {
                shellCmd.add("-e");
                shellCmd.add(key);
                shellCmd.add(value.toString());
            }
        }
        shellCmd.add(packageName);
String result = shell(shellCmd.toArray(ZERO_LENGTH_STRING_ARRAY));
return convertInstrumentResult(result);
}

//<End of snippet n. 0>








