//<Beginning of snippet n. 0>
boolean showingAttachment = false;

runOnUiThread(new Runnable() {
    @Override
    public void run() {
        try {
            boolean updated = mAttachmentEditor.update(mWorkingMessage);
            showingAttachment = updated;
            mAttachmentEditorScrollView.setVisibility(showingAttachment ? View.VISIBLE : View.GONE);
            showSubjectEditor(showSubjectEditor || mWorkingMessage.hasSubject());
            recreateOptionsMenu(); // Moved inside run method
        } catch (Exception e) {
            Log.e("AttachmentEditor", "Error updating the attachment editor", e);
            Toast.makeText(context, "Failed to update attachment, please try again.", Toast.LENGTH_SHORT).show();
        }
    }
});
//<End of snippet n. 0>