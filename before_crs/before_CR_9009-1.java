/*Object not initialized yet.*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/PdpConnection.java b/telephony/java/com/android/internal/telephony/gsm/PdpConnection.java
//Synthetic comment -- index 6428f70..6ab33ee 100644

//Synthetic comment -- @@ -155,11 +155,6 @@
this.dataLink = null;
receivedDisconnectReq = false;
this.dnsServers = new String[2];

        if (SystemProperties.get("ro.radio.use-ppp","no").equals("yes")) {
            dataLink = new PppLink(phone.mDataConnection);
            dataLink.setOnLinkChange(this, EVENT_LINK_STATE_CHANGED, null);
        }
}

/**
//Synthetic comment -- @@ -199,6 +194,7 @@
if (state == PdpState.ACTIVE) {
if (dataLink != null) {
dataLink.disconnect();
}

if (phone.mCM.getRadioState().isOn()) {
//Synthetic comment -- @@ -406,9 +402,9 @@
} else {
String[] response = ((String[]) ar.result);
cid = Integer.parseInt(response[0]);

if (response.length > 2) {
                            interfaceName = response[1];
ipAddress = response[2];
String prefix = "net." + interfaceName + ".";
gatewayAddress = SystemProperties.get(prefix + "gw");
//Synthetic comment -- @@ -435,10 +431,17 @@
}
}
}
                        
if (dataLink != null) {
dataLink.connect();
} else {
onLinkStateChanged(DataLink.LinkState.LINK_UP);
}








