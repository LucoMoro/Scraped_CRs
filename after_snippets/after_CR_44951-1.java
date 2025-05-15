
//<Beginning of snippet n. 0>


import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.DataConnection.FailCause;
import com.android.internal.telephony.DataConnection.UpdateLinkPropertyResult;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionAc;
import com.android.internal.telephony.DataConnectionTracker;
TelephonyManager.getDefault().getNetworkType());
}

    /**
     * @param cid Connection id provided from RIL.
     * @return DataConnectionAc associated with specified cid.
     */
    private DataConnectionAc findDataConnectionAcByCid(int cid) {
        for (DataConnectionAc dcac : mDataConnectionAsyncChannels.values()) {
            if (dcac.getCidSync() == cid) {
                return dcac;
            }
        }
        return null;
    }

protected void onDataStateChanged(AsyncResult ar) {
ArrayList<DataCallState> dataCallStates = (ArrayList<DataCallState>)(ar.result);
        DataCallState dcState = null;

if (ar.exception != null) {
// This is probably "radio not available" or something
// the DATA_CALL_LIST array
for (int index = 0; index < dataCallStates.size(); index++) {
connectionState = dataCallStates.get(index).active;
                dcState = dataCallStates.get(index);
if (connectionState != DATA_CONNECTION_ACTIVE_PH_LINK_INACTIVE) {
isActiveOrDormantConnectionPresent = true;

                    DataConnectionAc dcac = findDataConnectionAcByCid(dcState.cid);
                    if (dcac != null) {
                        // Its active so update the DataConnections link properties
                        UpdateLinkPropertyResult result =
                            dcac.updateLinkPropertiesDataCallStateSync(dcState);
                        if (result.setupResult == DataCallState.SetupResult.ERR_Stale) {
                            log("onDataStateChanged(ar): state is Inactive no changes");
                        } else if (result.oldLp.equals(result.newLp)) {
                            log("onDataStateChanged(ar): no change");
                        } else {
                            log("onDataStateChanged(ar): there is a change");
                            if (result.oldLp.isIdenticalInterfaceName(result.newLp)) {
                                if (! result.oldLp.isIdenticalDnses(result.newLp) ||
                                        ! result.oldLp.isIdenticalRoutes(result.newLp) ||
                                        ! result.oldLp.isIdenticalHttpProxy(result.newLp) ||
                                        ! result.oldLp.isIdenticalAddresses(result.newLp)) {
                                    mPhone.notifyDataConnection(
                                            PhoneConstants.REASON_LINK_PROPERTIES_CHANGED);
                                }
                            }
                        }
                    }

break;
}
}

//<End of snippet n. 0>








