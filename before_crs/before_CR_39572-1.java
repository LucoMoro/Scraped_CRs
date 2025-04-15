/*Second chunk of Support_PortManager removal.

Bug: 2441548
Change-Id:Id5fb5785259700db637c17522c105e6b0bb6b89f*/
//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/javax/net/ServerSocketFactoryTest.java b/luni/src/test/java/tests/api/javax/net/ServerSocketFactoryTest.java
//Synthetic comment -- index 053200f..34d7aed 100644

//Synthetic comment -- @@ -30,158 +30,77 @@

import junit.framework.TestCase;

import tests.support.Support_PortManager;


/**
 * Tests for <code>ServerSocketFactory</code> class constructors and methods.
 */
public class ServerSocketFactoryTest extends TestCase {

    /**
     * javax.net.SocketFactory#SocketFactory()
     */
public void test_Constructor() {
        try {
            ServerSocketFactory sf = new MyServerSocketFactory();
        } catch (Exception e) {
            fail("Unexpected exception " + e.toString());
        }
}

    /**
     * javax.net.ServerSocketFactory#createServerSocket()
     */
    public final void test_createServerSocket_01() {
ServerSocketFactory sf = ServerSocketFactory.getDefault();
        try {
            ServerSocket ss = sf.createServerSocket();
            assertNotNull(ss);
        } catch (SocketException e) {
        } catch (Exception e) {
            fail(e.toString());
        }
}

    /**
     * javax.net.ServerSocketFactory#createServerSocket(int port)
     */
    public final void test_createServerSocket_02() {
ServerSocketFactory sf = ServerSocketFactory.getDefault();
        int portNumber = Support_PortManager.getNextPort();

try {
            ServerSocket ss = sf.createServerSocket(portNumber);
            assertNotNull(ss);
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex);
        }

        try {
            sf.createServerSocket(portNumber);
fail("IOException wasn't thrown");
        } catch (IOException ioe) {
            //expected
        } catch (Exception ex) {
            fail(ex + " was thrown instead of IOException");
}

try {
sf.createServerSocket(-1);
fail("IllegalArgumentException wasn't thrown");
        } catch (IllegalArgumentException ioe) {
            //expected
        } catch (Exception ex) {
            fail(ex + " was thrown instead of IllegalArgumentException");
}
}

    /**
     * javax.net.ServerSocketFactory#createServerSocket(int port, int backlog)
     */
    public final void test_createServerSocket_03() {
ServerSocketFactory sf = ServerSocketFactory.getDefault();
        int portNumber = Support_PortManager.getNextPort();

try {
            ServerSocket ss = sf.createServerSocket(portNumber, 0);
            assertNotNull(ss);
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex);
        }

        try {
            sf.createServerSocket(portNumber, 0);
fail("IOException wasn't thrown");
        } catch (IOException ioe) {
            //expected
        } catch (Exception ex) {
            fail(ex + " was thrown instead of IOException");
}

try {
sf.createServerSocket(65536, 0);
fail("IllegalArgumentException wasn't thrown");
        } catch (IllegalArgumentException ioe) {
            //expected
        } catch (Exception ex) {
            fail(ex + " was thrown instead of IllegalArgumentException");
}
}

    /**
     * javax.net.ServerSocketFactory#createServerSocket(int port, int backlog, InetAddress ifAddress)
     */
    public final void test_createServerSocket_04() {
ServerSocketFactory sf = ServerSocketFactory.getDefault();
        int portNumber = Support_PortManager.getNextPort();

try {
            ServerSocket ss = sf.createServerSocket(portNumber, 0, InetAddress.getLocalHost());
            assertNotNull(ss);
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex);
        }

        try {
            sf.createServerSocket(portNumber, 0, InetAddress.getLocalHost());
fail("IOException wasn't thrown");
        } catch (IOException ioe) {
            //expected
        } catch (Exception ex) {
            fail(ex + " was thrown instead of IOException");
}

try {
sf.createServerSocket(Integer.MAX_VALUE, 0, InetAddress.getLocalHost());
fail("IllegalArgumentException wasn't thrown");
        } catch (IllegalArgumentException ioe) {
            //expected
        } catch (Exception ex) {
            fail(ex + " was thrown instead of IllegalArgumentException");
        }
    }

    /**
     * javax.net.ServerSocketFactory#getDefault()
     */
    public final void test_getDefault() {
        ServerSocketFactory sf = ServerSocketFactory.getDefault();
        ServerSocket s;
        try {
            s = sf.createServerSocket(0);
            s.close();
        } catch (IOException e) {
        }
        try {
            s = sf.createServerSocket(0, 50);
            s.close();
        } catch (IOException e) {
        }
        try {
            s = sf.createServerSocket(0, 50, InetAddress.getLocalHost());
            s.close();
        } catch (IOException e) {
}
}
}








//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/javax/net/SocketFactoryTest.java b/luni/src/test/java/tests/api/javax/net/SocketFactoryTest.java
//Synthetic comment -- index 2250602..e939a9b 100644

//Synthetic comment -- @@ -33,200 +33,135 @@

import junit.framework.TestCase;

import tests.support.Support_PortManager;


/**
 * Tests for <code>SocketFactory</code> class methods.
 */
public class SocketFactoryTest extends TestCase {

    /**
     * javax.net.SocketFactory#SocketFactory()
     */
    public void test_Constructor() {
        try {
            MySocketFactory sf = new MySocketFactory();
        } catch (Exception e) {
            fail("Unexpected exception " + e.toString());
        }
}

    /**
     * javax.net.SocketFactory#createSocket()
     */
    public final void test_createSocket_01() {
SocketFactory sf = SocketFactory.getDefault();

        try {
            Socket s = sf.createSocket();
            assertNotNull(s);
            assertEquals(-1, s.getLocalPort());
            assertEquals(0, s.getPort());
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }

MySocketFactory msf = new MySocketFactory();
try {
msf.createSocket();
fail("No expected SocketException");
        } catch (SocketException e) {
        } catch (IOException e) {
            fail(e.toString());
}
}

    /**
     * javax.net.SocketFactory#createSocket(String host, int port)
     */
    public final void test_createSocket_02() {
SocketFactory sf = SocketFactory.getDefault();
        int portNumber = Support_PortManager.getNextPort();
        int sport = startServer("Cons String,I");
int[] invalidPorts = {Integer.MIN_VALUE, -1, 65536, Integer.MAX_VALUE};

        try {
            Socket s = sf.createSocket(InetAddress.getLocalHost().getHostName(), sport);
            assertNotNull(s);
            assertTrue("Failed to create socket", s.getPort() == sport);
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }

try {
            Socket s = sf.createSocket("bla-bla", sport);
fail("UnknownHostException wasn't thrown");
        } catch (UnknownHostException uhe) {
            //expected
        } catch (Exception e) {
            fail(e + " was thrown instead of UnknownHostException");
}

for (int i = 0; i < invalidPorts.length; i++) {
try {
                Socket s = sf.createSocket(InetAddress.getLocalHost().getHostName(), invalidPorts[i]);
fail("IllegalArgumentException wasn't thrown for " + invalidPorts[i]);
            } catch (IllegalArgumentException iae) {
                //expected
            } catch (Exception e) {
                fail(e + " was thrown instead of IllegalArgumentException for " + invalidPorts[i]);
}
}

try {
            Socket s = sf.createSocket(InetAddress.getLocalHost().getHostName(), portNumber);
fail("IOException wasn't thrown");
        } catch (IOException ioe) {
            //expected
}

SocketFactory f = SocketFactory.getDefault();
try {
            Socket s = f.createSocket("localhost", 8082);
fail("IOException wasn't thrown ...");
        } catch (IOException e) {
}
}

    /**
     * javax.net.SocketFactory#createSocket(InetAddress host, int port)
     */
    public final void test_createSocket_03() {
SocketFactory sf = SocketFactory.getDefault();
        int portNumber = Support_PortManager.getNextPort();
        int sport = startServer("Cons InetAddress,I");
int[] invalidPorts = {Integer.MIN_VALUE, -1, 65536, Integer.MAX_VALUE};

        try {
            Socket s = sf.createSocket(InetAddress.getLocalHost(), sport);
            assertNotNull(s);
            assertTrue("Failed to create socket", s.getPort() == sport);
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }

for (int i = 0; i < invalidPorts.length; i++) {
try {
                Socket s = sf.createSocket(InetAddress.getLocalHost(), invalidPorts[i]);
fail("IllegalArgumentException wasn't thrown for " + invalidPorts[i]);
            } catch (IllegalArgumentException iae) {
                //expected
            } catch (Exception e) {
                fail(e + " was thrown instead of IllegalArgumentException for " + invalidPorts[i]);
}
}

try {
            Socket s = sf.createSocket(InetAddress.getLocalHost(), portNumber);
fail("IOException wasn't thrown");
        } catch (IOException ioe) {
            //expected
}

SocketFactory f = SocketFactory.getDefault();
try {
            Socket s = f.createSocket(InetAddress.getLocalHost(), 8081);
fail("IOException wasn't thrown ...");
        } catch (IOException e) {
}
}

    /**
     * javax.net.SocketFactory#createSocket(InetAddress address, int port,
     *                                             InetAddress localAddress, int localPort)
     */
    public final void test_createSocket_04() {
SocketFactory sf = SocketFactory.getDefault();
        int portNumber = Support_PortManager.getNextPort();
        int sport = startServer("Cons InetAddress,I,InetAddress,I");
int[] invalidPorts = {Integer.MIN_VALUE, -1, 65536, Integer.MAX_VALUE};

        try {
            Socket s = sf.createSocket(InetAddress.getLocalHost(), sport,
                                       InetAddress.getLocalHost(), portNumber);
            assertNotNull(s);
            assertTrue("1: Failed to create socket", s.getPort() == sport);
            assertTrue("2: Failed to create socket", s.getLocalPort() == portNumber);
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }

for (int i = 0; i < invalidPorts.length; i++) {
try {
                Socket s = sf.createSocket(InetAddress.getLocalHost(), invalidPorts[i],
                                           InetAddress.getLocalHost(), portNumber);
fail("IllegalArgumentException wasn't thrown for " + invalidPorts[i]);
            } catch (IllegalArgumentException iae) {
                //expected
            } catch (Exception e) {
                fail(e + " was thrown instead of IllegalArgumentException for " + invalidPorts[i]);
}

try {
                Socket s = sf.createSocket(InetAddress.getLocalHost(), sport,
                                           InetAddress.getLocalHost(), invalidPorts[i]);
fail("IllegalArgumentException wasn't thrown for " + invalidPorts[i]);
            } catch (IllegalArgumentException iae) {
                //expected
            } catch (Exception e) {
                fail(e + " was thrown instead of IllegalArgumentException for " + invalidPorts[i]);
}
}

try {
            Socket s = sf.createSocket(InetAddress.getLocalHost(), sport,
                                       InetAddress.getLocalHost(), portNumber);
fail("IOException wasn't thrown");
        } catch (IOException ioe) {
            //expected
}

SocketFactory f = SocketFactory.getDefault();
try {
            Socket s = f.createSocket(InetAddress.getLocalHost(), 8081, InetAddress.getLocalHost(), 8082);
fail("IOException wasn't thrown ...");
        } catch (IOException e) {
}
}

//Synthetic comment -- @@ -234,59 +169,41 @@
* javax.net.SocketFactory#createSocket(String host, int port,
*                                             InetAddress localHost, int localPort)
*/
    public final void test_createSocket_05() {
SocketFactory sf = SocketFactory.getDefault();
        int portNumber = Support_PortManager.getNextPort();
        int sport = startServer("Cons String,I,InetAddress,I");
int[] invalidPorts = {Integer.MIN_VALUE, -1, 65536, Integer.MAX_VALUE};

        try {
            Socket s = sf.createSocket(InetAddress.getLocalHost().getHostName(), sport,
                                       InetAddress.getLocalHost(), portNumber);
            assertNotNull(s);
            assertTrue("1: Failed to create socket", s.getPort() == sport);
            assertTrue("2: Failed to create socket", s.getLocalPort() == portNumber);
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }

        portNumber = Support_PortManager.getNextPort();
try {
            Socket s = sf.createSocket("bla-bla", sport, InetAddress.getLocalHost(), portNumber);
fail("UnknownHostException wasn't thrown");
        } catch (UnknownHostException uhe) {
            //expected
        } catch (Exception e) {
            fail(e + " was thrown instead of UnknownHostException");
}

for (int i = 0; i < invalidPorts.length; i++) {
            portNumber = Support_PortManager.getNextPort();
try {
                Socket s = sf.createSocket(InetAddress.getLocalHost().getHostName(), invalidPorts[i],
                                           InetAddress.getLocalHost(), portNumber);
fail("IllegalArgumentException wasn't thrown for " + invalidPorts[i]);
            } catch (IllegalArgumentException iae) {
                //expected
            } catch (Exception e) {
                fail(e + " was thrown instead of IllegalArgumentException for " + invalidPorts[i]);
}
try {
                Socket s = sf.createSocket(InetAddress.getLocalHost().getHostName(), sport,
                                           InetAddress.getLocalHost(), invalidPorts[i]);
fail("IllegalArgumentException wasn't thrown for " + invalidPorts[i]);
            } catch (IllegalArgumentException iae) {
                //expected
            } catch (Exception e) {
                fail(e + " was thrown instead of IllegalArgumentException for " + invalidPorts[i]);
}
}

        SocketFactory f = SocketFactory.getDefault();
try {
            Socket s = f.createSocket("localhost", 8081, InetAddress.getLocalHost(), 8082);
fail("IOException wasn't thrown ...");
        } catch (IOException e) {
}
}

//Synthetic comment -- @@ -297,12 +214,12 @@
SocketFactory sf = SocketFactory.getDefault();
Socket s;
try {
            s = sf.createSocket("localhost", 8082);
s.close();
} catch (IOException e) {
}
try {
            s = sf.createSocket("localhost", 8081, InetAddress.getLocalHost(), 8082);
s.close();
} catch (IOException e) {
}
//Synthetic comment -- @@ -317,17 +234,6 @@
} catch (IOException e) {
}
}

    protected int startServer(String name) {
        int portNumber = Support_PortManager.getNextPort();
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(portNumber);
        } catch (IOException e) {
            fail(name + ": " + e);
        }
        return ss.getLocalPort();
    }
}

class MySocketFactory extends SocketFactory {







