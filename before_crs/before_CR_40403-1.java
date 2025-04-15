/*NsdService: Fix to clean client requests after resolution request
is serviced and to bubble up unhandled native events to default
state of nsd state machine.

Currently, the client requests tracked in mClientIds for resolution
requests isn't cleared and causes failues of additional registration
and discovery requests once the limit reached MAX_LIMIT.

Change-Id:Ief14e0fff644aa2698fcddd71f538820f802be58*/
//Synthetic comment -- diff --git a/services/java/com/android/server/NsdService.java b/services/java/com/android/server/NsdService.java
//Synthetic comment -- index 87843d9..8c582ea 100644

//Synthetic comment -- @@ -388,8 +388,10 @@
break;
case NsdManager.NATIVE_DAEMON_EVENT:
NativeEvent event = (NativeEvent) msg.obj;
                        handleNativeEvent(event.code, event.raw,
                                NativeDaemonEvent.unescapeArgs(event.raw));
break;
default:
result = NOT_HANDLED;
//Synthetic comment -- @@ -482,8 +484,8 @@
}

private class NativeEvent {
        int code;
        String raw;

NativeEvent(int code, String raw) {
this.code = code;
//Synthetic comment -- @@ -505,13 +507,15 @@
}
}

    private void handleNativeEvent(int code, String raw, String[] cooked) {
NsdServiceInfo servInfo;
int id = Integer.parseInt(cooked[1]);
ClientInfo clientInfo = mIdToClientInfoMap.get(id);
if (clientInfo == null) {
Slog.e(TAG, "Unique id with no client mapping: " + id);
            return;
}

/* This goes in response as msg.arg2 */
//Synthetic comment -- @@ -580,7 +584,7 @@
if (!getAddrInfo(id, cooked[3])) {
clientInfo.mChannel.sendMessage(NsdManager.RESOLVE_SERVICE_FAILED,
NsdManager.FAILURE_INTERNAL_ERROR, clientId);
                    mIdToClientInfoMap.remove(id);
clientInfo.mResolvedService = null;
}
break;
//Synthetic comment -- @@ -588,7 +592,7 @@
/* NNN resolveId errorCode */
if (DBG) Slog.d(TAG, "SERVICE_RESOLVE_FAILED Raw: " + raw);
stopResolveService(id);
                mIdToClientInfoMap.remove(id);
clientInfo.mResolvedService = null;
clientInfo.mChannel.sendMessage(NsdManager.RESOLVE_SERVICE_FAILED,
NsdManager.FAILURE_INTERNAL_ERROR, clientId);
//Synthetic comment -- @@ -596,7 +600,7 @@
case NativeResponseCode.SERVICE_GET_ADDR_FAILED:
/* NNN resolveId errorCode */
stopGetAddrInfo(id);
                mIdToClientInfoMap.remove(id);
clientInfo.mResolvedService = null;
if (DBG) Slog.d(TAG, "SERVICE_RESOLVE_FAILED Raw: " + raw);
clientInfo.mChannel.sendMessage(NsdManager.RESOLVE_SERVICE_FAILED,
//Synthetic comment -- @@ -614,12 +618,14 @@
NsdManager.FAILURE_INTERNAL_ERROR, clientId);
}
stopGetAddrInfo(id);
                mIdToClientInfoMap.remove(id);
clientInfo.mResolvedService = null;
break;
default:
break;
}
}

private boolean startMDnsDaemon() {
//Synthetic comment -- @@ -800,8 +806,8 @@
private class ClientInfo {

private static final int MAX_LIMIT = 10;
        private AsyncChannel mChannel;
        private Messenger mMessenger;
/* Remembers a resolved service until getaddrinfo completes */
private NsdServiceInfo mResolvedService;








