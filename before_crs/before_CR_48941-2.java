/*Telephony: Update call timer for phantom call

For phantom call, update connectTimeReal via onConnectedInOrOut().
connectTimeReal is used to calculate the call duration per
getDurationMillis().
If phantom call is in HOLDING state, update holdingStartTime via
onStartedHolding().  holdingStartTime is used to calculate hold time
per getHoldDurationMillis().

Change-Id:I716f75b7565b5e869403c48de3dd190e2b425626*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaCallTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaCallTracker.java
//Synthetic comment -- index a7d5d0a..63a9a90 100644

//Synthetic comment -- @@ -1112,7 +1112,11 @@
// it won't appear as a Missed Call.
if (dc.state != DriverCall.State.ALERTING
&& dc.state != DriverCall.State.DIALING) {
                connections[i].connectTime = System.currentTimeMillis();
}
}
return newRinging;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaConnection.java b/src/java/com/android/internal/telephony/cdma/CdmaConnection.java
//Synthetic comment -- index 17eecbd..b03793a 100755

//Synthetic comment -- @@ -605,7 +605,7 @@
disconnected = true;
}

    private void
onStartedHolding() {
holdingStartTime = SystemClock.elapsedRealtime();
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java b/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java
//Synthetic comment -- index fc7fe8a..725d85c 100644

//Synthetic comment -- @@ -497,7 +497,11 @@
// it won't appear as a Missed Call.
if (dc.state != DriverCall.State.ALERTING
&& dc.state != DriverCall.State.DIALING) {
                            connections[i].connectTime = System.currentTimeMillis();
}

unknownConnectionAppeared = true;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmConnection.java b/src/java/com/android/internal/telephony/gsm/GsmConnection.java
//Synthetic comment -- index 59aa12a..e3ac6cd 100644

//Synthetic comment -- @@ -550,7 +550,7 @@
releaseWakeLock();
}

    private void
onStartedHolding() {
holdingStartTime = SystemClock.elapsedRealtime();
}







