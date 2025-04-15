/*Fix a race condition in NativeDaemonConnector

Fixes a race between the onDaemonConnected callback and setting the
mOutputStream in NativeDaemonConnector.

MountService connects to vold using the NativeDaemonConnector.
Throws a “NativeDaemonConnectorException: No output stream!”
when the onDaemonConnected callback in MountService calls the doListCommand.

Change-Id:Ib895bab37f7df680e4362df6366198c0a673c5e9*/




//Synthetic comment -- diff --git a/services/java/com/android/server/NativeDaemonConnector.java b/services/java/com/android/server/NativeDaemonConnector.java
//Synthetic comment -- index 7b68d68..c0c6c36 100644

//Synthetic comment -- @@ -97,11 +97,12 @@
LocalSocketAddress.Namespace.RESERVED);

socket.connect(address);

InputStream inputStream = socket.getInputStream();
mOutputStream = socket.getOutputStream();

            mCallbacks.onDaemonConnected();

byte[] buffer = new byte[BUFFER_SIZE];
int start = 0;








