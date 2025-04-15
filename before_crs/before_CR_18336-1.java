/*frameworks/base: Notify of dismissed call on radio technology change

	Make CallTrackers notify UI when ending calls in dispose, since
	it has already unregistered, and won't receive the CALL_STATE_CHANGED

Change-Id:Ie4901fe915bbe2254736f32b6f623f92fdb79f45*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaCallTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaCallTracker.java
//Synthetic comment -- index 3669e60..89ae46c 100644

//Synthetic comment -- @@ -107,14 +107,22 @@
cm.unregisterForCallWaitingInfo(this);
for(CdmaConnection c : connections) {
try {
                if(c != null) hangup(c);
} catch (CallStateException ex) {
Log.e(LOG_TAG, "unexpected error on hangup during dispose");
}
}

try {
            if(pendingMO != null) hangup(pendingMO);
} catch (CallStateException ex) {
Log.e(LOG_TAG, "unexpected error on hangup during dispose");
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmCallTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmCallTracker.java
//Synthetic comment -- index 06f310c..a68d062 100644

//Synthetic comment -- @@ -109,14 +109,22 @@

for(GsmConnection c : connections) {
try {
                if(c != null) hangup(c);
} catch (CallStateException ex) {
Log.e(LOG_TAG, "unexpected error on hangup during dispose");
}
}

try {
            if(pendingMO != null) hangup(pendingMO);
} catch (CallStateException ex) {
Log.e(LOG_TAG, "unexpected error on hangup during dispose");
}







