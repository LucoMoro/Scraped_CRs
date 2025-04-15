/*Make Apache HttpClient play nice with large kernel socket buffers.

Given the large maximum size likely to be set for kernel socket buffers on LTE
devices, we need to stop Apache HttpClient from allocating some integer
multiple of that size on the heap for each socket. On one device, 16 HTTP
connections would fill the heap.

Bug: 3514259
Change-Id:I4a8c13882ad794ddbeaf53a6cdc4d42d1aa3fb2f*/
//Synthetic comment -- diff --git a/src/org/apache/http/impl/io/SocketInputBuffer.java b/src/org/apache/http/impl/io/SocketInputBuffer.java
//Synthetic comment -- index 925e80a..4ca9e5f 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;

import org.apache.http.params.HttpParams;

//Synthetic comment -- @@ -44,55 +45,31 @@
* @author <a href="mailto:oleg at ural.ru">Oleg Kalnichevski</a>
*
* @version $Revision: 560358 $
 * 
* @since 4.0
*/
public class SocketInputBuffer extends AbstractSessionInputBuffer {

    static private final Class SOCKET_TIMEOUT_CLASS = SocketTimeoutExceptionClass();

    /**
     * Returns <code>SocketTimeoutExceptionClass<code> or <code>null</code> if the class
     * does not exist.
     * 
     * @return <code>SocketTimeoutExceptionClass<code>, or <code>null</code> if unavailable.
     */ 
    static private Class SocketTimeoutExceptionClass() {
        try {
            return Class.forName("java.net.SocketTimeoutException");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static boolean isSocketTimeoutException(final InterruptedIOException e) {
        if (SOCKET_TIMEOUT_CLASS != null) {
            return SOCKET_TIMEOUT_CLASS.isInstance(e);
        } else {
            return true;
        }
    }
    
private final Socket socket;
    
public SocketInputBuffer(
            final Socket socket, 
            int buffersize, 
final HttpParams params) throws IOException {
super();
if (socket == null) {
throw new IllegalArgumentException("Socket may not be null");
}
this.socket = socket;
        if (buffersize < 0) {
            buffersize = socket.getReceiveBufferSize();
        }
        if (buffersize < 1024) {
            buffersize = 1024;
        }
        init(socket.getInputStream(), buffersize, params);
}
    
public boolean isDataAvailable(int timeout) throws IOException {
boolean result = hasBufferedData();
if (!result) {
//Synthetic comment -- @@ -102,7 +79,7 @@
fillBuffer();
result = hasBufferedData();
} catch (InterruptedIOException e) {
                if (!isSocketTimeoutException(e)) {
throw e;
}
} finally {








//Synthetic comment -- diff --git a/src/org/apache/http/impl/io/SocketOutputBuffer.java b/src/org/apache/http/impl/io/SocketOutputBuffer.java
//Synthetic comment -- index efb91e9..c1d3b49 100644

//Synthetic comment -- @@ -43,37 +43,26 @@
* @author <a href="mailto:oleg at ural.ru">Oleg Kalnichevski</a>
*
* @version $Revision: 560358 $
 * 
* @since 4.0
*/
public class SocketOutputBuffer extends AbstractSessionOutputBuffer {

public SocketOutputBuffer(
            final Socket socket, 
int buffersize,
final HttpParams params) throws IOException {
super();
if (socket == null) {
throw new IllegalArgumentException("Socket may not be null");
}
        if (buffersize < 0) {
            buffersize = socket.getReceiveBufferSize();
// BEGIN android-changed
            // Workaround for http://b/issue?id=1083103.
            if (buffersize > 8096) {
                buffersize = 8096;
            }
// END android-changed
        }
        if (buffersize < 1024) {
            buffersize = 1024;
        }

// BEGIN android-changed
        socket.setSendBufferSize(buffersize * 3);
// END andrdoid-changed

        init(socket.getOutputStream(), buffersize, params);
}
    
}







