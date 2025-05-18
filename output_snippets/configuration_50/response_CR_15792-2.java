//<Beginning of snippet n. 0>
/**
 * Executes a remote command on the device.
 *
 * @param device the {@link IDevice} on which to execute the command.
 * @param receiver the {@link IShellOutputReceiver} that will receive the output of the shell command.
 * @param timeout timeout value in ms for the command execution. A timeout of 0 results in indefinite blocking.
 *               Be cautious with inappropriate values such as negative timeouts.
 * @throws TimeoutException if a timeout occurs during command execution or while waiting for output.
 * @throws IOException if an I/O error occurs during the command execution.
 */
static void executeRemoteCommand(InetSocketAddress adbSockAddr,
                                  String command, IDevice device, IShellOutputReceiver receiver, int timeout) throws TimeoutException, IOException {
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
/**
 * Executes a shell command on the device, and sends the result to a {@code receiver}.
 * <p>The timeout value is used as a maximum waiting time when expecting data from the device.
 * If the shell command takes a long time to run before outputting anything, this may be
 * impacted by the timeout. For instance, if the command outputs one line every 10 sec but the
 * timeout is set to 5 sec, the method will timeout.
 * <p>For commands like log output, a timeout value of 0 (no timeout, always blocking until the
 * receiver's {@link IShellOutputReceiver#isCancelled()} returns {@code true}) should be 
 * used.
 * <p>Finally, note that the timeout value is used both when interacting with the device 
 * to set up the command to run and when receiving the output of the command.
 *
 * @param command the shell command to execute
 * @param receiver the {@link IShellOutputReceiver} that will receive the output of the shell command.
 *                 A timeout of 0 results in no detectable timeout if there's no output 
 *                 from the receiver.
 * @param timeout timeout value in ms for the command execution. A {@link TimeoutException} will 
 *                be thrown if the timeout is reached.
 * @throws TimeoutException if a timeout occurs during command setup or while waiting for output.
 * @throws IOException if an I/O error occurs during the command execution.
 */
public void executeShellCommand(String command,
                                 IShellOutputReceiver receiver, int timeout) throws TimeoutException, IOException {
//<End of snippet n. 1>