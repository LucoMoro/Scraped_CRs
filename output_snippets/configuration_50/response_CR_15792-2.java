//<Beginning of snippet n. 0>
/**
 * Executes a shell command on the device, and sends the result to a {@link IShellOutputReceiver}.
 * 
 * <p>The timeout value is used as a maximum waiting time when expecting data from the device.
 * If the shell command takes a long time to run before outputting anything, this may be
 * impacted by the timeout. For instance, if the command outputs one line every 10 seconds but the
 * timeout is set to 5 seconds (default value), then the method will timeout.
 * 
 * <p>For commands like log output, a timeout value of 0 (no timeout, always blocking until the
 * receiver's {@link IShellOutputReceiver#isCancelled()} returns <code>true</code>) should be used.
 * 
 * <p>Finally, note that the timeout value is used both when interacting with the device
 * to setup the command to run and when receiving the output of the command.
 *
 * @param device the {@link IDevice} on which to execute the command.
 * @param receiver the {@link IShellOutputReceiver} that will receive the output of the shell command.
 * @param timeout timeout value in ms for the connection. 0 means no waiting for output, which 
 * may lead to immediate return without data.
 * @throws TimeoutException in case of timeout during command execution or output reception.
 * @throws IOException in case of I/O error on the connection.
 */
static void executeRemoteCommand(InetSocketAddress adbSockAddr,
                                  String command, IDevice device, IShellOutputReceiver receiver, int timeout) 
                                  throws TimeoutException, IOException {
// Implementation here
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
/**
 * Executes a shell command on the device, and sends the result to a {@link IShellOutputReceiver}.
 * 
 * <p>The timeout value is used as a maximum waiting time when expecting data from the device.
 * If the shell command takes a long time to run before outputting anything, this may be
 * impacted by the timeout. For instance, if the command outputs one line every 10 seconds but the
 * timeout is set to 5 seconds (default value), then the method will timeout.
 * 
 * <p>For commands like log output, a timeout value of 0 (no timeout, always blocking until the
 * receiver's {@link IShellOutputReceiver#isCancelled()} returns <code>true</code>) should be used.
 * 
 * <p>Finally, note that the timeout value is used both when interacting with the device
 * to setup the command to run and when receiving the output of the command.
 *
 * @param command the shell command to execute.
 * @param receiver the {@link IShellOutputReceiver} that will receive the output of the shell command.
 * @param timeout timeout value in ms for the connection. 0 means no waiting for output, which 
 * may lead to immediate return without data.
 * @throws TimeoutException in case of timeout during command execution or output reception.
 * @throws IOException in case of I/O error on the connection.
 */
public void executeShellCommand(String command,
                                 IShellOutputReceiver receiver, int timeout) 
                                 throws TimeoutException, IOException {
// Implementation here
}
//<End of snippet n. 1>