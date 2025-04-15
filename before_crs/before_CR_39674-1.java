/*Declare that various Posix methods can throw SocketException.

Bug: 5177516
Change-Id:Icf2f06c7df6686dd1f54a930bc3fa50b1ce4e1d4*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/net/PlainDatagramSocketImpl.java b/luni/src/main/java/java/net/PlainDatagramSocketImpl.java
//Synthetic comment -- index 5d01469..3226527 100644

//Synthetic comment -- @@ -216,6 +216,8 @@
Libcore.os.connect(fd, InetAddress.UNSPECIFIED, 0);
} catch (ErrnoException errnoException) {
throw new AssertionError(errnoException);
}
connectedPort = -1;
connectedAddress = null;








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/BlockGuardOs.java b/luni/src/main/java/libcore/io/BlockGuardOs.java
//Synthetic comment -- index 4f2858d..61c9765 100644

//Synthetic comment -- @@ -50,7 +50,7 @@
}
}

    @Override public FileDescriptor accept(FileDescriptor fd, InetSocketAddress peerAddress) throws ErrnoException {
BlockGuard.getThreadPolicy().onNetwork();
return tagSocket(os.accept(fd, peerAddress));
}
//Synthetic comment -- @@ -80,7 +80,7 @@
return linger.isOn() && linger.l_linger > 0;
}

    @Override public void connect(FileDescriptor fd, InetAddress address, int port) throws ErrnoException {
BlockGuard.getThreadPolicy().onNetwork();
os.connect(fd, address, port);
}
//Synthetic comment -- @@ -154,22 +154,22 @@
return os.readv(fd, buffers, offsets, byteCounts);
}

    @Override public int recvfrom(FileDescriptor fd, ByteBuffer buffer, int flags, InetSocketAddress srcAddress) throws ErrnoException {
BlockGuard.getThreadPolicy().onNetwork();
return os.recvfrom(fd, buffer, flags, srcAddress);
}

    @Override public int recvfrom(FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount, int flags, InetSocketAddress srcAddress) throws ErrnoException {
BlockGuard.getThreadPolicy().onNetwork();
return os.recvfrom(fd, bytes, byteOffset, byteCount, flags, srcAddress);
}

    @Override public int sendto(FileDescriptor fd, ByteBuffer buffer, int flags, InetAddress inetAddress, int port) throws ErrnoException {
BlockGuard.getThreadPolicy().onNetwork();
return os.sendto(fd, buffer, flags, inetAddress, port);
}

    @Override public int sendto(FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount, int flags, InetAddress inetAddress, int port) throws ErrnoException {
// We permit datagrams without hostname lookups.
if (inetAddress != null) {
BlockGuard.getThreadPolicy().onNetwork();








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/ForwardingOs.java b/luni/src/main/java/libcore/io/ForwardingOs.java
//Synthetic comment -- index 2c8e562..39b9861 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import libcore.util.MutableInt;
import libcore.util.MutableLong;
//Synthetic comment -- @@ -34,12 +35,12 @@
this.os = os;
}

    public FileDescriptor accept(FileDescriptor fd, InetSocketAddress peerAddress) throws ErrnoException { return os.accept(fd, peerAddress); }
public boolean access(String path, int mode) throws ErrnoException { return os.access(path, mode); }
    public void bind(FileDescriptor fd, InetAddress address, int port) throws ErrnoException { os.bind(fd, address, port); }
public void chmod(String path, int mode) throws ErrnoException { os.chmod(path, mode); }
public void close(FileDescriptor fd) throws ErrnoException { os.close(fd); }
    public void connect(FileDescriptor fd, InetAddress address, int port) throws ErrnoException { os.connect(fd, address, port); }
public FileDescriptor dup(FileDescriptor oldFd) throws ErrnoException { return os.dup(oldFd); }
public FileDescriptor dup2(FileDescriptor oldFd, int newFd) throws ErrnoException { return os.dup2(oldFd, newFd); }
public String[] environ() { return os.environ(); }
//Synthetic comment -- @@ -95,13 +96,13 @@
public int read(FileDescriptor fd, ByteBuffer buffer) throws ErrnoException { return os.read(fd, buffer); }
public int read(FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount) throws ErrnoException { return os.read(fd, bytes, byteOffset, byteCount); }
public int readv(FileDescriptor fd, Object[] buffers, int[] offsets, int[] byteCounts) throws ErrnoException { return os.readv(fd, buffers, offsets, byteCounts); }
    public int recvfrom(FileDescriptor fd, ByteBuffer buffer, int flags, InetSocketAddress srcAddress) throws ErrnoException { return os.recvfrom(fd, buffer, flags, srcAddress); }
    public int recvfrom(FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount, int flags, InetSocketAddress srcAddress) throws ErrnoException { return os.recvfrom(fd, bytes, byteOffset, byteCount, flags, srcAddress); }
public void remove(String path) throws ErrnoException { os.remove(path); }
public void rename(String oldPath, String newPath) throws ErrnoException { os.rename(oldPath, newPath); }
public long sendfile(FileDescriptor outFd, FileDescriptor inFd, MutableLong inOffset, long byteCount) throws ErrnoException { return os.sendfile(outFd, inFd, inOffset, byteCount); }
    public int sendto(FileDescriptor fd, ByteBuffer buffer, int flags, InetAddress inetAddress, int port) throws ErrnoException { return os.sendto(fd, buffer, flags, inetAddress, port); }
    public int sendto(FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount, int flags, InetAddress inetAddress, int port) throws ErrnoException { return os.sendto(fd, bytes, byteOffset, byteCount, flags, inetAddress, port); }
public void setegid(int egid) throws ErrnoException { os.setegid(egid); }
public void seteuid(int euid) throws ErrnoException { os.seteuid(euid); }
public void setgid(int gid) throws ErrnoException { os.setgid(gid); }








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/Os.java b/luni/src/main/java/libcore/io/Os.java
//Synthetic comment -- index d637b67..5fdc7be 100644

//Synthetic comment -- @@ -20,17 +20,18 @@
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import libcore.util.MutableInt;
import libcore.util.MutableLong;

public interface Os {
    public FileDescriptor accept(FileDescriptor fd, InetSocketAddress peerAddress) throws ErrnoException;
public boolean access(String path, int mode) throws ErrnoException;
    public void bind(FileDescriptor fd, InetAddress address, int port) throws ErrnoException;
public void chmod(String path, int mode) throws ErrnoException;
public void close(FileDescriptor fd) throws ErrnoException;
    public void connect(FileDescriptor fd, InetAddress address, int port) throws ErrnoException;
public FileDescriptor dup(FileDescriptor oldFd) throws ErrnoException;
public FileDescriptor dup2(FileDescriptor oldFd, int newFd) throws ErrnoException;
public String[] environ();
//Synthetic comment -- @@ -88,12 +89,12 @@
public int read(FileDescriptor fd, ByteBuffer buffer) throws ErrnoException;
public int read(FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount) throws ErrnoException;
public int readv(FileDescriptor fd, Object[] buffers, int[] offsets, int[] byteCounts) throws ErrnoException;
    public int recvfrom(FileDescriptor fd, ByteBuffer buffer, int flags, InetSocketAddress srcAddress) throws ErrnoException;
    public int recvfrom(FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount, int flags, InetSocketAddress srcAddress) throws ErrnoException;
public void remove(String path) throws ErrnoException;
public void rename(String oldPath, String newPath) throws ErrnoException;
    public int sendto(FileDescriptor fd, ByteBuffer buffer, int flags, InetAddress inetAddress, int port) throws ErrnoException;
    public int sendto(FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount, int flags, InetAddress inetAddress, int port) throws ErrnoException;
public long sendfile(FileDescriptor outFd, FileDescriptor inFd, MutableLong inOffset, long byteCount) throws ErrnoException;
public void setegid(int egid) throws ErrnoException;
public void seteuid(int euid) throws ErrnoException;








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/Posix.java b/luni/src/main/java/libcore/io/Posix.java
//Synthetic comment -- index 7bbf49f..0fc9f38 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.NioUtils;
import libcore.util.MutableInt;
//Synthetic comment -- @@ -28,12 +29,12 @@
public final class Posix implements Os {
Posix() { }

    public native FileDescriptor accept(FileDescriptor fd, InetSocketAddress peerAddress) throws ErrnoException;
public native boolean access(String path, int mode) throws ErrnoException;
    public native void bind(FileDescriptor fd, InetAddress address, int port) throws ErrnoException;
public native void chmod(String path, int mode) throws ErrnoException;
public native void close(FileDescriptor fd) throws ErrnoException;
    public native void connect(FileDescriptor fd, InetAddress address, int port) throws ErrnoException;
public native FileDescriptor dup(FileDescriptor oldFd) throws ErrnoException;
public native FileDescriptor dup2(FileDescriptor oldFd, int newFd) throws ErrnoException;
public native String[] environ();
//Synthetic comment -- @@ -119,33 +120,33 @@
}
private native int readBytes(FileDescriptor fd, Object buffer, int offset, int byteCount) throws ErrnoException;
public native int readv(FileDescriptor fd, Object[] buffers, int[] offsets, int[] byteCounts) throws ErrnoException;
    public int recvfrom(FileDescriptor fd, ByteBuffer buffer, int flags, InetSocketAddress srcAddress) throws ErrnoException {
if (buffer.isDirect()) {
return recvfromBytes(fd, buffer, buffer.position(), buffer.remaining(), flags, srcAddress);
} else {
return recvfromBytes(fd, NioUtils.unsafeArray(buffer), NioUtils.unsafeArrayOffset(buffer) + buffer.position(), buffer.remaining(), flags, srcAddress);
}
}
    public int recvfrom(FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount, int flags, InetSocketAddress srcAddress) throws ErrnoException {
// This indirection isn't strictly necessary, but ensures that our public interface is type safe.
return recvfromBytes(fd, bytes, byteOffset, byteCount, flags, srcAddress);
}
    private native int recvfromBytes(FileDescriptor fd, Object buffer, int byteOffset, int byteCount, int flags, InetSocketAddress srcAddress) throws ErrnoException;
public native void remove(String path) throws ErrnoException;
public native void rename(String oldPath, String newPath) throws ErrnoException;
public native long sendfile(FileDescriptor outFd, FileDescriptor inFd, MutableLong inOffset, long byteCount) throws ErrnoException;
    public int sendto(FileDescriptor fd, ByteBuffer buffer, int flags, InetAddress inetAddress, int port) throws ErrnoException {
if (buffer.isDirect()) {
return sendtoBytes(fd, buffer, buffer.position(), buffer.remaining(), flags, inetAddress, port);
} else {
return sendtoBytes(fd, NioUtils.unsafeArray(buffer), NioUtils.unsafeArrayOffset(buffer) + buffer.position(), buffer.remaining(), flags, inetAddress, port);
}
}
    public int sendto(FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount, int flags, InetAddress inetAddress, int port) throws ErrnoException {
// This indirection isn't strictly necessary, but ensures that our public interface is type safe.
return sendtoBytes(fd, bytes, byteOffset, byteCount, flags, inetAddress, port);
}
    private native int sendtoBytes(FileDescriptor fd, Object buffer, int byteOffset, int byteCount, int flags, InetAddress inetAddress, int port) throws ErrnoException;
public native void setegid(int egid) throws ErrnoException;
public native void seteuid(int euid) throws ErrnoException;
public native void setgid(int gid) throws ErrnoException;







