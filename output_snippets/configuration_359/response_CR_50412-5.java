//<Beginning of snippet n. 0>
@Override
public String shell(String cmd, int timeout = 0) {
    CommandOutputCapture capture = new CommandOutputCapture();
    try {
        device.executeShellCommand(cmd, capture, timeout);
    } catch (TimeoutException e) {
        LOG.log(Level.SEVERE, "Error executing command: " + cmd, e);
        return null;
    } catch (Exception e) {
        LOG.log(Level.SEVERE, "Unexpected error executing command: " + cmd, e);
        return null;
    }
    return capture.getOutput();
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>

/**
 * Execute a shell command.
 *
 * @param cmd the command to execute
 * @param timeout the maximum time to wait for command execution, defaults to 0 for infinite wait
 * @return the output of the command
 */
String shell(String cmd, int timeout = 0);

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

@MonkeyRunnerExported(doc = "Executes an adb shell command and returns the result, if any.",
            args = { "cmd", "timeout"},
            argDocs = { "The adb shell command to execute.", "The maximum time to wait for command execution." },
            returns = "The output from the command.")
public String shell(PyObject[] args, String[] kws) {
    ArgParser ap = JythonUtils.createArgParser(args, kws);
    Preconditions.checkNotNull(ap);

    String cmd = ap.getString(0);
    int timeout = ap.getInt(1, 0); // Default to infinite wait if not provided
    return impl.shell(cmd, timeout);
}
//<End of snippet n. 2>