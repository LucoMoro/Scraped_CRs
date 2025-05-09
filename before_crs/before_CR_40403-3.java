/*NsdService: Clear client info after resolution request is serviced.

Currently, the client requests tracked in mClientIds for resolution
requests isn't cleared at all and causes failues of additional registration
and discovery requests once the number of requests reaches MAX_LIMIT. In
addition, bubble up unhandled native events to default state of nsd state
machine.

Change-Id:Ief14e0fff644aa2698fcddd71f538820f802be58*/
//Synthetic comment -- diff --git a/services/java/com/android/server/NsdService.java b/services/java/com/android/server/NsdService.java
//Synthetic comment -- index 87843d9..3c1f462 100644

//Synthetic comment -- @@ -388,8 +388,10 @@
break;
case NsdManager.NATIVE_DAEMON_EVENT:
NativeEvent event = (NativeEvent) msg.obj;
                        handleNativeEvent(event.code, event.raw,
                                NativeDaemonEvent.unescapeArgs(event.raw));
break;
default:
result = NOT_HANDLED;
//Synthetic comment -- @@ -397,6 +399,127 @@
}
return result;
}
}
}

//Synthetic comment -- @@ -482,8 +605,8 @@
}

private class NativeEvent {
        int code;
        String raw;

NativeEvent(int code, String raw) {
this.code = code;
//Synthetic comment -- @@ -505,123 +628,6 @@
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
        int clientId = -1;
        int keyId = clientInfo.mClientIds.indexOfValue(id);
        if (keyId != -1) {
            clientId = clientInfo.mClientIds.keyAt(keyId);
        }
        switch (code) {
            case NativeResponseCode.SERVICE_FOUND:
                /* NNN uniqueId serviceName regType domain */
                if (DBG) Slog.d(TAG, "SERVICE_FOUND Raw: " + raw);
                servInfo = new NsdServiceInfo(cooked[2], cooked[3], null);
                clientInfo.mChannel.sendMessage(NsdManager.SERVICE_FOUND, 0,
                        clientId, servInfo);
                break;
            case NativeResponseCode.SERVICE_LOST:
                /* NNN uniqueId serviceName regType domain */
                if (DBG) Slog.d(TAG, "SERVICE_LOST Raw: " + raw);
                servInfo = new NsdServiceInfo(cooked[2], cooked[3], null);
                clientInfo.mChannel.sendMessage(NsdManager.SERVICE_LOST, 0,
                        clientId, servInfo);
                break;
            case NativeResponseCode.SERVICE_DISCOVERY_FAILED:
                /* NNN uniqueId errorCode */
                if (DBG) Slog.d(TAG, "SERVICE_DISC_FAILED Raw: " + raw);
                clientInfo.mChannel.sendMessage(NsdManager.DISCOVER_SERVICES_FAILED,
                        NsdManager.FAILURE_INTERNAL_ERROR, clientId);
                break;
            case NativeResponseCode.SERVICE_REGISTERED:
                /* NNN regId serviceName regType */
                if (DBG) Slog.d(TAG, "SERVICE_REGISTERED Raw: " + raw);
                servInfo = new NsdServiceInfo(cooked[2], null, null);
                clientInfo.mChannel.sendMessage(NsdManager.REGISTER_SERVICE_SUCCEEDED,
                        id, clientId, servInfo);
                break;
            case NativeResponseCode.SERVICE_REGISTRATION_FAILED:
                /* NNN regId errorCode */
                if (DBG) Slog.d(TAG, "SERVICE_REGISTER_FAILED Raw: " + raw);
                clientInfo.mChannel.sendMessage(NsdManager.REGISTER_SERVICE_FAILED,
                        NsdManager.FAILURE_INTERNAL_ERROR, clientId);
                break;
            case NativeResponseCode.SERVICE_UPDATED:
                /* NNN regId */
                break;
            case NativeResponseCode.SERVICE_UPDATE_FAILED:
                /* NNN regId errorCode */
                break;
            case NativeResponseCode.SERVICE_RESOLVED:
                /* NNN resolveId fullName hostName port txtlen txtdata */
                if (DBG) Slog.d(TAG, "SERVICE_RESOLVED Raw: " + raw);
                int index = cooked[2].indexOf(".");
                if (index == -1) {
                    Slog.e(TAG, "Invalid service found " + raw);
                    break;
                }
                String name = cooked[2].substring(0, index);
                String rest = cooked[2].substring(index);
                String type = rest.replace(".local.", "");

                clientInfo.mResolvedService.setServiceName(name);
                clientInfo.mResolvedService.setServiceType(type);
                clientInfo.mResolvedService.setPort(Integer.parseInt(cooked[4]));

                stopResolveService(id);
                if (!getAddrInfo(id, cooked[3])) {
                    clientInfo.mChannel.sendMessage(NsdManager.RESOLVE_SERVICE_FAILED,
                            NsdManager.FAILURE_INTERNAL_ERROR, clientId);
                    mIdToClientInfoMap.remove(id);
                    clientInfo.mResolvedService = null;
                }
                break;
            case NativeResponseCode.SERVICE_RESOLUTION_FAILED:
                /* NNN resolveId errorCode */
                if (DBG) Slog.d(TAG, "SERVICE_RESOLVE_FAILED Raw: " + raw);
                stopResolveService(id);
                mIdToClientInfoMap.remove(id);
                clientInfo.mResolvedService = null;
                clientInfo.mChannel.sendMessage(NsdManager.RESOLVE_SERVICE_FAILED,
                        NsdManager.FAILURE_INTERNAL_ERROR, clientId);
                break;
            case NativeResponseCode.SERVICE_GET_ADDR_FAILED:
                /* NNN resolveId errorCode */
                stopGetAddrInfo(id);
                mIdToClientInfoMap.remove(id);
                clientInfo.mResolvedService = null;
                if (DBG) Slog.d(TAG, "SERVICE_RESOLVE_FAILED Raw: " + raw);
                clientInfo.mChannel.sendMessage(NsdManager.RESOLVE_SERVICE_FAILED,
                        NsdManager.FAILURE_INTERNAL_ERROR, clientId);
                break;
            case NativeResponseCode.SERVICE_GET_ADDR_SUCCESS:
                /* NNN resolveId hostname ttl addr */
                if (DBG) Slog.d(TAG, "SERVICE_GET_ADDR_SUCCESS Raw: " + raw);
                try {
                    clientInfo.mResolvedService.setHost(InetAddress.getByName(cooked[4]));
                    clientInfo.mChannel.sendMessage(NsdManager.RESOLVE_SERVICE_SUCCEEDED,
                            0, clientId, clientInfo.mResolvedService);
                } catch (java.net.UnknownHostException e) {
                    clientInfo.mChannel.sendMessage(NsdManager.RESOLVE_SERVICE_FAILED,
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
if (DBG) Slog.d(TAG, "startMDnsDaemon");
try {
//Synthetic comment -- @@ -800,8 +806,8 @@
private class ClientInfo {

private static final int MAX_LIMIT = 10;
        private AsyncChannel mChannel;
        private Messenger mMessenger;
/* Remembers a resolved service until getaddrinfo completes */
private NsdServiceInfo mResolvedService;








