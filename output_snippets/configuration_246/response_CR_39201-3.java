//<Beginning of snippet n. 0>
boolean showingAttachment = mAttachmentEditor.update(mWorkingMessage);
mAttachmentEditorScrollView.setVisibility(showingAttachment ? View.VISIBLE : View.GONE);
showSubjectEditor(showSubjectEditor || mWorkingMessage.hasSubject());

mAttachmentEditor.setOnTopPanelRenderedListener(new TopPanelRenderedListener() {
    @Override
    public void onTopPanelRendered() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();  // Trigger menu recreation
                notifyCMA();  // Notify CMA after invalidating options menu
            }
        });
    }
});
//==========================================================
//<End of snippet n. 0>