//<Beginning of snippet n. 0>


import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.DataConnection.FailCause;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionAc;
import com.android.internal.telephony.DataConnectionTracker;

private ArrayList<DataCallState> lastKnownDataCallStates = new ArrayList<>();

protected void onDataStateChanged(AsyncResult ar) {
    ArrayList<DataCallState> dataCallStates = (ArrayList<DataCallState>)(ar.result);

    if (ar.exception != null) {
        return;
    }

    boolean isActiveOrDormantConnectionPresent = false;
    boolean propertiesChanged = false;

    for (int index = 0; index < dataCallStates.size(); index++) {
        DataCallState currentState = dataCallStates.get(index);
        boolean connectionState = currentState.active;

        if (connectionState != DATA_CONNECTION_ACTIVE_PH_LINK_INACTIVE) {
            isActiveOrDormantConnectionPresent = true;
            if (index < lastKnownDataCallStates.size()) {
                propertiesChanged |= !currentState.equals(lastKnownDataCallStates.get(index));
            } else {
                propertiesChanged = true;
            }
        }
    }

    if (propertiesChanged || lastKnownDataCallStates.size() != dataCallStates.size()) {
        lastKnownDataCallStates = dataCallStates;
        notifyChange();
    }
}

//<End of snippet n. 0>