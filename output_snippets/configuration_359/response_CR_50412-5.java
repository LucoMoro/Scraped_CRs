//<Beginning of snippet n. 0>
@Override
public String shell(String cmd, int timeout) {
    CommandOutputCapture capture = new CommandOutputCapture();
    Future<?> future = Executors.newSingleThreadExecutor().submit(() -> {
        try {
            device.executeShellCommand(cmd, capture);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error executing command: " + cmd, e);
        }
    });
    
    try {
        if (timeout > 0) {
            future.get(timeout, TimeUnit.MILLISECONDS);
        } else {
            future.get(); // Indefinite wait
        }
    } catch (TimeoutException e) {
        LOG.log(Level.SEVERE, "Command timed out: " + cmd, e);
        future.cancel(true);
        return null;
    } catch (Exception e) {
        LOG.log(Level.SEVERE, "Error executing command: " + cmd, e);
        return null;
    }
    
    return capture.toString();
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
/**
 * Execute a shell command.
 *
 * @param cmd the command to execute
 * @param timeout the time to wait for command execution in milliseconds
 * @return the output of the command
 */
String shell(String cmd, int timeout);
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
@MonkeyRunnerExported(doc = "Executes an adb shell command and returns the result, if any.",
        args = { "cmd", "timeout"},
        argDocs = { "The adb shell command to execute.", "The maximum time to wait for execution." },
        returns = "The output from the command.")
public String shell(PyObject[] args, String[] kws) {
    ArgParser ap = JythonUtils.createArgParser(args, kws);
    Preconditions.checkNotNull(ap);

    String cmd = ap.getString(0);
    int timeout = ap.getInt(1, 0); // Default to 0 for indefinite wait
    return impl.shell(cmd, timeout);
}
//<End of snippet n. 2>