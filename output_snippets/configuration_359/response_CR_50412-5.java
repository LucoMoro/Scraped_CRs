//<Beginning of snippet n. 0>
@Override
public String shell(String cmd) {
    return shell(cmd, 0);
}

public String shell(String cmd, int timeout) {
    CommandOutputCapture capture = new CommandOutputCapture();
    if (timeout > 0) {
        Future<?> future = Executors.newSingleThreadExecutor().submit(() -> {
            try {
                device.executeShellCommand(cmd, capture);
            } catch (TimeoutException e) {
                LOG.log(Level.SEVERE, "Command timed out: " + cmd, e);
                throw new RuntimeException(e);
            }
        });
        try {
            future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            LOG.log(Level.SEVERE, "Error executing command: " + cmd, e);
            return null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error executing command: " + cmd, e);
            return null;
        }
    } else {
        try {
            device.executeShellCommand(cmd, capture);
        } catch (TimeoutException e) {
            LOG.log(Level.SEVERE, "Error executing command: " + cmd, e);
            return null;
        }
    }
    return capture.getOutput();
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
/**
* Execute a shell command.
*
* @param cmd the command to execute
* @param timeout the timeout for command execution (0 for no timeout)
* @return the output of the command
*/
String shell(String cmd, int timeout);

//<End of snippet n. 1>

//<Beginning of snippet n. 2>
@MonkeyRunnerExported(doc = "Executes an adb shell command and returns the result, if any.",
            args = { "cmd", "timeout"},
            argDocs = { "The adb shell command to execute.", "The timeout in milliseconds." },
returns = "The output from the command.")
public String shell(PyObject[] args, String[] kws) {
    ArgParser ap = JythonUtils.createArgParser(args, kws);
    Preconditions.checkNotNull(ap);

    String cmd = ap.getString(0);
    int timeout = ap.getInt(1, 0); // Defaults to 0 if not provided
    return impl.shell(cmd, timeout);
}

@MonkeyRunnerExported(doc = "Reboots the specified device into a specified bootloader.",
   //<End of snippet n. 2>