/*Mms: Add enclosure icon should disappear when one has been added.

TestCase: 1> add a picture and back to see the
             ComposeMessageActivty UI
          2> add a audio and back to see the
             ComposeMessageActivty UI
          3> add a slishow and check the
             ComposeMessageActivty UI
          4> load a draft and check the UI
          5> remove the attachment and check the UI

When the message has attachment, we have to hide the actionbar add
attachment menu.

Change-Id:I7139dbb3d0016a98ab204ad6e8a44e4fff2663c6Author: zhang, ji <ji.zhang@borqs.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 27943*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 72df986..9a0d71f 100644

//Synthetic comment -- @@ -309,6 +309,8 @@
*/
private boolean mIsRunning;

    private MenuItem mAddAttachmentMenu;

@SuppressWarnings("unused")
public static void log(String logMsg) {
Thread current = Thread.currentThread();
//Synthetic comment -- @@ -2375,6 +2377,11 @@
runOnUiThread(new Runnable() {
@Override
public void run() {
                if (mWorkingMessage.hasAttachment()) {
                    mAddAttachmentMenu.setVisible(false);
                } else {
                    mAddAttachmentMenu.setVisible(true);
                }
drawBottomPanel();
updateSendButtonState();
drawTopPanel(isSubjectEditorVisible());
//Synthetic comment -- @@ -2517,10 +2524,15 @@
menu.add(0, MENU_ADD_SUBJECT, 0, R.string.add_subject).setIcon(
R.drawable.ic_menu_edit);
}
            mAddAttachmentMenu = menu.add(0, MENU_ADD_ATTACHMENT, 0, R.string.add_attachment)
.setIcon(R.drawable.ic_menu_attachment)
                .setTitle(R.string.add_attachment);
            mAddAttachmentMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);    // add to actionbar
            if (mWorkingMessage.hasAttachment()) {
                mAddAttachmentMenu.setVisible(false);
            } else {
                mAddAttachmentMenu.setVisible(true);
            }
}

if (isPreparedForSending()) {







