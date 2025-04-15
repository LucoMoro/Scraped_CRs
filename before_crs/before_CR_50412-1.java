/*Extended the MonkeyRunner shell method with timeout arg.

In python via MonkeyRunner it is possible execute shell
commands but the running time was reduced to 5 seconds.
This patch adds an extra argument for the "shell" method.
It decides the maximum amount of time during which the
command is allowed to not output any response. A value of
0 means that the method will wait forever. This extra agr
is optionally. So the original function works without any changes.

Change-Id:I1a6d9b37e1846715342b252b2a2a346c2309a08fSigned-off-by: pzoli <pzatya@gmail.com>*/
//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/adb/AdbChimpDevice.java b/chimpchat/src/com/android/chimpchat/adb/AdbChimpDevice.java
//Synthetic comment -- index 7c4b62a..a4daa0f 100644

//Synthetic comment -- @@ -284,6 +284,27 @@
}

@Override
public boolean installPackage(String path) {
try {
String result = device.installPackage(path, true);








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/IChimpDevice.java b/chimpchat/src/com/android/chimpchat/core/IChimpDevice.java
//Synthetic comment -- index 14b58a7..3b8d88e 100644

//Synthetic comment -- @@ -137,6 +137,15 @@
String shell(String cmd);

/**
* Install a given package.
*
* @param path the path to the installation package








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index e60d12e..90886a1 100644

//Synthetic comment -- @@ -206,15 +206,19 @@
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







