//<Beginning of snippet n. 0>
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.DataConnection.FailCause;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionAc;
import com.android.internal.telephony.DataConnectionTracker;

private ArrayList<DataCallState> previousDataCallStates = new ArrayList<>();

protected void onDataStateChanged(AsyncResult ar) {
    ArrayList<DataCallState> dataCallStates = (ArrayList<DataCallState>)(ar.result);

    if (ar.exception != null) {
        // Handle error case
        return;
    }
    
    boolean isActiveOrDormantConnectionPresent = false;
    boolean hasChanged = false;

    for (int index = 0; index < dataCallStates.size(); index++) {
        boolean connectionState = dataCallStates.get(index).active;
        if (connectionState != DATA_CONNECTION_ACTIVE_PH_LINK_INACTIVE) {
            isActiveOrDormantConnectionPresent = true;
            if (previousDataCallStates.size() > index) {
                if (!dataCallStates.get(index).equals(previousDataCallStates.get(index))) {
                    hasChanged = true;
                    break;
                }
            }
        }
    }

    if (hasChanged) {
        notifyLinkPropertyChanges(dataCallStates);
    }

    previousDataCallStates.clear();
    previousDataCallStates.addAll(dataCallStates);
}

private void notifyLinkPropertyChanges(ArrayList<DataCallState> dataCallStates) {
    // Code to send notifications for link property changes
    // (implement the notification logic as required)
}
//<End of snippet n. 0>