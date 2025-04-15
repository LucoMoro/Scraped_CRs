/*Telephony: Multiparty Call related fix

Block multiple Call switch request to RIL

Sending a call switch request to RIL before receiving
a response for the previous request causes the lower
layers to not respond. Pending responses from RIL
block the call polling in telephony.

Change-Id:I40eafeefa0860fd69969f0f592c54bd7861b2964CRs-Fixed: 269330, 278014, 282061, 293727, 441309*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java b/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java
//Synthetic comment -- index 2080976..779c06f 100644

//Synthetic comment -- @@ -81,6 +81,8 @@
GsmConnection pendingMO;
boolean hangupPendingMO;

GSMPhone phone;

boolean desiredMute = false;    // false = mute off
//Synthetic comment -- @@ -277,9 +279,12 @@
// Should we bother with this check?
if (ringingCall.getState() == GsmCall.State.INCOMING) {
throw new CallStateException("cannot be in the incoming state");
        } else {
cm.switchWaitingOrHoldingAndActive(
obtainCompleteMessage(EVENT_SWITCH_RESULT));
}
}

//Synthetic comment -- @@ -858,6 +863,7 @@
break;

case EVENT_SWITCH_RESULT:
case EVENT_CONFERENCE_RESULT:
case EVENT_SEPARATE_RESULT:
case EVENT_ECT_RESULT:







