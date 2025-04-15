/*Mms: Two popups are displayed when Press Enter key on message screen.

When the framework transfers the KEY EVENT to MMS, both KEY UP and
KEY DOWN event trigger the onEditorAction.
We need to confirm the send message only when we have the KEY DONW
event.

Change-Id:Ia0fcbb9a915f7e6b9f6b0101293eeaae80b3bccdAuthor: Kun Jiang <kunx.jiang@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 20356*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 72df986..09617a4 100644

//Synthetic comment -- @@ -3239,7 +3239,7 @@
if (event != null) {
// if shift key is down, then we want to insert the '\n' char in the TextView;
// otherwise, the default action is to send the message.
            if (!event.isShiftPressed()) {
if (isPreparedForSending()) {
confirmSendMessageIfNeeded();
}







