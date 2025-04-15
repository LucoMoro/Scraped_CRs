/*Telephony: notify the link properties changed in CdmaDCT

The CdmaDCT is not notifying the data connection state changed
when the UNSOL_DATA_CALL_LIST_CHANGED with the same active data
connection but the link properties are changed.
The link properties (IP address, Gateways, DNS) can get modified
in PPP resync scenarios, without disconnecting the existing PDP.
Changes are made to notify the updated link properties from the
CdmaDCT on processing the UNSOL_DATA_CALL_LIST_CHANGED notification.

Change-Id:I5522407600294e3e35705af6775493024b26bb4e*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 2acc5f9..fc29464 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.DataConnection.FailCause;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionAc;
import com.android.internal.telephony.DataConnectionTracker;
//Synthetic comment -- @@ -805,8 +806,22 @@
TelephonyManager.getDefault().getNetworkType());
}

protected void onDataStateChanged(AsyncResult ar) {
ArrayList<DataCallState> dataCallStates = (ArrayList<DataCallState>)(ar.result);

if (ar.exception != null) {
// This is probably "radio not available" or something
//Synthetic comment -- @@ -823,8 +838,33 @@
// the DATA_CALL_LIST array
for (int index = 0; index < dataCallStates.size(); index++) {
connectionState = dataCallStates.get(index).active;
if (connectionState != DATA_CONNECTION_ACTIVE_PH_LINK_INACTIVE) {
isActiveOrDormantConnectionPresent = true;
break;
}
}







