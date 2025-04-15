/*Fix infinite loop in ConnectionPool#get

If a connection isn't eligible to be recycled or
if it can't be tagged, it must be removed from the list
of connections in the pool after it's closed.

Change-Id:Icb0646ae5c9624fa3fca3be0fec4673e08a2a39e*/
//Synthetic comment -- diff --git a/src/main/java/com/squareup/okhttp/ConnectionPool.java b/src/main/java/com/squareup/okhttp/ConnectionPool.java
//Synthetic comment -- index afb0e58..50d2dbd 100644

//Synthetic comment -- @@ -79,24 +79,33 @@
synchronized (connectionPool) {
List<Connection> connections = connectionPool.get(address);
while (connections != null) {
                Connection connection = connections.get(connections.size() - 1);
                if (!connection.isSpdy()) {
                    connections.remove(connections.size() - 1);
                }
if (connections.isEmpty()) {
connectionPool.remove(address);
connections = null;
}
if (!connection.isEligibleForRecycling()) {
Util.closeQuietly(connection);
continue;
}
try {
Platform.get().tagSocket(connection.getSocket());
} catch (SocketException e) {
// When unable to tag, skip recycling and close
Platform.get().logW("Unable to tagSocket(): " + e);
Util.closeQuietly(connection);
continue;
}
return connection;







