/*Fix some javadocs regarding the timeout API change.

Change-Id:I7e96c253aa07b124bceab99b6f5ce11e17f7b950*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AdbHelper.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AdbHelper.java
//Synthetic comment -- index 0f5a532..f31e27f 100644

//Synthetic comment -- @@ -338,9 +338,12 @@
* @param device the {@link IDevice} on which to execute the command.
* @param rcvr the {@link IShellOutputReceiver} that will receives the output of the shell
* command
     * @param timeout timeout value in ms for the connection. 0 means no timeout.
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException in case of I/O error on the connection.
*/
static void executeRemoteCommand(InetSocketAddress adbSockAddr,
String command, IDevice device, IShellOutputReceiver rcvr, int timeout)








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java b/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java
//Synthetic comment -- index d75c9c3..1660060 100755

//Synthetic comment -- @@ -203,15 +203,15 @@

/**
* Executes a shell command on the device, and sends the result to a <var>receiver</var>.
     * <p/>The timeout value is used as a maximum waiting time when expecting data from the device.
* If the shell command takes a long time to run before outputting anything, this may be
* impacted by the timeout. For instance, if the command outputs one line every 10sec but the
* timeout is set to 5sec (default value) then the method will timeout.
* <p/>For commands like log output, a timeout value of 0 (no timeout, always blocking till the
* receiver's {@link IShellOutputReceiver#isCancelled()} return <code>true</code> should be
* used.
     * <p/>Finally note that the timeout value is used both when interacting with the device
     * to setup the command to run and when receiving the output of the command.
*
* @param command the shell command to execute
* @param receiver the {@link IShellOutputReceiver} that will receives the output of the shell
//Synthetic comment -- @@ -223,6 +223,8 @@
* no timeout will occur as it's not possible to detect a difference between no output and
* no timeout.
* @throws IOException in case of I/O error on the connection.
*/
public void executeShellCommand(String command,
IShellOutputReceiver receiver, int timeout) throws TimeoutException, IOException;







