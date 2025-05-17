//<Beginning of snippet n. 0>
boolean showingAttachment = mAttachmentEditor.update(mWorkingMessage);
mAttachmentEditorScrollView.setVisibility(showingAttachment ? View.VISIBLE : View.GONE);
showSubjectEditor(showSubjectEditor || mWorkingMessage.hasSubject());
runOnUiThread(() -> {
    invalidateOptionsMenu(); // Notify that options menu should be recreated
    notifyCMA(); // Mandatory notification to CMA about menu updates
});
@Override
public boolean onPrepareOptionsMenu(Menu menu) {
    // Logic to recreate the options menu
    return super.onPrepareOptionsMenu(menu);
}
//==========================================================
//<End of snippet n. 0>