/*Add DORMANT state to GSM data link activity

GsmDataConnectionTracker sets its activity flag to DORMANT and notifies
LinkActivity when RIL indicates that all its data calls are in
dormant state (i.e. active is set to 1 in UNSOL_DATA_CALL_LIST_CHANGED
message for all data calls)

Change-Id:I81b9db36375fea4f9fb946ddd5ddf0e1d16cbbab*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnectionTracker.java b/src/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index 89a02d3..ef50b91 100644

//Synthetic comment -- @@ -65,6 +65,13 @@
protected static final boolean DBG = true;
protected static final boolean VDBG = false;


/** Delay between APN attempts.
Note the property override mechanism is there just for testing purpose only. */








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 1088131..20f1d08 100644

//Synthetic comment -- @@ -80,14 +80,6 @@
"com.android.internal.telephony.cdma-data-stall";


    /**
     * Constants for the data connection activity:
     * physical link down/up
     */
     private static final int DATA_CONNECTION_ACTIVE_PH_LINK_INACTIVE = 0;
     private static final int DATA_CONNECTION_ACTIVE_PH_LINK_DOWN = 1;
     private static final int DATA_CONNECTION_ACTIVE_PH_LINK_UP = 2;

private static final String[] mSupportedApnTypes = {
PhoneConstants.APN_TYPE_DEFAULT,
PhoneConstants.APN_TYPE_MMS,








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 8c5368e..34ee647 100644

//Synthetic comment -- @@ -343,6 +343,10 @@
case DATAINANDOUT:
ret = DataActivityState.DATAINANDOUT;
break;
}
}









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 016513c..0969ec8 100644

//Synthetic comment -- @@ -1219,6 +1219,9 @@
}
if (DBG) log("onDataStateChanged(ar): DataCallState size=" + dataCallStates.size());

// Create a hash map to store the dataCallState of each DataConnectionAc
HashMap<DataCallState, DataConnectionAc> dataCallStateToDcac;
dataCallStateToDcac = new HashMap<DataCallState, DataConnectionAc>();
//Synthetic comment -- @@ -1240,6 +1243,9 @@
continue;
}

// The list of apn's associated with this DataConnection
Collection<ApnContext> apns = dcac.getApnListSync();

//Synthetic comment -- @@ -1325,6 +1331,28 @@
}
}

if (apnsToCleanup.size() != 0) {
// Add an event log when the network drops PDP
int cid = getCellLocationId();
//Synthetic comment -- @@ -1560,7 +1588,8 @@
} else if (sent == 0 && received > 0) {
newActivity = DctConstants.Activity.DATAIN;
} else {
                newActivity = DctConstants.Activity.NONE;
}

if (mActivity != newActivity && mIsScreenOn) {







