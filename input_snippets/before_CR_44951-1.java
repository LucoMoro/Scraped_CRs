
//<Beginning of snippet n. 0>


import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.DataConnection.FailCause;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionAc;
import com.android.internal.telephony.DataConnectionTracker;
TelephonyManager.getDefault().getNetworkType());
}

protected void onDataStateChanged(AsyncResult ar) {
ArrayList<DataCallState> dataCallStates = (ArrayList<DataCallState>)(ar.result);

if (ar.exception != null) {
// This is probably "radio not available" or something
// the DATA_CALL_LIST array
for (int index = 0; index < dataCallStates.size(); index++) {
connectionState = dataCallStates.get(index).active;
if (connectionState != DATA_CONNECTION_ACTIVE_PH_LINK_INACTIVE) {
isActiveOrDormantConnectionPresent = true;
break;
}
}

//<End of snippet n. 0>








