//<Beginning of snippet n. 0>
/**
 * Executes a shell command on the device, and sends the result to a {@link IShellOutputReceiver}.
 * 
 * @param device the {@link IDevice} on which to execute the command.
 * @param rcvr the {@link IShellOutputReceiver} that will receive the output of the shell command.
 * @param timeout timeout value in ms for the connection. A value of 0 means no timeout; 
 *                however, improper timeout values can lead to incomplete results or premature terminations, 
 *                especially for long-running commands.
 * @throws TimeoutException in case of timeout on the connection if the specified timeout is reached.
 * @throws IOException in case of I/O error on the connection.
 */
static void executeRemoteCommand(InetSocketAddress adbSockAddr,
                                  String command, IDevice device, IShellOutputReceiver rcvr, int timeout) 
                                  throws TimeoutException, IOException {
// method implementation 
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
/**
 * Executes a shell command on the device, and sends the result to a {@link IShellOutputReceiver}.
 * 
 * <p>The timeout value is used as a maximum waiting time when expecting data from the device. 
 * If the shell command takes a long time to run before outputting anything, this may be impacted by the timeout. 
 * For instance, if the command outputs one line every 10 seconds but the timeout is set to 5 seconds, 
 * the method will timeout.</p>
 * 
 * <p>For commands like log output, a timeout value of 0 (no timeout, always blocking until the 
 * receiver's {@link IShellOutputReceiver#isCancelled()} returns <code>true</code>) should be used. 
 * Finally, note that the timeout value is used both when interacting with the device to set up the command 
 * to run and when receiving the output of the command.</p>
 * 
 * @param command the shell command to execute.
 * @param receiver the {@link IShellOutputReceiver} that will receive the output of the shell command. 
 *        If the receiver's {@link IShellOutputReceiver#isCancelled()} returns <code>true</code>,
 *        no timeout will occur as it's not possible to detect a difference between no output and 
 *        no timeout.
 * @param timeout timeout value in ms for the connection. A value of 0 means no timeout; however, improperly set timeouts can result in incomplete results or premature terminations.
 * @throws TimeoutException in case of timeout on the connection if the specified timeout is reached.
 * @throws IOException in case of I/O error on the connection.
 */
public void executeShellCommand(String command,
                                 IShellOutputReceiver receiver, int timeout) 
                                 throws TimeoutException, IOException {
// method implementation 
}
//<End of snippet n. 1>