/*Always throw a SocketException from isConnected.

Either a SocketTimeoutException if it's a timeout, or a ConnectException
otherwise.

Bug: 6819002
Change-Id:I06da843bd4dd9a3492f9f1ec3e6cb36fe86ae776*/
//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/IoBridge.java b/luni/src/main/java/libcore/io/IoBridge.java
//Synthetic comment -- index 2d60a86..afb4146 100644

//Synthetic comment -- @@ -216,18 +216,11 @@
cause = errnoException;
}
}
        // TODO: is it really helpful/necessary to throw so many different exceptions?
String detail = connectDetail(inetAddress, port, timeoutMs, cause);
        if (cause.errno == ECONNRESET || cause.errno == ECONNREFUSED ||
                cause.errno == EADDRNOTAVAIL || cause.errno == EADDRINUSE ||
                cause.errno == ENETUNREACH) {
            throw new ConnectException(detail, cause);
        } else if (cause.errno == EACCES) {
            throw new SecurityException(detail, cause);
        } else if (cause.errno == ETIMEDOUT) {
throw new SocketTimeoutException(detail, cause);
}
        throw new SocketException(detail, cause);
}

// Socket options used by java.net but not exposed in SocketOptions.







