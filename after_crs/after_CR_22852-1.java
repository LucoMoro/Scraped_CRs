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
import java.net.SocketTimeoutException;

import org.apache.http.params.HttpParams;

//Synthetic comment -- @@ -44,55 +45,31 @@
* @author <a href="mailto:oleg at ural.ru">Oleg Kalnichevski</a>
*
* @version $Revision: 560358 $
 *
* @since 4.0
*/
public class SocketInputBuffer extends AbstractSessionInputBuffer {

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
        // BEGIN android-changed
        // Workaround for http://b/3514259. We take 'buffersize' as a hint in
        // the weakest sense, and always use an 8KiB heap buffer and leave the
        // kernel buffer size alone, trusting the system to have set a
        // network-appropriate default.
        init(socket.getInputStream(), 8192, params);
        // END android-changed
}

public boolean isDataAvailable(int timeout) throws IOException {
boolean result = hasBufferedData();
if (!result) {
//Synthetic comment -- @@ -102,7 +79,7 @@
fillBuffer();
result = hasBufferedData();
} catch (InterruptedIOException e) {
                if (!(e instanceof SocketTimeoutException)) {
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
        // BEGIN android-changed
        // Workaround for http://b/1083103 and http://b/3514259. We take
        // 'buffersize' as a hint in the weakest sense, and always use
        // an 8KiB heap buffer and leave the kernel buffer size alone,
        // trusting the system to have set a network-appropriate default.
        init(socket.getOutputStream(), 8192, params);
        // END android-changed
}

}







