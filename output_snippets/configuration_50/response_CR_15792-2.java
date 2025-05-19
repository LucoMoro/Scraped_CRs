//<Beginning of snippet n. 0>
/**
 * Executes a remote command on the specified device.
 * 
 * @param adbSockAddr the {@link InetSocketAddress} representing the ADB socket address.
 * @param command the command to execute on the device.
 * @param device the {@link IDevice} on which to execute the command.
 * @param receiver the {@link IShellOutputReceiver} that will receive the output of the shell command.
 * @param timeout timeout value in ms for the connection. A value of 0 disables the timeout mechanism leading to indefinite blocking if not canceled.
 *                Negative timeout values are unacceptable and may cause exceptions.
 * @throws TimeoutException in case of a timeout during command execution or data reception.
 * @throws IOException in case of I/O error on the connection.
 */
static void executeRemoteCommand(InetSocketAddress adbSockAddr,
                                  String command, IDevice device, IShellOutputReceiver receiver, int timeout) throws TimeoutException, IOException {
    if (timeout < 0) {
        throw new IllegalArgumentException("Timeout value cannot be negative.");
    }
    // Implementation of the method
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
/**
 * Executes a shell command on the device, and sends the result to a <var>receiver</var>.
 * <p>The timeout value is used as a maximum waiting time for receiving output from the device.
 * If the shell command takes a long time to run before outputting anything, this may be affected by the timeout.
 * For example, if the command outputs one line every 10 seconds but the timeout is set to 5 seconds,
 * then the method will timeout.
 * <p>For commands that produce log output, a timeout value of 0 (no timeout, always blocking until the
 * receiver's {@link IShellOutputReceiver#isCancelled()} method returns <code>true</code>) should be used.
 * <p>The timeout value is applicable when both setting up the command on the device and receiving
 * the command output. Negative timeout values are unacceptable and may cause errors.
 *
 * @param command the shell command to execute
 * @param receiver the {@link IShellOutputReceiver} that will receive the output of the shell command.
 * @param timeout the timeout value in milliseconds; a value of 0 disables the timeout mechanism leading to indefinite blocking.
 *                Negative timeout values are unacceptable and may cause exceptions.
 * @throws TimeoutException in case of timeout while waiting for command execution or output reception.
 * @throws IOException in case of I/O error on the connection.
 */
public void executeShellCommand(String command,
                                 IShellOutputReceiver receiver, int timeout) throws TimeoutException, IOException {
    if (timeout < 0) {
        throw new IllegalArgumentException("Timeout value cannot be negative.");
    }
    // Implementation of the method
}
//<End of snippet n. 1>