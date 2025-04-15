/*Send out failiure reason when connect to an APN fails

When a connection to an APN failed, the platform will not send
the failure reason out. This is not convenient for the upper
applications, as some application may want to do show the user
an error dialog or make some decision based on the failure
reason. Some mobile carriers have requirements concerning this.

Fixed by sending out the failure reason.

Change-Id:Ib5ef1dd3c1f180298ae35e43b95ed5b73d3b020e*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index ab9cf2a..105007f 100644

//Synthetic comment -- @@ -1043,6 +1043,7 @@

private void notifyNoData(GsmDataConnection.FailCause lastFailCauseCode) {
setState(State.FAILED);
        mGsmPhone.notifyDataConnectionFailed(lastFailCauseCode.toString());
}

protected void onRecordsLoaded() {







