//<Beginning of snippet n. 0>
@Override
public String shell(String cmd, int timeout) {
    if (timeout < 0) {
        throw new IllegalArgumentException("Timeout value cannot be negative.");
    }
    CommandOutputCapture capture = new CommandOutputCapture();
    try {
        if (timeout > 0) {
            device.executeShellCommand(cmd, capture, timeout);
        } else {
            device.executeShellCommand(cmd, capture);
        }
    } catch (TimeoutException e) {
        LOG.log(Level.SEVERE, "Error executing command: " + cmd, e);
        return "Command timed out";
    }
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
/**
* Execute a shell command.
*
* @param cmd the command to execute
* @param timeout the time in milliseconds to wait before timing out
* @return the output of the command
*/
String shell(String cmd, int timeout);

//<End of snippet n. 1>

//<Beginning of snippet n. 2>
@MonkeyRunnerExported(doc = "Executes an adb shell command and returns the result, if any.",
            args = { "cmd", "timeout"},
            argDocs = { "The adb shell command to execute.", "Timeout in milliseconds." },
returns = "The output from the command.")
public String shell(PyObject[] args, String[] kws) {
    ArgParser ap = JythonUtils.createArgParser(args, kws);
    Preconditions.checkNotNull(ap);

    String cmd = ap.getString(0);
    int timeout = ap.getInt(1, 0); // default to 0 if not provided
    return impl.shell(cmd, timeout);
}
//<End of snippet n. 2>