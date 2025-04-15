/*telephony: Ignore UNSOL_NITZ in case of Cdma

Ignore UNSOL_NITZ if time services not running
Otherwise, time updated with UNSOL_NITZ could overwrite
the RTC causing the modem to loose synchronization
with the network in CDMA mode

Change-Id:I51496ff692e62dff802f2934b0f440b1ffb9207dCRs-Fixed: 214153, 211003, 246895
CRs-Fixed: 287112*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 03a71d8..e0ba555 100755

//Synthetic comment -- @@ -426,12 +426,16 @@
break;

case EVENT_NITZ_TIME:
            if (SystemProperties.getBoolean("persist.timed.enable", false)) {
                ar = (AsyncResult) msg.obj;

                String nitzString = (String)((Object[])ar.result)[0];
                long nitzReceiveTime = ((Long)((Object[])ar.result)[1]).longValue();

                setTimeFromNITZString(nitzString, nitzReceiveTime);
            } else {
                log("EVENT_NITZ_TIME received, ignore updating time");
            }
break;

case EVENT_SIGNAL_STRENGTH_UPDATE:







