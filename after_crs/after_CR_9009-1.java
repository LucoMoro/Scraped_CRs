/*Object not initialized yet.*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/PdpConnection.java b/telephony/java/com/android/internal/telephony/gsm/PdpConnection.java
//Synthetic comment -- index 6428f70..6ab33ee 100644

//Synthetic comment -- @@ -155,11 +155,6 @@
this.dataLink = null;
receivedDisconnectReq = false;
this.dnsServers = new String[2];
}

/**
//Synthetic comment -- @@ -199,6 +194,7 @@
if (state == PdpState.ACTIVE) {
if (dataLink != null) {
dataLink.disconnect();
                dataLink = null;
}

if (phone.mCM.getRadioState().isOn()) {
//Synthetic comment -- @@ -406,9 +402,9 @@
} else {
String[] response = ((String[]) ar.result);
cid = Integer.parseInt(response[0]);
                        interfaceName = response[1];

if (response.length > 2) {
ipAddress = response[2];
String prefix = "net." + interfaceName + ".";
gatewayAddress = SystemProperties.get(prefix + "gw");
//Synthetic comment -- @@ -435,10 +431,17 @@
}
}
}

                        if (SystemProperties.get("ro.radio.use-ppp","no").equals("yes")) {
                            dataLink = new PppLink(phone.mDataConnection);
                            dataLink.setOnLinkChange(this, EVENT_LINK_STATE_CHANGED, null);
                        }

if (dataLink != null) {
                            if (DBG) log("PDP connecting to dataLink");
dataLink.connect();
} else {
                            if (DBG) log("PDP there is no dataLink");
onLinkStateChanged(DataLink.LinkState.LINK_UP);
}








