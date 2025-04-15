/*NsdManager: Updated documentation to warn usage of listerns implementing
more than one callback listener interface and specify the maximum number
of outstanding requests. NsdManager also throws a Runtime Exception when
async channel with NsdService fails to setup.

Change-Id:I9df2a3e1c338df1e3ff79de88d48c1e8b4756285*/
//Synthetic comment -- diff --git a/core/java/android/net/nsd/NsdManager.java b/core/java/android/net/nsd/NsdManager.java
//Synthetic comment -- index 08ba728..631fdc4 100644

//Synthetic comment -- @@ -111,6 +111,13 @@
* resolve is notified on {@link ResolveListener#onServiceResolved} and a failure is notified
* on {@link ResolveListener#onResolveFailed}.
*
* Applications can reserve for a service type at
* http://www.iana.org/form/ports-service. Existing services can be found at
* http://www.iana.org/assignments/service-names-port-numbers/service-names-port-numbers.xml
//Synthetic comment -- @@ -210,8 +217,6 @@
/** Dns based service discovery protocol */
public static final int PROTOCOL_DNS_SD = 0x0001;

    private Context mContext;

private static final int INVALID_LISTENER_KEY = 0;
private int mListenerKey = 1;
private final SparseArray mListenerMap = new SparseArray();
//Synthetic comment -- @@ -232,8 +237,7 @@
*/
public NsdManager(Context context, INsdManager service) {
mService = service;
        mContext = context;
        init();
}

/**
//Synthetic comment -- @@ -305,14 +309,22 @@
boolean listenerRemove = true;
switch (message.what) {
case AsyncChannel.CMD_CHANNEL_HALF_CONNECTED:
mAsyncChannel.sendMessage(AsyncChannel.CMD_CHANNEL_FULL_CONNECTION);
mConnected.countDown();
break;
case AsyncChannel.CMD_CHANNEL_FULLY_CONNECTED:
                    // Ignore
break;
case AsyncChannel.CMD_CHANNEL_DISCONNECTED:
Log.e(TAG, "Channel lost");
break;
case DISCOVER_SERVICES_STARTED:
String s = ((NsdServiceInfo) message.obj).getServiceType();
//Synthetic comment -- @@ -425,13 +437,13 @@
/**
* Initialize AsyncChannel
*/
    private void init() {
final Messenger messenger = getMessenger();
if (messenger == null) throw new RuntimeException("Failed to initialize");
HandlerThread t = new HandlerThread("NsdManager");
t.start();
mHandler = new ServiceHandler(t.getLooper());
        mAsyncChannel.connect(mContext, mHandler, messenger);
try {
mConnected.await();
} catch (InterruptedException e) {







