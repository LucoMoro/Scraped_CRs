/*Telephony:Avoid updateLinkProperty when the state is Inactive

When the user add APN for data connection and delete
the APN,doing this scenario multiple times. At one point of time,
the SETUP_DATA_CALL reponse and DEACTIVATE request are happening
at same time. Because of this,DEACTIVATE_DAT_CALL reponse and
UNSOL_DATA_CALL_LIST_CHANGED are handling at the same time in
different threads(UNSOL_DATA_CALL_LIST_CHANGED in DataConnection
Tracker thread and DEACTIVATE_DATA_CALL Response in Data
Connection thread with the span of msec timegap). In the less
span of time DEACTIVATE reponse is handled,APN settings are
cleared (ClearSettings API called) in Data Connection thread and
those settings are accessing at the time of handling
UNSOL_DATA_CALL_LIST_CHANGED(updateLinkPropertiesDataCallState
Sync API called) through DataConnection API updateLinkProperty.
Because of this there is a Null Pointer exception
@DataConnection.getDataCallProtocol

Do not update the updateLinkProperty in DataConnection when the
state is Inactive.

Change-Id:Iae0e4c91b037cae211473fe1c34d592225f41b0f*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnection.java b/src/java/com/android/internal/telephony/DataConnection.java
//Synthetic comment -- index 9751040..57f6efd 100644

//Synthetic comment -- @@ -705,8 +705,14 @@
}
case DataConnectionAc.REQ_UPDATE_LINK_PROPERTIES_DATA_CALL_STATE: {
DataCallState newState = (DataCallState) msg.obj;
                    UpdateLinkPropertyResult result =
                                             updateLinkProperty(newState);
if (VDBG) {
log("REQ_UPDATE_LINK_PROPERTIES_DATA_CALL_STATE result="
+ result + " newState=" + newState);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index ebb41fc..6bf5b11 100644

//Synthetic comment -- @@ -1259,7 +1259,9 @@
// Its active so update the DataConnections link properties
UpdateLinkPropertyResult result =
dcac.updateLinkPropertiesDataCallStateSync(newState);
                    if (result.oldLp.equals(result.newLp)) {
if (DBG) log("onDataStateChanged(ar): no change");
} else {
if (result.oldLp.isIdenticalInterfaceName(result.newLp)) {







