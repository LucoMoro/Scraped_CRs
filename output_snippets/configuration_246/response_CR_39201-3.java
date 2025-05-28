//<Beginning of snippet n. 0>
boolean showingAttachment = mAttachmentEditor.update(mWorkingMessage);
mAttachmentEditorScrollView.setVisibility(showingAttachment ? View.VISIBLE : View.GONE);
showSubjectEditor(showSubjectEditor || mWorkingMessage.hasSubject());
runOnUiThread(new Runnable() {
    @Override
    public void run() {
        invalidateOptionsMenu();  // Trigger menu recreation
        notifyCMA();  // Notify CMA after top panel rendering
    }
});
//==========================================================
//<End of snippet n. 0>