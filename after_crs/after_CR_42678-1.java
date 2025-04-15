/*Mms: On attachment size exceeded, show error message

When forwarding an MMS message which is more than
the maximum allowed size, message size is checked
by a call to checkMessageSize. Upon message size
less than 0 or greater than allowed maximum message
size, ExceedMessageSizeException is thrown. Any
error's occuring while sending MMS, mms app is
implemented to crash!

With this patch, atleast ExceedMessageSizeException will be
catched and an error message will be shown to the
user instead of allowing the mms app to crash.

Change-Id:Ib165ff7003c2708e775daa02f9a4bbe227f028e1Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 38953*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 2eda7cd..e7c7add 100644

//Synthetic comment -- @@ -118,6 +118,7 @@

import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.mms.ExceedMessageSizeException;
import com.android.mms.LogTag;
import com.android.mms.MmsApp;
import com.android.mms.MmsConfig;
//Synthetic comment -- @@ -3553,13 +3554,18 @@
// them back once the recipient list has settled.
removeRecipientsListeners();

            try {
                mWorkingMessage.send(mDebugRecipients);

                mSentMessage = true;
                mSendingMessage = true;
                addRecipientsListeners();

                mScrollOnSend = true;   // in the next onQueryComplete, scroll the list to the end.
            } catch (ExceedMessageSizeException ex) {
                handleAddAttachmentError(WorkingMessage.MESSAGE_SIZE_EXCEEDED, R.string.type_picture);
                return;
            }
}
// But bail out if we are supposed to exit after the message is sent.
if (mExitOnSent) {







