//<Beginning of snippet n. 1>
@Override
public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    // Logic to recreate the options menu goes here
    return true;
}

private void loadDraft() {
    // Load the draft in a non-UI thread
    new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
            // Load the draft data
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Notify Configuration Manager (CMA) after top panel is drawn
            mAttachmentEditorScrollView.setVisibility(showingAttachment ? View.VISIBLE : View.GONE);
            notifyCMA();
            invalidateOptionsMenu(); // Trigger a recreation of the options menu
        }
    }.execute();
}

private void notifyCMA() {
    // Notify the Configuration Manager that options menu needs to be recreated
}

boolean showingAttachment = mAttachmentEditor.update(mWorkingMessage);
showSubjectEditor(showSubjectEditor || mWorkingMessage.hasSubject());
//==========================================================

//<End of snippet n. 1>