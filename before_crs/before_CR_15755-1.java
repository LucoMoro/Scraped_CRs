/*Fixed problem using HTTPS with Apache HTTP Client when using proxy

Problem described at:http://code.google.com/p/android/issues/detail?id=2690The fix is to set default port for scheme to avoid passing -1 as port,
which would cause an IllegalArgumentException.

Change-Id:Ib1324618cdb6e3aa629dea2a2de1856136223aaf*/
//Synthetic comment -- diff --git a/src/org/apache/http/impl/conn/DefaultClientConnectionOperator.java b/src/org/apache/http/impl/conn/DefaultClientConnectionOperator.java
//Synthetic comment -- index 67e6bb0..fbc762d 100644

//Synthetic comment -- @@ -223,7 +223,7 @@
final Socket sock; 
try {
sock = lsf.createSocket
                (conn.getSocket(), target.getHostName(), target.getPort(), true);
} catch (ConnectException ex) {
throw new HttpHostConnectException(target, ex);
}







