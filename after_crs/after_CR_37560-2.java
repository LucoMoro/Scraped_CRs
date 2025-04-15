/*Fix instrument ignoring extra arguments

Extra arguments passed to instrument are silently ignored. This could lead
to some confusion when instrument is invoked with extra arguments that are
expected to alter the instrumentation being run.

A common use case for this is when 'instrument' is invoked from monkeyrunner
to run all the tests in one class:

   device.instrument(pkg, { 'class':'com.example.test.MyTests' })

without this patch, the previous method will run all the tests in pkg.

Change-Id:I908d49422fe2755a1bcf562c2d040651b9691a6a*/




//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/adb/AdbChimpDevice.java b/chimpchat/src/com/android/chimpchat/adb/AdbChimpDevice.java
//Synthetic comment -- index d4513d1..7c4b62a 100644

//Synthetic comment -- @@ -481,7 +481,17 @@

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







