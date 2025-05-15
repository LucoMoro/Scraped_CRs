//<Beginning of snippet n. 0>
/**
 * Executes a remote command on the device.
 * 
 * @param device the {@link IDevice} on which to execute the command.
 * @param receiver the {@link IShellOutputReceiver} that will receive the output of the shell command.
 * @param timeout timeout value in ms for the connection. A timeout of zero means the command will block indefinitely until the receiver signals cancellation.
 * @throws TimeoutException in case no output is produced before the timeout threshold is reached.
 * @throws IOException in case of I/O error on the connection.
 */
static void executeRemoteCommand(InetSocketAddress adbSockAddr,
String command, IDevice device, IShellOutputReceiver receiver, int timeout) 
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
/**
 * Executes a shell command on the device, and sends the result to a <var>receiver</var>.
 * <p/>The timeout value is used as a maximum waiting time when expecting data from the device.
 * If the shell command takes a long time to run before outputting anything, this may be
 * impacted by the timeout. For instance, if the command outputs one line every 10sec but the
 * timeout is set to 5sec (default value) then the method will timeout.
 * <p/>For commands like log output, a timeout value of 0 (no timeout, always blocking until the
 * receiver's {@link IShellOutputReceiver#isCancelled()} returns <code>true</code>) should be used.
 * <p/>Finally note that the timeout value is used both when interacting with the device
 * to setup the command to run and when receiving the output of the command.
 *
 * @param command the shell command to execute
 * @param receiver the {@link IShellOutputReceiver} that will receive the output of the shell command.
 * @param timeout timeout value in ms for the connection. A timeout of zero means no timeout.
 * @throws TimeoutException in case no output is produced before the timeout threshold is reached.
 * @throws IOException in case of I/O error on the connection.
 */
public void executeShellCommand(String command,
IShellOutputReceiver receiver, int timeout) throws TimeoutException, IOException;
//<End of snippet n. 1>