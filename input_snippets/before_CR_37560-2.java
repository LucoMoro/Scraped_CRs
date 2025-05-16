
//<Beginning of snippet n. 0>



@Override
public Map<String, Object> instrument(String packageName, Map<String, Object> args) {
        List<String> shellCmd = Lists.newArrayList("am", "instrument", "-w", "-r", packageName);
String result = shell(shellCmd.toArray(ZERO_LENGTH_STRING_ARRAY));
return convertInstrumentResult(result);
}

//<End of snippet n. 0>








