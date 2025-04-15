/*CB: Add system property to enable/disable dup detection

If system property persist.cb.dup_detection is set to false, duplicate
detection is disabled.  If it is set to true, duplicate detection is
enabled.  If the system property does not exist, using true/enabled
as a default value.

Change-Id:If86bdda9ba06b981c5bfc12393ca02280111163d*/




//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastAlertService.java b/src/com/android/cellbroadcastreceiver/CellBroadcastAlertService.java
//Synthetic comment -- index 3cc821c..6984532 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.telephony.CellBroadcastMessage;
//Synthetic comment -- @@ -52,6 +53,12 @@
/** Use the same notification ID for non-emergency alerts. */
static final int NOTIFICATION_ID = 1;

    /** system property to enable/disable broadcast duplicate detecion.  */
    private static final String CB_DUP_DETECTION = "persist.cb.dup_detection";

    /** Check for system property to enable/disable duplicate detection.  */
    static boolean mUseDupDetection = SystemProperties.getBoolean(CB_DUP_DETECTION, true);

/** Container for message ID and geographical scope, for duplicate message detection. */
private static final class MessageIdAndScope {
private final int mMessageId;
//Synthetic comment -- @@ -133,31 +140,33 @@
return;
}

        if (mUseDupDetection) {
            // Check for duplicate message IDs according to CMAS carrier requirements. Message IDs
            // are stored in volatile memory. If the maximum of 65535 messages is reached, the
            // message ID of the oldest message is deleted from the list.
            MessageIdAndScope newMessageId = new MessageIdAndScope(message.getSerialNumber(),
                    message.getLocation());

            // Add the new message ID to the list. It's okay if this is a duplicate message ID,
            // because the list is only used for removing old message IDs from the hash set.
            if (sCmasIdList.size() < MAX_MESSAGE_ID_SIZE) {
                sCmasIdList.add(newMessageId);
            } else {
                // Get oldest message ID from the list and replace with the new message ID.
                MessageIdAndScope oldestId = sCmasIdList.get(sCmasIdListIndex);
                sCmasIdList.set(sCmasIdListIndex, newMessageId);
                Log.d(TAG, "message ID limit reached, removing oldest message ID " + oldestId);
                // Remove oldest message ID from the set.
                sCmasIdSet.remove(oldestId);
                if (++sCmasIdListIndex >= MAX_MESSAGE_ID_SIZE) {
                    sCmasIdListIndex = 0;
                }
}
            // Set.add() returns false if message ID has already been added
            if (!sCmasIdSet.add(newMessageId)) {
                Log.d(TAG, "ignoring duplicate alert with " + newMessageId);
                return;
            }
}

final Intent alertIntent = new Intent(SHOW_NEW_ALERT_ACTION);







