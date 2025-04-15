/*Extended the MonkeyRunner shell method with timeout arg.

In python via MonkeyRunner it is possible execute shell
commands but the running time was reduced to 5 seconds.
This patch adds an extra argument for the "shell" method.
It decides the maximum amount of time during which the
command is allowed to not output any response. A value of
0 means that the method will wait forever. This extra arg
is optionally. So the original function works without any changes.

Change-Id:I1a6d9b37e1846715342b252b2a2a346c2309a08fSigned-off-by: Zolt�n Papp <pzatya@gmail.com>*/
//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/adb/AdbChimpDevice.java b/chimpchat/src/com/android/chimpchat/adb/AdbChimpDevice.java
//Synthetic comment -- index 7c4b62a..97ad5b3 100644

//Synthetic comment -- @@ -264,9 +264,14 @@

@Override
public String shell(String cmd) {
CommandOutputCapture capture = new CommandOutputCapture();
try {
            device.executeShellCommand(cmd, capture);
} catch (TimeoutException e) {
LOG.log(Level.SEVERE, "Error executing command: " + cmd, e);
return null;








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/IChimpDevice.java b/chimpchat/src/com/android/chimpchat/core/IChimpDevice.java
//Synthetic comment -- index 14b58a7..a25990d 100644

//Synthetic comment -- @@ -131,12 +131,23 @@
/**
* Execute a shell command.
*
* @param cmd the command to execute
* @return the output of the command
*/
String shell(String cmd);

/**
* Install a given package.
*
* @param path the path to the installation package








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index e60d12e..7d931f5 100644

//Synthetic comment -- @@ -206,15 +206,22 @@
}

@MonkeyRunnerExported(doc = "Executes an adb shell command and returns the result, if any.",
            args = { "cmd"},
            argDocs = { "The adb shell command to execute." },
returns = "The output from the command.")
public String shell(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);

String cmd = ap.getString(0);
        return impl.shell(cmd);
}

@MonkeyRunnerExported(doc = "Reboots the specified device into a specified bootloader.",







