/*Second chunk of Support_PortManager removal.

Bug: 2441548
Change-Id:Id5fb5785259700db637c17522c105e6b0bb6b89f*/




//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/javax/net/ServerSocketFactoryTest.java b/luni/src/test/java/tests/api/javax/net/ServerSocketFactoryTest.java
//Synthetic comment -- index 053200f..34d7aed 100644

//Synthetic comment -- @@ -30,158 +30,77 @@

import junit.framework.TestCase;

public class ServerSocketFactoryTest extends TestCase {

public void test_Constructor() {
        ServerSocketFactory sf = new MyServerSocketFactory();
}

    public final void test_createServerSocket() throws Exception {
ServerSocketFactory sf = ServerSocketFactory.getDefault();
        ServerSocket ss = sf.createServerSocket();
        assertNotNull(ss);
        ss.close();
}

    public final void test_createServerSocket_I() throws Exception {
ServerSocketFactory sf = ServerSocketFactory.getDefault();
        ServerSocket ss = sf.createServerSocket(0);
        assertNotNull(ss);

try {
            sf.createServerSocket(ss.getLocalPort());
fail("IOException wasn't thrown");
        } catch (IOException expected) {
}

        ss.close();

try {
sf.createServerSocket(-1);
fail("IllegalArgumentException wasn't thrown");
        } catch (IllegalArgumentException expected) {
}
}

    public final void test_createServerSocket_II() throws Exception {
ServerSocketFactory sf = ServerSocketFactory.getDefault();
        ServerSocket ss = sf.createServerSocket(0, 0);
        assertNotNull(ss);

try {
            sf.createServerSocket(ss.getLocalPort(), 0);
fail("IOException wasn't thrown");
        } catch (IOException expected) {
}

        ss.close();

try {
sf.createServerSocket(65536, 0);
fail("IllegalArgumentException wasn't thrown");
        } catch (IllegalArgumentException expected) {
}
}

    public final void test_createServerSocket_IIInetAddress() throws Exception {
ServerSocketFactory sf = ServerSocketFactory.getDefault();

        ServerSocket ss = sf.createServerSocket(0, 0, InetAddress.getLocalHost());
        assertNotNull(ss);

try {
            sf.createServerSocket(ss.getLocalPort(), 0, InetAddress.getLocalHost());
fail("IOException wasn't thrown");
        } catch (IOException expected) {
}

        ss.close();

try {
sf.createServerSocket(Integer.MAX_VALUE, 0, InetAddress.getLocalHost());
fail("IllegalArgumentException wasn't thrown");
        } catch (IllegalArgumentException expected) {
}
}
}








//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/javax/net/SocketFactoryTest.java b/luni/src/test/java/tests/api/javax/net/SocketFactoryTest.java
//Synthetic comment -- index 2250602..e939a9b 100644

//Synthetic comment -- @@ -33,200 +33,135 @@

import junit.framework.TestCase;

public class SocketFactoryTest extends TestCase {

    public void test_Constructor() throws Exception {
        new MySocketFactory();
}

    public final void test_createSocket() throws Exception {
SocketFactory sf = SocketFactory.getDefault();

        Socket s = sf.createSocket();
        assertNotNull(s);
        assertEquals(-1, s.getLocalPort());
        assertEquals(0, s.getPort());

MySocketFactory msf = new MySocketFactory();
try {
msf.createSocket();
fail("No expected SocketException");
        } catch (SocketException expected) {
}
}

    public final void test_createSocket_StringI() throws Exception {
SocketFactory sf = SocketFactory.getDefault();
        int sport = new ServerSocket(0).getLocalPort();
int[] invalidPorts = {Integer.MIN_VALUE, -1, 65536, Integer.MAX_VALUE};

        Socket s = sf.createSocket(InetAddress.getLocalHost().getHostName(), sport);
        assertNotNull(s);
        assertTrue("Failed to create socket", s.getPort() == sport);

try {
            sf.createSocket("bla-bla", sport);
fail("UnknownHostException wasn't thrown");
        } catch (UnknownHostException expected) {
}

for (int i = 0; i < invalidPorts.length; i++) {
try {
                sf.createSocket(InetAddress.getLocalHost().getHostName(), invalidPorts[i]);
fail("IllegalArgumentException wasn't thrown for " + invalidPorts[i]);
            } catch (IllegalArgumentException expected) {
}
}

try {
            sf.createSocket(InetAddress.getLocalHost().getHostName(), s.getLocalPort());
fail("IOException wasn't thrown");
        } catch (IOException expected) {
}

SocketFactory f = SocketFactory.getDefault();
try {
            f.createSocket(InetAddress.getLocalHost().getHostName(), 8082);
fail("IOException wasn't thrown ...");
        } catch (IOException expected) {
}
}

    public final void test_createSocket_InetAddressI() throws Exception {
SocketFactory sf = SocketFactory.getDefault();
        int sport = new ServerSocket(0).getLocalPort();
int[] invalidPorts = {Integer.MIN_VALUE, -1, 65536, Integer.MAX_VALUE};

        Socket s = sf.createSocket(InetAddress.getLocalHost(), sport);
        assertNotNull(s);
        assertTrue("Failed to create socket", s.getPort() == sport);

for (int i = 0; i < invalidPorts.length; i++) {
try {
                sf.createSocket(InetAddress.getLocalHost(), invalidPorts[i]);
fail("IllegalArgumentException wasn't thrown for " + invalidPorts[i]);
            } catch (IllegalArgumentException expected) {
}
}

try {
            sf.createSocket(InetAddress.getLocalHost(), s.getLocalPort());
fail("IOException wasn't thrown");
        } catch (IOException expected) {
}

SocketFactory f = SocketFactory.getDefault();
try {
            f.createSocket(InetAddress.getLocalHost(), 8081);
fail("IOException wasn't thrown ...");
        } catch (IOException expected) {
}
}

    public final void test_createSocket_InetAddressIInetAddressI() throws Exception {
SocketFactory sf = SocketFactory.getDefault();
        int sport = new ServerSocket(0).getLocalPort();
int[] invalidPorts = {Integer.MIN_VALUE, -1, 65536, Integer.MAX_VALUE};

        Socket s = sf.createSocket(InetAddress.getLocalHost(), sport,
                                   InetAddress.getLocalHost(), 0);
        assertNotNull(s);
        assertTrue("1: Failed to create socket", s.getPort() == sport);
        int portNumber = s.getLocalPort();

for (int i = 0; i < invalidPorts.length; i++) {
try {
              sf.createSocket(InetAddress.getLocalHost(), invalidPorts[i],
                              InetAddress.getLocalHost(), portNumber);
fail("IllegalArgumentException wasn't thrown for " + invalidPorts[i]);
            } catch (IllegalArgumentException expected) {
}

try {
                sf.createSocket(InetAddress.getLocalHost(), sport,
                                InetAddress.getLocalHost(), invalidPorts[i]);
fail("IllegalArgumentException wasn't thrown for " + invalidPorts[i]);
            } catch (IllegalArgumentException expected) {
}
}

try {
            sf.createSocket(InetAddress.getLocalHost(), sport,
                            InetAddress.getLocalHost(), portNumber);
fail("IOException wasn't thrown");
        } catch (IOException expected) {
}

SocketFactory f = SocketFactory.getDefault();
try {
            f.createSocket(InetAddress.getLocalHost(), 8081, InetAddress.getLocalHost(), 8082);
fail("IOException wasn't thrown ...");
        } catch (IOException expected) {
}
}

//Synthetic comment -- @@ -234,59 +169,41 @@
* javax.net.SocketFactory#createSocket(String host, int port,
*                                             InetAddress localHost, int localPort)
*/
    public final void test_createSocket_05() throws Exception {
SocketFactory sf = SocketFactory.getDefault();
        int sport = new ServerSocket(0).getLocalPort();
int[] invalidPorts = {Integer.MIN_VALUE, -1, 65536, Integer.MAX_VALUE};

        Socket s = sf.createSocket(InetAddress.getLocalHost().getHostName(), sport,
                                   InetAddress.getLocalHost(), 0);
        assertNotNull(s);
        assertTrue("1: Failed to create socket", s.getPort() == sport);

try {
            sf.createSocket("bla-bla", sport, InetAddress.getLocalHost(), 0);
fail("UnknownHostException wasn't thrown");
        } catch (UnknownHostException expected) {
}

for (int i = 0; i < invalidPorts.length; i++) {
try {
                sf.createSocket(InetAddress.getLocalHost().getHostName(), invalidPorts[i],
                                InetAddress.getLocalHost(), 0);
fail("IllegalArgumentException wasn't thrown for " + invalidPorts[i]);
            } catch (IllegalArgumentException expected) {
}
try {
                sf.createSocket(InetAddress.getLocalHost().getHostName(), sport,
                                InetAddress.getLocalHost(), invalidPorts[i]);
fail("IllegalArgumentException wasn't thrown for " + invalidPorts[i]);
            } catch (IllegalArgumentException expected) {
}
}

try {
            sf.createSocket(InetAddress.getLocalHost().getHostName(), 8081, InetAddress.getLocalHost(), 8082);
fail("IOException wasn't thrown ...");
        } catch (IOException expected) {
}
}

//Synthetic comment -- @@ -297,12 +214,12 @@
SocketFactory sf = SocketFactory.getDefault();
Socket s;
try {
            s = sf.createSocket(InetAddress.getLocalHost().getHostName(), 8082);
s.close();
} catch (IOException e) {
}
try {
            s = sf.createSocket(InetAddress.getLocalHost().getHostName(), 8081, InetAddress.getLocalHost(), 8082);
s.close();
} catch (IOException e) {
}
//Synthetic comment -- @@ -317,17 +234,6 @@
} catch (IOException e) {
}
}
}

class MySocketFactory extends SocketFactory {







