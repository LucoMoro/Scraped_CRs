/*P2P: Android Beam deadlock fix

When doing P2P transfer between 2 phones, it could happen that activity
which content is to be shared goes on Pause (e.g some other activity
could take over). In this case updateNfcService will be called from
NfcActivityManager, which will call setNdefToSend method from
P2pLinkManager. If at the same time a p2p target discovered event comes,
it will be processed and NDEF createMessage from NfcActivityManager is
triggered. Since setNdefToSend and event handling from P2pLinkManager are
mutually exclusive, as well as createMessage and updateNfcService
methods from NfcActivity manager, a deadlock situation will take place.
To avoid this, the update of NDEF message and NDEF callback, previously
done by setNdefToSend will now be done in a separate thread which will
receive a message with new NDEF message and callback to be updated.
Like this, setNdefToSend will not be blocking method anymore.

Note that a separate message handler thread is used to logically
distinguish handling of messages related to physical P2P link from the
application messages (e.g sent from application activities).

Change-Id:I35eceb96a689cd420713f593a60cacbedfe37489Author: Dejan REBRACA <dejanx.rebraca@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 28357*/
//Synthetic comment -- diff --git a/src/com/android/nfc/P2pLinkManager.java b/src/com/android/nfc/P2pLinkManager.java
//Synthetic comment -- index 9b23f65..79253bc 100755

//Synthetic comment -- @@ -136,6 +136,7 @@
static final int MSG_START_ECHOSERVER = 5;
static final int MSG_STOP_ECHOSERVER = 6;
static final int MSG_HANDOVER_NOT_SUPPORTED = 7;

// values for mLinkState
static final int LINK_STATE_DOWN = 1;
//Synthetic comment -- @@ -209,6 +210,24 @@
mDefaultRwSize = defaultRwSize;
}

/**
* May be called from any thread.
* Assumes that NFC is already on if any parameter is true.
//Synthetic comment -- @@ -243,9 +262,10 @@
* active as soon as P2P send is enabled.
*/
public void setNdefCallback(INdefPushCallback callbackNdef) {
        synchronized (this) {
            mCallbackNdef = callbackNdef;
        }
}

/**







