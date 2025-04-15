/*Add/Remove Some @KnownFailure Annotations

Bug 2639444 2564250 2639393

Remove the @KnownFailure annotations form tests now passing in
Froyo for Nexus One using the CTS test runner. Put on
@KnownFailure annotations for those that are failing but passing
with the run-core-tests test runner.

MultiSocketTest is an exception that doesn't seem to make sense,
and fails in both runners.

Change-Id:Id8eb6b5d6f2b04b77434411f2b04c56cd61acf85*/
//Synthetic comment -- diff --git a/libcore/luni/src/test/java/tests/api/java/lang/reflect/ParameterizedTypeTest.java b/libcore/luni/src/test/java/tests/api/java/lang/reflect/ParameterizedTypeTest.java
//Synthetic comment -- index bf3698e..4d7e2c4 100644

//Synthetic comment -- @@ -55,6 +55,7 @@
args = {}
)
})
public void testStringParameterizedSuperClass() {
Class<? extends B> clazz = B.class;
Type genericSuperclass = clazz.getGenericSuperclass();
//Synthetic comment -- @@ -91,6 +92,7 @@
args = {}
)
})
public void testTypeParameterizedSuperClass() {
Class<? extends D> clazz = D.class;
Type genericSuperclass = clazz.getGenericSuperclass();








//Synthetic comment -- diff --git a/libcore/luni/src/test/java/tests/api/java/net/MulticastSocketTest.java b/libcore/luni/src/test/java/tests/api/java/net/MulticastSocketTest.java
//Synthetic comment -- index 2959509..15d14b0 100644

//Synthetic comment -- @@ -163,6 +163,7 @@
	/**
	 * @tests java.net.MulticastSocket#getInterface()
	 */
	public void test_getInterface() throws Exception {
		// Test for method java.net.InetAddress
		// java.net.MulticastSocket.getInterface()
//Synthetic comment -- @@ -352,7 +353,6 @@
	 * @throws InterruptedException 
	 * @tests java.net.MulticastSocket#joinGroup(java.net.SocketAddress,java.net.NetworkInterface)
	 */
    @KnownFailure("Fails in CTS but passes under run-core-tests")
	public void test_joinGroupLjava_net_SocketAddressLjava_net_NetworkInterface() throws IOException, InterruptedException {
		// security manager that allows us to check that we only return the
		// addresses that we should








//Synthetic comment -- diff --git a/libcore/nio_char/src/test/java/tests/api/java/nio/charset/CharsetProviderTest.java b/libcore/nio_char/src/test/java/tests/api/java/nio/charset/CharsetProviderTest.java
//Synthetic comment -- index a2ae12e..c77f3a5 100644

//Synthetic comment -- @@ -140,7 +140,6 @@
)
@AndroidOnly("Looks like RI doesn't use current thread's context class "+
"loader to lookup charset providers")
    @KnownFailure("Fails in CTS but passes under run-core-tests")
public void testIsSupported_And_ForName_NormalProvider() throws Exception {
try {
assertFalse(Charset.isSupported("mockCharset10"));
//Synthetic comment -- @@ -267,6 +266,7 @@
method = "charsetForName",
args = {String.class}
)
public void testIsSupported_InsufficientPrivilege() throws Exception {
SecurityManager oldMan = System.getSecurityManager();
System.setSecurityManager(new MockSecurityManager());








//Synthetic comment -- diff --git a/libcore/sql/src/test/java/org/apache/harmony/sql/tests/java/sql/DriverManagerTest.java b/libcore/sql/src/test/java/org/apache/harmony/sql/tests/java/sql/DriverManagerTest.java
//Synthetic comment -- index 6fa7949..6bbdbfe 100644

//Synthetic comment -- @@ -372,7 +372,7 @@
method = "getDrivers",
args = {}
)
    @KnownFailure("We're working out issues with built-in SQL drivers")
public void testGetDrivers() {
// Load a driver manager
Enumeration<Driver> driverList = DriverManager.getDrivers();
//Synthetic comment -- @@ -761,5 +761,3 @@
}

} // end class DriverManagerTest









