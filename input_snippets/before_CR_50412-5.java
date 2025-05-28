
//<Beginning of snippet n. 0>



@Override
public String shell(String cmd) {
CommandOutputCapture capture = new CommandOutputCapture();
try {
            device.executeShellCommand(cmd, capture);
} catch (TimeoutException e) {
LOG.log(Level.SEVERE, "Error executing command: " + cmd, e);
return null;

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


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

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


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

//<End of snippet n. 2>








