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
        } catch (Exception e) {
            // User notification and logging of the error
            Log.e("AttachmentEditor", "Error updating the attachment editor", e);
            Toast.makeText(context, "Failed to update attachment, please try again.", Toast.LENGTH_SHORT).show();
        }
    }
});

// Check for completion before recreating options menu
if (showingAttachment) {
    recreateOptionsMenu();
}

//==========================================================
//<End of snippet n. 0>