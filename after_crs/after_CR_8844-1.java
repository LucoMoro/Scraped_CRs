/*Fixed PPP bug in Android Telephone framework*/




//Synthetic comment -- diff --git a/core/java/android/net/MobileDataStateTracker.java b/core/java/android/net/MobileDataStateTracker.java
//Synthetic comment -- index 1d939e1..0d1ebce 100644

//Synthetic comment -- @@ -57,7 +57,10 @@
"net.eth0.dns3",
"net.eth0.dns4",
"net.gprs.dns1",
          "net.gprs.dns2",
	  //TI WORKAROUND
          "net.ppp0.dns1",
          "net.ppp0.dns2"
};
private List<String> mDnsServers;
private String mInterfaceName;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/DataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/DataConnectionTracker.java
//Synthetic comment -- index b0b8cdc..4fbeac1 100644

//Synthetic comment -- @@ -339,7 +339,8 @@
phone.getContext().getContentResolver().registerContentObserver(
Telephony.Carriers.CONTENT_URI, true, apnObserver);

	//TI WORKAROUND
        //createAllPdpList();

// This preference tells us 1) initial condition for "dataEnabled",
// and 2) whether the RIL will setup the baseband to auto-PS attach.
//Synthetic comment -- @@ -1593,7 +1594,8 @@
}
}

    //TI WORKAROUND
    public void createAllPdpList() {
pdpList = new ArrayList<PdpConnection>();
PdpConnection pdp;









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index f314944..93abcee 100644

//Synthetic comment -- @@ -176,6 +176,9 @@
mSIMFileHandler = new SIMFileHandler(this);
mSIMRecords = new SIMRecords(this);
mDataConnection = new DataConnectionTracker (this);
	//TI WORKAROUND
	mDataConnection.createAllPdpList();

mSimCard = new GsmSimCard(this);
if (!unitTestMode) {
mSimPhoneBookIntManager = new SimPhoneBookInterfaceManager(this);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/PdpConnection.java b/telephony/java/com/android/internal/telephony/gsm/PdpConnection.java
//Synthetic comment -- index 6428f70..3842808 100644

//Synthetic comment -- @@ -406,7 +406,17 @@
} else {
String[] response = ((String[]) ar.result);
cid = Integer.parseInt(response[0]);

			// TI WORKAROUND
			interfaceName = response[1];
			{                                                           
			    String prefix = "net." + interfaceName + ".";
			    ipAddress = SystemProperties.get(prefix + "local-ip");
			    gatewayAddress = SystemProperties.get(prefix + "gw");
			    dnsServers[0] = SystemProperties.get(prefix + "dns1");
			    dnsServers[1] = SystemProperties.get(prefix + "dns2");
			}
			
if (response.length > 2) {
interfaceName = response[1];
ipAddress = response[2];








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/PppLink.java b/telephony/java/com/android/internal/telephony/gsm/PppLink.java
//Synthetic comment -- index 43d4f1f..cb0d38f 100644

//Synthetic comment -- @@ -82,6 +82,8 @@
SystemService.start(SERVICE_PPPD_GPRS);
removeMessages(EVENT_POLL_DATA_CONNECTION);
Message poll = obtainMessage();
	//TI WORKAROUND
	dataConnection.state = State.CONNECTING;
poll.what = EVENT_POLL_DATA_CONNECTION;
sendMessageDelayed(poll, POLL_SYSFS_MILLIS);
}







