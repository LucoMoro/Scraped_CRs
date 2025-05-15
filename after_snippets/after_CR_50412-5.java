
//<Beginning of snippet n. 0>



@Override
public String shell(String cmd) {
        // 5000 is the default timeout from the ddmlib.
        // This timeout arg is needed to the backwards compatibility.
        return shell(cmd, 5000);
    }

    @Override
    public String shell(String cmd, int timeout) {
CommandOutputCapture capture = new CommandOutputCapture();
try {
            device.executeShellCommand(cmd, capture, timeout);
} catch (TimeoutException e) {
LOG.log(Level.SEVERE, "Error executing command: " + cmd, e);
return null;

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


/**
* Execute a shell command.
*
     * The default timeout is 5 secounds.
     *
* @param cmd the command to execute
* @return the output of the command
*/
String shell(String cmd);

/**
     * Execute a shell command.
     *
     * @param cmd the command to execute
     * @param maximum time to output response
     * @return the output of the command
     */
    String shell(String cmd, int timeout);

    /**
* Install a given package.
*
* @param path the path to the installation package

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


}

@MonkeyRunnerExported(doc = "Executes an adb shell command and returns the result, if any.",
            args = { "cmd", "timeout"},
            argDocs = { "The adb shell command to execute.",
            "This arg is optional. It decides the maximum amount of time during which the" +
            "command is not allowed to output any response. A value of 0 means the method" +
            "will wait forever. The unit of the timeout is milisecond"},
returns = "The output from the command.")
public String shell(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
String cmd = ap.getString(0);

        if (args.length == 2) {
            return impl.shell(cmd, ap.getInt(1));
        } else {
            return impl.shell(cmd);
        }
}

@MonkeyRunnerExported(doc = "Reboots the specified device into a specified bootloader.",

//<End of snippet n. 2>








