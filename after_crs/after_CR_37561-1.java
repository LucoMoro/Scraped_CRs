/*CB: Turn on CMAS Presidential

Per 3GPP TS 22.268 Section 6.2, CMAS Presidential must be turned on.

Change-Id:I37e2d583e132172658ffb2ed00db2dfd695bbc29*/




//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastConfigService.java b/src/com/android/cellbroadcastreceiver/CellBroadcastConfigService.java
//Synthetic comment -- index 112dfc7..485352a 100644

//Synthetic comment -- @@ -52,12 +52,14 @@
}

private void setChannelRange(SmsManager manager, String ranges, boolean enable) {
        if (DBG) Log.d(TAG, "setChannelRange: " + ranges);

try {
for (String channelRange : ranges.split(",")) {
int dashIndex = channelRange.indexOf('-');
if (dashIndex != -1) {
                    int startId = Integer.decode(channelRange.substring(0, dashIndex).trim());
                    int endId = Integer.decode(channelRange.substring(dashIndex + 1).trim());
if (enable) {
if (DBG) Log.d(TAG, "enabling emergency IDs " + startId + '-' + endId);
manager.enableCellBroadcastRange(startId, endId);
//Synthetic comment -- @@ -66,7 +68,7 @@
manager.disableCellBroadcastRange(startId, endId);
}
} else {
                    int messageId = Integer.decode(channelRange.trim());
if (enable) {
if (DBG) Log.d(TAG, "enabling emergency message ID " + messageId);
manager.enableCellBroadcast(messageId);
//Synthetic comment -- @@ -79,6 +81,10 @@
} catch (NumberFormatException e) {
Log.e(TAG, "Number Format Exception parsing emergency channel range", e);
}

        // Make sure CMAS Presidential is enabled (See 3GPP TS 22.268 Section 6.2).
        if (DBG) Log.d(TAG, "setChannelRange: enabling CMAS Presidential");
        manager.enableCellBroadcast(SmsCbConstants.MESSAGE_ID_CMAS_ALERT_PRESIDENTIAL_LEVEL);
}

static boolean isOperatorDefinedEmergencyId(int messageId) {
//Synthetic comment -- @@ -91,13 +97,13 @@
for (String channelRange : emergencyIdRange.split(",")) {
int dashIndex = channelRange.indexOf('-');
if (dashIndex != -1) {
                    int startId = Integer.decode(channelRange.substring(0, dashIndex).trim());
                    int endId = Integer.decode(channelRange.substring(dashIndex + 1).trim());
if (messageId >= startId && messageId <= endId) {
return true;
}
} else {
                    int emergencyMessageId = Integer.decode(channelRange.trim());
if (emergencyMessageId == messageId) {
return true;
}
//Synthetic comment -- @@ -135,6 +141,10 @@
manager.enableCellBroadcastRange(
SmsCbConstants.MESSAGE_ID_PWS_FIRST_IDENTIFIER,
SmsCbConstants.MESSAGE_ID_PWS_LAST_IDENTIFIER);

                        // CMAS Presidential must be on (See 3GPP TS 22.268 Section 6.2).
                        manager.enableCellBroadcast(
                               SmsCbConstants.MESSAGE_ID_CMAS_ALERT_PRESIDENTIAL_LEVEL);
}
if (DBG) Log.d(TAG, "enabled emergency cell broadcast channels");
} else {
//Synthetic comment -- @@ -144,9 +154,13 @@
setChannelRange(manager, emergencyIdRange, false);
} else {
// No emergency channel system property, disable all emergency channels
                        // except for CMAS Presidential (See 3GPP TS 22.268 Section 6.2)
manager.disableCellBroadcastRange(
SmsCbConstants.MESSAGE_ID_PWS_FIRST_IDENTIFIER,
SmsCbConstants.MESSAGE_ID_PWS_LAST_IDENTIFIER);

                        manager.enableCellBroadcast(
                                SmsCbConstants.MESSAGE_ID_CMAS_ALERT_PRESIDENTIAL_LEVEL);
}
if (DBG) Log.d(TAG, "disabled emergency cell broadcast channels");
}







