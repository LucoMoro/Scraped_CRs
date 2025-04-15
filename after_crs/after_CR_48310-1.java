/*Mms: On attachment size exceeded, show error message

When forwarding an MMS message which is more than
the maximum allowed size, message size is checked
by a call to checkMessageSize. Upon message size
less than 0 or greater than allowed maximum message
size, ExceedMessageSizeException is thrown. Any
error's occuring while sending MMS, mms app is
implemented to crash.

With this patch, atleast ExceedMessageSizeException
will be catched and an error message will be shown
to the user instead of allowing the mms app to crash.

Change-Id:I5e94e98e5627f3cc6417004097fa8af40fe97520Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 38953*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 42bda28..964adc1 100644

//Synthetic comment -- @@ -120,6 +120,7 @@

import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.mms.ExceedMessageSizeException;
import com.android.mms.LogTag;
import com.android.mms.MmsApp;
import com.android.mms.MmsConfig;
//Synthetic comment -- @@ -3722,13 +3723,19 @@
// them back once the recipient list has settled.
removeRecipientsListeners();

            try {
                mWorkingMessage.send(mDebugRecipients);

                mSentMessage = true;
                mSendingMessage = true;
                addRecipientsListeners();

                mScrollOnSend = true;   // in the next onQueryComplete, scroll the list to the end.
            } catch (ExceedMessageSizeException ex) {
                handleAddAttachmentError(WorkingMessage.MESSAGE_SIZE_EXCEEDED,
                        R.string.type_picture);
                return;
            }
}
// But bail out if we are supposed to exit after the message is sent.
if (mExitOnSent) {







