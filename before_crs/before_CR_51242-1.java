/*Telephony: CDMA: Handle call collision scenarios

Handle the case when RIL sends multiple calls in the call list
due to a call collision

Change-Id:I845ed804d7501334d7f72671a6e645dd950dca6aCRs-Fixed: 217808*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaCallTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaCallTracker.java
//Synthetic comment -- index 8ec5633..25d8cb7 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.telephony.Rlog;
import android.os.SystemProperties;

import com.android.internal.telephony.CallStateException;
//Synthetic comment -- @@ -38,6 +39,7 @@
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


//Synthetic comment -- @@ -53,7 +55,7 @@

//***** Constants

    static final int MAX_CONNECTIONS = 1;   // only 1 connection allowed in CDMA
static final int MAX_CONNECTIONS_PER_CALL = 1; // only 1 connection allowed per call

//***** Instance Variables
//Synthetic comment -- @@ -471,12 +473,44 @@
}
}

// ***** Overwritten from CallTracker

protected void
handlePollCalls(AsyncResult ar) {
List polledCalls;

if (ar.exception == null) {
polledCalls = (List)ar.result;
} else if (isCommandExceptionRadioNotAvailable(ar.exception)) {
//Synthetic comment -- @@ -496,6 +530,7 @@
boolean needsPollDelay = false;
boolean unknownConnectionAppeared = false;

for (int i = 0, curDC = 0, dcSize = polledCalls.size()
; i < connections.length; i++) {
CdmaConnection conn = connections[i];
//Synthetic comment -- @@ -515,7 +550,19 @@
if (DBG_POLL) log("poll: conn[i=" + i + "]=" +
conn+", dc=" + dc);

if (conn == null && dc != null) {
// Connection appeared in CLCC response that we don't know about
if (pendingMO != null && pendingMO.compareTo(dc)) {

//Synthetic comment -- @@ -560,26 +607,36 @@
}
hasNonHangupStateChanged = true;
} else if (conn != null && dc == null) {
                // This case means the RIL has no more active call anymore and
                // we need to clean up the foregroundCall and ringingCall.
                // Loop through foreground call connections as
                // it contains the known logical connections.
                int count = foregroundCall.connections.size();
                for (int n = 0; n < count; n++) {
                    if (Phone.DEBUG_PHONE) log("adding fgCall cn " + n + " to droppedDuringPoll");
                    CdmaConnection cn = (CdmaConnection)foregroundCall.connections.get(n);
                    droppedDuringPoll.add(cn);
}
                count = ringingCall.connections.size();
                // Loop through ringing call connections as
                // it may contain the known logical connections.
                for (int n = 0; n < count; n++) {
                    if (Phone.DEBUG_PHONE) log("adding rgCall cn " + n + " to droppedDuringPoll");
                    CdmaConnection cn = (CdmaConnection)ringingCall.connections.get(n);
                    droppedDuringPoll.add(cn);
                }
                foregroundCall.setGeneric(false);
                ringingCall.setGeneric(false);

// Re-start Ecm timer when the connected emergency call ends
if (mIsEcmTimerCanceled) {
//Synthetic comment -- @@ -591,9 +648,9 @@
// Dropped connections are removed from the CallTracker
// list but kept in the Call list
connections[i] = null;
            } else if (conn != null && dc != null) { /* implicit conn.compareTo(dc) */
                // Call collision case
if (conn.isIncoming != dc.isMT) {
if (dc.isMT == true){
// Mt call takes precedence than Mo,drops Mo
droppedDuringPoll.add(conn);
//Synthetic comment -- @@ -715,6 +772,7 @@
if (hasNonHangupStateChanged || newRinging != null) {
phone.notifyPreciseCallStateChanged();
}

//dumpState();
}







