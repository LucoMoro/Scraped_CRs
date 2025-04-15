/*DO NOT MERGE. Mark select MulticastSocketTest tests as KnownFailures.

Change-Id:I45afaa1b2e1bed84e27da036cd071ad983e34f10*/
//Synthetic comment -- diff --git a/libcore/luni/src/test/java/tests/api/java/net/MulticastSocketTest.java b/libcore/luni/src/test/java/tests/api/java/net/MulticastSocketTest.java
//Synthetic comment -- index 018d58a..12f68bf 100644

//Synthetic comment -- @@ -17,6 +17,8 @@

package tests.api.java.net;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
//Synthetic comment -- @@ -162,6 +164,7 @@
	/**
	 * @tests java.net.MulticastSocket#getInterface()
	 */
	public void test_getInterface() throws Exception {
		// Test for method java.net.InetAddress
		// java.net.MulticastSocket.getInterface()
//Synthetic comment -- @@ -217,6 +220,7 @@
	 * @throws IOException
	 * @tests java.net.MulticastSocket#getNetworkInterface()
	 */
	public void test_getNetworkInterface() throws IOException {
int groupPort = Support_PortManager.getNextPortForUDP();
if (atLeastOneInterface) {
//Synthetic comment -- @@ -351,6 +355,7 @@
	 * @throws InterruptedException 
	 * @tests java.net.MulticastSocket#joinGroup(java.net.SocketAddress,java.net.NetworkInterface)
	 */
	public void test_joinGroupLjava_net_SocketAddressLjava_net_NetworkInterface() throws IOException, InterruptedException {
		// security manager that allows us to check that we only return the
		// addresses that we should
//Synthetic comment -- @@ -600,6 +605,7 @@
	/**
	 * @tests java.net.MulticastSocket#leaveGroup(java.net.SocketAddress,java.net.NetworkInterface)
	 */
	public void test_leaveGroupLjava_net_SocketAddressLjava_net_NetworkInterface() throws Exception {
		// security manager that allows us to check that we only return the
		// addresses that we should







